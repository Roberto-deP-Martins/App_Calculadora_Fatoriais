package com.example.calculadorafatoriais

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.calculadorafatoriais.databinding.FragmentPermutacaoRepeticaoBinding

/**
 * A simple [Fragment] subclass.
 * Use the [PermutacaoRepeticao.newInstance] factory method to
 * create an instance of this fragment.
 */
class PermutacaoRepeticao : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentPermutacaoRepeticaoBinding>(inflater, R.layout.fragment_permutacao_repeticao, container, false)
        val formulaDefString : SpannableString =  SpannableString("Pnk = ")
        formulaDefString.setSpan(SubscriptSpan(), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)  // Permite uso de subscript
        formulaDefString.setSpan(SuperscriptSpan(), 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.permutacaoRepeticaoTextView.setText(formulaDefString, TextView.BufferType.SPANNABLE)  // Define o spannableString como texto e segundo parâmetro adapta tamanho da view ao mesmo

        binding.numeratorPermutationRepetition.setOnClickListener {
            binding.numeratorPermutationRepetition.background = null
        }
        return binding.root
    }
}