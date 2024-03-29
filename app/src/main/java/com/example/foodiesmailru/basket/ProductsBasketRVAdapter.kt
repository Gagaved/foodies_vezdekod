package com.example.foodiesmailru.basket

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodiesmailru.MainActivity
import com.example.foodiesmailru.R
import com.example.foodiesmailru.dataclasses.Product
import com.google.android.material.button.MaterialButton

class ProductsBasketRVAdapter(private val model: MainActivity.FolderViewModel) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var itemClickListener: (Product) -> Unit = {}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_product_basket, parent, false)
        return ProductBasketViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ProductBasketViewHolder).bind(model.basketOfProducts.value!![position])
    }

    inner class ProductBasketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val currentPriceView = itemView.findViewById<TextView>(R.id.current_price)
        private val oldPriceView = itemView.findViewById<TextView>(R.id.old_price)
        private val productNameView = itemView.findViewById<TextView>(R.id.name)
        private val productCountView = itemView.findViewById<TextView>(R.id.count)
        private val plusButton = itemView.findViewById<MaterialButton>(R.id.plus_button)
        private val minusButton = itemView.findViewById<MaterialButton>(R.id.minus_button)

        @SuppressLint("SetTextI18n", "NotifyDataSetChanged")

        fun bind(product: Product) {
            productNameView.text = product.name
            productCountView.text = product.count.toString()
            currentPriceView.text =
                (product.price_current / 10).toString() + itemView.context.getString(R.string.currency_symbol)
            if (product.price_old == null) {
                oldPriceView.visibility = View.INVISIBLE
            } else {
                oldPriceView.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    text =
                        (product.price_old!! / 10).toString() + itemView.context.getString(R.string.currency_symbol)
                }

            }
            plusButton.setOnClickListener {
                model.totalPrice.value = model.totalPrice.value!!.plus(product.price_current)
                if (product.count <= 0) {
                    product.count = 1
                } else {
                    product.count += 1
                }
                productCountView.text = product.count.toString()
            }
            minusButton.setOnClickListener {
                if (product.count <= 0) {
                    product.count = 0
                } else {
                    model.totalPrice.value = model.totalPrice.value!!.minus(product.price_current)
                    product.count -= 1
                }
                if (product.count <= 0) {
                    model.updateBasket()
                }
                productCountView.text = product.count.toString()
                notifyDataSetChanged()//TODO(убрать эту штуку)
            }

            itemView.setOnClickListener {
                itemClickListener.invoke(product)
            }
        }
    }

    fun setItemClickListener(listener: (Product) -> Unit) {
        itemClickListener = listener
    }

    override fun getItemCount(): Int {
        return model.basketOfProducts.value!!.size
    }
}