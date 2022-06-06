package com.yahdi.arkanapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yahdi.arkanapp.data.response.AyahResponse
import com.yahdi.arkanapp.databinding.RowItemDetailSurahBinding
import com.yahdi.arkanapp.utils.Utils
import com.yahdi.arkanapp.utils.diffutils.AyahDiffUtils

class AyahListAdapter : RecyclerView.Adapter<AyahListAdapter.MyViewHolder>() {
    private var ayahList: ArrayList<AyahResponse> = arrayListOf()

    class MyViewHolder(private val binding: RowItemDetailSurahBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: AyahResponse) {
            println(data)
            binding.tvAyahArabic.text = Utils.removeBasmallah(data)
            binding.tvAyahTranslate.text = data.translation
            binding.tvAyahLatin.text = data.transliteration
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                return MyViewHolder(
                    RowItemDetailSurahBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    ),
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder.from(parent)

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(ayahList[position])
    }

    override fun getItemCount() = ayahList.size

    fun setData(newList: List<AyahResponse>) {
        val diffUtil = AyahDiffUtils(ayahList, newList)
        val result = DiffUtil.calculateDiff(diffUtil)
        ayahList = ArrayList(newList)
        result.dispatchUpdatesTo(this)
    }
}