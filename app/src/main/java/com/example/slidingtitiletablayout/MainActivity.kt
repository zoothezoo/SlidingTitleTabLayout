package com.example.slidingtitiletablayout

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.example.slidingtitiletablayout.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

internal class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        val imageAdapter = ImageAdapter()

        with(binding) {
            viewPager.adapter = imageAdapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = getTabTextList()[position]
            }.attach()

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                @SuppressLint("UseCompatLoadingForDrawables")
                override fun onTabSelected(tab: TabLayout.Tab) {
                    viewPager.currentItem = tab.position
                    val indicator = if (tab.position == 0) {
                        getDrawable(R.drawable.tab_layout_round_shape_yellow)
                    } else {
                        getDrawable(R.drawable.tab_layout_round_shape_purple)
                    }
                    tabLayout.setSelectedTabIndicator(indicator)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })

            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    val color = when {
                        positionOffset != 0f -> {
                            ColorUtils.blendARGB(
                                getColor(R.color.yellow),
                                getColor(R.color.purple),
                                positionOffset
                            )
                        }
                        position == 0 -> getColor(R.color.yellow)
                        position == 1 -> getColor(R.color.purple)
                        else -> getColor(R.color.yellow)
                    }
                    // ここで色の指定しない方がいい？
                    tabLayout.setSelectedTabIndicatorColor(color)
                    Log.d("onPageScrolled", "position: $position, offset: $positionOffset")
                }
            })
        }

        imageAdapter.update(
            listOf(
                ImageData(
                    title = "imageA",
                    color = R.color.yellow
                ),
                ImageData(
                    title = "imageB",
                    color = R.color.purple
                )
            )
        )
    }
}

private fun getTabTextList() =
    listOf("おはようございます", "おやすみなさい")
