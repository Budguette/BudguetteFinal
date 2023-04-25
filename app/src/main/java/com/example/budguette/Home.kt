package com.example.budguette

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import kotlin.math.log

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Home : Fragment() {

    lateinit var databaseHelper: DatabaseHelper
    lateinit var pieChart: PieChart


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        pieChart = view.findViewById<PieChart>(R.id.pie_chart)
        databaseHelper = context?.let { DatabaseHelper(it) }!!
        loadCategories()
        return view;
    }

    private fun loadCategories() {
        var overBudget: Int = 0
        var warning: Int = 0
        var good: Int = 0

        val listCategories = databaseHelper.getAllCategories()
        for (i in listCategories.indices) {
            var budgetStatus = getExpensesStats(listCategories[i]);

            when (budgetStatus) {
                1 -> {
                    overBudget += 1
                    Log.d("SayanText", "loadCategories: overBudget")
                }
                2 -> {
                    warning += 1
                    Log.d("SayanText", "loadCategories: warning")
                }
                3 -> {
                    good += 1
                    Log.d("SayanText", "loadCategories: good")
                }
            }
        }


        if (overBudget == 0 && warning == 0 && good == 0) {
            Toast.makeText(context, "No Expenses Fond!", Toast.LENGTH_SHORT).show()
        }

//        val total = overBudget + warning + good // calculate the total value of all categories
//        if (total == 0) {
//            setGraph(, 0f, 0f)
//            return
//        }
//        val overBudgetPercentage =
//            overBudget / total * 100.0f // calculate the percentage of category 1
//        val warningPercentage = warning / total * 100.0f // calculate the percentage of category 2
//        val goodPercentage = good / total * 100.0f // calculate the percentage of category 3


//        Toast.makeText(
//            context,
//            "Over:$overBudget Good: $good Warning:$warning ",
//            Toast.LENGTH_SHORT
//        ).show()
        setGraph(overBudget.toFloat(), warning.toFloat(), good.toFloat())
    }

    private fun setGraph(
        overBudgetPercentage: Float,
        warningPercentage: Float,
        goodPercentage: Float
    ) {
        val entries = listOf(
            PieEntry(goodPercentage, "Good"),
            PieEntry(warningPercentage, "Warning"),
            PieEntry(overBudgetPercentage, "OverBudget")
        )

        val dataSet = PieDataSet(entries, "Budget")
        dataSet.colors = listOf(Color.GREEN, Color.LTGRAY, Color.RED)
        dataSet.valueTextSize = 21f

        val data = PieData(dataSet)

        pieChart.data = data
        pieChart.invalidate()
    }


    //getting all expenses of one category and check if its good overbudget or warning
    private fun getExpensesStats(category: Category): Int {
        val lisExpenses = databaseHelper.getAllExpenses(category.title)
        var spendBudget = 0
        for (i in lisExpenses.indices) {
            spendBudget += lisExpenses[i].amount
        }


        //check budget status 1=overBudget ,2 = warning  ,3 = good
        val bdget =
            (spendBudget * 100) / category.budget


        return if (spendBudget >= category.budget) {
            1
        } else if (bdget >= 90) {
            2
        } else {
            3
        }

    }

}