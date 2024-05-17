package com.example.placesapi2;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;

import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.List;

public class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

    private Context mContext;
    private PlacesClient mPlacesClient;
    private int mResource;
    private List<String> mResults;

    public PlacesAutoCompleteAdapter(Context context, int resource, PlacesClient placesClient) {
        super(context, resource);
        mContext = context;
        mResource = resource;
        mPlacesClient = placesClient;
        mResults = new ArrayList<>();
    }

    @Override
    public int getCount() {

        return mResults.size();
    }

    @Override
    public String getItem(int position) {

        return mResults.get(position);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<String> suggestions = new ArrayList<>();

                // Perform the search for autocomplete suggestions
                FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                        .setSessionToken(AutocompleteSessionToken.newInstance())
                        .setQuery(constraint.toString())
                        .build();

                mPlacesClient.findAutocompletePredictions(request).addOnSuccessListener(response -> {
                    for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                        suggestions.add(prediction.getFullText(null).toString());
                    }
                    results.values = suggestions;
                    results.count = suggestions.size();
                    publishResults(constraint, results);
                }).addOnFailureListener(exception -> {
                    // Handle error
                });



                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    mResults = (List<String>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}
