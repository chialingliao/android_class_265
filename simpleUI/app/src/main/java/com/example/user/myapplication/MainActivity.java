package com.example.user.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    TextView textView;//宣告
    EditText editText;
    RadioGroup radioGroup;
    CheckBox checkBox;
    String note = "";
    String drinkName = "black tea";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);//去找VIEW ,須轉型態
        editText = (EditText) findViewById(R.id.editText);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        checkBox = (CheckBox) findViewById(R.id.hideCheckBox);

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

                RadioButton radioButton = (RadioButton)findViewById(checkedId);//直接取名字來用
                drinkName = radioButton.getText().toString();
            }
        });
   }

    public void click(View view) {//BTN 改值 須設定ONCLICK
        note = editText.getText().toString(); //把畫面資料抓出來
        String text = note;
        textView.setText(text);
        editText.setText("");//資料抓完清空
    }

}
