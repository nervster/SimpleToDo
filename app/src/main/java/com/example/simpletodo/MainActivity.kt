package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listofTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                // Remove item from the list
                listofTasks.removeAt(position)
                // notify adapter
                adapter.notifyDataSetChanged()
                saveItems()
            }
        }

//        // Detect button click
//        findViewById<Button>(R.id.button).setOnClickListener{
//            Log.i("Caren", "User clicked button")
//        }

        loadItems()

        // look up recycler view in the layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = TaskItemAdapter(listofTasks, onLongClickListener)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val inputTextField = findViewById<EditText>(R.id.addTaskField)
        // set up button and input field to insert task
        findViewById<Button>(R.id.button).setOnClickListener {
            // grab text
            val userInputtedTask = inputTextField.text.toString()

            // add string to list of task
            listofTasks.add(userInputtedTask)

            // notify the adapter
            adapter.notifyItemInserted(listofTasks.size - 1)

            // reset text field
            inputTextField.setText("")
            saveItems()

        }
    }

    // save the data user has inputted
    // Save the data by reading and writing from file

    // create a method to get the file we need
    fun getDataFile(): File {

        // Every line will be a new task
        return File(filesDir, "data.txt")
    }

    // load the items by reading every line in the data file
    fun loadItems() {
        try {
            listofTasks = org.apache.commons.io.FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()

        }
    }

    // save items by writing them into our data file
    fun saveItems() {
        try {
            org.apache.commons.io.FileUtils.writeLines(getDataFile(), listofTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
}