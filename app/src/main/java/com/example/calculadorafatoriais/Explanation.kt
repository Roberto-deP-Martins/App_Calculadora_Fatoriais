package com.example.calculadorafatoriais

import android.app.ActionBar
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannedString
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import com.example.calculadorafatoriais.databinding.FragmentExplanationBinding


class Explanation : Fragment() {
    lateinit var binding: FragmentExplanationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentExplanationBinding.inflate(inflater, container, false)
        val args = ExplanationArgs.fromBundle(requireArguments())
        val operationsArray = resources.getStringArray(R.array.spinnerOptions).map { it.lowercase() }
        val operationName = when (args.operation) {
            0 -> operationsArray[0]  // Permutação
            1 -> operationsArray[1]  // Arranjo
            2 -> operationsArray[2]  // Permutação com Repetição
            3 -> operationsArray[3]  // Arranjo com Repetição
            4 -> operationsArray[4]  // Combinação
            else -> operationsArray[0]
        }
        binding.explanationIntro.text = getString(R.string.explanationIntro, "$operationName:")
        binding.formulaPresentation.addView(when (args.operation) {
            0 -> writePermutation(this.requireContext())
            3 -> writeArranjo(this.requireContext())
            else -> writeFormulaWithFraction(this.requireContext(), args.operation)
        })
        binding.formulaWithValues.addView(when (args.operation) {
            0 -> writePermutation(this.requireContext(), args.n)
            3 -> writeArranjo(this.requireContext(), args.n, args.k)
            else -> writeFormulaWithFraction(this.requireContext(), args.operation, args.n, args.k)
        })
        binding.developmentLayout.addView(when (args.operation) {
            0 -> writePermutation(this.requireContext(), args.n, true)
            3 -> writeArranjo(this.requireContext(), args.n, args.k)  // TODO:
            else -> writeFormulaWithFraction(this.requireContext(), args.operation, args.n, args.k)  // TODO:
        })
        return binding.root
    }
}

/**
* Retorna uma string que representa uma sequência de multiplicações que vai de rangeStart até
 * rangeEnd de forma decrescente.
*  */
private fun multiplicationSeriesString(rangeStart: Int, rangeEnd: Int): String {
    var seriesText = ""
    for (i in rangeStart downTo rangeEnd) {  // Printa sequência de valores multiplicadas no fatorial
        seriesText += if (i != 1) "$i x " else "$i"
    }
    return seriesText
}

private fun dpToPx(context: Context): Int {
    val r: Resources = context.resources
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20F, r.displayMetrics).toInt()
}

private fun createParentLinearLayout(context: Context): LinearLayout {
    val linearLayout = LinearLayout(context)
    linearLayout.orientation = LinearLayout.HORIZONTAL
    linearLayout.layoutParams = configureLayoutParams(context)
    return linearLayout
}

private fun configureLayoutParams(context: Context): ActionBar.LayoutParams {  // Configura margem do LinearLayout
    val params = ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT)
    val px = dpToPx(context)
    params.setMargins(px, px, 0, 0)
    return params
}

private fun createFormulaDefTextView(context: Context, text: String, spanStart: Int, spanEnd: Int, isArranjo: Boolean = false): TextView {
    val textView = TextView(context)
    val formulaDefString = SpannableString(text)
    val spanType = if (isArranjo) SuperscriptSpan() else SubscriptSpan()
    formulaDefString.setSpan(spanType, spanStart, spanEnd, SpannedString.SPAN_EXCLUSIVE_EXCLUSIVE)
    textView.setText(formulaDefString, TextView.BufferType.SPANNABLE)
    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
    return textView
}

private fun createFormulaPartTextView(context: Context, text: String, background: Int? = null): TextView {
    val textView = TextView(context)
    textView.text = text
    textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
    if (background != null) textView.background = ContextCompat.getDrawable(context, background)
    return textView
}

private fun writePermutation(context: Context,n: String = "n", isDeveloping:Boolean = false): LinearLayout {
    val formulaParentLayout = createParentLinearLayout(context)
    var text = "P$n = "
    if (!isDeveloping || !n.isDigitsOnly()) {  // Será executado antes do passo de desevolvimento da expressão
        text += "$n!"
    }
    else {  // Será executado no passo de desevolvimento
        text += "$n x "
        if (n.toInt() > 5) {  // Mostra primeiro item da sequência seguido de reticências para indicar outras multiplicações e depois a sequência
            text = text + "${(n.toInt() - 1)} x ... x " + multiplicationSeriesString(3, 1)
        }
        else {  // Printa sequência de valores multiplicadas no fatorial
            text += multiplicationSeriesString(n.toInt() - 1, 1)
        }
    }
    formulaParentLayout.addView(createFormulaDefTextView(context, text, 1, 1 + n.length))
    return formulaParentLayout
}

private fun writeArranjo(context: Context, n: String = "n", k: String = "k"): LinearLayout {
    val formulaParentLayout = createParentLinearLayout(context)
    formulaParentLayout.addView(createFormulaDefTextView(context, "Ar = $n$k", 5 + n.length, 5 + n.length + k.length, true))
    return formulaParentLayout
}

private fun writeFormulaWithFraction(context: Context, opeationType: Int, n: String = "n", k: String = "k"): LinearLayout {
    val formulaParentLayout = createParentLinearLayout(context)
    val formulaNameString = when (opeationType) {
        1 -> "A$n,$k = "
        2 -> "P$n,$k = "
        else -> "C$n,$k = "
    }
    val formulaNameView = createFormulaDefTextView(context, formulaNameString, 1, n.length + k.length + 2)
    formulaParentLayout.addView(formulaNameView)
    val formulaDefLayout = LinearLayout(context)  // LinearLayout que contém o numerador e denominador da fórmula
    formulaDefLayout.orientation = LinearLayout.VERTICAL
    val numeratorTextView = createFormulaPartTextView(context, "$n!")
    val denominatorString = when (opeationType) {
        1 -> "($n - $k)!"
        2 -> "$k!"
        else -> "$k!($n - $k)!"
    }
    val denominatorTextView = createFormulaPartTextView(context, denominatorString, R.drawable.fraction_symbol)
    formulaDefLayout.addView(numeratorTextView)
    formulaDefLayout.addView(denominatorTextView)

    formulaParentLayout.addView(formulaDefLayout)
    return formulaParentLayout
}