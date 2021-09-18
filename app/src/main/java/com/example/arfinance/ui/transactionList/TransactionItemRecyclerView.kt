package com.example.arfinance.ui.transactionList

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.arfinance.R
import com.example.arfinance.data.dataModel.Transactions
import com.example.arfinance.data.local.CategoryDao
import com.example.arfinance.databinding.ItemChildTransactionBinding
import com.example.arfinance.util.enumerian.TransactionType
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TransactionItemRecyclerView(val transactions: Transactions) :
    Item<GroupieViewHolder>() {

    override fun getLayout(): Int = R.layout.item_child_transaction
    @SuppressLint("SetTextI18n")
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        val binding = ItemChildTransactionBinding.bind(viewHolder.itemView)

        binding.apply {
            transactionTitle.text = transactions.title
            transactionCategory.text = transactions.categoryName


            Glide.with(viewHolder.itemView)
                .load(transactions.categoryIcon)
                .into(transactionIcon)

            if (transactions.type == TransactionType.Expense) {
                transactionAmount.setTextColor(
                    ContextCompat.getColor(
                        this.root.context,
                        R.color.red
                    )
                )
                transactionAmount.text = "- ${transactions.amount}"

            } else {
                transactionAmount.setTextColor(
                    ContextCompat.getColor(
                        this.root.context,
                        R.color.green
                    )
                )
                transactionAmount.text = "+ ${transactions.amount}"
            }


        }
    }


}