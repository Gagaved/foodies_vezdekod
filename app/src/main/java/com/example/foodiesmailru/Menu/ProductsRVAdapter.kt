package com.example.foodiesmailru.Menu

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodiesmailru.R
import com.example.foodiesmailru.dataclasses.Product
import com.google.android.material.button.MaterialButton
import org.w3c.dom.Text
import java.util.*
import kotlin.math.min

class ProductsRVAdapter(private val products: MutableList<Product>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var itemClickListener: (Int) -> Unit = {}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductCategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ProductCategoryViewHolder).bind(products[position])
    }
    inner class ProductCategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val currentPriceView = itemView.findViewById<TextView>(R.id.current_price)
        private val oldPriceView = itemView.findViewById<TextView>(R.id.old_price)
        private val leftCurrentPriceView = itemView.findViewById<TextView>(R.id.left_current_price)
        private val weightView = itemView.findViewById<TextView>(R.id.weight)
        private val productCardView = itemView.findViewById<CardView>(R.id.product_card)
        private val productNameView = itemView.findViewById<TextView>(R.id.name)
        private val productCountView = itemView.findViewById<TextView>(R.id.count)
        private val plusButton = itemView.findViewById<MaterialButton>(R.id.plus_button)
        private val minusButton= itemView.findViewById<MaterialButton>(R.id.minus_button)
        private val firstButton= itemView.findViewById<MaterialButton>(R.id.first_button)
        @SuppressLint("SetTextI18n")
        fun bind(product: Product) {
            productNameView.text = product.name
            if(product.name.length>=20) {
                productNameView.textSize = 14F
            }
            weightView.text = product.measure.toString()+product.measure_unit
            if(product.measure==0){weightView.visibility =View.INVISIBLE}
            productCountView.text = product.count.toString()
            currentPriceView.text = (product.price_current/10).toString()+'₽'
            if(product.price_old == null){
                currentPriceView.text = (product.price_current/10).toString()+'₽'
                leftCurrentPriceView.text = (product.price_current/10).toString()+'₽'
                oldPriceView.text = ""
            }else{
                currentPriceView.text = (product.price_current/10).toString()+'₽'
                leftCurrentPriceView.text = (product.price_current/10).toString()+'₽'
                oldPriceView.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    text = (product.price_old!!/10).toString()+'₽'
                }
            }
            if (product.count<=0) {
                if(product.price_old == null){
                    currentPriceView.visibility = View.VISIBLE
                    leftCurrentPriceView.visibility=View.INVISIBLE
                    oldPriceView.visibility=View.INVISIBLE
                }
                else {
                    currentPriceView.visibility = View.INVISIBLE
                    leftCurrentPriceView.visibility=View.VISIBLE
                    oldPriceView.visibility=View.VISIBLE
                }
                plusButton.visibility = View.INVISIBLE
                minusButton.visibility= View.INVISIBLE
                productCountView.visibility = View.INVISIBLE
                firstButton.visibility = View.VISIBLE
            } else {
                currentPriceView.visibility = View.INVISIBLE
                leftCurrentPriceView.visibility=View.INVISIBLE
                oldPriceView.visibility=View.INVISIBLE

                plusButton.visibility = View.VISIBLE
                minusButton.visibility= View.VISIBLE
                productCountView.visibility = View.VISIBLE
                firstButton.visibility = View.INVISIBLE
            }
            productCountView.text = product.count.toString()
            plusButton.setOnClickListener {
                if(product.count<=0){
                    product.count = 1
                }else{
                    product.count+=1
                }
                if (product.count<=0) {
                    if(product.price_old == null){
                        currentPriceView.visibility = View.VISIBLE
                        leftCurrentPriceView.visibility=View.INVISIBLE
                        oldPriceView.visibility=View.INVISIBLE
                    }
                    else {
                        currentPriceView.visibility = View.INVISIBLE
                        leftCurrentPriceView.visibility=View.VISIBLE
                        oldPriceView.visibility=View.VISIBLE
                    }
                    plusButton.visibility = View.INVISIBLE
                    minusButton.visibility= View.INVISIBLE
                    productCountView.visibility = View.INVISIBLE
                    firstButton.visibility = View.VISIBLE
                } else {
                    currentPriceView.visibility = View.INVISIBLE
                    leftCurrentPriceView.visibility=View.INVISIBLE
                    oldPriceView.visibility=View.INVISIBLE

                    plusButton.visibility = View.VISIBLE
                    minusButton.visibility= View.VISIBLE
                    productCountView.visibility = View.VISIBLE
                    firstButton.visibility = View.INVISIBLE
                }
                productCountView.text = product.count.toString()
            }
            minusButton.setOnClickListener {
                    if(product.count<=0){
                        product.count = 0
                    }else{
                        product.count-=1
                    }
                if (product.count<=0) {
                    if(product.price_old == null){
                        currentPriceView.visibility = View.VISIBLE
                        leftCurrentPriceView.visibility=View.INVISIBLE
                        oldPriceView.visibility=View.INVISIBLE
                    }
                    else {
                        currentPriceView.visibility = View.INVISIBLE
                        leftCurrentPriceView.visibility=View.VISIBLE
                        oldPriceView.visibility=View.VISIBLE
                    }
                    plusButton.visibility = View.INVISIBLE
                    minusButton.visibility= View.INVISIBLE
                    productCountView.visibility = View.INVISIBLE
                    firstButton.visibility = View.VISIBLE
                } else {
                    currentPriceView.visibility = View.INVISIBLE
                    leftCurrentPriceView.visibility=View.INVISIBLE
                    oldPriceView.visibility=View.INVISIBLE

                    plusButton.visibility = View.VISIBLE
                    minusButton.visibility= View.VISIBLE
                    productCountView.visibility = View.VISIBLE
                    firstButton.visibility = View.INVISIBLE
                }
                productCountView.text = product.count.toString()
                }
            firstButton.setOnClickListener {
                if(product.count<=0){
                    product.count = 1
                }else{
                    product.count+=1
                }
                if (product.count<=0) {
                    if(product.price_old == null){
                        currentPriceView.visibility = View.VISIBLE
                        leftCurrentPriceView.visibility=View.INVISIBLE
                        oldPriceView.visibility=View.INVISIBLE
                    }
                    else {
                        currentPriceView.visibility = View.INVISIBLE
                        leftCurrentPriceView.visibility=View.VISIBLE
                        oldPriceView.visibility=View.VISIBLE
                    }
                    plusButton.visibility = View.INVISIBLE
                    minusButton.visibility= View.INVISIBLE
                    productCountView.visibility = View.INVISIBLE
                    firstButton.visibility = View.VISIBLE
                } else {
                    currentPriceView.visibility = View.INVISIBLE
                    leftCurrentPriceView.visibility=View.INVISIBLE
                    oldPriceView.visibility=View.INVISIBLE

                    plusButton.visibility = View.VISIBLE
                    minusButton.visibility= View.VISIBLE
                    productCountView.visibility = View.VISIBLE
                    firstButton.visibility = View.INVISIBLE
                }
                productCountView.text = product.count.toString()
            }
            itemView.setOnClickListener {
            }
        }
    }
    fun setItemClickListener(listener: (Int) -> Unit) {
        itemClickListener = listener
    }
    override fun getItemCount(): Int {
        return products.size
    }
}