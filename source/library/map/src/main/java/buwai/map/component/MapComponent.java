package buwai.map.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.Circle;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;

import buwai.map.R;
import buwai.map.constant.Constant;
import buwai.map.util.SensorEventHelper;
import lombok.Getter;

/**
 * 地图组件
 *
 * @author 不歪
 * @version 创建时间：2019/4/20 5:12 PM
 */
public class MapComponent implements AMapLocationListener, LocationSource {

    public static final String LOCATION_MARKER_FLAG = "未知位置";
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);

    /**
     * 地图组件配置
     */
    private MapComponentConfig mapComponentConfig;

    private Context context;
    private AMap aMap;
    private MapView mapView;
    private LocationSource.OnLocationChangedListener mListener;
    private boolean mFirstFix = false;
    private SensorEventHelper mSensorHelper;
    private Marker mLocMarker;
    private Circle mCircle;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private GeocodeSearchListenerImpl geocodeSearchListener;

    /**
     * 地图位置结果
     */
    @Getter
    private MapResult mapResult;

    public MapComponent(MapComponentConfig mapComponentConfig) {
        this.mapComponentConfig = mapComponentConfig;
        init();
    }

    private void init() {
        this.context = mapComponentConfig.getContext();
        mapView = mapComponentConfig.getMapView();
        mapView.onCreate(mapComponentConfig.getSavedInstanceState());// 此方法必须重写

        // 初始化锚点图标
        if (null == mapComponentConfig.getMapConfig().getMarkerIcon()) {
            Bitmap bMap = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.navi_map_gps_locked);
            mapComponentConfig.getMapConfig().setMarkerIcon(BitmapDescriptorFactory.fromBitmap(bMap));
        }

        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
        mSensorHelper = new SensorEventHelper(context, mapComponentConfig.getMapConfig());
        mSensorHelper.registerSensorListener();

        //地理搜索类
        geocodeSearchListener = new GeocodeSearchListenerImpl(context, MapResultUpdateType.LATEST, this);
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
//        val locationStyle = MyLocationStyle()
//        locationStyle.myLocationType(MyLocationStyle.LOCALTIO)
//        locationStyle.showMyLocation(true)
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true); // 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
//        aMap.setMyLocationStyle(locationStyle)
//        //地图定位,使自定位Maker处于地图中心
//        aMap.setOnMyLocationChangeListener(object : AMap.OnMyLocationChangeListener {
//            override fun onMyLocationChange(location: Location) {
//
//                val latLng = LatLng(location.getLatitude(), location.getLongitude())
//                Constant.latLng = latLng
//                if (localMark == null) {
//                    var url = ""
//                    if (!TextUtils.isEmpty(TokenSavemanager.userId(this@MainActivity))) {
//                        url = UserInfoManager.getUserBean(this@MainActivity).getPhoto()
//                    }
//                    val view = MyMarkerView(this@MainActivity, url, R.mipmap.img_head_maintenance_default)//自定义Maker
//                    localMark = aMap.addMarker(MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromView(view)))
//                } else {
//                    localMark.setPosition(latLng)
//                }
//
//                requestMarksData(latLng)//请求网络，加载其余的Maker(注：给这些Maker设Tag,例：maker.setObject("Tag"))
//
//            }
//        })

        //地图移动，自定义Maker始终处于地图中心点
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {

            private GeocodeSearchListenerImpl geocodeSearchListener = new GeocodeSearchListenerImpl(MapComponent.this.context, MapResultUpdateType.MARKER, MapComponent.this);

            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                setMarkerTitle(LOCATION_MARKER_FLAG);
                if (mLocMarker != null) {
                    mLocMarker.setPosition(cameraPosition.target);
                    geocodeSearchListener.getAddressByLatlng(cameraPosition.target);
                }
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
//                removeMarksFromMap()//移动结束的时候，先移除地图上Maker(除了处于地图中心的Maker)
//                requestMarksData(cameraPosition.target)

            }
        });
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                LatLng latLng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                if (!mFirstFix) {
                    mFirstFix = true;
//                    addCircle(latLng!!, amapLocation.accuracy.toDouble())//添加定位精度圆
                    addMarker(latLng);//添加定位图标
                    mSensorHelper.setCurrentMarker(mLocMarker);//定位图标旋转
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f));
                    geocodeSearchListener.getAddressByLatlng(latLng);
                } else {
//                    mCircle.center = location
//                    mCircle.radius = amapLocation.accuracy.toDouble()
//                    mLocMarker.position = location
                }
//                Log.i(Constant.LOG_TAG, "latLng=$latLng")
//                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18f))
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e(Constant.LOG_ERROR_TAG, errText);
                Toast.makeText(context, errText, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    @Override
    public void activate(LocationSource.OnLocationChangedListener listener) {
        mListener = listener;
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(context);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mLocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mLocationClient.startLocation();
        } else {
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapResult.getLatestPosition(), 18f));
        }
    }

    private void addCircle(LatLng latlng, Double radius) {
        CircleOptions options = new CircleOptions();
        options.strokeWidth(1f);
        options.fillColor(FILL_COLOR);
        options.strokeColor(STROKE_COLOR);
        options.center(latlng);
        options.radius(radius);
        mCircle = aMap.addCircle(options);
    }

    private void addMarker(LatLng latlng) {
        if (mLocMarker != null) {
            return;
        }


        //		BitmapDescriptor des = BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked);
        MarkerOptions options = new MarkerOptions();
        options.icon(mapComponentConfig.getMapConfig().getMarkerIcon());
        options.anchor(0.5f, 0.5f);
        options.position(latlng);
        mLocMarker = aMap.addMarker(options);
        setMarkerTitle(LOCATION_MARKER_FLAG);
    }

    public void setMarkerTitle(String title) {
        mLocMarker.setTitle(title);
        mLocMarker.showInfoWindow();
    }

    public void onSaveInstanceState(Bundle outState) {
        mapView.onSaveInstanceState(outState);
    }

    public void onResume() {
        mapView.onResume();
        if (mSensorHelper != null) {
            mSensorHelper.registerSensorListener();
        }
    }

    public void onPause() {
        if (mSensorHelper != null) {
            mSensorHelper.unRegisterSensorListener();
            mSensorHelper.setCurrentMarker(null);
            mSensorHelper = null;
        }
        mapView.onPause();
        deactivate();
        mFirstFix = false;
    }

    public void onDestroy() {
        mapView.onDestroy();
        if (null != mLocationClient) {
            mLocationClient.onDestroy();
        }
    }

}
