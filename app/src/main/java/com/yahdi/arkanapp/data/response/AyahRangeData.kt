package com.yahdi.arkanapp.data.response

class AyahRangeData(
    next: AyahResponse? = null,
    current: AyahResponse,
    previous: AyahResponse? = null
) {
    var next: AyahResponse? = next
        private set
    var current = current
        private set
    var previous = previous
        private set

    fun goNext() {
        next?.let {
            previous = current
            current = it
            next = null
        }
    }

    fun goPrevious() {
        previous?.let {
            next = current
            current = it
            previous = null
        }
    }
}