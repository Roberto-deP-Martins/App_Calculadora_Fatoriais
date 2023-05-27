package com.example.calculadorafatoriais

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.SubscriptSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.calculadorafatoriais.databinding.FragmentPermutacaoBinding

/**
 * A simple [Fragment] subclass.
 * Use the [Permutacao.newInstance] factory method to
 * create an instance of this fragment.
 */
class Permutacao : Fragment() {
    val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentPermutacaoBinding>(inflater, R.layout.fragment_permutacao, container, false)

        val formulaDefString : SpannableString =  SpannableString("Pn = ")
        formulaDefString.setSpan(SubscriptSpan(), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)  // Permite uso de subscript
        binding.formulaNameTextView.setText(formulaDefString, TextView.BufferType.SPANNABLE)  // Define o spannableString como texto e segundo parâmetro adapta tamanho da view ao mesmo
        binding.valorEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.length in 2..4) { binding.valorEditText.setEms(s.length) }
                sharedViewModel.n = s.toString().toIntOrNull()
                val parentFragment: Calculadora = this@Permutacao.parentFragment as Calculadora
                parentFragment.eraseExplanationButton()
            }

        })

        return binding.root  // Retorna a view raíz
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sharedViewModel.n = null
        Log.d(TAG, "n = ${sharedViewModel.n}")
    }
}