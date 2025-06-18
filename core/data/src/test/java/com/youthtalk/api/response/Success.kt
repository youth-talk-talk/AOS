package com.youthtalk.api.response

val commonSuccessJson = """
    {
      "status": 200,
      "message": "요청에 성공하였습니다.",
      "code": "S01",
      "data": null
    }
""".trimIndent()

val postImageSuccessJson = """
    {
      "status": 200,
      "message": "요청에 성공하였습니다.",
      "code": "S01",
      "data": "https://youthtalktalk.s3.ap-northeast-2.amazonaws.com/1588076a-c11d-4078-92fd-7721106616e5-test_img.jpeg"
    }
""".trimIndent()

val commentEmptySuccessJson = """
    {
      "status": 200,
      "message": "해당하는 댓글이 없습니다.",
      "code": "S09",
      "data": null
    }
""".trimIndent()
