package com.example.calculadorafatoriais

import android.os.Bundle
import android.text.SpannableString
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        return binding.root
    }
}