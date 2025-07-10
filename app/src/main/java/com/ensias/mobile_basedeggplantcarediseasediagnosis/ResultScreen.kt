package com.ensias.mobile_basedeggplantcarediseasediagnosis

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ensias.mobile_basedeggplantcarediseasediagnosis.data.ImageResult
import com.ensias.mobile_basedeggplantcarediseasediagnosis.ui.theme.PrimaryGreen
import com.ensias.mobile_basedeggplantcarediseasediagnosis.ui.theme.PrimaryViolet
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



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    0f to PrimaryViolet,
                    1f to PrimaryGreen
                )
            )
            .systemBarsPadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.arrow),
                contentDescription = "Back",
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        navController.navigate(Routes.homePage) {
                            popUpTo(Routes.resultPage) { inclusive = true }
                        }
                    }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        image?.let {
            Image(
                bitmap = it,
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp)
                    .padding(top = 32.dp)

                    .clip(RoundedCornerShape(24.dp))
            )
        }


        Spacer(modifier = Modifier.height(20.dp))

        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            colors = CardDefaults.cardColors(Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(8.dp),
            shape = MaterialTheme.shapes.medium,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState()),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "$result\n$confidence%",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                        .fillMaxWidth()
                )


            }
        }

        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            colors = CardDefaults.cardColors(Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(8.dp),
            shape = MaterialTheme.shapes.medium,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState()),
            ) {


                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .padding(bottom = 12.dp)
                            .fillMaxWidth()
                    )

            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(), // Ensures the Row takes full width
            verticalAlignment = Alignment.CenterVertically // Aligns children vertically center
        ) {
            // Capture Button
            ActionButton(
                onClick = {
                    navController.navigate(Routes.plantPage) {
                        popUpTo(Routes.resultPage) { inclusive = true }
                    }
                },
                text = "Capture"
            )
            Spacer(modifier = Modifier.width(16.dp)) // Adds spacing between buttons and text

            // Conditional rendering of Save button or Saved text
            if (!isSaved) {
                ActionButton(
                    onClick = {
                        saveImage()
                    },
                    text = "Save"
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){
                    Text(
                        text = "Saved",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                        textAlign = TextAlign.End,
                        color = Color.White
                    )
                }
            }
        }
    }
}
