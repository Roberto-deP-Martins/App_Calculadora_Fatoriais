package com.example.calculadorafatoriais

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.SubscriptSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import com.example.calculadorafatoriais.databinding.FragmentArranjoBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class Arranjo : Fragment() {
    private val sharedViewModel : SharedViewModel by activityViewModels()
    lateinit var binding : FragmentArranjoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<FragmentArranjoBinding>(inflater, R.layout.fragment_arranjo, container, false)
        writeFormula("n", "k", 1, 1)
        setUpTextChangeListener(binding.numeradorEditView, binding.nDenominatorEditView)
        setUpTextChangeListener(binding.nDenominatorEditView, binding.numeradorEditView)

        binding.kDenominatorEditView.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
            override fun afterTextChanged(s: Editable?) {
                var n = "n"
                /* variável n tem valor "n" se nenhum editText com valor de n tiver sido preenchido,
                caso contrário, terá valor do campo */
                if (binding.numeradorEditView.text.toString() != "" || binding.nDenominatorEditView.text.toString() != "") {
                    n = if (binding.numeradorEditView.text.toString() != "") binding.numeradorEditView.text.toString()
                        else binding.nDenominatorEditView.text.toString()
                }
                val k = binding.kDenominatorEditView.text.toString()
                sharedViewModel.k = k.toULongOrNull()
                Log.d(TAG, "viewmodel: n ${sharedViewModel.n} e k ${sharedViewModel.k}")
                writeFormula(n, k, n.length, k.length)
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
        val n = if (_n != "") _n else "n"
        val k = if (_k != "") _k else "k"
        val nLength = if (_nLength != 0) _nLength else 1
        val kLength = if (_kLength != 0) _kLength else 1
        val formulaDefString : SpannableString =  SpannableString("A${n},${k} = ")
        formulaDefString.setSpan(SubscriptSpan(), 1, 2 + nLength + kLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.arranjoTextView.setText(formulaDefString, TextView.BufferType.SPANNABLE)
    }

    /**
     * Função responsável por manter múltiplas ocorrências de uma variável, que pode ser n ou k,
     * iguais dentre as múltiplas editText que as representam
     */
    // listenedView é view que receberá listener, affectedView será a view alterada pela função do listener
    private fun setUpTextChangeListener(listenedView: EditText, affectedView: EditText) {
        listenedView.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

            override fun afterTextChanged(s: Editable?) {
                if (affectedView.text.toString() != listenedView.text.toString()) {  // Necessário para evitar recursão
                    affectedView.text = listenedView.text
                    val k = binding.kDenominatorEditView.text.toString()
                    val n = binding.nDenominatorEditView.text.toString()
                    sharedViewModel.n = n.toULongOrNull()
                    writeFormula(n, k, n.length, k.length)
                    Log.d(TAG, "viewmodel: n ${sharedViewModel.n} e k ${sharedViewModel.k}")
                }
            }
        })
    }
}
