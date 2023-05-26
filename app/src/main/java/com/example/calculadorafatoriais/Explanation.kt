package com.example.calculadorafatoriais

import android.app.ActionBar
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannedString
import android.text.style.StrikethroughSpan
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
                binding.formulaPresentation.addView(writeOperationWithFraction(this.requireContext(), args.operation))
                binding.formulaWithValues.addView(writeOperationWithFraction(this.requireContext(), args.operation, args.n, args.k))
                if (args.operation != 2) { binding.developmentLayout.addView(writeFraction(this.requireContext(), args.n, args.k, args.operation)) }
                binding.developmentLayout.addView(writeFraction(this.requireContext(), args.n, args.k, args.operation, true))

            }
        }
        return binding.root
    }
}

/**
* Retorna uma string que representa uma sequência de multiplicações que vai de rangeStart até
 * rangeEnd de forma decrescente.
*  */
private fun factorialMultiplicationsString(rangeStart: Int, rangeEnd: Int = 1): SpannableString {
    var seriesText = ""
    for (i in rangeStart downTo rangeEnd) {  // Printa sequência de valores multiplicadas no fatorial
        if (i != rangeEnd) {
            seriesText += "$i x "
        }
        else {
            seriesText += if (rangeEnd == 1) "$i" else "$i!"
        }
    }
    val seriesTextString = SpannableString(seriesText)
    if (rangeEnd != 1) {  // rangeEnd só não é um quando se usa a função para demonstrar corte do fatorial por causa de uma divisão
        val spanStart = seriesText.indexOf(seriesText.split(" x ").last())  // Dá o índice do último número da série de multiplicações na string
        seriesTextString.setSpan(StrikethroughSpan(), spanStart, spanStart + seriesText.split(" x ").last().length, SpannedString.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    return seriesTextString
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

private fun createFormulaPartTextView(context: Context, text: SpannableString, background: Int? = null): TextView {
    val textView = TextView(context)
    textView.text = text
    textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
    if (background != null) textView.background = ContextCompat.getDrawable(context, background)
    return textView
}

private fun writePermutation(context: Context,n: String = "n", isDeveloping: Boolean = false): LinearLayout {
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

private fun writeOperationWithFraction(context: Context, operationType: Int, n: String = "n", k: String = "k"): LinearLayout {
    val formulaParentLayout = createParentLinearLayout(context)
    val formulaNameString = when (operationType) {
        1 -> "A$n,$k = "
        2 -> "P$n,$k = "
        else -> "C$n,$k = "
    }
    val formulaNameView = createFormulaDefTextView(context, formulaNameString, 1, n.length + k.length + 2)
    formulaParentLayout.addView(formulaNameView)
    val formulaDefLayout = LinearLayout(context)  // LinearLayout que contém o numerador e denominador da fórmula
    formulaDefLayout.orientation = LinearLayout.VERTICAL
    val numeratorTextView = createFormulaPartTextView(context, "$n!")
    val denominatorString = when (operationType) {
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

private fun writeFraction(context: Context, n: String, k: String, operationType: Int, isDeveloping: Boolean = false): LinearLayout {
    val formulaParentLayout = createParentLinearLayout(context)
    val formulaNameString = when (operationType) {
        1 -> "A$n,$k = "
        2 -> "P$n,$k = "
        else -> "C$n,$k = "
    }
    val formulaNameView = createFormulaDefTextView(context, formulaNameString, 1, n.length + k.length + 2)
    formulaParentLayout.addView(formulaNameView)
    val formulaDefLayout = LinearLayout(context)  // LinearLayout que contém o numerador e denominador da fórmula
    formulaDefLayout.orientation = LinearLayout.VERTICAL
    val numeratorTextView: TextView
    val denominatortext: String
    val denominatorTextView: TextView
    if (!isDeveloping) {
        numeratorTextView = createFormulaPartTextView(context, "$n!")
        denominatortext = if (operationType == 1) "${n.toInt() - k.toInt()}!" else "$k! x ${n.toInt() - k.toInt()}!"  // Texto de arranjo ou combinação
        denominatorTextView = createFormulaPartTextView(context, denominatortext, R.drawable.fraction_symbol)
        formulaDefLayout.addView(numeratorTextView)
        formulaDefLayout.addView(denominatorTextView)
        formulaParentLayout.addView(formulaDefLayout)
        return formulaParentLayout
    }
    else {
        val rangeEnd: Int = when (operationType) {  // Valor onde string do fatorial deve acabar de acordo com operação desempenhada
            1-> n.toInt() - k.toInt()  // Arranjo
            2-> k.toInt()  // Permutação com Repetição
            else -> if (k.toInt() > n.toInt() - k.toInt()) k.toInt() else (n.toInt() - k.toInt())  // Combinação
        }
        numeratorTextView = createFormulaPartTextView(context, factorialMultiplicationsString(n.toInt(), rangeEnd))
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
                val denominatorSpannableText = "$k! x (${n.toInt() - k.toInt()}!)"
                denominatorSpannable = SpannableString(denominatorSpannableText)
                val splitText = denominatorSpannableText.split(" x ")
                val rangeStart: Int
                val spanRangeEnd: Int
                if (k.toInt() > n.toInt() - k.toInt()) {
                    rangeStart = denominatorSpannableText.indexOf(splitText[0])  // Primeiro valor da string do denominador
                    spanRangeEnd = rangeStart + splitText[0].length
                }
                else  {
                    rangeStart = denominatorSpannableText.indexOf(splitText[1])  // Segundo valor da string do denominador
                    spanRangeEnd = rangeStart + splitText[1].length
                }
                denominatorSpannable.setSpan(StrikethroughSpan(),rangeStart, spanRangeEnd, SpannedString.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        denominatorTextView = createFormulaPartTextView(context, denominatorSpannable, R.drawable.fraction_symbol)
        formulaDefLayout.addView(numeratorTextView)
        formulaDefLayout.addView(denominatorTextView)
        formulaParentLayout.addView(formulaDefLayout)
        return formulaParentLayout
    }
}