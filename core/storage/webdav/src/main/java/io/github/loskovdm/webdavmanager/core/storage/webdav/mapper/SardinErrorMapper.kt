package io.github.loskovdm.webdavmanager.core.storage.webdav.mapper

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpResponseException
import com.thegrizzlylabs.sardineandroid.impl.SardineException
import io.github.loskovdm.webdavmanager.core.storage.webdav.model.WebDavError
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.security.cert.CertPathValidatorException
import javax.net.ssl.SSLHandshakeException

fun Throwable.asWebDavError(): WebDavError =
    when (this) {

        is IllegalArgumentException -> {
            if (this.message?.contains("Expected URL scheme") == true) {
                WebDavError.MissingScheme
            } else {
                WebDavError.InvalidUrl
            }
        }

        is UnknownHostException -> WebDavError.HostUnreachable

        is SocketTimeoutException -> WebDavError.Timeout

        is SSLHandshakeException -> {
            if (cause is CertPathValidatorException) {
                WebDavError.SSLCertInvalid
            } else {
                WebDavError.SSLHandshakeFailed
            }
        }

        is HttpResponseException -> when (statusCode) {
            401 -> WebDavError.Unauthorized
            403 -> WebDavError.Forbidden
            404 -> WebDavError.NotFound
            405 -> WebDavError.MethodNotAllowed
            in 500..599 -> WebDavError.ServerError
            else -> WebDavError.Unknown(this)
        }

        is SardineException -> {
            val statusCode = this.statusCode
            when (statusCode) {
                401 -> WebDavError.Unauthorized
                403 -> WebDavError.Forbidden
                404 -> WebDavError.NotFound
                405 -> WebDavError.MethodNotAllowed
                in 500..599 -> WebDavError.ServerError
                else -> WebDavError.Unknown(this)
            }
        }

        else -> WebDavError.Unknown(this)
    }
