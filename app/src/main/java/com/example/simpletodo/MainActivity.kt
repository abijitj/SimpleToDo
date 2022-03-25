package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOError
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                //1. Remove the item from the list
                listOfTasks.removeAt(position)
                //2. Notify the adapter that our data set has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }
        }

        //1. Let's detect when the user clicks the add button
        findViewById<Button>(R.id.button).setOnClickListener {
            //code in here executes when the user clicks the button
            Log.i("Caren", "User clicked on the button")
        }

        loadItems()

        //Look up the recycler view in the layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        //initialize list
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        //Attach the adapter to the recycler view
        recyclerView.adapter = adapter
        //Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Set up the button and input field so that the user can enter a task
        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        //Get a reference to the button and set an onlick listener to it
        findViewById<Button>(R.id.button).setOnClickListener() {
            //1. Grab the text that the user has inputted into @id/addTaskField
            val userInputtedTask = inputTextField.text.toString()

            //2. Add the string to our list of tasks: listOfTasks
            listOfTasks.add(userInputtedTask)

            //Notify the adapter that our data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)

            //3. Reset the text field
            inputTextField.setText("")

            saveItems()
        }
    }

    //Save the data that the user has inputted by writing and reading from a file

    //Get the file we need
    fun getDataFile(): File {

        //Each line is going to represent one task
        return File(filesDir, "data.txt")
    }

    //Load the items by reading every line in our file
    fun loadItems() {
        try{
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch(ioException: IOException){
            ioException.printStackTrace()
        }
    }

    //Save items by writing the items into the data file
    fun saveItems() {
        try{
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch(ioException: IOException){
            ioException.printStackTrace()
        }
    }

}