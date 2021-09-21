package com.example.arfinance.ui.analytics

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.arfinance.R
import com.example.arfinance.data.dataModel.Transactions
import com.example.arfinance.data.dataModel.TransactionsHelper
import com.example.arfinance.databinding.AnalyticsFragmentBinding
import com.example.arfinance.util.autoCleared
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
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
    private var expenseList = arrayListOf<Transactions>()
    var days = arrayOf("Sunday", "Monday", "Tuesday", "Thursday", "Friday", "Saturday")

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
                incomeStartDate.value = get7DaysAgo()
                incomeEndDate.value = getToday()
                expenseStartDate.value = get7DaysAgo()
                expenseEndDate.value = getToday()
                barChartStartDate.value = get7DaysAgo()
                barChartEndDate.value = getToday()

                println("${getToday()} ${get7DaysAgo()} ${get1MonthAgo()} ${get1YearAgo()} ")
                toggleButtonGroupIncome.addOnButtonCheckedListener { _, checkedId, isChecked ->
                    if (isChecked) {
                        when (checkedId) {
                            R.id.btn_income_day -> incomeStartDate.value = getYesterday()
                            R.id.btn_income_week -> incomeStartDate.value = get7DaysAgo()
                            R.id.btn_income_month -> incomeStartDate.value = get1MonthAgo()
                            R.id.btn_income_year -> incomeStartDate.value = get1YearAgo()
                        }
                    }
                }
                toggleButtonGroupExpense.addOnButtonCheckedListener { _, checkedId, isChecked ->
                    if (isChecked) {
                        when (checkedId) {
                            R.id.btn_expense_day -> {
                                expenseStartDate.value = getToday()
                                println("day")
                            }
                            R.id.btn_expense_week -> {
                                expenseStartDate.value = get7DaysAgo()
                                println("week")

                            }
                            R.id.btn_expense_month -> {
                                expenseStartDate.value = get1MonthAgo()
                                println("month")
                            }
                            R.id.btn_expense_year -> {
                                expenseStartDate.value = get1YearAgo()
                                println("year")
                            }
                        }
                    }
                }
                toggleButtonGroupExpenseByCategory.addOnButtonCheckedListener { _, checkedId, isChecked ->
                    if (isChecked) {
                        when (checkedId) {
                            R.id.btn_expense_by_category_day -> {
                                barChartStartDate.value = getToday()
                                println("day")
                            }
                            R.id.btn_expense_by_category_week -> {
                                barChartStartDate.value = get7DaysAgo()
                                println("week")

                            }
                            R.id.btn_expense_by_category_month -> {
                                barChartStartDate.value = get1MonthAgo()
                                println("month")
                            }
                            R.id.btn_expense_by_category_year -> {
                                barChartStartDate.value = get1YearAgo()
                                println("year")
                            }
                        }
                    }
                }
                batChartListDateRange.observe(viewLifecycleOwner) {
                    if (it == null) return@observe
                    if (it.isEmpty()) {
                        barChart.visibility = View.GONE
                        noDataForBarChartExpenseAnalytics.visibility = View.VISIBLE
                    } else {
                        barChart.visibility = View.VISIBLE
                        noDataForBarChartExpenseAnalytics.visibility = View.GONE
                        showBarChart(barChart, it)
                    }
                }

                incomeListDateRange.observe(viewLifecycleOwner) {
                    if (it == null) return@observe
                    if (it.isEmpty()) {
                        pieChartIncome.visibility = View.GONE
                        noDataForPieChartIncomeAnalytics.visibility = View.VISIBLE
                    } else {
                        pieChartIncome.visibility = View.VISIBLE
                        noDataForPieChartIncomeAnalytics.visibility = View.GONE
                        //  println(it.toString())
                        setupPieChart(pieChartIncome, "Income By Category")
                        loadCharData(it, pieChartIncome)
                    }
                }
                expenseListDateRange.observe(viewLifecycleOwner) {
                    if (it == null) return@observe
                    if (it.isEmpty()) {
                        pieChartExpense.visibility = View.GONE
                        noDataForPieChartExpenseAnalytics.visibility = View.VISIBLE
                    } else {
                        pieChartExpense.visibility = View.VISIBLE
                        noDataForPieChartExpenseAnalytics.visibility = View.GONE
                        // println(it.toString())
                        setupPieChart(pieChartExpense, "Expense By Category")
                        loadCharData(it, pieChartExpense)
                    }
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
        return sdf.format(System.currentTimeMillis() - 86_400_000) //yesterday
    }

    private fun get7DaysAgo(): String {
        val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        return sdf.format(System.currentTimeMillis() - 604800000L) //7 days ago
    }

    private fun get1MonthAgo(): String {
        val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        return sdf.format(System.currentTimeMillis() - 2_592_000_000L) //30 days ago
    }

    private fun get1YearAgo(): String {
        val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        return sdf.format(System.currentTimeMillis() - 20_736_000_000L) //8 months ago //todo one year ago
    }


    private fun setupPieChart(pieChart: PieChart, centerTxt: String) {
        pieChart.apply {
            isDrawHoleEnabled = true
            setUsePercentValues(true)
            setEntryLabelTextSize(12f)
            setEntryLabelColor(Color.BLACK)
            centerText = centerTxt
            setCenterTextSize(20f)
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

    /* // array list for first set
     private fun getBarEntries(list: List<Transactions>,categoryName:String): ArrayList<BarEntry> {
         val barEntries = ArrayList<BarEntry>()

         list.forEach {
             if(it.categoryName == categoryName) barEntries.add(BarEntry(1f, it.amount.toFloat()))
         }
         return barEntries
     }


     private fun showBarChart(list: List<Transactions>, barChart: BarChart){
         // creating a new bar data set.
         // creating a new bar data set.
         val barDataSets = arrayListOf<BarDataSet>()
         barDataSets.add( BarDataSet(getBarEntries(list,"clothes"), "Clothes"))
         barDataSets.add( BarDataSet(getBarEntries(list,"book"), "Book"))
        // barDataSet1.color = requireContext().resources.getColor(R.color.purple_200)
        // val barDataSet2 = BarDataSet(getBarEntriesTwo(), "Second Set")
        // barDataSet2.color = Color.BLUE

         // below line is to add bar data set to our bar data.

         // below line is to add bar data set to our bar data.
         val data = BarData(barDataSets[0],barDataSets[1])

         // after adding data to our bar data we
         // are setting that data to our bar chart.

         // after adding data to our bar data we
         // are setting that data to our bar chart.
         barChart.data = data

         // below line is to remove description
         // label of our bar chart.

         // below line is to remove description
         // label of our bar chart.
         barChart.description.isEnabled = false

         // below line is to get x axis
         // of our bar chart.

         // below line is to get x axis
         // of our bar chart.
         val xAxis: XAxis = barChart.xAxis

         // below line is to set value formatter to our x-axis and
         // we are adding our days to our x axis.

         // below line is to set value formatter to our x-axis and
         // we are adding our days to our x axis.
         xAxis.valueFormatter = IndexAxisValueFormatter(days)

         // below line is to set center axis
         // labels to our bar chart.

         // below line is to set center axis
         // labels to our bar chart.
         xAxis.setCenterAxisLabels(true)

         // below line is to set position
         // to our x-axis to bottom.

         // below line is to set position
         // to our x-axis to bottom.
         xAxis.position = XAxis.XAxisPosition.BOTTOM

         // below line is to set granularity
         // to our x axis labels.

         // below line is to set granularity
         // to our x axis labels.
         xAxis.granularity = 1f

         // below line is to enable
         // granularity to our x axis.

         // below line is to enable
         // granularity to our x axis.
         xAxis.isGranularityEnabled = true

         // below line is to make our
         // bar chart as draggable.

         // below line is to make our
         // bar chart as draggable.
         barChart.isDragEnabled = true

         // below line is to make visible
         // range for our bar chart.

         // below line is to make visible
         // range for our bar chart.
         barChart.setVisibleXRangeMaximum(3f)

         // below line is to add bar
         // space to our chart.

         // below line is to add bar
         // space to our chart.
         val barSpace = 0.1f

         // below line is use to add group
         // spacing to our bar chart.

         // below line is use to add group
         // spacing to our bar chart.
         val groupSpace = 0.5f

         // we are setting width of
         // bar in below line.

         // we are setting width of
         // bar in below line.
         data.barWidth = 0.15f

         // below line is to set minimum
         // axis to our chart.

         // below line is to set minimum
         // axis to our chart.
         barChart.xAxis.axisMinimum = 0f

         // below line is to
         // animate our chart.

         // below line is to
         // animate our chart.
         barChart.animate()

         // below line is to group bars
         // and add spacing to it.

         // below line is to group bars
         // and add spacing to it.
         barChart.groupBars(0f, groupSpace, barSpace)

         // below line is to invalidate
         // our bar chart.

         // below line is to invalidate
         // our bar chart.
         barChart.invalidate()
     }*/


    private fun showBarChart(barChart: BarChart, list: List<TransactionsHelper>) {
        initBarChart(barChart, list)


        //now draw bar chart with dynamic data
        val entries: ArrayList<BarEntry> = ArrayList()

        //you can replace this data object with  your custom object
        for (i in list.indices) {
            val score = list[i]
            entries.add(BarEntry(i.toFloat(), score.amount.toFloat()))
        }

        val barDataSet = BarDataSet(entries, "")
        barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)

        val data = BarData(barDataSet)
        barChart.data = data

        barChart.invalidate()
    }

    private fun initBarChart(barChart: BarChart, list: List<TransactionsHelper>) {

//        hide grid lines
        barChart.axisLeft.setDrawGridLines(false)
        val xAxis = barChart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

        //remove right y-axis
        barChart.axisRight.isEnabled = false

        //remove legend
        barChart.legend.isEnabled = false


        //remove description label
        barChart.description.isEnabled = false


        //add animation
        barChart.animateY(3000)

        // to draw label on xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
        xAxis.valueFormatter = MyAxisFormatter(list)
        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = +90f

    }

    inner class MyAxisFormatter(private val list: List<TransactionsHelper>) :
        IndexAxisValueFormatter() {

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            return if (index < list.size) {
                list[index].categoryName
            } else {
                ""
            }
        }
    }

}