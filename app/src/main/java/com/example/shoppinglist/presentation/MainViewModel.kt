package com.example.shoppinglist.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.shoppinglist.data.ShopListRepositoryImpl
import com.example.shoppinglist.domain.DeleteShopItemUseCase
import com.example.shoppinglist.domain.EditShopItemUseCase
import com.example.shoppinglist.domain.GetShopListUseCase
import com.example.shoppinglist.domain.ShopItem

class MainViewModel: ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)

    val shopList = getShopListUseCase.getShopList()

    /*fun getShopList() {
        getShopListUseCase.getShopList()
    }*/

    fun deleteShopItem(shopItem: ShopItem) {
        deleteShopItemUseCase.deleteShopItem(shopItem)
    }

    fun changeEnableState(shopItem: ShopItem) {
        Log.d("MainViewModel", "Enable state change")
        val newItem = shopItem.copy(enabled = !shopItem.enabled)

        Log.d("MainViewModel", "new item enable = ${newItem.enabled}")
        editShopItemUseCase.editShopItem(newItem)
    }
}