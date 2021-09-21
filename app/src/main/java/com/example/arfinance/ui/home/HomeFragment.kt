package com.example.arfinance.ui.home

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.arfinance.R
import com.example.arfinance.data.dataModel.Balance
import com.example.arfinance.data.dataModel.Transactions
import com.example.arfinance.databinding.HomeFragmentBinding
import com.example.arfinance.ui.base.BottomNavigationDrawerFragment
import com.example.arfinance.util.autoCleared
import com.example.arfinance.util.enumerian.BalanceTime
import com.example.arfinance.util.exhaustive
import com.example.arfinance.util.interfaces.OpenAllTransactionsClickListener
import com.example.arfinance.util.interfaces.OpenAnalyticsClickListener
import com.example.arfinance.util.interfaces.OpenCategoriesClickListener
import com.example.arfinance.util.startMyAnimation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*



@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.home_fragment),
    OpenCategoriesClickListener, OpenAnalyticsClickListener, OpenAllTransactionsClickListener {

    private val viewModel: HomeViewModel by viewModels()
    private var binding: HomeFragmentBinding by autoCleared()
    private val calendar = Calendar.getInstance()
    private val balance = Balance(0, BalanceTime.WEEK, 0, 0)
    private lateinit var bottomNavDrawerFragment: BottomNavigationDrawerFragment


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        bindUI()
    }

    private fun bindUI() {


        viewModel.dateQuery.value = getToday()

        val animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.fab_explosion_anim).apply {
                duration = 700
                interpolator = AccelerateDecelerateInterpolator()
            }

        binding.apply {
            addTransactionFab.setOnClickListener {
                if (it == null) return@setOnClickListener

                addTransactionFab.visibility = View.INVISIBLE
                explosionCircle.visibility = View.VISIBLE

                explosionCircle.startMyAnimation(animation) {
                    explosionCircle.visibility = View.INVISIBLE
                    viewModel.addNewTransactionClicked()
                }
            }
            txtDate.setOnClickListener { showDatePickerDialog() }

            txtTransactions.setOnClickListener { viewModel.showAllTransactionsClicked() }

            bottomAppBar.apply {

                setNavigationOnClickListener {
                    bottomNavDrawerFragment = BottomNavigationDrawerFragment(
                        this@HomeFragment,
                        this@HomeFragment,
                        this@HomeFragment
                    )
                    bottomNavDrawerFragment.show(
                        requireActivity().supportFragmentManager,
                        bottomNavDrawerFragment.tag
                    )
                }

                setOnMenuItemClickListener {
                    if (it.itemId == R.id.setting_action) {
                        val action = HomeFragmentDirections.navigateToSetting()
                        Navigation.findNavController(requireView()).navigate(action)
                        true
                    } else {
                        false
                    }
                }

            }
        }

        viewModel.categoryDbSize.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            if (it == 0) {//fill category db if it's empty
                viewModel.addCategoryList()
            }
        }

        viewModel.balanceWeekStartDate.value = get7DaysAgo()
        viewModel.balanceWeekEndDate.value = getToday()

        viewModel.transaction.observe(viewLifecycleOwner) {
            binding.txtDate.text = viewModel.dateQuery.value

            if (it.isNullOrEmpty()) {
                binding.transactionListRecyclerView.visibility = View.GONE
                binding.noResultImageView.visibility = View.VISIBLE
            } else {
                binding.transactionListRecyclerView.visibility = View.VISIBLE
                binding.noResultImageView.visibility = View.GONE
                initTransactionsRecyclerView(
                    it.toTransactionItems(),
                    binding.transactionListRecyclerView
                )
            }
        }

        viewModel.balanceIncomeWeek.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            balance.income = it
            println(it)
            binding.header.formatIncome( it.toLong())
        }
        viewModel.balanceExpenseWeek.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            println(it)

            binding.header.formatExpense(it.toLong())

            balance.expense = it
            println(balance.getBalance())
            binding.header.formatBalance( balance.getBalance())

        }

        viewModel.transactionListDateRange.observe(viewLifecycleOwner) { transactionsListByRange ->
            if (transactionsListByRange == null) return@observe
            if (transactionsListByRange.isEmpty()) {
                binding.header.apply {
                    binding.header.hideChart()
                }
            } else {
                binding.header.apply {
                   binding.header.showChart()
                }
                binding.header.setupPieChart()
                binding.header.loadCharData(transactionsListByRange)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.transactionEvent.collect { event ->
                when (event) {
                    is HomeViewModel.TransactionListEvent.NavigateToAddTransactionScreen -> {
                        val actionAdd = HomeFragmentDirections.addEditTransaction()
                        findNavController().navigate(actionAdd)
                    }
                    is HomeViewModel.TransactionListEvent.NavigateToEditTransactionScreen -> {
                        val actionEdit =
                            HomeFragmentDirections.addEditTransaction(event.transactions)
                        findNavController().navigate(actionEdit)
                    }
                    is HomeViewModel.TransactionListEvent.DeleteTransaction -> {
                        showConfirmDeleteDialog(event.transaction)
                    }
                    is HomeViewModel.TransactionListEvent.NavigateToAllTransactionsListScreen -> {
                        val actionSeeAll =
                            HomeFragmentDirections.navigateToAllTransactionsList()
                        findNavController().navigate(actionSeeAll)
                    }
                    is HomeViewModel.TransactionListEvent.NavigateToAnalyticsListScreen -> {
                        val action = HomeFragmentDirections.navigateToAnalytics()
                        Navigation.findNavController(requireView()).navigate(action)
                    }
                     is HomeViewModel.TransactionListEvent.NavigateToCategoryScreen -> {
                        val action = HomeFragmentDirections.navigateToCategoriesList()
                        Navigation.findNavController(requireView()).navigate(action)
                    }
                }.exhaustive
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
            anchorView = binding.addTransactionFab
            show()
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
        groupAdapter.setOnItemLongClickListener { item, view ->
            (item as? TransactionItemRecyclerView)?.let {
                viewModel.onTransactionLongClick(it.transactions)
            }
            return@setOnItemLongClickListener true
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

        /* if (selectedDate == getToday())
             binding.txtDate.setText(R.string.today)
         else
             binding.txtDate.text = selectedDate*/

        binding.txtDate.text = selectedDate

        viewModel.dateQuery.value = selectedDate

    }

    fun formatDate(date: Date): String {
        val myFormat = "MM/dd/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        return sdf.format(date)
    }

    private fun getToday(): String {
        val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        return sdf.format(System.currentTimeMillis())
    }

    private fun get7DaysAgo(): String {
        val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        return sdf.format(System.currentTimeMillis() - 604800000L) //7 days ago
    }



    /*//todo save day with view model
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(DATE_KEY, formatDate(calendar.time))
    }*/

   /* private fun setupPieChart() {
        binding.header.pieChart.apply {
            isDrawHoleEnabled = true
            setUsePercentValues(true)
            setEntryLabelTextSize(12f)
            setEntryLabelColor(Color.BLACK)
            centerText = "Expenses by Category"
            setCenterTextSize(24f)
            description.isEnabled = false
        }

        val legend: Legend = binding.header.pieChart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.setDrawInside(false)
        legend.isEnabled = true
    }

    private fun loadCharData(transactions: List<Transactions>, pieChart: PieChart) {
        val entries = ArrayList<PieEntry>()
        transactions.forEach {
            entries.add(PieEntry(it.amount.toFloat(), it.title))
        }


        val colors = ArrayList<Int>()
        for (color in ColorTemplate.MATERIAL_COLORS) {
            colors.add(color)
        }

        for (color in ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color)
        }

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setDrawValues(true)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(12f)
        data.setValueTextColor(Color.BLACK)

        pieChart.data = data
        pieChart.invalidate()

        pieChart.animateY(1400, Easing.EaseInOutQuad)
    }
*/
    override fun openAnalytics() {
        viewModel.analyticsClicked()
        bottomNavDrawerFragment.dismiss()
    }

    override fun openCategories() {
        viewModel.categoryClicked()
        bottomNavDrawerFragment.dismiss()
    }

    override fun openAllTransactions() {
        viewModel.showAllTransactionsClicked()
        bottomNavDrawerFragment.dismiss()
    }


}