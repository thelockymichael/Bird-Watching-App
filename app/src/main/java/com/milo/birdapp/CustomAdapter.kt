package com.milo.birdapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.milo.sqlitesavebitmap.Utils.Utils
import java.lang.Exception
import java.net.URLDecoder

class CustomAdapter(
    private val context: Context,
    private val birdList: ArrayList<Bird>,
    private val rarityTypes: Map<Int, String> = mapOf(
        Pair(0, "Common"),
        Pair(1, "Rare"),
        Pair(2, "Extremely rare")
    )


) : BaseAdapter() {
    private val inflater: LayoutInflater =
        this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getItem(position: Int): Any {
        return birdList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return birdList.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        Log.i("VIEW", convertView.toString())
        var dataitem = birdList[position]

        val rowView = inflater.inflate(R.layout.list_row, parent, false)

        rowView.findViewById<TextView>(R.id.row_name).text = dataitem.name
        rowView.findViewById<TextView>(R.id.row_rarity).text = "(${rarityTypes[dataitem.rarity]})"
        rowView.findViewById<TextView>(R.id.row_notes).text = dataitem.notes
        rowView.findViewById<TextView>(R.id.row_date).text = dataitem.date
        rowView.findViewById<TextView>(R.id.row_address).text = dataitem.address
/*
        rowView.findViewById<TextView>(R.id.row_address).text = dataitem.address
*/

        if (dataitem.image != null) {
            val bitmap = BitmapFactory.decodeByteArray(dataitem.image, 0, dataitem.image!!.size)

            rowView.findViewById<ImageView>(R.id.row_image).setImageBitmap(bitmap)

            Log.i("PERKELE", "PERKELE")
        }

        rowView.tag = position
        return rowView
    }
}