package buwai.map.component;

import android.content.Context;
import android.util.Log;

import buwai.map.constant.Constant;

import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

/**
 * 地理编码监听器
 */
public class GeocodeSearchListenerImpl implements GeocodeSearch.OnGeocodeSearchListener {

    private Context context;
    private MapResultUpdateType mapResultUpdateType;
    private MapComponent mapComponent;
    private GeocodeSearch geocodeSearch;
    
    public GeocodeSearchListenerImpl(Context context, MapResultUpdateType mapResultUpdateType, MapComponent mapComponent) {
        this.context = context;
        this.mapResultUpdateType = mapResultUpdateType;
        this.mapComponent = mapComponent;

        geocodeSearch = new GeocodeSearch(context);
        geocodeSearch.setOnGeocodeSearchListener(this);
    }

    /**
     * 得到逆地理编码异步查询结果
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
        String formatAddress = regeocodeAddress.getFormatAddress();
        String simpleAddress = formatAddress.substring(9);
        Log.i(Constant.LOG_TAG, "[onRegeocodeSearched] 查询经纬度对应详细地址：$formatAddress/$simpleAddress");

        LatLng latLng = new LatLng(regeocodeResult.getRegeocodeQuery().getPoint().getLatitude(), regeocodeResult.getRegeocodeQuery().getPoint().getLongitude());
        if (mapResultUpdateType == MapResultUpdateType.LATEST) {
            mapComponent.getMapResult().setLatestPosition(latLng);
            mapComponent.getMapResult().setLatestAddress(formatAddress);
        }
        if (mapResultUpdateType == MapResultUpdateType.MARKER || null == mapComponent.getMapResult().getMarkerPosition()) {
            mapComponent.getMapResult().setMarkerPosition(latLng);
            mapComponent.getMapResult().setMarkerAddress(formatAddress);
        }
        mapComponent.setMarkerTitle(simpleAddress);
    }

    @Override
    public void onGeocodeSearched(GeocodeResult p0, int p1) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    public void getAddressByLatlng(LatLng latLng) {
        //逆地理编码查询条件：逆地理编码查询的地理坐标点、查询范围、坐标类型。
        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 500f, GeocodeSearch.AMAP);
        //异步查询
        geocodeSearch.getFromLocationAsyn(query);
    }

}
