package com.hultron.photogallery;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import gson.Flickr;
import gson.Photo;

public class FlickrFetchr {

    private static final String TAG = "FlickrFetchr";

    private static final String API_KEY = "35c040301e6cb83f3d9701e43a936c94";
    private static final String FETCH_RECENTS_METHOD = "flickr.photos.getRecent";
    private static final String SEARCH_METHOD = "flickr.photos.search";

    private static final Uri ENDPOINT = Uri.parse("https://api.flickr.com/services/rest/")
            .buildUpon()
            .appendQueryParameter("api_key", API_KEY)
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1")
            .appendQueryParameter("extras", "url_s")
            .build();

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ":with " + urlSpec);
            }
            int bytesRead = 0;
            byte[] b = new byte[1024];
            while ((bytesRead = in.read(b)) != -1) {
                out.write(b, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public List<GalleryItem> fetchRecentPhotos() {
        String url = buildUrl(FETCH_RECENTS_METHOD, null);
        return downloadGalleryItems(url);
    }

    public List<GalleryItem> searchPhotos(String query) {
        String url = buildUrl(SEARCH_METHOD, query);
        return downloadGalleryItems(url);
    }

    public List<GalleryItem> downloadGalleryItems(String url) {

        List<GalleryItem> items = new ArrayList<>();
        try {
            String jsonString = getUrlString(url);
            Log.d(TAG, "Received JSON: " + jsonString);
            //JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(items, jsonString);
        } catch (IOException e) {
            Log.e(TAG, "Failed to fetch items", e);
        } catch (JSONException e) {
            Log.e(TAG, "Failed to parse JSON", e);
        }
        return items;
    }

    private String buildUrl(String method, String query) {
        Uri.Builder uriBuilder = ENDPOINT.buildUpon()
                .appendQueryParameter("method", method);

        if (method.equals(SEARCH_METHOD)) {
            uriBuilder.appendQueryParameter("text", query);
        }

        return uriBuilder.build().toString();
    }


    private void parseItems(List<GalleryItem> items, String jsonBody) throws IOException,
            JSONException {

        Gson gson = new Gson();
        Flickr flickr = gson.fromJson(jsonBody, Flickr.class);
        for (Photo p : flickr.photos.photo) {
            GalleryItem item = new GalleryItem();
            item.setId(p.id);
            item.setCaption(p.title);
            if (p.url_s == null) {
                continue;
            }
            item.setUrl(p.url_s);
            item.setOwner(p.owner);
            items.add(item);
        }

        //        JSONObject photosJsonObject = jsonBody.getJSONObject("photos");
        //        JSONArray photoJsonArray = photosJsonObject.getJSONArray("photo");

        //        for (int i = 0; i < photoJsonArray.length(); i++) {
        //            JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);
        //            GalleryItem item = new GalleryItem();
        //            item.setId(photoJsonObject.getString("id"));
        //            item.setCaption(photoJsonObject.getString("title"));

        //            if (!photoJsonObject.has("url_s")) {
        //                continue;
        //            }

        //            item.setUrl(photoJsonObject.getString("url_s"));
        //            items.add(item);
        //        }
    }
}
