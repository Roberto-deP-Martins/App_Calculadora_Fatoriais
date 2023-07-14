package com.example.calculadorafatoriais.explanationWidgets

import android.content.Context
import android.text.SpannableString
import android.text.SpannedString
import android.text.style.SuperscriptSpan
import android.widget.LinearLayout

class ArranjoRepeticao(context: Context, n: String = "n", k: String = "k") : LinearLayout(context) {
    init {
        val formulaPartString = SpannableString("$n$k")
        formulaPartString.setSpan(SuperscriptSpan(), n.length, n.length + k.length, SpannedString.SPAN_EXCLUSIVE_EXCLUSIVE)
        val formulaPartTextView = DivisionPartTextView(context,formulaPartString)
        addView(FormulaDefinitionTextView(context, n, k, 3))
        addView(formulaPartTextView)
    }
}