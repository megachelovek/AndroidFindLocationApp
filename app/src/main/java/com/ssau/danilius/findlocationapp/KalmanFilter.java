package com.ssau.danilius.findlocationapp;

import org.apache.commons.math3.filter.DefaultProcessModel;
import org.apache.commons.math3.filter.ProcessModel;

import java.util.ArrayList;
import java.util.List;

public class KalmanFilter {
    private List<float[]> dataToFilter = new ArrayList<float[]>();
    private static final float Q = (float) Math.pow(10, 5); //Дисперсия процесса
    private static final float R = (float) Math.pow(10, 6) * 2; // Погрешность процесса - менять в основном это

    public KalmanFilter(List<float[]> rawData) {
        dataToFilter = rawData;
    }

    public List<float[]> DoFiltering() {
        List<float[]> result = new ArrayList<float[]>();
        float[] temp;
        for (int i = 0; i < dataToFilter.get(0).length; i++) {
            temp = getOneArray(i);
            temp = FilterData(temp);
            this.pushOneArray(temp,i);
        }
        return  dataToFilter;
    }

    private float[] getOneArray( int i) {
        float[] result =new float[dataToFilter.size()];
        for (int k=0;k<dataToFilter.size();k++) {
            result[k] = dataToFilter.get(k)[i];
        }
        return result;
    }
    private void  pushOneArray(float[] temp, int i) {
        for (int k =0; k<dataToFilter.size();k++) {
            dataToFilter.get(k)[i] = temp[k];
        }
    }

    float[] FilterData(float[] rawSignal) {
        float[] xhatminus = new float[dataToFilter.size()];
        float[] xhat = new float[dataToFilter.size()];
        float[] P = new float[dataToFilter.size()];
        float[] Pminus = new float[dataToFilter.size()];
        float[] K = new float[dataToFilter.size()];

        for (int k = 1; k < dataToFilter.size(); k++) {
            xhatminus[k] = xhat[k - 1];
            Pminus[k] = P[k - 1] + Q;
            K[k] = Pminus[k] / (Pminus[k] + R);
            xhat[k] = xhatminus[k] + K[k] * (rawSignal[k] - xhatminus[k]);
            P[k] = (1 - K[k]) * Pminus[k];
        }
        return rawSignal;
    }
}