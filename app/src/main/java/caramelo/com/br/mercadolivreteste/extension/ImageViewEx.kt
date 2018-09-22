package caramelo.com.br.mercadolivreteste.extension

import android.support.annotation.DrawableRes
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.loadImage(
        url: String,
        @DrawableRes placeholder: Int) {
    Glide.with(this)
            .load(url)
            .apply(RequestOptions()
                    .override(54, 54)
                    .placeholder(placeholder)
                    .error(placeholder))
            .into(this)
}

fun ImageView.loadGif(
        url: String,
        @DrawableRes placeholder: Int) {
    Glide.with(this)
            .asGif()
            .load(url)
            .apply(RequestOptions()
                    .override(54, 54)
                    .placeholder(placeholder)
                    .error(placeholder))
            .into(this)
}