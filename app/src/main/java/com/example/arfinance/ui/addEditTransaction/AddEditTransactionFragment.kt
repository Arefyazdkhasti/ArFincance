package com.example.arfinance.ui.addEditTransaction

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.arfinance.R
import com.example.arfinance.databinding.AddEditTransactionFragmentBinding
import com.example.arfinance.util.autoCleared
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditTransactionFragment : Fragment(R.layout.add_edit_transaction_fragment) {


    private val viewModel: AddEditTransactionViewModel by viewModels()
    private var binding: AddEditTransactionFragmentBinding by autoCleared()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddEditTransactionFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

        }
    }

}