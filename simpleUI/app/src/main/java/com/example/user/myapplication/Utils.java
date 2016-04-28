package com.example.user.myapplication;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
}
