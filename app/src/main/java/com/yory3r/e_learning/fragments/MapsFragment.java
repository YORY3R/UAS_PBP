package com.yory3r.e_learning.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.yory3r.e_learning.R;
import com.yory3r.e_learning.databinding.FragmentAboutBinding;
import com.yory3r.e_learning.databinding.FragmentMapsBinding;

import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback, PermissionsListener
{
    private MapView mapView;
    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private FragmentMapsBinding binding;
    private int menuItem = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Mapbox.getInstance(getActivity().getApplicationContext(),getString(R.string.mapbox_access_token));

        binding = FragmentMapsBinding.inflate(inflater, container, false);


        mapView = binding.mapView;

//        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        return binding.getRoot();
    }

    public Toolbar.OnMenuItemClickListener topAppBar = new Toolbar.OnMenuItemClickListener()
    {
        @Override
        public boolean onMenuItemClick(MenuItem item)
        {
            if(item.getItemId() == R.id.menuStreetMaps)
            {
                menuItem = 0;
            }
            else if(item.getItemId() == R.id.menuDarkMaps)
            {
                menuItem = 1;
            }
            else if(item.getItemId() == R.id.menuLightMaps)
            {
                menuItem = 2;
            }
            else if(item.getItemId() == R.id.menuOutdoorsMaps)
            {
                menuItem = 3;
            }
            else if(item.getItemId() == R.id.menuSatelliteMaps)
            {
                menuItem = 4;
            }
            else
            {
                menuItem = 5;
            }

            return true;
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain)
    {
        Toast.makeText(getContext(), R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted)
    {
        if(granted)
        {
            mapboxMap.getStyle(new Style.OnStyleLoaded()
            {
                @Override
                public void onStyleLoaded(@NonNull Style style)
                {
                    enableLocationComponent(style);
                }
            });
        }
        else
        {
            Toast.makeText(getContext(), R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap)
    {
        this.mapboxMap = mapboxMap;
        String style = Style.MAPBOX_STREETS;

        if(menuItem == 0)
        {
            style = Style.MAPBOX_STREETS;
        }
        else if(menuItem == 1)
        {
            style = Style.DARK;
        }
        else if(menuItem == 2)
        {
            style = Style.LIGHT;
        }
        else if(menuItem == 3)
        {
            style = Style.OUTDOORS;
        }
        else if(menuItem == 4)
        {
            style = Style.SATELLITE;
        }
        else
        {
            style = Style.SATELLITE_STREETS;
        }

        mapboxMap.setStyle(style, new Style.OnStyleLoaded()
        {
            @Override
            public void onStyleLoaded(@NonNull Style style)
            {
                enableLocationComponent(style);
            }
        });
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle)
    {
        if(PermissionsManager.areLocationPermissionsGranted(getContext()))
        {
            LocationComponentOptions customLocationComponentOptions = LocationComponentOptions.builder(getContext()).pulseEnabled(true).build();
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(getContext(),loadedMapStyle).locationComponentOptions(customLocationComponentOptions).build());
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.NORMAL);
        }
        else
        {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }
}