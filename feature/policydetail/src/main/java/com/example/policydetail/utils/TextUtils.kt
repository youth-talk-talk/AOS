package com.example.policydetail.utils

object TextUtils {
    fun isNotEmptyContent(text: String): Boolean {
        return text.isNotEmpty() && text != "-" && text != "null"
    }
}
