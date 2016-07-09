package com.example.rajesh.udacitycapstoneproject.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ExpenseCategories extends RealmObject {
    @PrimaryKey private int id;
    private String categoriesName;
    private String categoriesColor;

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
}
