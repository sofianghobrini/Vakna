package com.app.vakna

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.app.vakna.controller.ControllerDetailsCompagnon
import com.app.vakna.controller.ControllerDetailsObjet
import com.app.vakna.databinding.ActivityDetailsCompagnonBinding
import com.app.vakna.databinding.ActivityDetailsObjetBinding

class DetailsCompagnonActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsCompagnonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsCompagnonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ControllerDetailsCompagnon(binding, intent)
    }



    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = android.graphics.Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    v.clearFocus()
                    hideKeyboard(v)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun hideKeyboard(view: android.view.View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}