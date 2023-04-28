package com.example.calculadorafatoriais

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.calculadorafatoriais.combinatorics.*
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
    val sharedViewModel: SharedViewModel by activityViewModels()
    var result: ULong? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<FragmentCalculadoraBinding>(inflater, R.layout.fragment_calculadora, container, false)
        binding.calcularButton.setOnClickListener { calculate() }
        binding.explanationButton.setOnClickListener { view?.findNavController()?.navigate(CalculadoraDirections.actionCalculadoraToExplanationFragment(binding.spinner.selectedItemPosition, result.toString())) }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val manager = childFragmentManager
        lateinit var operationFragment : Fragment
        binding.spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val transaction = manager.beginTransaction()
                operationFragment = when (position) {
                    0 -> Permutacao()
                    1 -> Arranjo()
                    2 -> PermutacaoRepeticao()
                    3 -> ArranjoRepeticao()
                    4 -> Combinacao()
                    else -> Permutacao()
                 }
                transaction.replace(R.id.operacaoContainerView, operationFragment)
                transaction.commit()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun calculate() {
        if (sharedViewModel.n == null) {
            Toast.makeText(context, this.getString(R.string.nVariableNotWritten), Toast.LENGTH_LONG).show()
        }
        else {
            // Permutação
            if (binding.spinner.selectedItemPosition == 0) { result = permutacao(sharedViewModel.n!!) }
            else if (sharedViewModel.k == null) {
                Toast.makeText(context, this.getString(R.string.kVariableNotWritten), Toast.LENGTH_LONG).show()
            }
            else { // Atribui à operation referência à função de operação selecionada
                val operation = when (binding.spinner.selectedItemPosition) {
                    1 -> ::arranjo
                    2 -> ::permutacaoRepeticao
                    3 -> ::arranjoRepeticao
                    4 -> ::combinacao
                    else -> null
                }
                if (operation != null) {
                    if ((operation != ::arranjoRepeticao) && (sharedViewModel.k!! > sharedViewModel.n!!)) {
                        Toast.makeText(context, this.getString(R.string.kBiggerThanN), Toast.LENGTH_LONG).show()  // k > n e operação não é arranjo com repetição
                    }
                    else {
                        result = operation(sharedViewModel.n!!, sharedViewModel.k!!)
                    }
                }
            }
        }
        if (result != null) {  // Mostra resultado
            binding.resultadoTextView.text = getString(R.string.showResult, result)
            binding.resultadoTextView.visibility = View.VISIBLE
            binding.explanationButton.visibility = View.VISIBLE
        }
        else { Toast.makeText(context, this.getString(R.string.integerOverflow), Toast.LENGTH_LONG).show() }  // Overflow
    }
}