package com.example.shoppinglist.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem

class ShopListAdapter : ListAdapter<ShopItem, ShopItemViewHolder>(ShopItemDiffCallback()) {

    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val view = if (viewType == ITEM_ENABLED) {
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_shop_enabled,
                parent,
                false
            )
        } else {
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_shop_disabled,
                parent,
                false
            )
        }
        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = getItem(position)
        val status = if (shopItem.enabled) {
            "Active"
        } else {
            "Not active"
        }

        holder.name.text = "${shopItem.name} $status"
        holder.count.text = shopItem.count.toString()
        holder.itemView.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }

        holder.itemView.setOnClickListener {
            Log.d("Adapter", "OnClickListener from holder")
            onShopItemClickListener?.invoke(shopItem)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).enabled) {
            ITEM_ENABLED
        } else {
            ITEM_DISABLED
        }
    }

    companion object {
        const val ITEM_ENABLED = 1
        const val ITEM_DISABLED = 0
        const val MAX_POOL_SIZE = 5
    }
}