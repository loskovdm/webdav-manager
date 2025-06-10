package io.github.loskovdm.webdavmanager.core.data.model

import io.github.loskovdm.webdavmanager.core.storage.webdav.model.WebDavError

sealed class NetworkErrorModel : Throwable() {
    data object ConnectionFailed : NetworkErrorModel()
    data object Timeout : NetworkErrorModel()
    data object AuthenticationFailed : NetworkErrorModel()
    data object AccessDenied : NetworkErrorModel()
    data object ResourceNotFound : NetworkErrorModel()
    data object OperationNotSupported : NetworkErrorModel()
    data object ServerError : NetworkErrorModel()
    object MissingScheme : NetworkErrorModel()
    object InvalidUrl : NetworkErrorModel()
    data object SecurityError : NetworkErrorModel()
    data class Unknown(val throwable: Throwable) : NetworkErrorModel()
}

fun WebDavError.asNetworkErrorModel(): NetworkErrorModel =
    when (this) {
        WebDavError.MissingScheme -> NetworkErrorModel.MissingScheme
        WebDavError.InvalidUrl -> NetworkErrorModel.InvalidUrl
        WebDavError.HostUnreachable -> NetworkErrorModel.ConnectionFailed
        WebDavError.Timeout -> NetworkErrorModel.Timeout
        WebDavError.Unauthorized -> NetworkErrorModel.AuthenticationFailed
        WebDavError.Forbidden -> NetworkErrorModel.AccessDenied
        WebDavError.NotFound -> NetworkErrorModel.ResourceNotFound
        WebDavError.MethodNotAllowed -> NetworkErrorModel.OperationNotSupported
        WebDavError.ServerError -> NetworkErrorModel.ServerError
        WebDavError.SSLCertInvalid,
        WebDavError.SSLHandshakeFailed -> NetworkErrorModel.SecurityError
        is WebDavError.Unknown -> NetworkErrorModel.Unknown(this.cause ?: Exception("Unknown error"))
    }
