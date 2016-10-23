package com.hackharvard.smartmusicshuffle.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.hackharvard.smartmusicshuffle.R;
import com.hackharvard.smartmusicshuffle.activity.HomeActivity;
import com.microsoft.projectoxford.emotion.contract.Scores;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SmileFragment extends Fragment {

    private OnSmileFragmentInteractionListener mListener;

    @BindView(R.id.chart)
    RadarChart radarChart;

    public SmileFragment() {
        // Required empty public constructor
    }

    public static SmileFragment newInstance() {
        SmileFragment fragment = new SmileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_smile, container, false);

        ButterKnife.bind(this, view);

        setData();

        setupRadarChart();

        return view;
    }

    private void setupRadarChart() {
        radarChart.setBackgroundColor(Color.rgb(60, 65, 82));

        radarChart.getDescription().setEnabled(false);

        radarChart.setWebLineWidth(1f);
        radarChart.setWebColor(Color.LTGRAY);
        radarChart.setWebLineWidthInner(1f);
        radarChart.setWebColorInner(Color.LTGRAY);
        radarChart.setWebAlpha(100);

        radarChart.animateXY(
                1400, 1400,
                Easing.EasingOption.EaseInOutQuad,
                Easing.EasingOption.EaseInOutQuad);

        XAxis xAxis = radarChart.getXAxis();
        xAxis.setTextSize(9f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private String[] mActivities = new String[]{"Happiness", "Contempt", "Fear", "Disgust", "Anger", "Sadness", "Neutral", "Surprise"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mActivities[(int) value % mActivities.length];
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });
        xAxis.setTextColor(Color.WHITE);

        YAxis yAxis = radarChart.getYAxis();
        yAxis.setLabelCount(8, false);
        yAxis.setTextSize(9f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(100f);
        yAxis.setDrawLabels(false);

        Legend legend = radarChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setXEntrySpace(7f);
        legend.setYEntrySpace(5f);
        legend.setTextColor(Color.WHITE);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSmileFragmentInteractionListener) {
            mListener = (OnSmileFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnSmileFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void setData() {

        float mult = 80;
        float min = 20;
        int cnt = 8;

        ArrayList<RadarEntry> entries1 = new ArrayList<RadarEntry>();

        Scores scores = ((HomeActivity) getActivity()).scores;

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < cnt; i++) {

            Double value = Math.random();
            if (scores != null) {
                if (i == 0) {
                    value = scores.happiness;
                } else if (i == 2) {
                    value = scores.contempt;
                } else if (i == 3) {
                    value = scores.fear;
                } else if (i == 4) {
                    value = scores.disgust;
                } else if (i == 5) {
                    value = scores.anger;
                } else if (i == 6) {
                    value = scores.sadness;
                } else if (i == 7) {
                    value = scores.neutral;
                } else if (i == 8) {
                    value = scores.surprise;
                }
            }

            float val1 = (float) (value * mult) + min;
            entries1.add(new RadarEntry(val1));
        }

        RadarDataSet set1 = new RadarDataSet(entries1, "Last Photo Recognition Scores");
        set1.setColor(Color.rgb(103, 110, 129));
        set1.setFillColor(Color.rgb(103, 110, 129));
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(2f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);

        ArrayList<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();
        sets.add(set1);

        RadarData data = new RadarData(sets);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.WHITE);

        radarChart.setData(data);
        radarChart.invalidate();
    }
}
