package com.yahdi.arkanapp.utils

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData

class LoadingManager(lifecycleOwner: LifecycleOwner, private val loadingView: View) {
    val isLoading = MutableLiveData<Boolean>()

    init {
        isLoading.observe(lifecycleOwner) {
            when (it) {
                true -> loadingView.visibility = View.VISIBLE
                false -> loadingView.visibility = View.INVISIBLE
            }
        }
    }
}