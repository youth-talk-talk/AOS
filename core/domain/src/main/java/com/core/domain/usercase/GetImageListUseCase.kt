package com.core.domain.usercase

import com.core.dataapi.repository.CommunityRepository
import javax.inject.Inject

class GetImageListUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke() = communityRepository.getListImage()
}
