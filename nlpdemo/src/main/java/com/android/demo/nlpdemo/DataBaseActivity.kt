package com.android.demo.nlpdemo

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.demo.nlpdemo.resolver.HanLpResolver
import com.android.demo.nlpdemo.resolver.IResolver
import com.hankcs.hanlp.HanLP
import com.hankcs.hanlp.seg.common.Term
import dalvik.system.DexClassLoader
//import jackmego.com.jieba_android.JiebaSegmenter
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

class DataBaseActivity : AppCompatActivity() {

    lateinit var decoder: DecoderInterface

    val decodeResultList: ArrayList<DecoderBean> = ArrayList()

    lateinit var rv_decode: RecyclerView

    val mHandler = Handler(Looper.getMainLooper())
    var iResolver: IResolver = HanLpResolver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_database)
        rv_decode = findViewById(R.id.rv_cmcc)

        DataBaseMgr.makeSureDir(this)

        val optimizedDexOutputPath = App.application.cacheDir
        val methodLoadFile = File(App.application.cacheDir.absolutePath + File.separator + "testjar.jar")
        val classLoader = DexClassLoader(methodLoadFile.absolutePath, optimizedDexOutputPath.absolutePath, null, classLoader)

//        val decoderClass = classLoader.loadClass("com.example.testjar.Decode")
//        val getResMethod = decoderClass.getDeclaredMethod("getRes")
//        var invoke: String? = getResMethod.invoke(null) as String?
//
//        Toast.makeText(this, invoke, Toast.LENGTH_SHORT).show()

        decoder = CsvDecoder()

        decoder.setFilePath(DataBaseMgr.path + File.separator + "ct.csv")

        decodeResultList.clear()

        val decodeLines = decoder.decodeLines(500)
        decodeLines.forEach { decodeResultList.add(DecoderBean(it, null)) }

        decodeResultList.forEach {
            it.decoderRes = (iResolver.resolve(it.smsContent) ?: "- -")
        }

        rv_decode.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_decode.adapter = SmsDecodeContentAdapter(this, decodeResultList)

//        val textView = findViewById<TextView>(R.id.tv)
//        var segment : List<Term>
//        segment = HanLP.segment("本月实时话费158.00元，当前可用余额185.57元。5G智享套餐158元国内拨打国际长途优惠分钟数200分钟，5G智享套餐158元基本通话优惠分钟数397分钟，国内通用流量共80.00GB，当前可使用30.20GB")
//        segment = HanLP.segment("本月实时话费158.00元，当前可用余额185.57元。5G智享套餐158元国内拨打国际长途优惠分钟数200分钟，5G智享套餐158元基本通话优惠分钟数397分钟，国内通用流量共80.00GB，当前可使用30.20GB")
//        textView.setText(segment.toString())
    }
}