package com.danjdt.githubjavarepos.ui.core

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.danjdt.githubjavarepos.R
import com.danjdt.githubjavarepos.extensions.isConnected
import kotlinx.android.synthetic.main.view_error.view.*
import retrofit2.HttpException

/**
 * @autor danieljdt
 * @date 2019-11-03
 */
@Suppress("DEPRECATION")
class ErrorView : FrameLayout {

    constructor(context: Context) : super(context) {
        setup()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setup()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        setup()
    }

    private fun setup() {
        LayoutInflater.from(context).inflate(R.layout.view_error, this, false)
    }

    fun displayError(e: Exception) {
        if (!context.isConnected) {
            displayNetworkError()
            return
        }

        if (e is HttpException) {
            displayHttpError(e)
        } else {
            displayGenericError()
        }
    }

    private fun displayNetworkError() {
        errorImageView.setImageDrawable(context.resources.getDrawable(R.drawable.ic_wifi))
        titleTextView.text = context.getString(R.string.network_error)
        messageTextView.text = context.getString(R.string.message_network_error)
    }

    private fun displayHttpError(e : HttpException) {
        errorImageView.setImageDrawable(context.resources.getDrawable(R.drawable.ic_error))
        titleTextView.text = context.getString(R.string.error_x, e.code())
        messageTextView.text = e.message
    }

    private fun displayGenericError() {
        errorImageView.setImageDrawable(context.resources.getDrawable(R.drawable.ic_error))
        titleTextView.text = context.getString(R.string.error)
        messageTextView.text = context.getString(R.string.message_generic_error)
    }
}
