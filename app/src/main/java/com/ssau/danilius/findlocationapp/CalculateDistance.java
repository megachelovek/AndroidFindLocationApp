package com.ssau.danilius.findlocationapp;

import java.util.ArrayList;
import java.util.List;

public class CalculateDistance {
    private static final int axisX = 0;
    private static final int axisY = 1;
    private static final int axisZ = 2;

    public static float[] GetDistance(float dt, List<float[]> data){
        float result[] = new float[3];
        float dx=0.0f;
        float dy =0.0f;
        float dz =0.0f;
        float vx=0.0f,vy = 0.0f,vz = 0.0f;
        for (int i=1; i<data.size(); i++)
        {
            vx+=(data.get(i-1)[0] + data.get(i)[0])/2.0f*dt;
            dx+=vx*dt;
//            vx+=(data.get(i-1)[0] + data.get(i)[0])/2.0f*dt*dt;
//            dx+=vx;
        }
        for (int i=1; i<data.size(); i++)
        {
            vy+=(data.get(i-1)[1] + data.get(i)[1])/2.0f*dt;
            dy+=vy*dt;
        }
        for (int i=1; i<data.size(); i++)
        {
            vz+=(data.get(i-1)[2] + data.get(i)[2])/2.0f*dt;
            dz+=vz*dt;
        }
        result[0]=dx;
        result[1]=dy;
        result[2]=dz;
        return result;
    }
    public static float[] getTestDistance(float dt, List<float[]> data){
        float resultDistance[] = new float[3];
        List<float[]> resultSpeed = new ArrayList<float[]>();
        float result[] = new float[3];
        resultSpeed = calculateSpeed(dt,data);
        result[axisX] = calculateDistance(dt,data,resultSpeed,axisX);
        result[axisY] = calculateDistance(dt,data,resultSpeed,axisY);
        result[axisZ] = calculateDistance(dt,data,resultSpeed,axisZ);
        return result;
    }

    private static List<float[]> calculateSpeed(float dt, List<float[]> data){
        List<float[]> resultSpeed = new ArrayList<float[]>();
        for (int j=0; j<data.size();j++){resultSpeed.add(new float[]{0.0f,0.0f,0.0f});}
        for (int axis=0; axis<3;axis++) {
            for (int i = 0; i < data.size()-1; i++) {
                resultSpeed.get(i + 1)[axis] = resultSpeed.get(i)[axis] + (data.get(i + 1)[axis] + data.get(i)[axis]) / 2.0f * dt;
            }
        }
        return resultSpeed;
    }

    private static float calculateDistance(float dt, List<float[]> data, List<float[]> speedData,int indexData){
        List<float[]> resultDistance = new ArrayList<float[]>();
        for (int j=0; j<data.size();j++){resultDistance.add(new float[]{0.0f,0.0f,0.0f});}
        for(int i=0; i<data.size()-1; i++) {
            resultDistance.get(i + 1)[indexData] = resultDistance.get(i)[indexData] + speedData.get(i)[indexData] / dt + (data.get(i + 1)[indexData] + data.get(i)[indexData]) / 2.0f * dt * dt;
        }
        return resultDistance.get(data.size()-1)[indexData];
    }
}
