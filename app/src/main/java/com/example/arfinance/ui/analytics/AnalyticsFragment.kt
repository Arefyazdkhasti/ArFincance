package com.example.arfinance.ui.analytics

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.arfinance.R
import com.example.arfinance.data.dataModel.Transactions
import com.example.arfinance.databinding.AnalyticsFragmentBinding
import com.example.arfinance.databinding.TransactionListFragmentBinding
import com.example.arfinance.ui.transactionList.TransactionListViewModel
import com.example.arfinance.util.autoCleared
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AnalyticsFragment : Fragment(R.layout.analytics_fragment) {


    private val viewModel: AnalyticsViewModel by viewModels()
    private var binding: AnalyticsFragmentBinding by autoCleared()
    private val calendar = Calendar.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AnalyticsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        binding.apply {
            viewModel.apply {
                incomeStartDate.value = getToday()
                expenseStartDate.value = getToday()
                println("${getToday()} ${get7DaysAgo()} ${get1MonthAgo()} ${get1YearAgo()} ")
                toggleButtonGroupIncome.addOnButtonCheckedListener { _, checkedId, isChecked ->
                    if (isChecked) {
                        when (checkedId) {
                            R.id.btn_income_day -> incomeEndDate.value = getYesterday()
                            R.id.btn_income_week -> incomeEndDate.value = get7DaysAgo()
                            R.id.btn_income_month -> incomeEndDate.value = get1MonthAgo()
                            R.id.btn_income_year -> incomeEndDate.value = get1YearAgo()
                        }
                    }
                }
                toggleButtonGroupExpense.addOnButtonCheckedListener { _, checkedId, isChecked ->
                    if (isChecked) {
                        when (checkedId) {
                            R.id.btn_expense_day -> {
                                expenseEndDate.value = getYesterday()
                                println("day")
                            }
                            R.id.btn_expense_week -> {
                                expenseEndDate.value = get7DaysAgo()
                                println("week")

                            }
                            R.id.btn_expense_month -> {
                                expenseEndDate.value = get1MonthAgo()
                                println("month")
                            }
                            R.id.btn_expense_year -> {
                                expenseEndDate.value = get1YearAgo()
                                println("year")
                            }
                        }
                    }
                }

                incomeListDateRange.observe(viewLifecycleOwner){
                    if (it == null) return@observe
                    setupPieChart(pieChartIncome)
                    loadCharData(it,pieChartIncome)
                }
                expenseListDateRange.observe(viewLifecycleOwner){
                    if (it == null) return@observe
                    setupPieChart(pieChartExpense)
                    loadCharData(it,pieChartExpense)
                }
            }

        }
    }

    private fun getToday(): String {
        val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        return sdf.format(System.currentTimeMillis())
    }

    private fun getYesterday(): String {
        val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        return sdf.format(System.currentTimeMillis() - 86400000) //yesterday
    }

    private fun get7DaysAgo(): String {
        val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        return sdf.format(System.currentTimeMillis() - 604800000L) //7 days ago
    }

    private fun get1MonthAgo(): String {
        val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        return sdf.format(System.currentTimeMillis() - 2592000000L) //30 days ago
    }

    private fun get1YearAgo(): String {
        val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        return sdf.format(System.currentTimeMillis() - 31536000000L) //one year ago
    }


    private fun setupPieChart(pieChart: PieChart) {
        pieChart.apply {
            isDrawHoleEnabled = true
            setUsePercentValues(true)
            setEntryLabelTextSize(12f)
            setEntryLabelColor(Color.BLACK)
            centerText = "Expenses by Category"
            setCenterTextSize(24f)
            description.isEnabled = false
        }

        val legend: Legend = pieChart.legend
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
}