package com.example.foodiesmailru.menu

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodiesmailru.databinding.FragmentMenuRvBinding
import com.example.foodiesmailru.MainActivity
import com.example.foodiesmailru.R
import com.example.foodiesmailru.dataclasses.Product
import com.example.foodiesmailru.dataclasses.ProductCategory

class MenuRvFragment(private val model: MainActivity.FolderViewModel, private val categoryId: Int) :
    Fragment() {
    private var _binding: FragmentMenuRvBinding? = null
    private val binding get() = _binding!!
    private lateinit var productList: MutableList<Product>
    private lateinit var adapterForProducts: ProductsRVAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuRvBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productList = getProductList(categoryId, model.selectedTagId.value!!)
        val recyclerViewForProducts = binding.productsRV
        recyclerViewForProducts.apply {
            layoutManager = GridLayoutManager(context, 2)
        }
        recyclerViewForProducts.adapter = getNewRVAdapter()
        model.selectedTagId.observe(viewLifecycleOwner) {
            productList = getProductList(categoryId, model.selectedTagId.value!!)
            recyclerViewForProducts.swapAdapter(getNewRVAdapter(),true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getProductList(categoryId: Int, tagId: Int): MutableList<Product> {
        val filteredProductList = mutableListOf<Product>()
        if (tagId == -1) {
            return model.mapOfProducts.value!![categoryId]!!
        }
        for (product in model.mapOfProducts.value!![categoryId]!!) {
            if (product.tag_ids.contains(tagId)) filteredProductList.add(product)
        }
        return filteredProductList
    }

    private fun getNewRVAdapter(): ProductsRVAdapter {
        val newAdapterForProducts = ProductsRVAdapter(model, productList)
        newAdapterForProducts.setItemClickListener {
            model.selectedProduct.value = it
            findNavController().navigate(
                R.id.action_MenuFragment_to_ProductDetailsFragment,
                arguments
            )
        }
        return newAdapterForProducts
    }
}