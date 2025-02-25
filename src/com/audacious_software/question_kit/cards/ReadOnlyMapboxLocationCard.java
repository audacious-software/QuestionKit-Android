package com.audacious_software.question_kit.cards;

import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.appcompat.widget.SwitchCompat;

import com.audacious_software.question_kit.QuestionsActivity;
import com.audacious_software.question_kit.R;

import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.MapboxMap;
import com.mapbox.maps.Style;
import com.mapbox.maps.ViewAnnotationOptions;
import com.mapbox.maps.plugin.gestures.GesturesPlugin;
import com.mapbox.maps.plugin.gestures.GesturesUtils;
import com.mapbox.maps.viewannotation.ViewAnnotationManager;

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

        final ArrayList<Point> locations = new ArrayList<>();

        JSONArray points = prompt.getJSONArray("points");

        for (int i = 0; i < points.length(); i++) {
            JSONObject point = points.getJSONObject(i);

            Point latLng = Point.fromLngLat(point.getDouble("longitude"), point.getDouble("latitude"));

            locations.add(latLng);
        }

        final SwitchCompat styleSwitch = this.findViewById(R.id.style_switch);

        final MapView mapView = this.findViewById(R.id.mapbox_map_view);

        MapboxMap map = mapView.getMapboxMap();

        if (styleSwitch.isChecked()) {
            map.loadStyle(Style.SATELLITE_STREETS);
        } else {
            map.loadStyle(Style.MAPBOX_STREETS);
        }

        styleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    map.loadStyle(Style.SATELLITE_STREETS);
                } else {
                    map.loadStyle(Style.MAPBOX_STREETS);
                }
            }
        });

        GesturesPlugin gestures = GesturesUtils.getGestures(mapView);
        gestures.setPitchEnabled(false);
        gestures.setRotateEnabled(false);
        gestures.setDoubleTapToZoomInEnabled(false);

        CameraOptions.Builder cameraBuilder = new CameraOptions.Builder();
        cameraBuilder.center(locations.get(0));

        ViewAnnotationManager viewAnnotations = mapView.getViewAnnotationManager();

        if (locations.size() > 0) {
            cameraBuilder.zoom(16.0);

            for (Point location : locations) {
                ViewAnnotationOptions.Builder options = new ViewAnnotationOptions.Builder();
                cameraBuilder.center(location);

                viewAnnotations.addViewAnnotation(R.layout.view_annotation_map_pin, options.build());
            }
        }

        map.setCamera(cameraBuilder.build());
    }

    protected int getCardLayoutResource() {
        return R.layout.card_question_location_mapbox;
    }
}
