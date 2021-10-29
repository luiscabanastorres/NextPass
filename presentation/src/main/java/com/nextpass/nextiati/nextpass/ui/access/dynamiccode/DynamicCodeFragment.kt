package com.nextpass.nextiati.nextpass.ui.access.dynamiccode

import android.app.Activity
import android.app.AlertDialog
import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.nextpass.nextiati.nextpass.FingerprintHandler
import com.nextpass.nextiati.nextpass.R
import com.nextpass.nextiati.nextpass.base.BaseFragment
import com.nextpass.nextiati.nextpass.databinding.FragmentDynamicCodeBinding
import com.nextpass.nextiati.nextpass.state.ViewModelResult
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.lang.Exception
import java.security.*
import java.security.cert.CertificateException
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey

@AndroidEntryPoint
class DynamicCodeFragment : BaseFragment<FragmentDynamicCodeBinding>(R.layout.fragment_dynamic_code) {
    private val viewModel by navGraphViewModels<DynamicCodeViewModel>(R.id.navigationAccess){defaultViewModelProviderFactory}

    var mAlertDialog: AlertDialog? = null
    var Timer = 0
    var timer: Timer? = null
    var onExecutionTask: TimerTask? = null
    var stopTask: TimerTask? = null
    private val KEY_NAME = "NEXTPASS"
    private var cipher: Cipher? = null
    private var keyStore: KeyStore? = null
    var cancelFingerPrint = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeViewModel()
    }

    private fun setupUi() {
        with(binding) {
            actionbarImgBtnBack.setOnClickListener { findNavController().popBackStack() }

            create.setOnClickListener { v: View? ->
                val keyguardManager =
                    requireContext().getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                if (keyguardManager.isKeyguardSecure) {
                    showAuthOptions(keyguardManager)
                } else {
                    ValidateAction()
                }
            }

            initTimer()
        }
    }

    private fun observeViewModel() {
        viewModel.dynamicCodeResponse.observe(viewLifecycleOwner,{
            when(it){
                is ViewModelResult.Loading->{

                }
                is ViewModelResult.Success->{
                    generateQR(it.data.encrypted_string)
                }
                is ViewModelResult.Error->{
                    binding.create.isEnabled = true
                    showMessage(it.title, it.message) { }
                }
            }
        })
    }

    private fun generateQR(encryptedString: String) {
        binding.qr.setImageResource(android.R.color.transparent)
        binding.qr2.visibility = View.VISIBLE
        timer!!.cancel()
        onExecutionTask!!.cancel()
        stopTask!!.cancel()
        try {
            Timer = 30
            val multiFormatWriter = MultiFormatWriter()
            try {
                val bitMatrix = multiFormatWriter.encode(encryptedString, BarcodeFormat.QR_CODE, 500, 500)
                val barcodeEncoder = BarcodeEncoder()
                val bitmap = barcodeEncoder.createBitmap(bitMatrix)
                binding.legend.visibility = View.VISIBLE
                binding.counter.visibility = View.VISIBLE
                binding.qr2.visibility = View.GONE
                binding.qr.setImageBitmap(bitmap)
                binding.counter.visibility = View.VISIBLE
                binding.legend.visibility = View.GONE
                binding.create.isEnabled = false
                initTimer()
                timer!!.scheduleAtFixedRate(onExecutionTask, 0, 1000)
                timer!!.schedule(stopTask, 30000)
            } catch (e: WriterException) {
                e.printStackTrace()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                createQR()
            }
        }
    }

    fun initTimer() {
        timer = Timer()
        stopTask = object : TimerTask() {
            override fun run() {
                requireActivity().runOnUiThread {
                    binding.qr.setImageResource(android.R.color.transparent)
                    binding.qr2.visibility = View.VISIBLE
                    binding.counter.visibility = View.GONE
                    binding.legend.visibility = View.VISIBLE
                    binding.create.isEnabled = true
                    timer!!.cancel()
                }
            }
        }
        onExecutionTask = object : TimerTask() {
            override fun run() {
                requireActivity().runOnUiThread {
                    binding.counter.text = String.format("%d", Timer)
                    Timer--
                }
            }
        }
    }

    private fun ValidateAction() {
        createQR()
    }

    private fun showAuthOptions(keyguardManager: KeyguardManager) {
        var showSecondaryAuthMethod = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val fingerprintManager =
                requireContext().getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager?
            if (fingerprintManager != null && fingerprintManager.isHardwareDetected && fingerprintManager.hasEnrolledFingerprints()) {
                genKey()
                if (cipherInit()) {
                    val cryptoObject: FingerprintManager.CryptoObject =
                        FingerprintManager.CryptoObject(cipher!!)
                    val helper = FingerprintHandler(requireContext())
                    helper.setListener(object : FingerprintHandler.ListenerFingerprint {
                        override fun success() {
                            if (!cancelFingerPrint) {
                                createQR()
                            }
                        }
                    })
                    helper.startAuthentication(fingerprintManager, cryptoObject, "new_qr_code")
                    val alert: View = layoutInflater.inflate(R.layout.alert_with_image, null)
                    cancelFingerPrint = false
                    mAlertDialog = AlertDialog.Builder(requireContext(), R.style.DialogTheme)
                        .setView(alert)
                        .setMessage("Por favor coloque su dedo en el lector.")
                        .setPositiveButton("Aceptar") { dialog: DialogInterface, which: Int ->
                            //helper.startAuthentication(fingerprintManager, cryptoObject, "new_qr_code");
                            cancelFingerPrint = true
                            helper.cancel()
                            dialog.dismiss()
                        }
                        .setNegativeButton("Usar contraseña") { dialog: DialogInterface?, which: Int ->
                            val authIntent =
                                keyguardManager.createConfirmDeviceCredentialIntent(
                                    "Confirmar",
                                    "Por favor ingrese su contraseña para poder continuar"
                                )
                            startActivityForResult(authIntent, 1)
                        }
                        .show()
                } else {
                    showSecondaryAuthMethod = true
                }
            } else {
                showSecondaryAuthMethod = true
            }
        } else {
            showSecondaryAuthMethod = true
        }
        if (showSecondaryAuthMethod) {
            val authIntent = keyguardManager.createConfirmDeviceCredentialIntent(
                "Confirmar",
                "Por favor ingrese su contraseña para poder continuar"
            )
            startActivityForResult(authIntent, 1)
        }
    }

    private fun cipherInit(): Boolean {
        try {
            cipher =
                Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7)
        } catch (e1: NoSuchAlgorithmException) {
            e1.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        }
        return try {
            keyStore!!.load(null)
            val key = keyStore!!.getKey(
                KEY_NAME,
                null
            ) as SecretKey
            cipher!!.init(Cipher.ENCRYPT_MODE, key)
            true
        } catch (e2: IOException) {
            e2.printStackTrace()
            false
        } catch (e2: NoSuchAlgorithmException) {
            e2.printStackTrace()
            false
        } catch (e2: CertificateException) {
            e2.printStackTrace()
            false
        } catch (e2: UnrecoverableKeyException) {
            e2.printStackTrace()
            false
        } catch (e2: KeyStoreException) {
            e2.printStackTrace()
            false
        } catch (e2: InvalidKeyException) {
            e2.printStackTrace()
            false
        }
    }

    private fun genKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore")
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        }
        var keyGenerator: KeyGenerator? = null
        try {
            keyGenerator =
                KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: NoSuchProviderException) {
            e.printStackTrace()
        }
        try {
            keyStore!!.load(null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                keyGenerator!!.init(
                    KeyGenParameterSpec.Builder(
                        KEY_NAME,
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                    )
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setUserAuthenticationRequired(true)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7).build()
                )
            }
            keyGenerator!!.generateKey()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: CertificateException) {
            e.printStackTrace()
        } catch (e: InvalidAlgorithmParameterException) {
            e.printStackTrace()
        }
    }

    fun createQR() {
        if (mAlertDialog != null) {
            mAlertDialog!!.dismiss()
        }
        viewModel.getDynamicCode()
    }
}