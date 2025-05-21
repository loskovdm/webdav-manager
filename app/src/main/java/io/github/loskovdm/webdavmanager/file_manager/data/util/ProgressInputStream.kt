package io.github.loskovdm.webdavmanager.file_manager.data.util

import java.io.InputStream

class ProgressInputStream(
    private val originalStream: InputStream,
    private val totalBytes: Long,
    private val onProgressChanged: (bytesRead: Long, totalBytes: Long) -> Unit
) : InputStream() {
    private var bytesRead: Long = 0

    override fun read(): Int {
        val result = originalStream.read()
        if (result != -1) {
            bytesRead ++
            onProgressChanged(bytesRead, totalBytes)
        }
        return result
    }

    override fun read(b: ByteArray): Int {
        return read(b, 0, b.size)
    }

    override fun read(b: ByteArray, off: Int, len: Int): Int {
        val result = originalStream.read(b, off, len)
        if (result != -1) {
            bytesRead += result
            onProgressChanged(bytesRead, totalBytes)
        }
        return result
    }

    override fun available() = originalStream.available()
    override fun close() = originalStream.close()
    override fun mark(readlimit: Int) = originalStream.mark(readlimit)
    override fun reset() = originalStream.reset()
    override fun markSupported() = originalStream.markSupported()

}