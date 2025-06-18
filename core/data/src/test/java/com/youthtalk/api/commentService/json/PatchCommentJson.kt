package com.youthtalk.api.commentService.json

val patchSuccessJson = """
    {
      "status": 200,
      "message": "댓글을 성공적으로 수정했습니다.",
      "code": "S07",
      "data": null
    }
""".trimIndent()

val patchCommentIdEmptyErrorJson = """
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

val patchContentEmptyErrorJson = """
    {
      "status": 400,
      "message": "유효하지 않은 값을 입력하였습니다.",
      "code": "F01",
      "data": {
        "messages": [
          "must not be blank"
        ]
      }
    }
""".trimIndent()

val deleteSuccessJson = """
    {
      "status": 200,
      "message": "댓글을 성공적으로 삭제했습니다.",
      "code": "S08",
      "data": null
    }
""".trimIndent()
