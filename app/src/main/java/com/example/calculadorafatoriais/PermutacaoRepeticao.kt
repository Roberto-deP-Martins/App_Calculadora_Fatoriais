package com.example.calculadorafatoriais

import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.calculadorafatoriais.databinding.FragmentPermutacaoRepeticaoBinding

/**
 * A simple [Fragment] subclass.
 * Use the [PermutacaoRepeticao.newInstance] factory method to
 * create an instance of this fragment.
 */
class PermutacaoRepeticao : Fragment() {
    val sharedViewModel: SharedViewModel by activityViewModels()
    lateinit var binding: FragmentPermutacaoRepeticaoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<FragmentPermutacaoRepeticaoBinding>(inflater, R.layout.fragment_permutacao_repeticao, container, false)
        writeFormula("", "", 0, 0)

        binding.numeratorPermutationRepetition.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                sharedViewModel.n = s.toString().toIntOrNull()
                val k = binding.denominatorPermutationRepetition.text.toString()
                writeFormula(s.toString(), k, s.toString().length, k.length)
            }
        })

        binding.denominatorPermutationRepetition.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                sharedViewModel.k = s.toString().toIntOrNull()
                val n = binding.numeratorPermutationRepetition.text.toString()
                writeFormula(n, s.toString(), n.length, s.toString().length)
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sharedViewModel.n = null
        sharedViewModel.k = null
    }

    /**
     * Função que altera a textView da fórmula de acordo com valor das variáveis n e k alteradas
     * pelo usuário
     */
    private fun writeFormula(_n: String, _k: String, _nLength: Int, _kLength: Int) {
        /* Caso editText esteja vazio, tornará length 0 da string em 1, considerando que a string
        vazia será substituída pelo caractere n ou  k */
        val nLength = if (_nLength != 0) _nLength else 1
        val kLength = if (_kLength != 0) _kLength else 1

        val n = if (_n != "") _n else "n"
        val k = if (_k != "") _k else "k"
        val firstMathVarStart = 1
        val firstMathVarEnd = firstMathVarStart + nLength
        val secondMathVarEnd = firstMathVarEnd + kLength
        val formulaDefString : SpannableString =  SpannableString("P${n}${k} = ")
        formulaDefString.setSpan(SubscriptSpan(), firstMathVarStart, firstMathVarEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)  // Permite uso de subscript
        formulaDefString.setSpan(SuperscriptSpan(), firstMathVarEnd, secondMathVarEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.permutacaoRepeticaoTextView.setText(formulaDefString, TextView.BufferType.SPANNABLE)  // Define o spannableString como texto e segundo parâmetro adapta tamanho da view ao mesmo
    }

}