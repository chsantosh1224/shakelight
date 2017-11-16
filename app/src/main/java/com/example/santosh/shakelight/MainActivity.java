package com.example.santosh.shakelight;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.util.Log;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener {
    private static final int SHAKE_THRESHOLD = 850;
    static boolean flashLightStatus = true;
    static int shake_count=0;
    static long shake_last=System.currentTimeMillis();
    SensorManager mSensorManager;
    Sensor accSensor;
    Sensor magnetSensor;
    long lastUpdate=System.currentTimeMillis();
    float x,y,z,last_x=0,last_y=0,last_z=0;

    float gravity[]={0.0f,0.0f,0.0f};
   // float geoMagnetic[];
    float linear_acceleration[]={0.0f,0.0f,0.0f};
    //float azimut;
    //float pitch;
    //float roll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
       // magnetSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        mSensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
        //mSensorManager.registerListener(this, magnetSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this, accSensor);
       // mSensorManager.unregisterListener(this, magnetSensor);
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {

    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {

        Toast mytoast1;
        Toast mytoast2=Toast.makeText(getApplicationContext(),"magnetic field On",Toast.LENGTH_SHORT);
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            long temp;
            long curTime = System.currentTimeMillis();


            // only allow one update every 100ms.

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                x = event.values[0];
                y = event.values[0];
                z = event.values[0];

                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {


                    if((System.currentTimeMillis()-shake_last)>1000)
                    {
                        shake_count=0;
                    }
                    shake_count++;
                    if(shake_count>7){
                        if(flashLightStatus)
                            onFlash();
                            else
                                flashLightOff();
                        Log.d("count is"+shake_count,"mobile shook");
                        shake_count=0;
                }
                    shake_last=System.currentTimeMillis();
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }
    public void onFlash()
    {
        CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try{

            String cameraId = camManager.getCameraIdList()[0]; // Usually front camera is at 0 position.
            camManager.setTorchMode(cameraId, true);
            flashLightStatus=false;
        } catch (Exception e)
        {
        }
    }
    private void flashLightOff() {
        CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try{
            String cameraId = camManager.getCameraIdList()[0]; // Usually front camera is at 0 position.
            camManager.setTorchMode(cameraId, false);
            flashLightStatus=true;
        } catch (Exception e){
        }
    }
}







