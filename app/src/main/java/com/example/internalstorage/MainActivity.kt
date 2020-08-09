package com.example.internalstorage

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.util.*


class MainActivity : AppCompatActivity() {
    //    private val FILE_NAME = "myTestFile.txt"
    var mEditText: EditText? = null
    var isClicked: Boolean = false
    var allTextSaveFun: String = ""
    var textInTheFile: String = ""
    private var counterOfFile1 = 0
    private var counterOfFile2 = 1
    private var fileAllName = "myTestFile"+counterOfFile1
    private var FILE_NAME = "myTestFile$counterOfFile1.txt"
    // i thought maybe its good to separate the counters, not for now, so the line under get "//"
//    private val FILE_NAMECounter2 = "myTestFile$counterOfFile2.txt"
    private var maxCharsInFile = 200
    private var bool = true

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    private val handler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mEditText = findViewById(R.id.edit_text)
        val buttonSave = findViewById<Button>(R.id.button_save)
        buttonSave.setOnClickListener {
            if (!isClicked) {
                startTimer()
                isClicked = true
                switch1.isChecked = true
            } else {
                stopTimer()
                isClicked = false
                switch1.isChecked = false
            }
        }
        val buttonLoad = findViewById<Button>(R.id.button_load)
        buttonLoad.setOnClickListener { load() }
        val buttonClear = findViewById<Button>(R.id.buttonClearData)
        buttonClear.setOnClickListener { ClearData() }
        val buttonCleanText = findViewById<Button>(R.id.buttonCleanText)
        buttonCleanText.setOnClickListener { CleanTextBox() }
    }

    private fun CleanTextBox() {
        mEditText!!.text.clear()
    }

    private fun stopTimer() {
        if (timer != null) {
            timer!!.cancel()
            timer!!.purge()
            Log.d("tag", "Stop Timer- Text in the file: $textInTheFile")
        }
    }

    private fun startTimer() {
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                handler.post {

                    ifFun()
                    // we don't need the stop timer now, but it an option to remember.
//                        stopTimer()
                    // if the condition not true rise the counter and run function again.
                    if(bool == false) {
                        counterOfFile1++
                        FILE_NAME = "myTestFile$counterOfFile1.txt"
                        ifFun()
                    }
                }
            }
        }
        // start running after 1 seconds, continue each 1 seconds
        timer!!.schedule(timerTask, 1000, 1000)
    }

    private fun ifFun(){
        if (checkCharsNum() < maxCharsInFile * (counterOfFile1 + 1)) {
            openNewFile()
            bool = true
        }
        else bool = false
    }

    private fun openNewFile() {
        val text = mEditText!!.text.toString()
        // collect all the text lines
        allTextSaveFun += "$text ElorIsraeli"
        var fos: FileOutputStream? = null
        try {
            fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
            // writing the text we collect
            fos.write(allTextSaveFun.toByteArray())
//                mEditText!!.setText(allTextSaveFun)
            Log.d("tag", "Chars number: ${checkCharsNum()} - All Text is: $allTextSaveFun")
            Log.d("tag", "File: $FILE_NAME Text In The File is: $textInTheFile")
//            Toast.makeText(this, "Saved to $filesDir/$FILE_NAME", Toast.LENGTH_SHORT).show()
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
                Log.d("tag", "Wrote Text: $sb")
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
        textInTheFile = ""
        var fos: FileOutputStream? = null
        try {
            fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
            // writing the empty text, delete all another text
            fos.write(textInTheFile.toByteArray())
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

    // fun not in use, the first fun i work with.
    fun writeInTheFile() {
        val text = mEditText!!.text.toString()
        // collect all the text lines
        allTextSaveFun += "$text ElorIsraeli"
        var fos: FileOutputStream? = null
        try {
            fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
            // writing the text we collect
            fos.write(allTextSaveFun.toByteArray())
//                mEditText!!.setText(allTextSaveFun)
            Log.d("tag", "Chars number: ${checkCharsNum()} - All Text is: $allTextSaveFun")
            Log.d("tag", "File: $FILE_NAME Text In The File is: $textInTheFile")
            Toast.makeText(this, "Saved to $filesDir/$FILE_NAME", Toast.LENGTH_SHORT).show()
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

}
