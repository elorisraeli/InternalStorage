package com.example.internalstorage

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.util.*


class MainActivity : AppCompatActivity() {
    private val FILE_NAME = "myTestFile.txt"
    var mEditText: EditText? = null
    var isClicked: Boolean = false
    var allText: String = ""

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    private val handler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mEditText = findViewById(R.id.edit_text)
        val buttonSave = findViewById<Button>(R.id.button_save)
        buttonSave.setOnClickListener {
//            save()
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
    }

    private fun stopTimer() {
        if (timer != null) {
            timer!!.cancel()
            timer!!.purge()
        }
    }

    private fun startTimer() {
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                handler.post {
                    save()
                }
            }
        }
        // start running after 3 seconds, continue each 3 seconds
        timer!!.schedule(timerTask, 3000, 3000)
    }

    private fun save() {
        val text = mEditText!!.text.toString()
        // collect all the text lines
        allText += text
        var fos: FileOutputStream? = null
        try {
            fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
            // writing the text we collect
            fos.write(allText.toByteArray())
            mEditText!!.text.clear()
            Toast.makeText(this, "Saved to $filesDir/$FILE_NAME", Toast.LENGTH_LONG).show()
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
                sb.append("\n").append(text)
            }
            // deployed the data in the edit text
            mEditText!!.setText(sb.toString())
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
}
