package com.rashel.conwayslife

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rashel.conwayslife.databinding.ItemGridBinding

class MainAdapter : RecyclerView.Adapter<MainAdapter.MyViewHolder>() {

    var items = ArrayList<Person>()
    var listener: OnItemClickListener? = null
    var isGameRunning = false

    inner class MyViewHolder(private val binding: ItemGridBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Person) {
            binding.textView.text = item.id.toString()
            if (item.isAlive) {
                binding.textView.setBackgroundColor(itemView.context.resources.getColor(R.color.black))
            } else {
                binding.textView.setBackgroundColor(itemView.context.resources.getColor(R.color.grey))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val lp = binding.textView
        lp.width = parent.measuredWidth / 10
        return MyViewHolder((binding))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        items[position].let { person ->
            holder.bind(person)
            holder.itemView.setOnClickListener {
                if (isGameRunning) return@setOnClickListener
                listener?.onItemClick(person.apply {
                    isAlive = !isAlive
                })
                notifyItemChanged(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateGameRunning(isGameRunning: Boolean) {
        this.isGameRunning = isGameRunning
    }

    fun updateData(items: ArrayList<Person>, listener: OnItemClickListener) {
        this.items = items
        this.listener = listener
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(item: Person)
    }
}