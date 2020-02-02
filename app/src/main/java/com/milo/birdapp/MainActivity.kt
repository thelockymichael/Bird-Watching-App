package com.milo.birdapp

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    val dbHandler = DBHelper(this, null)
    var dataList = ArrayList<HashMap<String, String>>()

    private lateinit var spinner: Spinner
    private lateinit var textView: TextView

    private lateinit var listView: ListView
    private var customAdapter: CustomAdapter? = null

    private var rarityTypes =
        mapOf(Pair(0, "Common"), Pair(1, "Rare"), Pair(2, "Extremely rare"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
        spinner = findViewById(R.id.sortSpinner)
        listView = findViewById(R.id.listView)
        //customAdapter = CustomAdapter()

        val sortNames = resources.getStringArray(R.array.sort_types)

        var myAdapter = ArrayAdapter<String>(
            applicationContext,
            android.R.layout.simple_list_item_1,
            sortNames
        )

        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = myAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                when (sortNames[position]) {
                    "SORT BY NAME" -> loadIntoList("ORDER BY LENGTH(name) DESC")
                    "SORT BY RARITY" -> loadIntoList("ORDER BY rarity DESC")
                    "SORT BY NOTES" -> loadIntoList("ORDER BY LENGTH(notes) DESC")
                    "SORT BY DATE" -> loadIntoList("ORDER BY date DESC")
                }
                Toast.makeText(
                    this@MainActivity,
                    "" + sortNames[position], Toast.LENGTH_SHORT
                ).show()

            }
        }

    }


    fun loadIntoList(orderBy: String) {
        dataList.clear()
        val cursor: Cursor? = dbHandler.getAllRow(orderBy)
        cursor!!.moveToFirst()

        while (!cursor.isAfterLast) {

            var arvo = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_RARITY))
            var string = rarityTypes[arvo.toInt()]


            val map = HashMap<String, String>()
            map["id"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ID))
            map["name"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME))
            map["rarity"] = string.toString()
            map["notes"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NOTES))
            map["date"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DATE))

            Log.i("DATE", cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DATE)))

            dataList.add(map)
            cursor.moveToNext()
        }

        customAdapter?.notifyDataSetChanged()

        if (dataList.count() == 0) {
            textView.text = "Add a bird."
        } else {
            textView.visibility = View.GONE

            customAdapter = CustomAdapter(this@MainActivity, dataList)
            listView.adapter = customAdapter

            findViewById<ListView>(R.id.listView).setOnItemClickListener { _, _, i, _ ->
                val intent = Intent(this, DetailsActivity::class.java)
                intent.putExtra("id", dataList[+i]["id"])
                intent.putExtra("name", dataList[+i]["name"])
                intent.putExtra("notes", dataList[+i]["notes"])
                intent.putExtra("rarity", dataList[+i]["rarity"])
                startActivity(intent)
            }
        }
    }

    fun fabClicked(v: View) {
        val intent = Intent(this, DetailsActivity::class.java)
        startActivity(intent)
    }

    public override fun onResume() {
        super.onResume()
        loadIntoList("ORDER BY date DESC")
    }
}