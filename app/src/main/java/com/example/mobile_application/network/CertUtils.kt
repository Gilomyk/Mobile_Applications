package com.example.mobile_application.network

import android.content.Context
import android.util.Base64
import com.example.mobile_application.R
import java.io.InputStream
import java.security.MessageDigest
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

fun Context.getPublicKeyPinFromRawCert(): String {
    val certInputStream: InputStream = resources.openRawResource(R.raw.cinemaland)
    val certFactory = CertificateFactory.getInstance("X.509")
    val cert = certFactory.generateCertificate(certInputStream) as X509Certificate
    val publicKey = cert.publicKey
    val encoded = publicKey.encoded
    val sha256 = MessageDigest.getInstance("SHA-256").digest(encoded)
    return Base64.encodeToString(sha256, Base64.NO_WRAP)
}