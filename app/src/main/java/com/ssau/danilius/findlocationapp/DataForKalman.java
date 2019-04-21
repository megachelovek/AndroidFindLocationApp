package com.ssau.danilius.findlocationapp;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.List;

public class DataForKalman {
    RealMatrix stateTransition; // Вектор состояния системы
    RealMatrix control; // Матрица управления
    RealMatrix processNoise; // Ковариационная матрица шума процесса
    RealVector initialStateEstimate; // Вектор состояния системы
    RealMatrix initialErrorCovarience;  //Ковариационная матрица шума измерений

    DataForKalman(float[] inputAccelerometrData){
        double[] inputAccelerometrDataDouble = new double[inputAccelerometrData.length];
        for (int i=0;  i< inputAccelerometrData.length; i++){
            inputAccelerometrDataDouble[i] = inputAccelerometrData[i];
        }
        initialStateEstimate = new ArrayRealVector(inputAccelerometrDataDouble);
//TODO
    }
}

