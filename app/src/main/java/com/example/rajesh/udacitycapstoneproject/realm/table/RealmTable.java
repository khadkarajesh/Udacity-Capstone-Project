package com.example.rajesh.udacitycapstoneproject.realm.table;


public interface RealmTable {
    String ID = "id";
    String DATE = "date";
    String TITLE = "title";
    String TYPE = "type";
    String AMOUNT = "amount";

    interface Expense {
        String DESCRIPTION = "expenseDescription";
    }

    interface Category {
        String NAME = "categoriesName";
        String COLOR = "categoriesColor";
    }
}
