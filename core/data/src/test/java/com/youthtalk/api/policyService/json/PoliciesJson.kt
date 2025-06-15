package com.youthtalk.api.policyService.json

val homePolicyJson = """
    {
      "status": 200,
      "message": "요청에 성공하였습니다.",
      "code": "S01",
      "data": {
        "popularPolicies": [

        ],
        "policiesWithReviews": [

        ],
        "bestPosts": [

        ]
      }
    }
""".trimIndent()

val newPolicyJson = """
    {
      "status": 200,
      "message": "요청에 성공하였습니다.",
      "code": "S01",
      "data": {
        "ALL": [

        ],
        "JOB": [

        ],
        "DWELLING": [

        ],
        "EDUCATION": [

        ],
        "LIFE": [

        ],
        "PARTICIPATION": [

        ]
      }
    }
""".trimIndent()

// policyId로 내려오는 값이 policyNum인지 확인 필요 (포스트맨에서는 string 값으로 떨어짐)
val scrapedPoliciesJson = """
    {
      "status": 200,
      "message": "요청에 성공하였습니다.",
      "code": "S01",
      "data": [
        {
          "policyId": 123,
          "category": "LIFE",
          "title": "김해시 신혼부부 전세자금 대출이자 지원 사업",
          "deadlineStatus": "마감",
          "hostDep": "김해시(공동주택과)",
          "scrap": true,
          "scrapCount":1,
          "departmentImgUrl":"",
          "region":"전국"
        }
      ]
    }
""".trimIndent()

val recentlyViewPoliciesJson = """
    {
      "status": 200,
      "message": "요청에 성공하였습니다.",
      "code": "S01",
      "data": [
        {
          "policyId": 60,
          "category": "DWELLING",
          "title": "청년과 어르신 주거공유(한지붕세대공감)",
          "deadlineStatus": "",
          "hostDep": "주택정책관",
          "scrapCount": 0,
          "departmentImgUrl": "default",
          "scrap": false,
          "region":"전국"
        },
        {
          "policyId": 55,
          "category": "JOB",
          "title": "부천 청년 사진 Dream(드림)",
          "deadlineStatus": "D-209",
          "hostDep": "부천시",
          "scrapCount": 0,
          "departmentImgUrl": "default",
          "scrap": false,
          "region":"전국"
        }
      ]
    }
""".trimIndent()
