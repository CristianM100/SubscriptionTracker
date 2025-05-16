package com.example.subscriptiontracker.ui.category

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.subscriptiontracker.R
import com.example.subscriptiontracker.viewmodel.SubscriptionCategoryViewModel
import com.example.subscriptiontracker.ui.adapter.SubscriptionCategoryAdapter
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate


class SubscriptionCategoryActivity : AppCompatActivity() {

    private lateinit var viewModel: SubscriptionCategoryViewModel
    private lateinit var adapter: SubscriptionCategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscription_category)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        adapter = SubscriptionCategoryAdapter(emptyList(), object : SubscriptionCategoryAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
            }
        })
        recyclerView.adapter = adapter

        viewModel = ViewModelProvider(this).get(SubscriptionCategoryViewModel::class.java)

        viewModel.subscriptionsByCategory.observe(this) { items ->
            adapter.updateItems(items)
        }

        val pieChart: PieChart = this.findViewById(R.id.pieChart)

        viewModel.groupedByCategoryAmount.observe(this) { groupedMap ->
            val entries = ArrayList<PieEntry>()

            groupedMap.forEach { (category, totalAmount) ->
                entries.add(PieEntry(totalAmount.toFloat(), category))
            }

            val legend = pieChart.legend
            legend.isEnabled = true
            legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            legend.orientation = Legend.LegendOrientation.HORIZONTAL
            legend.setDrawInside(false)
            legend.textSize = 20f

            val dataSet = PieDataSet(entries, "")
            dataSet.setColors(ColorTemplate.MATERIAL_COLORS, 255)

            val data = PieData(dataSet)
            data.setValueTextSize(15f)
            data.setValueFormatter(object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return String.format("%.1f%%", value)
                }
            })

            pieChart.data = data
            pieChart.description.isEnabled = false
            pieChart.centerText = "Monthly Average(%)"
            pieChart.setCenterTextSize(25f)
            pieChart.setUsePercentValues(true)
            pieChart.animateY(1000)
            legend.setDrawInside(false)
            legend.yOffset = 50f
            pieChart.setExtraOffsets(10f, 30f, 10f, 30f)
            pieChart.invalidate()
        }
    }
}


