package com.example.arfinance.ui.addCategory

import android.os.Bundle
import android.view.*
import android.widget.GridView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.arfinance.R
import com.example.arfinance.databinding.AddCategoryFragmentBinding
import com.example.arfinance.ui.base.CategoryBottomSheetDialog
import com.example.arfinance.util.UiUtil.Companion.showSnackBar
import com.example.arfinance.util.UiUtil.Companion.showToast
import com.example.arfinance.util.autoCleared
import com.example.arfinance.util.interfaces.CategoryIconSelectedListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AddCategoryFragment : Fragment(R.layout.add_category_fragment),CategoryIconSelectedListener {


    private val viewModel: AddCategoryViewModel by viewModels()
    private var binding: AddCategoryFragmentBinding by autoCleared()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<GridView>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddCategoryFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindUI()

        setHasOptionsMenu(true)
    }

    private fun bindUI() {
        binding.apply {

            categoryTitle.addTextChangedListener {
                if (!it.isNullOrEmpty())
                    viewModel.categoryTitle = it.toString()
            }

            selectCategoryIconRoot.setOnClickListener {
                showChooseIconDialog()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addcategoryEvent.collect { event ->
                when(event){
                    is AddCategoryViewModel.AddCategoryEvent.ShowInvalidInputMessage ->{
                        showSnackBar(requireView(), event.msg)
                    }
                    is AddCategoryViewModel.AddCategoryEvent.ShowSuccessMessage ->{
                        showToast(requireContext(),event.msg)
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    private fun showChooseIconDialog() {
        val  bottomSheet= CategoryBottomSheetDialog()
        bottomSheet.show(requireActivity().supportFragmentManager,bottomSheet.tag)

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

    override fun selectIcon(title: String, drawable: Int) {
        viewModel.categoryIcon = drawable
        binding.categoryIcon.setImageResource(drawable)
    }

}