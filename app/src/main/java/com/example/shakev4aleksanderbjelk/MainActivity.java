package com.example.shakev4aleksanderbjelk;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
    ImageView imageView;

    SensorManager sensorManager;
    Sensor sensor;

    float Accel;
    float AccelCurrent;
    float AccelLast;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        progressBarX = findViewById(R.id.progressBarX);
        progressBarY = findViewById(R.id.progressBarY);
        progressBarZ = findViewById(R.id.progressBarZ);

        progressBarHX = findViewById(R.id.progressBarHX);
        progressBarHY = findViewById(R.id.progressBarHY);
        progressBarHZ = findViewById(R.id.progressBarHZ);

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
        float x = sensorEvent.values[0]*10;
        float y = sensorEvent.values[1]*10;
        float z= sensorEvent.values[2]*10;
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

        if (Accel > 12) {
            Toast toast = Toast.makeText(getApplicationContext(), "Device has shaken.", Toast.LENGTH_LONG);
            toast.show();
        }

        if (x > 10) {
            imageView.setRotation(x);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

        @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this, sensor);
    }
}