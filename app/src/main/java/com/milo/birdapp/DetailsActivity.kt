package com.milo.birdapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import kotlin.collections.ArrayList

class DetailsActivity : AppCompatActivity() {

    private val dbHandler = DBHelper(this, null)
    lateinit var nameEditText: EditText
    lateinit var notesEditText: EditText
    private lateinit var raritySpinner: Spinner

    lateinit var modifyId: String

    private var rarityTypes =
        mapOf(Pair("Common", 0), Pair("Rare", 1), Pair("Extremely rare", 2))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        nameEditText = findViewById(R.id.nameEditText)
        notesEditText = findViewById(R.id.notesEditText)
        raritySpinner = findViewById(R.id.raritySpinner)


        raritySpinner.adapter =
            ArrayAdapter(
                applicationContext,
                android.R.layout.simple_list_item_1, ArrayList(rarityTypes.keys)
            )

        raritySpinner.setSelection(rarityTypes["Common"]!!)


        /* Check if activity opened from List Item Click */
        if (intent.hasExtra("id")) {
            modifyId = intent.getStringExtra("id")
            nameEditText.setText(intent.getStringExtra("name"))
            notesEditText.setText(intent.getStringExtra("notes"))
            raritySpinner.setSelection(rarityTypes[intent.getStringExtra("rarity")]!!)
            findViewById<Button>(R.id.btnAdd).visibility = View.GONE
        } else {
            findViewById<Button>(R.id.btnUpdate).visibility = View.GONE
            findViewById<Button>(R.id.btnDelete).visibility = View.GONE
        }
    }

    fun add(v: View) {
        val name = nameEditText.text.toString()
        val notes = notesEditText.text.toString()
        val rarity = rarityTypes[raritySpinner.selectedItem]
        Log.i("RARITY", rarity.toString())

        dbHandler.insertRow(name, rarity.toString(), notes)

        Toast.makeText(this, "Data added", Toast.LENGTH_SHORT).show()
        finish()
    }

    fun update(v: View) {
        val name = nameEditText.text.toString()
        val notes = notesEditText.text.toString()
        val rarity = rarityTypes[raritySpinner.selectedItem]

        dbHandler.updateRow(modifyId, name, rarity.toString(), notes)
        Toast.makeText(this, "Data updated", Toast.LENGTH_SHORT).show()
        finish()
    }

    fun delete(v: View) {
        dbHandler.deleteRow(modifyId)
        Toast.makeText(this, "Data deleted", Toast.LENGTH_SHORT).show()
        finish()
    }
}
