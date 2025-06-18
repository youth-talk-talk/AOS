package com.youthtalk.api.response

val notFoundPostsJson = """
    {
      "status": 400,
      "message": "해당 게시글을 찾을 수 없습니다.",
      "code": "PS01",
      "data": null
    }
""".trimIndent()

val forbiddenJson = """
    {
      "status": 403,
      "message": "해당 게시글에 대한 권한이 없습니다.",
      "code": "PS02",
      "data": null
    }
""".trimIndent()

val notFoundPolicyJson = """
    {
      "status": 400,
      "message": "해당 정책을 찾을 수 없습니다.",
      "code": "PC01",
      "data": null
    }
""".trimIndent()

val reportedPostsJson = """
    {
      "status": 400,
      "message": "신고한 게시글은 조회할 수 없습니다.",
      "code": "PS03",
      "data": null
    }
""".trimIndent()

val reportedUserJson = """
    {
      "status": 400,
      "message": "차단한 유저의 게시글은 조회할 수 없습니다.",
      "code": "PS04",
      "data": null
    }
""".trimIndent()

val notFoundCommentJson = """
    {
      "status": 400,
      "message": "해당 댓글을 찾을 수 없습니다.",
      "code": "C01",
      "data": null
    }
""".trimIndent()

val invalidParameterJson = """
    {
      "status": 400,
      "message": "유효하지 않은 값을 입력하였습니다.",
      "code": "F01",
      "data": null
    }
""".trimIndent()
