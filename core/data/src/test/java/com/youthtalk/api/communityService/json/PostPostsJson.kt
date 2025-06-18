package com.youthtalk.api.communityService.json

val postDataJson = """
    {
        "title" : "newPostTest",
        "postType" : "post",
        "policyId" : null,
        "contentList":[
            {"content":"글1","type":"TEXT"},
            {"content":"https://youthtalktalk.s3.ap-northeast-2.amazonaws.com/fc56bfec-6088-4ecb-8237-a5ec33c5f378-elmo.jpeg","type":"IMAGE"}
        ]
    }
""".trimIndent()

val postSuccessJson = """
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
        "writerId": 2,
        "nickname": "admin",
        "profileImage": null,
        "view": 0,
        "category": null,
        "updatedAt": "2025-05-19 12:01:15",
        "scrap": false
      }
    }
""".trimIndent()

val postReviewDataJson = """
    {
    "title" : "newPostTest",
    "postType" : "review",
    "policyId" : 1,
    "contentList":[
        {"content":"글1","type":"TEXT"},
        {"content":"https://youthtalktalk.s3.ap-northeast-2.amazonaws.com/fc56bfec-6088-4ecb-8237-a5ec33c5f378-elmo.jpeg","type":"IMAGE"}
    ]
}
""".trimIndent()

val postReviewSuccessJson = """
{
  "status": 200,
  "message": "요청에 성공하였습니다.",
  "code": "S01",
  "data": {
    "postId": 50,
    "postType": "review",
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
    "policyId": 1,
    "policyTitle": "전국민 마음투자 지원사업",
    "writerId": 2,
    "nickname": "admin",
    "profileImage": null,
    "view": 0,
    "category": "LIFE",
    "updatedAt": "2025-05-19 12:00:33",
    "scrap": false
  }
}
""".trimIndent()

val postWithoutParameterJson = """
    {
        "title" : "newPostTest",
        "postType" : "post",
        "policyId" : null,
        "contentList":[

        ]
    }
""".trimIndent()

val postErrorWithoutParameterJson = """
    {
      "status": 400,
      "message": "유효하지 않은 값을 입력하였습니다.",
      "code": "F01",
      "data": {
        "messages": [
          "Content list must contain at least one item"
        ]
      }
    }
""".trimIndent()
