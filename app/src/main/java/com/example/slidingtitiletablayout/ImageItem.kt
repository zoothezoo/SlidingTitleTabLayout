package com.example.slidingtitiletablayout

import android.annotation.SuppressLint
import androidx.annotation.ColorRes
import com.example.slidingtitiletablayout.databinding.ImageItemBinding
import com.xwray.groupie.databinding.BindableItem

internal class ImageItem(
    private val data: ImageData
) : BindableItem<ImageItemBinding>() {
    @SuppressLint("ResourceAsColor")
    override fun bind(
        binding: ImageItemBinding,
        position: Int
    ) {
        with(binding) {
            textTitle.text = data.title
            textTitle.setBackgroundColor(data.color)
        }
    }

    override fun getLayout(): Int = R.layout.image_item
}

data class ImageData(
    val title: String,
    val imageUrl: String = "",
    @ColorRes val color: Int,
)