package com.nextpass.nextiati.nextpass

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.CancellationSignal
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat

@TargetApi(Build.VERSION_CODES.M)
@RequiresApi(api = Build.VERSION_CODES.M)
class FingerprintHandler : FingerprintManager.AuthenticationCallback {


    constructor(context: Context?) : super() {
        this.context = context
    }
    var context: Context? = null
    var action: String? = null
    lateinit var listener: ListenerFingerprint
    private var oncanccellationSignal: CancellationSignal? = null

    fun startAuthentication(
        fingerprintManager: FingerprintManager,
        cryptoObject: FingerprintManager.CryptoObject?,
        action: String?
    ) {
        this.action = action
        oncanccellationSignal = CancellationSignal()
        if (ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.USE_FINGERPRINT
            ) != PackageManager.PERMISSION_GRANTED
        ) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fingerprintManager.authenticate(cryptoObject, oncanccellationSignal, 0, this, null)
        }
    }

    override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
        super.onAuthenticationError(errorCode, errString)
        when (errorCode) {
            FingerprintManager.FINGERPRINT_ERROR_CANCELED -> if (action == "new_qr_code") {
           /*     if (!(context as CodeQrDynamic).cancelFingerPrint) {
                    Toast.makeText(
                        context,
                        "No se reconoció, intente nuevamente",
                        Toast.LENGTH_SHORT
                    ).show()
                    (context as CodeQrDynamic).startFingerPrint()
                }*/
            } else {
                Toast.makeText(context, "No se reconoció, intente nuevamente", Toast.LENGTH_SHORT)
                    .show()
            }
            else -> if (action == "new_qr_code") {
                if (errorCode == FingerprintManager.FINGERPRINT_ERROR_LOCKOUT) {
                    Toast.makeText(context, errString, Toast.LENGTH_SHORT).show()
                }
                //((CodeQrDynamic)context).closeDialog();
            }
        }
    }

    override fun onAuthenticationFailed() {
        super.onAuthenticationFailed()
        oncanccellationSignal!!.cancel()
    }

    fun cancel() {
        if (oncanccellationSignal != null && !oncanccellationSignal!!.isCanceled) {
            oncanccellationSignal!!.cancel()
        }
    }

    override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult?) {
        when (action) {
            "new_qr_code" -> {
                listener.success()
                /*if (!(context as CodeQrDynamic).cancelFingerPrint) {
                    (context as CodeQrDynamic).createQR()
                }*/
            }
        }
    }

    @JvmName("setListener1")
    fun setListener(listener: ListenerFingerprint){
        this.listener = listener
    }

    interface ListenerFingerprint{
        fun success()
    }
}