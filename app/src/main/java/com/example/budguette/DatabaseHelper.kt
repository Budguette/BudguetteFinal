package com.example.budguette

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteCursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "categories.db"
        private const val DATABASE_VERSION = 1
    }

    fun insertCategory(category: Category): Long {
        val values = ContentValues().apply {
            put("title", category.title)
            put("budget", category.budget)
            put("icon", category.icon)
        }
        return writableDatabase.insert("categories", null, values)
    }

    fun updateCategoryTitle(oldTitle: String,category: Category):Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put("title", category.title)
        values.put("budget", category.budget)
        values.put("icon", category.icon)
        val update = db.update("categories", values, "title = ?", arrayOf(oldTitle))
        db.close()
        return update > 0
        
    }


    fun insertExpense(expense: Expense): Long {
        val values = ContentValues().apply {
            put("categoryTitle", expense.categoryTitle)
            put("title", expense.title)
            put("amount", expense.amount)
        }
        return writableDatabase.insert("expenses", null, values)
    }


    @SuppressLint("Range")
    fun getAllExpenses(cate_title: String): List<Expense> {
        val expenses = mutableListOf<Expense>()
        val query = "SELECT * FROM expenses WHERE categoryTitle = ?"
        val cursor = readableDatabase.rawQuery(query, arrayOf(cate_title))

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val categoryTitle = cursor.getString(cursor.getColumnIndex("categoryTitle"))
            val title = cursor.getString(cursor.getColumnIndex("title"))
            val amount = cursor.getInt(cursor.getColumnIndex("amount"))
            expenses.add(Expense(id, categoryTitle, title, amount))
        }
        cursor.close()
        return expenses
    }


    @SuppressLint("Range")
    fun getAllCategories(): List<Category> {
        val categories = mutableListOf<Category>()
        val query = "SELECT * FROM categories"
        var cursor: Cursor = readableDatabase.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val title = cursor.getString(cursor.getColumnIndex("title"))
            val budget = cursor.getInt(cursor.getColumnIndex("budget"))
            val icon = cursor.getBlob(cursor.getColumnIndex("icon"))
            categories.add(Category(id, title, budget, icon))
        }
        cursor.close()
        return categories
    }


    @SuppressLint("Range")
    fun getCategoryByTitle(title: String): Category? {
        val query = "SELECT * FROM categories WHERE title = ?"
        val cursor = readableDatabase.rawQuery(query, arrayOf(title))
        val category = if (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val budget = cursor.getInt(cursor.getColumnIndex("budget"))
            val icon = cursor.getBlob(cursor.getColumnIndex("icon"))
            Category(id, title, budget, icon)
        } else {
            null
        }
        cursor.close()
        return category
    }

    fun deleteCategoryByTitle(title: String): Boolean {
        val whereClause = "title = ?" // the condition to match the title column
        val whereArgs = arrayOf(title) // the value to replace the ? placeholder in the where clause
        val rowsDeleted = writableDatabase.delete("categories", whereClause, whereArgs)
        return rowsDeleted > 0
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createCategoriesTable = "CREATE TABLE categories " +
                "(id INTEGER PRIMARY KEY, " +
                "title TEXT, " +
                "budget INTEGER, " +
                "icon BLOB)"
        db.execSQL(createCategoriesTable)


        val createExpensesTable = "CREATE TABLE expenses " +
                "(id INTEGER PRIMARY KEY, " +
                "categoryTitle TEXT, " +
                "title TEXT, " +
                "amount INTEGER)"
        db.execSQL(createExpensesTable)
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // not needed for this example
    }
}
