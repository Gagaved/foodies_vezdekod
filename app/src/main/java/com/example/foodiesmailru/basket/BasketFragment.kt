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
import com.example.foodiesmailru.databinding.FragmentBasketBinding
import com.example.foodiesmailru.dataclasses.Product
import com.example.foodiesmailru.dataclasses.ProductCategory
import com.example.foodiesmailru.MainActivity

class BasketFragment : Fragment() {
    private var _binding: FragmentBasketBinding? = null
    private val binding get() = _binding!!
    private val model: MainActivity.FolderViewModel by activityViewModels()
    private lateinit var adapterForProducts: ProductsBasketRVAdapter
    override fun onAttach(context: Context) {
        super.onAttach(context)
        adapterForProducts = ProductsBasketRVAdapter(model)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBasketBinding.inflate(inflater, container, false)
        return binding.root
    }
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerViewForProducts = binding.recyclerView
        recyclerViewForProducts.apply {
            layoutManager = GridLayoutManager(context, 1)
        }
        recyclerViewForProducts.adapter = adapterForProducts
        model.totalPrice.observe(viewLifecycleOwner) { _ ->
            binding.button.text = "Заказать за " + (model.totalPrice.value!! / 10).toString() + " ₽"
        }
        model.basketOfProducts.observe(viewLifecycleOwner) { _ ->
            if (model.basketOfProducts.value!!.size <= 0) {
                Log.d("tag", "в обсервере")
                binding.bottomPanelShadow.visibility = View.INVISIBLE
                binding.button.visibility = View.INVISIBLE
                binding.backgroundText.visibility = View.VISIBLE
            }else{
                binding.bottomPanelShadow.visibility = View.VISIBLE
                binding.button.visibility = View.VISIBLE
                binding.backgroundText.visibility = View.INVISIBLE
            }
        }
        adapterForProducts.setItemClickListener {
//            (activity as MainActivity).setSelectedProduct(it)
//            findNavController().navigate(
//                R.id.action_MenuFragment_to_ProductDetailsFragment,
//                arguments
//            )
        }
        binding.button.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}