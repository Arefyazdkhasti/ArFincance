package com.example.arfinance.ui.allTransactionList

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.arfinance.R
import com.example.arfinance.data.dataModel.TransactionRecyclerHelper
import com.example.arfinance.databinding.ItemDateTransactionBinding
import com.example.arfinance.databinding.ItemTransactionBinding
import com.example.arfinance.util.enumerian.TransactionType
import com.example.arfinance.util.interfaces.OnAllTransactionsItemEventListener


class TransactionListAdapter(
    private val context: Context,
    var list: ArrayList<TransactionRecyclerHelper>,
    val onAllTransactionsItemEventListener: OnAllTransactionsItemEventListener
    ) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_DATE = 1
        const val VIEW_TYPE_TRANSACTION = 2
    }

    private inner class ViewHeaderViewHolder(
        private val binding: ItemDateTransactionBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val recyclerViewModel = list[position]
            binding.dateTextViewTransactionList.text = recyclerViewModel.transactions.date
        }
    }

    private inner class ViewTransactionViewHolder(
        private val binding: ItemTransactionBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(position: Int) {
            val recyclerViewModel = list[position]
            binding.apply {
                transactionTitle.text = recyclerViewModel.transactions.title
                transactionCategory.text = recyclerViewModel.transactions.categoryName


                Glide.with(context)
                    .load(recyclerViewModel.transactions.categoryIcon)
                    .into(transactionIcon)

                if (recyclerViewModel.transactions.type == TransactionType.Expense) {
                    transactionAmount.setTextColor(
                        ContextCompat.getColor(
                            this.root.context,
                            R.color.red
                        )
                    )
                    transactionAmount.text = "- ${recyclerViewModel.transactions.amount}"

                } else {
                    transactionAmount.setTextColor(
                        ContextCompat.getColor(
                            this.root.context,
                            R.color.green
                        )
                    )
                    transactionAmount.text = "+ ${recyclerViewModel.transactions.amount}"
                }

                itemTransactionRoot.setOnClickListener {
                    onAllTransactionsItemEventListener.onEditClickListener(recyclerViewModel.transactions)
                }
                itemTransactionRoot.setOnLongClickListener {
                    onAllTransactionsItemEventListener.onDeleteClickListener(recyclerViewModel.transactions)
                    return@setOnLongClickListener true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_DATE) {
            val binding = ItemDateTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ViewHeaderViewHolder(binding)
        } else{
            val binding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ViewTransactionViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (list[position].viewType == VIEW_TYPE_DATE) {
            (holder as ViewHeaderViewHolder).bind(position)
        } else {
            (holder as ViewTransactionViewHolder).bind(position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }
}