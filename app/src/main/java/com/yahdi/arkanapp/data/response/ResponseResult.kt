package com.yahdi.arkanapp.data.response

class ResponseResult<T>(
    isSuccess: Boolean = false,
    isFailed: Boolean = false,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    response: T? = null,
) {
    var isSuccess: Boolean = isSuccess
        private set
    var isFailed: Boolean = isFailed
        private set
    var isLoading: Boolean = isLoading
        private set
    var errorMessage: String? = errorMessage
        private set
    var response: T? = response
        private set

    fun error(message: String): ResponseResult<T> {
        return this.apply {
            isSuccess = false
            isFailed = true
            isLoading = false
            errorMessage = message
            response = null
        }
    }

    fun success(result: T): ResponseResult<T> {
        return this.apply {
            isSuccess = true
            isFailed = false
            isLoading = false
            errorMessage = null
            response = result
        }
    }

    fun loading(): ResponseResult<T> {
        return this.apply {
            isSuccess = false
            isFailed = false
            isLoading = true
            errorMessage = null
            response = null
        }
    }
}