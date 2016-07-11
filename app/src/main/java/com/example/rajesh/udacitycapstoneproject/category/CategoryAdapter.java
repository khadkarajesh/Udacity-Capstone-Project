package com.example.rajesh.udacitycapstoneproject.category;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rajesh.udacitycapstoneproject.R;
import com.example.rajesh.udacitycapstoneproject.realm.ExpenseCategories;
import com.example.rajesh.udacitycapstoneproject.widget.CircularView;

import io.realm.RealmResults;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ExpenseHolder> {

    Context context;
    RealmResults<ExpenseCategories> mExpenseCategories;

    public CategoryAdapter(Context context, RealmResults<ExpenseCategories> expenseCategories) {
        this.context = context;
        this.mExpenseCategories = expenseCategories;
    }

    @Override
    public ExpenseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_category_list_layout, parent, false);
        return new ExpenseHolder(view);
    }

    @Override
    public void onBindViewHolder(ExpenseHolder holder, final int position) {
        holder.expenseTitle.setText(mExpenseCategories.get(position).getCategoriesName());
        holder.circularView.setFillColor(mExpenseCategories.get(position).getCategoriesColor());
        holder.llContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                context.startActivity(CategoryEditActivity.getLaunchIntent(context, Integer.toString(mExpenseCategories.get(position).getId())));
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mExpenseCategories.size();
    }


    public void deleteItem(int itemPosition) {
        notifyItemRangeRemoved(itemPosition, mExpenseCategories.size());
        notifyItemRemoved(itemPosition);
        notifyDataSetChanged();
    }

    public int getCategoryIdByPosition(int position) {
        return mExpenseCategories.get(position).getId();
    }


    public static class ExpenseHolder extends RecyclerView.ViewHolder {
        TextView expenseTitle;
        CircularView circularView;
        LinearLayout llContainer;

        public ExpenseHolder(View itemView) {
            super(itemView);
            expenseTitle = (TextView) itemView.findViewById(R.id.tv_categories_title);
            circularView = (CircularView) itemView.findViewById(R.id.iv_categories_indicator);
            llContainer = (LinearLayout) itemView.findViewById(R.id.ll_container);
        }
    }
}
