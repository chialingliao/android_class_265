package com.example.user.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderDetailActivity extends AppCompatActivity {

    TextView note;
    TextView storeInfo;
    TextView menuResults;
    ImageView photo;
    ImageView mapImageView;

    String storeName = "";
    String address = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        note = (TextView)findViewById(R.id.note);
        storeInfo = (TextView) findViewById(R.id.storeInfo);
        menuResults = (TextView)findViewById(R.id.menuResults);
        photo = (ImageView)findViewById(R.id.photoImageView);
        mapImageView = (ImageView)findViewById(R.id.mapImageView);

        Intent intent = getIntent();
        note.setText(intent.getStringExtra("note"));
        storeInfo.setText(intent.getStringExtra("storeInfo"));

        String[] info = intent.getStringExtra("storeInfo").split(",");

        storeName = info[0];
        address = info[1];

        String results = intent.getStringExtra("menuResults");
        String text = "";
        try {
            JSONArray jsonArray = new JSONArray(results);
            for(int i = 0 ;i< jsonArray.length() ; i++){
                JSONObject object = jsonArray.getJSONObject(i);
                text += object.getString("name") + "：大杯" + object.getString("l") + "杯 中杯" + object.getString("m")+" 杯\n" ;

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        menuResults.setText(text);

        if(!intent.getStringExtra("photoURL").equals("")) {
        //    Picasso.with(this).load(intent.getStringExtra("photoURL")).into(photo);

           (new ImageLoadingTask(photo)).execute(intent.getStringExtra("photoURL"));
        //    (new GeoCodingTask(photo)).execute("台北縣汐止區大同路一段369號");

        }
        (new GeoCodingTask(mapImageView)).execute(address);
        //匿名函式 檔案即使已關閉  其實還是綁住資源 所以可從thread看是否已存在
     /*   for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true){
                        SystemClock.sleep(1000);
                    }
                }
            });

        }*/

    }

    private  static  class GeoCodingTask extends AsyncTask<String, Void, Bitmap>{
        ImageView imageView;
        @Override
        protected Bitmap doInBackground(String... params) {
            String address = params[0];
            double[] latlng = Utils.addressToLatLng(address);
            return Utils.getStaticMap(latlng);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
        }
        public GeoCodingTask(ImageView imageView){this.imageView = imageView;}
    }


    //屬於自己的記憶體
    private static class ImageLoadingTask extends AsyncTask<String, Void, Bitmap>{
        ImageView imageView;
        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            byte[] bytes = Utils.urlToBytes(url);
            if(bytes!=null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                return bitmap;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            if(bitmap != null){
                imageView.setImageBitmap(bitmap);
            }
        }

        public ImageLoadingTask(ImageView imageView){this.imageView = imageView;}
    }

}
