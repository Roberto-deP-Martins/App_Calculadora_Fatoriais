package com.example.calculadorafatoriais.explanationWidgets

import android.content.Context
import android.text.SpannableString
import android.text.SpannedString
import android.text.style.StrikethroughSpan
import android.widget.LinearLayout
import android.widget.TextView
import com.example.calculadorafatoriais.R

/**Classse que representa layout para operações que são expressas por frações*/
class FractionLayout: ParentLinearLayout, Factorial {
    private lateinit var formulaNameView: FormulaDefinitionTextView
    private val formulaDefLayout = LinearLayout(context)
    private var numeratorTextView: DivisionPartTextView
    private var denominatorTextView: TextView

    constructor(context: Context, operationType: Int, n: String = "n", k: String = "k") : super(context) {
        formulaNameView = FormulaDefinitionTextView(context, n, k, operationType)
        addView(formulaNameView)
        formulaDefLayout.orientation = VERTICAL
        denominatorTextView = DivisionPartTextView(context, when (operationType) {
            1 -> "($n - $k)!"
            2 -> "$k!"
            else -> "$k!($n - $k)!"
        }, R.drawable.fraction_symbol)
        numeratorTextView = DivisionPartTextView(context, "$n!")
        formulaDefLayout.addView(numeratorTextView)
        formulaDefLayout.addView(denominatorTextView)
        addView(formulaDefLayout)
    }

    constructor(context: Context, operationType: Int,n: String, k: String, isDeveloping: Boolean) : super(context) {
        formulaNameView = FormulaDefinitionTextView(context, n, k, operationType)
        addView(formulaNameView)
        formulaDefLayout.orientation = VERTICAL
        if (!isDeveloping) {
            numeratorTextView = DivisionPartTextView(context, "$n!")
            val denominatortext = if (operationType == 1) "${n.toInt() - k.toInt()}!" else "$k! x ${n.toInt() - k.toInt()}!"  // Texto de arranjo ou combinação
            denominatorTextView = DivisionPartTextView(context, denominatortext, R.drawable.fraction_symbol)
            formulaDefLayout.addView(numeratorTextView)
            formulaDefLayout.addView(denominatorTextView)
            addView(formulaDefLayout)
        }
        else {
            numeratorTextView = DivisionPartTextView(context, factorialMultiplicationsString(n.toInt(), when (operationType) {  // Valor onde string do fatorial deve acabar de acordo com operação desempenhada
                1-> n.toInt() - k.toInt()  // Arranjo
                2-> k.toInt()  // Permutação com Repetição
                else -> if (k.toInt() > n.toInt() - k.toInt()) k.toInt() else (n.toInt() - k.toInt())  // Combinação
            }, true))
            val denominatorSpannable: SpannableString
            when (operationType) {
                1-> {  // Arranjo
                    denominatorSpannable = SpannableString("${n.toInt() - k.toInt()}!")
                    denominatorSpannable.setSpan(StrikethroughSpan(),0, (n.toInt() - k.toInt()).toString().length + 1, SpannedString.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                2-> {  // Permutação com Repetição
                    denominatorSpannable = SpannableString("$k!")
                    denominatorSpannable.setSpan(StrikethroughSpan(),0, k.length + 1, SpannedString.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                else -> {  // Combinação
                    val denominatorSpannableText : String
                    val rangeStart: Int
                    val spanRangeEnd: Int
                    if (k.toInt() > n.toInt() - k.toInt()) {
                        denominatorSpannableText = "$k! x ${factorialMultiplicationsString(n.toInt() - k.toInt())}"
                        rangeStart = 0  // Primeiro valor da string do denominador
                        spanRangeEnd = k.length + 1
                    }
                    else  {
                        denominatorSpannableText = "${factorialMultiplicationsString(k.toInt())} x ${n.toInt() - k.toInt()}!"
                        rangeStart = denominatorSpannableText.lastIndex - (n.toInt() - k.toInt()).toString().length  // Segundo valor da string do denominador
                        spanRangeEnd = rangeStart + (n.toInt() - k.toInt()).toString().length + 1
                    }
                    denominatorSpannable = SpannableString(denominatorSpannableText)
                    denominatorSpannable.setSpan(StrikethroughSpan(),rangeStart, spanRangeEnd, SpannedString.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
            denominatorTextView = DivisionPartTextView(context, denominatorSpannable, R.drawable.fraction_symbol)
            formulaDefLayout.addView(numeratorTextView)
            formulaDefLayout.addView(denominatorTextView)
            addView(formulaDefLayout)
        }
    }
}