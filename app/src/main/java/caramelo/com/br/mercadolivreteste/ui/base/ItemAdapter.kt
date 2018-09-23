package caramelo.com.br.mercadolivreteste.ui.base

import android.os.Parcelable
import android.support.annotation.DrawableRes
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import caramelo.com.br.mercadolivreteste.R
import caramelo.com.br.mercadolivreteste.extension.bind
import caramelo.com.br.mercadolivreteste.extension.loadGif
import kotlinx.android.parcel.Parcelize

class ItemAdapter(
        @DrawableRes private val placeholder: Int,
        private val onItemSelected: (String) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ItemHolder>() {

    var data = listOf<ItemData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(parent)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    inner class ItemHolder(
            parent: ViewGroup
    ) : BaseViewHolder(parent, R.layout.adapter_item) {

        private val itemImage: ImageView by bind(R.id.adapter_item_image)
        private val itemText: TextView by bind(R.id.adapter_item_text)
        private val itemRadio: RadioButton by bind(R.id.adapter_item_radio)

        fun bind(item: ItemData) {
            itemText.text = item.name
            itemImage.loadGif(item.thumbnail, placeholder)
            itemRadio.isChecked = item.selected

            itemView.setOnClickListener {
                item.selected = true
                data.filter { it != item }.forEach { it.selected = false }
                notifyDataSetChanged()
                onItemSelected(item.id)
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