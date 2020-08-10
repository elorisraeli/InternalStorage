package com.example.internalstorage

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_extra.*
import java.io.*

class ExtraActivity : AppCompatActivity() {
    private val FILE_NAME_TEST = "anotherTestFile.txt"
    var textInTheFile2: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extra)

        imageViewPlay2.setOnClickListener {
            loadFileText()
            insertData(imageViewPlay2)
        }
        checkBoxPlay2.setOnClickListener {
            loadFileText()
            insertData(checkBoxPlay2)
        }
        radioButtonPlay2.setOnClickListener {
            loadFileText()
            insertData(radioButtonPlay2)
        }
        switchPlay2.setOnClickListener {
            loadFileText()
            insertData(switchPlay2)
        }
    }

    private fun insertData(view: View) {
        val viewID: String = "{activityType: " + 2 + ", data: " + view.tag + "}"
        textInTheFile2 += viewID
        var fos: FileOutputStream? = null
        try {
            fos = openFileOutput(FILE_NAME_TEST, Context.MODE_PRIVATE)
            // writing the text we collect
            fos.write(textInTheFile2.toByteArray())
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (fos != null) {
                try {
                    fos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun loadFileText() {
        var fis: FileInputStream? = null
        try {
            fis = openFileInput(FILE_NAME_TEST)
            val isr = InputStreamReader(fis)
            val br = BufferedReader(isr)
            val sb = StringBuilder()
            var text: String?
            while (br.readLine().also { text = it } != null) {
                // add the text, a line down
                sb.append(text).append("\n")
                // just some checks
                Log.d("tag", "FileName: $FILE_NAME_TEST Wrote Text: $sb")
            }
            // to be able use the read text, universal variable.
            textInTheFile2 = sb.toString()
            Log.d("tag", "Done to load, text in the file: $textInTheFile2")
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (fis != null) {
                try {
                    fis.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}
