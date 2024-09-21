package com.core.domain.usercase.mypage

import com.core.dataapi.repository.AnnounceRepository
import javax.inject.Inject

class GetAnnounceDetailUseCase @Inject constructor(
    private val announceRepository: AnnounceRepository,
) {
    operator fun invoke(id: Long) = announceRepository.getAnnounceDetail(id)
}
