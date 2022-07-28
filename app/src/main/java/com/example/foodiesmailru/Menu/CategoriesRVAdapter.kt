package com.example.foodiesmailru.Menu

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodiesmailru.R
import com.example.foodiesmailru.dataclasses.ProductCategory
import java.util.*

class CategoriesRVAdapter(private val categories: List<ProductCategory>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var itemClickListener: (Int) -> Unit = {}
    var idOfSelectedCategory = categories[0].id;
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_category_item, parent, false)
        return ProductCategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ProductCategoryViewHolder).bind(categories[position])
    }
    inner class ProductCategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val productCategoryTextView = itemView.findViewById<TextView>(R.id.product_category)
        private val productCategoryCardView = itemView.findViewById<CardView>(R.id.product_category_card)
        @SuppressLint("NotifyDataSetChanged")
        fun bind(category: ProductCategory) {
            productCategoryTextView.text = category.name

            itemView.isSelected = category.id == idOfSelectedCategory
            if (itemView.isSelected) {
                productCategoryCardView.setCardBackgroundColor(Color.parseColor("#F15412"))
                productCategoryTextView.setTextColor(Color.parseColor("#FFFFFFFF"))
            } else {
                productCategoryCardView.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
                productCategoryTextView.setTextColor(Color.parseColor("#FF000000"))
            }
            itemView.setOnClickListener {
                idOfSelectedCategory = category.id
                itemClickListener.invoke(category.id)
                notifyDataSetChanged()
            }
        }
    }
    fun setItemClickListener(listener: (Int) -> Unit) {
        itemClickListener = listener
    }
    override fun getItemCount(): Int {
        return categories.size
    }
}