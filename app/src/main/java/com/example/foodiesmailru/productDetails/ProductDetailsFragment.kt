package com.example.foodiesmailru.productDetails

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.foodiesmailru.MainActivity
import com.example.foodiesmailru.R
import com.example.foodiesmailru.databinding.FragmentProductsDetailsBinding

class ProductDetailsFragment : Fragment() {
    private var _binding: FragmentProductsDetailsBinding? = null
    private val binding get() = _binding!!

    private val model: MainActivity.FolderViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsDetailsBinding.inflate(inflater, container, false)
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fun switchVisibility() {
            if (
                binding.button.visibility == View.INVISIBLE &&
                binding.plusButton.visibility == View.VISIBLE &&
                binding.minusButton.visibility == View.VISIBLE &&
                binding.count.visibility == View.VISIBLE
            ) {
                binding.button.visibility = View.VISIBLE
                binding.plusButton.visibility = View.INVISIBLE
                binding.minusButton.visibility = View.INVISIBLE
                binding.count.visibility = View.INVISIBLE
            } else {
                binding.button.visibility = View.INVISIBLE
                binding.plusButton.visibility = View.VISIBLE
                binding.minusButton.visibility = View.VISIBLE
                binding.count.visibility = View.VISIBLE
            }
        }
        binding.name.text = model.selectedProduct.value!!.name
        binding.productDescription.text = model.selectedProduct.value!!.description
        binding.weight.text = model.selectedProduct.value!!.measure.toString()
            .plus(' ') + model.selectedProduct.value!!.measure_unit
        binding.energy.text = model.selectedProduct.value!!.energy_per_100_grams.toString()
            .plus(' ') + getString(R.string.energy_unit)
        binding.proteins.text = model.selectedProduct.value!!.proteins_per_100_grams.toString()
            .plus(' ') + getString(R.string.gram_symbol)
        binding.fats.text = model.selectedProduct.value!!.fats_per_100_grams.toString()
            .plus(' ') + getString(R.string.gram_symbol)
        binding.carbohydrates.text =
            model.selectedProduct.value!!.carbohydrates_per_100_grams.toString()
                .plus(' ') + getString(R.string.gram_symbol)
        binding.button.text =
            getString(R.string.product_details_button_empty).plus(' ') + model.selectedProduct.value!!.price_current / 10 + getString(
                R.string.currency_symbol
            )
        binding.count.text =
            getString(R.string.product_details_button).plus(' ') + model.selectedProduct.value!!.count
        if (model.selectedProduct.value!!.count >= 0) {
            switchVisibility()
        }
        binding.plusButton.setOnClickListener {
            model.selectedProduct.value!!.count += 1
            binding.count.text =
                getString(R.string.product_details_button).plus(' ') + model.selectedProduct.value!!.count
        }
        binding.minusButton.setOnClickListener {
            if (model.selectedProduct.value!!.count > 0) {
                model.selectedProduct.value!!.count -= 1
            }
        }
        binding.button.setOnClickListener {
            model.totalPrice.value =
                model.totalPrice.value!!.plus(model.selectedProduct.value!!.price_current)
            switchVisibility()
            model.selectedProduct.value!!.count += 1
            model.notifyObserverOfSelectedProduct()
        }
        binding.minusButton.setOnClickListener {
            model.selectedProduct.value!!.count--
            model.notifyObserverOfSelectedProduct()
        }
        model.selectedProduct.observe(viewLifecycleOwner) { selectedProduct ->
            if (selectedProduct.count <= 0) {
                switchVisibility()
            }
            binding.count.text =
                getString(R.string.product_details_button).plus(' ') + model.selectedProduct.value!!.count
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}