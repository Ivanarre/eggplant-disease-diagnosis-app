package com.ensias.mobile_basedeggplantcarediseasediagnosis

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.ensias.mobile_basedeggplantcarediseasediagnosis.data.ImageResult
import com.ensias.mobile_basedeggplantcarediseasediagnosis.ui.theme.PrimaryGreen
import com.ensias.mobile_basedeggplantcarediseasediagnosis.ui.theme.PrimaryViolet
import com.ensias.mobilemangrove.data.AppDatabase
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ItemScreen(navController: NavController, itemId: Int) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val database = remember { AppDatabase.getDatabase(context, coroutineScope) }
    val imageResultDao = database.imageResultDao()

    var imageResult by remember { mutableStateOf<ImageResult?>(null) }

    // Fetch data from the database
    LaunchedEffect(itemId) {
        imageResultDao.getResultById(itemId).collect { result ->
            imageResult = result
        }
    }

    fun deleteItem() {
        coroutineScope.launch {
            imageResult?.let {
                imageResultDao.delete(it)
                navController.navigate(Routes.diagnoseHistoryPage) {
                    popUpTo(Routes.itemtPage) { inclusive = true }
                }
            }
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
        Spacer(modifier = Modifier.height(12.dp))

        imageResult?.let { result ->
            val painter = rememberImagePainter(result.image)

            Image(
                painter = painter,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp)
                    .padding(top = 32.dp)
                    .clip(RoundedCornerShape(24.dp))
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Display image result name
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            colors = CardDefaults.cardColors(Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
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
                    text = imageResult?.name ?: "No result available",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                        .fillMaxWidth()
                )
            }
        }

        // Display image result description
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            colors = CardDefaults.cardColors(Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(8.dp),
            shape = MaterialTheme.shapes.medium,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                imageResult?.let {
                    Text(
                        text = it.description,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .padding(bottom = 12.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ActionButton(
                onClick = { deleteItem() },
                text = "Delete"
            )
            Spacer(modifier = Modifier.width(16.dp))
            ActionButton(
                onClick = {
                    navController.navigate(Routes.diagnoseHistoryPage) {
                        popUpTo(Routes.itemtPage) { inclusive = true }
                    }
                },
                text = "Back"
            )
        }
    }
}
