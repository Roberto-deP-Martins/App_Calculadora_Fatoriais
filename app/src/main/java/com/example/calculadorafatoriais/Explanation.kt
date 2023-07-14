package com.example.calculadorafatoriais

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.calculadorafatoriais.combinatorics.permutacao
import com.example.calculadorafatoriais.databinding.FragmentExplanationBinding
import com.example.calculadorafatoriais.explanationWidgets.*


class Explanation : Fragment(), Factorial, SetLayoutParams {
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
                binding.formulaPresentation.addView(PermutationLayout(this.requireContext()))
                binding.formulaWithValues.addView(PermutationLayout(this.requireContext(), args.n))
                binding.developmentLayout.addView(PermutationLayout(this.requireContext(), args.n, true))
            }
            3 -> {  // Arranjo com Repetição
                binding.formulaPresentation.addView(ArranjoRepeticao(this.requireContext()))
                binding.formulaWithValues.addView(ArranjoRepeticao(this.requireContext(), args.n, args.k))
                val developmentText = TextView(this.requireContext())
                developmentText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
                developmentText.text = "Ar = " + powerMultiplicationsString(args.n.toInt(), args.k.toInt())
                developmentText.layoutParams = configureLayoutParams(this.requireContext())
                binding.developmentLayout.addView(developmentText)
            }
            else -> {
                binding.formulaPresentation.addView(FractionLayout(this.requireContext(), args.operation))
                binding.formulaWithValues.addView(FractionLayout(this.requireContext(), args.operation, args.n, args.k))
                if (args.operation != 2) { binding.developmentLayout.addView(FractionLayout(this.requireContext(), args.operation, args.n, args.k, false)) }
                binding.developmentLayout.addView(FractionLayout(this.requireContext(), args.operation, args.n, args.k, true))
                if (args.operation == 4) {
                    val finalDevelopmentLayout = ParentLinearLayout(this.requireContext())
                    finalDevelopmentLayout.addView(FormulaDefinitionTextView(this.requireContext(), args.n, args.k, 4))
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

    private fun powerMultiplicationsString(base: Int, power: Int): String {
        if (power == 0) return "1" else if (power > 5) return  "$base x $base x ... x $base x $base"
        var seriesText = ""
        for (i in power downTo 1) {  // Printa sequência de valores multiplicadas no fatorial
            seriesText += if (i != 1) "$base x " else "$base"
        }
        return seriesText
    }

    private fun presentResult(n: String, k: String, result: String, operationType: Int) : LinearLayout {
        val parentLinearLayout = ParentLinearLayout(this@Explanation.requireContext())
        val formulaDef = FormulaDefinitionTextView(this@Explanation.requireContext() ,n, k, operationType)
        val resultTextView = DivisionPartTextView(this@Explanation.requireContext() ,result)
        parentLinearLayout.addView(formulaDef)
        parentLinearLayout.addView(resultTextView)
        return parentLinearLayout
    }
}