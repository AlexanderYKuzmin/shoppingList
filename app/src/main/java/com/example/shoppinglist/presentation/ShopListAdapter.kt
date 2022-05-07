package com.example.shoppinglist.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem

class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {

    var shopList: MutableList<ShopItem> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var count = 0

    // var onShopItemLongClickListener: OnShopItemLongClickListener? = null
    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        Log.d("Adapter", "on create count = ${++count}")
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
        val shopItem = shopList[position]
        var status = if (shopItem.enabled) {
            "Active"
        } else {
            "Not active"
        }

        holder.name.text = "${shopItem.name} $status"
        holder.count.text = shopItem.count.toString()
        holder.itemView.setOnLongClickListener {
            //onShopItemLongClickListener?.onShopItemLongClick(shopItem)
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }

        holder.itemView.setOnClickListener {
            Log.d("Adapter", "OnClickListener from holder")
            onShopItemClickListener?.invoke(shopItem)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (shopList[position].enabled) {
            ITEM_ENABLED
        } else {
            ITEM_DISABLED
        }
    }

    override fun getItemCount(): Int {
        return shopList.size
    }

    class ShopItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.tv_name)
        val count = view.findViewById<TextView>(R.id.tv_count)
    }

    /*interface OnShopItemLongClickListener {
        fun onShopItemLongClick(shopItem: ShopItem)
    }*/

    companion object {
        const val ITEM_ENABLED = 1
        const val ITEM_DISABLED = 0
        const val MAX_POOL_SIZE = 5
    }
}