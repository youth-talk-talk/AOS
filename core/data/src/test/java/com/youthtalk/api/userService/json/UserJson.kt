package com.youthtalk.api.userService.json

val userInfoJson = """
    {
      "status": 200,
      "message": "요청에 성공하였습니다.",
      "code": "S01",
      "data": {
        "memberId": 21,
        "nickname": "user222",
        "profileImgUrl": null,
        "region": "서울"
      }
    }
""".trimIndent()

val myCommentInfo = """
    {
      "status": 200,
      "message": "요청에 성공하였습니다.",
      "code": "S01",
      "data": {
        "commentCount": 2,
        "comments": [
          {
            "commentId": 91,
            "content": "리뷰댓글222",
            "articleId": 35,
            "articleType": "review",
            "articleTitle": "후기게시글",
            "isLikedByMember": false,
            "likeCount": 0
          },
          {
            "commentId": 90,
            "content": "리뷰댓글111",
            "articleId": 35,
            "articleType": "review",
            "articleTitle": "후기게시글",
            "isLikedByMember": false,
            "likeCount": 0
          }
        ]
      }
    }
""".trimIndent()
