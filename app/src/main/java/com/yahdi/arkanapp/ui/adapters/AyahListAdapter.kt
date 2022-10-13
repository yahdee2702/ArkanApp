package com.yahdi.arkanapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yahdi.arkanapp.R
import com.yahdi.arkanapp.data.response.AyahResponse
import com.yahdi.arkanapp.databinding.RowItemDetailSurahBinding
import com.yahdi.arkanapp.ui.fragments.quran_activity.SurahFragmentDirections
import com.yahdi.arkanapp.utils.Utils
import com.yahdi.arkanapp.utils.diffutils.AyahDiffUtils

class AyahListAdapter : RecyclerView.Adapter<AyahListAdapter.MyViewHolder>() {
    private var ayahList: ArrayList<AyahResponse> = arrayListOf()

    class MyViewHolder(private val binding: RowItemDetailSurahBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: AyahResponse) {
            binding.apply {
                tvAyahArabic.text = binding.root.context.getString(R.string.txt_quran_content).format(Utils.removeBasmallah(data), data.idInSurah)
                tvAyahTranslate.text = data.translation
                tvAyahLatin.text = data.transliteration

                root.apply {
                    setOnLongClickListener {
                        val popupMenu = PopupMenu(it.context, binding.tvAyahArabic)
                        popupMenu.menuInflater.inflate(R.menu.ayah_menu, popupMenu.menu)
                        popupMenu.show()
                        popupMenu.setOnMenuItemClickListener { menu ->
                            when (menu.itemId) {
                                R.id.action_ayah_read_fullscreen -> {
                                    val navController = it.findNavController()
                                    navController.navigate(SurahFragmentDirections.actionSurahFragmentToAyahFragment(data))
                                    false
                                }
                                else -> {
                                    false
                                }
                            }
                        }
                        false
                    }
                }
            }
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