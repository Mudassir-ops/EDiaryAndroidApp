package com.example.neweasydairy.utilis

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.easydiaryandjournalwithlock.R
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import com.google.android.flexbox.FlexboxLayout
import androidx.core.content.edit

private var toast: Toast? = null
fun Activity.toast(message: String) {
    try {
        if (this.isDestroyed || this.isFinishing) return
        if (toast != null) {
            toast?.cancel()
        }
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        if (this.isDestroyed || this.isFinishing) return
        toast?.show()

    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Activity.shareApp() {
    try {

        getSharedPreferences("AppPrefs", MODE_PRIVATE).edit() {
            putBoolean("skipPinOnce", true)
        }

        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.type = "text/plain"
        sendIntent.putExtra(
            Intent.EXTRA_SUBJECT, "ChargingAnimation"
        )
        var shareMessage = "\n Let me recommend you this application\n\n"
        shareMessage = """
             ${shareMessage}https://play.google.com/store/apps/details?id=${this.packageName}
        """.trimIndent()
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        this.startActivity(Intent.createChooser(sendIntent, "Choose one"))
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        this.toast("No Launcher")
    }
}

fun Context.feedBackWithEmail(title: String, message: String, emailId: String) {
    try {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        emailIntent.data = Uri.parse("mailto:")
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailId))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, title)
        emailIntent.putExtra(Intent.EXTRA_TEXT, message)
        this.startActivity(emailIntent)

    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
}

fun Activity.privacyPolicyUrl() {
    try {
        this.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(this.getString(R.string.privacy_policy_link))
            )
        )
    } catch (e: Exception) {
        e.printStackTrace()
        toast(this.getString(R.string.no_launcher))

    }
}

fun Activity.moreApps() {
    try {
        this.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(this.getString(R.string.more_app_link))
            )
        )
    } catch (e: Exception) {
        e.printStackTrace()
        toast(this.getString(R.string.no_launcher))

    }
}

fun Activity.rateUs() {
    try {
        this.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=${this.packageName}")
            )
        )

    } catch (e: Exception) {
        e.printStackTrace()
        toast("No Launcher")
    }
}

fun currentDateAndTime(context: Context, textView: TextView) {
    val currentDate = Date()
    val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    val timeFormat = SimpleDateFormat("hh:mm,a", Locale.getDefault())
    val formattedDate = dateFormat.format(currentDate)
    val formattedTime = timeFormat.format(currentDate)
    val dateTimeString = "$formattedTime\n$formattedDate"
    textView.text = dateTimeString
}

@Suppress("DEPRECATION")
fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
    val runningServices = activityManager?.getRunningServices(Integer.MAX_VALUE)

    if (runningServices != null) {
        for (service in runningServices) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
    }
    return false
}


fun AppCompatImageView.setSelectedAlpha(selected: Boolean) {
    this.alpha = if (selected) 1f else 0.4f
}

fun List<AppCompatImageView>.setSelectedItem(selectedItem: AppCompatImageView) {
    for (item in this) {
        item.setSelectedAlpha(item == selectedItem)
    }
}

@SuppressLint("DefaultLocale")
fun getAudioDuration(audioPath: String): String {
    val retriever = MediaMetadataRetriever()
    try {
        retriever.setDataSource(audioPath)
        val durationString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        val durationMs = durationString?.toLongOrNull() ?: 0L

        val minutes = TimeUnit.MILLISECONDS.toMinutes(durationMs)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(durationMs) % 60
        return String.format("%02d:%02d", minutes, seconds)
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        retriever.release()
    }
    return "00:00"
}

