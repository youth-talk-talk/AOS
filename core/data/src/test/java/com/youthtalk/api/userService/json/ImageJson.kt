package com.youthtalk.api.userService.json

val imageJson = """
    {
      "status": 200,
      "message": "요청에 성공하였습니다.",
      "code": "S01",
      "data": "https://youthtalktalk.s3.ap-northeast-2.amazonaws.com/1588076a-c11d-4078-92fd-7721106616e5-test_img.jpeg"
    }
""".trimIndent()

val sizeOverImageJson = """
    {
      "status": 400,
      "message": "프로필 이미지는 최대 5MB까지 업로드할 수 있습니다.",
      "code": "M12",
      "data": null
    }
""".trimIndent()
