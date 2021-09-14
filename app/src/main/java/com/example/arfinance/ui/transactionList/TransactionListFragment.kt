package com.example.arfinance.ui.transactionList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
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
import kotlinx.coroutines.flow.collect

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

        binding.addTransactionFab.setOnClickListener {
            if (it == null) return@setOnClickListener
            viewModel.addNewTransactionClicked()
        }
        bindUI()
    }

    private fun bindUI() {

        viewModel.transaction.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty())
                Toast.makeText(requireContext(), "gooz", Toast.LENGTH_SHORT).show()
            else {
                println(it[0].toString())
                initTransactionsRecyclerView(it.toTransactionItems(),binding.transactionListRecyclerView)
            }

        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.transactionEvent.collect { event ->
                when(event){
                    is TransactionListViewModel.TransactionListEvent.NavigateToAddTransactionScreen -> {
                        val action = TransactionListFragmentDirections.addEditTransaction()
                        findNavController().navigate(action)
                    }
                    is TransactionListViewModel.TransactionListEvent.NavigateToEditTransactionScreen -> {
                        //todo edit transaction
                    }
                }.exhaustive
            }
        }
    }

    private fun initTransactionsRecyclerView(items: List<TransactionItemRecyclerView>, recyclerView: RecyclerView) {
        val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(items)
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            adapter = groupAdapter
        }

        groupAdapter.setOnItemClickListener { item, view ->
            //todo
        }
    }

    private fun List<Transactions>.toTransactionItems(): List<TransactionItemRecyclerView> = this.map {
        TransactionItemRecyclerView(it)
    }

}