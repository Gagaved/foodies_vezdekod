package com.example.foodiesmailru

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodiesmailru.dataclasses.Product
import com.example.foodiesmailru.dataclasses.ProductCategory
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    class FolderViewModel : ViewModel() {
        var totalPrice = MutableLiveData<Int>(0)
        var basketOfProducts = MutableLiveData<MutableList<Product>>(mutableListOf<Product>())
        var selectedProduct = MutableLiveData<Product>()
        var selectedCategoryId=MutableLiveData<Int>()
        var categories = MutableLiveData<MutableList<ProductCategory>>()
        var products = MutableLiveData<MutableList<Product>>()
        var mapOfProducts = MutableLiveData<MutableMap<Int,MutableList<Product>>>()
        fun updateBasket() {
            val newValue = mutableListOf<Product>()
            for(category in categories.value!!){
                for (product in mapOfProducts.value!![category.id]!!){
                    if(product.count>0){
                        newValue.add(product)
                    }
                }
            }
            basketOfProducts.value = newValue
        }
        fun notifyObserverOfSelectedProduct(){
            selectedProduct.value = selectedProduct.value
        }
    }
    private val model: FolderViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model.categories.value = loadCategories()
        setSelectedCategory()
        model.products.value = loadProducts()
        model.mapOfProducts.value = model.categories.value!!.associateBy ({ it.id },{ mutableListOf<Product>()}).toMutableMap()
        for(item in model.products.value!!){
            model.mapOfProducts.value!![item.category_id]?.add(item)
        }
        setContentView(R.layout.activity_main)
    }

    private fun loadJson(path: String): String? {
        val iStream = this.assets?.open(path) ?: return null
        val size: Int = iStream.available()
        val buffer = ByteArray(size)
        iStream.read(buffer)
        iStream.close()
        return String(buffer, charset("UTF-8"))
    }
    private fun loadCategories(): MutableList<ProductCategory> {
        val json = loadJson("categories.json")
        val gson = Gson()
        return gson.fromJson(json, Array<ProductCategory>::class.java).toList().toMutableList()
    }
    private fun loadProducts(): MutableList<Product> {
        val json = loadJson("products.json")
        val gson = Gson()
        return gson.fromJson(json, Array<Product>::class.java).toList().toMutableList()
    }
    private fun setSelectedCategory(){
        model.selectedCategoryId.value = model.categories.value!![0].id
    }
}