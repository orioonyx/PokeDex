/*
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Author: KyungEun Noh
 */

package com.github.orioonyx.pokedex.binding

import android.animation.ObjectAnimator
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.orioonyx.pokedex.R
import com.github.orioonyx.pokedex.utils.Event
import com.github.orioonyx.pokedex.utils.PokemonTypeUtils
import com.github.orioonyx.pokedex.utils.StatusBarUtils.setStatusBarColor
import com.google.android.material.card.MaterialCardView

object ViewBindingAdapters {

    @JvmStatic
    @BindingAdapter("gone")
    fun setGone(view: View, isGone: Boolean) {
        view.visibility = if (isGone) View.GONE else View.VISIBLE
    }

    @JvmStatic
    @BindingAdapter("toast")
    fun bindToast(view: View, event: Event<String>?) {
        event?.getContentIfNotHandled()?.let { message ->
            Toast.makeText(view.context, message, Toast.LENGTH_SHORT).show()
        }
    }

    @JvmStatic
    @BindingAdapter("paletteImage", "paletteCard")
    fun bindLoadImagePalette(view: ImageView, imageUrl: String, paletteCard: MaterialCardView) {
        Glide.with(view.context)
            .load(imageUrl)
            .listener(object : RequestListener<Drawable> {
                override fun onResourceReady(
                    resource: Drawable, model: Any, target: Target<Drawable>?,
                    dataSource: DataSource, isFirstResource: Boolean
                ): Boolean {
                    (resource as? BitmapDrawable)?.bitmap?.let { bitmap ->
                        Palette.from(bitmap).generate { palette ->
                            val color = palette?.dominantSwatch?.rgb ?: ContextCompat.getColor(view.context, R.color.gray_21)
                            paletteCard.setCardBackgroundColor(color)
                        }
                    }
                    return false
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    e?.logRootCauses("GlideLoadError")
                    return false
                }
            })
            .into(view)
    }

    @JvmStatic
    @BindingAdapter("paletteImage", "paletteView")
    fun bindLoadImagePaletteView(view: ImageView, url: String?, paletteView: View) {
        url?.let {
            Glide.with(view.context)
                .load(it)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        e?.logRootCauses("GlideLoadError")
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable, model: Any, target: Target<Drawable>?,
                        dataSource: DataSource, isFirstResource: Boolean
                    ): Boolean {
                        (resource as? BitmapDrawable)?.bitmap?.let { bitmap ->
                            Palette.from(bitmap).generate { palette ->
                                val color = palette?.dominantSwatch?.rgb ?: ContextCompat.getColor(view.context, R.color.gray_21)
                                paletteView.setBackgroundColor(color)
                                setStatusBarColor(view.context as? AppCompatActivity, color)
                            }
                        }
                        return false
                    }
                })
                .into(view)
        }
    }

    @JvmStatic
    @BindingAdapter("gifImage")
    fun bindGifImage(view: ImageView, gifUrl: String?) {
        gifUrl?.let {
            Glide.with(view.context)
                .asGif()
                .load(it)
                .into(view)
        }
    }

    @JvmStatic
    @BindingAdapter("onBackPressed")
    fun bindOnBackPressed(view: View, onBackPress: Boolean) {
        val context = view.context
        if (onBackPress && context is OnBackPressedDispatcherOwner) {
            view.setOnClickListener {
                context.onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    @JvmStatic
    @BindingAdapter("typeBackground")
    fun bindTypeBackground(cardView: MaterialCardView, typeName: String?) {
        val context = cardView.context
        val currentColor = (cardView.background as? ColorDrawable)?.color
        val colorResId = typeName?.let { PokemonTypeUtils.getTypeColor(it.lowercase()) } ?: R.color.dark
        val newColor = ContextCompat.getColor(context, colorResId)

        if (currentColor != newColor) {
            cardView.setCardBackgroundColor(newColor)
        }
    }

    @JvmStatic
    @BindingAdapter("progressWithAnimation", "animationDuration", requireAll = false)
    fun setProgressWithAnimation(progressBar: ProgressBar, progress: Int, duration: Long? = 800L) {
        ObjectAnimator.ofInt(progressBar, "progress", progress).apply {
            this.duration = duration ?: 800L
            start()
        }
    }
}
