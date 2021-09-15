package com.example.arfinance.ui.transactionList

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.arfinance.R
import com.example.arfinance.data.dataModel.Transactions
import com.example.arfinance.databinding.TransactionListFragmentBinding
import com.example.arfinance.util.autoCleared
import com.example.arfinance.util.exhaustive
import com.example.arfinance.util.interfaces.OpenFullScreenListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*

private const val DATE_KEY = "com.example.arfinance.ui.transactionList_date_key"
@AndroidEntryPoint
class TransactionListFragment : Fragment(R.layout.transaction_list_fragment) {

    private val viewModel: TransactionListViewModel by viewModels()
    private var binding: TransactionListFragmentBinding by autoCleared()
    private val calendar = Calendar.getInstance()

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

        val bundle = savedInstanceState?.getString(DATE_KEY)

        bindUI(bundle)
    }

    private fun bindUI(bundle: String?) {

        if (bundle != null){
            binding.txtDate.text = bundle
            viewModel.dateQuery.value = bundle
        }else {
            viewModel.dateQuery.value = getToday()
        }

        binding.apply {
            addTransactionFab.setOnClickListener {
                if (it == null) return@setOnClickListener
                viewModel.addNewTransactionClicked()
            }
            txtDate.setOnClickListener { showDatePickerDialog() }
        }

        viewModel.categoryDbSize.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            if (it == 0) {//fill category db if it's empty
                viewModel.addCategoryList()
            }
        }

        viewModel.transaction.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.transactionListRecyclerView.visibility = View.GONE
            } else {
                binding.transactionListRecyclerView.visibility = View.VISIBLE
                initTransactionsRecyclerView(
                    it.toTransactionItems(),
                    binding.transactionListRecyclerView
                )
            }

        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.transactionEvent.collect { event ->
                when (event) {
                    is TransactionListViewModel.TransactionListEvent.NavigateToAddTransactionScreen -> {
                        val actionAdd = TransactionListFragmentDirections.addEditTransaction()
                        findNavController().navigate(actionAdd)
                    }
                    is TransactionListViewModel.TransactionListEvent.NavigateToEditTransactionScreen -> {
                        val actionEdit =
                            TransactionListFragmentDirections.addEditTransaction(event.transactions)
                        findNavController().navigate(actionEdit)
                    }
                }.exhaustive
            }
        }
    }

    private fun initTransactionsRecyclerView(
        items: List<TransactionItemRecyclerView>,
        recyclerView: RecyclerView
    ) {
        val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(items)
        }

        recyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = groupAdapter
            val divider = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            addItemDecoration(divider)
        }

        groupAdapter.setOnItemClickListener { item, view ->
            (item as? TransactionItemRecyclerView)?.let {
                viewModel.onTransactionSelected(it.transactions)
            }
        }
    }

    private fun List<Transactions>.toTransactionItems(): List<TransactionItemRecyclerView> =
        this.map {
            TransactionItemRecyclerView(it)
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
        val selectedDate = formatDate(calendar.time)

        if (selectedDate == getToday())
            binding.txtDate.setText(R.string.today)
        else
            binding.txtDate.text = selectedDate

        viewModel.dateQuery.value = selectedDate

    }

    private fun formatDate(date: Date): String {
        val myFormat = "MM/dd/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        return sdf.format(date)
    }

    private fun getToday(): String {
        val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        return sdf.format(System.currentTimeMillis())
    }

    //todo save day with view model
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(DATE_KEY, formatDate(calendar.time))
    }
}