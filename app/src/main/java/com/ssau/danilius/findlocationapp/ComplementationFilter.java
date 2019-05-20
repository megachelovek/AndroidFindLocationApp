package com.ssau.danilius.findlocationapp;

import java.util.ArrayList;
import java.util.List;

public class ComplementationFilter {
    private List<float[]> dataToFilter;
    private float dt;

    public ComplementationFilter(List<float[]> data, float dt){
        this.dataToFilter = data;
        this.dt = dt;
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

    public float[] FilterData(float[] data) {
    return null;
    }
}
