package com.example.slidingtitiletablayout

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.ColorUtils

class TabMultipleTextView(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val info: TextView
    private val title: TextView

    init {
        layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val rootView = LayoutInflater.from(context).inflate(R.layout.mutiple_text_tab, this)
        info = rootView.findViewById(R.id.text_info)
        title = rootView.findViewById(R.id.text_title)
    }

    fun setText(info: CharSequence, title: CharSequence) {
        this.info.text = info
        this.title.text = title
    }

    fun setTextColor(
        tabPosition: Int,
        currentPosition: Int,
        positionOffset: Float
    ) {
        val color =
            if (tabPosition == 0) {
                when {
                    positionOffset != 0f -> {
                        ColorUtils.blendARGB(
                            context.getColor(R.color.white),
                            context.getColor(R.color.yellow),
                            positionOffset
                        )
                    }
                    currentPosition == 1 -> context.getColor(R.color.yellow)
                    else -> context.getColor(R.color.white)
                }
            } else {
                when {
                    positionOffset != 0f -> {
                        ColorUtils.blendARGB(
                            context.getColor(R.color.purple),
                            context.getColor(R.color.white),
                            positionOffset
                        )
                    }
                    currentPosition == 0 -> context.getColor(R.color.purple)
                    else -> context.getColor(R.color.white)
                }
            }
        this.info.setTextColor(color)
        this.title.setTextColor(color)
    }
}
