package com.yahdi.arkanapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yahdi.arkanapp.R
import com.yahdi.arkanapp.data.response.SurahResponse
import com.yahdi.arkanapp.databinding.RowItemSurahListBinding
import com.yahdi.arkanapp.ui.fragments.quran_activity.QuranContentsFragmentDirections
import com.yahdi.arkanapp.utils.diffutils.SurahDiffUtils

class SurahListAdapter: RecyclerView.Adapter<SurahListAdapter.MyViewHolder>() {
    private var surahList: ArrayList<SurahResponse> = arrayListOf()

    class MyViewHolder(private val binding: RowItemSurahListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: SurahResponse) {
            binding.apply {
                root.apply {
                    alpha = 0.0f
                    animate()
                        .alpha(1.0f)
                        .setDuration(200L)
                        .start()
                }
                tvListSurahTitle.text = data.name
                tvListSurahInfo.text = binding.root.context.getString(R.string.txt_surah_info).format(data.ayahsAmount.toString(), data.revelationType)
                tvListSurahArabic.text = data.nameArabic.substring(7)

                root.setOnClickListener {
                    val action = QuranContentsFragmentDirections.actionQuranContentsFragmentToSurahFragment(data)
                    root.findNavController().navigate(action)
                }
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

    fun setData(newList: List<SurahResponse>) {
        val diffUtil = SurahDiffUtils(surahList, newList)
        val result = DiffUtil.calculateDiff(diffUtil)
        surahList = ArrayList(newList)
        result.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int = surahList.size
}