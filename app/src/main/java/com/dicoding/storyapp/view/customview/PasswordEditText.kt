package com.dicoding.storyapp.view.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewParent
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class PasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): TextInputEditText(context, attrs, defStyleAttr) {

    init {
        setupView()
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val parentTextInputLayout = findParentTextInputLayout()

                if (s.length < 8) {
                    parentTextInputLayout?.error = ("Password tidak boleh kurang dari 8 karakter")
                } else {
                    parentTextInputLayout?.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun setupView() {
        isFocusable = true
        isFocusableInTouchMode = true
        isClickable = true
        isEnabled = true
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        gravity = Gravity.CENTER_VERTICAL
    }

    private fun findParentTextInputLayout(): TextInputLayout? {
        var parentView: ViewParent? = parent
        while (parentView != null) {
            if (parentView is TextInputLayout) {
                return parentView
            }
            parentView = (parentView as? View)?.parent
        }
        return null
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = "Masukkan password"
        textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        textSize = 16f
    }
}