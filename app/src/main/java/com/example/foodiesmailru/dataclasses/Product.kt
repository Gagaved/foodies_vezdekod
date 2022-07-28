package com.example.foodiesmailru.dataclasses
data class  Product(
    var id: Int,
    var category_id: Int,
    var name: String,
    var description: String,
    var image: String,
    var price_current: Int,
    var price_old: Int?,
    var measure: Int,
    var measure_unit: String,
    var energy_per_100_grams: Float,
    var proteins_per_100_grams: Float,
    var carbohydrates_per_100_grams: Float,
    var tag_ids: List<Int>,

    var count: Int = 0,
)