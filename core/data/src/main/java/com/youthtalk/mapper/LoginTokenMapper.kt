package com.youthtalk.mapper

import com.youthtalk.model.Token
import com.youthtalk.model.TokenResponse

fun TokenResponse.toData(): Token =
    Token(
        this.accessToken,
        this.refreshToken,
    )
