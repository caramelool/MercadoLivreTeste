package caramelo.com.br.mercadolivreteste.ui.installment

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import caramelo.com.br.mercadolivreteste.R
import caramelo.com.br.mercadolivreteste.extension.bind
import caramelo.com.br.mercadolivreteste.extension.loadGif
import caramelo.com.br.mercadolivreteste.model.Installment
import caramelo.com.br.mercadolivreteste.ui.base.BaseViewHolder

class InstallmentAdapter(
        private val onInstallmentsSelected: (Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_INFO = 0
        private const val TYPE_INSTALLMENT = 1
        private const val TYPE_INSTALLMENT_RECOMMENDED = 2
    }

    private val data = mutableListOf<Data>()

    fun setInstallment(installment: Installment) {
        data.apply {
            add(Data.Info(
                    installment.paymentMethodId,
                    installment.issuer.name,
                    installment.issuer.thumbnail
            ))

            installment.payerCosts
                    .sortedByDescending { it.labels.contains("recommended_installment") }
                    .forEach {
                        val isRecommended = it.labels.contains("recommended_installment")
                        add(Data.Installment(
                                it.installments,
                                it.recommendedMessage,
                                isRecommended,
                                isRecommended
                        ))
                        if (isRecommended) {
                            onInstallmentsSelected(it.installments)
                        }
                    }
        }
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val item = data[position]
        return when (item) {
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
        return when (viewType) {
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
                var selected: Boolean = false
        ) : Data()
    }

    private fun onInstallmentClick(item: InstallmentAdapter.Data.Installment) {
        data.filter { it is Data.Installment && it.selected }
                .map { it as Data.Installment }
                .forEach {
                    it.selected = false
                }
        item.selected = true
        notifyDataSetChanged()
        onInstallmentsSelected(item.count)
    }

    inner class InfoViewHolder(
            parent: ViewGroup
    ) : BaseViewHolder(parent, R.layout.adapter_installment_info) {

        private val image: ImageView by bind(R.id.adapter_installment_info_image)
        private val txtBank: TextView by bind(R.id.adapter_installment_info_txt_bank)
        private val txtMethod: TextView by bind(R.id.adapter_installment_info_txt_method)

        fun bind(item: InstallmentAdapter.Data.Info) {
            image.loadGif(item.thumbnail, R.drawable.ic_bank_placeholder)
            txtBank.text = item.bankName
            txtMethod.text = item.paymentMethod
        }
    }

    inner class InstallmentViewHolder(
            parent: ViewGroup
    ) : BaseViewHolder(parent, R.layout.adapter_installment) {

        private val txtBank: TextView by bind(R.id.adapter_installment_txt_message)
        private val txtMethod: RadioButton by bind(R.id.adapter_installment_radio)

        fun bind(item: InstallmentAdapter.Data.Installment) {
            txtBank.text = item.message
            txtMethod.isChecked = item.selected

            itemView.setOnClickListener {
                onInstallmentClick(item)
            }
        }
    }

    inner class InstallmentRecommendedViewHolder(
            parent: ViewGroup
    ) : BaseViewHolder(parent, R.layout.adapter_installment_recommended) {

        private val txtBank: TextView by bind(R.id.adapter_installment_txt_message)
        private val txtMethod: RadioButton by bind(R.id.adapter_installment_radio)

        fun bind(item: InstallmentAdapter.Data.Installment) {
            txtBank.text = item.message
            txtMethod.isChecked = item.selected

            itemView.setOnClickListener {
                onInstallmentClick(item)
            }
        }
    }
}