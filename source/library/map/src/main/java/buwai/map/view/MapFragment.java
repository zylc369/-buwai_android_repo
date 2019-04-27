package buwai.map.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps2d.MapView;

import buwai.map.R;
import buwai.map.component.MapComponent;
import buwai.map.component.MapComponentConfig;
import buwai.map.component.MapConfig;
import buwai.map.component.MapResult;

/**
 * 地图UI Fragment
 * <p>
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MapFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MapFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 * @author 不歪
 * @version 创建时间：2019-04-27 08 14
 */
public class MapFragment extends Fragment {

    private final static String ARG_MAP_CONFIG = "ARG_MAP_CONFIG";

    /**
     * 地图配置
     */
    private MapConfig mapConfig = null;
    /**
     * 地图监听器对象
     */
    private OnFragmentInteractionListener listener = null;

    /**
     * 地图组件
     */
    private MapComponent mapComponent = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (null != arguments) {
            mapConfig = arguments.getParcelable(ARG_MAP_CONFIG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        MapView mapView = rootView.findViewById(R.id.map);
        MapComponentConfig mapComponentConfig = new MapComponentConfig();
        mapComponentConfig.setContext(getContext());
        mapComponentConfig.setSavedInstanceState(savedInstanceState);
        mapComponentConfig.setMapView(mapView);
        mapComponentConfig.setMapConfig(mapConfig);
        mapComponent = new MapComponent(mapComponentConfig);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    public MapResult getMapResult() {
        return mapComponent.getMapResult();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        mapComponent = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * <p>
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html);
     * for more information.
     */
    public interface OnFragmentInteractionListener {
    }

    @Override
    public void onResume() {
        super.onResume();
        mapComponent.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapComponent.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapComponent.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapComponent.onDestroy();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mapConfig 地图配置
     * @return A new instance of fragment MapFragment.
     */
    public static MapFragment newInstance(MapConfig mapConfig) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_MAP_CONFIG, mapConfig);
        fragment.setArguments(args);
        return fragment;
    }

}
