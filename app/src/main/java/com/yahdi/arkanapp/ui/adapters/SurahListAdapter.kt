package com.yahdi.arkanapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yahdi.arkanapp.data.response.SurahResponse
import com.yahdi.arkanapp.databinding.RowItemSurahListBinding

class SurahListAdapter: RecyclerView.Adapter<SurahListAdapter.MyViewHolder>() {
    private var surahList: ArrayList<SurahResponse> = arrayListOf()

    class MyViewHolder(val binding: RowItemSurahListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: SurahResponse) {
            binding.apply {
                tvListSurahTitle.text = data.name
                tvListSurahInfo.text = "15"
            }
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val bind = RowItemSurahListBinding.inflate(inflater, parent, false)
                return MyViewHolder(bind)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder = MyViewHolder.from(parent)

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(surahList[position])
    }

    fun setData(newList: ArrayList<SurahResponse>) {
        surahList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = surahList.size
}