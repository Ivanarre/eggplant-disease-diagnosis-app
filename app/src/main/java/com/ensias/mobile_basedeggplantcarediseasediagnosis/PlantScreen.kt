package com.ensias.mobile_basedeggplantcarediseasediagnosis

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.ensias.mobile_basedeggplantcarediseasediagnosis.ml.EggplantClassModel
import com.ensias.mobile_basedeggplantcarediseasediagnosis.ui.theme.*
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantScreen(navController: NavController) {
    val context = LocalContext.current
    var permissionRequested by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var resultText by remember { mutableStateOf("Result will be shown here") }

    val requestPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            // Permission granted
        } else {
            // Handle permission denied
        }
    }

    fun checkAndRequestPermission(permission: String, action: () -> Unit) {
        when {
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED -> {
                action()
            }
            else -> {
                if (permissionRequested) {
                    requestPermissionLauncher.launch(permission)
                } else {
                    permissionRequested = true
                    requestPermissionLauncher.launch(permission)
                }
            }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageUri = uri
            val inputStream = context.contentResolver.openInputStream(uri)
            imageBitmap = BitmapFactory.decodeStream(inputStream)
            imageBitmap?.let { bitmap ->
                classifyImage(bitmap, context) { result, confidence ->
                    resultText = result

                    val path = context.getExternalFilesDir(null)!!.absolutePath
                    val image = imageBitmap
                    val tempFile = File(path, "tempFileName.jpg")
                    val fOut = FileOutputStream(tempFile)
                    image?.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                    fOut.close()

                    navController.navigate("ResultPage/${resultText}/${confidence}")
                }
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
        if (success) {
            imageUri?.let { uri ->
                val inputStream = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                bitmap?.let { imageBitmap ->
                    classifyImage(imageBitmap, context) { result, confidence ->
                        resultText = result

                        val path = context.getExternalFilesDir(null)!!.absolutePath
                        val image = imageBitmap
                        val tempFile = File(path, "tempFileName.jpg")
                        val fOut = FileOutputStream(tempFile)
                        image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                        fOut.close()

                        navController.navigate("ResultPage/${resultText}/${confidence}")
                    }
                }
            }
        }
    }

    fun uploadClick() {
        checkAndRequestPermission(Manifest.permission.READ_EXTERNAL_STORAGE) {
            galleryLauncher.launch("image/*")
        }
    }

    fun captureClick() {
        checkAndRequestPermission(Manifest.permission.CAMERA) {
            val tempUri = FileProvider.getUriForFile(context, context.packageName + ".provider", createImageFile(context))
            imageUri = tempUri
            cameraLauncher.launch(tempUri)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "My Plant",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Routes.homePage) }) {
                        Icon(Icons.Rounded.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Text(
                        text = "Take a picture of your plant or choose from gallery",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Medium
                        ),
                        textAlign = TextAlign.Center
                    )

                    Image(
                        painter = painterResource(id = R.drawable.camera),
                        contentDescription = null,
                        modifier = Modifier
                            .size(200.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(16.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        "Diagnose Your Plant",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                    Text(
                        "Take a photo or upload an image to check your plant's health",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ElevatedButton(
                            onClick = { captureClick() },
                            modifier = Modifier
                                .weight(1f)
                                .height(80.dp)
                                .padding(end = 8.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    Icons.Filled.AddAPhoto,
                                    contentDescription = "Take photo",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Take photo", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                        ElevatedButton(
                            onClick = { uploadClick() },
                            modifier = Modifier
                                .weight(1f)
                                .height(80.dp)
                                .padding(start = 8.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    Icons.Filled.PhotoLibrary,
                                    contentDescription = "Choose from gallery",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("From gallery", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ActionButton(onClick: () -> Unit, text: String) {
    ElevatedButton(
        elevation = ButtonDefaults.elevatedButtonElevation(5.dp),
        onClick = onClick,
        modifier = Modifier
            .width(150.dp)
            .height(80.dp)
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryGreenDark,
            contentColor = Color.White
        )
    ) {
        Text(text = text, color = Color.White, fontSize = 16.sp)
    }
}

@Throws(IOException::class)
fun createImageFile(context: Context): File {
    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile("JPEG_${timestamp}_", ".jpg", storageDir)
}

fun classifyImage(image: Bitmap, context: Context, onResult: (String, String) -> Unit) {
    try {
        val model = EggplantClassModel.newInstance(context)

        val imageSize = 224
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, imageSize, imageSize, 3), DataType.FLOAT32)
        val byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(imageSize * imageSize)
        val scaledBitmap = Bitmap.createScaledBitmap(image, imageSize, imageSize, false)
        scaledBitmap.getPixels(intValues, 0, imageSize, 0, 0, imageSize, imageSize)

        var pixel = 0
        for (i in 0 until imageSize) {
            for (j in 0 until imageSize) {
                val value = intValues[pixel++]
                byteBuffer.putFloat(((value shr 16) and 0xFF) * (1f / 255f))
                byteBuffer.putFloat(((value shr 8) and 0xFF) * (1f / 255f))
                byteBuffer.putFloat((value and 0xFF) * (1f / 255f))
            }
        }

        inputFeature0.loadBuffer(byteBuffer)
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val confidences = outputFeature0.floatArray
        val classes = arrayOf("Wilt Disease", "Healthy Leaf", "Leaf Spot Disease", "Insect Pest Disease")
        val maxPos = confidences.indices.maxByOrNull { confidences[it] } ?: 0
        val result = classes[maxPos]
        val maxConfidence = confidences[maxPos]
        val confidenceText = "${String.format("%.2f", maxConfidence * 100)}"

        onResult(result, confidenceText)
        model.close()
    } catch (e: IOException) {
        Log.e("ImageClassification", "Error classifying image", e)
        onResult("Error", "Unable to classify image")
    }
}