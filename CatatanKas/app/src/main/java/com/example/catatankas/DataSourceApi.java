package com.example.catatankas;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.http.GET;

public class DataSourceApi {
    public static final String TAG = DataSourceApi.class.getSimpleName();
    private Context context;
    private RequestQueue requestQueue;

    public DataSourceApi(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    public void getTransaksis(GetTransaksisCallback callback){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("script.google.com")
                .appendEncodedPath("macros/s/AKfycbxNEp6XogMxNFDO4Ng7jKk0RBWLMGsDzW2nEHjupRx23JytegPfIIGmQGmjdCb-YL0d/exec")
                .appendQueryParameter("action", "read");

        String url = builder.build().toString();

        Log.i(TAG, "getTransaksis: APICall: " + url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            String dataString = jsonObject.toString();
                            Log.i(TAG, "onResponse: " + dataString);
                            GetTransaksiResultBody getTransaksiResult
                                    = (new Gson()).fromJson(dataString, GetTransaksiResultBody.class);

                            getTransaksiResult.records.sort(new Comparator<TransaksiModel>() {
                                @Override
                                public int compare(TransaksiModel o1, TransaksiModel o2) {
                                    if(o1.getTime() < o2.getTime()) return -1;
                                    else if(o1.getTime() == o2.getTime()) return 0;
                                    else if(o1.getTime() > o2.getTime()) return 1;
                                    return 0;
                                }
                            });

                            Collections.reverse(getTransaksiResult.records);

                            callback.onSuccess(getTransaksiResult.getRecords());
                        }catch (Throwable t){
                            callback.onError(t);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e(TAG, "onErrorResponse: ", volleyError);
                        callback.onError(volleyError);
                    }
                }
        );

        requestQueue.cancelAll(Objects::nonNull);
        requestQueue.add(jsonObjectRequest);
    }

    public void addTransaksi(TransaksiModel transaksi, AddTransaksiCallback callback){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("script.google.com")
                .appendEncodedPath("macros/s/AKfycbxNEp6XogMxNFDO4Ng7jKk0RBWLMGsDzW2nEHjupRx23JytegPfIIGmQGmjdCb-YL0d/exec")
                .appendQueryParameter("action", "insert");
        builder
                .appendQueryParameter("title", transaksi.getTitle())
                .appendQueryParameter("description", transaksi.getDescription())
                .appendQueryParameter("amount", String.valueOf(transaksi.getAmount()))
                .appendQueryParameter("type", String.valueOf(transaksi.getTipeTransaksi()));
        String url = builder.build().toString();

        Log.i(TAG, "addTransaksis: APICall: " + url);

        JsonObjectRequest addTransaksiRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        callback.onSuccess();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        callback.onError(volleyError);
                    }
                }
        );
        requestQueue.cancelAll(Objects::nonNull);
        requestQueue.add(addTransaksiRequest);
    }

    public void deleteTransaksis(DeleteTransaksisCallback callback){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("script.google.com")
                .appendEncodedPath("macros/s/AKfycbxNEp6XogMxNFDO4Ng7jKk0RBWLMGsDzW2nEHjupRx23JytegPfIIGmQGmjdCb-YL0d/exec")
                .appendQueryParameter("action", "delete");

        String url = builder.build().toString();
        Log.i(TAG, "deleteTransaksis: APICall: " + url);

        JsonObjectRequest deleteTransaksisRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        callback.onSuccess();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        callback.onError(volleyError);
                    }
                }
        );

        requestQueue.cancelAll(Objects::nonNull);
        requestQueue.add(deleteTransaksisRequest);
    }

    public interface GetTransaksisCallback{
        void onSuccess(List<TransaksiModel> transaksis);
        void onError(Throwable t);
    }
    public class GetTransaksiResultBody{
        private List<TransaksiModel> records;

        public List<TransaksiModel> getRecords() {
            return records;
        }
    }
    public interface AddTransaksiCallback{
        void onSuccess();
        void onError(Throwable t);
    }

    public interface DeleteTransaksisCallback{
        void onSuccess();
        void onError(Throwable t);
    }


}
