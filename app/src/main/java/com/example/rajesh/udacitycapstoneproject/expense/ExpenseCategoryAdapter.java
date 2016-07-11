package com.example.rajesh.udacitycapstoneproject.expense;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rajesh.udacitycapstoneproject.R;
import com.example.rajesh.udacitycapstoneproject.realm.ExpenseCategories;
import com.example.rajesh.udacitycapstoneproject.widget.CircularView;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmResults;

public class ExpenseCategoryAdapter extends BaseAdapter {
    RealmResults<ExpenseCategories> categories;
    Context mContext;

    public ExpenseCategoryAdapter(Context context, RealmResults<ExpenseCategories> data) {
        this.categories = data;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getExpenseCategoryId(int position) {
        return categories.get(position).getId();
    }

    public int getCategoryPositionById(int id) {
        int index = -1;
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId() == id) {
                index = i;
            }
        }
        return index;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ExpenseCategoryHolder holder;
        View view = convertView;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.single_catgories_layout, null);
            holder = new ExpenseCategoryHolder(view);
            view.setTag(holder);
        } else {
            holder = (ExpenseCategoryHolder) view.getTag();
        }
        holder.tvCategoriesName.setText(categories.get(position).getCategoriesName());
        holder.ivCategoriesIndicator.setFillColor(categories.get(position).getCategoriesColor());
        return view;
    }

    public static class ExpenseCategoryHolder {
        @Bind(R.id.iv_categories_indicator)
        CircularView ivCategoriesIndicator;

        @Bind(R.id.tv_categories_name)
        TextView tvCategoriesName;

        public ExpenseCategoryHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
