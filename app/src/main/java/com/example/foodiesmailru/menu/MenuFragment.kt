package com.example.foodiesmailru.menu
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.foodiesmailru.R
import com.example.foodiesmailru.databinding.FragmentMenuBinding
import com.example.foodiesmailru.MainActivity
import com.google.android.material.tabs.TabLayoutMediator

class MenuFragment : Fragment() {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    private val model: MainActivity.FolderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        setObservers()
        setClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun bind(){
        val mViewPager = binding.viewPager2
        mViewPager.adapter = (ProductsVPAdapter(this, model))
        val tabLayout = binding.tabLayout
        TabLayoutMediator(
            tabLayout,
            mViewPager
        ) { tab, position ->
            tab.text =
                ((mViewPager.adapter) as ProductsVPAdapter?)!!.mFragmentNames[position]
        }.attach()
        binding.button.text = (model.totalPrice.value!! / 10).toString().plus( " ₽")
    }
    private fun setObservers(){
        model.totalPrice.observe(viewLifecycleOwner) { price ->
            binding.button.text = (price / 10).toString().plus( " ₽")
        }
        model.basketOfProducts.observe(viewLifecycleOwner) { basket ->
            if (basket.size == 0) {
                binding.basketCounterIcon.visibility = View.INVISIBLE
                binding.basketCounter.visibility = View.INVISIBLE
                binding.bottomPanel.visibility = View.INVISIBLE
                binding.bottomPanelShadow.visibility =View.INVISIBLE
            } else {
                binding.basketCounterIcon.visibility = View.VISIBLE
                binding.basketCounter.visibility = View.VISIBLE
                binding.bottomPanel.visibility = View.VISIBLE
                binding.bottomPanelShadow.visibility =View.VISIBLE
                binding.basketCounter.text = basket.size.toString()
            }
        }
    }
    private fun setClickListeners() {
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
}
