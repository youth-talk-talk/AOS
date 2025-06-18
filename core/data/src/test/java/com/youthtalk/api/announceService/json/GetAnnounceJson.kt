package com.youthtalk.api.announceService.json

val getAnnounceSuccessJson = """
    {
      "status": 200,
      "message": "요청에 성공하였습니다.",
      "code": "S01",
      "data": {
        "id": 3,
        "title": "test",
        "content": "test",
        "imageList": [
          {
            "id": 9,
            "imgUrl": "https://youthtalktalk.s3.ap-northeast-2.amazonaws.com/e5326552-618d-4456-8ec6-3329c70a7d66-elmo.jpeg"
          }
        ],
        "updateAt": "2024-09-08T19:07:25.536651"
      }
    }
""".trimIndent()

val getNoExistAnnounceErrorJson = """
    {
      "status": 404,
      "message": "해당 공지사항을 찾을 수 없습니다.",
      "code": "A01",
      "data": null
    }
""".trimIndent()

val getAllAnnounceSuccessJson = """
    {
      "status": 200,
      "message": "요청에 성공하였습니다.",
      "code": "S01",
      "data": {
        "pageNum": 0,
        "pageSize": 1,
        "totalPage": 3,
        "announcementList": [
          {
            "id": 4,
            "title": "test",
            "updateAt": "2024-09-08T19:22:15.681713"
          }
        ]
      }
    }
""".trimIndent()
