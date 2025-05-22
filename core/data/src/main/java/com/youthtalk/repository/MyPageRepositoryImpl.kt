package com.youthtalk.repository

import com.core.dataapi.repository.MyPageRepository
import com.youthtalk.data.CommunityService
import com.youthtalk.data.PolicyService
import com.youthtalk.data.UserService
import com.youthtalk.datasource.room.YouthDatabase
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(
    private val policyService: PolicyService,
    private val communityService: CommunityService,
    private val userService: UserService,
    private val appDatabase: YouthDatabase
) : MyPageRepository
