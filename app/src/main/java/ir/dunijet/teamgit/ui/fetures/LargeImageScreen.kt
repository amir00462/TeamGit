package ir.dunijet.teamgit.ui.fetures

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.burnoo.cokoin.navigation.getNavController
import ir.dunijet.teamgit.data.model.Blog
import ir.dunijet.teamgit.ui.theme.cLargeToolbar
import ir.dunijet.teamgit.util.Cache
import ir.dunijet.teamgit.util.FadeInOutWidget
import ir.dunijet.teamgit.util.KEY_BLOG
import ir.dunijet.teamgit.util.SITE_BASE_URL
import ir.dunijet.teamgit.util.ScaleAnimation
import ir.dunijet.teamgit.util.showToast
import ir.dunijet.teamgit.util.toParagraph
import ir.dunijet.teamgit.util.toReadableDateTime
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LargeImageScreen() {
    var isMultiTouchScaling by remember { mutableStateOf(false) }
    var isToolbarVisible by remember { mutableStateOf(true) }
    val navigation = getNavController()
    val context = LocalContext.current

    var lastClickTime by remember { mutableLongStateOf(0L) }
    var scale by remember { mutableFloatStateOf(1f) }
    var rotation by remember { mutableFloatStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val blog = Cache.get(KEY_BLOG)
    val systemUiController = rememberSystemUiController()

    LaunchedEffect(Unit) {
        delay(55)
        systemUiController.setStatusBarColor(color = cLargeToolbar, darkIcons = false)
        systemUiController.setNavigationBarColor(color = cLargeToolbar, darkIcons = false)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {

                detectTapGestures { _ ->
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastClickTime < 200) {

                        // Double click detected
                        isMultiTouchScaling = false
                        scale = when (scale) {
                            1f -> 2f
                            2f -> 3f
                            else -> 1f
                        }

                    } else {

                        // click detected
                        isToolbarVisible = !isToolbarVisible
                    }
                    lastClickTime = currentTime

                }
            }
    ) {

        val samePartModifier: Modifier = Modifier
            .pointerInput(Unit) {

                detectTransformGestures { _, pan, gestureZoom, gestureRotation ->
                    isMultiTouchScaling = true

                    scale *= gestureZoom
                    rotation += gestureRotation
                    offset = Offset(offset.x + pan.x, offset.y + pan.y)
                }

            }
            .background(Color.Black)
            .fillMaxSize()
        ScaleAnimation(working = !isMultiTouchScaling, scale = scale, onFinished = { }) {
            Box(
                modifier = if (isMultiTouchScaling) samePartModifier
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        rotationZ = rotation,
                        translationX = offset.x,
                        translationY = offset.y
                    ) else samePartModifier
                    .graphicsLayer(
                        rotationZ = rotation,
                        translationX = offset.x,
                        translationY = offset.y
                    )
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(if (blog.image.startsWith("upload/")) SITE_BASE_URL + blog.image else blog.image)
                        .crossfade(true)
                        .build(),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit,
                    contentDescription = null
                )
            }
        }

        FadeInOutWidget(isToolbarVisible) {

            LargeImageToolbar(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .background(cLargeToolbar.copy(0.9f)),
                blog = blog
            ) {
                navigation.popBackStack()
            }

        }

    }
}

@SuppressLint("QueryPermissionsNeeded")
@OptIn(DelicateCoroutinesApi::class)
fun shareContent(context: Context, text: String, imageUrl: String) {
    val imageLoader = ImageLoader.Builder(context).build()
    val request = ImageRequest.Builder(context).data(imageUrl).build()

    GlobalScope.launch(Dispatchers.IO) {
        val drawable = (imageLoader.execute(request).drawable)
        val bitmap = drawable?.toBitmap()

        withContext(Dispatchers.Main) {
            if (bitmap != null) {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "image/*"
                    putExtra(Intent.EXTRA_TEXT, text)
                    putExtra(Intent.EXTRA_STREAM, getUriFromBitmap(context, bitmap))
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                }

                val chooser = Intent.createChooser(shareIntent, "اشتراک گذاری")
                if (shareIntent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(chooser)
                } else {
                    Log.v("teamGitLog", "no app can receive share...")
                }
            }
        }
    }
}

private fun getUriFromBitmap(context: Context, bitmap: Bitmap): Uri? {
    return try {
        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs()
        val stream = FileOutputStream("$cachePath/image.png")
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.close()
        FileProvider.getUriForFile(
            context,
            context.packageName + ".fileprovider",
            File("$cachePath/image.png")
        )
    } catch (e: Exception) {
        Log.v("teamGitLog", e.message ?: "can't convert...")
        null
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LargeImageToolbar(modifier: Modifier, blog: Blog, onBackPressed: () -> Unit) {
    val context = LocalContext.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row {
            IconButton(
                onClick = { onBackPressed.invoke() }
            ) {
                Icon(
                    modifier = Modifier.rotate(180f),
                    imageVector = Icons.Default.ArrowBack,
//                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = null,
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {

                Text(
                    text = blog.author,
                    style = MaterialTheme.typography.h4,
                    color = Color.White
                )

                Text(
                    text = blog.updatedAt.toReadableDateTime(),
                    style = MaterialTheme.typography.caption,
                    color = Color.White
                )

            }
        }

        IconButton(
            onClick = {

                context.showToast("در حال آماده سازی برای اشتراک گذاری...")
                shareContent(
                    context = context,
                    text = blog.title + "\n\n" + blog.content.toParagraph(3) + "\n\nتوسط : " + blog.author,
                    imageUrl = if (blog.image.startsWith("upload/")) SITE_BASE_URL + blog.image else blog.image
                )

            }
        ) {
            Icon(
                Icons.Default.Share,
                contentDescription = null,
                tint = Color.White
            )
        }

    }

}