package com.xiang.pulltorefrelistviewproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ReFlashListView.reflashListener {
    /*
    * 1 添加顶部布局
    * 2 隐藏顶部布局
    * 3.监听list view 滚动事件
    *
    * */
    ReFlashListView list;
    List<Persen> persens=new ArrayList<>();
   ListAdapter adapter ;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list= (ReFlashListView) findViewById(R.id.list);
        initData();
        adapter=new ListAdapter(MainActivity.this,persens);
        list.setAdapter(adapter);
        list.setInterface(this);


    }
    private  void initData(){
        Persen persen=new Persen();
        persen.setName("原始数据");
        persen.setAge("20");
        persens.add(persen);
        persens.add(persen);
        persens.add(persen);
        persens.add(persen);
        persens.add(persen);
    }
    private  void initData2(){
        Persen persen2=new Persen();
        persen2.setName("刷星数据");
        persen2.setAge("30");
        persens.add(0,persen2);
    }

    @Override
    public void onRefalsh() {
        initData2();
        adapter.notifyDataSetChanged();

        list.reflashComplete();
    }

}
