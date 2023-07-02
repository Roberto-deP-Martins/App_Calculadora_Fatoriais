package com.example.calculadorafatoriais.explanationWidgets

import android.content.Context
import android.text.SpannableString
import android.text.SpannedString
import android.text.style.SubscriptSpan
import android.util.TypedValue

/** TextView que inicializa com a notação de uma fórmula de Análise Combinatória, com
 * SpannableString. */
class FormulaDefinitionTextView(context: Context, n: String = "n", k: String = "k", operationValue: Int):
    androidx.appcompat.widget.AppCompatTextView(context) {
    init {
        val (formulaString, spanIndexes) = when (operationValue) {
            0 -> {
                Pair("P$n = ", Pair(1 , n.length + 1))
            }
            1 -> {
                Pair("A$n,$k = ", Pair(1, 2 + n.length + k.length))
            }
            2 -> {
                Pair("P$n,$k = ", Pair(1, 2 + n.length + k.length))
            }
            3 -> {
                Pair("Ar$n,$k = ", Pair(2, 3 + n.length + k.length))
            }
            else -> {
                Pair("C$n,$k = ", Pair(1, 2 + n.length + k.length))
            }
        }
        val spannableString = SpannableString(formulaString)
        spannableString.setSpan(SubscriptSpan(), spanIndexes.first, spanIndexes.second, SpannedString.SPAN_EXCLUSIVE_EXCLUSIVE)
        setText(spannableString, BufferType.SPANNABLE)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
    }
}