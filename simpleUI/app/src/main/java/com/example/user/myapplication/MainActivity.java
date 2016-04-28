package com.example.user.myapplication;

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


public class MainActivity extends AppCompatActivity {
    TextView textView;//宣告
    EditText editText;
    RadioGroup radioGroup;
    CheckBox checkBox;
    ArrayList<Order> orders;
    String note = "";
    String drinkName = "black tea";
    ListView listView;
    Spinner spinner;
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

        editText.setOnKeyListener(new View.OnKeyListener() {//ENTER 等同BTN效果
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
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

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton radioButton = (RadioButton) findViewById(checkedId);//直接取名字來用
                drinkName = radioButton.getText().toString();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//點擊listView 的資訊
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order order = (Order)parent.getAdapter().getItem(position);//取得點擊的資訊 物件可轉型   parent.getAdapter() 拿出資料串
   /*             Toast.makeText(MainActivity.this,order.note, Toast.LENGTH_SHORT).show();//顯示效果  LENGTH_SHORT /LENGTH_LONG 效果的長短
                //↑this 如果MainActivity 未加 會變成  AdapterView.OnItemClickListener 本身*/
                Snackbar.make(view,order.note, Snackbar.LENGTH_SHORT).show();//顯示功能速度比Toast 快 搭配元件 compile 'com.android.support:design:23.2.1'
                //Snackbar 取代Toast 原因1.點擊後可在進一步UI 2.各元件可以UI互通
                //Snackbar.make(view,order.note, Snackbar.LENGTH_SHORT).setAction()//點擊後可執行的METHOD
            }
        });

        setupListView();
        setupSpinner();
   }
    void setupListView(){
 //      ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, orders);//把orders 放到simple_list_item_1 上面
        OrderAdapter adapter = new OrderAdapter(this, orders);//自建物件
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
        order.drinkName = drinkName;
        order.note = note;
        order.storeInfo = (String)spinner.getSelectedItem();//選擇後的值
        orders.add(order);

        editText.setText("");//資料抓完清空
        setupListView();
        setupSpinner();
    }

}
