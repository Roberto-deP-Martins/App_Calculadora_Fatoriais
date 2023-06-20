package com.example.calculadorafatoriais.explanationWidgets

import android.content.Context
import android.text.SpannableString
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat

class DivisionPartTextView: androidx.appcompat.widget.AppCompatTextView {

    constructor(context: Context, _text: String, _background: Int? = null) : super(context) {
        text = _text
        textAlignment = View.TEXT_ALIGNMENT_CENTER
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
        if (_background != null) background = ContextCompat.getDrawable(context, _background)
    }

    constructor(context: Context, _text: SpannableString, _background: Int? = null) : super(context) {
        text = _text
        textAlignment = View.TEXT_ALIGNMENT_CENTER
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
        if (_background != null) background = ContextCompat.getDrawable(context, _background)
    }
}