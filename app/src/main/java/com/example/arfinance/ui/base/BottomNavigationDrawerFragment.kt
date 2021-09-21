package com.example.arfinance.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.arfinance.R
import com.example.arfinance.databinding.FragmentBottomsheetBottomAppbarBinding
import com.example.arfinance.util.interfaces.OpenAllTransactionsClickListener
import com.example.arfinance.util.interfaces.OpenAnalyticsClickListener
import com.example.arfinance.util.interfaces.OpenCategoriesClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomNavigationDrawerFragment(
    private val openCategoriesClickListener: OpenCategoriesClickListener,
    private val openAnalyticsClickListener: OpenAnalyticsClickListener,
    private val openAllTransactionsClickListener: OpenAllTransactionsClickListener
) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomsheetBottomAppbarBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomsheetBottomAppbarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        binding.apply {
            navigationView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.analytics_action -> {
                        openAnalyticsClickListener.openAnalytics()
                        true

                    }
                    R.id.categories_action -> {
                        openCategoriesClickListener.openCategories()
                        true
                    }
                    R.id.all_transaction_action -> {
                        openAllTransactionsClickListener.openAllTransactions()
                        true
                    }
                    else -> false
                }
            }
        }
    }

}