package com.android.demo.nlpdemo

import android.app.Application
import android.system.ErrnoException
import android.system.Os
import com.hankcs.hanlp.HanLP
import com.hankcs.hanlp.corpus.io.IIOAdapter
import jackmego.com.jieba_android.JiebaSegmenter
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.*
import kotlin.concurrent.thread


class App : Application() {

    companion object {

        lateinit var application: Application
    }

    override fun onCreate() {
        super.onCreate()

        application = this

//        try {
//            Os.setenv("HANLP_ROOT", "", true)
//        } catch (e: ErrnoException) {
//            throw RuntimeException(e)
//        }
//        HanLP.Config.IOAdapter = object : IIOAdapter {
//            override fun open(path: String): InputStream {
//                return assets.open(path)
//            }
//
//            override fun create(path: String): OutputStream {
//                throw IllegalAccessError("不支持写入$path！请在编译前将需要的数据放入app/src/main/assets/data");
//            }
//        }
        thread {
            JiebaSegmenter.init(getApplicationContext());
        }
    }
}