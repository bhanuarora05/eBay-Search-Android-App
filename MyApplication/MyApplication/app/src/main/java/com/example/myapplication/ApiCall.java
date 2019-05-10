package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.URLEncoder;

public class ApiCall {
   //private static final String host= "http://10.26.28.251:8081/";
   private static final String host="http://Webdev-env.yy6wtizp3e.us-east-2.elasticbeanstalk.com/";
    private static ApiCall mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;
    private static SharedPreferences localstorage;
    private static SharedPreferences.Editor handler;


    public ApiCall(Context ctx) {
        mCtx = ctx;
        mRequestQueue = getRequestQueue();
        localstorage = ctx.getApplicationContext().getSharedPreferences( "Webtech_HW9_localstorage",Context.MODE_PRIVATE);
        handler = localstorage.edit();

    }

    public static SharedPreferences getLocalstorage() {
        return localstorage;
    }

    public static SharedPreferences.Editor getHandler() {
        return handler;
    }

    public static synchronized ApiCall getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ApiCall(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public static void make(Context ctx, String query, Response.Listener<String>
            listener, Response.ErrorListener errorListener) {
        String url = host + "zipcode?startswith=" + query;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                listener, errorListener);
        ApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
    }

    public static void zipcode(Context ctx, Response.Listener<String>
            listener, Response.ErrorListener errorListener) {
        String url = "http://ip-api.com/json";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                listener, errorListener);
        ApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
    }

    public static void ebayresults(Context ctx,String keyword,String category,String otherdist,String ziphere,String inputdist,String freeshipping,String local,String New,String used,String unspecified,String enablenearby,Response.Listener<String>
            listener, Response.ErrorListener errorListener) {
        String url="";
        try {
            url = host + "callapi?keyword=" + URLEncoder.encode(keyword, "UTF-8") + "&category=" + category + "&new=" + New + "&used=" + used + "&unspecified=" + unspecified + "&local=" + local + "&freeshipping=" + freeshipping + "&inputdist=" + URLEncoder.encode(inputdist, "UTF-8") + "&otherdist=" + URLEncoder.encode(otherdist, "UTF-8") + "&ziphere=" + ziphere + "&enablenearby=" + enablenearby;
        }
        catch(Exception e){}
        //Log.i("Bhanu",url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                listener, errorListener);
        ApiCall.getInstance(ctx).addToRequestQueue(stringRequest);

    }

    public static void fetchprodetails(Context ctx,String prodtitle,String productId,Response.Listener<String>
            listener, Response.ErrorListener errorListener) {
        String url="";
        try {
            url = host + "prodetails?prodtitle=" + URLEncoder.encode(prodtitle, "UTF-8") + "&productId=" + productId ;
            //Log.i("Bhanu",url);
        }
        catch(Exception e){}
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                listener, errorListener);
        ApiCall.getInstance(ctx).addToRequestQueue(stringRequest);

    }
}
