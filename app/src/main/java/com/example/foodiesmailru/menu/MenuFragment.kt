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
    private val model: MainActivity.FolderViewModel by activityViewModels()
    private lateinit var adapterForCategories: CategoriesRVAdapter
    private lateinit var adapterForProducts: ProductsRVAdapter
    @SuppressLint("SetTextI18n")
    override fun onAttach(context: Context) {
        super.onAttach(context)
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
        }
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        model.basketOfProducts.observe(viewLifecycleOwner){basket ->
            if(basket.size==0){
                binding.basketCounterIcon.visibility = View.INVISIBLE
                binding.basketCounter.visibility = View.INVISIBLE
            }else{
                binding.basketCounterIcon.visibility = View.VISIBLE
                binding.basketCounter.visibility = View.VISIBLE
                binding.basketCounter.text = basket.size.toString()
            }
        }
        binding.button.setOnClickListener {
            model.updateBasket()
            findNavController().navigate(R.id.action_MenuFragment_to_basketFragment)
            }
        binding.mainToolbar.setOnMenuItemClickListener(){
            if (it.itemId == R.id.basket_menu_button){
                model.updateBasket()
                findNavController().navigate(R.id.action_MenuFragment_to_basketFragment)
            }
            true
        }
    }
}