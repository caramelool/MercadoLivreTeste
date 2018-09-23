package caramelo.com.br.mercadolivreteste.extension

import android.app.Activity
import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView
import android.view.View

fun <T: View> Activity.bind(@IdRes id: Int) = lazy { findViewById<T>(id) }

fun <T: View> RecyclerView.ViewHolder.bind(@IdRes id: Int) = lazy { itemView.findViewById<T>(id) }