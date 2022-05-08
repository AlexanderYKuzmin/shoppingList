package com.example.shoppinglist.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class GetShopListUseCase(private val shopListRepository: ShopListRepository) {
    
    fun getShopList(): MutableLiveData<List<ShopItem>> {
        return shopListRepository.getShopList()
    }
}