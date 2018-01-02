package com.milab.ido.timezoneapp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ido on 1/2/18.
 */

class TimeFetcher {
    private RequestQueue _queue;
    private final static String REQUEST_URL = "https://intense-plateau-61675.herokuapp.com/";

    public TimeFetcher(Context context) {
        _queue = Volley.newRequestQueue(context);
    }

    public interface TimeResponseListener {
        void onResponse(TimeFetcher.TimeResponse response);
    }

    public class TimeResponse {
        public boolean isError;
        public String time;

        public TimeResponse(boolean isError, String time) {
            this.isError = isError;
            this.time = time;
        }
    }

    private TimeResponse createErrorResponse() {
        return new TimeResponse(true, null);
    }

    public void dispatchRequest(boolean useOffset, int timeZone, final TimeResponseListener listener) {
        JSONObject requestObject = new JSONObject();
        try {
            if(useOffset) {
                requestObject.put("timezone", timeZone);
            }
        }
        catch (JSONException e) {}

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, REQUEST_URL, requestObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            TimeResponse res = new TimeResponse(false,
                                    response.getString("time"));
                            listener.onResponse(res);
                        }
                        catch (JSONException e) {
                            listener.onResponse(createErrorResponse());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onResponse(createErrorResponse());
            }
        });

        _queue.add(req);
    }

}
