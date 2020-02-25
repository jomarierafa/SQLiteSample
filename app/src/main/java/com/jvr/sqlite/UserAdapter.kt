package com.jvr.sqlite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list.view.*


/**
 * Created by OA-JomRafa on 24/02/2020.
 */


class UserAdapter(private val items : List<User>, private val delegate: Delegate) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    interface Delegate{
        fun onItemClick(user: User)
    }

    // Gets the number of user in the list
    override fun getItemCount(): Int {
        return items.size
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false))
    }

    // Binds each user in the list to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = items[position]
        holder.name.text = user.name
        holder.age.text = user.age.toString()
        holder.layout.setOnClickListener {
            delegate.onItemClick(user)
        }

    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        // Holds the view that will add each user to
        val name  = view.tv_name
        val age = view.tv_age
        val layout = view.ll_item

    }

}