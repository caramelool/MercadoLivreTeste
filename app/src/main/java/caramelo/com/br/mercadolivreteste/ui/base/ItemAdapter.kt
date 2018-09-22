package caramelo.com.br.mercadolivreteste.ui.base

import android.os.Parcelable
import android.support.annotation.DrawableRes
import caramelo.com.br.mercadolivreteste.R
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import caramelo.com.br.mercadolivreteste.extension.loadGif
import caramelo.com.br.mercadolivreteste.extension.loadImage
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.item_adapter.view.*

class ItemAdapter(
        @DrawableRes private val placeholder: Int,
        private val onItemSelected: (String) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ItemHolder>() {

    var data = listOf<ItemData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(group: ViewGroup, type: Int): ItemHolder {
        val view = LayoutInflater.from(group.context)
                .inflate(R.layout.item_adapter, group, false)
        return ItemHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    inner class ItemHolder(
            view: View
    ) : RecyclerView.ViewHolder(view) {

        fun bind(item: ItemData) {
            with(itemView) {
                itemText.text = item.name
                itemImage.loadImage(item.thumbnail, placeholder)
                itemRadio.isChecked = item.selected

                setOnClickListener {
                    item.selected = true
                    data.filter { it != item }.forEach { it.selected = false }
                    notifyDataSetChanged()
                    onItemSelected(item.id)
                }
            }
        }

    }
}

@Parcelize
data class ItemData(
        val id: String,
        val name: String,
        val thumbnail: String,
        var selected: Boolean = false
) : Parcelable