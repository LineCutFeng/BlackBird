package com.android.demo.nlpdemo

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hankcs.hanlp.HanLP
import com.hankcs.hanlp.seg.common.Term
import dalvik.system.DexClassLoader
//import jackmego.com.jieba_android.JiebaSegmenter
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    lateinit var decoder: DecoderInterface

    val path = App.application.filesDir.absolutePath + File.separator + "messagefiles"

    val decodeResultList: ArrayList<DecoderBean> = ArrayList()

    lateinit var rv_decode: RecyclerView

    val mHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        rv_decode = findViewById(R.id.rv_cmcc)

        makeSureDir(this)

        val optimizedDexOutputPath = App.application.cacheDir
        val methodLoadFile = File(App.application.cacheDir.absolutePath + File.separator + "testjar.jar")
        val classLoader = DexClassLoader(methodLoadFile.absolutePath, optimizedDexOutputPath.absolutePath, null, classLoader)

//        val decoderClass = classLoader.loadClass("com.example.testjar.Decode")
//        val getResMethod = decoderClass.getDeclaredMethod("getRes")
//        var invoke: String? = getResMethod.invoke(null) as String?
//
//        Toast.makeText(this, invoke, Toast.LENGTH_SHORT).show()

        decoder = CsvDecoder()

        decoder.setFilePath(path + File.separator + "ct.csv")

        decodeResultList.clear()

        val decodeLines = decoder.decodeLines(500)
        decodeLines.forEach { decodeResultList.add(DecoderBean(it, null)) }

        decodeResultList.forEach {

            val segment: List<Term?> = HanLP.segment(it.smsContent)
//                    val dividedString = JiebaSegmenter.getJiebaSegmenterSingleton().getDividedString(it.smsContent)

            if (it.smsContent.contains("0.75元。直接回复HF查询实时")) {
                println("好耶")
            }

            var decorderResult: String? = null

            for (termWithIndex in segment.withIndex()) {
                if (termWithIndex.value?.nature?.toString() == "m") {       //数量词 m

                    var numberStr = termWithIndex.value?.word

                    var indexTmp = termWithIndex.index - 1

                    if (segment.getOrNull(termWithIndex.index - 1).let { it?.nature.toString() == "nx" && it?.word == "-" }) {
                        numberStr = "-$numberStr"
                        indexTmp--
                    }

                    var finded = false;
                    while (indexTmp >= 0) {
                        if (segment[indexTmp]?.nature.toString() == "w") {      //标点符号
                            indexTmp--
                        } else if (segment[indexTmp]?.nature.toString() == "p" && segment[indexTmp]?.word in arrayOf("是", "为")) {
                            indexTmp--
                        } else if (segment[indexTmp]?.nature.toString() in arrayOf("n", "vn") && segment[indexTmp]?.word in arrayOf("余额", "结余")) {        //余额 名词n
                            finded = true
                            break
                        } else if (segment[indexTmp]?.nature.toString() in arrayOf("n", "vn") && segment[indexTmp]?.word in arrayOf("欠费")) {
                            if (numberStr?.contains("") == false) {
                                numberStr = "-$numberStr"
                            }
                            finded = true
                            break
                        } else if (segment[indexTmp]?.nature.toString() in arrayOf("n", "vn") && segment[indexTmp]?.word in arrayOf("剩余")) {
                            if (indexTmp - 1 > 0 && segment[indexTmp - 1]?.nature.toString() == "n" && segment[indexTmp - 1]?.word.toString() == "话费") {
                                finded = true
                                break
                            } else {
                                break
                            }
                        } else {
                            break
                        }
                    }
                    if (finded) {
                        if (termWithIndex.index + 1 < segment.size && segment[termWithIndex.index + 1]?.nature?.toString() == "q" && segment[termWithIndex.index + 1]?.word in arrayOf("元", "块")) {      //单位q
                            decorderResult = numberStr + "元"
                            break;
                        }
                    }
                }
            }

            it.decoderRes = (decorderResult ?: "- -")
        }

        rv_decode.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_decode.adapter = SmsDecodeContentAdapter(this, decodeResultList)

//        val textView = findViewById<TextView>(R.id.tv)
//        var segment : List<Term>
//        segment = HanLP.segment("本月实时话费158.00元，当前可用余额185.57元。5G智享套餐158元国内拨打国际长途优惠分钟数200分钟，5G智享套餐158元基本通话优惠分钟数397分钟，国内通用流量共80.00GB，当前可使用30.20GB")
//        segment = HanLP.segment("本月实时话费158.00元，当前可用余额185.57元。5G智享套餐158元国内拨打国际长途优惠分钟数200分钟，5G智享套餐158元基本通话优惠分钟数397分钟，国内通用流量共80.00GB，当前可使用30.20GB")
//        textView.setText(segment.toString())
    }

    private fun makeSureDir(context: Context) {
        val messagePath = File(path)
        if (messagePath.isFile) {
            messagePath.delete()
        }
        if (!messagePath.exists()) {
            messagePath.mkdirs()
        }

        this.assets.list("messagefiles")?.forEach {

            val buffer = ByteArray(5 * 1024)
            val tmpFile = File(path + File.separator + it)
            if (!tmpFile.exists()) {
                val input = BufferedInputStream(this.assets.open("messagefiles/${it}"))
                val output = BufferedOutputStream(FileOutputStream("${path}/${it}"))
                while (input.read(buffer) > 0) {
                    output.write(buffer)
                }
                output.flush()
            }
        }
        val input = this.assets.open("testjar.jar")
        val output = FileOutputStream(App.application.cacheDir.absolutePath + File.separator + "testjar.jar")
        val buffer = ByteArray(5 * 1024)
        try {
            while (input.read(buffer) >= 0) {
                output.write(buffer)
            }
        } catch (e: Exception) {
        } finally {
            output.flush()
            output.close()
            input.close()
        }

    }
}