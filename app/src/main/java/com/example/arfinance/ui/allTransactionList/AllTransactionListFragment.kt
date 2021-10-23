package com.example.arfinance.ui.allTransactionList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arfinance.R
import com.example.arfinance.data.dataModel.TransactionRecyclerHelper
import com.example.arfinance.data.dataModel.Transactions
import com.example.arfinance.data.dataModel.TransactionsHelper
import com.example.arfinance.databinding.FragmentAllTransactionListBinding
import com.example.arfinance.databinding.HomeFragmentBinding
import com.example.arfinance.ui.allTransactionList.TransactionListAdapter.Companion.VIEW_TYPE_DATE
import com.example.arfinance.ui.allTransactionList.TransactionListAdapter.Companion.VIEW_TYPE_TRANSACTION
import com.example.arfinance.ui.home.HomeFragmentDirections
import com.example.arfinance.ui.home.HomeViewModel
import com.example.arfinance.ui.home.TransactionItemRecyclerView
import com.example.arfinance.util.autoCleared
import com.example.arfinance.util.enumerian.PaymentType
import com.example.arfinance.util.enumerian.TransactionType
import com.example.arfinance.util.exhaustive
import com.example.arfinance.util.interfaces.OnAllTransactionsItemEventListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AllTransactionListFragment : Fragment(R.layout.fragment_all_transaction_list), OnAllTransactionsItemEventListener {

    private val viewModel: AllTransactionListViewModel by viewModels()
    private var binding: FragmentAllTransactionListBinding by autoCleared()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllTransactionListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        binding.apply {
            //allTransactionListRecyclerView.showShimmerAdapter()

            viewModel.transactionsDates.observe(viewLifecycleOwner) { dateList ->
                if (dateList == null) {
                    //allTransactionListRecyclerView.hideShimmerAdapter()
                    return@observe
                }

                viewModel.transactions.observe(viewLifecycleOwner) { transactionList ->
                    if (transactionList == null) {
                        //allTransactionListRecyclerView.hideShimmerAdapter()
                        return@observe
                    }

                    setUpData(dateList, transactionList)
                }
            }


            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.transactionEvent.collect { event ->
                    when (event) {
                        is AllTransactionListViewModel.TransactionListEvent.NavigateToEditTransactionScreen -> {
                            val actionEdit = AllTransactionListFragmentDirections.editTransaction(event.transactions)
                            findNavController().navigate(actionEdit)
                        }
                        is AllTransactionListViewModel.TransactionListEvent.DeleteTransaction -> {
                            showConfirmDeleteDialog(event.transaction)
                        }
                    }.exhaustive
                }
            }
        }
    }

    private fun showConfirmDeleteDialog(transition: Transactions) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete")
            .setMessage("Do you want to delete this transaction?")
            .setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("Yes") { dialog, which ->
                viewModel.deleteTransaction(transition)
                showUndoDeleteSnackBar(transition)
                dialog.dismiss()
            }.show()
    }

    private fun showUndoDeleteSnackBar(transition: Transactions) {
        val snackBar = Snackbar.make(requireView(), "Transaction Deleted", Snackbar.LENGTH_LONG)
        snackBar.apply {
            setAction("UNDO") {
                viewModel.undoDeleteTransaction(transition)
            }
                .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            show()
        }
    }
    private fun setUpData(dateList: List<String>, transactionList: List<Transactions>) {
        val transactionData = arrayListOf<TransactionRecyclerHelper>()

        dateList.forEach { date ->
            transactionData.add(
                TransactionRecyclerHelper(
                    VIEW_TYPE_DATE,
                    Transactions(
                        title = "",
                        amount = 0,
                        type = TransactionType.Unknown,
                        categoryName = "",
                        categoryIcon = "",
                        paymentType = PaymentType.Unknown,
                        note = "",
                        date = date
                    )
                )
            )
            transactionList.forEach { _transaction ->
                if (_transaction.date == date) {
                    transactionData.add(
                        TransactionRecyclerHelper(
                            VIEW_TYPE_TRANSACTION,
                            _transaction
                        )
                    )
                }
            }
            initTransactionRecyclerView(transactionData)
        }
    }

    private fun initTransactionRecyclerView(data: ArrayList<TransactionRecyclerHelper>) {

        val recyclerAdapter = TransactionListAdapter(requireContext(), data,this)
        binding.allTransactionListRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recyclerAdapter
        }
        //binding.allTransactionListRecyclerView.hideShimmerAdapter()
    }

    override fun onDeleteClickListener(transactions: Transactions) {
        viewModel.onTransactionLongClick(transactions)
    }

    override fun onEditClickListener(transactions: Transactions) {
        viewModel.onTransactionSelected(transactions)
    }

}