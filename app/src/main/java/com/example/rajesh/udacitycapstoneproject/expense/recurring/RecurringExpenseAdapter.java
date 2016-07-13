package com.example.rajesh.udacitycapstoneproject.expense.recurring;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rajesh.udacitycapstoneproject.R;
import com.example.rajesh.udacitycapstoneproject.expense.ExpenseActivity;
import com.example.rajesh.udacitycapstoneproject.realm.Expense;
import com.example.rajesh.udacitycapstoneproject.widget.CircularView;

import io.realm.RealmResults;

public class RecurringExpenseAdapter extends RecyclerView.Adapter<RecurringExpenseAdapter.ExpenseHolder> {

    RealmResults<Expense> expense;
    Context context;

    public RecurringExpenseAdapter(Context context, RealmResults<Expense> expenses) {
        this.context = context;
        this.expense = expenses;
    }

    @Override
    public ExpenseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_expense_item_layout, parent, false);
        return new ExpenseHolder(view);
    }

    @Override
    public void onBindViewHolder(ExpenseHolder holder, final int position) {
        holder.expenseTitle.setText(expense.get(position).getExpenseTitle());
        holder.tvPrice.setText("" + expense.get(position).getExpenseAmount());
        holder.ivCategoriesIndicator.setFillColor(expense.get(position).getExpenseCategories().getCategoriesColor());

        holder.llDashboardContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                context.startActivity(ExpenseActivity.getLaunchIntent(context, String.valueOf(expense.get(position).getId())));
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return expense.size();
    }

    public long getExpenseIdByPosition(int position) {
        return expense.get(position).getId();
    }

    public static class ExpenseHolder extends RecyclerView.ViewHolder {
        TextView expenseTitle, tvPrice;
        CircularView ivCategoriesIndicator;
        LinearLayout llDashboardContainer;

        public ExpenseHolder(View itemView) {
            super(itemView);
            expenseTitle = (TextView) itemView.findViewById(R.id.tv_expense_title);
            ivCategoriesIndicator = (CircularView) itemView.findViewById(R.id.iv_categories_indicator);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            llDashboardContainer = (LinearLayout) itemView.findViewById(R.id.ll_dashboard_container);
        }
    }
}
