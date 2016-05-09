package com.example.user.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

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
            fis.read(buffer, 0 , buffer.length);
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
}
