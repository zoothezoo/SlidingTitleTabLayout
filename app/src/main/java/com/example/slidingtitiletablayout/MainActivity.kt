package com.example.slidingtitiletablayout

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.viewpager2.widget.ViewPager2
import com.example.slidingtitiletablayout.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

internal class MainActivity : AppCompatActivity() {

    private val tabViews = mutableListOf<TabMultipleTextView>()
    private lateinit var customView: TabMultipleTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        val imageAdapter = ImageAdapter()

        initTabLayout(
            tabLayout = binding.tabLayout,
            viewPager = binding.viewPager,
        ) { tab, position ->
            customView = TabMultipleTextView(this)
            val (title, info) = when (position) {
                0 -> "おはようございます" to "今日も1日"
                1 -> "おやすみなさい" to "明日も1日"
                else -> "おやすみなさい" to "明日も1日"
            }
            customView.setText(info, title)
            tab.customView = customView
            tabViews.add(customView)
        }

        with(binding) {
            viewPager.adapter = imageAdapter
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
                    tabViews.forEachIndexed { index, tabView ->
                        tabView.setTextColor(
                            tabPosition = index,
                            currentPosition = position,
                            positionOffset = positionOffset
                        )
                    }
                    // tabLayout.setSelectedTabIndicatorColor(color)
                    Log.d("onPageScrolled", "position: $position, offset: $positionOffset")
                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
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

    private fun initTabLayout(
        tabLayout: TabLayout,
        viewPager: ViewPager2,
        tabConfigurationStrategy: TabLayoutMediator.TabConfigurationStrategy
    ) {
        val tabLayoutMediator = TabLayoutMediator(
            tabLayout,
            viewPager,
            tabConfigurationStrategy
        )
        ViewTreeLifecycleOwner.get(viewPager)?.lifecycle?.addObserver(
            object : DefaultLifecycleObserver {
                override fun onCreate(owner: LifecycleOwner) {
                    tabLayoutMediator.attach()
                }

                override fun onDestroy(owner: LifecycleOwner) {
                    tabLayoutMediator.detach()
                    ViewTreeLifecycleOwner.get(viewPager)?.lifecycle?.removeObserver(this)
                }
            }
        )
    }
}

private fun getTabTextList() =
    listOf("おはようございます", "おやすみなさい")
