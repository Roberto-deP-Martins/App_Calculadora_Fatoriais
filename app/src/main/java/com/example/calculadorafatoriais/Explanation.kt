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
        when (args.operation) {
            0 -> {  // Permutação
                binding.formulaPresentation.addView(writePermutation(this.requireContext()))
                binding.formulaWithValues.addView(writePermutation(this.requireContext(), args.n))
                binding.developmentLayout.addView(writePermutation(this.requireContext(), args.n, true))
            }
            3 -> {  // Arranjo com Repetição
                binding.formulaPresentation.addView(writeArranjoRepeticao(this.requireContext()))
                binding.formulaWithValues.addView(writeArranjoRepeticao(this.requireContext(), args.n, args.k))
                val developmentText = TextView(this.requireContext())
                developmentText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
                developmentText.text = "Ar = " + powerMultiplicationsString(args.n.toInt(), args.k.toInt())
                developmentText.layoutParams = configureLayoutParams(this.requireContext())
                binding.developmentLayout.addView(developmentText)
            }
            else -> {
                binding.formulaPresentation.addView(writeFormulaWithFraction(this.requireContext(), args.operation))
                binding.formulaWithValues.addView(writeFormulaWithFraction(this.requireContext(), args.operation, args.n, args.k))
                // binding.formulaWithValues.addView(writeFormulaWithFraction(this.requireContext(), args.operation, args.n, args.k))  // TODO:
            }
        }
        return binding.root
    }
}

/**
* Retorna uma string que representa uma sequência de multiplicações que vai de rangeStart até
 * rangeEnd de forma decrescente.
*  */
private fun factorialMultiplicationsString(rangeStart: Int): String {
    var seriesText = ""
    for (i in rangeStart downTo 1) {  // Printa sequência de valores multiplicadas no fatorial
        seriesText += if (i != 1) "$i x " else "$i"
    }
    return seriesText
}

private fun powerMultiplicationsString(base: Int, power: Int): String {
    if (power == 0) return "1" else if (power > 5) return  "$base x $base x ... x $base x $base"
    var seriesText = ""
    for (i in power downTo 1) {  // Printa sequência de valores multiplicadas no fatorial
        seriesText += if (i != 1) "$base x " else "$base"
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
            text = text + "${(n.toInt() - 1)} x ... x " + factorialMultiplicationsString(3)
        }
        else {  // Printa sequência de valores multiplicadas no fatorial
            text += factorialMultiplicationsString(n.toInt() - 1)
        }
    }
    formulaParentLayout.addView(createFormulaDefTextView(context, text, 1, 1 + n.length))
    return formulaParentLayout
}

private fun writeArranjoRepeticao(context: Context, n: String = "n", k: String = "k"): LinearLayout {
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