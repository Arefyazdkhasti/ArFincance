package com.example.arfinance.ui.selectCategory

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.arfinance.R
import com.example.arfinance.data.dataModel.Category
import com.example.arfinance.data.dataModel.Transactions
import com.example.arfinance.databinding.SelectCategoryFragmentBinding
import com.example.arfinance.databinding.TransactionListFragmentBinding
import com.example.arfinance.ui.addEditTransaction.CATEGORY_BUNDLE
import com.example.arfinance.ui.addEditTransaction.CATEGORY_REQUEST_KEY
import com.example.arfinance.ui.transactionList.TransactionItemRecyclerView
import com.example.arfinance.util.autoCleared
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectCategoryFragment : Fragment(R.layout.select_category_fragment) {

    private var binding: SelectCategoryFragmentBinding by autoCleared()
    private val viewModel: SelectCategoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SelectCategoryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
       viewModel.categories.observe(viewLifecycleOwner){
            if (it == null) return@observe
            println("size -> ${it.size}")
            initCategoryRecyclerView(it.toCategoryItems(),binding.categoryRecyclerView)
        }
    }


    private fun initCategoryRecyclerView(items: List<CategoryItemRecyclerView>, recyclerView: RecyclerView) {
        val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(items)
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            adapter = groupAdapter
        }

        groupAdapter.setOnItemClickListener { item, view ->
            (item as? CategoryItemRecyclerView)?.let {
                setFragmentResult(CATEGORY_REQUEST_KEY, bundleOf(CATEGORY_BUNDLE to  it.category))
                findNavController().navigateUp()
            }
        }
    }

    private fun List<Category>.toCategoryItems(): List<CategoryItemRecyclerView> = this.map {
        CategoryItemRecyclerView(it)
    }

}