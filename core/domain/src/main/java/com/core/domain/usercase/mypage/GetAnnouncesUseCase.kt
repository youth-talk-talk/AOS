package com.core.domain.usercase.mypage

import com.core.dataapi.repository.AnnounceRepository
import javax.inject.Inject

class GetAnnouncesUseCase @Inject constructor(
    private val announceRepository: AnnounceRepository,
) {
    operator fun invoke() = announceRepository.getAnnounces()
}
