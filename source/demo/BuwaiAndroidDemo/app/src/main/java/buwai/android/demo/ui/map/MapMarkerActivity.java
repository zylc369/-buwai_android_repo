package buwai.android.demo.ui.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.amap.api.maps2d.model.BitmapDescriptorFactory;

import buwai.android.commons.lang3.bitmap.BitmapUtils;
import buwai.android.demo.R;
import buwai.map.component.MapConfig;
import buwai.map.view.MapFragment;

public class MapMarkerActivity extends AppCompatActivity {

    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_marker);

        MapConfig mapConfig = new MapConfig();
        mapConfig.setMarkerRotate(false);
        Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.map_marker_128px);
        BitmapDrawable newDrawable = BitmapUtils.zoomDrawable(BitmapUtils.bitmapToDrawable(getResources(), bMap), 129f, 190.5f);
        mapConfig.setMarkerIcon(BitmapDescriptorFactory.fromBitmap(BitmapUtils.drawableToBitmap(newDrawable)));
        mapFragment = MapFragment.newInstance(mapConfig);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_map, mapFragment, "f1")
        //.addToBackStack("fname");
                .commit();
    }
}
