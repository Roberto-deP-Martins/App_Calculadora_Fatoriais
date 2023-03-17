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
import com.example.calculadorafatoriais.databinding.FragmentArranjoBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class Arranjo : Fragment() {
    // TODO: Rename and change types of parameters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentArranjoBinding>(inflater, R.layout.fragment_arranjo, container, false)
        val formulaDefString : SpannableString =  SpannableString("Ank = ")
        formulaDefString.setSpan(SubscriptSpan(), 1, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.arranjoTextView.setText(formulaDefString, TextView.BufferType.SPANNABLE)
        return binding.root
    }
}
