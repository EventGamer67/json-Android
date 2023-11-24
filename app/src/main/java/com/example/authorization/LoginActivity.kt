package com.example.authorization

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.nio.charset.Charset


class LoginActivity : AppCompatActivity() {

    private lateinit var itemsListView: LinearLayout
    private lateinit var searchEditText: EditText
    private lateinit var sortRadioGroup: RadioGroup
    private lateinit var itemsAdapter: ArrayAdapter<String>

    private var itemsList = mutableListOf<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        itemsListView = findViewById(R.id.itemsListView)
        searchEditText = findViewById(R.id.searchEditText)
        sortRadioGroup = findViewById(R.id.sortRadioGroup)
        // Load items from JSON
        loadItemsFromJson()

        // Add text change listener for search
//        searchEditText.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                filterItems(s.toString())
//            }
//
//            override fun afterTextChanged(s: Editable?) {}
//        })
//
//        // Set radio group listener for sorting
//        sortRadioGroup.setOnCheckedChangeListener { _, checkedId ->
//            when (checkedId) {
//                R.id.radioAscending -> sortItemsAscending()
//                R.id.radioDescending -> sortItemsDescending()
//            }
//        }
    }

    private fun loadItemsFromJson() {
        try {
            val json = loadJSONFromAsset("items.json")
            val jsonArray = JSONArray(json)

            for (i in 0 until jsonArray.length()) {

                val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                val item = Item(
                    jsonObject.getInt("number"),
                    jsonObject.getString("name"),
                    jsonObject.getInt("quantity"),
                    jsonObject.getDouble("price"),
                    jsonObject.getString("image"),
                )

                val tile = layoutInflater.inflate(R.layout.list_item_layout, null)
                tile.findViewById<TextView>(R.id.itemNumberTextView).setText("Номер: "+item.number)
                tile.findViewById<TextView>(R.id.itemNameTextView).setText("Наименование: "+item.name)
                tile.findViewById<TextView>(R.id.itemQuantityTextView).setText("Количество: "+item.quantity)
                tile.findViewById<TextView>(R.id.itemPriceTextView).setText("Цена: "+item.price)

                Glide.with(this)
                    .load(item.image)
                    .into(tile.findViewById<ImageView>(R.id.image))

                itemsListView.addView(tile)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadJSONFromAsset(filename: String): String {
        var json: String? = null
        try {
            val inputStream: InputStream = assets.open(filename)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charset.forName("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return ""
        }
        return json
    }

}

