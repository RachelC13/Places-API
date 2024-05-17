package com.example.placesapi2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.libraries.places.api.model.Place;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlacesAdapter extends ArrayAdapter<Place> {

    private Context mContext;
    private List<Place> mPlaces;

    public PlacesAdapter(Context context, List<Place> places) {
        super(context, 0, places);
        mContext = context;
        mPlaces = places;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        }

        Place currentPlace = mPlaces.get(position);

        TextView nameTextView = listItemView.findViewById(R.id.name_text_view);
        if (currentPlace.getName() != null ) {
            nameTextView.setText(currentPlace.getName());
        } else {
            nameTextView.setText("");
        }

        TextView addressTextView = listItemView.findViewById(R.id.address_text_view);
        if (currentPlace.getAddress() != null) {
            addressTextView.setText(currentPlace.getAddress());
        } else {
            addressTextView.setText("");
        }

        ImageView photoImageView = listItemView.findViewById(R.id.photo_image_view);
        if (currentPlace.getIconUrl() != null) {
            Picasso.get().load(currentPlace.getIconUrl()).into(photoImageView);
            photoImageView.setVisibility(View.VISIBLE);
        } else {
            photoImageView.setVisibility(View.GONE);
        }

        return listItemView;
    }
}
