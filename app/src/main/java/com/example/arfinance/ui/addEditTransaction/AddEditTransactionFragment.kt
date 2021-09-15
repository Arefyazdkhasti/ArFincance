package com.example.arfinance.ui.addEditTransaction

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.arfinance.R
import com.example.arfinance.data.dataModel.Category
import com.example.arfinance.databinding.AddEditTransactionFragmentBinding
import com.example.arfinance.util.UiUtil
import com.example.arfinance.util.UiUtil.Companion.showSnackBar
import com.example.arfinance.util.UiUtil.Companion.showToast
import com.example.arfinance.util.autoCleared
import com.example.arfinance.util.enumerian.PaymentType
import com.example.arfinance.util.enumerian.TransactionType
import com.example.arfinance.util.exhaustive
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*

const val CATEGORY_REQUEST_KEY = "com.example.arfinance.ui.addEditTransaction_category_request_key"
const val CATEGORY_BUNDLE = "com.example.arfinance.ui.addEditTransaction_category_bundle"

@AndroidEntryPoint
class AddEditTransactionFragment : Fragment(R.layout.add_edit_transaction_fragment) {


    private val viewModel: AddEditTransactionViewModel by viewModels()
    private var binding: AddEditTransactionFragmentBinding by autoCleared()
    private val calendar = Calendar.getInstance()

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

        bindUI()
    }

    private fun bindUI() {
        binding.apply {
            title.addTextChangedListener {
                if (!it.isNullOrEmpty())
                    viewModel.transactionTitle = it.toString()
            }

            amount.addTextChangedListener {
                if (!it.isNullOrEmpty())
                    viewModel.transactionAmount = it.toString().toLong()
            }

            date.apply {
                isFocusable = false
                isClickable = true
                setOnClickListener {
                    showDatePickerDialog()
                }
            }


            category.apply {
                isFocusable = false
                isClickable = true
                setOnClickListener {
                    viewModel.onSelectCategoryClick()
                }
            }

            note.addTextChangedListener {
                if (!it.isNullOrEmpty())
                    viewModel.transactionNote = it.toString()
            }

            if (operationTypeGroup.checkedChipId == 0)
                viewModel.transactionType = TransactionType.Income
            else if (operationTypeGroup.checkedChipId == 1)
                viewModel.transactionType = TransactionType.Expense


            if (paymentTypeGroup.checkedChipId == 0)
                viewModel.transactionPaymentType = PaymentType.Cash
            else if (operationTypeGroup.checkedChipId == 1)
                viewModel.transactionPaymentType = PaymentType.CreditCard

        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditTransactionEvent.collect { event ->
                when (event) {
                    is AddEditTransactionViewModel.AddEditTransactionEvent.ShowInvalidInputMessage -> {
                        showSnackBar(requireView(), event.msg)
                    }
                    is AddEditTransactionViewModel.AddEditTransactionEvent.ShowSuccessMessage -> {
                        showToast(requireContext(), event.msg)
                        findNavController().popBackStack()
                    }
                    is AddEditTransactionViewModel.AddEditTransactionEvent.NavigateToSelectCategory -> {
                        setFragmentResultListener(CATEGORY_REQUEST_KEY) { key, bundle ->
                            // read from the bundle
                            val category = bundle.getParcelable<Category>(CATEGORY_BUNDLE)
                            if (category != null) {
                                binding.category.setText(category.categoryName)
                                viewModel.transactionCategory = category.id
                            }
                        }
                        val action = AddEditTransactionFragmentDirections.selectCategory()
                        Navigation.findNavController(requireView()).navigate(action)
                    }
                }
            }
        }
    }

    private fun showDatePickerDialog() {
        DatePickerDialog(
            requireContext(),
            getCalenderDataSetListener(),
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun getCalenderDataSetListener() =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            setSelectedDate()
        }

    private fun setSelectedDate() {
        val myFormat = "MM/dd/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.date.setText(sdf.format(calendar.time))
        viewModel.transactionDate = binding.date.text.toString()
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