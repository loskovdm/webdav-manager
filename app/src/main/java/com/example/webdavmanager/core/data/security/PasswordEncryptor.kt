package com.example.webdavmanager.core.data.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class PasswordEncryptor {
    companion object {
        private const val KEY_ALIAS = "password_encryption_key"
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val TRANSFORMATION = "${KeyProperties.KEY_ALGORITHM_AES}/${KeyProperties.BLOCK_MODE_GCM}/${KeyProperties.ENCRYPTION_PADDING_NONE}"
        private const val IV_SEPARATOR = ":"
    }

    private val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply {
        load(null)
    }

    private fun getOrCreateKey(): SecretKey {
        if (!keyStore.containsAlias(KEY_ALIAS)) {
            val keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                ANDROID_KEYSTORE
            )
            keyGenerator.init(
                KeyGenParameterSpec.Builder(
                    KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setRandomizedEncryptionRequired(true)
                    .build()
            )
            keyGenerator.generateKey()
        }
        return keyStore.getKey(KEY_ALIAS, null) as SecretKey
    }

    fun encrypt(plaintext: String): String {
        var cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateKey())
        val iv = cipher.iv
        val encryptedBytes = cipher.doFinal(plaintext.toByteArray(Charsets.UTF_8))

        val ivString = Base64.encodeToString(iv, Base64.DEFAULT)
        val encryptedString = Base64.encodeToString(encryptedBytes, Base64.DEFAULT)

        return "$ivString$IV_SEPARATOR$encryptedString"
    }

    fun decrypt(encryptedText: String): String {
        val parts = encryptedText.split(IV_SEPARATOR)
        if (parts.size != 2) return ""

        val iv = Base64.decode(parts[0], Base64.DEFAULT)
        val encryptedBytes = Base64.decode(parts[1], Base64.DEFAULT)

        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, getOrCreateKey(), GCMParameterSpec(128, iv))

        return String(cipher.doFinal(encryptedBytes), Charsets.UTF_8)
    }
}