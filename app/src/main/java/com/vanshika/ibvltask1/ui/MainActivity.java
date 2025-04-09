package com.vanshika.ibvltask1.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vanshika.ibvltask1.adapter.TransactionAdapter;
import com.vanshika.ibvltask1.db.AppDatabase;
import com.vanshika.ibvltask1.utils.BiometricHelper;
import com.vanshika.ibvltask1.R;
import com.vanshika.ibvltask1.utils.TokenManager;
import com.vanshika.ibvltask1.db.TransactionEntity;
import com.vanshika.ibvltask1.model.Transaction;
import com.vanshika.ibvltask1.api.ApiService;
import com.vanshika.ibvltask1.api.RetrofitClient;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TransactionAdapter adapter;
    TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            tokenManager = new TokenManager(this);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new TransactionAdapter(new ArrayList<>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Biometric Prompt
        BiometricHelper.showBiometricPrompt(this, this::fetchTransactions);

        // Logout button
        findViewById(R.id.logoutButton).setOnClickListener(v -> {
            tokenManager.clearToken();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

    }

    private void fetchTransactions()
    {
        String token =  tokenManager.getToken();
        Log.d("API_TOKEN", "Token used: " + token);  // 🔍 Print token in log

        ApiService api = RetrofitClient.getApiService();

        api.getTransactions(token).enqueue(new Callback<List<Transaction>>() {
            @Override
            public void onResponse(Call<List<Transaction>> call, Response<List<Transaction>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Transaction> transactions = response.body();
                    Log.d("API_SUCCESS", "Transactions fetched: " + transactions.size());

                    adapter.setData(transactions);

                    // Save to Room
                    List<TransactionEntity> entities = new ArrayList<>();
                    for (Transaction t : transactions) {
                        entities.add(new TransactionEntity(t.getId(), t.getDate(), t.getAmount(), t.getDescription()));
                    }

                    new Thread(() -> {
                        AppDatabase db = AppDatabase.getInstance(MainActivity.this);
                        db.transactionDao().deleteAll();
                        db.transactionDao().insertAll(entities);
                        Log.d("ROOM_DB", "Transactions saved to Room DB");
                    }).start();

                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "null";
                        Log.e("API_ERROR", "API failed with code: " + response.code() + "\nError body: " + errorBody);
                    } catch (Exception e) {
                        Log.e("API_ERROR", "Error reading error body", e);
                    }
                    loadFromDb(); // fallback
                }
            }

            @Override
            public void onFailure(Call<List<Transaction>> call, Throwable t) {
                Log.e("API_FAILURE", "API call failed: " + t.getMessage(), t);
                loadFromDb(); // fallback
            }
        });
    }


    private void loadFromDb() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            List<TransactionEntity> dbList = db.transactionDao().getAll();
            List<Transaction> fallbackList = new ArrayList<>();
            for (TransactionEntity e : dbList) {
                fallbackList.add(new Transaction(e.id, e.date, e.amount, e.description));
            }

            runOnUiThread(() -> adapter.setData(fallbackList));
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setQueryHint("Search transactions...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        return true;
    }
}
