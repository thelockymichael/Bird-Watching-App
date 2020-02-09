package com.milo.sqlitesavebitmap.Utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.collection.LruCache
import java.io.ByteArrayOutputStream


object Utils {
    var mMemoryCache = LruCache<String, Bitmap?>(1024)

    init {
        var maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()

        var cacheSize = maxMemory / 8

        mMemoryCache = object : LruCache<String, Bitmap?>(cacheSize) {
            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                return bitmap.getByteCount() / 1024; }
        }
    }

    fun getBytes(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream)
        return stream.toByteArray()
    }

    fun getImage(image: ByteArray?): Bitmap? {
        return BitmapFactory.decodeByteArray(image!!, 0, image!!.size)
    }


    fun getBitmapFromMemCache(key: String): Bitmap? {

        return mMemoryCache[key]
    }

    fun addBitmapToMemoryCache(key: String, bitmap: Bitmap?) {

/*        if (getBitmapFromMemCache(key) != null) {
            mMemoryCache.remove(key)
            mMemoryCache.put(key, bitmap!!)
        }*/
        Log.i("INFOA", mMemoryCache.size().toString())
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.remove(key)
            mMemoryCache.put(key, bitmap!!)
            Log.i("REMOVING", "REMOVING")
        }
    }

}
