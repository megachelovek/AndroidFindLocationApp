package com.ssau.danilius.findlocationapp;

import java.util.ArrayList;
import java.util.List;

public class AlphaBetaFilter {
    private List<float[]> dataToFilter;
    private float dt;

    public AlphaBetaFilter(List<float[]> data, float dt){
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
        float xk_1 = (float)0;
        float vk_1 =(float) 0;
        float a = (float)0.85;
        float b = (float) 0.005;
        float xk, vk, rk;
        float[] result = new float[data.length];
        for(int i=0; i<data.length;i++)
        {
            xk = xk_1 + (vk_1 * dt);
            vk = vk_1;

            rk = data[i] - xk;

            xk += a * rk;
            vk += (b * rk) / dt;

            xk_1 = xk;
            vk_1 = vk;

            result[i]= xk_1;
        }
        return result;
    }
}
