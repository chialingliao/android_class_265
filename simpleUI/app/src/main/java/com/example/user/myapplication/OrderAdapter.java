package com.example.user.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by user on 2016/4/25.
 */
public class OrderAdapter extends BaseAdapter{
    ArrayList<Order> orders;
    LayoutInflater inflater;//幫助放大縮小

    public OrderAdapter(Context contex, ArrayList<Order> orders){//建構子
        this.inflater = LayoutInflater.from(contex);
        this.orders = orders;
    }


    @Override
    public int getCount() {//回傳幾欄
        return orders.size();
    }

    @Override
    public Object getItem(int position) {//找出物建裡的資料
        return orders.get(position);
    }

    @Override
    public long getItemId(int position) {//每個物件的KEY值
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {//最主要的FUNC
        //一般習慣用Holder
        Holder holder ;

        if(convertView== null){
            convertView = inflater.inflate(R.layout.listview_item, null);// 第一次給要先定義
            holder = new Holder();
            holder.drinkName = (TextView)convertView.findViewById(R.id.drinkName);
            holder.note = (TextView)convertView.findViewById(R.id.note);

            convertView.setTag(holder);
        }else {
            holder = (Holder)convertView.getTag();
        }

        holder.drinkName.setText(orders.get(position).drinkName);
        holder.note.setText(orders.get(position).note);
        //設定值
  /*      drinkName.setText(orders.get(position).drinkName);
        note.setText(orders.get(position).note);
*/
        return convertView;
    }

    class Holder{
        TextView drinkName;
        TextView note;
    }
}
