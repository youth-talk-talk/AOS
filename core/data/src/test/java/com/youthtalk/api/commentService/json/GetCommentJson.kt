package com.youthtalk.api.commentService.json

val getPolicyCommentJson = """
    {
      "status": 200,
      "message": "요청에 성공하였습니다.",
      "code": "S01",
      "data": {
        "commentCount": 2,
        "comments": [
          {
            "commentId": 88,
            "writerId": 19,
            "nickname": "elmo3356",
            "profileImg": null,
            "content": "content111",
            "isLikedByMember": false,
            "createdAt": "2025-05-03 20:31:23"
          },
          {
            "commentId": 92,
            "writerId": 18,
            "nickname": "압도적도적",
            "profileImg": null,
            "content": "댓글!!",
            "isLikedByMember": false,
            "createdAt": "2025-05-14 14:52:50"
          }
        ]
      }
    }
""".trimIndent()

val getPolicyEmptyCommentJson = """
    {
      "status": 200,
      "message": "해당하는 댓글이 없습니다.",
      "code": "S09",
      "data": null
    }
""".trimIndent()

val getPolicyLeaveUserCommentJson = """
    {
      "status": 200,
      "message": "요청에 성공하였습니다.",
      "code": "S01",
      "data": {
        "commentCount": 2,
        "comments": [
          {
            "commentId": 88,
            "writerId": 19,
            "nickname": "elmo3356",
            "profileImg": null,
            "content": "content111",
            "isLikedByMember": false,
            "createdAt": "2025-05-03 20:31:23"
          },
          {
            "commentId": 92,
            "writerId": -1,
            "nickname": "알 수 없음",
            "profileImg": null,
            "content": "댓글!!",
            "isLikedByMember": false,
            "createdAt": "2025-05-14 14:52:50"
          }
        ]
      }
    }
""".trimIndent()

val getIncorrectPolicyIdCommentJson = """
    {
      "status": 400,
      "message": "유효하지 않은 값을 입력하였습니다.",
      "code": "F01",
      "data": null
    }
""".trimIndent()
