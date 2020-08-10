package com.example.internalstorage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Text
import java.io.*
import java.util.*


class MainActivity : AppCompatActivity() {
    var mEditText: EditText? = null
    var textInTheFile: String = ""
    private var counterOfFile1 = 0
    private val FILE_NAME_TEST = "anotherTestFile.txt"

    var isClicked: Boolean = false
    var allTextSaveFun: String = ""
    private var FILE_NAME = "myTestFile$counterOfFile1.txt"
    private var maxCharsInFile = 400
    private var bool = true
    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    private val handler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mEditText = findViewById(R.id.edit_text)
        // Have been used before.
//        val buttonSave = findViewById<Button>(R.id.button_save)
//        buttonSave.setOnClickListener {
//            if (!isClicked) {
//                startTimer()
//                isClicked = true
//                switch1.isChecked = true
//            } else {
//                stopTimer()
//                isClicked = false
//                switch1.isChecked = false
//            }
//        }
        val buttonLoad = findViewById<Button>(R.id.button_load)
        buttonLoad.setOnClickListener { load() }
        val buttonClear = findViewById<Button>(R.id.buttonClearData)
        buttonClear.setOnClickListener { ClearData() }
        val buttonCleanText = findViewById<Button>(R.id.buttonCleanText)
        buttonCleanText.setOnClickListener { CleanTextBox() }

        load()
        imageViewPlay.setOnClickListener { startTimer(imageViewPlay) }
        checkBoxPlay.setOnClickListener { startTimer(checkBoxPlay) }
        radioButtonPlay.setOnClickListener { startTimer(radioButtonPlay) }
        switchPlay.setOnClickListener { startTimer(switchPlay) }

        buttonNextActivity.setOnClickListener {
            val intent = Intent(this, ExtraActivity::class.java)
            startActivity(intent)
        }
    }

    private fun insertData(view: View) {
        val viewID: String = "{activityType: " + 1 + ", data: " + view.tag + "}"
        textInTheFile += viewID
        var fos: FileOutputStream? = null
        try {
            fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
            // writing the text we collect
            fos.write(textInTheFile.toByteArray())
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
            textInTheFile = sb.toString()
            // deployed the data in the edit text
            mEditText!!.setText(sb.toString())
            Log.d("tag", "Done to load, text in the file: $textInTheFile")
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

    fun ClearData() {
        val emptyText = ""
        var fos: FileOutputStream? = null
        try {
            fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
            // writing the empty text, delete all another text
            fos.write(emptyText.toByteArray())
            mEditText!!.text.clear()
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

    private fun CleanTextBox() {
        mEditText!!.text.clear()
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
                    ifFun(view)
                    load()
                    // if the condition not true rise the counter, change file name,
                    // clear the text in the file variable and run function again.
                    if (!bool) {
                        counterOfFile1++
                        FILE_NAME = "myTestFile$counterOfFile1.txt"
                        textInTheFile = ""
                        ifFun(view)
                    }
                    }
                }
            }
            // start running after 1 seconds, continue each 2 seconds
            timer!!.schedule(timerTask, 1000, 2000)
        }
        private fun ifFun(view: View) {
            if (checkCharsNum() < maxCharsInFile) {
                insertData(view)
                load()
                bool = true
            } else bool = false
        }

        fun checkCharsNum(): Int {
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
                textInTheFile = sbCheck.toString()
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
            return textInTheFile.length
        }
        // Have been used before.
//    private fun stopTimer() {
//        if (timer != null) {
//            timer!!.cancel()
//            timer!!.purge()
//            Log.d("tag", "Stop Timer- Text in the file: $textInTheFile")
//        }
//    }
//
//    private fun startTimer() {
//        timer = Timer()
//        timerTask = object : TimerTask() {
//            override fun run() {
//                handler.post {
//                    ifFun()
//                    // if the condition not true rise the counter and run function again.
//                    if (bool == false) {
//                        counterOfFile1++
//                        FILE_NAME = "myTestFile$counterOfFile1.txt"
//                        ifFun()
//                    }
//                }
//            }
//        }
//        // start running after 1 seconds, continue each 1 seconds
//        timer!!.schedule(timerTask, 1000, 1000)
//    }
//
        //
//    private fun openNewFile() {
//        val text = mEditText!!.text.toString()
//        // collect all the text lines
//        allTextSaveFun += "$text ElorIsraeli"
//        var fos: FileOutputStream? = null
//        try {
//            fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
//            // writing the text we collect
//            fos.write(allTextSaveFun.toByteArray())
//            Log.d("tag", "Chars number: ${checkCharsNum()} - All Text is: $allTextSaveFun")
//            Log.d("tag", "File: $FILE_NAME Text In The File is: $textInTheFile")
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

    }
