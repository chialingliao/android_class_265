package com.example.user.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.internal.async.QueryUpdateTask;


public class MainActivity extends AppCompatActivity {
    TextView textView;//宣告
    EditText editText;
    RadioGroup radioGroup;
    CheckBox checkBox;
    List<Order> orders;
    String note = "";
    String drinkName;
    ListView listView;
    Spinner spinner;
    //存入記憶體 若大量存取及寫入會爆炸
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);//去找VIEW ,須轉型態
        editText = (EditText) findViewById(R.id.editText);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        checkBox = (CheckBox) findViewById(R.id.hideCheckBox);
        listView = (ListView)findViewById(R.id.listView);
        orders = new ArrayList<>();
        spinner = ( Spinner)findViewById(R.id.spinner);

        sp = getSharedPreferences("setting", Context.MODE_PRIVATE);//你要拿setting 裡的東西
        editor = sp.edit();//拿出setting裡某特殊內容 寫入

        // Create a RealmConfiguration which is to locate Realm file in package's "files" directory.
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
        // Get a Realm instance for this thread
        realm = Realm.getInstance(realmConfig);

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
        int checkedId = sp.getInt("radioGroup",R.id.blackteaRadioButton);
        radioGroup.check(checkedId);
        RadioButton radioButton = (RadioButton) findViewById(checkedId);//直接取名字來用
        drinkName = radioButton.getText().toString();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                editor.putInt("radioGroup", checkedId);//先定義
                editor.apply();
                RadioButton radioButton = (RadioButton) findViewById(checkedId);//直接取名字來用
                drinkName = radioButton.getText().toString();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//點擊listView 的資訊
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order order = (Order) parent.getAdapter().getItem(position);//取得點擊的資訊 物件可轉型   parent.getAdapter() 拿出資料串
   /*             Toast.makeText(MainActivity.this,order.note, Toast.LENGTH_SHORT).show();//顯示效果  LENGTH_SHORT /LENGTH_LONG 效果的長短
                //↑this 如果MainActivity 未加 會變成  AdapterView.OnItemClickListener 本身*/
                Snackbar.make(view, order.getNote(), Snackbar.LENGTH_SHORT).show();//顯示功能速度比Toast 快 搭配元件 compile 'com.android.support:design:23.2.1'
                //Snackbar 取代Toast 原因1.點擊後可在進一步UI 2.各元件可以UI互通
                //Snackbar.make(view,order.note, Snackbar.LENGTH_SHORT).setAction()//點擊後可執行的METHOD
            }
        });

        setupListView();
        setupSpinner();

        int selectedId = sp.getInt("spinner",0);
        spinner.setSelection(selectedId);

       spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               editor.putInt("spinner", position);//先定義
               editor.apply();


           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {
//               editor.putInt("spinner", 0);//先定義
//               editor.apply();
      /*         int selectedId = sp.getInt("spinner",0);
               spinner.setSelection(selectedId);*/
           }
       });

//    spinner.setSelection();







   }
    void setupListView(){
 //      ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, orders);//把orders 放到simple_list_item_1 上面
        RealmResults results = realm.allObjects(Order.class);//所有的訂單

        OrderAdapter adapter = new OrderAdapter(this, results.subList(0,results.size()));//自建物件
        listView.setAdapter(adapter);//把東西丟進去
    }

    void setupSpinner(){
        String[] data = getResources().getStringArray(R.array.storeInfo);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, data);

        spinner.setAdapter(adapter);
    }



    public void click(View view) {//BTN 改值 須設定ONCLICK
        note = editText.getText().toString(); //把畫面資料抓出來
        String text = note;
        textView.setText(text);
        //改成塞物件
        Order order = new Order();
        order.setDrinkName(drinkName);
        order.setNote(note);
        order.setStoreInfo((String) spinner.getSelectedItem());//選擇後的值


        realm.beginTransaction();
        realm.copyToRealm(order);
        realm.commitTransaction();



      //  orders.add(order);



        editText.setText("");//資料抓完清空
        setupListView();
        setupSpinner();
    }

}
