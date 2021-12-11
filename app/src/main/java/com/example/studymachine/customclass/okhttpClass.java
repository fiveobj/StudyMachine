package com.example.studymachine.customclass;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class okhttpClass {
    private static OkHttpClient okHttpClient= new OkHttpClient.Builder().connectTimeout(8000, TimeUnit.MILLISECONDS).build();
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


    public String searchVideo(String typeStr){
        Request.Builder builder=new Request.Builder().url("http://192.168.31.95:8080/video/getVideoByKeyword"+typeStr);
        builder.method("GET",null);
        //builder.addHeader("courseId",courseid);
        Request request=builder.build();
        try(Response response=okHttpClient.newCall(request).execute()){
            if(response.isSuccessful()){
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("searchVideo-e",e.toString());
        }
        return "FW";
    }


    public String getViode(String typeName) {


        Request.Builder builder=new Request.Builder().url("http://192.168.31.95:8080/video/getVideoByTypeName?typeName="+typeName);
        builder.method("GET",null);
        //builder.addHeader("courseId",courseid);
        Request request=builder.build();
        Call call=okHttpClient.newCall(request);
        final boolean[] issuccessful = {false};

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("getViode-e",e.toString());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                string=response.body().string();
                mdata.getData(string);
                //aBoolean=true;

            }
        });
        //str[0]=call.execute().body().string();
        //Log.d("issuccessful", issuccessful[0]==true?"true":"false");



        /*try(Response response=okHttpClient.newCall(request).execute()){
            if(response.isSuccessful()){
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("getViode-e",e.toString());
        }*/
        //if (issuccessful[0]==true)return str[0];

    //if (aBoolean==true)return string;
        return string;
    }

    public interface data{
        String getData(String str);
    }

    public okhttpClass setData(data data){
        mdata=data;
        return null;
    }

}
