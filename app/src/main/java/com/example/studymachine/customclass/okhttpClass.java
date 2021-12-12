package com.example.studymachine.customclass;

import android.os.Looper;
import android.util.Log;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class okhttpClass {
    private static OkHttpClient okHttpClient= new OkHttpClient.Builder().connectTimeout(160000, TimeUnit.MILLISECONDS).build();
    private String string="null";
    //private boolean aBoolean=false;
    public okhttpClass(){}
    data mdata;


//------------------------------post------------------------------------------------------------
// ------------------------------get------------------------------------------------------------
public String getViodeType(){
    Request.Builder builder=new Request.Builder().url("http://192.168.31.95:8080/video/getVideoTypeBySId?sID=1");
    builder.method("GET",null);

    Request request=builder.build();
    try(Response response=okHttpClient.newCall(request).execute()){
        if(response.isSuccessful()){
            return response.body().string();
        }
    } catch (IOException e) {
        e.printStackTrace();
        Log.d("getVideoType-e",e.toString());
    }
    return "FW";
}





    public String getViode(String typeName) {

        Log.d("getVIdeo-1:","11111");
        Request.Builder builder=new Request.Builder().url("http://192.168.31.95:8080/video/getVideoByTypeName?typeName="+typeName);
        Log.d("getVIdeo-2:","11111");
        builder.method("GET",null);
        //builder.addHeader("courseId",courseid);
        Request request=builder.build();
        Log.d("getVIdeo-4:",request.toString());
        okHttpClient.dispatcher().setMaxRequestsPerHost(9);
        Call call=okHttpClient.newCall(request);
        final boolean[] issuccessful = {false};
        Log.d("getVIdeo-3:",call.toString());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("getViode-e",e.toString());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                Looper.prepare();
                string=response.body().string();
                Log.d("getVIdeo-5:",call.toString());
                mdata.getData(string);
                Looper.loop();
                Log.d("getVideo-6", response.body().string());
                //aBoolean=true;

            }
        });

        return string;
    }

    public interface data{
        String getData(String str);
    }

    public okhttpClass setData(data data){
        mdata=data;
        return null;
    }

    public String searchVideo(String keyword){
        FormBody.Builder builder=new FormBody.Builder();
        RequestBody requestBody=builder.add("keyword",keyword).build();
        Request request=new Request.Builder().url("http://192.168.31.95:8080/video/getVideoByKeyword").post(requestBody).build();
        try (Response response=okHttpClient.newCall(request).execute()){
            if(response.isSuccessful()){
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Log.d("cookic",cookieStore.toString());
        return "FW";
    }

}
