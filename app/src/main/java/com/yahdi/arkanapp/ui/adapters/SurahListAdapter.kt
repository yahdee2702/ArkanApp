package com.yahdi.arkanapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yahdi.arkanapp.databinding.RowItemSurahListBinding

class SurahListAdapter: RecyclerView.Adapter<SurahListAdapter.MyViewHolder>() {
    class MyViewHolder(val binding: RowItemSurahListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind() {
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val bind = RowItemSurahListBinding.inflate(inflater, parent, false)
                return MyViewHolder(bind)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}