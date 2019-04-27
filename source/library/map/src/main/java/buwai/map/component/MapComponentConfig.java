package buwai.map.component;

import android.content.Context;
import android.os.Bundle;

import com.amap.api.maps2d.MapView;

import lombok.Data;

/**
 * @author 不歪
 * @version 创建时间：2019/4/27 1:16 PM
 */
@Data
public class MapComponentConfig {

    private Context context = null;

    private MapView mapView = null;

    private Bundle savedInstanceState = null;

    /**
     * 锚点图标
     *
     * 如果没有传值，则使用默认风格的锚点
     */
    private MapConfig mapConfig = new MapConfig();
    
}
