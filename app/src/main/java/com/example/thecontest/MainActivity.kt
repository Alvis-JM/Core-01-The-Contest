package com.example.thecontest

import android.graphics.Color
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        Log.i("TAG", "onStart")
    }

    var count = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        Log.i("TAG", "onCreate")

        //connecting xml view with java view variables
        val score = findViewById<Button>(R.id.btn_score);
        val steal = findViewById<Button>(R.id.btn_steal);
        val reset = findViewById<Button>(R.id.btn_reset);
        val result = findViewById<TextView>(R.id.result);

        val sharePref = getSharedPreferences("pref", MODE_PRIVATE);

        // 0 is the default value given in case the value is not found
        count = sharePref.getInt("num", 0)

        Log.i("TAG", "" + count);

        // Setting the textview to last stored value so that on rotation we can see that stored value
        result.setText(Integer.toString(count))

        check(count, result)

        if (count > 14) {

            sharePref.edit().putInt("num", count).apply()
            count = 0
        }

        //function called checkBounds() to set max count to 15
        fun checkBounds() {
            score.setEnabled(count < 15);
            steal.setEnabled(count > 0);
        }

        var mediaPlayer = MediaPlayer.create(this, R.raw.beep_short); //audio

        score.setOnClickListener {
            count++
            result.text = count.toString()
            sharePref.edit().putInt("num", count).apply()

            check(count, result)
            checkBounds()
            mediaPlayer.start()
        }

        steal.setOnClickListener {
            count--
            result.text = count.toString()
            sharePref.edit().putInt("num", count).apply()

            check(count, result)
            checkBounds();
            mediaPlayer.start()
        }

        reset.setOnClickListener {
            count = 0

            result.text = count.toString()
            sharePref.edit().putInt("num", count).apply()
            check(count, result)
            mediaPlayer.start()
        }
    }

    fun check(increment_number: Int, result: TextView) {


        if (increment_number >= 10) {
            result.setTextColor(Color.GREEN)
        } else if (increment_number >= 5) {
            result.setTextColor(Color.BLUE)
        }
        else {
            result.setTextColor(Color.BLACK)
        }
    }

    //Use onSaveInstanceState(Bundle) and onRestoreInstanceState
    override fun onSaveInstanceState(savedInstanceState: Bundle) {

        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putInt("MyInt", count)
        super.onSaveInstanceState(savedInstanceState)
    }

    //onRestoreInstanceState
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Restore UI state from the savedInstanceState.
        count = savedInstanceState.getInt("MyInt")
    }


}