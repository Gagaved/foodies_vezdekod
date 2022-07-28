package com.example.foodiesmailru.Menu

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodiesmailru.databinding.MenuFragmentBinding
import com.example.foodiesmailru.dataclasses.Product
import com.example.foodiesmailru.dataclasses.ProductCategory
import com.google.gson.Gson
import java.io.IOException
import java.io.InputStream

class MenuFragment : Fragment() {

    fun loadJson(path: String): String? {
        if(activity==null){
            Log.e("jsontag","pizdec2")
        }
        val iStream = activity?.assets?.open(path) ?: return null
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
    private var current_category: Int = -1;
    private lateinit var categories: MutableList<ProductCategory>
    private lateinit var products: MutableList<Product>
    private lateinit var mapOfProducts: MutableMap<Int,MutableList<Product>>
    private lateinit var adapterForCategories: CategoriesRVAdapter
    private lateinit var adapterForProducts: ProductsRVAdapter
    override fun onAttach(context: Context) {
        super.onAttach(context)
        categories = loadCategories()
        current_category = categories[0].id
        products = loadProducts()
        mapOfProducts = categories.associateBy ({ it.id },{ mutableListOf<Product>()}).toMutableMap()
        for(item in products){
            //if(current_category == -1 ) current_category = item.category_id
            mapOfProducts[item.category_id]?.add(item)
        }
        adapterForCategories = CategoriesRVAdapter(categories)
        adapterForProducts = ProductsRVAdapter(mapOfProducts[current_category]!!)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MenuFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    private lateinit var binding: MenuFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("TAG","proverochka")
        val recyclerViewForCategory = binding.categoryRV
        recyclerViewForCategory.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewForCategory.adapter = adapterForCategories

        val recyclerViewForProducts = binding.productsRV
        recyclerViewForProducts.apply {
            layoutManager = GridLayoutManager(context, 2)
        }
        recyclerViewForProducts.adapter = adapterForProducts
        adapterForProducts.setItemClickListener {
        }
        adapterForCategories.setItemClickListener {
            if(it!=current_category){
                Log.d("TAG", "current category:$current_category")
                current_category =it
                recyclerViewForProducts.swapAdapter(ProductsRVAdapter(mapOfProducts[it]!!),true)
            }else{
                Log.d("TAG", "current category:$current_category")
            }
        }
    }
}