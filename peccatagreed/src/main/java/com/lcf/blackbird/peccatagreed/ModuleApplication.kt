package com.lcf.blackbird.peccatagreed

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import java.lang.ref.Reference
import java.lang.ref.ReferenceQueue
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.HashMap

object ModuleApplication {

    lateinit var application: Application

    val queue: ReferenceQueue<Any> = ReferenceQueue<Any>()
    val hashMap = HashMap<String, WeakReference<Activity>>()

    val mHandler = Handler(Looper.getMainLooper())

    fun initActivityLifeCycle() {
        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                hashMap[activity.toString()] = WeakReference(activity, queue)
            }

            override fun onActivityStarted(activity: Activity) {

            }

            override fun onActivityResumed(activity: Activity) {

            }

            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivityStopped(activity: Activity) {

            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

            override fun onActivityDestroyed(activity: Activity) {
                val weakRef = hashMap.remove(activity.toString())
                checkRecycle(false, weakRef)
            }
        })
    }

    private fun checkRecycle(isAgain: Boolean, weakRef: WeakReference<Activity>?) {
        val runnable = Runnable {
            var tmpRef: Reference<*>?
            var isRecycled = false

            while (queue.poll().apply { tmpRef = this } != null) {
                if (weakRef == tmpRef) {
                    isRecycled = true
                    //表示被回收掉了，中止流程
                    break
                }
            }

            print("poll end")
            if (!isRecycled) {
                if (!isAgain) {
                    System.gc()
                    checkRecycle(true, weakRef)
                } else {
                    //没有被回收需要通知前台
                    Toast.makeText(ModuleApplication.application, "${weakRef?.get()?.componentName} 内存泄漏", Toast.LENGTH_LONG).show()
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Looper.getMainLooper().queue.addIdleHandler {
                runnable.run()
                false
            }
        } else {
            mHandler.post {
                runnable.run()
            }
        }
    }
}