package buwai.map.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import com.amap.api.maps2d.model.Marker;

import buwai.map.component.MapConfig;

public class SensorEventHelper implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private long lastTime = 0;
    private final int TIME_SENSOR = 100;
    private float mAngle;
    private Context mContext;
    private Marker mMarker;
    private MapConfig mapConfig;

    public SensorEventHelper(Context context, MapConfig mapConfig) {
        mContext = context;
        this.mapConfig = mapConfig;
        mSensorManager = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

    }

    public void registerSensorListener() {
        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unRegisterSensorListener() {
        mSensorManager.unregisterListener(this, mSensor);
    }

    public void setCurrentMarker(Marker marker) {
        mMarker = marker;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (System.currentTimeMillis() - lastTime < TIME_SENSOR) {
            return;
        }
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ORIENTATION: {
                lastTime = System.currentTimeMillis();
                if (!mapConfig.isMarkerRotate()) {
                    break;
                }
                if (mMarker == null) {
                    break;
                }
                Float local = calcAngle(event);
                if (null == local) {
                    break;
                }
                mAngle = local;
                mMarker.setRotateAngle(360 - mAngle);
            }
        }

    }

    private Float calcAngle(SensorEvent event) {
        float x = event.values[0];
        x += getScreenRotationOnPhone(mContext);
        x %= 360.0F;
        if (x > 180.0F)
            x -= 360.0F;
        else if (x < -180.0F)
            x += 360.0F;

        if (Math.abs(mAngle - x) < 3.0f) {
            return null;
        }
        return Float.isNaN(x) ? 0 : x;
    }

    public static int getScreenRotationOnPhone(Context context) {
        final Display display = ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        switch (display.getRotation()) {
            case Surface.ROTATION_0:
                return 0;

            case Surface.ROTATION_90:
                return 90;

            case Surface.ROTATION_180:
                return 180;

            case Surface.ROTATION_270:
                return -90;
        }
        return 0;
    }
}
