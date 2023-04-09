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
import com.example.calculadorafatoriais.databinding.FragmentArranjoBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class Arranjo : Fragment() {
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
        writeFormula("n", "k")
        setUpTextChangeListener(binding.numeradorEditView, binding.nDivisorEditView)
        setUpTextChangeListener(binding.nDivisorEditView, binding.numeradorEditView)

        binding.kDivisorEditView.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
            override fun afterTextChanged(s: Editable?) {
                var n = "n"
                /* variável n tem valor "n" se nenhum editText com valor de n tiver sido preenchido,
                caso contrário, terá valor do campo */
                if (binding.numeradorEditView.text.toString() != "" || binding.nDivisorEditView.text.toString() != "") {
                    n = if (binding.numeradorEditView.text.toString() != "") binding.numeradorEditView.text.toString()
                        else binding.nDivisorEditView.text.toString()
                }
                val k = if (binding.kDivisorEditView.text.toString() != "") binding.kDivisorEditView.text.toString() else "k"
                writeFormula(n, k)
            }
        })

        return binding.root
    }

    private fun writeFormula(n: String, k: String) {
        val formulaDefString : SpannableString =  SpannableString("A${n},${k} = ")
        formulaDefString.setSpan(SubscriptSpan(), 1, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.arranjoTextView.setText(formulaDefString, TextView.BufferType.SPANNABLE)
    }

    // listenedView é view que receberá listener, affectedView será a view alterada pela função do listener
    private fun setUpTextChangeListener(listenedView: EditText, affectedView: EditText) {
        listenedView.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

            override fun afterTextChanged(s: Editable?) {
                if (affectedView.text.toString() != listenedView.text.toString()) {  // Necessário para evitar recursão
                    affectedView.text = listenedView.text
                    val k = if (binding.kDivisorEditView.text.toString() == "") "k" else binding.kDivisorEditView.text.toString()
                    val n = if (listenedView.text.toString() == "") "n" else listenedView.text.toString()
                    writeFormula(n, k)
                }
            }
        })
    }
}
