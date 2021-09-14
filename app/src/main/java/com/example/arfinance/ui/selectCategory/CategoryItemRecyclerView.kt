package com.example.arfinance.ui.selectCategory

import com.bumptech.glide.Glide
import com.example.arfinance.R
import com.example.arfinance.data.dataModel.Category
import com.example.arfinance.databinding.ItemCategoryBinding
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class CategoryItemRecyclerView(val category: Category) : Item<GroupieViewHolder>() {

    override fun getLayout(): Int = R.layout.item_category
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        val binding = ItemCategoryBinding.bind(viewHolder.itemView)

        binding.apply {
            categoryTitle.text = category.categoryName.name

            Glide.with(viewHolder.itemView)
                .load(category.categoryIcon)
                .into(categoryIcon)

        }
    }


}