package com.example.soul.rxjavademo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mTalkme;

    private  int[] maths = {1,2,3};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initView();
        initData();
        initEvent();
    }


    /**
     * 创建一个被观察者
     */
   Observable<String>  mObservable = Observable.create(new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> subscriber) {
            subscriber.onNext("Hello,wolrd!");
            subscriber.onCompleted();
        }
    });

    /**
     * 创建一个观察者
     */

    Subscriber<String> mSubscriber = new Subscriber<String>(){
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(String s) {
            System.out.println(s);
        }
    };





    private void initView() {
        mTalkme = (Button) findViewById(R.id.bt_talkme);
        mTalkme.setOnClickListener(this);


    }
    private void initData() {

    }

    private void initEvent() {

    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_talkme:
                //一 Rxjava 基础
                mObservable.subscribe(mSubscriber);

                Observable.just("hello,world")
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                System.out.println(s);
                            }
                        });
                Observable.just("Hello,world")
                        .subscribe(s-> Log.i("log-i",s));
                // map操作符
                Observable.just("hello,world")
                        .map(s-> s.hashCode())
                        .subscribe(s->Log.i("log-i",s+""));
                //二 Rxjava 进阶

                query("hello,rxjava")
                        .subscribe(url->{
                            for (String u:url){
                                Log.i("log-i",u);
                            }
                        });
                //from
                query("hello,rxjava")
                        .subscribe(urls->{
                            Observable.from(urls).subscribe(url->Log.i("log-i",url));
                        });

                //flatMap
                query("hello,rxjava")
                        .flatMap(urls->Observable.from(urls))
                        .subscribe(url->Log.i("log-i",url));

                query("hello,android")
                        .flatMap(urls->Observable.from(urls))
                        .flatMap(url->getTitle(url))
                        .take(5)//只保留5個數字
                        .doOnNext(titel->savaTitle(titel))//在輸入元素之前做些額外的事
                        .subscribe(url->Log.i("log-i",url));

                Observable.just("hello,world-----------")
                        .map(s-> new IOException(s))
                        .subscribe(new Subscriber<IOException>() {
                            @Override
                            public void onCompleted() {
                                Log.i("log-i","dsf");
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(IOException e) {
                                e.printStackTrace();
                            }
                        });


//                paixu(maths,0);




                break;
        }
    }



public void paixu(int[] number,int num){
    for(int x= 0;x<number.length-1;x++){
            //打印数组
        if(x!=0){
            ChangeBound(number,x,x+1);
        }
            pintln(number);
    }
    num++;
    if (num<number.length){
        //排序
        paixu2(number);
        ChangeBound(number,0,num);
        paixu(number,num);
    }

}

    private void paixu2(int[] number) {
        for (int x = 0;x<number.length-1;x++){
            for (int y = x+1;y<number.length;y++){
                if (number[x]>number[y]){
                    ChangeBound(number,x,y);
                }
            }
        }
    }

    private void pintln(int[] number) {

        for(int  num:number){
            Log.i("log-i",num+"");
        }
    }

    private void ChangeBound(int[] number, int x, int y) {
        int temp = number[x];
        number[x] = number[y];
        number[y] = temp;
    }


    private void savaTitle(String titel) {
        Toast.makeText(MainActivity.this,titel, Toast.LENGTH_SHORT).show();
    }

    Observable<List<String>> query(String text) {
        ArrayList<String> strings = new ArrayList<>();
        strings.add(text+"1");
        strings.add(text+"2");
        strings.add(text+"3");
        return Observable.create(subscriber -> {
            subscriber.onNext(strings);
            subscriber.onCompleted();
        });
    }

    Observable<String> getTitle(String url) {
        String  str =  url+"fsd";
        return Observable.create(sub->{sub.onNext(str);sub.onCompleted();});
    }

}
