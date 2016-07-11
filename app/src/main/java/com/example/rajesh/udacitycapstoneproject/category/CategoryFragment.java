package com.example.rajesh.udacitycapstoneproject.category;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.rajesh.udacitycapstoneproject.R;
import com.example.rajesh.udacitycapstoneproject.base.frament.BaseFragment;
import com.example.rajesh.udacitycapstoneproject.realm.ExpenseCategories;

import butterknife.Bind;
import io.realm.Realm;
import io.realm.RealmResults;

public class CategoryFragment extends BaseFragment {


    @Bind(R.id.rv_category)
    RecyclerView rvCategory;

    CategoryAdapter categoryAdapter;
    Realm mRealm;


    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRealm = Realm.getDefaultInstance();
        setRecyclerViewAdapter();

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_category;
    }

    private void setRecyclerViewAdapter() {

        RealmResults<ExpenseCategories> expenseCategories = mRealm.where(ExpenseCategories.class).findAll();
        rvCategory.setLayoutManager(new LinearLayoutManager(getActivity()));
        categoryAdapter = new CategoryAdapter(getActivity(), expenseCategories);
        rvCategory.setAdapter(categoryAdapter);

        ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                deleteItem(categoryAdapter.getCategoryIdByPosition(viewHolder.getAdapterPosition()));
                categoryAdapter.deleteItem(viewHolder.getAdapterPosition());
                categoryAdapter.notifyItemRangeRemoved(viewHolder.getAdapterPosition(), categoryAdapter.getItemCount());
                categoryAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                categoryAdapter.notifyDataSetChanged();
            }
        });
        swipeToDismissTouchHelper.attachToRecyclerView(rvCategory);
    }

    private void deleteItem(final int id) {
        mRealm.beginTransaction();
        ExpenseCategories expenseCategories = mRealm.where(ExpenseCategories.class).equalTo("id", id).findFirst();
        expenseCategories.deleteFromRealm();
        mRealm.commitTransaction();
    }

}
