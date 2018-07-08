package com.allen.rxjavademo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import rx.Subscriber

class MainActivity : AppCompatActivity() {
    val TAG = "tog"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //RxJava基本实现
        //1、创建Observer(观察者)
        createObserver()

    }

    /**
     * 创建Observer(创建观察者)
     */
    private fun createObserver() {
        var subscriber = object : Subscriber<String>(){
            override fun onNext(t: String?) {
                Log.e(TAG,t)
            }

            override fun onCompleted() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onError(e: Throwable?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onStart() {
                super.onStart()
            }
        }

    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
