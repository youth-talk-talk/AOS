package com.youthtalk.util

import android.content.Context
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import timber.log.Timber

object FileConverter {
    fun uriToFile(context: Context, uri: Uri): File? {
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            inputStream?.let {
                val file = createTempImageFile(context)
                copyInputStreamToFile(it, file)
                return file
            }
        } catch (e: FileNotFoundException) {
            Timber.e("파일 찾기 실패 $uri")
            return null
        }

        return null
    }

    private fun createTempImageFile(context: Context): File {
        val timeStamp = System.currentTimeMillis()
        val imageFileName = "JPEG_${timeStamp}_"
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        )
    }

    private fun copyInputStreamToFile(inputStream: InputStream, file: File) {
        try {
            FileOutputStream(file).use { outputStream ->
                val buffer = ByteArray(4 * 1024) // buffer size
                var read: Int
                while (inputStream.read(buffer).also { read = it } != -1) {
                    outputStream.write(buffer, 0, read)
                }
                outputStream.flush()
            }
        } catch (e: IOException) {
            Timber.e(e.message)
        } finally {
            try {
                inputStream.close()
            } catch (e: IOException) {
                Timber.e(e.message)
            }
        }
    }
}
