package com.danjdt.githubjavarepos.ui.core

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import com.danjdt.domain.exception.EmptyListException
import com.danjdt.githubjavarepos.R
import com.danjdt.githubjavarepos.extensions.isConnected
import retrofit2.HttpException

/**
 * @autor danieljdt
 * @date 2019-11-03
 */
@Suppress("DEPRECATION")
class ErrorView : FrameLayout {

    lateinit var view: View

    val errorImageView: ImageView by lazy {
        view.findViewById<ImageView>(R.id.errorImageView)
    }

    val titleTextView: TextView by lazy {
        view.findViewById<TextView>(R.id.titleTextView)
    }

    val messageTextView: TextView by lazy {
        view.findViewById<TextView>(R.id.messageTextView)
    }


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
        view = LayoutInflater.from(context).inflate(R.layout.view_error, this, true)
    }

    fun displayError(e: Exception?) {
        e?.let {
            show()
            if (!context.isConnected) {
                displayNetworkError()
                return
            }

            when (e) {
                is HttpException -> displayHttpError(e)
                is EmptyListException -> displayEmptyListError(e)
                else -> displayGenericError()
            }

        } ?: hide()
    }

    private fun show() {
        visibility = VISIBLE
    }

    private fun hide() {
        visibility = GONE
    }


    private fun displayEmptyListError(e: EmptyListException) {
        errorImageView.setImageDrawable(context.resources.getDrawable(R.drawable.ic_list))
        titleTextView.text = context.getString(R.string.ops)
        messageTextView.text = context.getString(R.string.message_empty)
    }

    private fun displayNetworkError() {
        errorImageView.setImageDrawable(context.resources.getDrawable(R.drawable.ic_wifi))
        titleTextView.text = context.getString(R.string.network_error)
        messageTextView.text = context.getString(R.string.message_network_error)
    }

    private fun displayHttpError(e: HttpException) {
        errorImageView.setImageDrawable(context.resources.getDrawable(R.drawable.ic_error))
        titleTextView.text = context.getString(R.string.error_x, e.code())
        messageTextView.text = e.message
    }

    private fun displayGenericError() {
        errorImageView.setImageDrawable(context.resources.getDrawable(R.drawable.ic_error))
        titleTextView.text = context.getString(R.string.ops)
        messageTextView.text = context.getString(R.string.message_generic_error)
    }
}
