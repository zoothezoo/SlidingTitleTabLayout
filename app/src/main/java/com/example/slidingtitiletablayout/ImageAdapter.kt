package com.example.slidingtitiletablayout

import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

internal class ImageAdapter : GroupAdapter<GroupieViewHolder>() {
    fun update(titles: List<ImageData>) {
        update(titles.map { ImageItem(it) })
    }
}