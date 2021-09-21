package com.example.arfinance.ui.selectCategory

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.arfinance.R
import com.example.arfinance.data.dataModel.Category
import com.example.arfinance.databinding.SelectCategoryFragmentBinding
import com.example.arfinance.ui.addEditTransaction.CATEGORY_BUNDLE
import com.example.arfinance.ui.addEditTransaction.CATEGORY_REQUEST_KEY
import com.example.arfinance.util.autoCleared
import com.example.arfinance.util.onQueryTextChanged
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SelectCategoryFragment : Fragment(R.layout.select_category_fragment) {

    private var binding: SelectCategoryFragmentBinding by autoCleared()
    private val viewModel: SelectCategoryViewModel by viewModels()
    private lateinit var searchView: SearchView

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
        setHasOptionsMenu(true)
    }

    private fun bindUI() {

        binding.addCategoryFab.setOnClickListener{
            viewModel.addNewCategoryClicked()
        }

       viewModel.categories.observe(viewLifecycleOwner){
            if (it == null) return@observe
            //println("size -> ${it.size}")
            initCategoryRecyclerView(it.toCategoryItems(),binding.categoryRecyclerView)
       }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.selectCategoryEvent.collect { event ->
                when(event){
                     is SelectCategoryViewModel.SelectCategoryListEvent.NavigateToAddCategoryScreen ->{
                         val action = SelectCategoryFragmentDirections.addCategory()
                         Navigation.findNavController(requireView()).navigate(action)
                     }
                }
            }
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.category_menu, menu)

        val searchItem = menu.findItem(R.id.search)
        searchView = searchItem.actionView as SearchView

        val pendingQuery = viewModel.searchQuery.value
        if (pendingQuery.isNotEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(pendingQuery, false)
        }

        searchView.onQueryTextChanged {
            viewModel.searchQuery.value = it
        }
    }
}