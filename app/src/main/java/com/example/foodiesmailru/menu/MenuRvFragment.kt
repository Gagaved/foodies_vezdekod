package com.example.foodiesmailru.menu
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

class MenuRvFragment(private val model: MainActivity.FolderViewModel,private val categoryId: Int) : Fragment() {
    private var _binding: FragmentMenuRvBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapterForProducts: ProductsRVAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuRvBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterForProducts = ProductsRVAdapter(model, categoryId)
        val recyclerViewForProducts = binding.productsRV
        recyclerViewForProducts.apply {
            layoutManager = GridLayoutManager(context,2)
        }
        adapterForProducts.setItemClickListener {
            model.selectedProduct.value = it
            findNavController().navigate(
                R.id.action_MenuFragment_to_ProductDetailsFragment,
                arguments
            )
        }
        recyclerViewForProducts.adapter = adapterForProducts
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}