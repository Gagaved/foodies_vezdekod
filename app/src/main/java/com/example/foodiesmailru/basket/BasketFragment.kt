package com.example.foodiesmailru.basket

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodiesmailru.databinding.FragmentBasketBinding
import com.example.foodiesmailru.MainActivity
import com.example.foodiesmailru.R

class BasketFragment : Fragment() {
    private var _binding: FragmentBasketBinding? = null
    private val binding get() = _binding!!
    private val model: MainActivity.FolderViewModel by activityViewModels()
    private lateinit var adapterForProducts: ProductsBasketRVAdapter
    override fun onAttach(context: Context) {
        super.onAttach(context)
        adapterForProducts = ProductsBasketRVAdapter(model)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBasketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerViewForProducts = binding.recyclerView
        recyclerViewForProducts.apply {
            layoutManager = GridLayoutManager(context, 1)
        }
        recyclerViewForProducts.adapter = adapterForProducts
        model.totalPrice.observe(viewLifecycleOwner) {
            binding.button.text = getString(R.string.basket_button).plus(' ').plus(
                (model.totalPrice.value!! / 10).toString().plus(getString(R.string.currency_symbol))
            )
        }
        model.basketOfProducts.observe(viewLifecycleOwner) {
            if (model.basketOfProducts.value!!.size <= 0) {
                binding.bottomPanelShadow.visibility = View.INVISIBLE
                binding.button.visibility = View.INVISIBLE
                binding.backgroundText.visibility = View.VISIBLE
            } else {
                binding.bottomPanelShadow.visibility = View.VISIBLE
                binding.button.visibility = View.VISIBLE
                binding.backgroundText.visibility = View.INVISIBLE
            }
        }
        adapterForProducts.setItemClickListener {
            model.selectedProduct.value = it
            findNavController().navigate(R.id.action_basketFragment_to_ProductDetailsFragment)
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
        _binding = null
    }
}