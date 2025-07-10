package com.ensias.mobile_basedeggplantcarediseasediagnosis

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.AddAPhoto
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ensias.mobile_basedeggplantcarediseasediagnosis.data.ImageResult
import com.ensias.mobilemangrove.data.AppDatabase
import kotlinx.coroutines.launch
import java.io.File

data class ItemDisease(val name: String, val description: String)

val ItemDiseases = listOf(
    ItemDisease("Insect Pest Disease", "Dark-colored sooty mold often develops on the honeydew, which reduces the plant's ability to photosynthesize. These pests feed on the sap of infected plants and transfer the virus to healthy ones, making insect control essential to managing the disease."),
    ItemDisease("Leaf Spot Disease", "Symptoms appear first on lower part of plant and move upwards; initial symptoms are small circular or oval chlorotic spots on leaves which develop light to dark brown centers; as the lesions expand, they may develop concentric zones; severely infested leaves may dry out and curl then drop from the plant."),
    ItemDisease("Healthy Leaf", "A healthy eggplant leaf is an indicator of a well-nourished and thriving plant. These leaves play a crucial role in photosynthesis, enabling the plant to grow and produce fruits efficiently."),
    ItemDisease("Wilt Disease", "Symptoms appear first on lower leaves and spread upwards; symptoms include yellow blotches on lower leaves, rapid yellowing and the edges of leaves rolling inward; leaves on severely infested plants turn brown and dry")
)

fun getDiseaseDescription(result: String): String {
    val disease = ItemDiseases.find { it.name == result }
    return disease?.description ?: "Disease not found"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(navController: NavController, result: String, confidence: String) {
    val context = LocalContext.current
    val description = getDiseaseDescription(result)
    val coroutineScope = rememberCoroutineScope()
    val database = remember { AppDatabase.getDatabase(context, coroutineScope) }
    val imageResultDao = database.imageResultDao()
    val path = context.getExternalFilesDir(null)!!.absolutePath
    val imagePath = "$path/tempFileName.jpg"
    val image = BitmapFactory.decodeFile(imagePath)?.asImageBitmap()

    var isSaved by remember { mutableStateOf(false) }

    fun resizeBitmap(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height

        val matrix = Matrix()

        matrix.postScale(scaleWidth, scaleHeight)

        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
    }

    fun saveImage() {
        coroutineScope.launch {
            val imageFile = File(imagePath)
            if (imageFile.exists()) {
                val originalBitmap = BitmapFactory.decodeFile(imagePath)
                val resizedBitmap = resizeBitmap(originalBitmap, 800, 800)

                val resizedImageFile = File("$path/resizedTempFileName.jpg")
                val outputStream = resizedImageFile.outputStream()
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
                outputStream.flush()
                outputStream.close()

                val imageBytes = resizedImageFile.readBytes()
                val imageresult = ImageResult(
                    name = result,
                    image = imageBytes,
                    description = description
                )
                imageResultDao.insert(imageresult)
                resizedImageFile.deleteOnExit()
            }
            imageFile.deleteOnExit()

            isSaved = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Diagnosis Result",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(Routes.homePage) {
                                popUpTo(Routes.resultPage) { inclusive = true }
                            }
                        }
                    ) {
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
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    image?.let {
                        Image(
                            bitmap = it,
                            contentDescription = null,
                            modifier = Modifier
                                .size(250.dp)
                                .clip(RoundedCornerShape(16.dp))
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Disease name and confidence
                    Text(
                        text = result,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Confidence: $confidence%",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // Description card
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Description",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (!isSaved) {
                    Button(
                        onClick = { saveImage() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        enabled = !isSaved
                    ) {
                        if (isSaved) {
                            Icon(
                                Icons.Rounded.CheckCircle,
                                contentDescription = "Saved",
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
                            Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                            Text("Saved to History")
                        } else {
                            Icon(
                                Icons.Rounded.Save,
                                contentDescription = "Save",
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
                            Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                            Text("Save to History")
                        }
                    }
                } else {
                    Button(
                        onClick = { /* Already saved */ },
                        enabled = false,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Rounded.CheckCircle,
                            contentDescription = "Saved",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Saved")
                    }
                }

                Button(
                    onClick = {
                        navController.navigate(Routes.plantPage) {
                            popUpTo(Routes.resultPage) { inclusive = true }
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Rounded.AddAPhoto,
                        contentDescription = "Take another photo",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("New Scan")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}