package com.example.placesapi2;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private PlacesClient mPlacesClient;
    private AutoCompleteTextView mAutoCompleteTextView;
    private Spinner mTypeSpinner;
    private Button mSearchButton;
    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    private ListView mPlacesListView;
    private Object Task;
    private boolean task;
    private PlaceLikelihood fetchResponse;
    private Object attributions;
    private String placeId;
    private String constructedPhotoUrl;
    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAutoCompleteTextView = findViewById(R.id.autocomplete);
        mTypeSpinner = findViewById(R.id.type);
        mSearchButton = findViewById(R.id.search_button);
        mPlacesListView = findViewById(R.id.places_list);


        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        mPlacesClient = Places.createClient(this);

        // Set up the type spinner
        String[] types = {"school", "restaurant", "gym", "supermarket", "university", "library", "park"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        mTypeSpinner.setAdapter(typeAdapter);

        // Set up the search button click listener
        mSearchButton.setOnClickListener(v -> {
            String query = mAutoCompleteTextView.getText().toString();
            String type = mTypeSpinner.getSelectedItem().toString();
            searchNearbyPlaces(query,type);
        });

        // Initialize the map fragment
        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(googleMap -> mMap = googleMap);
    }

    // Helper method to perform the Places API search
    private void searchNearbyPlaces(String query, String type) {
        RectangularBounds bounds = RectangularBounds.newInstance(new LatLng(-33.880490, 151.199814), new LatLng(-33.858754, 151.229502));
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .setLocationRestriction(bounds)
                .setTypeFilter(TypeFilter.valueOf(type.toUpperCase()))
                .build();

        mPlacesClient.findAutocompletePredictions(request).addOnSuccessListener(response -> {

            List<Place> placeList = new ArrayList<>();
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                prediction.getPlaceId();
                Place place = fetchResponse.getPlace();
                String name = place.getName();
                String address = place.getAddress();
                LatLng latLng = place.getLatLng();
                String photoUrl = null;
                if(place.getPhotoMetadatas() != null && place.getPhotoMetadatas().isEmpty()) {
                    photoUrl = getIconUrl(place.getPhotoMetadatas().get(0).getAttributions());
                }

            }
            displayPlaces(placeList);
        }).addOnFailureListener(exception -> {
            Log.e("MainActivity", "Error getting autocomplete predictions", exception);
        });

    }

    private String getIconUrl(String attributions) {
        String requestUrl = "https://maps.googleapis.com/maps/api/place/details/json?"
                + "place_id" + placeId
                + "&key=" + "AIzaSyASQbEAMJknhJ46y2D5bvpJ3yL2Il1iyfM";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(requestUrl)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String jsonResponse = response.body().string();
                return constructedPhotoUrl;
            } else {
                Log.e(TAG, "Failed to fetch place details: " + response.code());
                return null;
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    // Helper method to display the search results
    private void displayPlaces(List<Place> places) {
        // Clear the previous results
        mPlacesListView.setAdapter(null);

        // Create a new adapter to display the search results
        PlacesAdapter adapter = new PlacesAdapter(this, places);
        mPlacesListView.setAdapter(adapter);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Handle the connection failure
    }
}