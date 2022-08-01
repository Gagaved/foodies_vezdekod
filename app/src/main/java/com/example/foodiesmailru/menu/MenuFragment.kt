package com.example.foodiesmailru.menu

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
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
    private var _binding: MenuFragmentBinding? = null
    private val binding get() = _binding!!
    private val model: MainActivity.FolderViewModel by activityViewModels()
    private lateinit var adapterForCategories: CategoriesRVAdapter
    private lateinit var adapterForProducts: ProductsRVAdapter
    private var currentCategory: Int = -1
    override fun onAttach(context: Context) {
        super.onAttach(context)
        adapterForCategories = CategoriesRVAdapter(model)
        adapterForProducts = ProductsRVAdapter(model)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = MenuFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerViewForCategory = binding.categoryRV
        recyclerViewForCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
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
            if (it != currentCategory) {
                currentCategory = it
                recyclerViewForProducts.swapAdapter(ProductsRVAdapter(model), true)
            }
        }
        binding.button.text = (model.totalPrice.value!! / 10).toString() + " ₽"
        model.totalPrice.observe(viewLifecycleOwner) { price ->
            binding.button.text = (price / 10).toString() + " ₽"
        }
        model.basketOfProducts.observe(viewLifecycleOwner) { basket ->
            if (basket.size == 0) {
                binding.basketCounterIcon.visibility = View.INVISIBLE
                binding.basketCounter.visibility = View.INVISIBLE
                binding.bottomPanel.visibility = View.INVISIBLE
                binding.bottomPanelShadow.visibility = View.INVISIBLE
            } else {
                binding.basketCounterIcon.visibility = View.VISIBLE
                binding.basketCounter.visibility = View.VISIBLE
                binding.bottomPanel.visibility = View.VISIBLE
                binding.bottomPanelShadow.visibility =
                    View.VISIBLE//TODO(Объеденить с верхней строкой)
                binding.basketCounter.text = basket.size.toString()
            }
        }
        binding.button.setOnClickListener {
            model.updateBasket()
            findNavController().navigate(R.id.action_MenuFragment_to_basketFragment)
        }

        binding.mainToolbar.setOnMenuItemClickListener()
        {
            if (it.itemId == R.id.basket_menu_button) {
                model.updateBasket()
                findNavController().navigate(R.id.action_MenuFragment_to_basketFragment)
            }
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}