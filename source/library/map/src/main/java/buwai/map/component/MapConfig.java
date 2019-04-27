package buwai.map.component;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.maps2d.model.BitmapDescriptor;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 地图配置
 *
 * @author 不歪
 * @version 创建时间：2019/4/27 1:29 PM
 */
@Data
@NoArgsConstructor
public class MapConfig implements Parcelable {

    /**
     * 锚点图标
     */
    private BitmapDescriptor markerIcon = null;
    /**
     * 锚点是否旋转
     */
    private boolean isMarkerRotate = true;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.markerIcon, flags);
        dest.writeByte(this.isMarkerRotate ? (byte) 1 : (byte) 0);
    }

    protected MapConfig(Parcel in) {
        this.markerIcon = in.readParcelable(BitmapDescriptor.class.getClassLoader());
        this.isMarkerRotate = in.readByte() != 0;
    }

    public static final Creator<MapConfig> CREATOR = new Creator<MapConfig>() {
        @Override
        public MapConfig createFromParcel(Parcel source) {
            return new MapConfig(source);
        }

        @Override
        public MapConfig[] newArray(int size) {
            return new MapConfig[size];
        }
    };
}
