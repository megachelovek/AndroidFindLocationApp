package com.ssau.danilius.findlocationapp;

import java.util.ArrayList;
import java.util.List;

public class SimpleFilter { //ИСПОЛЬЗУЮ ФИЛЬТР КАЛМАНА
    public static List<float[]> FilterListSignal(List<float[]> inputData, float alfa){
        float[] newArrayX = new float[inputData.size()];
        float[] newArrayY = new float[inputData.size()];
        float[] newArrayZ = new float[inputData.size()];
        for (int i=0;i<inputData.size();i++) {
            newArrayX[i] = inputData.get(i)[0];
            newArrayY[i] = inputData.get(i)[1];
            newArrayZ[i] = inputData.get(i)[2];
        }
        List<float[]> result = new ArrayList<float[]>(){};
        newArrayX = LowFilterArray(newArrayX, alfa);
        newArrayY = LowFilterArray(newArrayY, alfa);
        newArrayZ = LowFilterArray(newArrayZ, alfa);
        for (int i=0;i<inputData.size();i++) {
            result.add(new float[]{newArrayX[i],newArrayY[i],newArrayZ[i]});
        }
        return result;
    }

    public static float[] LowFilterArray(float[] inputArray, float alfa){
        float[] filterArray = new float[inputArray.length];
        filterArray[0] = inputArray[0];
        for (int i=1; i< inputArray.length;i++){
            filterArray[i]= filterArray[i-1]+alfa*(inputArray[i]-filterArray[i-1]);
        }
        return filterArray;
    }
}