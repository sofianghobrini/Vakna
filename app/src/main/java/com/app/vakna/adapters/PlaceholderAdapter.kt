package com.app.vakna.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import com.app.vakna.R
import com.app.vakna.databinding.FragmentCompagnonBinding

class PlaceholderAdapter(
    private val binding: FragmentCompagnonBinding,
    private val items: List<PlaceholderData>
) : BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val context = binding.root.context
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.placeholder_item, parent, false)

        val item = items[position]
        val messageTextView: TextView = view.findViewById(R.id.placeholder_message)
        val button: Button = view.findViewById(R.id.placeholder_button)

        messageTextView.text = item.message
        button.text = item.buttonText
        button.setOnClickListener { item.buttonAction.invoke() }

        return view
    }
}
