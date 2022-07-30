package com.example.foodiesmailru

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.foodiesmailru.R
import com.example.foodiesmailru.databinding.ActivityMainBinding
import com.example.foodiesmailru.dataclasses.Product
import com.example.foodiesmailru.dataclasses.ProductCategory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import java.io.File

class MainActivity : AppCompatActivity() {
    class FolderViewModel : ViewModel() {
        var totalPrice = MutableLiveData<Int>(0)
        var basketOfProducts = MutableLiveData<MutableList<Product>>(mutableListOf<Product>())
        var selectedProduct = MutableLiveData<Product>() //продукт который выбрал юзер в меню
        var selectedCategoryId=MutableLiveData<Int>()
        var categories = MutableLiveData<MutableList<ProductCategory>>()
        var products = MutableLiveData<MutableList<Product>>()
        var mapOfProducts = MutableLiveData<MutableMap<Int,MutableList<Product>>>()
        fun updateBasket() {
            basketOfProducts.value = mutableListOf<Product>()
            for(category in categories.value!!){
                for (product in mapOfProducts.value!![category.id]!!){
                    if(product.count>0){
                        basketOfProducts.value!!.add(product)
                    }
                }
            }
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
        var json = loadJson("categories.json")
        val gson = Gson()
        return gson.fromJson(json, Array<ProductCategory>::class.java).toList().toMutableList()
    }
    private fun loadProducts(): MutableList<Product> {
        var json = loadJson("products.json")
        val gson = Gson()
        return gson.fromJson(json, Array<Product>::class.java).toList().toMutableList()
    }
    private fun setSelectedCategory(){
        model.selectedCategoryId.value = model.categories.value!![0].id
    }
//    public fun getCategories(): MutableList<ProductCategory>{
//        return categories
//    }
//    public fun getMapOfProducts(): MutableMap<Int,MutableList<Product>>{
//        return mapOfProducts
//    }
//    public fun setMapOfProducts(map: MutableMap<Int,MutableList<Product>>){
//        mapOfProducts = map
//    }
//    public fun setSelectedProduct(product: Product){
//        selectedProduct = product
//    }
//    public fun getSelectedProduct():Product{
//        return selectedProduct
//    }

    fun fillBasketOfProductList(){
        for(category in model.categories.value!!){
            for (product in model.mapOfProducts.value!![category.id]!!){
                if(product.count>0){
                    model.basketOfProducts.value!!.add(product)
                }
            }
        }
    }
//    public fun getBasketOfProducts(): MutableList<Product>{
//        return basketOfProducts
//    }
//    public fun getTotalPrice(): Int{
//        var totalPrice: Int = 0
//        for(product in basketOfProducts){
//            totalPrice+=product.price_current
//        }
//        return totalPrice
//    }
}