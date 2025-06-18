package com.youthtalk.api.commentService.json

val postEmptyContentIdJson = """
    {
      "status": 400,
      "message": "유효하지 않은 값을 입력하였습니다.",
      "code": "F01",
      "data": {
        "messages": [
          "content는 필수값입니다."
        ]
      }
    }
""".trimIndent()

val postEmptyPostsJson = """
    {
      "status": 400,
      "message": "해당 게시글을 찾을 수 없습니다.",
      "code": "PS01",
      "data": null
    }
""".trimIndent()
