package com.ssau.danilius.findlocationapp;

import java.util.List;

public class CalculateDistance {
    public static float[] GetDistance(float dt, List<float[]> data){
        float result[] = new float[3];
        float dx=0.0f;
        float dy =0.0f;
        float dz =0.0f;
        float vx=0.0f,vy = 0.0f,vz = 0.0f;
        for (int i=1; i<data.size(); i++)
        {
//            vx+=(data.get(i-1)[0] + data.get(i)[0])/2.0f*dt;
//            dx+=vx*dt;
            vx+=(data.get(i-1)[0] + data.get(i)[0])/2.0f*dt*dt;
            dx+=vx;//*dt;
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
}
