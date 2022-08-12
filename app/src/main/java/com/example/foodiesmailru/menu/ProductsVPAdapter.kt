package com.example.foodiesmailru.menu
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.foodiesmailru.MainActivity


class ProductsVPAdapter(fragment: MenuFragment, model: MainActivity.FolderViewModel): FragmentStateAdapter(fragment)  {
    private val mFragments: Array<Fragment>
    val mFragmentNames: Array<String>
    init {
        val names = mutableListOf<String>()
        val fragments = mutableListOf<Fragment>()
        for(category in model.categories.value!!){
            fragments.add(MenuRvFragment(model,category.id))
            names.add(category.name)
        }
        mFragments = fragments.toTypedArray()
        mFragmentNames = names.toTypedArray()
    }
    override fun getItemCount(): Int {
        return mFragments.size
    }
    override fun createFragment(position: Int): Fragment {
        return mFragments[position]
    }
}