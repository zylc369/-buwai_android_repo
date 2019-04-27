package buwai.map.component;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.maps2d.model.LatLng;

import lombok.Data;

/**
 * 地图位置结果
 *
 * @author 不歪
 * @version 创建时间：2019-04-27 14:06
 */
@Data
public class MapResult implements Parcelable {

    /**
     * 当前人所在的位置
     */
    private LatLng latestPosition = null;

    /**
     * 当前人所在的地址
     */
    private String latestAddress = null;

    /**
     * 当前锚点所在位置
     */
    private LatLng markerPosition = null;
    /**
     * 当前锚点所在地址
     */
    private String markerAddress = null;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.latestPosition, flags);
        dest.writeString(this.latestAddress);
        dest.writeParcelable(this.markerPosition, flags);
        dest.writeString(this.markerAddress);
    }

    protected MapResult(Parcel in) {
        this.latestPosition = in.readParcelable(LatLng.class.getClassLoader());
        this.latestAddress = in.readString();
        this.markerPosition = in.readParcelable(LatLng.class.getClassLoader());
        this.markerAddress = in.readString();
    }

    public static final Creator<MapResult> CREATOR = new Creator<MapResult>() {
        @Override
        public MapResult createFromParcel(Parcel source) {
            return new MapResult(source);
        }

        @Override
        public MapResult[] newArray(int size) {
            return new MapResult[size];
        }
    };
}
