package com.example.foodiesmailru.menu

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodiesmailru.R
import com.example.foodiesmailru.databinding.MenuFragmentBinding
import com.example.foodiesmailru.MainActivity

class MenuFragment : Fragment() {
    private var current_category: Int = -1;
    private var product_list_index: Int = -1;
    private val model: MainActivity.FolderViewModel by activityViewModels()
//    private var totalPrice: Int = 0;
//    private lateinit var categories: MutableList<ProductCategory>
//    private lateinit var products: MutableList<Product>
//    private lateinit var mapOfProducts: MutableMap<Int,MutableList<Product>>
    private lateinit var adapterForCategories: CategoriesRVAdapter
    private lateinit var adapterForProducts: ProductsRVAdapter
    @SuppressLint("SetTextI18n")
    override fun onAttach(context: Context) {
        super.onAttach(context)
//        categories = (activity as MainActivity).getCategories()
//        current_category = categories[0].id
//        mapOfProducts = (activity as MainActivity).getMapOfProducts()
//        totalPrice = ((activity as MainActivity).getTotalPrice())
//        mapOfProducts = categories.associateBy ({ it.id },{ mutableListOf<Product>()}).toMutableMap()
//        for(item in products){
//            //if(current_category == -1 ) current_category = item.category_id
//            mapOfProducts[item.category_id]?.add(item)
//        }
        adapterForCategories = CategoriesRVAdapter(model)
        adapterForProducts = ProductsRVAdapter(model)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MenuFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    private lateinit var binding: MenuFragmentBinding

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        //binding.button.text = "В корзину за " + (model.totalPrice.value!!/10).toString()+" ₽"
    }
    @SuppressLint("SetTextI18n")
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
            model.selectedProduct.value = it
            findNavController().navigate(
                R.id.action_MenuFragment_to_ProductDetailsFragment,
                arguments
            )
        }
        adapterForCategories.setItemClickListener {
            if(it!=current_category){
                Log.d("TAG", "current category:$current_category")
                current_category =it
                recyclerViewForProducts.swapAdapter(ProductsRVAdapter(model),true)
            }else{
                Log.d("TAG", "current category:$current_category")
            }
        }
        binding.button.text = (model.totalPrice.value!!/10).toString()+" ₽"
        model.totalPrice.observe(viewLifecycleOwner){price ->
            binding.button.text = (price/10).toString()+" ₽"
        }

        //OTLADKA
        binding.button.setOnClickListener {
            model.updateBasket()
            findNavController().navigate(R.id.action_MenuFragment_to_basketFragment)
            }

    }

        //OTLADKA

//    public fun getSelectedProduct(): Product{
//        return mapOfProducts[current_category]!![product_list_index]
//    }
}