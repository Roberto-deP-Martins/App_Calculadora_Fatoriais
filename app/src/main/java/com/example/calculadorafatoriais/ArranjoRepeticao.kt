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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.calculadorafatoriais.databinding.FragmentArranjoRepeticaoBinding
import com.example.calculadorafatoriais.databinding.FragmentPermutacaoRepeticaoBinding

/**
 * A simple [Fragment] subclass.
 * Use the [ArranjoRepeticao.newInstance] factory method to
 * create an instance of this fragment.
 */
class ArranjoRepeticao : Fragment() {
    val sharedViewModel: SharedViewModel by activityViewModels()

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

        binding.baseEditTextNumber.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                sharedViewModel.n = if (s.toString() != "") s.toString().toInt() else null
            }
        })

        binding.exponentEditTextNumber.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                sharedViewModel.k = if (s.toString() != "") s.toString().toInt() else null
                Log.d(TAG, "k ${sharedViewModel.k}")
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sharedViewModel.n = null
        sharedViewModel.k = null
    }
}