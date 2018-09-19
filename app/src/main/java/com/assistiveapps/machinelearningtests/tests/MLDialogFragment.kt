package com.assistiveapps.machinelearningtests.tests

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.assistiveapps.machinelearningtests.R
import kotlinx.android.synthetic.main.ml_dialog_fragment.*

class MLDialogFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.ml_dialog_fragment, container)
        setup(v)
        return v
    }

    fun setup(v: View) {

    }

    fun setResults(text: String) {
        progress_bar.visibility = View.GONE
        results.text = Html.fromHtml(text)
    }

    fun setHeader(header: String) {
        val builder = StringBuilder(header).append(" Results")
        header_text.text = builder.toString()
    }
}
