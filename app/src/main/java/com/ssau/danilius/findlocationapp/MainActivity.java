package com.ssau.danilius.findlocationapp;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final int SHAKE_THRESHOLD = 600;
    SensorManager sensorManager;
    Sensor accelerometr, gyroscope;
    Button btnStartFindLocation;
    TextView xDistanceTextView;
    TextView yDistanceTextView;
    TextView zDistanceTextView;
    TextView toOneMetres;
    Boolean startFlag = null;
    List<float[]> dataAccelerometr = new ArrayList<float[]>();
    List<float[]> dataGyroscope = new ArrayList<float[]>();
    List<float[]> temp = new ArrayList<float[]>();
    float[] resultData;
    List<Long> timerms = new ArrayList<Long>();
    float valuesFromGyroscope[] = new float[]{0.0f, 0.0f, 0.0f}, valuesFromAccelerometr[] = new float[]{0.0f, 0.0f, 0.0f};
    long init;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStartFindLocation = (Button) findViewById(R.id.start_find_location);
        xDistanceTextView = (TextView) findViewById(R.id.xDistance);
        yDistanceTextView = (TextView) findViewById(R.id.yDistance);
        zDistanceTextView = (TextView) findViewById(R.id.zDistance);
        toOneMetres = (TextView) findViewById(R.id.toOneMultiply);

        long curTime = System.currentTimeMillis();

        //КНОПКА НАЧАЛА
        View.OnClickListener oclBtnFlag = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startFlag == null) {
                    startFlag = true;
                    btnStartFindLocation.setBackgroundColor(Color.GREEN);
                    sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                    accelerometr = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
                    gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
                    sensorManager.registerListener((SensorEventListener) MainActivity.this, accelerometr, SensorManager.SENSOR_DELAY_GAME);
                    sensorManager.registerListener((SensorEventListener) MainActivity.this, gyroscope, SensorManager.SENSOR_DELAY_GAME);
                    init = System.currentTimeMillis();
                } else {
                    if (startFlag) {
                        startFlag = false;
                        btnStartFindLocation.setBackgroundColor(Color.RED);

                        convertDataToTextAccelerometr(dataAccelerometr);
                        temp = dataAccelerometr.subList(0,dataAccelerometr.size());
                        dataAccelerometr = CheckData(dataAccelerometr);
                        KalmanFilter kalmanFilter = new KalmanFilter(dataAccelerometr);
                        dataAccelerometr = kalmanFilter.DoFiltering();
                        resultData = CalculateDistance.GetDistance((float) 0.3, dataAccelerometr);
                        float tempResult = resultData[0];
                        convertDataToTextAccelerometr(dataAccelerometr);
                        AlphaBetaFilter alphaBetaFilter= new AlphaBetaFilter(temp,(float) 0.3);
                        dataAccelerometr = alphaBetaFilter.DoFiltering();
                        convertDataToTextAccelerometr(dataAccelerometr);
                        resultData = CalculateDistance.GetDistance((float) 0.3, dataAccelerometr);

                        xDistanceTextView.setText("Расстояние X:" + String.valueOf(tempResult * 0.01));///1e15
                        yDistanceTextView.setText("Расстояние Y:" + String.valueOf(resultData[0] * 0.01));//*0.0002264
                        zDistanceTextView.setText("Расстояние Z:" + String.valueOf(resultData[2] * 0.01));
                        toOneMetres.setText("До метра (без коэф.):" + String.valueOf(1 / resultData[0]));
                        sensorManager.unregisterListener((SensorEventListener) MainActivity.this);
                        dataAccelerometr = new ArrayList<float[]>() {
                        };
                    } else {
                        startFlag = true;
                        btnStartFindLocation.setBackgroundColor(Color.GREEN);
                        dataAccelerometr = new ArrayList<float[]>() {};
                        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                        init = System.currentTimeMillis();
                    }
                }
            }
        };
        btnStartFindLocation.setOnClickListener(oclBtnFlag);
    }

    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        if (startFlag) {
            if (Math.abs(sensorEvent.values[0]) > 0.1) {
                saveDataFromSensor(0,sensorEvent.values[0],sensorEvent.sensor.getName());
            }
            if (Math.abs(sensorEvent.values[1]) > 0.1) {
                saveDataFromSensor(1,sensorEvent.values[1],sensorEvent.sensor.getName());
            }
            if (Math.abs(sensorEvent.values[2]) > 0.1) {
                saveDataFromSensor(2,sensorEvent.values[2],sensorEvent.sensor.getName());
            };
            if (sensorEvent.sensor.getName() == "Linear Acceleration"){dataAccelerometr.add(new float[]{valuesFromAccelerometr[0], valuesFromAccelerometr[1], valuesFromAccelerometr[2]});}
            if (sensorEvent.sensor.getName() == "BMI160 Gyroscope"){dataGyroscope.add(new float[]{valuesFromGyroscope[0], valuesFromGyroscope[1], valuesFromGyroscope[2]});}
            timerms.add(System.currentTimeMillis() - init);
        }

    }

    public void saveDataFromSensor(int indexValue, float value, String sensorName){
        if (sensorName == "Linear Acceleration"){valuesFromAccelerometr[indexValue] = value;}
        if (sensorName == "BMI160 Gyroscope"){valuesFromGyroscope[indexValue] = value;}
    }

    public void convertDataToTextAccelerometr(List<float[]> data) {
        String  resultXAccelerometr= "", resultYAccelerometr = "", resultZAccelerometr = "", time = "";
        for (int i = 0; i < data.size(); i++) {
            resultXAccelerometr += data.get(i)[0] + "\n";
            resultYAccelerometr += data.get(i)[1] + "\n";
            resultZAccelerometr += data.get(i)[2] + "\n";
            time += timerms.get(i) + "\n";
        }
        resultXAccelerometr += "end";
        resultYAccelerometr += "end";
        resultZAccelerometr += "end";
    }
    public void convertDataToTextGyroscope(List<float[]> data) {
        String  resultXGyroscope= "", resultYGyroscope = "", resultZGyroscope = "", time = "";
        for (int i = 0; i < data.size(); i++) {
            resultXGyroscope += data.get(i)[0] + "\n";
            resultYGyroscope += data.get(i)[1] + "\n";
            resultZGyroscope += data.get(i)[2] + "\n";
            time += timerms.get(i) + "\n";
        }
        resultXGyroscope += "end";
        resultYGyroscope += "end";
        resultZGyroscope += "end";
    }

    public List<float[]> CheckData(List<float[]> data) {
        double countX = 0, countY = 0, countZ = 0;
        for (int i = 1; i < data.size(); i++) {
            if (data.get(i)[0] == data.get(i - 1)[0]) {
                countX += 1;
            }
            if (data.get(i)[1] == data.get(i - 1)[1]) {
                countY += 1;
            }
            if (data.get(i)[2] == data.get(i - 1)[2]) {
                countZ += 1;
            }
        }
        if (countX > data.size() / 2) {
            NullArray(data, 0);
        }
        if (countY > data.size() / 2) {
            NullArray(data, 1);
        }
        if (countZ > data.size() / 2) {
            NullArray(data, 2);
        }
        return data;
    }

    private List<float[]> NullArray(List<float[]> data, int index) {
        for (int i = 1; i < data.size(); i++) {
            data.get(i)[index] = 0;
        }
        return data;
    }

}

