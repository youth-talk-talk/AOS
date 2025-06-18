package com.youthtalk.api.communityService.json

val patchRequestBody = """
{
    "title" : "newPostTest",
    "postType" : "review",
    "policyId" : 1,
    "contentList":[
        {"content":"글1","type":"TEXT"},
        {"content":"글3","type":"TEXT"}
    ],
    "addImgUrlList" : [],
    "deletedImgUrlList" : ["https://youthtalktalk.s3.ap-northeast-2.amazonaws.com/fc56bfec-6088-4ecb-8237-a5ec33c5f378-elmo.jpeg"]
}
""".trimIndent()

val patchSuccessJson = """
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
            "content": "글3",
            "type": "TEXT"
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

val patchEmptyContentRequestBody = """
    {
        "title" : "test",
        "postType" : "post",
        "policyId" : null,
        "contentList":null,
        "addImgUrlList" : [],
        "deletedImgUrlList" : []
    }
""".trimIndent()

val patchEmptyContentFailJson = """
    {
      "status": 400,
      "message": "유효하지 않은 값을 입력하였습니다.",
      "code": "F01",
      "data": {
        "messages": [
          "게시글 본문은 필수값입니다."
        ]
      }
    }
""".trimIndent()
