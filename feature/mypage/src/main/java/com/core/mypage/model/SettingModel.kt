package com.core.mypage.model

sealed interface SettingModel {
    fun getSettingName(): String
}

sealed class CommunityModel(val title: String) : SettingModel {
    data class Write(val name: String) : CommunityModel(name)
    data class Scrap(val name: String) : CommunityModel(name)
    data class Like(val name: String) : CommunityModel(name)
    data class Comment(val name: String) : CommunityModel(name)

    companion object {
        fun getList(): List<CommunityModel> = listOf(
            Write("작성한 글"),
            Scrap("스크랩한 게시글"),
            Like("좋아요한 댓글"),
            Comment("내 댓글")
        )
    }

    override fun getSettingName() = title
}

sealed class ManageModel(val title: String) : SettingModel {
    data class Policy(val name: String) : ManageModel(name)
    data class Inquire(val name: String) : ManageModel(name)
    data class Etc(val name: String) : ManageModel(name)

    companion object {
        fun getList(): List<ManageModel> = listOf(
            Policy("약관 및 정책"),
            Inquire("문의하기"),
            Etc("기타 관리")
        )
    }

    override fun getSettingName() = title
}
