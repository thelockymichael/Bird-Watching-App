package com.milo.birdapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.ArraySet
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.drawToBitmap
import com.milo.sqlitesavebitmap.Utils.Utils
import kotlinx.android.synthetic.main.activity_details.*
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.absoluteValue


class DetailsActivity : AppCompatActivity() {

    private val dbHandler = DBHelper(this, null)

    // UI
    private lateinit var nameEditText: EditText
    private lateinit var notesEditText: EditText
    private lateinit var toolBar: Toolbar
    private lateinit var uploadImageButton: Button
    private lateinit var uploadImageView: ImageView

    private lateinit var raritySpinner: Spinner

    // Temp strings
    private lateinit var modifyId: String
    //private lateinit var latitude: String
    //private lateinit var longitude: String
    private var latLng: String = ""
    private var address: String = ""

    private var rarityTypes =
        mapOf(Pair("Common", 0), Pair("Rare", 1), Pair("Extremely rare", 2))

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        // Setting up image related vars
        uploadImageButton = findViewById(R.id.uploadImageButton)
        uploadImageView = findViewById(R.id.uploadImageView)

        // Setting up Toolbar
        toolBar = findViewById(R.id.toolBar)
        setSupportActionBar(toolBar)

        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

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
            latLng = intent.getStringExtra("latLng")
            address = intent.getStringExtra("address")
            Log.i("LATLNG", "From item Click: " + latLng)
            Log.i("LATLNG", "From item Click: " + address)

            uploadImageView.setImageBitmap(Utils.getBitmapFromMemCache(intent.getStringExtra("id")))

            findViewById<Button>(R.id.btnAdd).visibility = View.GONE

        } else {
            findViewById<Button>(R.id.btnUpdate).visibility = View.GONE
            findViewById<Button>(R.id.btnDelete).visibility = View.GONE
        }

        uploadImageButton.setOnClickListener {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            } else {
                getPhoto()
            }
        }

        /*markBirdButton.setOnClickListener {

            startActivityForResult(
                Intent(applicationContext, MarkBirdLocationActivity::class.java),
                1
            )
            //startActivity(Intent(applicationContext, MarkBirdLocationActivity::class.java))
        }*/
    }

    fun setBirdMarker(view: View) {

        val intent = Intent(applicationContext, MarkBirdLocationActivity::class.java)
        intent.putExtra("address", address)
        intent.putExtra("latLng", latLng)

        Log.i("LATLNG", "setBirdMarker: " + latLng)
        Log.i("LATLNG", "setBirdMarker: " + address)

        startActivityForResult(intent, 1)
        //startActivity(Intent(applicationContext, MarkBirdLocationActivity::class.java))
    }

    private fun getPhoto() {
        var intent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoto()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var selectedImage: Uri? = data?.data
        Log.i("REMOVING", "onActivityResult")

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && selectedImage != null) {
            try {
                var bitMap: Bitmap =
                    MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)


                uploadImageView.setImageBitmap(bitMap)
                Utils.addBitmapToMemoryCache(intent.getStringExtra("id"), bitMap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if (requestCode == 1 && resultCode == RESULT_OK && data!!.hasExtra("latLng")) {
            Log.i("ADDRESS", "PERKELE")
            Log.i("LATLNG", "DetailsActivity: " + data!!.getStringExtra("latLng"))
            Log.i("LATLNG", "DetailsActivity: " + data!!.getStringExtra("address"))

            latLng = data!!.getStringExtra("latLng")
            //latitude = data!!.getStringExtra("latitude")
            //longitude = data!!.getStringExtra("longitude")
            address = data!!.getStringExtra("address")
        }
    }

    fun add(v: View) {

        val name = nameEditText.text.toString()
        val notes = notesEditText.text.toString()
        val rarity = rarityTypes[raritySpinner.selectedItem]
        val image = uploadImageView


        //Log.i("IMAGE", image.size.toString())

        dbHandler.insertRow(
            name,
            rarity.toString(),
            notes,
            Utils.getBytes(image.drawToBitmap()),
            latLng, address

            //latitude, longitude, address
        )


        Toast.makeText(this, "Data added", Toast.LENGTH_SHORT).show()
        finish()
    }

    fun update(v: View) {
        val name = nameEditText.text.toString()
        val notes = notesEditText.text.toString()
        val rarity = rarityTypes[raritySpinner.selectedItem]
        val image = uploadImageView

        dbHandler.updateRow(
            modifyId,
            name,
            rarity.toString(),
            notes,
            Utils.getBytes(image.drawToBitmap()),
            latLng, address
            //latitude, longitude, address,
        )


        Toast.makeText(this, "Data updated", Toast.LENGTH_SHORT).show()
        finish()
    }

    fun delete(v: View) {
        dbHandler.deleteRow(modifyId)
        Toast.makeText(this, "Data deleted", Toast.LENGTH_SHORT).show()
        finish()
    }
}

