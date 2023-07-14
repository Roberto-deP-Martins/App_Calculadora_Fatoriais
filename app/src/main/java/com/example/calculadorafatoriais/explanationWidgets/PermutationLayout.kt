package com.example.calculadorafatoriais.explanationWidgets

import android.content.Context
import androidx.core.text.isDigitsOnly

/**Classe que representa Layout que abriga Permutação*/
class PermutationLayout(context: Context, n: String = "n", isDeveloping: Boolean = false) : ParentLinearLayout(context), Factorial {
    init {
        val formulaDefTextView = FormulaDefinitionTextView(context, n, operationValue = 0)
        val formulaText = if (!isDeveloping || !n.isDigitsOnly()) "$n!" else factorialMultiplicationsString(n.toInt())
        val formulaValuesTextView = DivisionPartTextView(context, formulaText.toString())
        this.addView(formulaDefTextView)
        this.addView(formulaValuesTextView)
    }
}