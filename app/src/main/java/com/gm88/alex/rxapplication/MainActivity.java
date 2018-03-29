package com.gm88.alex.rxapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private Context mContext;
    private Disposable disposable;
    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();

//        initRetro();
//        initTest();
//        initTest2();
//        initTest3();
//        initTest4();
//        initTest5();
//        initTest6();
//        initTest7();

//        initTest8();

//        initTest9();
//        initTest10();
//        initTest11();
//        initTest12();
        initTest13();
    }

    private void initTest13() {
        boolean flag = 10 % 2 == 1 && 10 / 3 == 0 && 1 / 0 == 0 ;
        System.out.println(flag ? "mldn" : "yootk") ;
    }

    private void initTest12() {
    }

    private void initTest11() {
        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                File f = new File("test.txt");
                if (!f.exists()){
                    boolean i = new File("test.txt").mkdirs();
                }
                Log.d(TAG, "subscribe: " +  f.exists());
                FileReader reader = new FileReader("test.txt");
                BufferedReader bufferedReader = new BufferedReader(reader);
                String str ;

                while ((str = bufferedReader.readLine())!=null && !e.isCancelled()){
                    while (e.requested() == 0){
                        if (e.isCancelled()){
                            break;
                        }
                    }
                e.onNext(str);
                }

                bufferedReader.close();
                reader.close();

                e.onComplete();
            }
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription = s;
                        s.request(1);
                    }

                    @Override
                    public void onNext(String s) {
                        Log.i(TAG, " " + s);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e(TAG, "onError: " + t.getMessage() );
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //Backpressure 背压概念 oom
    private void initTest10() {
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0 ; ; i++){
                    e.onNext(i);
                }
            }
        }).subscribeOn(Schedulers.io())
                .filter(new Predicate<Integer>() {                  // 限量
            @Override
            public boolean test(Integer integer) throws Exception {
                return integer % 10 == 0;
            }
        }).sample(10 , TimeUnit.SECONDS); //sample 操作  // 通过限时 去限量

        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("a");
            }
        }).subscribeOn(Schedulers.io());

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, Object>() {
            @Override
            public Object apply(Integer integer, String s) throws Exception {
                return s + integer;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object value) {
                        Log.d(TAG, "onNext: " + value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //zip 实践 222  打包请求  (四)end
    private void initTest9() {
    }
    //实践 zip  111
    private void initTest8() {
        assert isChild() : "是否 承认ischild() 就不是child";

        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
            e.onNext(1);
            e.onNext(2);
            e.onNext(3);
            e.onNext(4);
                Log.d(TAG, "observable1    " + Thread.currentThread().getName());
            e.onComplete();
            }
        }).subscribeOn(Schedulers.io());

        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("A");
                e.onNext("B");
                e.onNext("C");
                e.onNext("D");
                e.onNext("E");
                Log.d(TAG, "observable2    " + Thread.currentThread().getName());
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io());

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, Map<Integer, String>>() {
            @Override
            public Map<Integer, String> apply(Integer integer, String s) throws Exception {
                Map<Integer , String> map = new HashMap<>();
                map.put(integer , s);
                return map;
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Map<Integer, String>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
                Log.d(TAG, "onSubscribe: " + d.toString());
            }

            @Override
            public void onNext(Map<Integer, String> value) {
                Log.d(TAG, "onNext: " + value.toString());
                Toast.makeText(mContext, "" + value.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    //实践
    private void initTest7() {
        RetroInterface retro = RetrofitUtils.instance().create(RetroInterface.class);
        retro.register("xxxxxx" )
                .subscribeOn(Schedulers.io())  //发起登陆
                .observeOn(AndroidSchedulers.mainThread())  //发起登陆
                .doOnNext(new Consumer<RegisterBean>() {
                    @Override
                    public void accept(RegisterBean registerBean) throws Exception {
                        //注册完成 后 的结果回调
                    }
                })
                .observeOn(Schedulers.io())     // 发起登陆的时候，切换线程
                .flatMap(new Function<RegisterBean, ObservableSource<LoginBean>>() {
                    @Override
                    public ObservableSource<LoginBean> apply(RegisterBean registerBean) throws Exception {
                        return retro.login("yyyyy");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginBean>() {
                    @Override
                    public void accept(LoginBean loginBean) throws Exception {
                        Log.d(TAG, "accept: " + loginBean);
                    }
                });
    }

    //  lamada
    private void initTest6() {
        TextView tv = new TextView(getApplicationContext());
        tv.setOnClickListener((view) -> {
            Toast.makeText(mContext ,"xxx",Toast.LENGTH_LONG).show();
        });
    }

    // concatMap 的使用 有顺序保证 同 flatmap
    private void initTest5() {
        Observable.create(e -> {
            e.onNext("1");
            e.onNext("2");
            e.onNext("3");
        }).concatMap(s -> {
            return Observable.fromArray(s).delay(10 , TimeUnit.MILLISECONDS);
        }).subscribe((s) -> {
            Log.d(TAG, "initTest5: " + s);
        });
    }

    //FlatMap 的使用 每接受一个事件  默认 新增一个数据Observables （1  -->  N）   但是顺序不保证
    private void initTest4() {

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(1);
                e.onNext(1);
            }
        }).flatMap((integer) -> {
            List<String > list = new ArrayList<>();
            for (int i = 0; i < 5 ; i ++){
                list.add("I am Value " + integer);
            }
            return Observable.fromIterable(list).delay(10 , TimeUnit.MILLISECONDS);
        }).subscribe((s) -> {
            Log.d(TAG, "initTest4: " + s);
        });

        /*
        *
        new Function<Integer, ObservableSource<String>>() {      //类型变化 和数据的变化
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                List<String > list = new ArrayList<>();
                for (int i = 0; i < 5 ; i ++){
                    list.add("I am Value " + integer);
                }

                return Observable.fromIterable(list).delay(10 , TimeUnit.MILLISECONDS);
            }
        }



        new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "accept: " + s);
            }
        }



        *
        **/


    }

    //map 使用 ： RxJava中最简单的一个变换操作符
    private void initTest3() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {

                return integer + "?  ";
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "accept: " + s);
            }
        });
    }
    //无 rxjava
    private void initRetro() {
        RetroInterface retroInterface = RetrofitUtils.instance().create(RetroInterface.class);
        retroInterface.loginCall("").enqueue(new Callback<LoginBean>() {
            @Override
            public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {

            }

            @Override
            public void onFailure(Call<LoginBean> call, Throwable t) {

            }
        });
    }
        //  初始化 rxjava + retrofit
    private void initTest() {
        RetroInterface retro = RetrofitUtils.instance().create(RetroInterface.class);
        retro.login("onBusy")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LoginBean value) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(mContext, "登录失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(mContext, "登录成功", Toast.LENGTH_SHORT).show();
                    }
                });


    }
        //  泛型
    private void initTest2() {
        HashMap a = MainActivity.<String>getList();
        List<String> b = MainActivity.getList2();
        List<String> c = new ArrayList<>();
        List<String> d = MainActivity.getList2();
    }





    public static <T> HashMap<T,T> getList(){
      return new HashMap<>();
    }

    public static  List<String> getList2(){
      return new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        disposable.dispose();
        super.onDestroy();
    }
}
