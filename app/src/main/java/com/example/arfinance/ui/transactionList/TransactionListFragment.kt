package com.example.arfinance.ui.transactionList

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.arfinance.R
import com.example.arfinance.ui.base.ScopedFragment
import com.example.arfinance.util.interfaces.OpenFullScreenListener

class TransactionListFragment : ScopedFragment() {

    companion object {
        fun newInstance() = TransactionListFragment()
    }

    private lateinit var viewModel: TransactionListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.transaction_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TransactionListViewModel::class.java)
        // TODO: Use the ViewModel
        (requireActivity() as OpenFullScreenListener).onScreenClose()

    }

}