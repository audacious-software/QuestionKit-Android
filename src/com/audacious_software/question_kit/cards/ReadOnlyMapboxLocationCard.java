package com.audacious_software.question_kit.cards;

import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.appcompat.widget.SwitchCompat;

import com.audacious_software.question_kit.QuestionsActivity;
import com.audacious_software.question_kit.R;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReadOnlyMapboxLocationCard extends ReadOnlyTextCard {
    public ReadOnlyMapboxLocationCard(QuestionsActivity activity, JSONObject prompt, String defaultLanguage) {
        super(activity, prompt, defaultLanguage);
    }

    protected void initializeView(JSONObject prompt, ViewGroup parent) throws JSONException {
        super.initializeView(prompt, parent);

        final ReadOnlyMapboxLocationCard me = this;

        final ArrayList<LatLng> locations = new ArrayList<>();

        JSONArray points = prompt.getJSONArray("points");

        for (int i = 0; i < points.length(); i++) {
            JSONObject point = points.getJSONObject(i);

            LatLng latLng = new LatLng(point.getDouble("latitude"), point.getDouble("longitude"));

            locations.add(latLng);
        }

        final SwitchCompat styleSwitch = this.findViewById(R.id.style_switch);


        final MapView mapView = this.findViewById(R.id.mapbox_map_view);
        mapView.onCreate(null);

        mapView.getMapAsync(new OnMapReadyCallback() {
            public void onMapReady(final MapboxMap map) {
                if (styleSwitch.isChecked()) {
                    map.setStyle(Style.SATELLITE_STREETS);
                } else {
                    map.setStyle(Style.MAPBOX_STREETS);
                }

                styleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                        if (checked) {
                            map.setStyle(Style.SATELLITE_STREETS);
                        } else {
                            map.setStyle(Style.MAPBOX_STREETS);
                        }
                    }
                });

                map.getUiSettings().setAllGesturesEnabled(false);

                Handler handler = new Handler(Looper.getMainLooper());

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final DisplayMetrics metrics = new DisplayMetrics();
                        me.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

                        if (locations.size() > 0) {
                            try {
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(locations.get(0), 16));
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                                // View not ready to update yet...
                            }
                        }

                        for (LatLng latLng : locations) {
                            map.addMarker(new MarkerOptions().position(latLng));
                        }
                    }
                }, 500);

                mapView.onResume();
            }
        });
    }

    protected int getCardLayoutResource() {
        return R.layout.card_question_location_mapbox;
    }
}
