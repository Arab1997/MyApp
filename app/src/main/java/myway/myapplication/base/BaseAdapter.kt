package myway.myapplication.base

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import myway.myapplication.utils.commons.ViewHolder
import myway.myapplication.utils.extensions.inflate

abstract class  BaseAdapter<T>(@LayoutRes val layoutID: Int) : RecyclerView.Adapter<ViewHolder>() {

    protected var items = arrayListOf<T>()

    open fun setData(data: ArrayList<T>) {
        items = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(layoutID))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            bindViewHolder(this, items[holder.adapterPosition])
        }
    }

    abstract fun bindViewHolder(holder: ViewHolder, data: T)

}