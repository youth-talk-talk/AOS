package com.youthtalk.api.communityService.json

val postScrapSuccessJson = """
    {   
      "status": 200,
      "message": "스크랩에 성공하였습니다.",
      "code": "S02",
      "data": null
    }
""".trimIndent()

val postScrapCancelJson = """
    {
      "status": 200,
      "message": "스크랩을 취소하였습니다.",
      "code": "S02",
      "data": null
    }
""".trimIndent()
