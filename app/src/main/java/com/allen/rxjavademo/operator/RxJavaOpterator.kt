package com.allen.rxjavademo.operator

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.allen.rxjavademo.R
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * RxJava的常用创建操作符
 */
class RxJavaCreateOperator : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        CreateOperator().createOperator()
        TransformOperator().transformOperator()
        AssistOperator().assistOperator()

    }

    /**
     * 创建操作符的例子
     */
    class CreateOperator {
        internal fun createOperator() {
            //直接创建一个Observable
            unsafeCreateOperator()
            //使用just操作符-- 将一个或多个对象转化成发射这个或这些对象的Observable
            justOperator()
            //使用from操作符 -- 将一个Iterable,一个Future,或者一个数组转化成Observable
            fromOperator()
            //使用interval操作符 -- 创建一个按固定时间间隔发射整数序列的Observable，相当于定时器
            intervalOperator()
            //使用range操作符 -- 创建一个发射指定范围整数序列的Observable，第一个参数是开始位置（必须不小于0），第二个参数是个数
            rangeOperator()
            //使用repeat操作符 -- 创建一个N次重复发送特定数据的Observable
            repeatOperator()
        }

        private val tog = "tog"
        private fun repeatOperator() {
            Observable.range(0, 3)
                    .repeat(2)
                    .subscribe {
                        Log.e(tog, "repeat $it")
                    }
        }

        private fun rangeOperator() {
            //可以使用它来代替for循环
            Observable.range(0, 5)
                    .subscribe {
                        Log.e(tog, "range $it")
                    }
        }

        /**
         * 使用unsafeCreate()来直接创建一个Observable
         * create()方法已经过时
         */
        private fun unsafeCreateOperator() {
            Observable.unsafeCreate<Int> {
                try {
                    if (!it.isUnsubscribed) {
                        for (i: Int in 1..4) {
                            it.onNext(i)
                        }
                        it.onCompleted()
                    }
                } catch (e: Exception) {
                    it.onError(e)
                }
            }.subscribe(object : Subscriber<Int>() {
                override fun onNext(t: Int?) {
                    Log.e(tog, "onNext $t")
                }

                override fun onCompleted() {
                    Log.e(tog, "onCompleted ")
                }

                override fun onError(e: Throwable?) {
                    Log.e(tog, "onError ${e?.message}")
                }
            })
        }

        /**
         * 使用just() 方法创建一个Observable
         */
        private fun justOperator() {
            Observable.just("A", "B")
                    .subscribe(object : Subscriber<String>() {
                        override fun onNext(t: String?) {
                            Log.e(tog, "onNext $t")
                        }

                        override fun onCompleted() {
                            Log.e(tog, "just onCompleted ")
                        }

                        override fun onError(e: Throwable?) {
                            Log.e(tog, "onError ${e?.message}")
                        }
                    })
        }

        private fun fromOperator() {
            Observable.from(arrayOf("A1", "B1")).subscribe {
                Log.e(tog, it)
            }
        }

        private fun intervalOperator() {
            Observable.interval(3, TimeUnit.SECONDS)
                    .subscribe(object : Subscriber<Long>() {
                        override fun onNext(t: Long?) {
                            Log.e(tog, "interval onNext $t")
                        }

                        override fun onCompleted() {
                            Log.e(tog, "interval onCompleted")
                        }

                        override fun onError(e: Throwable?) {
                            Log.e(tog, "interval onError ${e?.message}")
                        }

                    })
        }
    }

    /**
     * 变换操作符
     */
    class TransformOperator {
        private val tog = "tog"
        fun transformOperator() {
            //map操作符-- 对序列的每一项都应用一个函数来变化Observable发射的数据序列
            mapOperator()
        }

        private fun mapOperator() {
            val host = "http://blog.csdn.net/"
            Observable.just("itachi85")
                    .map { t -> host + t }
                    .subscribe {
                        Log.e(tog, "transform---map $it")
                    }

        }
    }

    class AssistOperator{
        fun assistOperator(){
            subscribeOnAndObserveOn()
        }

        /**
         * subscribeOn操作符用于指定Observable自身在那个线程上运行。如果Observable需要执行耗时操作，一般可以
         * 让其在新开的一个子线程上运行。
         * observeOn操作符用于指定Observer所运行的线程，这样就可以修改UI
         *
         */
        private fun subscribeOnAndObserveOn() {
            Observable.unsafeCreate(Observable.OnSubscribe<String> { it ->
                Log.e("tog","Observable: ${Thread.currentThread().name}")
                it?.onNext("a")
            }).subscribeOn(Schedulers.newThread())//
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Subscriber<String>() {
                        override fun onNext(t: String?) {
                            Log.e("tog","Observable ${Thread.currentThread().name}  $t")
                        }

                        override fun onCompleted() {
                            Log.e("tog","onCompleted")
                        }

                        override fun onError(e: Throwable?) {
                            Log.e("tog","onError")
                        }

                    })
        }
    }
}