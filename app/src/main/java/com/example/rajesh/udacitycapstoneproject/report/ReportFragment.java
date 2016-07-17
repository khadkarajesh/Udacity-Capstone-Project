package com.example.rajesh.udacitycapstoneproject.report;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.rajesh.udacitycapstoneproject.R;
import com.example.rajesh.udacitycapstoneproject.base.frament.BaseFragment;
import com.example.rajesh.udacitycapstoneproject.realm.Account;
import com.example.rajesh.udacitycapstoneproject.realm.Expense;
import com.example.rajesh.udacitycapstoneproject.realm.ExpenseCategories;
import com.example.rajesh.udacitycapstoneproject.realm.table.RealmTable;
import com.example.rajesh.udacitycapstoneproject.utils.MonthTimeStamp;
import com.example.rajesh.udacitycapstoneproject.utils.WeekTimeStamp;
import com.example.rajesh.udacitycapstoneproject.utils.YearTimeStamp;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import io.realm.Realm;
import io.realm.RealmResults;
import timber.log.Timber;


public class ReportFragment extends BaseFragment implements OnChartValueSelectedListener {

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    public enum ReportType {
        REPORT_BY_WEEK, REPORT_BY_MONTH, REPORT_BY_YEAR
    }

    @Bind(R.id.chart)
    PieChart pieChart;

    @Bind(R.id.spinner)
    Spinner spinner;

    Double mTotalAmount = 0.0;

    ReportType reportType = null;

    private Typeface tf;
    private Realm mRealm = null;
    private RealmResults<ExpenseCategories> categories;
    private ArrayList<Integer> colors = new ArrayList<>();
    private Date startDate = null, endDate = null, currentDate = new Date();

    public ReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRealm = Realm.getDefaultInstance();

        categories = mRealm.where(ExpenseCategories.class).findAll();
        reportType = ReportType.REPORT_BY_WEEK;
        populateSpinner();
        setPieChart();
    }


    private void populateSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.sort_by, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        reportType = ReportType.REPORT_BY_WEEK;
                        break;
                    case 1:
                        reportType = ReportType.REPORT_BY_MONTH;
                        break;
                    case 2:
                        reportType = ReportType.REPORT_BY_YEAR;
                        break;
                }
                setPieChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setPieChart() {
        pieChart.setUsePercentValues(true);
        pieChart.setDescription("");
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");

        pieChart.setCenterTextTypeface(Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf"));
        pieChart.setCenterText(generateCenterSpannableText());

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);

        pieChart.setDrawCenterText(true);

        pieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        pieChart.setOnChartValueSelectedListener(this);

        //setData(3, 100);
        setData();

        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = pieChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_report;
    }

    private ArrayList<String> getCategories() {
        ArrayList<String> categoryTitle = new ArrayList<>();
        for (ExpenseCategories expenseCategory : categories) {
            categoryTitle.add(expenseCategory.getCategoriesName());
        }
        return categoryTitle;
    }

    private ArrayList<Entry> getExpenses() {
        ArrayList<Entry> expenses = new ArrayList<>();
        setDateByReport();
        mTotalAmount = (Double) mRealm.where(Account.class)
                .greaterThanOrEqualTo(RealmTable.DATE, startDate)
                .lessThanOrEqualTo(RealmTable.DATE, endDate).sum(RealmTable.AMOUNT);
        for (int i = 0; i < categories.size(); i++) {
            double maxValue = (double) mRealm.where(Expense.class).equalTo("expenseCategories.id", categories.get(i).getId())
                    .findAll()
                    .where()
                    .greaterThanOrEqualTo(RealmTable.DATE, startDate)
                    .lessThanOrEqualTo(RealmTable.DATE, endDate)
                    .findAll()
                    .sum(RealmTable.AMOUNT);

            Timber.d("max value found %s category %s", maxValue, categories.get(i).getCategoriesName());
            if (maxValue > 0) {
                expenses.add(new Entry((float) maxValue, i));
                colors.add(Color.parseColor(categories.get(i).getCategoriesColor()));
            }
        }

        return expenses;
    }

    private void setDateByReport() {
        if (reportType == ReportType.REPORT_BY_MONTH) {
            startDate = MonthTimeStamp.getStartTimeStamp(currentDate);
            endDate = MonthTimeStamp.getEndTimeStamp(currentDate);
        } else if (reportType == ReportType.REPORT_BY_WEEK) {
            startDate = WeekTimeStamp.getStartDate(currentDate);
            endDate = WeekTimeStamp.getEndDate(currentDate);
        } else if (reportType == ReportType.REPORT_BY_YEAR) {
            startDate = YearTimeStamp.getStartDate(currentDate);
            endDate = YearTimeStamp.getEndDate(currentDate);
        }
    }

    private void setData() {
        PieDataSet dataSet = new PieDataSet(getExpenses(), getActivity().getString(R.string.categories));
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        dataSet.setColors(getColors());

        PieData data = new PieData(getCategories(), dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(Typeface.DEFAULT);
        pieChart.setData(data);

        // undo all highlights
        pieChart.highlightValues(null);

        pieChart.invalidate();
    }

    private ArrayList<Integer> getColors() {
        return colors;
    }


    private SpannableString generateCenterSpannableText() {
        SpannableString s = new SpannableString(getActivity().getString(R.string.total_amount) + mTotalAmount);
        return s;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }
}
