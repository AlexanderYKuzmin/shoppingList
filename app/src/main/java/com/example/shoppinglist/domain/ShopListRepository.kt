package com.example.shoppinglist.domain

import androidx.lifecycle.MutableLiveData

interface ShopListRepository {
    fun getShopList(): MutableLiveData<List<ShopItem>>

    fun getShopItem(shopItemId: Int): ShopItem

    fun addShopItem(shopItem: ShopItem)

    fun deleteShopItem(shopItem: ShopItem)

    fun editShopItem(shopItem: ShopItem)
}