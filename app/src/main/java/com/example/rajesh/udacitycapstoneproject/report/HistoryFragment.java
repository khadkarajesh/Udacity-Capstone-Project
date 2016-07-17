package com.example.rajesh.udacitycapstoneproject.report;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.rajesh.udacitycapstoneproject.R;
import com.example.rajesh.udacitycapstoneproject.base.frament.BaseFragment;
import com.example.rajesh.udacitycapstoneproject.expense.recurring.RecurringExpenseAdapter;
import com.example.rajesh.udacitycapstoneproject.realm.Expense;
import com.example.rajesh.udacitycapstoneproject.realm.table.RealmTable;

import butterknife.Bind;
import io.realm.Realm;
import io.realm.RealmResults;


public class HistoryFragment extends BaseFragment {
    RecurringExpenseAdapter expenseAdapter;

    @Bind(R.id.rv_dashboard)
    RecyclerView rvDashBoard;

    private Realm mRealm;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRealm = Realm.getDefaultInstance();
        setRecyclerViewAdapter();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_history;
    }

    private void setRecyclerViewAdapter() {
        RealmResults<Expense> expenses = mRealm.where(Expense.class).findAllSorted(RealmTable.DATE);
        rvDashBoard.setLayoutManager(new LinearLayoutManager(getActivity()));
        expenseAdapter = new RecurringExpenseAdapter(getActivity(), expenses);
        rvDashBoard.setAdapter(expenseAdapter);

        ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                deleteItem((int) expenseAdapter.getExpenseIdByPosition(viewHolder.getAdapterPosition()));
                expenseAdapter.notifyItemRangeRemoved(viewHolder.getAdapterPosition(), expenseAdapter.getItemCount());
                expenseAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                expenseAdapter.notifyDataSetChanged();
            }
        });
        swipeToDismissTouchHelper.attachToRecyclerView(rvDashBoard);
    }

    private void deleteItem(final int id) {
        mRealm.beginTransaction();
        Expense expense = mRealm.where(Expense.class).equalTo(RealmTable.ID, id).findFirst();
        expense.deleteFromRealm();
        mRealm.commitTransaction();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }
}
