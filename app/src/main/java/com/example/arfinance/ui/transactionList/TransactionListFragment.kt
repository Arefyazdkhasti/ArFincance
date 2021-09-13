package com.example.arfinance.ui.transactionList

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.arfinance.R
import com.example.arfinance.databinding.TransactionListFragmentBinding
import com.example.arfinance.ui.base.ScopedFragment
import com.example.arfinance.util.interfaces.OpenFullScreenListener
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class TransactionListFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: TransactionListViewModelFactory by instance()

    private lateinit var viewModel: TransactionListViewModel

    private var _binding: TransactionListFragmentBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TransactionListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(TransactionListViewModel::class.java)
        // TODO: Use the ViewModel
        (requireActivity() as OpenFullScreenListener).onScreenClose()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() = launch {
        val transactions = viewModel.transactions.await()
       /* transactions.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            println(it[0].title)
        })*/
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}