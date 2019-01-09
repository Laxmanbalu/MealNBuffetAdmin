package admin.mealbuffet.com.mealnbuffetadmin.model

import admin.mealbuffet.com.mealnbuffetadmin.util.Constants.EMPTY_STRING


class Category {
    var categoryName: String? = null
    var id: String? = null

    override fun toString(): String {
        return categoryName ?: EMPTY_STRING
    }
}