package com.xue.support.view;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xue.support.R;

import java.util.ArrayList;

public class DatePicker {

    private AppCompatDialog mDialog;

    private WheelView mYearWheelView;

    private WheelView mMonthWheelView;

    private TextView mConfirmTextView;

    private ArrayList<String> years;

    private ArrayList<String> months;

    private SelectedCallback callback;

    public DatePicker(Context context, int startYear, int endYear, boolean since) {
        mDialog = new AppCompatDialog(context);
        mDialog.setTitle("选择时间");
        mDialog.setContentView(R.layout.date_picker);

        findView();
        init(startYear, endYear, since);
    }

    public void setCallback(SelectedCallback callback) {
        this.callback = callback;
    }

    private void findView() {
        mYearWheelView = mDialog.findViewById(R.id.yearWheelView);
        mMonthWheelView = mDialog.findViewById(R.id.monthWheelView);
        mConfirmTextView = mDialog.findViewById(R.id.confirm);

        mConfirmTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if (callback != null) {
                    callback.onSelected(getYear(), getMonth());
                }
            }
        });
    }

    private void init(final int startYear, final int endYear, final boolean since) {

        years = new ArrayList();
        for (int i = 0; i < endYear - startYear; i++) {
            years.add(String.valueOf(startYear + i));
        }
        if (since) {
            years.add("至今");
        }

        months = new ArrayList();
        for (int i = 1; i <= 12; i++) {
            months.add(String.valueOf(i));
        }

        mYearWheelView.setOffset(1);
        mYearWheelView.setSeletion((endYear - startYear) / 2);
        mYearWheelView.setItems(years);
        mYearWheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                if (TextUtils.equals("至今", item)) {
                    mMonthWheelView.setVisibility(View.INVISIBLE);
                } else {
                    mMonthWheelView.setVisibility(View.VISIBLE);
                }
            }
        });


        mMonthWheelView.setOffset(1);
        mMonthWheelView.setItems(months);
    }

    public String getYear() {
        return mYearWheelView.getSeletedItem();
    }

    public String getMonth() {
        if (TextUtils.equals("至今", getYear())) {
            return null;
        }

        return mMonthWheelView.getSeletedItem();
    }

    public void show() {
        mDialog.show();
    }

    public interface SelectedCallback {
        public void onSelected(String year, String month);
    }
}
