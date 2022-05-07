package com.example.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopListRepository
import java.lang.RuntimeException
import kotlin.random.Random

object ShopListRepositoryImpl : ShopListRepository {

    private val shopListLD = MutableLiveData<MutableList<ShopItem>>()
    //private val shopList = mutableListOf<ShopItem>()
    /*private val shopList = sortedSetOf<ShopItem>(object : Comparator<ShopItem>{
        override fun compare(o1: ShopItem, o2: ShopItem): Int {
            return o1.id.compareTo(o2.id)
        }
    })*/
    private val shopList = sortedSetOf<ShopItem>({ o1, o2 -> o1.id.compareTo(o2.id) })
    private var autoIncrementId = 0

    init {
        for (i in 0..30) {
            addShopItem(ShopItem("Item_$i", 1, true))
        }
    }

    override fun getShopList(): LiveData<MutableList<ShopItem>> {
        updateList()
        return shopListLD
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return shopList.find {
            it.id == shopItemId
        } ?: throw RuntimeException("Element with id: $shopItemId does not exist")
    }

    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoIncrementId++
        }
        shopList.add(shopItem)
        updateList()
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        updateList()
    }

    override fun editShopItem(shopItem: ShopItem) {
        shopList.remove(getShopItem(shopItem.id))
        addShopItem(shopItem)
    }

    private fun updateList() {
        shopListLD.value = shopList.toMutableList()
    }
}