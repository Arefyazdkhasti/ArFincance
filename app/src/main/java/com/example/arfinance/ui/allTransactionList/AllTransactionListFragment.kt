package com.example.arfinance.ui.allTransactionList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arfinance.R
import com.example.arfinance.data.dataModel.TransactionRecyclerHelper
import com.example.arfinance.data.dataModel.Transactions
import com.example.arfinance.data.dataModel.TransactionsHelper
import com.example.arfinance.databinding.FragmentAllTransactionListBinding
import com.example.arfinance.databinding.HomeFragmentBinding
import com.example.arfinance.ui.allTransactionList.TransactionListAdapter.Companion.VIEW_TYPE_DATE
import com.example.arfinance.ui.allTransactionList.TransactionListAdapter.Companion.VIEW_TYPE_TRANSACTION
import com.example.arfinance.ui.home.TransactionItemRecyclerView
import com.example.arfinance.util.autoCleared
import com.example.arfinance.util.enumerian.PaymentType
import com.example.arfinance.util.enumerian.TransactionType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllTransactionListFragment : Fragment(R.layout.fragment_all_transaction_list) {

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
            allTransactionListRecyclerView.showShimmerAdapter()

            viewModel.transactionsDates.observe(viewLifecycleOwner) { dateList ->
                if (dateList == null) return@observe

                viewModel.transactions.observe(viewLifecycleOwner) { transactionList ->
                    if (transactionList == null) return@observe

                    setUpData(dateList,transactionList)
                }
            }



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

        val recyclerAdapter = TransactionListAdapter(requireContext(), data)
        binding.allTransactionListRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recyclerAdapter
        }

        binding.allTransactionListRecyclerView.hideShimmerAdapter()
    }

}