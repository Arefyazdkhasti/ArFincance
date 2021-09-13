package com.example.arfinance.ui.addEditTransaction

import android.os.Bundle
import android.view.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.arfinance.R
import com.example.arfinance.data.dataModel.Category
import com.example.arfinance.databinding.AddEditTransactionFragmentBinding
import com.example.arfinance.util.autoCleared
import com.example.arfinance.util.enumerian.CategoryType
import com.example.arfinance.util.enumerian.TransactionType
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
        setHasOptionsMenu(true)

        binding.apply {
            title.addTextChangedListener {
                viewModel.transactionTitle = it.toString()
            }

            amount.addTextChangedListener {
                viewModel.transactionAmount = it.toString().toLong()
            }

            date.addTextChangedListener {
                viewModel.transactionDate = it.toString()
            }

            category.addTextChangedListener {
                viewModel.transactionCategory = it.toString().toInt()
            }

            note.addTextChangedListener {
                viewModel.transactionNote = it.toString()
            }

            if (operationTypeGroup.checkedChipId == 0)
                viewModel.transactionType = TransactionType.Income
            else if (operationTypeGroup.checkedChipId == 1)
                viewModel.transactionType = TransactionType.Expense

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_option_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_menu_option -> {
                viewModel.onSaveClick()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}