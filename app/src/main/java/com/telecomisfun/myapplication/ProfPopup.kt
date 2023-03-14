package com.telecomisfun.myapplication

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity

class ProfPopup(
    private val prof: Prof,
    context: Context): Dialog(context) {

    private lateinit var profNameTextView: TextView
    private lateinit var profGradTextView: TextView
    private lateinit var profPhoneNumberTextView: TextView
    private lateinit var profEmailTextView: TextView
    private lateinit var phoneImageView: ImageView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.prof_popup)
        this.window?.setBackgroundDrawableResource(android.R.color.transparent)

        profNameTextView = findViewById(R.id.profName)
        profGradTextView = findViewById(R.id.profGrad)
        profPhoneNumberTextView = findViewById(R.id.profPhoneNumber)
        profEmailTextView = findViewById(R.id.profEmail)
        phoneImageView = findViewById(R.id.phoneImageView)

        profNameTextView.text = prof.name
        profGradTextView.text = prof.grad
        profPhoneNumberTextView.text = formatPhoneNumber(prof.phoneNumber)
        profEmailTextView.text = prof.email

        profPhoneNumberTextView.setOnLongClickListener {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("PROF_LIST", prof.phoneNumber)
            clipboard.setPrimaryClip(clip)

            Toast.makeText(context, context.getString(R.string.phone_number_has_been_copied), Toast.LENGTH_SHORT).show()
            true
        }

        profEmailTextView.setOnLongClickListener {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("PROF_LIST", prof.email)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, context.getString(R.string.email_has_been_copied), Toast.LENGTH_SHORT).show()
            true
        }

        profNameTextView.setOnLongClickListener {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("PROF_LIST", prof.name)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, context.getString(R.string.name_has_been_copied), Toast.LENGTH_SHORT).show()
            true
        }

        phoneImageView.setOnClickListener {
            try {
                when{
                    prof.phoneNumber.length == 10 -> {
                        Intent(Intent.ACTION_DIAL).also { intent ->
                            intent.data = Uri.parse("tel:${prof.phoneNumber}")
                            context.startActivity(intent)
                        }
                    }
                    prof.phoneNumber.isEmpty() || prof.phoneNumber.isBlank() -> {
                        Toast.makeText(context, context.getString(R.string.unvailable_number), Toast.LENGTH_SHORT).show()
                    }

                    else -> Toast.makeText(context, context.getString(R.string.wrong_number), Toast.LENGTH_SHORT).show()
                }
            }catch (e: Exception){
                Toast.makeText(context, context.getString(R.string.error_message, e.message), Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun formatPhoneNumber(phoneNumber: String): String{
        return if (phoneNumber.trim().length == 10){
            val first = phoneNumber.substring(0, 3)
            val second = phoneNumber.substring(3, 5)
            val third = phoneNumber.substring(5, 8)
            val last = phoneNumber.substring(8, 10)
            "$first $second $third $last"
        }else phoneNumber
    }

}