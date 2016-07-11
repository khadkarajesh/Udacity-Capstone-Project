package com.example.rajesh.udacitycapstoneproject.expense.recurring;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.rajesh.udacitycapstoneproject.Constant;
import com.example.rajesh.udacitycapstoneproject.R;
import com.example.rajesh.udacitycapstoneproject.base.frament.BaseFragment;
import com.example.rajesh.udacitycapstoneproject.realm.Expense;
import com.example.rajesh.udacitycapstoneproject.realm.table.RealmTable;

import butterknife.Bind;
import io.realm.Realm;
import io.realm.RealmResults;


public class RecurringFragment extends BaseFragment {

    RecurringExpenseAdapter expenseAdapter;

    @Bind(R.id.rv_recurring_expense)
    RecyclerView rvDashBoard;

    Realm mRealm;

    public RecurringFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRealm = Realm.getDefaultInstance();
        setRecyclerViewAdapter();
    }

    private void setRecyclerViewAdapter() {
        RealmResults<Expense> expenses = mRealm.where(Expense.class).equalTo(RealmTable.TYPE, Constant.RECURRING_TYPE).findAll();
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
                deleteItem(expenseAdapter.getExpenseIdByPosition(viewHolder.getAdapterPosition()));
                expenseAdapter.notifyItemRangeRemoved(viewHolder.getAdapterPosition(), expenseAdapter.getItemCount());
                expenseAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                expenseAdapter.notifyDataSetChanged();
            }
        });
        swipeToDismissTouchHelper.attachToRecyclerView(rvDashBoard);
    }

    private void deleteItem(final long id) {
        mRealm.beginTransaction();
        Expense expense = mRealm.where(Expense.class).equalTo(RealmTable.ID, id).findFirst();
        expense.deleteFromRealm();
        mRealm.commitTransaction();
    }


    @Override
    protected int getLayout() {
        return R.layout.fragment_reccuring;
    }
}
