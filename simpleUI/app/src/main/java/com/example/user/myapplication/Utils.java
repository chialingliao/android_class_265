package com.example.user.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by user on 2016/4/28.
 */
public class Utils {
    public static  void writeFile(Context context, String fileName, String content){

        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_APPEND);//.MODE_APPEND 檔案累加 PRIVATE 當前
            fos.write(content.getBytes());//寫檔
            fos.close();//寫完記得關起來
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static  String readFile(Context context, String fileName){
        //驗證資料 故須讀檔
        try {
            FileInputStream fis = context.openFileInput(fileName);
            byte[] buffer = new byte[1024];
            fis.read(buffer, 0, buffer.length);
            fis.close();
            return new String(buffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Uri getPhotoURI(){
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);//放照片的資料夾
        if(dir.exists() == false){
            dir.mkdir();
        }

        File file = new File(dir, "simpleUI_photo.png");//檔案名稱
        return Uri.fromFile(file);
    }

    public  static byte[] uriToBytes(Context context, Uri uri){
        try {//二維讀取
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int len = 0;
            while((len = inputStream.read(buffer)) != -1){
                byteArrayOutputStream.write(buffer);

            }
            return byteArrayOutputStream.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public  static byte[] urlToBytes(String urlString){
        try {

            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            InputStream inputStream = connection.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int len = 0;
            while((len = inputStream.read(buffer)) != -1){
                byteArrayOutputStream.write(buffer,0,len);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public static String getGeoCodingUrl(String address){
        try {
            address = URLEncoder.encode(address, "utf-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = "http://maps.google.com/maps/api/geocode/json?address="+address;
        return  url;
    }

    public  static  double[] addressToLatLng(String address){
        String url = Utils.getGeoCodingUrl(address);
        byte[] bytes = Utils.urlToBytes(url);
        if(bytes !=null){
            String result = new String(bytes);
            try {
                JSONObject object = new JSONObject(result);
                if(object.getString("status").equals("OK")) {
                    JSONObject location = object.getJSONArray("results")
                            .getJSONObject(0)
                            .getJSONObject("geometry")
                            .getJSONObject("location");
                    double lat = location.getDouble("lat");
                    double lng = location.getDouble("lng");
                    return  new double[]{lat, lng};
                }
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
        return  null;
    }

    public static Bitmap getStaticMap(double[] latlng){
        String center = latlng[0] + "," + latlng[1];
        String staticMapUrl =  "http://maps.googleapis.com/maps/api/staticmap?center=" + center + "&zoom=17&size=640x400&maptype=roadmap";
        byte[] bytes = Utils.urlToBytes(staticMapUrl);
        if(bytes !=null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            return bitmap;
        }
        return null;
    }

}
