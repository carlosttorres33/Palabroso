package com.carlostorres.wordsgame.home.domain.repository

import android.content.Context
import javax.inject.Inject

class CanAccessToAppUseCase @Inject constructor(
    private val repository: WordsRepository
) {

    suspend operator fun invoke(): Boolean {
        val currentVersion = repository.getCurrentVersion()
        val minAllowedVersion = repository.getMinAllowedVersion()

        for ((currentPath, minVersionPath) in currentVersion.zip(minAllowedVersion)) {
            if (currentPath != minVersionPath) {
                return currentPath > minVersionPath
            }
        }

        return true

    }

}