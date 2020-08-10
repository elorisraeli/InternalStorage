package com.example.internalstorage

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_extra.*
import java.io.*
import java.util.*

class ExtraActivity : AppCompatActivity() {
    private val FILE_NAME_TEST = "anotherTestFile.txt"
    private var bool = true
    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    private val handler: Handler = Handler()
    var textInTheFile2: String = ""
    private var counterOfFile2 = 0
    private var FILE_NAME = "myTestFile$counterOfFile2.txt"
    private var maxCharsInFile2 = 400


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extra)

        imageViewPlay2.setOnClickListener { startTimer(imageViewPlay2) }
        checkBoxPlay2.setOnClickListener { startTimer(checkBoxPlay2) }
        radioButtonPlay2.setOnClickListener { startTimer(radioButtonPlay2) }
        switchPlay2.setOnClickListener { startTimer(switchPlay2) }
    }

    private fun insertData(view: View) {
        val viewID: String = "{activityType: " + 2 + ", data: " + view.tag + "}"
        textInTheFile2 += viewID
        var fos: FileOutputStream? = null
        try {
            fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
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

    fun load() {
        var fis: FileInputStream? = null
        try {
            fis = openFileInput(FILE_NAME)
            val isr = InputStreamReader(fis)
            val br = BufferedReader(isr)
            val sb = StringBuilder()
            var text: String?
            while (br.readLine().also { text = it } != null) {
                // add the text, a line down
                sb.append(text).append("\n")
                // just some checks
                Log.d("tag", "FileName: $FILE_NAME Wrote Text: $sb")
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

    private fun stopTimer() {
        if (timer != null) {
            timer!!.cancel()
            timer!!.purge()
        }
    }

    private fun startTimer(view: View) {
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                handler.post {
                    ifFun2(view)
                    load()
                    // if the condition not true rise the counter, change file name,
                    // clear the text in the file variable and run function again.
                    if (!bool) {
                        counterOfFile2++
                        FILE_NAME = "myTestFile$counterOfFile2.txt"
                        textInTheFile2 = ""
                        ifFun2(view)
                    }
                }
            }
        }
        // start running after 1 seconds, continue each 2 seconds
        timer!!.schedule(timerTask, 1000, 2000)
    }

    private fun ifFun2(view: View) {
        if (checkCharsNum2() < maxCharsInFile2) {
            insertData(view)
            load()
            bool = true
        } else bool = false
    }

    fun checkCharsNum2(): Int {
        var fis: FileInputStream? = null
        try {
            fis = openFileInput(FILE_NAME)
            val isrCheck = InputStreamReader(fis)
            val brCheck = BufferedReader(isrCheck)
            val sbCheck = StringBuilder()
            var text: String?
            while (brCheck.readLine().also { text = it } != null) {
                // add the text, a line down
                sbCheck.append(text).append("\n")
            }
            textInTheFile2 = sbCheck.toString()
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
        // return the num of chars in the file
        return textInTheFile2.length
    }

//    private fun insertData(view: View) {
//        val viewID: String = "{activityType: " + 2 + ", data: " + view.tag + "}"
//        textInTheFile2 += viewID
//        var fos: FileOutputStream? = null
//        try {
//            fos = openFileOutput(FILE_NAME_TEST, Context.MODE_PRIVATE)
//            // writing the text we collect
//            fos.write(textInTheFile2.toByteArray())
//        } catch (e: FileNotFoundException) {
//            e.printStackTrace()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        } finally {
//            if (fos != null) {
//                try {
//                    fos.close()
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//            }
//        }
//    }
//
//    fun loadFileText() {
//        var fis: FileInputStream? = null
//        try {
//            fis = openFileInput(FILE_NAME_TEST)
//            val isr = InputStreamReader(fis)
//            val br = BufferedReader(isr)
//            val sb = StringBuilder()
//            var text: String?
//            while (br.readLine().also { text = it } != null) {
//                // add the text, a line down
//                sb.append(text).append("\n")
//                // just some checks
//                Log.d("tag", "FileName: $FILE_NAME_TEST Wrote Text: $sb")
//            }
//            // to be able use the read text, universal variable.
//            textInTheFile2 = sb.toString()
//            Log.d("tag", "Done to load, text in the file: $textInTheFile2")
//        } catch (e: FileNotFoundException) {
//            e.printStackTrace()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        } finally {
//            if (fis != null) {
//                try {
//                    fis.close()
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//            }
//        }
//    }
}
