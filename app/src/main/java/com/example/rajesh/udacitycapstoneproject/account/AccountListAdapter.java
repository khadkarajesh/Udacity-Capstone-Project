package com.example.rajesh.udacitycapstoneproject.account;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rajesh.udacitycapstoneproject.R;
import com.example.rajesh.udacitycapstoneproject.realm.Account;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmResults;

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.AccountListHolder> {

    RealmResults<Account> mAccounts;
    Context mContext;

    public AccountListAdapter(Context context, RealmResults<Account> accounts) {
        this.mContext = context;
        this.mAccounts = accounts;
    }

    @Override
    public AccountListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_list_layout, parent, false);
        return new AccountListHolder(view);
    }

    @Override
    public void onBindViewHolder(AccountListHolder holder, final int position) {
        holder.tvAccountName.setText(mAccounts.get(position).getTitle());
        holder.tvAccountPrice.setText("" + mAccounts.get(position).getAccountAmount());

        holder.llAccountContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mContext.startActivity(AccountActivity.getLaunchIntent(mContext, String.valueOf(mAccounts.get(position).getId())));
                return false;
            }
        });
    }

    public int getAccountIdByPosition(int position) {
        return (int) mAccounts.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return mAccounts.size();
    }

    public static class AccountListHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_account_name)
        TextView tvAccountName;

        @Bind(R.id.tv_account_price)
        TextView tvAccountPrice;

        @Bind(R.id.ll_account_container)
        LinearLayout llAccountContainer;

        public AccountListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
