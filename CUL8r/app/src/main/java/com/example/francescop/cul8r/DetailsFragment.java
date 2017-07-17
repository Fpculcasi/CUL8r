package com.example.francescop.cul8r;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class DetailsFragment extends Fragment implements OnMapReadyCallback{
    Dialog loadingDialog;
    GoogleMap mGoogleMap;
    View mView;
    // GPSTracker class
    GPSTracker gps;

    ListView lv;

    // Stores the markers' id
    Map <String, String> mMarkers = new HashMap<>();
    String markerChosenId;

    private static final LatLng PISA_ING = new LatLng(43.721361, 10.389927);

    // factory
    public static DetailsFragment newInstance(int index, String username) {
        DetailsFragment f = new DetailsFragment();

        // provides index as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        args.putString("username", username);
        f.setArguments(args);
        return f;
    }

    public int getIndexFromArguments() {
        return getArguments().getInt("index", 0);
    }

    /*TODO save fragment instance******************************************************************
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            //Restore the fragment's state here
            switch (getIndexFromArguments()) {
                case 2:
                    ((EditText) mView.findViewById(R.id.editTextDescription)).setText(savedInstanceState.getString("descr"));
                    ((EditText) mView.findViewById(R.id.editTextPosition)).setText(savedInstanceState.getString("pos"));
                    ((EditText) mView.findViewById(R.id.editTextDateStart)).setText(savedInstanceState.getString("start"));
                    ((EditText) mView.findViewById(R.id.editTextDateEnd)).setText(savedInstanceState.getString("end"));
                    break;
                case 3:
                    //l = R.layout.details_fragment3; break;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        switch (getIndexFromArguments()) {
            case 2:
                outState.putString("descr",((EditText) mView.findViewById(R.id.editTextDescription)).getText().toString());
                EditText posET = (EditText) mView.findViewById(R.id.editTextPosition);
                outState.putString("pos", posET.getText().toString());
                EditText startET = (EditText) mView.findViewById(R.id.editTextDateStart);
                outState.putString("start",startET.getText().toString());
                EditText endET = (EditText) mView.findViewById(R.id.editTextDateEnd);
                outState.putString("end",endET.getText().toString());
                break;
            case 3:
                //l = R.layout.details_fragment3; break;
        }
    }
    /**********************************************************************************************/

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        int l = 0;
        gps = new GPSTracker(getActivity());
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

        //programmatically set buttons' onClick listener
        switch(shownIndex){
            case 1:
                final MapView m =(MapView) mView.findViewById(R.id.map);
                m.onCreate(savedInstanceState);
                m.onResume(); //without this, map showed but was empty
                m.getMapAsync(this);
                break;
            case 2:
                mView.findViewById(R.id.buttonAdd).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText descrET = (EditText) mView.findViewById(R.id.editTextDescription);
                        String descr = descrET.getText().toString();
                        EditText posET = (EditText) mView.findViewById(R.id.editTextPosition);
                        String pos = posET.getText().toString();
                        EditText startET = (EditText) mView.findViewById(R.id.editTextDateStart);
                        String start = startET.getText().toString();
                        EditText endET = (EditText) mView.findViewById(R.id.editTextDateEnd);
                        String end = endET.getText().toString();

                        boolean ok = true;
                        if (descr.isEmpty()){ // a descriprion (even a is enough character) is needed
                            ok = false;
                            descrET.setBackgroundColor(Color.RED);
                        }else descrET.setBackgroundColor(Color.TRANSPARENT);

                        if(!pos.matches("^\\(-?\\d+(\\.\\d+)?;-?\\d+(\\.\\d+)?\\)$")) {
                            ok = false;
                            posET.setBackgroundColor(Color.RED);
                        }else posET.setBackgroundColor(Color.TRANSPARENT);

                        if(!start.matches("^\\d{4}-\\d{2}-\\d{2}\\s([01]?\\d|2[0-3]):[0-5]\\d(:[0-5]\\d)?")){
                            ok = false;
                            startET.setBackgroundColor(Color.RED);
                        }else startET.setBackgroundColor(Color.TRANSPARENT);
                        if(!end.matches("^\\d{4}-\\d{2}-\\d{2}\\s([01]?\\d|2[0-3]):[0-5]\\d(:[0-5]\\d)?")){
                            ok = false;
                            endET.setBackgroundColor(Color.RED);
                        }else endET.setBackgroundColor(Color.TRANSPARENT);

                        if(ok) new AddEvent().execute(getArguments().getString("username"), descr, pos, start, end);
                        else Toast.makeText(getActivity(),"Invalid parameters",Toast.LENGTH_LONG).show();
                    }
                });
                mView.findViewById(R.id.buttonPosition).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // check if GPS enabled
                        if(gps.canGetLocation()){
                            double latitude = gps.getLatitude();
                            double longitude = gps.getLongitude();
                            gps.stopUsingGPS();

                            ((EditText)mView.findViewById(R.id.editTextPosition)).setText("("+latitude+";"+longitude+")");

                        }else { // can't get location (GPS or Network is not enabled)
                            gps.showSettingsAlert();
                            // Ask user to enable GPS/network in settings
                        }
                    }
                });
                break;
            case 3:
                lv = (ListView) mView.findViewById(R.id.listview);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {
                        Event entry = (Event) parent.getItemAtPosition(position);
                        Log.d("***event "+entry.getDescription(),"verified "+entry.isVerified());
                        if (entry.isVerified()) return;
                        LatLng l = new LatLng(gps.getLatitude(), gps.getLongitude());
                        new UpdateEvent(entry).execute(l);
                    }
                });
                new GetEvents().execute();
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

        // Handle marker selection
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            public void onInfoWindowClick(Marker marker) {
                markerChosenId = mMarkers.get(marker.getId());
                Button b = (Button) mView.findViewById(R.id.partecipate);
                b.setVisibility(View.VISIBLE);
                String buttonTxt = "Participate: " + markerChosenId;
                b.setText(buttonTxt);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new Participate().execute(markerChosenId);
                    }
                });
            }
        });

        // GPSTracker class
        //gps = new GPSTracker(getActivity());
        // check if GPS enabled
        if(gps.canGetLocation()){
            LatLng l = new LatLng(gps.getLatitude(), gps.getLongitude());
            gps.stopUsingGPS();
            /*googleMap.addMarker(new MarkerOptions()
                    .position(l)
                    .title("here you are")
                    .snippet("move")
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));*/

            /*(--TODO: remove the following code, it's just a test
            float[] markerColors  = {BitmapDescriptorFactory.HUE_RED,
                    BitmapDescriptorFactory.HUE_ORANGE,
                    BitmapDescriptorFactory.HUE_YELLOW,
                    BitmapDescriptorFactory.HUE_GREEN,
                    BitmapDescriptorFactory.HUE_CYAN,
                    BitmapDescriptorFactory.HUE_AZURE,
                    BitmapDescriptorFactory.HUE_BLUE,
                    BitmapDescriptorFactory.HUE_VIOLET,
                    BitmapDescriptorFactory.HUE_MAGENTA,
                    BitmapDescriptorFactory.HUE_ROSE,
            };
            for(double i=0; i<10; i++) {
                LatLng lx = new LatLng(gps.getLatitude()+(i%3/100),gps.getLongitude()+(i/300));
                Log.d("***lx","lat:"+lx.latitude+"; lng:"+lx.longitude);
                googleMap.addMarker(new MarkerOptions()
                        .position(lx)
                        .title("here you are" + i).snippet("move")
                        .icon(BitmapDescriptorFactory.defaultMarker(markerColors[(int) i])));
            }
            //--)*/

            CameraPosition first = CameraPosition.builder()
                    .target(l).zoom(14).bearing(0).tilt(30).build();

            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(first));
            new Search().execute(l);

        } else { // can't get location (GPS or Network is not enabled)
            gps.showSettingsAlert();
            // Ask user to enable GPS/network in settings

            googleMap.addMarker(new MarkerOptions().position(PISA_ING)
                    .title("Scuola di Ingegneria a Pisa").snippet("Dio ti salvi"));

            CameraPosition IngUnipi = CameraPosition.builder()
                    .target(PISA_ING).zoom(16).bearing(0).tilt(45).build();

            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(IngUnipi));
        }
    }

    private class AddEvent extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(isAdded()) loadingDialog = ProgressDialog.show(getActivity(), "Add a new event", "Issuing the request...");
        }

        @Override
        protected String doInBackground(String... params) {
            String uname = params[0];
            String descr = params[1];
            String pos = params[2];
            String start = params[3];
            String end = params[4];

            String posizioni[] = pos.split(";");

            Map<String,String> nameValuePairs = new HashMap<>();
            nameValuePairs.put("uname", uname);
            nameValuePairs.put("descr", descr);
            nameValuePairs.put("lat", posizioni[0].replace("(",""));
            nameValuePairs.put("lng", posizioni[1].replace(")",""));
            nameValuePairs.put("start", start);
            nameValuePairs.put("end", end);

            ServiceHandler jsonParser = new ServiceHandler();
            String result = jsonParser.makeServiceCall("addEvent.php", nameValuePairs);

            Log.d("***Response", "" + result);
            return result;
        }

        @Override
        protected void onPostExecute(String result){
            if (result == null){
                Log.e("***Data", "Didn't receive any data from server!");
                if(isAdded()) Toast.makeText(getActivity(),
                        "Server unreachable!", Toast.LENGTH_LONG).show();
            }
            else{
                if(isAdded()) Toast.makeText(getActivity(),
                        "Added!", Toast.LENGTH_SHORT).show();
            }
            if(loadingDialog.isShowing()) loadingDialog.dismiss();
        }
    }

    private class Participate extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String eventID = params[0];

            Map<String,String> nameValuePairs = new HashMap<>();
            nameValuePairs.put("uname", getArguments().getString("username"));
            nameValuePairs.put("event", eventID);

            ServiceHandler jsonParser = new ServiceHandler();
            String result = jsonParser.makeServiceCall("participate.php", nameValuePairs);

            Log.d("***Response", "" + result);
            return result;
        }

        @Override
        protected void onPostExecute(String result){
            if (result == null){
                Log.e("***Data", "Didn't receive any data from server!");
                if(isAdded()) Toast.makeText(getActivity(),
                        "Server unreachable!", Toast.LENGTH_LONG).show();
            }else{
                if(result.trim().equalsIgnoreCase("exists")){
                    if(isAdded()) Toast.makeText(getActivity(), "Already DONE!", Toast.LENGTH_SHORT).show();
                }else
                if(isAdded()) Toast.makeText(getActivity(), "Added!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class Search extends AsyncTask<Object, Void, String>{
        double dLat, dLon;
        PolylineOptions rectOptions;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(isAdded()) loadingDialog = ProgressDialog.show(getActivity(), "Search", "Looking for nearest events");
        }

        @Override
        protected String doInBackground(Object... params) {
            LatLng position = (LatLng) params[0];

            Map<String,String> nameValuePairs = new HashMap<>();
            //Position, decimal degrees
            double lat = position.latitude;
            double lng = position.longitude;

            //Earth’s radius, sphere
            long R=6378137;

            //offsets in meters
            double dn = 1000;
            double de = 1000;

            //Coordinate offsets in radians
            dLat = dn/R;
            dLon = de/(R*Math.cos(Math.PI*lat/180));
            //these values are in radiants, needs to be converted in decimal degrees
            double maxLat = lat+dLat*180/Math.PI;
            double minLat = lat-dLat*180/Math.PI;
            double maxLng = lng+dLon*180/Math.PI;
            double minLng = lng-dLon*180/Math.PI;

            nameValuePairs.put("maxLat", ""+maxLat);
            nameValuePairs.put("maxLng", ""+maxLng);
            nameValuePairs.put("minLat", ""+minLat);
            nameValuePairs.put("minLng", ""+minLng);

            Log.d("***lat",""+lat);
            Log.d("***lng",""+lng);

            Log.d("***dLat", ""+dLat);
            Log.d("***dLon", ""+dLon);

            Log.d("***maxLat", nameValuePairs.get("maxLat"));
            Log.d("***maxLng", nameValuePairs.get("maxLng"));
            Log.d("***minLat", nameValuePairs.get("minLat"));
            Log.d("***minLng", nameValuePairs.get("minLng"));

            // Instantiates a new Polyline object and adds points to define a rectangle
            rectOptions = new PolylineOptions()
                    .add(new LatLng(minLat, maxLng))
                    .add(new LatLng(maxLat, maxLng))  // North of the previous point, but at the same longitude
                    .add(new LatLng(maxLat, minLng))  // Same latitude, to the west
                    .add(new LatLng(minLat, minLng))  // Same longitude, to the south
                    .add(new LatLng(minLat, maxLng));  // Closes the polyline.

            ServiceHandler jsonParser = new ServiceHandler();
            String result = jsonParser.makeServiceCall("search.php", nameValuePairs);

            Log.i("***Response", "" + result);
            return result;
        }

        @Override
        protected void onPostExecute(String result){
            if (result == null){
                Log.e("***JSON Data","Didn't receive any data from server!");
                if(isAdded()) Toast.makeText(getActivity(),
                        "Server unreachable!", Toast.LENGTH_LONG).show();
            }
            else{
                mGoogleMap.addPolyline(rectOptions).setColor(0x7FFF0000); //colored red, half transparent

                // Parse the JSON input
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray events = jsonObj.getJSONArray("events");
                    mMarkers.clear();

                    for (int i = 0; i < events.length();i++) {
                        JSONObject eventObj = (JSONObject) events.get(i);
                        Marker mkr = mGoogleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(eventObj.getString("lat")),
                                        Double.parseDouble(eventObj.getString("lng"))))
                                .icon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                .title(eventObj.getString("descr"))
                                .snippet("Start: "+eventObj.getString("start")+
                                        "; End: "+eventObj.getString("end")));
                        mMarkers.put(mkr.getId(), eventObj.getString("id"));
                    }

                } catch (JSONException e) { e.printStackTrace(); }
            }
            if(loadingDialog.isShowing()) loadingDialog.dismiss();
        }
    }

    private class GetEvents extends AsyncTask<Void, Void, MyArrayAdapter> {
        MyArrayAdapter maa;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(isAdded()) loadingDialog = ProgressDialog.show(getActivity(), "Retrieve the event list", "Issuing the request...");
        }

        @Override
        protected MyArrayAdapter doInBackground(Void... args) {
            String uname = getArguments().getString("username");
            Map<String,String> nameValuePairs = new HashMap<>();
            nameValuePairs.put("uname", uname);

            ServiceHandler jsonParser = new ServiceHandler();
            String result = jsonParser.makeServiceCall("getEvents.php", nameValuePairs);
            Log.d("***GetEvents","result: "+result);

            if (result != null) {
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray jsonEvents = jsonObj.getJSONArray("events");
                    ArrayList<Event> events = new ArrayList<>();

                    for (int i = 0; i < jsonEvents.length();i++) {
                        JSONObject eventsObj = (JSONObject) jsonEvents.get(i);

                        Log.d("***Event","i: "+i);

                        Event e = new Event(
                                Integer.parseInt(eventsObj.getString("id")),
                                eventsObj.getString("descr"),
                                eventsObj.getString("start"),
                                eventsObj.getString("end"),
                                Double.parseDouble(eventsObj.getString("lat")),
                                Double.parseDouble(eventsObj.getString("lng")),
                                (eventsObj.getString("verified").equals("1")));
                        Log.d("***Event","id: "+e.getId());
                        Log.d("***Event","descr: "+e.getDescription());
                        Log.d("***Event","start: "+e.getStart());
                        Log.d("***Event","end: "+e.getEnd());
                        Log.d("***Event","lat: "+e.getLat());
                        Log.d("***Event","lng: "+e.getLng());
                        Log.d("***Event","ver: "+e.isVerified() + " (" + eventsObj.getString("verified") + ")");

                        events.add(e);
                    }
                    Log.d("***events",""+events);
                    Log.d("***events",""+getActivity());
                    if(isAdded()) maa = new MyArrayAdapter(getActivity(), events);

                } catch (JSONException e) { e.printStackTrace(); }
            } else Log.e("***JSON Data","Didn't receive any data from server!");

            return maa;
        }

        @Override
        protected void onPostExecute(MyArrayAdapter result){
            if(isAdded()) if(loadingDialog.isShowing()) loadingDialog.dismiss();
            Log.d("***Event","setAdapter: "+result);
            if(result!=null){
                result.sort(new Comparator<Event>() {
                    @Override
                    public int compare(Event e1, Event e2) {
                        return e1.getStart().compareTo(e2.getStart());
                    }
                });
                lv.setAdapter(result);
            }
        }
    }

    private class UpdateEvent extends AsyncTask<Object, Void, String> {
        final Event event;
        double dLat, dLon;

        UpdateEvent(Event e){
            this.event = e;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Object... params) {
            LatLng position = (LatLng) params[0];

            Map<String,String> nameValuePairs = new HashMap<>();
            //Position, decimal degrees
            double lat = position.latitude;
            double lng = position.longitude;

            //Earth’s radius, sphere
            long R=6378137;

            //offsets in meters
            double dn = 1000;
            double de = 1000;

            //Coordinate offsets in radians
            dLat = dn/R;
            dLon = de/(R*Math.cos(Math.PI*lat/180));
            //these values are in radiants, needs to be converted in decimal degrees
            double maxLat = lat+dLat*180/Math.PI;
            double minLat = lat-dLat*180/Math.PI;
            double maxLng = lng+dLon*180/Math.PI;
            double minLng = lng-dLon*180/Math.PI;

            nameValuePairs.put("uname", getArguments().getString("username"));
            nameValuePairs.put("event", ""+event.getId());
            nameValuePairs.put("maxLat", ""+maxLat);
            nameValuePairs.put("maxLng", ""+maxLng);
            nameValuePairs.put("minLat", ""+minLat);
            nameValuePairs.put("minLng", ""+minLng);

            Log.d("***lat",""+lat);
            Log.d("***lng",""+lng);

            Log.d("***dLat", ""+dLat);
            Log.d("***dLon", ""+dLon);

            Log.d("***maxLat", nameValuePairs.get("maxLat"));
            Log.d("***maxLng", nameValuePairs.get("maxLng"));
            Log.d("***minLat", nameValuePairs.get("minLat"));
            Log.d("***minLng", nameValuePairs.get("minLng"));

            ServiceHandler jsonParser = new ServiceHandler();
            String result = jsonParser.makeServiceCall("updateEvent.php", nameValuePairs);

            Log.d("***Response", "" + result);
            return result;
        }

        @Override
        protected void onPostExecute(String result){
            if (result == null){
                Log.e("***Data", "Didn't receive any data from server!");
                if(isAdded()) Toast.makeText(getActivity(),
                        "Server unreachable!", Toast.LENGTH_LONG).show();
            }else{
                if(result.trim().equalsIgnoreCase("exit"))
                    if(isAdded()) Toast.makeText(getActivity(),
                            "Too far from the event \""+event.getDescription()+"\"",
                            Toast.LENGTH_SHORT).show();
                else {
                    if (isAdded()) Toast.makeText(getActivity(),
                            "Updated!", Toast.LENGTH_SHORT).show();
                    //... refresh fragment
                }
            }
        }
    }
}