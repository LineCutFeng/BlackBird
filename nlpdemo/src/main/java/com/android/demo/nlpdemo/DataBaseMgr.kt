package com.android.demo.nlpdemo

import android.content.Context
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

object DataBaseMgr {

    val path = App.application.filesDir.absolutePath + File.separator + "messagefiles"

    fun makeSureDir(context: Context) {
        val messagePath = File(path)
        if (messagePath.isFile) {
            messagePath.delete()
        }
        if (!messagePath.exists()) {
            messagePath.mkdirs()
        }

        context.assets.list("messagefiles")?.forEach {

            val buffer = ByteArray(5 * 1024)
            val tmpFile = File(path + File.separator + it)
            if (!tmpFile.exists()) {
                val input = BufferedInputStream(context.assets.open("messagefiles/${it}"))
                val output = BufferedOutputStream(FileOutputStream("${path}/${it}"))
                while (input.read(buffer) > 0) {
                    output.write(buffer)
                }
                output.flush()
            }
        }
        val input = context.assets.open("testjar.jar")
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