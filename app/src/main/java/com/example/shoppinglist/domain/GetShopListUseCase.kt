package com.example.shoppinglist.domain

import androidx.lifecycle.LiveData

class GetShopListUseCase(private val shopListRepository: ShopListRepository) {
    
    fun getShopList(): LiveData<MutableList<ShopItem>> {
        return shopListRepository.getShopList()
    }
}