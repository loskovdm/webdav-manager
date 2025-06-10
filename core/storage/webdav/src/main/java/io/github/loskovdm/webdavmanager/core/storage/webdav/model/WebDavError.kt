    package io.github.loskovdm.webdavmanager.core.storage.webdav.model

    sealed class WebDavError() : Throwable() {

        // Client-side input/configuration issues
        object MissingScheme : WebDavError()           // URL without http/https
        object InvalidUrl : WebDavError()              // Malformed or unsupported URL

        // Network-level issues
        object HostUnreachable : WebDavError()         // DNS resolution failed
        object Timeout : WebDavError()                 // Connection timed out

        // SSL-related issues
        object SSLCertInvalid : WebDavError()          // Untrusted certificate
        object SSLHandshakeFailed : WebDavError()      // Generic SSL handshake failure

        // HTTP errors returned by server
        object Unauthorized : WebDavError()        // HTTP 401: wrong or missing credentials
        object Forbidden : WebDavError()           // HTTP 403: access denied
        object NotFound : WebDavError()            // HTTP 404: resource not found
        object MethodNotAllowed : WebDavError()    // HTTP 405: probably wrong WebDAV path
        object ServerError : WebDavError()         // 5xx

        // Fallback for unexpected exceptions
        data class Unknown(override val cause: Throwable?) : WebDavError()

    }
