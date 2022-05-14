package com.example.shoppinglist.presentation

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {
    private val LOG_TAG = "MainActivity"

    private var fragmentContainerView: FragmentContainerView? = null

    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter

    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var rvShopList: RecyclerView

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        constraintLayout = findViewById(R.id.cl)
        fragmentContainerView = findViewById(R.id.shop_item_container)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        setUpRecyclerView()

        setUpLongClickListener()

        setUpClickListener()

        setUpSwipeListener(rvShopList)

        viewModel.shopList.observe(this, Observer {
            shopListAdapter.submitList(it)
        })

        val button = findViewById<FloatingActionButton>(R.id.button_add_shop_item)
        button.setOnClickListener {
            it.backgroundTintList = ColorStateList.valueOf(
                resources.getColor(
                    R.color.purple_700,
                    null
                )
            )
            if (fragmentContainerView == null) {
                val intent = ShopItemActivity.newIntentAddItem(this)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newInstanceAddItem())
            }
        }

        button.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                v.backgroundTintList = ColorStateList.valueOf(
                    resources.getColor(
                        R.color.purple_700,
                        null
                    )
                )
            }
        }
    }

    private fun setUpRecyclerView() {
        rvShopList = findViewById<RecyclerView>(R.id.rv_shop_list)
        with(rvShopList) {
            shopListAdapter = ShopListAdapter()
            adapter = shopListAdapter
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.ITEM_ENABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.ITEM_DISABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
        }
    }

    private fun setUpSwipeListener(rvShopList: RecyclerView) {
        val swipeToDeleteCallback =
            object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    viewModel.deleteShopItem(shopListAdapter.currentList[viewHolder.adapterPosition])
                }
            }
        ItemTouchHelper(swipeToDeleteCallback).attachToRecyclerView(rvShopList)
    }

    private fun setUpClickListener() {
        shopListAdapter.onShopItemClickListener = {
            if (fragmentContainerView == null) {
                val intent = ShopItemActivity.newIntentEditItem(this, it.id)
                startActivity(intent)
            } else {
                val fragment = ShopItemFragment.newInstanceEditItem(it.id)
                launchFragment(fragment)
            }
        }
    }

    private fun setUpLongClickListener() {
        shopListAdapter.onShopItemLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }

    private fun launchFragment(fragment: ShopItemFragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .add(R.id.shop_item_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onEditingFinished() {
        Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
        supportFragmentManager.popBackStack()
    }
}

