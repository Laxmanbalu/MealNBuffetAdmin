package admin.mealbuffet.com.mealnbuffetadmin.network

interface ResponseCallback {
    fun onSuccess(data: Any? = null)
    fun onError(data: Any? = null)
}