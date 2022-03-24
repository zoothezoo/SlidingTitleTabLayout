package com.example.slidingtitiletablayout

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.TintAwareDrawable
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

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        val imageAdapter = ImageAdapter()
        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(getColor(R.color.yellow_light), getColor(R.color.yellow_dark))
        ).apply {
            cornerRadius = 16f
        }

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
            //tabLayout.setSelectedTabIndicator(TintDisabledDrawableWrapper(tabDrawable))
            tabLayout.setSelectedTabIndicator(TintDisabledDrawableWrapper(gradientDrawable))
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
/*
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
*/
                    val (light, dark) = when {
                        positionOffset != 0f -> {
                            val light = ColorUtils.blendARGB(
                                getColor(R.color.yellow_light),
                                getColor(R.color.purple_light),
                                positionOffset
                            )
                            val dark = ColorUtils.blendARGB(
                                getColor(R.color.yellow_dark),
                                getColor(R.color.purple_dark),
                                positionOffset
                            )
                            light to dark
                        }
                        position == 0 -> getColor(R.color.yellow_light) to getColor(R.color.yellow_dark)
                        position == 1 -> getColor(R.color.purple_light) to getColor(R.color.purple_dark)
                        else -> getColor(R.color.yellow_light) to getColor(R.color.yellow_dark)
                    }
                    // val wrappedDrawable = DrawableCompat.wrap(gradientDrawable)
                    // DrawableCompat.setTint(wrappedDrawable)
                    gradientDrawable.colors = listOf(light, dark).toIntArray()
                    // val wrappedDrawable = DrawableCompat.wrap(tabDrawable)
                    // DrawableCompat.setTint(wrappedDrawable, color)
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

@SuppressLint("RestrictedApi")
class TintDisabledDrawableWrapper(
    private val wrappedDrawable: Drawable
) : Drawable(), TintAwareDrawable {
    override fun getDirtyBounds(): Rect = wrappedDrawable.dirtyBounds
    override fun jumpToCurrentState() = wrappedDrawable.jumpToCurrentState()
    override fun draw(canvas: Canvas) = wrappedDrawable.draw(canvas)
    override fun getChangingConfigurations(): Int = wrappedDrawable.changingConfigurations
    override fun isStateful(): Boolean = wrappedDrawable.isStateful
    override fun setState(stateSet: IntArray): Boolean = wrappedDrawable.setState(stateSet)
    override fun getState(): IntArray = wrappedDrawable.state
    override fun getCurrent(): Drawable = wrappedDrawable.current
    override fun getOpacity(): Int = wrappedDrawable.opacity
    override fun getTransparentRegion(): Region? = wrappedDrawable.transparentRegion
    override fun getIntrinsicWidth(): Int = wrappedDrawable.intrinsicWidth
    override fun getIntrinsicHeight(): Int = wrappedDrawable.intrinsicHeight
    override fun getMinimumWidth(): Int = wrappedDrawable.minimumWidth
    override fun getMinimumHeight(): Int = wrappedDrawable.minimumHeight
    override fun getPadding(padding: Rect): Boolean = wrappedDrawable.getPadding(padding)
    override fun isAutoMirrored(): Boolean = wrappedDrawable.isAutoMirrored
    override fun getConstantState(): ConstantState? = null
    override fun mutate(): Drawable = wrappedDrawable.mutate()
    override fun onLevelChange(level: Int): Boolean = wrappedDrawable.setLevel(level)
    override fun setTint(tint: Int) = Unit
    override fun setTintList(tint: ColorStateList?) = Unit
    override fun setTintMode(tintMode: PorterDuff.Mode?) = Unit
    override fun setHotspot(x: Float, y: Float) {
        wrappedDrawable.setHotspot(x, y)
    }

    override fun setHotspotBounds(left: Int, top: Int, right: Int, bottom: Int) {
        wrappedDrawable.setHotspotBounds(left, top, right, bottom)
    }

    override fun getOutline(outline: Outline) {
        wrappedDrawable.getOutline(outline)
    }

    override fun onBoundsChange(bounds: Rect) {
        wrappedDrawable.bounds = bounds
    }

    override fun setChangingConfigurations(configs: Int) {
        wrappedDrawable.changingConfigurations = configs
    }

    override fun setDither(dither: Boolean) {
        wrappedDrawable.setDither(dither)
    }

    override fun setFilterBitmap(filter: Boolean) {
        wrappedDrawable.isFilterBitmap = filter
    }

    override fun setAlpha(alpha: Int) {
        wrappedDrawable.alpha = alpha
    }

    override fun setColorFilter(cf: ColorFilter?) {
        wrappedDrawable.colorFilter = cf
    }

    override fun setAutoMirrored(mirrored: Boolean) {
        wrappedDrawable.isAutoMirrored = mirrored
    }

    override fun setVisible(visible: Boolean, restart: Boolean): Boolean =
        wrappedDrawable.setVisible(visible, restart)
}
