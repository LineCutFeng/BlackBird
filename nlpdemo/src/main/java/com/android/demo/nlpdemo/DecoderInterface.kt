package com.android.demo.nlpdemo

interface DecoderInterface {
    fun decodeLines(/*lineNumber: Int, */length: Int): List<String>
    fun setFilePath(path : String)
}