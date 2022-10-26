package com.android.demo.nlpdemo.resolver

import com.hankcs.hanlp.HanLP
import com.hankcs.hanlp.seg.common.Term

class HanLpResolver : IResolver {

    override fun resolve(smsContent: String) : String? {
        val segment: List<Term?> = HanLP.segment(smsContent)
        //val dividedString = JiebaSegmenter.getJiebaSegmenterSingleton().getDividedString(it.smsContent)

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
        return decorderResult
    }
}