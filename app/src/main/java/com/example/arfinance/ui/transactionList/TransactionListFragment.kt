package com.example.arfinance.ui.transactionList

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.arfinance.R
import com.example.arfinance.data.dataModel.Balance
import com.example.arfinance.data.dataModel.Transactions
import com.example.arfinance.databinding.TransactionListFragmentBinding
import com.example.arfinance.util.autoCleared
import com.example.arfinance.util.enumerian.BalanceTime
import com.example.arfinance.util.exhaustive
import com.example.arfinance.util.interfaces.OpenFullScreenListener
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


private const val DATE_KEY = "com.example.arfinance.ui.transactionList_date_key"

@AndroidEntryPoint
class TransactionListFragment : Fragment(R.layout.transaction_list_fragment) {

    private val viewModel: TransactionListViewModel by viewModels()
    private var binding: TransactionListFragmentBinding by autoCleared()
    private val calendar = Calendar.getInstance()
    private val balance = Balance(0, BalanceTime.WEEK, 0, 0)

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

        val bundle = savedInstanceState?.getString(DATE_KEY)

        bindUI(bundle)
    }

    private fun bindUI(bundle: String?) {

        if (bundle != null) {
            binding.txtDate.text = bundle
            viewModel.dateQuery.value = bundle
        } else {
            viewModel.dateQuery.value = getToday()
        }

        binding.apply {
            addTransactionFab.setOnClickListener {
                if (it == null) return@setOnClickListener
                viewModel.addNewTransactionClicked()
            }
            txtDate.setOnClickListener { showDatePickerDialog() }
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
            if (it.isNullOrEmpty()) {
                binding.transactionListRecyclerView.visibility = View.GONE
            } else {
                binding.transactionListRecyclerView.visibility = View.VISIBLE
                initTransactionsRecyclerView(
                    it.toTransactionItems(),
                    binding.transactionListRecyclerView
                )
            }
        }

        viewModel.balanceIncomeWeek.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            binding.header.income.text = moneyFormatter(it.toLong())
            balance.income = it

        }
        viewModel.balanceExpenseWeek.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            binding.header.expense.text = moneyFormatter(it.toLong())
            balance.expense = it

            binding.header.totalBalance.text = moneyFormatter(balance.getBalance())
        }

        viewModel.transactionListDateRange.observe(viewLifecycleOwner) { transactionsListByRange ->
            if (transactionsListByRange == null) return@observe
            setupPieChart()
            loadCharData(transactionsListByRange, binding.header.pieChart)

        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.transactionEvent.collect { event ->
                when (event) {
                    is TransactionListViewModel.TransactionListEvent.NavigateToAddTransactionScreen -> {
                        val actionAdd = TransactionListFragmentDirections.addEditTransaction()
                        findNavController().navigate(actionAdd)
                    }
                    is TransactionListViewModel.TransactionListEvent.NavigateToEditTransactionScreen -> {
                        val actionEdit =
                            TransactionListFragmentDirections.addEditTransaction(event.transactions)
                        findNavController().navigate(actionEdit)
                    }
                }.exhaustive
            }
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

        if (selectedDate == getToday())
            binding.txtDate.setText(R.string.today)
        else
            binding.txtDate.text = selectedDate

        viewModel.dateQuery.value = selectedDate

    }

    private fun formatDate(date: Date): String {
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

    private fun moneyFormatter(number: Long): String {
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.maximumFractionDigits = 0
        format.currency = Currency.getInstance("IRR")

        return format.format(number)
    }

    //todo save day with view model
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(DATE_KEY, formatDate(calendar.time))
    }

    private fun setupPieChart() {
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

    private fun loadCharData(transactions: List<Transactions>, pieChart: PieChart){
        val entries = ArrayList<PieEntry>()
        transactions.forEach {
            entries.add(PieEntry(it.amount.toFloat(),it.title ))
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

}