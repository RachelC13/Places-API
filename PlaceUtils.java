package com.example.placesapi2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlaceUtils {

    public static Place createPlaceFromJson(JSONObject placeJson) throws JSONException {
        String name = placeJson.getString("name");
        String address = placeJson.getString("formatted_address");

        // Check if the place has photos
        String photoUrl = null;
        if (placeJson.has("photos")) {
            JSONArray photos = placeJson.getJSONArray("photos");
            if (photos.length() > 0) {
                JSONObject photo = photos.getJSONObject(0); // Assuming you're interested in the first photo
                String photoReference = photo.getString("photo_reference");
                photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photoReference + "&key=AIzaSyASQbEAMJknhJ46y2D5bvpJ3yL2Il1iyfM";
            }
        }

        return new Place();
    }
}

