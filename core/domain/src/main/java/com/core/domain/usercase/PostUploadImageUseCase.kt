package com.core.domain.usercase

import com.core.dataapi.repository.CommunityRepository
import java.io.File
import javax.inject.Inject

class PostUploadImageUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
) {
    operator fun invoke(file: File) = communityRepository.postUploadImage(file)
}
