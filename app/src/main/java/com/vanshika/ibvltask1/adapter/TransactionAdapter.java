package com.vanshika.ibvltask1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vanshika.ibvltask1.R;
import com.vanshika.ibvltask1.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> implements Filterable {

    private List<Transaction> transactionList = new ArrayList<>();
    private List<Transaction> filteredList = new ArrayList<>();
    private final LayoutInflater inflater;

    public TransactionAdapter(List<Transaction> data, Context context) {
        this.transactionList = data;
        this.filteredList = new ArrayList<>(data);
        this.inflater = LayoutInflater.from(context);
    }

    public void setData(List<Transaction> data) {
        this.transactionList = data;
        this.filteredList = new ArrayList<>(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = filteredList.get(position);
        holder.amount.setText(String.valueOf(transaction.getAmount()));
        holder.date.setText(transaction.getDate());
        holder.description.setText(transaction.getDescription());
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Transaction> filtered = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filtered = new ArrayList<>(transactionList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Transaction item : transactionList) {
                        if (item.getDescription().toLowerCase().contains(filterPattern)) {
                            filtered.add(item);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filtered;
                return results;
            }

            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (List<Transaction>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView amount, date, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.tvAmount);
            date = itemView.findViewById(R.id.tvDate);
            description = itemView.findViewById(R.id.tvDescription);
        }
    }
}
