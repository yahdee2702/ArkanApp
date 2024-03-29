package com.yahdi.arkanapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yahdi.arkanapp.R
import com.yahdi.arkanapp.data.response.AyahResponse
import com.yahdi.arkanapp.data.response.SearchResponse
import com.yahdi.arkanapp.databinding.RowItemSearchListBinding
import com.yahdi.arkanapp.ui.fragments.quran_activity.SearchFragmentDirections
import com.yahdi.arkanapp.utils.diffutils.AyahDiffUtils

class SearchListAdapter : RecyclerView.Adapter<SearchListAdapter.MyViewHolder>() {
    private var searchedList: ArrayList<AyahResponse> = arrayListOf()

    class MyViewHolder(private val binding: RowItemSearchListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                return MyViewHolder(
                    RowItemSearchListBinding.inflate(
                        layoutInflater,
                        parent,
                        false,
                    ),
                )
            }
        }

        fun bind(data: AyahResponse) {
            binding.apply {
                root.setOnClickListener {
                    val action = SearchFragmentDirections.actionSearchFragmentToSurahFragment(data.surah!!, data.idInSurah)
                    it.findNavController().navigate(action)
                }

                tvSearchContent.text = root.context.getString(R.string.txt_search_content, data.content, data.surah!!.name, data.surah!!.id, data.idInSurah)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder = MyViewHolder.from(parent)

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(searchedList[position])
    }

    override fun getItemCount(): Int = searchedList.size

    fun setData(searched: SearchResponse) {
        val diffUtil = AyahDiffUtils(searchedList, searched.matches)
        val result = DiffUtil.calculateDiff(diffUtil)
        searchedList = ArrayList(searched.matches)
        result.dispatchUpdatesTo(this)
    }
}