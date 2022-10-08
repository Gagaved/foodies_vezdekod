package com.example.foodiesmailru.menu

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodiesmailru.MainActivity
import com.example.foodiesmailru.R
import com.example.foodiesmailru.databinding.FragmentMenuBinding
import com.example.foodiesmailru.dataclasses.ProductTag
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator


class MenuFragment : Fragment() {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    private val model: MainActivity.FolderViewModel by activityViewModels()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
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

    private fun bind() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // handle onSlide
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    binding.bottomSheetShadow.visibility = View.INVISIBLE
                    Toast.makeText(context, "STATE_HIDDEN", Toast.LENGTH_SHORT).show()
                }
            }
        })
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.bottomSheetShadow.visibility = View.INVISIBLE

        binding.bottomSheetList.adapter = TagsRVAdapter(model)
        binding.bottomSheetList.apply {
            layoutManager = GridLayoutManager(context, 1)
        }
        val dividerItemDecoration = DividerItemDecoration(
            binding.bottomSheetList.context,
            LinearLayoutManager.VERTICAL
        )
        binding.bottomSheetList.addItemDecoration(dividerItemDecoration)


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
        binding.button.text = (model.totalPrice.value!! / 10).toString().plus(" ₽")
    }

    private fun setObservers() {
        model.totalPrice.observe(viewLifecycleOwner) { price ->
            binding.button.text = (price / 10).toString().plus(" ₽")
        }
        model.basketOfProducts.observe(viewLifecycleOwner) { basket ->
            if (basket.size == 0) {
                binding.bottomPanel.visibility = View.INVISIBLE
                binding.bottomPanelShadow.visibility = View.INVISIBLE
            } else {
                binding.bottomPanel.visibility = View.VISIBLE
                binding.bottomPanelShadow.visibility = View.VISIBLE
            }
        }
    }

    private fun setClickListeners() {
        binding.bottomSheetButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            binding.bottomSheetShadow.visibility = View.INVISIBLE
        }
        binding.mainToolbar.setNavigationOnClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            else
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            binding.bottomSheetShadow.visibility = View.VISIBLE
        }
        binding.button.setOnClickListener {
            model.updateBasket()
            findNavController().navigate(R.id.action_MenuFragment_to_basketFragment)
        }
        binding.mainToolbar.setOnMenuItemClickListener()
        {
            if (it.itemId == R.id.search_menu_button) {
                model.updateBasket()
                findNavController().navigate(R.id.action_MenuFragment_to_searchFragment)
            }
            true
        }
        (binding.bottomSheetList.adapter as TagsRVAdapter).setItemClickListener {
            if (it.id == model.selectedTagId.value) {
                model.selectedTagId.value = -1
            } else {
                model.selectedTagId.value = it.id
            }
        }
    }
}

class TagsRVAdapter(
    private val model: MainActivity.FolderViewModel,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var itemClickListener: (ProductTag) -> Unit = {}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tag, parent, false)
        return ProductTagViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ProductTagViewHolder).bind(model.tags.value!![position])
    }

    inner class ProductTagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tagNameView = itemView.findViewById<TextView>(R.id.tag_name)
        private val mark = itemView.findViewById<TextView>(R.id.mark)

        @SuppressLint("NotifyDataSetChanged")
        fun bind(tag: ProductTag) {
            tagNameView.text = tag.name
            if (tag.id == model.selectedTagId.value) {
                mark.visibility = View.VISIBLE
            } else {
                mark.visibility = View.INVISIBLE
            }
            itemView.setOnClickListener {
                itemClickListener.invoke(tag)
                notifyDataSetChanged()
            }
        }
    }

    fun setItemClickListener(listener: (ProductTag) -> Unit) {
        itemClickListener = listener
    }

    override fun getItemCount(): Int {
        return model.tags.value!!.size
    }
}