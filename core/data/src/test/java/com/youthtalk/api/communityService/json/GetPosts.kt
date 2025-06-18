package com.youthtalk.api.communityService.json

val getLeaveUserPostJson = """
    {
      "status": 200,
      "message": "요청에 성공하였습니다.",
      "code": "S01",
      "data": {
        "postId": 51,
        "postType": "post",
        "title": "newPostTest",
        "contentList": [
          {
            "content": "글1",
            "type": "TEXT"
          },
          {
            "content": "https://youthtalktalk.s3.ap-northeast-2.amazonaws.com/fc56bfec-6088-4ecb-8237-a5ec33c5f378-elmo.jpeg",
            "type": "IMAGE"
          }
        ],
        "policyId": null,
        "policyTitle": null,
        "writerId": null,
        "nickname": "null",
        "profileImage": null,
        "view": 2,
        "category": null,
        "updatedAt": "2025-05-19 12:12:38",
        "scrap": false
      }
    }
""".trimIndent()
