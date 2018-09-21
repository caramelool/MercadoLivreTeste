package caramelo.com.br.mercadolivreteste.extension

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.DecimalFormat

interface CurrentMaskListener {
    fun onValueChange(value: Float)
}

fun EditText.addCurrentMask(listener: CurrentMaskListener? = null) {
    addTextChangedListener(object : TextWatcher {

        private val formatter by lazy { DecimalFormat.getCurrencyInstance() }

        override fun afterTextChanged(s: Editable?) {
            //Do nothing
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //Do nothing
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            removeTextChangedListener(this)

            val unmask = text.toString().replace("[^0-9]".toRegex(), "")
            val value = try { unmask.toFloat() / 100 } catch (e: NumberFormatException) { 0f }

            if (value > 0f) {
                formatter.format(value).let { setText(it) }
            } else {
                setText("")
            }
            setSelection(text.length)

            listener?.onValueChange(value)

            addTextChangedListener(this)
        }
    })
    setText(text.toString())
}