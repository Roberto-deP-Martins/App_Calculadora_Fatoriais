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
import androidx.appcompat.content.res.AppCompatResources
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
            0 -> operationsArray[0]
            1 -> operationsArray[1]
            2 -> operationsArray[2]
            3 -> operationsArray[3]
            4 -> operationsArray[4]
            else -> operationsArray[0]
        }
        binding.explanationIntro.text = getString(R.string.explanationIntro, "$operationName:")
        binding.formulaPresentation.addView(when (args.operation) {
            0 -> writePermutation(this.requireContext())
            else -> writePermutation(this.requireContext())
        })
        return binding.root
    }

}

private fun dpToPx(context: Context): Int {
    val r: Resources = context.resources
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20F, r.displayMetrics).toInt()
}

private fun writePermutation(context: Context,n: String = "n"): LinearLayout {
    val linearLayout = LinearLayout(context)
    linearLayout.orientation = LinearLayout.HORIZONTAL
    val params = ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT)
    val px = dpToPx(context)
    params.setMargins(px, px, 0, 0)
    linearLayout.layoutParams = params

    val formulaText = TextView(context)

    // Configurando texto para formulaNameText
    val formulaDefString = SpannableString("Pn = ${n}!")
    formulaDefString.setSpan(SubscriptSpan(), 1, 2, SpannedString.SPAN_EXCLUSIVE_EXCLUSIVE)
    formulaText.setText(formulaDefString, TextView.BufferType.SPANNABLE)
    formulaText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)

    linearLayout.addView(formulaText)
    return linearLayout
}