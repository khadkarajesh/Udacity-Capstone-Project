package com.example.rajesh.udacitycapstoneproject.realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ExpenseCategories extends RealmObject {
    @PrimaryKey
    private int id;
    private String categoriesName;
    private String categoriesColor;
    private RealmList<Expense> expenses;

    public String getCategoriesColor() {
        return categoriesColor;
    }

    public void setCategoriesColor(String categoriesColor) {
        this.categoriesColor = categoriesColor;
    }

    public String getCategoriesName() {
        return categoriesName;
    }

    public void setCategoriesName(String categoriesName) {
        this.categoriesName = categoriesName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RealmList<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(RealmList<Expense> expenses) {
        this.expenses = expenses;
    }
}
