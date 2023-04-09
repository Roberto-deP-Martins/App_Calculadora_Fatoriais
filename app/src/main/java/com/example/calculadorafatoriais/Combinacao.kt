package com.example.calculadorafatoriais

import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.example.calculadorafatoriais.databinding.FragmentCombinacaoBinding

/**
 * A simple [Fragment] subclass.
 * Use the [Combinacao.newInstance] factory method to
 * create an instance of this fragment.
 */
class Combinacao : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentCombinacaoBinding>(inflater, R.layout.fragment_combinacao, container, false)
        // binding.numeratorNEditText.addTextChangedListener()

        setUpTextChangeListener(binding.numeratorNEditText, binding.nSubtractionEditText)
        setUpTextChangeListener(binding.nSubtractionEditText, binding.numeratorNEditText)
        setUpTextChangeListener(binding.kDivisorEditText, binding.kSubtractionEditText)
        setUpTextChangeListener(binding.kSubtractionEditText, binding.kDivisorEditText)

        return binding.root
    }

    // listenedView é view que receberá listener, affectedView será a view alterada pela função do listener
    private fun setUpTextChangeListener(listenedView: EditText, affectedView: EditText) {
        listenedView.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

            override fun afterTextChanged(s: Editable?) {
                if (affectedView.text.toString() != listenedView.text.toString()) {
                    affectedView.text = listenedView.text
                }
            }
        })
    }
}