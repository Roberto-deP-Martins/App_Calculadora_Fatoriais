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
import androidx.fragment.app.activityViewModels
import com.example.calculadorafatoriais.databinding.FragmentCombinacaoBinding

/**
 * A simple [Fragment] subclass.
 * Use the [Combinacao.newInstance] factory method to
 * create an instance of this fragment.
 */
class Combinacao : Fragment() {
    lateinit var binding: FragmentCombinacaoBinding
    val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<FragmentCombinacaoBinding>(inflater, R.layout.fragment_combinacao, container, false)
        // binding.numeratorNEditText.addTextChangedListener()

        setUpTextChangeListener(binding.numeratorNEditText, binding.nSubtractionEditText, 'n')
        setUpTextChangeListener(binding.nSubtractionEditText, binding.numeratorNEditText, 'n')
        setUpTextChangeListener(binding.kDenominatorEditText, binding.kSubtractionEditText, 'k')
        setUpTextChangeListener(binding.kSubtractionEditText, binding.kDenominatorEditText, 'k')

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sharedViewModel.n = null
        sharedViewModel.k = null
    }

    /**
     * Função responsável por manter múltiplas ocorrências de uma variável, que pode ser n ou k,
     * iguais dentre as múltiplas editText que as representam
    */
    // listenedView é view que receberá listener, affectedView será a view alterada pela função do listener
    private fun setUpTextChangeListener(listenedView: EditText, affectedView: EditText, mathVariable: Char) {
        listenedView.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

            override fun afterTextChanged(s: Editable?) {
                val parentFragment: Calculadora = this@Combinacao.parentFragment as Calculadora
                parentFragment.eraseExplanationButton()
                if (affectedView.text.toString() != listenedView.text.toString()) {
                    affectedView.text = listenedView.text
                    if (mathVariable == 'n') {
                        binding.nValueTextView.text = if (affectedView.text.toString() == "") "n" else affectedView.text.toString()
                        sharedViewModel.n = affectedView.text.toString().toIntOrNull()
                    }
                    else binding.kValueTextView.text = if (affectedView.text.toString() == "") "k" else affectedView.text.toString()
                    sharedViewModel.k = affectedView.text.toString().toIntOrNull()
                }
            }
        })
    }
}