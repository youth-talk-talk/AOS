package com.youthtalk.api.policyService.json

val postCommentFailWithoutIdJson = """
    {
      "status": 400,
      "message": "유효하지 않은 값을 입력하였습니다.",
      "code": "F01",
      "data": {
        "messages": [
          "policyId는 필수값입니다."
        ]
      }
    }
""".trimIndent()

val postCommentFailWithoutContentJson = """
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
