package com.example.rajesh.udacitycapstoneproject.account;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.rajesh.udacitycapstoneproject.R;
import com.example.rajesh.udacitycapstoneproject.base.frament.BaseFragment;
import com.example.rajesh.udacitycapstoneproject.realm.Account;
import com.example.rajesh.udacitycapstoneproject.realm.table.RealmTable;

import butterknife.Bind;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;


public class AccountFragment extends BaseFragment {

    @Bind(R.id.rv_account_list)
    RecyclerView rvAccount;

    @Bind(R.id.rl_no_account)
    RelativeLayout rlNoAccount;

    AccountListAdapter accountListAdapter;
    private Realm mRealm;
    private RealmResults<Account> accounts;


    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRealm = Realm.getDefaultInstance();
        accounts = mRealm.where(Account.class).findAll();
        if (accounts.size() > 0) {
            setRecyclerView();
        } else {
            rvAccount.setVisibility(View.GONE);
            rlNoAccount.setVisibility(View.VISIBLE);
        }
    }

    private void setRecyclerView() {
        rvAccount.setLayoutManager(new LinearLayoutManager(getActivity()));
        accountListAdapter = new AccountListAdapter(getActivity(), accounts);
        rvAccount.setAdapter(accountListAdapter);

        ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                deleteItem(accountListAdapter.getAccountIdByPosition(viewHolder.getAdapterPosition()));
                accountListAdapter.notifyItemRangeRemoved(viewHolder.getAdapterPosition(), accountListAdapter.getItemCount());
                accountListAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                accountListAdapter.notifyDataSetChanged();
            }
        });
        swipeToDismissTouchHelper.attachToRecyclerView(rvAccount);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_account;
    }

    private void deleteItem(final int id) {
        mRealm.beginTransaction();
        Account account = mRealm.where(Account.class).equalTo(RealmTable.ID, id).findFirst();
        account.deleteFromRealm();
        mRealm.commitTransaction();
    }

    @OnClick(R.id.iv_no_account)
    public void onClick() {
        getActivity().startActivity(AccountActivity.getLaunchIntent(getActivity(), null));
    }
}
