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
