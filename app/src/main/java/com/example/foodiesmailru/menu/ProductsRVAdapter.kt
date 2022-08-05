package com.example.foodiesmailru.menu

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodiesmailru.MainActivity
import com.example.foodiesmailru.R
import com.example.foodiesmailru.dataclasses.Product
import com.google.android.material.button.MaterialButton

class ProductsRVAdapter(
    private val model: MainActivity.FolderViewModel,
    private val categoryId: Int,
    private val concretelyProductList: MutableList<Product> = mutableListOf<Product>()
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var itemClickListener: (Product) -> Unit = {}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductCategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (categoryId == -1) {
            (holder as ProductCategoryViewHolder).bind(concretelyProductList[position])
        } else {
            (holder as ProductCategoryViewHolder).bind(model.mapOfProducts.value!![categoryId]!![position])
        }
    }

    inner class ProductCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tagView = itemView.findViewById<ImageView>(R.id.product_tag)
        private val currentPriceView = itemView.findViewById<TextView>(R.id.current_price)
        private val oldPriceView = itemView.findViewById<TextView>(R.id.old_price)
        private val leftCurrentPriceView = itemView.findViewById<TextView>(R.id.left_current_price)
        private val weightView = itemView.findViewById<TextView>(R.id.weight)
        private val productNameView = itemView.findViewById<TextView>(R.id.name)
        private val productCountView = itemView.findViewById<TextView>(R.id.count)
        private val plusButton = itemView.findViewById<MaterialButton>(R.id.plus_button)
        private val minusButton = itemView.findViewById<MaterialButton>(R.id.minus_button)
        private val firstButton = itemView.findViewById<MaterialButton>(R.id.first_button)

        fun bind(product: Product) {
            productNameView.text = product.name
            if (product.name.length >= 12) {
                productNameView.textSize = 18F
            }
            if (product.name.length >= 16) {
                productNameView.textSize = 16F
            }
            if (product.name.length >= 20) {
                productNameView.textSize = 14F
            }
            weightView.text = product.measure.toString().plus(product.measure_unit)
            if (product.measure == 0) {
                weightView.visibility = View.INVISIBLE
            }

            productCountView.text = product.count.toString()
            currentPriceView.text = (product.price_current / 10).toString()
                .plus(itemView.context.getString(R.string.currency_symbol))
            currentPriceView.text = (product.price_current / 10).toString()
                .plus(itemView.context.getString(R.string.currency_symbol))
            leftCurrentPriceView.text = (product.price_current / 10).toString()
                .plus(itemView.context.getString(R.string.currency_symbol))
            productCountView.text = product.count.toString()
            if (product.tag_ids.isNotEmpty()) {  //TODO(Переделать отоброжение тэгов)
                if (product.tag_ids[0] == 1) tagView.setImageResource(R.drawable.ic_tag1)
                if (product.tag_ids[0] == 2) tagView.setImageResource(R.drawable.ic_tag2)
                if (product.tag_ids[0] == 3) tagView.setImageResource(R.drawable.ic_tag3)
                if (product.tag_ids[0] == 4) tagView.setImageResource(R.drawable.ic_tag4)
                if (product.tag_ids[0] == 5) tagView.setImageResource(R.drawable.ic_tag5)

            }
            if (product.price_old != null) {
                oldPriceView.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    text = (product.price_old!! / 10).toString()
                        .plus(itemView.context.getString(R.string.currency_symbol))
                }
            }
            if (product.count <= 0) {
                if (product.price_old == null) {
                    currentPriceView.visibility = View.VISIBLE
                    leftCurrentPriceView.visibility = View.INVISIBLE
                    oldPriceView.visibility = View.INVISIBLE
                } else {
                    currentPriceView.visibility = View.INVISIBLE
                    leftCurrentPriceView.visibility = View.VISIBLE
                    oldPriceView.visibility = View.VISIBLE
                }
                plusButton.visibility = View.INVISIBLE
                minusButton.visibility = View.INVISIBLE
                productCountView.visibility = View.INVISIBLE
                firstButton.visibility = View.VISIBLE
            } else {
                currentPriceView.visibility = View.INVISIBLE
                leftCurrentPriceView.visibility = View.INVISIBLE
                oldPriceView.visibility = View.INVISIBLE

                plusButton.visibility = View.VISIBLE
                minusButton.visibility = View.VISIBLE
                productCountView.visibility = View.VISIBLE
                firstButton.visibility = View.INVISIBLE
            }
            plusButton.setOnClickListener {
                model.totalPrice.value = model.totalPrice.value?.plus(product.price_current)
                if (product.count <= 0) {
                    product.count = 1
                } else {
                    product.count += 1
                }
                model.updateBasket()
                productCountView.text = product.count.toString()
                currentPriceView.visibility = View.INVISIBLE
                leftCurrentPriceView.visibility = View.INVISIBLE
                oldPriceView.visibility = View.INVISIBLE
                plusButton.visibility = View.VISIBLE
                minusButton.visibility = View.VISIBLE
                productCountView.visibility = View.VISIBLE
                firstButton.visibility = View.INVISIBLE
            }
            minusButton.setOnClickListener {
                if (product.count <= 0) {
                    product.count = 0
                    return@setOnClickListener
                } else {
                    model.totalPrice.value = model.totalPrice.value?.minus(product.price_current)
                    product.count -= 1
                    productCountView.text = product.count.toString()
                    model.updateBasket()
                }
                if (product.count == 0) {
                    if (product.price_old == null) {
                        currentPriceView.visibility = View.VISIBLE
                        leftCurrentPriceView.visibility = View.INVISIBLE
                        oldPriceView.visibility = View.INVISIBLE
                    } else {
                        currentPriceView.visibility = View.INVISIBLE
                        leftCurrentPriceView.visibility = View.VISIBLE
                        oldPriceView.visibility = View.VISIBLE
                    }
                    plusButton.visibility = View.INVISIBLE
                    minusButton.visibility = View.INVISIBLE
                    productCountView.visibility = View.INVISIBLE
                    firstButton.visibility = View.VISIBLE
                } else {
                    currentPriceView.visibility = View.INVISIBLE
                    leftCurrentPriceView.visibility = View.INVISIBLE
                    oldPriceView.visibility = View.INVISIBLE
                    plusButton.visibility = View.VISIBLE
                    minusButton.visibility = View.VISIBLE
                    productCountView.visibility = View.VISIBLE
                    firstButton.visibility = View.INVISIBLE
                }
                productCountView.text = product.count.toString()
            }
            firstButton.setOnClickListener {
                model.totalPrice.value = model.totalPrice.value?.plus(product.price_current)
                model.updateBasket()
                if (product.count == 0) {
                    product.count = 1

                    currentPriceView.visibility = View.INVISIBLE
                    leftCurrentPriceView.visibility = View.INVISIBLE
                    oldPriceView.visibility = View.INVISIBLE
                    plusButton.visibility = View.VISIBLE
                    minusButton.visibility = View.VISIBLE
                    productCountView.visibility = View.VISIBLE
                    firstButton.visibility = View.INVISIBLE
                } else {
                    product.count += 1
                }
                model.updateBasket()
                productCountView.text = product.count.toString()
            }
            productCountView.text = product.count.toString()
            itemView.setOnClickListener {
                itemClickListener.invoke(product)
                //notifyDataSetChanged()
            }
        }
    }

    fun setItemClickListener(listener: (Product) -> Unit) {
        itemClickListener = listener
    }

    override fun getItemCount(): Int {
        if (categoryId == -1) {
            return concretelyProductList.size
        } else {
            return model.mapOfProducts.value!![categoryId]!!.size
        }
    }
}