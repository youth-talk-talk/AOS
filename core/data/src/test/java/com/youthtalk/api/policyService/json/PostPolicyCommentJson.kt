package com.youthtalk.api.policyService.json

val postPolicyCommentSuccessJson = """
    {
      "status": 200,
      "message": "댓글을 성공적으로 등록했습니다.",
      "code": "S06",
      "data": {
        "commentId": 381
      }
    }
""".trimIndent()

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
