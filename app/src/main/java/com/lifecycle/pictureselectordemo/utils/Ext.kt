package com.lifecycle.pictureselectordemo.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.widget.ImageView
import androidx.core.net.toFile
import com.bumptech.glide.Glide
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random


fun ImageView.load(uri: Uri?) {
    if (uri != null) Glide.with(this.context).load(uri).into(this)

}


fun ImageView.load(file: File?) {
    if (file != null) Glide.with(this.context).load(file).into(this)

}


fun Uri.toFile(context: Context): File? = when (scheme) {
    ContentResolver.SCHEME_FILE -> toFile()
    ContentResolver.SCHEME_CONTENT -> {
        val cursor = context.contentResolver.query(this, null, null, null, null)
        val file = cursor?.let {
            if (it.moveToFirst()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    //保存到本地
                    val ois = context.contentResolver.openInputStream(this)
                    val displayName =
                        it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    ois?.let {
                        val file = File(
                            context.externalCacheDir!!.absolutePath,
                            "${Random.nextInt(0, 9999)}$displayName"
                        )
                        val fos = FileOutputStream(file)
                        android.os.FileUtils.copy(ois, fos)
                        fos.close()
                        it.close()
                        file
                    }
                } else
                //直接转换
                    File(it.getString(it.getColumnIndex(MediaStore.Images.Media.DATA)))
            } else null

        }
        cursor?.close()
        file
    }
    else -> null
}