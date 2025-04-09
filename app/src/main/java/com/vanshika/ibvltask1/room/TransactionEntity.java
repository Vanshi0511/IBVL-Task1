package com.vanshika.ibvltask1.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "transactions")
public class TransactionEntity {
    @PrimaryKey
    @NonNull
    public String id;

    public String date;
    public String amount;
    public String description;

    public TransactionEntity(@NonNull String id, String date, String amount, String description) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.description = description;
    }
}
