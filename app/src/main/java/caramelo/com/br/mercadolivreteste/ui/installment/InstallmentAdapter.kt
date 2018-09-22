package caramelo.com.br.mercadolivreteste.ui.installment

import caramelo.com.br.mercadolivreteste.R
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import caramelo.com.br.mercadolivreteste.model.Installment
import caramelo.com.br.mercadolivreteste.ui.base.BaseViewHolder

class InstallmentAdapter(
        installment: Installment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_INFO = 0
        private const val TYPE_INSTALLMENT = 1
        private const val TYPE_INSTALLMENT_RECOMMENDED = 2
    }

    private val data = mutableListOf<Data>().apply {
        add(Data.Info(
                installment.paymentMethodId,
                installment.issuer.name,
                installment.issuer.thumbnail
        ))

        installment.payerCosts
                .sortedByDescending { it.labels.contains("recommended_installment") }
                .forEach {
                    add(Data.Installment(
                            it.installments,
                            it.recommendedMessage,
                            it.labels.contains("recommended_installment"),
                            it.labels.contains("recommended_installment")
                    ))
                }
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val item = data[position]
        return when(item) {
            is Data.Info -> TYPE_INFO
            is Data.Installment -> {
                if (item.recommended) {
                    TYPE_INSTALLMENT_RECOMMENDED
                } else {
                    TYPE_INSTALLMENT
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TYPE_INFO -> InfoViewHolder(parent)
            TYPE_INSTALLMENT -> InstallmentViewHolder(parent)
            TYPE_INSTALLMENT_RECOMMENDED -> InstallmentRecommendedViewHolder(parent)
            else -> throw IllegalStateException()
        }
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data[position]
        when (holder) {
            is InfoViewHolder -> {
                holder.bind(item as Data.Info)
            }
            is InstallmentViewHolder -> {
                holder.bind(item as Data.Installment)
            }
            is InstallmentRecommendedViewHolder -> {
                holder.bind(item as Data.Installment)
            }
        }
    }

    sealed class Data {
        data class Info(
                val paymentMethod: String,
                val bankName: String,
                val thumbnail: String
        ) : Data()
        data class Installment(
                val count: Int,
                val message: String,
                val recommended: Boolean,
                val selected: Boolean = false
        ) : Data()
    }
}

class InfoViewHolder(
        parent: ViewGroup
): BaseViewHolder(parent, R.layout.adapter_installment_info) {
    fun bind(item: InstallmentAdapter.Data.Info) {

    }
}

class InstallmentViewHolder(
        parent: ViewGroup
): BaseViewHolder(parent, R.layout.adapter_installment) {
    fun bind(item: InstallmentAdapter.Data.Installment) {

    }
}

class InstallmentRecommendedViewHolder(
        parent: ViewGroup
): BaseViewHolder(parent, R.layout.adapter_installment_recommended) {
    fun bind(item: InstallmentAdapter.Data.Installment) {

    }
}