package com.example.user.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;
import android.Manifest;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_MENU_ACTIVITY = 0;
    private static final int REQUEST_CODE_CAMERA_ACTIVITY = 1;

    private boolean hasPhoto = false;
    TextView textView;//宣告
    EditText editText;
    RadioGroup radioGroup;
    CheckBox checkBox;
    List<Order> orders;
    String note = "";
    String drinkName;
    ListView listView;
    Spinner spinner;
    String menuResults = "";
    ProgressBar progressBar;
    ProgressDialog progressDialog;
    ImageView photoImageView;

    //存入記憶體 若大量存取及寫入會爆炸
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    Realm realm;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("debug", "Main Activity OnCreate");

 /*
        HW2
        ParseObject testObject = new ParseObject("HomeworkParse");//CLASS名稱
        testObject.put("sid", "廖佳玲");//欄位 對應值
        testObject.put("email", "green730304@yahoo.com.tw");//欄位 對應值
        testObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e!=null) {
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "save success", Toast.LENGTH_SHORT).show();
                }
            }
        });
*/

        textView = (TextView) findViewById(R.id.textView);//去找VIEW ,須轉型態
        editText = (EditText) findViewById(R.id.editText);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        checkBox = (CheckBox) findViewById(R.id.hideCheckBox);
        listView = (ListView) findViewById(R.id.listView);
        orders = new ArrayList<>();
        spinner = (Spinner) findViewById(R.id.spinner);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        photoImageView = (ImageView)findViewById(R.id.imageView);
        progressDialog = new ProgressDialog(this);//圖片放置
        sp = getSharedPreferences("setting", Context.MODE_PRIVATE);//你要拿setting 裡的東西
        editor = sp.edit();//拿出setting裡某特殊內容 寫入

        // Create a RealmConfiguration which is to locate Realm file in package's "files" directory.
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfig);//Default
        // Get a Realm instance for this thread
       // realm = Realm.getInstance(realmConfig);
        realm = Realm.getDefaultInstance();
        editText.setText(sp.getString("editText", ""));//下次進來可直接取得 但二變數是預設值 無值顯示空字串

        editText.setOnKeyListener(new View.OnKeyListener() {//ENTER 等同BTN效果
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String text = editText.getText().toString();//拿到前一次的內容
                editor.putString("editText", text);
                editor.apply();//必寫才能運作
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    click(v);
                    return true;//攔截ENTER
                }
                return false;
            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {//虛擬鍵盤有作用　
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {//須將SINGLELINE打勾才能有反應
                    click(v);
                    return true;
                }
                return false;
            }
        });
        //       radioGroup.check(sp.getInt("radioGroup",R.id.blackteaRadioButton));//預設要抓ID  儲存radioGroup 的狀態
