package com.core.utils

object RandomNickname {
    fun getRandomNickname(first: List<String>, second: List<String>): String {
        return "${first.random()} ${second.random()}"
    }
}
