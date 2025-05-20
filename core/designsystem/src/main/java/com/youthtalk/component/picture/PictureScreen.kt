package com.youthtalk.component.picture

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import com.youth.app.core.designsystem.R
import com.youthtalk.designsystem.gray30
import com.youthtalk.designsystem.gray40
import com.youthtalk.designsystem.gray80
import com.youthtalk.model.Image
import com.youthtalk.util.FileConverter
import java.io.File
import timber.log.Timber

@Composable
fun PictureScreen(modifier: Modifier = Modifier, images: List<Image>, onBack: () -> Unit, onSelectImage: (File) -> Unit) {
    var selected by remember {
        mutableStateOf<Image?>(null)
    }
    val context = LocalContext.current
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    val tackPicture = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && photoUri != null) {
            Timber.e("CameraCapture 사진 URI: $photoUri")
            photoUri?.let { uri ->
                FileConverter.uriToFile(context, uri)?.let { file ->
                    onSelectImage(file)
                }
            }
        }
    }

    BackHandler {
        onBack()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 20.dp)
        ) {
            IconButton(onClick = onBack) {
                Image(
                    painter = painterResource(R.drawable.close),
                    contentDescription = "닫기"
                )
            }

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "최근 항목",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        selected?.let { img ->
                            FileConverter.uriToFile(context, img.uri.toUri())?.let { file ->
                                onSelectImage(file)
                            }
                        }
                    },
                text = "첨부",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = if (selected != null) MaterialTheme.colorScheme.primary else gray80
                )
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            item {
                Box(
                    modifier = modifier
                        .background(color = gray30)
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            val file = File(context.cacheDir, "photo_${System.currentTimeMillis()}.jpg")
                            photoUri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.fileprovider",
                                file
                            )
                            photoUri?.let { uri ->
                                tackPicture.launch(uri)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier.size(40.dp),
                        painter = painterResource(R.drawable.camera),
                        contentDescription = "카메라"
                    )
                }
            }

            items(
                count = images.size
            ) {
                val image = images[it]
                Box(
                    modifier = Modifier
                        .background(color = gray30)
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            selected = if (selected == image) null else image
                        }
                ) {
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = image.uri,
                        contentDescription = null
                    )

                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .align(Alignment.TopStart)
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(if (selected == image) MaterialTheme.colorScheme.primary else gray40),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.check),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}
