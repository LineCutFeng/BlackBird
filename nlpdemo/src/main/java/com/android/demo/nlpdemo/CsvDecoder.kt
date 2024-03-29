package com.android.demo.nlpdemo

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.lang.Exception

class CsvDecoder() : DecoderInterface {

    private var file: File? = null
    var bufferedReader : BufferedReader? = null

    override fun setFilePath(path : String) {
        file = File(path)
        kotlin.runCatching { bufferedReader?.close() }
        bufferedReader = BufferedReader(FileReader(file))
    }

    override fun decodeLines(readLineSize: Int): List<String> {

        val res = mutableListOf<String>()

        while (res.size < readLineSize) {
            try {
                val tmpLine = bufferedReader?.readLine()

                if (tmpLine == null) return res;
                else res.add(tmpLine)
            } catch (e : Exception) {
                return res
            }
        }
        return res
    }

}