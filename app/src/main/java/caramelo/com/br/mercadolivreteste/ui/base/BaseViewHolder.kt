package caramelo.com.br.mercadolivreteste.ui.base

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

abstract class BaseViewHolder(
        parent: ViewGroup,
        @LayoutRes layout: Int
): RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
                .inflate(layout, parent, false)
)