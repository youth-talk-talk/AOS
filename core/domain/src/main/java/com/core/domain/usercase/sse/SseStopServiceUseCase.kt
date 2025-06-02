package com.core.domain.usercase.sse

import com.core.dataapi.repository.SseRepository
import javax.inject.Inject

class SseStopServiceUseCase @Inject constructor(
    private val sseRepository: SseRepository
) {
    operator fun invoke() = sseRepository.stopSseService()
}
