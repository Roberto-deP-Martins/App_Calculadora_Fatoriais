package com.example.calculadorafatoriais

import android.app.ActionBar
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import com.example.calculadorafatoriais.combinatorics.permutacao
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
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import com.example.calculadorafatoriais.databinding.FragmentExplanationBinding
import com.example.calculadorafatoriais.explanationWidgets.*


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
                if (args.operation == 4) {
                    val finalDevelopmentLayout = ParentLinearLayout(this.requireContext())
                    finalDevelopmentLayout.addView(createFormulaDefTextView(this.requireContext(), args.n, args.k, 4))
                    val fractionLayout = LinearLayout(this.requireContext())
                    fractionLayout.orientation = LinearLayout.VERTICAL
                    val numeratorRangeEnd: Int
                    val denominatorRangeStart: Int
                    if (args.k.toInt() > (args.n.toInt() - args.k.toInt())) {
                        numeratorRangeEnd = args.k.toInt()
                        denominatorRangeStart = args.n.toInt() - args.k.toInt()
                    }
                    else {
                        numeratorRangeEnd = (args.n.toInt()-args.k.toInt())
                        denominatorRangeStart = args.k.toInt()
                    }
                    val numeratorTextView = DivisionPartTextView(this.requireContext(), permutacao(args.n.toInt(), numeratorRangeEnd).toString())
                    val denominatorTextView = DivisionPartTextView(this.requireContext(), permutacao(denominatorRangeStart).toString(), R.drawable.fraction_symbol)
                    fractionLayout.addView(numeratorTextView)
                    fractionLayout.addView(denominatorTextView)
                    finalDevelopmentLayout.addView(fractionLayout)
                    binding.developmentLayout.addView(finalDevelopmentLayout)
                }

            }
        }
        binding.resultLayout.addView(presentResult(args.n, args.k, args.result, args.operation))
        return binding.root
    }

    /**
     * Retorna uma string que representa uma sequência de multiplicações que vai de rangeStart até
     * rangeEnd de forma decrescente.
     *  */
    private fun factorialMultiplicationsString(rangeStart: Int, rangeEnd: Int = 1, hasStrikethrough: Boolean = false): SpannableString {
        var seriesText = ""
        if (hasStrikethrough) {
            if (1 + rangeStart - rangeEnd > 3) {
                seriesText += "$rangeStart x ${rangeStart - 1} x ... x ${rangeEnd + 1} x ${rangeEnd}!"
            }
            else {
                for (i in 0..rangeStart - rangeEnd) {
                    seriesText += if (i != (rangeStart - rangeEnd)) "${rangeStart - i} x " else "${rangeStart - i}!"
                }
            }
        }
        else {
            if (rangeStart > 5) {
                seriesText += "$rangeStart x ${rangeStart - 1} x ... x " + factorialMultiplicationsString(3, rangeEnd)
            }
            else {
                for (i in rangeStart downTo rangeEnd) {  // Printa sequência de valores multiplicadas no fatorial
                    if (i != rangeEnd) {
                        seriesText += "$i x "
                    } else {
                        seriesText += if (rangeEnd == 1) "$i" else "$i!"
                    }
                }
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

    private fun dpToPx(context: Context, size: Float): Int {
        val r: Resources = context.resources
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, r.displayMetrics).toInt()
    }

    private fun configureLayoutParams(context: Context): ActionBar.LayoutParams {  // Configura margem do LinearLayout
        val params = ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT)
        params.setMargins(dpToPx(context, 20F), dpToPx(context, 25F), 0, 0)
        return params
    }

    private fun createFormulaDefTextView(context: Context, n: String = "n", k: String = "k", operationType: Int): TextView {
        val textView = TextView(context)
        val text: String
        val spanIndexes: Pair<Int, Int>  // Começo e fim do span, nessa ordem
        when (operationType) {
            0 -> {
                text = "P$n = "
                spanIndexes = Pair(1 , n.length + 1)
            }
            1 -> {
                text = "A$n,$k = "
                spanIndexes = Pair(1, 2 + n.length + k.length)
            }
            2 -> {
                text = "P$n,$k = "
                spanIndexes = Pair(1, 2 + n.length + k.length)
            }
            3 -> {
                text = "Ar$n,$k = "
                spanIndexes = Pair(2, 3 + n.length + k.length)
            }
            else -> {
                text = "C$n,$k = "
                spanIndexes = Pair(1, 2 + n.length + k.length)
            }
        }
        val formulaDefString = SpannableString(text)
        formulaDefString.setSpan(SubscriptSpan(), spanIndexes.first, spanIndexes.second, SpannedString.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.setText(formulaDefString, TextView.BufferType.SPANNABLE)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
        return textView
    }

    private fun writePermutation(context: Context,n: String = "n", isDeveloping: Boolean = false): LinearLayout {
        val formulaParentLayout = ParentLinearLayout(context)
        val formulaDefTextView = createFormulaDefTextView(context, n, operationType = 0)
        val formulaText = if (!isDeveloping || !n.isDigitsOnly()) "$n!" else factorialMultiplicationsString(n.toInt())
        val formulaValuesTextView = DivisionPartTextView(context, formulaText.toString())
        formulaParentLayout.addView(formulaDefTextView)
        formulaParentLayout.addView(formulaValuesTextView)
        return formulaParentLayout
    }

    private fun writeArranjoRepeticao(context: Context, n: String = "n", k: String = "k"): LinearLayout {
        val formulaParentLayout = ParentLinearLayout(context)
        val formulaPartString = SpannableString("$n$k")
        formulaPartString.setSpan(SuperscriptSpan(), n.length, n.length + k.length, SpannedString.SPAN_EXCLUSIVE_EXCLUSIVE)
        val formulaPartTextView = DivisionPartTextView(context,formulaPartString)
        formulaParentLayout.addView(createFormulaDefTextView(context, n, k, 3))
        formulaParentLayout.addView(formulaPartTextView)
        return formulaParentLayout
    }

    private fun writeOperationWithFraction(context: Context, operationType: Int, n: String = "n", k: String = "k"): LinearLayout {
        val formulaParentLayout = ParentLinearLayout(context)
        val formulaNameView = createFormulaDefTextView(context, n, k, operationType)
        formulaParentLayout.addView(formulaNameView)
        val formulaDefLayout = LinearLayout(context)  // LinearLayout que contém o numerador e denominador da fórmula
        formulaDefLayout.orientation = LinearLayout.VERTICAL
        val numeratorTextView = DivisionPartTextView(context, "$n!")
        val denominatorString = when (operationType) {
            1 -> "($n - $k)!"
            2 -> "$k!"
            else -> "$k!($n - $k)!"
        }
        val denominatorTextView = DivisionPartTextView(context, denominatorString, R.drawable.fraction_symbol)
        formulaDefLayout.addView(numeratorTextView)
        formulaDefLayout.addView(denominatorTextView)

        formulaParentLayout.addView(formulaDefLayout)
        return formulaParentLayout
    }

    private fun writeFraction(context: Context, n: String, k: String, operationType: Int, isDeveloping: Boolean = false): LinearLayout {
        val formulaParentLayout = ParentLinearLayout(context)
        val formulaNameView = createFormulaDefTextView(context, n, k, operationType)
        formulaParentLayout.addView(formulaNameView)
        val formulaDefLayout = LinearLayout(context)  // LinearLayout que contém o numerador e denominador da fórmula
        formulaDefLayout.orientation = LinearLayout.VERTICAL
        val numeratorTextView: TextView
        val denominatortext: String
        val denominatorTextView: TextView
        if (!isDeveloping) {
            numeratorTextView = DivisionPartTextView(context, "$n!")
            denominatortext = if (operationType == 1) "${n.toInt() - k.toInt()}!" else "$k! x ${n.toInt() - k.toInt()}!"  // Texto de arranjo ou combinação
            denominatorTextView = DivisionPartTextView(context, denominatortext, R.drawable.fraction_symbol)
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
            numeratorTextView = DivisionPartTextView(context, factorialMultiplicationsString(n.toInt(), rangeEnd, true))
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
            formulaParentLayout.addView(formulaDefLayout)
            return formulaParentLayout
        }
    }

    private fun presentResult(n: String, k: String, result: String, operationType: Int) : LinearLayout {
        val parentLinearLayout = ParentLinearLayout(this@Explanation.requireContext())
        val formulaDef = createFormulaDefTextView(this@Explanation.requireContext() ,n, k, operationType)
        val resultTextView = DivisionPartTextView(this@Explanation.requireContext() ,result)
        parentLinearLayout.addView(formulaDef)
        parentLinearLayout.addView(resultTextView)
        return parentLinearLayout
    }
}