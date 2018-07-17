package com.dev.jhonyrg.fragmentsapp.utils;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dev.jhonyrg.fragmentsapp.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiCaller extends AsyncTask<Void, Integer, String> {
    private static final String TAG = "ApiCaller";
    private boolean isPost;
    private String url;
    private int statusCode;
    private OnApiCallFinish listener;
    private int requestId;
    private HashMap<String, String> values;
    private Context contextActivity;
    private MaterialDialog dialog;

    public ApiCaller() {
        this.isPost = false;
        this.url = "";
        this.requestId = 0;
        this.statusCode = 0;
        this.values = new HashMap<>();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (this.contextActivity != null)
        {
             this.dialog = new MaterialDialog.Builder(this.contextActivity)
                    .title(R.string.dlgTitle)
                    .content(R.string.dlgContent)
                    .progress(true, 0)
                    .show();
        }
    }

    @Override
    protected String doInBackground(Void... voids) {
        //verify if is a POST
        if(isPost)
        {
            //verify if url is not empty
            if(!this.url.equals(""))
            {
                Request request;
                //verify exist key-values to POST
                 if(!values.isEmpty())
                 {
                     //create a builder with key-values
                     FormBody.Builder builderBody = new FormBody.Builder();

                     //add key-values to builder
                     for (Map.Entry<String, String> pair:
                             values.entrySet()) {
                         builderBody.add(pair.getKey(), pair.getValue());
                     }

                     //create body POST with key-values that contain the builder
                     RequestBody body = builderBody.build();

                     //Execute POST
                     request = new Request.Builder().url(this.url).post(body).build();
                 }
                 else
                 {
                     request = new Request.Builder().delete().url(this.url).build();
                 }

                //Process the response of service
                return onResponse(request);
            }
            return "";
        }
        else
        {
            Request.Builder requestBuilder = new Request.Builder();

            //Evaluate conditions
            if(!this.url.equals(""))
            {
                requestBuilder.url(this.url);

                //Execute call at service
                Request request = requestBuilder.build();

                //Process response of service
                return onResponse(request);
            }
            return "";
        }
    }

    @Override
    protected void onPostExecute(String content) {
        super.onPostExecute(content);
        if(this.dialog != null)
        {
            this.dialog.dismiss();
        }

        if(this.listener == null)
        {
            return;
        }

        if(this.statusCode >= 200 && this.statusCode < 300)
        {
            this.listener.onSuccess(this.requestId, content);
        }
        else
        {
            this.listener.onError(this.requestId, this.statusCode);
        }
    }

    private String onResponse(Request request)
    {
        OkHttpClient client = new OkHttpClient();
        try
        {
            Response response = client.newCall(request).execute();
            this.statusCode = response.code();
            if(this.statusCode >= 200 && statusCode < 300)
            {
                if(response.body() != null)
                {
                    return response.body().string();
                }
                else
                {
                    return "";
                }
            }
            else
            {
                return "";
            }
        }
        catch (IOException e)
        {
            if(!e.getMessage().isEmpty())
            {
                Log.e(TAG, e.getMessage());
            }
            else
            {
                Log.e(TAG, "TIMEOUT");
                this.statusCode = 408;
            }

        }
        return "";
    }



    public void setUrl(String url) {
        this.url = url;
    }

    public void setOnApiCallFinish(OnApiCallFinish listener) {
        this.listener = listener;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public void setPost(boolean post) {
        isPost = post;
    }

    public void setValues(HashMap<String, String> values) {
        this.values = values;
    }

    public void setContextActivity(Activity contextActivity) {
        this.contextActivity = contextActivity;
    }
}
