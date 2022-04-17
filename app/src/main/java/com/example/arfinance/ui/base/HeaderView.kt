package com.example.arfinance.ui.base


import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import com.example.arfinance.R
import com.example.arfinance.data.dataModel.Transactions
import com.example.arfinance.databinding.HeaderViewBinding
import com.example.arfinance.util.enumerian.BalanceTime
import com.example.arfinance.util.moneyFormatter
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.util.*


class HeaderView(
    contextInstant: Context,
    attrs: AttributeSet
) : FrameLayout(contextInstant, attrs) {


    private var binding: HeaderViewBinding

    private var selectedTime = BalanceTime.WEEK

    init {
        val view = inflate(context, R.layout.header_view, this)

        binding = HeaderViewBinding.inflate(LayoutInflater.from(context), this, true)
        //get attrs
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.HeaderView)
        val totalBalance = typedArray.getString(R.styleable.HeaderView_total_balance)
        val totalIncome = typedArray.getString(R.styleable.HeaderView_total_income)
        val totalExpense = typedArray.getString(R.styleable.HeaderView_total_expense)
        //recycle type array after use
        typedArray.recycle()


        /*binding.totalBalance.text = totalBalance
        binding.income.text = totalIncome
        binding.expense.text = totalExpense*/


    }

    fun setupPieChart() {
        binding.pieChart.apply {
            isDrawHoleEnabled = true
            setUsePercentValues(true)
            setEntryLabelTextSize(12f)
            setEntryLabelColor(Color.BLACK)
            centerText = "Expenses by Category"
            setCenterTextSize(24f)
            description.isEnabled = false
        }

        val legend: Legend = binding.pieChart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.setDrawInside(false)
        legend.isEnabled = true
    }

    fun loadCharData(transactions: List<Transactions>) {
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

        val _data = PieData(dataSet)
        _data.setDrawValues(true)
        _data.setValueFormatter(PercentFormatter())
        _data.setValueTextSize(12f)
        _data.setValueTextColor(Color.BLACK)

        binding.pieChart.apply {
            data = _data
            invalidate()
            animateY(1400, Easing.EaseInOutQuad)
        }
    }

    fun showChart() {
        binding.pieChart.visibility = View.VISIBLE
        binding.noDataForPieChartTxt.visibility = View.GONE
    }

    fun hideChart() {
        binding.pieChart.visibility = View.GONE
        binding.noDataForPieChartTxt.visibility = View.INVISIBLE
    }


    //todo can use attrs instead of these three functions
    fun formatIncome(item: Long) {
        binding.income.text = moneyFormatter(item)
    }

    fun formatExpense(item: Long) {
        binding.expense.text = moneyFormatter(item)

    }

    fun formatBalance(item: Long) {
        binding.totalBalance.text = moneyFormatter(item)

    }

    // TODO return view null in onItemSelectedListener
    fun setUpSpinner() {
        val times = resources.getStringArray(R.array.Times)
        val spinner = binding.txtTotalBalanceSpinner ?: return

        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, times) ?: return
        spinner.adapter = adapter


        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                updateSelectedTime(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun updateSelectedTime(item: Int) {
        selectedTime = when (item) {
            0 -> BalanceTime.WEEK
            1 -> BalanceTime.MONTH
            2 -> BalanceTime.THREEMONTHS
            else -> BalanceTime.WEEK
        }
    }

    fun getSelectedTime() = selectedTime
}
