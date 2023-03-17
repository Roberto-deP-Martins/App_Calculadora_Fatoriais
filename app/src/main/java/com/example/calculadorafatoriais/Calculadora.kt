package com.example.calculadorafatoriais

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import com.example.calculadorafatoriais.databinding.FragmentCalculadoraBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Calculadora.newInstance] factory method to
 * create an instance of this fragment.
 */
class Calculadora : Fragment() {

    lateinit var binding: FragmentCalculadoraBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<FragmentCalculadoraBinding>(inflater, R.layout.fragment_calculadora, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val manager = childFragmentManager

        binding.spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                print("a")
                val transaction = manager.beginTransaction()
                val operation : Fragment = when (position) {
                    0 -> Permutacao()
                    1 -> Arranjo()
                    2 -> PermutacaoRepeticao()
                    else -> Permutacao()
                 }
                transaction.replace(R.id.operacaoContainerView, operation)

                transaction.commit()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}