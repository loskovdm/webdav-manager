package io.github.loskovdm.webdavmanager.core.storage.webdav.util

inline fun <T> Result<T>.mapError(transform: (Throwable) -> Throwable): Result<T> =
    fold(
        onSuccess = { Result.success(it) },
        onFailure = { Result.failure(transform(it)) }
    )
