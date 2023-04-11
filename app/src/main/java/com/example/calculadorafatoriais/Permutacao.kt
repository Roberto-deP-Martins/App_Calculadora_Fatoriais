package com.example.calculadorafatoriais

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.SubscriptSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.example.calculadorafatoriais.databinding.FragmentPermutacaoBinding

/**
 * A simple [Fragment] subclass.
 * Use the [Permutacao.newInstance] factory method to
 * create an instance of this fragment.
 */
class Permutacao : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentPermutacaoBinding>(inflater, R.layout.fragment_permutacao, container, false)

        val formulaDefString : SpannableString =  SpannableString("Pn = ")
        formulaDefString.setSpan(SubscriptSpan(), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)  // Permite uso de subscript
        binding.formulaNameTextView.setText(formulaDefString, TextView.BufferType.SPANNABLE)  // Define o spannableString como texto e segundo parâmetro adapta tamanho da view ao mesmo

        return binding.root  // Retorna a view raíz
    }
}