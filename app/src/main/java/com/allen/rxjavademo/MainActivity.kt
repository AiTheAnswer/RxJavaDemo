package com.allen.rxjavademo

import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import rx.Observable
import rx.Observer
import rx.Subscriber

class MainActivity : AppCompatActivity() {
    val tag = "tog"
    private var mImg: ImageView? = null
    private var subscriber: Subscriber<String>? = null
    private var observable: Observable<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        //RxJava基本实现
        //1、创建Observer(观察者)
        createObserver()
        //2、创建Observable(被观察者)
        createObservable()
        //3、Subscribe(订阅)
        observable?.subscribe(subscriber)
        example()

    }

    /**
     * 使用RxJava的简单例子
     */
    private fun example() {
        //一、打印字符串数组
        val names = arrayOf("Allen")
        Observable.from(names).subscribe {
            Log.e(tag, it)
        }
        //二、由id取得图片并显示
        val drawableRes = R.mipmap.ic_launcher
        Observable.unsafeCreate(Observable.OnSubscribe<Drawable> {
            val drawable = resources.getDrawable(drawableRes)
            it.onNext(drawable)
            it.onCompleted()
        }).subscribe(object : Observer<Drawable>{
            override fun onNext(t: Drawable?) {
                mImg?.setImageDrawable(t)
            }

            override fun onCompleted() {
                Log.e(tag,"mImg  onCompleted")
            }

            override fun onError(e: Throwable?) {
                Log.e(tag,"mImg  onError")
            }

        })

    }

    /**
     * 创建Observer(创建观察者)
     */
    private fun createObserver() {
        subscriber = object : Subscriber<String>() {
            /**
             * RxJava的事件回调方法
             */
            override fun onNext(t: String?) {
                Log.e(tag, t)
            }

            /**
             * 事件队列完结。RxJava不仅把每个事件单独处理，还会把它们看做一个队列。RxJava规定，当不会
             * 再有新的onNext()发生时，需要触发onCompleted()方法作为标志。
             */
            override fun onCompleted() {
                Log.e(tag, "onCompleted")
            }

            /**
             * 事件队列异常，在处理过程中出异常时，onError（）会被触发，同时队列自动终止，不允许再有事件发出
             */
            override fun onError(e: Throwable?) {
                Log.e(tag, "onError")

            }

            /**
             * 这是Subscriber增加的方法。它会在subscriber刚开始，事件还未发送之前调用，可以用于做一些准备工作，
             * 例如数据的清零或重置。这是一个可选方法，默认情况下它的实现为空。（是子线程）
             */
            override fun onStart() {
                super.onStart()
                Log.e(tag, "onStart")
            }
        }

    }

    /**
     * 创建Observable(被观察者)
     * Observable即被观察者，它决定什么时候触发事件以及触发怎样的事件。RxJava使用create()
     * 方法来创建一个Observable,并为它定义事件触发规则
     */
    private fun createObservable() {
        //方式1：使用unsafeCreate()
        /* observable = Observable.unsafeCreate {
             it.onNext("Hello")
             it.onNext("Hi")
             it.onNext("Aloha")
             it.onCompleted()
         }*/
        /**
         * 方式二、三 实质上还是调用unsafeCreate()方法进行创建
         */
        //方式2： 也可以使用just()方法来实现
        // observable = Observable.just("杨影枫", "月眉儿")
        //方式3： 还可以使用from()方法来实现
        val words = arrayOf("杨影枫", "月眉儿")
        observable = Observable.from(words)
    }

    private fun initView() {
        mImg = findViewById(R.id.img)
    }

}