fun Bitmap.saveToExternalStorage(context: Context, fileName: String): Uri? {
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "$fileName.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        put(MediaStore.Images.Media.IS_PENDING, 1)
    }

    val uri: Uri? =
        context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

    uri?.let {
        try {
            context.contentResolver.openOutputStream(it)?.use { outputStream ->
                this.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
            contentValues.clear()
            contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
            context.contentResolver.update(it, contentValues, null, null)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    return uri
}

fun String.deleteImageFile(tag: String): Boolean {
    val file = File(this)
    return if (file.exists()) {
        file.delete().also { deleted ->
            if (!deleted) {
                Log.e(tag, "Error occurred while deleting the file")
            }
        }
    } else {
        Log.e(tag, "File does not exist")
        false
    }
}

fun Context.deleteAudioFile(audioPath: String) {
    val file = File(audioPath)
    if (file.exists()) {
        file.delete()
    }
}

fun Context?.formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

fun Context?.monthlyFormatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("d MMMM, yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

fun ImageView.loadImageFromResources(resourceId: Int) {
    this.setImageResource(resourceId)
}

fun Context.saveImageToSpecificFolder(uri: Uri, folderName: String, fileName: String): String? {
    return try {
        val directory = File(filesDir, folderName)
        if (!directory.exists()) directory.mkdirs()
        val file = File(directory, fileName)
        val inputStream: InputStream = contentResolver.openInputStream(uri) ?: return null
        FileOutputStream(file).use { outputStream ->
            inputStream.use { input ->
                input.copyTo(outputStream)
            }
        }
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


fun Fragment.showDatePickerWithTime(
    calendar: Calendar,
    onDateTimeSelected: (String, String) -> Unit
) {
    val contextThemeWrapper = ContextThemeWrapper(requireContext(), R.style.TimePickerDialogTheme)

    DatePickerDialog(
        contextThemeWrapper,
        { _, year, monthOfYear, dayOfMonth ->
            calendar.set(year, monthOfYear, dayOfMonth)
            val formattedDate = calendar.time.toFormattedString("dd/MM/yyyy")
            showTimePicker(calendar) { formattedTime ->
                onDateTimeSelected(formattedDate, formattedTime)
            }
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}

fun Fragment.showTimePicker(calendar: Calendar, onTimeSelected: (String) -> Unit) {
    val contextThemeWrapper = ContextThemeWrapper(requireContext(), R.style.TimePickerDialogTheme)

    TimePickerDialog(
        contextThemeWrapper,
        { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            val formattedTime = calendar.time.toFormattedString("hh:mm a")
            onTimeSelected(formattedTime)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        false
    ).show()
}

fun Date.toFormattedString(pattern: String, locale: Locale = Locale.getDefault()): String {
    val dateFormat = SimpleDateFormat(pattern, locale)
    return dateFormat.format(this)
}

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun FlexboxLayout.addTags(
    tagList: MutableList<String>,
    onTagClick: ((String) -> Unit)? = null
) {
    if (tagList.isEmpty()) {
        this.visibility = View.GONE
        return
    } else {
        this.visibility = View.VISIBLE
    }
    this.removeAllViews()
    for (tag in tagList) {
        val tagContainer = LinearLayout(this.context).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(8, 8, 8, 8)
            setBackgroundResource(R.drawable.bg_tag)
            layoutParams = FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                marginEnd = 8
                bottomMargin = 8
            }
            gravity = Gravity.CENTER_VERTICAL
        }

        val hashIcon = ImageView(this.context).apply {
            setImageResource(R.drawable.ic_hash_small)
            setPadding(8, 8, 4, 8)
        }

        val tagText = TextView(this.context).apply {
            text = tag
            setPadding(0, 8, 8, 8)
            setTextColor(ContextCompat.getColor(context, R.color.black))
            textSize = 14f
            setOnClickListener {
                onTagClick?.invoke(tag)
            }
        }

        val closeIcon = ImageView(this.context).apply {
            setImageResource(R.drawable.ic_close)
            setPadding(8, 12, 12, 8)
            setOnClickListener {
                this@addTags.removeView(tagContainer)
                tagList.remove(tag)
                if (tagList.isEmpty()) {
                    this@addTags.visibility = View.GONE
                }
            }
        }

        tagContainer.addView(hashIcon)
        tagContainer.addView(tagText)
        tagContainer.addView(closeIcon)

        this.addView(tagContainer)
    }
}

fun Long.formatTimestampForDisplay(): String {
    val timestamp = this
    val now = Calendar.getInstance()
    val dateToCheck = Calendar.getInstance().apply { timeInMillis = timestamp }

    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    val dateFormat = SimpleDateFormat("dd MMM, h:mm a", Locale.getDefault())

    val isSameDay = now.get(Calendar.YEAR) == dateToCheck.get(Calendar.YEAR) &&
            now.get(Calendar.DAY_OF_YEAR) == dateToCheck.get(Calendar.DAY_OF_YEAR)

    val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
    val isYesterday = yesterday.get(Calendar.YEAR) == dateToCheck.get(Calendar.YEAR) &&
            yesterday.get(Calendar.DAY_OF_YEAR) == dateToCheck.get(Calendar.DAY_OF_YEAR)

    val tomorrow = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }
    val isTomorrow = tomorrow.get(Calendar.YEAR) == dateToCheck.get(Calendar.YEAR) &&
            tomorrow.get(Calendar.DAY_OF_YEAR) == dateToCheck.get(Calendar.DAY_OF_YEAR)

    return when {
        isSameDay -> "Today, ${timeFormat.format(dateToCheck.time)}"
        isYesterday -> "Yesterday, ${timeFormat.format(dateToCheck.time)}"
        isTomorrow -> "Tomorrow"
        else -> dateFormat.format(dateToCheck.time)
    }

}
