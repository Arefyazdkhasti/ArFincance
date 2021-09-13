package com.example.arfinance.ui.transactionList

import androidx.core.content.ContextCompat
import com.example.arfinance.R
import com.example.arfinance.data.dataModel.Transactions
import com.example.arfinance.databinding.ItemChildTransactionBinding
import com.example.arfinance.util.enumerian.CategoryType
import com.example.arfinance.util.enumerian.TransactionType
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import java.util.*

class TransactionItemRecyclerView(val transactions: Transactions) : Item<GroupieViewHolder>() {

    override fun getLayout(): Int = R.layout.item_child_transaction
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        val binding = ItemChildTransactionBinding.bind(viewHolder.itemView)

        binding.apply {
            transactionTitle.text = transactions.title
            transactionCategory.text = transactions.categoryId.toString()

            transactionAmount.text = transactions.amount.toString()

          //  transactionIcon.setImageResource(transactions.category.categoryIcon)

            if (transactions.type == TransactionType.Expense)
                transactionAmount.setTextColor(ContextCompat.getColor(this.root.context, R.color.red))
            else
                transactionAmount.setTextColor(ContextCompat.getColor(this.root.context, R.color.green))

        }
    }


}