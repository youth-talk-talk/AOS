package com.youthtalk.api.userService.json

val nicknameAndRegionPatchJson = """
    {
      "status": 200,
      "message": "회원정보 수정을 완료하였습니다.",
      "code": "S12",
      "data": {
        "memberId": 21,
        "nickname": "닉네임1",
        "profileImgUrl": null,
        "region": "제주"
      }
    }
""".trimIndent()

val nicknamePatchJson = """
    {
      "status": 200,
      "message": "회원정보 수정을 완료하였습니다.",
      "code": "S12",
      "data": {
        "memberId": 21,
        "nickname": "닉네임2",
        "profileImgUrl": null,
        "region": "제주"
      }
    }
""".trimIndent()

val regionPatchJson = """
    {
      "status": 200,
      "message": "회원정보 수정을 완료하였습니다.",
      "code": "S12",
      "data": {
        "memberId": 21,
        "nickname": "닉네임2",
        "profileImgUrl": null,
        "region": "부산"
      }
    }
""".trimIndent()

val sizeOverNicknameJson = """
    {
      "status": 400,
      "message": "유효하지 않은 값을 입력하였습니다.",
      "code": "F01",
      "data": {
        "messages": [
          "닉네임 길이는 8자 이하입니다."
        ]
      }
    }
""".trimIndent()

val incorrectNickNameJson = """
    {
      "status": 400,
      "message": "유효하지 않은 값을 입력하였습니다.",
      "code": "F01",
      "data": {
        "messages": [
          "닉네임은 한글과 영어(대소문자) 및 숫자만 가능하며, 공백과 특수문자는 사용할 수 없습니다."
        ]
      }
    }
""".trimIndent()

val sizeOverAndIncorrectNickNameJson = """
    {
      "status": 400,
      "message": "유효하지 않은 값을 입력하였습니다.",
      "code": "F01",
      "data": {
        "messages": [
          "닉네임 길이는 1자 이상 8자 이하여야 합니다.",
          "닉네임은 한글과 영어(대소문자) 및 숫자만 가능하며, 공백과 특수문자는 사용할 수 없습니다."
        ]
      }
    }
""".trimIndent()

val nonExistRegionJson = """
    {
      "status": 400,
      "message": "유효하지 않은 값을 입력하였습니다.",
      "code": "F01",
      "data": {
        "messages": [
          "지역이 유효하지 않습니다."
        ]
      }
    }
""".trimIndent()
