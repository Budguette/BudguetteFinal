package com.example.budguette

import java.time.temporal.TemporalAmount


data class Expense(
    val id: Int,
    val categoryTitle: String,
    val title: String,
    val amount: Int,
)

