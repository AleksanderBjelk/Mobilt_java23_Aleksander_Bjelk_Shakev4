package com.example.shakev4aleksanderbjelk;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    TextView textX , textY ,textZ ;
    ProgressBar progressBarX, progressBarY, progressBarZ;
    ProgressBar progressBarHX, progressBarHY, progressBarHZ;
    SeekBar SeekBarX, SeekBarY, SeekBarZ;
    ImageView imageView;

    SensorManager sensorManager;
    Sensor sensor;
    Sensor lightSensor;


    float Accel;
    float AccelCurrent;
    float AccelLast;
    private boolean canShowToast = true;  // Flag to control toast display
    private static final long TOAST_DELAY = 2000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        progressBarX = findViewById(R.id.progressBarX);
        progressBarY = findViewById(R.id.progressBarY);
        progressBarZ = findViewById(R.id.progressBarZ);

        progressBarHX = findViewById(R.id.progressBarHX);
        progressBarHY = findViewById(R.id.progressBarHY);
        progressBarHZ = findViewById(R.id.progressBarHZ);

        SeekBarX = findViewById(R.id.seekBarX);
        SeekBarY = findViewById(R.id.seekBarY);
        SeekBarZ = findViewById(R.id.seekBarZ);

        textX = findViewById(R.id.textX);
        textY = findViewById(R.id.textY);
        textZ = findViewById(R.id.textZ);

        imageView = findViewById(R.id.imageView);

        Accel = 0.00f;
        AccelCurrent = SensorManager.GRAVITY_EARTH;
        AccelLast = SensorManager.GRAVITY_EARTH;




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }



    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z= sensorEvent.values[2];

        AccelLast = AccelCurrent;
        AccelCurrent = (float) Math.sqrt((double) (x*x+y*y+z*z));
        float delta = AccelCurrent - AccelLast;
        Accel = Accel * 0.9f + delta;

        textX.setText("X : " + (int) x + "rad s/m");
        textY.setText("Y : " + (int) y + "rad s/m");
        textZ.setText("Z : " + (int) z + "rad s/m");

        progressBarX.setProgress((int) Math.abs(x)*10);
        progressBarY.setProgress((int) Math.abs(y)*10);
        progressBarZ.setProgress((int) Math.abs(z)*10);

        progressBarHX.setProgress((int) Math.abs(x)*10);
        progressBarHY.setProgress((int) Math.abs(y)*10);
        progressBarHZ.setProgress((int) Math.abs(z)*10);

        SeekBarX.setProgress((int) Math.abs(x)*10);
        SeekBarY.setProgress((int) Math.abs(y)*10);
        SeekBarZ.setProgress((int) Math.abs(z)*10);


        if (Accel > 2 && canShowToast) {
            Toast toast = Toast.makeText(getApplicationContext(), "SLUTA SKAKA MIG", Toast.LENGTH_LONG);
            toast.show();
            canShowToast = false;
            new Handler().postDelayed(() -> canShowToast = true, TOAST_DELAY);
        }
        imageView.setRotation(x*10);

        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            float lightBrightness = sensorEvent.values[0];
            Log.d("LightSensor", "Light brightness: " + lightBrightness);
            float alpha = lightBrightness / 40000f;
            imageView.setAlpha(alpha);
        }
            
        }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

        @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this, sensor);
        sensorManager.unregisterListener(this, lightSensor);
    }
}