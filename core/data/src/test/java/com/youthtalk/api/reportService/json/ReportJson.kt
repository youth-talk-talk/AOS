package com.youthtalk.api.reportService.json

val alreadyReportedResponseJson = """
    {
      "status": 409,
      "message": "이미 신고한 게시글(또는 댓글)입니다.",
      "code": "R01",
      "data": null
    }
""".trimIndent()

val myReportedResponseJson = """
    {
      "status": 400,
      "message": "본인의 게시글(또는 댓글)은 신고할 수 없습니다.",
      "code": "R02",
      "data": null
    }
""".trimIndent()
