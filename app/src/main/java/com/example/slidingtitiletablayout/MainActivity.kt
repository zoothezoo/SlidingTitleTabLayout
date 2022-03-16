package com.example.slidingtitiletablayout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.slidingtitiletablayout.databinding.ActivityMainBinding
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
