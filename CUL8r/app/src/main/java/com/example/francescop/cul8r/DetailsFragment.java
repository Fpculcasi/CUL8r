package com.example.francescop.cul8r;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetailsFragment extends Fragment implements OnMapReadyCallback{
    GoogleMap mGoogleMap;
    View mView;

    private static final LatLng PISA_ING = new LatLng(43.721361, 10.389927);

    // factory
    public static DetailsFragment newInstance(int index) {
        DetailsFragment f = new DetailsFragment();

        // provides index as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    public int getIndexFromArguments() {
        return getArguments().getInt("index", 0);
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("***onCrateView>","");
        int l = 0;
        int shownIndex = getIndexFromArguments();
        switch (shownIndex) {
            case 1:
                l = R.layout.fragment_search; break;
            case 2:
                l = R.layout.fragment_add; break;
            case 3:
                l = R.layout.details_fragment3; break;
        }
        mView = inflater.inflate(l, container, false);

        //programmatically set button onClick listener
        switch(shownIndex){
            case 1:
                MapView m =(MapView) mView.findViewById(R.id.map);
                m.onCreate(savedInstanceState);
                m.onResume(); //without this, map showed but was empty
                m.getMapAsync(this);
                break;
            case 2:
                mView.findViewById(R.id.buttonAdd).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        /*TODO: code to add an event on the server*/
                        //new Add().execute(username, );
                        Toast.makeText(getActivity(), "Added!", Toast.LENGTH_LONG).show();
                    }
                });
                break;
        }

        return mView;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getActivity());

        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        googleMap.addMarker(new MarkerOptions().position(PISA_ING).title("Scuola di Ingegneria a Pisa").snippet("Dio ti salvi"));

        CameraPosition IngUnipi = CameraPosition.builder().target(PISA_ING).zoom(16).bearing(0).tilt(45).build();

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(IngUnipi));
    }
}