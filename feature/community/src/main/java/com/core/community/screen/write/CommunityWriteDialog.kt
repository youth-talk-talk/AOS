package com.core.community.screen.write

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import com.core.community.worker.UploadWork.uploadWorkManager
import com.youthtalk.component.CustomDialog
import java.io.OutputStream
import java.util.UUID

@Composable
fun CommunityWriteDialog(
    showPictureDialog: Boolean,
    context: Context,
    deleteDialog: Boolean,
    checkPermission: (String) -> Boolean,
    onDismissDeleteDialog: () -> Unit,
    onBack: () -> Unit,
    onDismissShowPictureDialog: () -> Unit,
    addImage: (String) -> Unit,
) {
    var repeatRequestDialog by remember { mutableStateOf(false) }
    var settingDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var requestPermission by remember { mutableStateOf("") }

    val cameraLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview()) { camera ->
        camera?.let {
            val uri = getImageUri(context, it)
            uploadWorkManager(
                context,
                scope,
                UUID.randomUUID(),
                uri,
                changeUri = addImage,
            )
        }
    }

    val pictureLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadWorkManager(context, scope, UUID.randomUUID(), uri, changeUri = addImage)
        }
    }

    val cameraRequestPermission = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch()
        } else {
            requestPermission = Manifest.permission.CAMERA
            if (checkPermission(requestPermission)) {
                repeatRequestDialog = true
            } else {
                settingDialog = true
            }
        }
    }

    val albumsRequestPermission = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            pictureLauncher.launch("image/*")
        } else {
            requestPermission = if (Build.VERSION.SDK_INT <= 32) Manifest.permission.READ_EXTERNAL_STORAGE else Manifest.permission.READ_MEDIA_IMAGES
            if (checkPermission(requestPermission)) {
                repeatRequestDialog = true
            } else {
                settingDialog = true
            }
        }
    }

    if (showPictureDialog) {
        AddPictureDialog(
            onDismiss = { onDismissShowPictureDialog() },
            onCamera = {
                onRequestPermission(
                    context = context,
                    permission = Manifest.permission.CAMERA,
                    requestLauncher = { cameraRequestPermission.launch(it) },
                    success = {
                        cameraLauncher.launch()
                        onDismissShowPictureDialog()
                    },
                )
            },
            onAlbum = {
                onRequestPermission(
                    context = context,
                    permission = if (Build.VERSION.SDK_INT <= 32) {
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    } else {
                        Manifest.permission.READ_MEDIA_IMAGES
                    },
                    requestLauncher = { albumsRequestPermission.launch(it) },
                    success = {
                        pictureLauncher.launch("image/*")
                        onDismissShowPictureDialog()
                    },
                )
            },
        )
    }

    if (deleteDialog) {
        CustomDialog(
            title = "글쓰기를 중단하시겠습니까?\n" +
                "작성중이던 글이 사라집니다",
            onCancel = onDismissDeleteDialog,
            onSuccess = { onBack() },
            onDismiss = onDismissDeleteDialog,
        )
    }

    if (repeatRequestDialog) {
        CustomDialog(
            title = "권한을 허용해야 기능을 사용할 수 있습니다.",
            cancelTitle = "나중에 요청하기",
            successTitle = "권한 요청하기",
            onCancel = { repeatRequestDialog = false },
            onSuccess = {
                if (requestPermission == Manifest.permission.CAMERA) {
                    cameraRequestPermission.launch(requestPermission)
                } else {
                    albumsRequestPermission.launch(requestPermission)
                }
            },
            onDismiss = { repeatRequestDialog = false },
        )
    }

    if (settingDialog) {
        CustomDialog(
            title = "설정 화면에서 권한을 허용해주세요",
            successTitle = "설정화면 이동",
            cancelTitle = "나중에 설정하기",
            onCancel = { settingDialog = false },
            onSuccess = {
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                context.startActivity(intent)
            },
            onDismiss = { settingDialog = false },
        )
    }
}

private fun getImageUri(context: Context, bitmap: Bitmap): Uri {
    val contentResolver = context.contentResolver
    val fileName = "${System.currentTimeMillis()}.jpg"
    var fos: OutputStream?
    var imageUri: Uri?
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        put(MediaStore.Video.Media.IS_PENDING, 1)
    }

    contentResolver.also { resolver ->
        imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        fos = imageUri?.let { resolver.openOutputStream(it) }
    }

    fos?.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }
    contentValues.clear()
    contentValues.put(MediaStore.Video.Media.IS_PENDING, 0)
    imageUri?.let { uri ->
        contentResolver.update(uri, contentValues, null, null)
    }

    return imageUri ?: Uri.EMPTY
}

private fun onRequestPermission(context: Context, permission: String, requestLauncher: (String) -> Unit, success: () -> Unit) {
    if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
        // 성공했을 때
        success()
    } else {
        // 실패했을 때
        requestLauncher(permission)
    }
}
