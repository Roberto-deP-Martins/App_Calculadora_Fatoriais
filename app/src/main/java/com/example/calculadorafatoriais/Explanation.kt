package com.example.calculadorafatoriais

import android.app.ActionBar
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannedString
import android.text.style.SubscriptSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
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
            1 -> writeArranjo(this.requireContext())
            else -> writePermutation(this.requireContext())
        })
        return binding.root
    }
}

private fun dpToPx(context: Context): Int {
    val r: Resources = context.resources
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20F, r.displayMetrics).toInt()
}

private fun configureLayoutParams(context: Context): ActionBar.LayoutParams {  // Configura margem do LinearLayout
    val params = ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT)
    val px = dpToPx(context)
    params.setMargins(px, px, 0, 0)
    return params
}

private fun createFormulaDefTextView(context: Context, text: String, spanStart: Int, spanEnd: Int): TextView {
    val textView = TextView(context)
    val formulaDefString = SpannableString(text)
    formulaDefString.setSpan(SubscriptSpan(), spanStart, spanEnd, SpannedString.SPAN_EXCLUSIVE_EXCLUSIVE)
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

private fun writePermutation(context: Context,n: String = "n"): LinearLayout {
    val linearLayout = LinearLayout(context)
    linearLayout.orientation = LinearLayout.HORIZONTAL
    linearLayout.layoutParams = configureLayoutParams(context)
    linearLayout.addView(createFormulaDefTextView(context, "Pn = $n!", 1, 2))
    return linearLayout
}

private fun writeArranjo(context: Context, n: String = "n", k: String = "k") : LinearLayout {
    val linearLayout = LinearLayout(context)
    linearLayout.orientation = LinearLayout.HORIZONTAL
    linearLayout.layoutParams = configureLayoutParams(context)

    val formulaName = createFormulaDefTextView(context, "An,k = ", 1, 4)
    linearLayout.addView(formulaName)
    val formulaDefLayout = LinearLayout(context)  // LinearLayout que contém o numerador e denominador da fórmula
    formulaDefLayout.orientation = LinearLayout.VERTICAL
    val numeratorTextView = createFormulaPartTextView(context, "n!")
    val denominatorTextView = createFormulaPartTextView(context, "(n - k)!", R.drawable.fraction_symbol)
    formulaDefLayout.addView(numeratorTextView)
    formulaDefLayout.addView(denominatorTextView)

    linearLayout.addView(formulaDefLayout)
    return linearLayout

}