package com.example.rajesh.udacitycapstoneproject.realm;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Account extends RealmObject {
    @PrimaryKey private long id;
    private String title;
    private Date date;
    private Double amount;
    private String type;

    public Double getAccountAmount() {
        return amount;
    }

    public void setAccountAmount(Double accountAmount) {
        this.amount = accountAmount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAccountType() {
        return type;
    }

    public void setAccountType(String accountType) {
        this.type = accountType;
    }

    public Date getDateCreated() {
        return date;
    }

    public void setDateCreated(Date dateCreated) {
        this.date = dateCreated;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