//        int checkedId = sp.getInt("radioGroup",R.id.blackteaRadioButton);
//        radioGroup.check(checkedId);
//        RadioButton radioButton = (RadioButton) findViewById(checkedId);//直接取名字來用
//        drinkName = radioButton.getText().toString();
//
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                editor.putInt("radioGroup", checkedId);//先定義
//                editor.apply();
//                RadioButton radioButton = (RadioButton) findViewById(checkedId);//直接取名字來用
//                drinkName = radioButton.getText().toString();
//            }
//        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    photoImageView.setVisibility(View.GONE);
                }else{
                    photoImageView.setVisibility(View.VISIBLE);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//點擊listView 的資訊
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order order = (Order) parent.getAdapter().getItem(position);//取得點擊的資訊 物件可轉型   parent.getAdapter() 拿出資料串
   /*             Toast.makeText(MainActivity.this,order.note, Toast.LENGTH_SHORT).show();//顯示效果  LENGTH_SHORT /LENGTH_LONG 效果的長短
                //↑this 如果MainActivity 未加 會變成  AdapterView.OnItemClickListener 本身*/
                goToDetailOrder(order);
                Snackbar.make(view, order.getNote(), Snackbar.LENGTH_SHORT).show();//顯示功能速度比Toast 快 搭配元件 compile 'com.android.support:design:23.2.1'
                //Snackbar 取代Toast 原因1.點擊後可在進一步UI 2.各元件可以UI互通
                //Snackbar.make(view,order.note, Snackbar.LENGTH_SHORT).setAction()//點擊後可執行的METHOD
            }
        });

        setupListView();
        setupSpinner();

        //下拉初始化
        int selectedId = sp.getInt("spinner", 0);
        spinner.setSelection(selectedId);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putInt("spinner", position);//先定義
                editor.apply();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//    spinner.setSelection();



    }



    void setupListView() {
        //      ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, orders);//把orders 放到simple_list_item_1 上面
/*        RealmResults results = realm.allObjects(Order.class);//所有的訂單

        OrderAdapter adapter = new OrderAdapter(this, results.subList(0, results.size()));//自建物件
        listView.setAdapter(adapter);//把東西丟進去
*/
        progressBar.setVisibility(View.VISIBLE);//資料回來前顯示
        final RealmResults results = realm.allObjects(Order.class);//所有的訂單

        OrderAdapter adapter = new OrderAdapter(MainActivity.this, results.subList(0, results.size()));//自建物件
        listView.setAdapter(adapter);//把東西丟進去



        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Order");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();


                    progressBar.setVisibility(View.GONE);//結束後 要消失 不管成功與否

                    return;
                }
                List<Order> orders = new ArrayList<Order>();
                Realm realm = Realm.getDefaultInstance();
                for (int i = 0; i < objects.size(); i++) {
                    Order order = new Order();
                    order.setNote(objects.get(i).getString("note"));
                    order.setStoreInfo(objects.get(i).getString("storeInfo"));
                    order.setMenuResults(objects.get(i).getString("menuResults"));
                    if(objects.get(i).getParseFile("photo")!=null){//下載圖片
                        order.photoURL = objects.get(i).getParseFile("photo").getUrl();
                    }


                    orders.add(order);

                    if (results.size() <= i) {//遠端個數 > LOCAL 自動載入
                        realm.beginTransaction();
                        realm.copyToRealm(order);
                        realm.commitTransaction();

                    }

                }

                realm.close();
                progressBar.setVisibility(View.GONE);//結束後 要消失 不管成功與否

                OrderAdapter adapter = new OrderAdapter(MainActivity.this, orders);
                listView.setAdapter(adapter);//把東西丟進去
            }
        });
    }

    void setupSpinner() {
/*        String[] data = getResources().getStringArray(R.array.storeInfo);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, data);

        spinner.setAdapter(adapter);

        */
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("StoreInfo");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null || objects == null) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();


                    progressBar.setVisibility(View.GONE);//結束後 要消失 不管成功與否

                    return;
                }

                progressBar.setVisibility(View.GONE);//結束後 要消失 不管成功與否
                String[] storeInfo = new String[objects.size()];
                for (int i = 0; i < objects.size(); i++) {

                    ParseObject object = objects.get(i);
                    storeInfo[i] = object.getString("name") + "," + object.getString("address");

                }
                ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_dropdown_item, storeInfo);
                spinner.setAdapter(adapter);//把東西丟進去
            }
        });

    }


    public void click(View view) {//BTN 改值 須設定ONCLICK
        progressDialog.setTitle("Loading....");
        progressDialog.show();//顯示

        note = editText.getText().toString(); //把畫面資料抓出來
        String text = note;
        textView.setText(text);
        //改成塞物件
        Order order = new Order();
        order.setMenuResults(menuResults);
        order.setNote(note);
        order.setStoreInfo((String) spinner.getSelectedItem());//選擇後的值
        //如果有圖要上傳 URI 讀檔
        if(hasPhoto){
            Uri uri = Utils.getPhotoURI();
            byte[] photo = Utils.uriToBytes(this, uri);

            if(photo == null){
                Log.d("Debug", "Read Photo Fail");
            }else{
                order.photo = photo;
            }
        }


        //LOCAL端
 /*       realm.beginTransaction();
        realm.copyToRealm(order);
        realm.commitTransaction();
*/
        SaveCallbackWithRealm callbackWithRealm = new SaveCallbackWithRealm(order, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(MainActivity.this, "Save Fail", Toast.LENGTH_LONG).show();

                }
                editText.setText("");//資料抓完清空
                menuResults = "";
                photoImageView.setImageResource(0);
                hasPhoto = false;
                progressDialog.dismiss();//圖片消失
     /*           Realm realm = Realm.getDefaultInstance();
                //LOCAL端
                realm.beginTransaction();
                realm.copyToRealm(order);
                realm.commitTransaction();

                realm.close();*/
                setupListView();


            }
        });
        //  orders.add(order);


        //    setupSpinner();
        order.saveToRemote(callbackWithRealm);
    }

    public void goToMenu(View view) {
        Intent intent = new Intent();//媒介 讓ACTIVITY 跳ACTIVITY
        intent.setClass(this, DrinkMenuActivity.class);//呼叫他
        //    startActivity(intent);//新的Activity即產生
        startActivityForResult(intent, REQUEST_CODE_MENU_ACTIVITY);//知道是誰帶回 資訊可以做處理

    }

    public void goToDetailOrder(Order order) {
        Intent intent = new Intent();
        intent.setClass(this, OrderDetailActivity.class);

        intent.putExtra("note", order.getNote());
        intent.putExtra("storeInfo", order.getStoreInfo());
        intent.putExtra("menuResults", order.getMenuResults());
        intent.putExtra("photoURL", order.photoURL);
        startActivity(intent);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//讀取結果
        super.onActivityResult(requestCode, resultCode, data);
        //先檢查是否通過驗證 得到回傳資料
        if (requestCode == REQUEST_CODE_MENU_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                menuResults = data.getStringExtra("result");

            }
        }else if (requestCode == REQUEST_CODE_CAMERA_ACTIVITY) {
            if (resultCode == RESULT_OK) {
               photoImageView.setImageURI(Utils.getPhotoURI());
                hasPhoto = true;

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_take_photo){
            Toast.makeText(this, "Take Photo", Toast.LENGTH_LONG).show();
            goToCamera();
        }
        return super.onOptionsItemSelected(item);
    }

    protected  void goToCamera(){
        if(Build.VERSION.SDK_INT >= 23){//版本須先確定可存取
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {//確定未接受
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);//詢問接受
                return;
            }
        }
        //呼叫CameraActivity
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//找出 相機 拍完一張就會回來
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Utils.getPhotoURI());//照片存放位置
        startActivityForResult(intent, REQUEST_CODE_CAMERA_ACTIVITY);
    }

    protected void onStart() {
        super.onStart();
        Log.d("debug", "Main Activity OnStart");
    }

    protected void onResume() {
        super.onResume();
        Log.d("debug", "Main Activity OnResume");
    }

    protected void onPause() {
        super.onPause();
        Log.d("debug", "Main Activity OnPause");
    }

    protected void onStop() {//儲存動作 避免流失
        super.onStop();
        Log.d("debug", "Main Activity OnStop");


    }

    protected void onRestart() {//重新整料再次顯示
        super.onRestart();
        Log.d("debug", "Main Activity OnRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        Log.d("debug", "Main Activity OnDestroy");
    }
}
