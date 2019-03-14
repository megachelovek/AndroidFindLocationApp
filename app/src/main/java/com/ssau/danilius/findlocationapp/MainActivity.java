package com.ssau.danilius.findlocationapp;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;
    Sensor accelerometr;
    Button btnStartFindLocation;
    TextView xDistanceTextView;
    TextView yDistanceTextView;
    TextView zDistanceTextView;
    TextView toOneMetres;
    Boolean startFlag = null;
    float dx = 0.0f;
    float vx = 0.0f;
    List<float[]> data = new ArrayList<float[]>();
    float[] resultData;
    List<Long> timerms = new ArrayList<Long>();
    private static final int SHAKE_THRESHOLD = 600;
    float accel[] = new float[]{0.0f, 0.0f, 0.0f}, result[] = new float[]{0.0f, 0.0f, 0.0f};
    float kFilteringFactor = 0.8f;
    long init,now,time;

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
                    sensorManager.registerListener((SensorEventListener) MainActivity.this, accelerometr, SensorManager.SENSOR_DELAY_GAME);
                    init=System.currentTimeMillis();
                } else {
                    if (startFlag) {
                        startFlag = false;
                        btnStartFindLocation.setBackgroundColor(Color.RED);
                        GetData(data);
                        data = CheckData(data);
                        data = SimpleFilter.FilterListSignal(data,(float)0.05); //(float) 5.23e-6
                        resultData = CalculateDistance.GetDistance(1, data);
                        GetData(data);
                        xDistanceTextView.setText("Расстояние X:" + String.valueOf(resultData[0]*0.0002264));///1e15
                        yDistanceTextView.setText("Расстояние Y:" + String.valueOf(resultData[1]*0.0002264));
                        zDistanceTextView.setText("Расстояние Z:" + String.valueOf(resultData[2]*0.0002264));
                        toOneMetres.setText("До метра (без коэф.):"+ String.valueOf(1/resultData[0])  );
                        sensorManager.unregisterListener((SensorEventListener) MainActivity.this);
                        data = new ArrayList<float[]>(){};
                    } else {
                        startFlag = true;
                        btnStartFindLocation.setBackgroundColor(Color.GREEN);
                        data = new ArrayList<float[]>(){};
                        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                        accelerometr = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
                        sensorManager.registerListener((SensorEventListener) MainActivity.this, accelerometr, SensorManager.SENSOR_DELAY_GAME);
                        init=System.currentTimeMillis();
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
            if (Math.abs(sensorEvent.values[0])>0.4){result[0] = sensorEvent.values[0];}
            if (Math.abs(sensorEvent.values[1])>0.4){result[1] = sensorEvent.values[1];}
            if (Math.abs(sensorEvent.values[2])>0.4){result[2] = sensorEvent.values[2];}
            float[] thisData = new float[]{result[0], result[1], result[2]};
            data.add(thisData);
            timerms.add(System.currentTimeMillis() - init);
        }

    }

    public void GetData(List<float[]> data) {
        String Resultx = "",Resulty = "",Resultz = "",time ="";
        for (int i=0;i<data.size();i++) {
            Resultx +=data.get(i)[0]+"\n";
            Resulty +=data.get(i)[1]+"\n";
            Resultz +=data.get(i)[2]+"\n";
            time += timerms.get(i)+"\n";
        }
        Resultx += "end";
        Resulty += "end";
        Resultz += "end";
    }

    public List<float[]>  CheckData(List<float[]> data) {
        Boolean check = false;
        double countX=0,countY=0,countZ =0;
        for (float[] value: data) {
            countX += Math.abs(value[0]);
            countY += Math.abs(value[1]);
            countZ += Math.abs(value[2]);
            if (data.get(0)[0] == value[0]){countX++;}
            if (data.get(0)[1] == value[1]){countY++;}
            if(data.get(0)[2] == value[2]){countZ++;}
        }
        /*if (countX > data.size()/2) {
            for (float[] value : data) {
                value[0] = 0;
            }
        }
        if (countY > data.size()/2) {
            for (float[] value : data) {
                value[1] = 0;
            }
        }
        if (countZ > data.size()/2) {
            for (float[] value : data) {
                value[2] = 0;
            }
        }*/
        return data;
    }

}

