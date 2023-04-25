package com.example.budguette

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast


class ExpensesFragment : Fragment() {
    lateinit var categoryBudgetTextview: TextView
    lateinit var categoryTextview: TextView
    lateinit var itemTv: TextView
    lateinit var remainingAmountTv: TextView
    lateinit var itemNameEditText: EditText
    lateinit var itemAmountEditText: EditText
    lateinit var btnAdd: Button
    lateinit var databaseHelper: DatabaseHelper

    private var id: Int = 0
    private var title: String? = null
    private var budget: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getInt("id")
            title = it.getString("title")
            budget = it.getInt("budget")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_expenses, container, false)

        databaseHelper = context?.let { DatabaseHelper(it) }!!

        categoryBudgetTextview = view.findViewById(R.id.categoryBudgetTextview)
        categoryTextview = view.findViewById(R.id.categoryTextview)
        itemTv = view.findViewById(R.id.itemTv)
        remainingAmountTv = view.findViewById(R.id.remainingAmountTv)
        itemNameEditText = view.findViewById(R.id.itemNameEditText)
        itemAmountEditText = view.findViewById(R.id.itemAmountEditText)
        btnAdd = view.findViewById(R.id.btnAdd)

        btnAdd.setOnClickListener {
            addItem()
        }
        loadExpenses()

        return view
    }

    private fun addItem() {
        val itemName = itemNameEditText.text.toString()
        val itemAmount = itemAmountEditText.text.toString()

        if (itemName.isEmpty()) {
            Toast.makeText(context, "Add title", Toast.LENGTH_SHORT).show()
            itemNameEditText.error = "Add title"
            itemAmountEditText.requestFocus()
        } else if (itemAmount.isEmpty()) {
            Toast.makeText(context, "Add Amount", Toast.LENGTH_SHORT).show()
            itemAmountEditText.error = "Add Amount"
            itemAmountEditText.requestFocus()
        } else {

            var let =
                databaseHelper.insertExpense(Expense(0, title!!, itemName, itemAmount.toInt()))
            if (let > 0) {
                loadExpenses()
                itemNameEditText.setText(null)
                itemAmountEditText.setText(null)
                Toast.makeText(context, "Item Added", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Item didn't Added", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun loadExpenses() {
        var usedBalance: Int = 0;
        var remainingBalance = 0

        val listExpenses = title?.let { databaseHelper.getAllExpenses(it) }
        val message: StringBuffer = StringBuffer()
        if (listExpenses != null) {
            for (i in listExpenses.indices) {
                message.append(listExpenses[i].title + "         " + listExpenses[i].amount + "$\n")
                usedBalance += listExpenses[i].amount
            }
        }

        remainingBalance = budget!! - usedBalance
        itemTv.text = message
        remainingAmountTv.text = "Remaining :$remainingBalance"
        categoryBudgetTextview.text = "Total budget $budget $"
        categoryTextview.text=title

    }

}