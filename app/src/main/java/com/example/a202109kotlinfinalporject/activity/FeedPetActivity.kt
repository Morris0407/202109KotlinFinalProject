package com.example.a202109kotlinfinalporject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.a202109kotlinfinalporject.MyAdapter
import com.example.a202109kotlinfinalporject.R
import com.example.a202109kotlinfinalporject.dataclass.FoodItem
import org.w3c.dom.Text

class FeedPetActivity : AppCompatActivity() {


    private var petName: String = ""
    private var growPoint: Int = 0
    private var foodItems: ArrayList<FoodItem> = ArrayList()
    private lateinit var foodItemCounts: IntArray
    private var eggLove = arrayOf(0, 1)
    private var boxLove = arrayOf(2, 3)
    private var stoneLove = arrayOf(4, 5)
    private val fullGrowPoint = 50
    private var isFullGrowPoint: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_pet)

        var growPointTextView = findViewById<TextView>(R.id.growPointTextView)


        intent?.extras?.let{
            growPointTextView.text = "成長值:${it.getInt("growPoint").toString()}"
            growPoint = it.getInt("growPoint")
            foodItemCounts = it.getIntArray("foodItemsArray")!!
            petName = it.getString("petName").toString()
        }

        setPetImage()
        setFoodItem()
        setListener()
        setGridView()
    }

    private fun setGridView() {
        val gridView = findViewById<GridView>(R.id.feedPetGridView)

        gridView.numColumns = 3
        gridView.adapter = MyAdapter(this, foodItems, R.layout.foodlayout)

        gridView.onItemClickListener =
        AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedItemText: FoodItem = parent.getItemAtPosition(position) as FoodItem
            val selectedItem = view.findViewById<TextView>(R.id.foodCountTextView)
            Log.i("FeedPetActivity", "selectId ${id}")
            if(selectedItemText.count > 0) {
                var tmpId: Int = id.toInt()
                addGrowPoint(tmpId)
                decreaseCount(selectedItemText.name, selectedItem)
                isFullGrowPoint()
            }
            else showToast("沒有更多的食物了")
        }
    }

    private fun isFullGrowPoint() {
        if(growPoint >= fullGrowPoint) showToast("Is full growPoint")
    }


    private fun setFoodItem() {
        val foodName = ArrayList<String>()
        foodName.add("火")
        foodName.add("油")
        foodName.add("剪刀")
        foodName.add("膠水")
        foodName.add("木鎬")
        foodName.add("砂紙")

        val foodPrice = ArrayList<Int>()
        foodPrice.add(10)
        foodPrice.add(30)
        foodPrice.add(50)
        foodPrice.add(20)
        foodPrice.add(200)
        foodPrice.add(30)

        val array = resources.obtainTypedArray(R.array.food_image_list)
        for(i in 0 until array.length()) {
            var photo: Int = array.getResourceId(i, 0)
            var name: String = foodName[i]
            var price: Int = foodPrice[i]

            foodItems.add(FoodItem(photo, name, foodItemCounts[i], price))
        }
        array.recycle()
    }

    private fun decreaseCount(name: String, textView: TextView) {
        for (i in 0..(foodItemCounts.size - 1)) {
            if (foodItems[i].name == name) {
                foodItemCounts[i]--
                foodItems[i].count--
                textView.text = "數量:${foodItems[i].count.toString()}"
                break
            }
        }
    }

    private fun setPetImage() {
        val petImageButton = findViewById<ImageButton>(R.id.petButton)

        if(isFullGrowPoint) {

        }else {
            if(petName == "箱子") {
                petImageButton.setImageResource(R.drawable.box)
            }else if(petName == "蛋") {
                petImageButton.setImageResource(R.drawable.egg)
            }else if(petName == "石頭") {
                petImageButton.setImageResource(R.drawable.stone)
            }
        }
    }


    private fun addGrowPoint(id: Int) {
        if((petName == "蛋" && eggLove.contains(id)) ||
            (petName == "箱子" && boxLove.contains(id)) ||
            (petName == "石頭" && stoneLove.contains(id)))
            growPoint += 40
        else
            growPoint+= 20
        var growPointTextView = findViewById<TextView>(R.id.growPointTextView)
        growPointTextView.text = "成長值:${growPoint}"
    }

    private fun setListener() {
        setReturnButton()
    }

    private fun setReturnButton() {
        var returnButton = findViewById<Button>(R.id.feedPetReturnButton)

        returnButton.setOnClickListener {
            val bundle = Bundle()

            bundle.putInt("growPoint", growPoint)
            bundle.putIntArray("foodItemCounts", foodItemCounts)
            setResult(RESULT_OK, Intent().putExtras(bundle))

            finish()
        }
    }

    private fun showToast(text: String) =
        Toast.makeText(this,text, Toast.LENGTH_LONG).show()

}