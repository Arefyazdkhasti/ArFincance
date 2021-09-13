package com.example.arfinance.ui.transactionList

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.arfinance.R
import com.example.arfinance.databinding.TransactionListFragmentBinding
import com.example.arfinance.util.autoCleared
import com.example.arfinance.util.interfaces.OpenFullScreenListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionListFragment : Fragment(R.layout.transaction_list_fragment) {

    private val viewModel: TransactionListViewModel by viewModels()
    private var binding: TransactionListFragmentBinding by autoCleared()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TransactionListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as OpenFullScreenListener).onScreenClose()

        binding.addTransactionFab.setOnClickListener{
            if (it == null) return@setOnClickListener
            val action = TransactionListFragmentDirections.addEditTransaction()
            Navigation.findNavController(requireActivity(), it.id).navigate(action)
        }
        bindUI()
    }

    private fun bindUI() {
        binding.header.income.text = "12.200.000 toman"
        viewModel.transaction.observe(viewLifecycleOwner) {

        }
    }


}