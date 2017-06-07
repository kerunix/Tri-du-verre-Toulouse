package com.example.keru.opendatatlse;

import android.util.Log;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;


public class RecupRequestModel extends GoogleHttpClientSpiceRequest<RecupVerreModel>{

    public static final String TAG = "RecupRequestModel";
    private String url;
    private Double lat;
    private Double lon;
    private int radius;

    public RecupRequestModel (Double lat, Double lon, int radius) {
        super( RecupVerreModel.class);
        this.lat = lat;
        this.lon = lon;
        this.radius = radius;
        this.url = String.format("https://data.toulouse-metropole.fr/api/records/1.0/search/?dataset=recup-verre&rows=50&geofilter.distance=%f,%f,%d",
                lat,
                lon,
                radius);

    }


    @Override
    public RecupVerreModel loadDataFromNetwork() throws Exception {

        Log.d(TAG, "Calling WebService" + url);

        HttpRequest request = getHttpRequestFactory()
                .buildGetRequest(new GenericUrl(url));

        request.setParser(new JacksonFactory().createJsonObjectParser());
        return request.execute().parseAs(getResultType());
    }
}
