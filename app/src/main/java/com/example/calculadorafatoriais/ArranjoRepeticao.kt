package com.example.calculadorafatoriais

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.SubscriptSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.calculadorafatoriais.databinding.FragmentArranjoRepeticaoBinding
import com.example.calculadorafatoriais.databinding.FragmentPermutacaoRepeticaoBinding

/**
 * A simple [Fragment] subclass.
 * Use the [ArranjoRepeticao.newInstance] factory method to
 * create an instance of this fragment.
 */
class ArranjoRepeticao : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentArranjoRepeticaoBinding>(inflater, R.layout.fragment_arranjo_repeticao, container, false)
        val formulaDefString : SpannableString =  SpannableString("AR = ")
        formulaDefString.setSpan(SubscriptSpan(), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.arranjoRepDefTextView.setText(formulaDefString, TextView.BufferType.SPANNABLE)

        binding.baseEditTextNumber.setOnClickListener { removeEditTextBackground(binding.baseEditTextNumber) }
        binding.exponentEditTextNumber.setOnClickListener { removeEditTextBackground(binding.exponentEditTextNumber) }
        return binding.root
    }

    private fun removeEditTextBackground(view: EditText) { view.background = null }
}