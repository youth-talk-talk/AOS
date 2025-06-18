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

val getTotalPostsJson = """
    {
      "status": 200,
      "message": "요청에 성공하였습니다.",
      "code": "S01",
      "data": {
        "top5Posts": [
          {
            "postId": 34,
            "title": "자유게시글",
            "writerId": 2,
            "scraps": 0,
            "scrap": false,
            "scrapCount": 0,
            "createdAt": "",
            "comments": 1,
            "policyId": null,
            "policyTitle": null,
            "contentPreview": "자유 게시글 입니다."
          }
        ],
        "allPosts": [
          {
            "postId": 37,
            "title": "newPostTest",
            "writerId": 19,
            "scraps": 0,
            "scrap": false,
            "scrapCount": 0,
            "createdAt": "",
            "comments": 1,
            "policyId": null,
            "policyTitle": null,
            "contentPreview": "글1"
          }
        ]
      }
    }
""".trimIndent()

val getTotalReviewsJson = """
    {
      "status": 200,
      "message": "요청에 성공하였습니다.",
      "code": "S01",
      "data": {
        "top5Posts": [
          {
            "postId": 35,
            "title": "후기게시글",
            "writerId": 2,
            "policyId": 1,
            "policyTitle": "전국민 마음투자 지원사업",
            "comments": 2,
            "contentPreview": "후기 게시글 입니다.",
            "scrapCount": 0,
            "scrap": false,
            "category": "LIFE",
            "createdAt": "2025-04-01 22:43:11"
          }
        ],
        "allPosts": [
          {
            "postId": 38,
            "title": "메리 크리스마스!",
            "writerId": 2,
            "policyId": 3538,
            "policyTitle": "광주광역시 상반기 공공기관 직원 통합채용",
            "comments": 0,
            "contentPreview": "상품이 정말 마음에 들어요. 품질도 좋고 사용하기 편리합니다. 정말 좋은 상품인거 같습니다...",
            "scrapCount": 0,
            "scrap": false,
            "category": "JOB",
            "createdAt": "2025-05-04 13:06:06"
          }
        ]
      }
    }
""".trimIndent()

val getSearchJson = """
    {
      "status": 200,
      "message": "요청에 성공하였습니다.",
      "code": "S01",
      "data": {
        "total": 0,
        "page": 0,
        "posts": [

        ]
      }
    }
""".trimIndent()
