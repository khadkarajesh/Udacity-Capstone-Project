package com.example.rajesh.udacitycapstoneproject.realm;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Expense extends RealmObject {
    @PrimaryKey
    private long id;
    private Date date;
    private String title;
    private Double amount;
    private String description;
    private String type;
    private ExpenseCategories expenseCategories;

    public Double getExpenseAmount() {
        return amount;
    }

    public void setExpenseAmount(Double expenseAmount) {
        this.amount = expenseAmount;
    }

    public Date getExpenseDate() {
        return date;
    }

    public void setExpenseDate(Date expenseDate) {
        this.date = expenseDate;
    }

    public String getExpenseDescription() {
        return description;
    }

    public void setExpenseDescription(String expenseDescription) {
        this.description = expenseDescription;
    }

    public String getExpenseTitle() {
        return title;
    }

    public void setExpenseTitle(String expenseTitle) {
        this.title = expenseTitle;
    }

    public String getExpenseType() {
        return type;
    }

    public void setExpenseType(String expenseType) {
        this.type = expenseType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ExpenseCategories getExpenseCategories() {
        return expenseCategories;
    }

    public void setExpenseCategories(ExpenseCategories expenseCategories) {
        this.expenseCategories = expenseCategories;
    }
}
