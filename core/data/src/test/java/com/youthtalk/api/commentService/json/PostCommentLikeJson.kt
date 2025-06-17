package com.youthtalk.api.commentService.json

val postLikeSuccessJson = """
    {
      "status": 200,
      "message": "좋아요 등록이 완료되었습니다.",
      "code": "S10",
      "data": null
    }
""".trimIndent()

val postUnLikeSuccessJson = """
    {
      "status": 200,
      "message": "좋아요 해제가 완료되었습니다.",
      "code": "S11",
      "data": null
    }
""".trimIndent()

val postLikeNoCommentIdErrorJson = """
    {
      "status": 400,
      "message": "유효하지 않은 값을 입력하였습니다.",
      "code": "F01",
      "data": {
        "messages": [
          "must not be null"
        ]
      }
    }
""".trimIndent()

val postNotFoundCommentIdErrorJson = """
    {
      "status": 400,
      "message": "해당 댓글을 찾을 수 없습니다.",
      "code": "C01",
      "data": null
    }
""".trimIndent()
