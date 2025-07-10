package com.ensias.mobile_basedeggplantcarediseasediagnosis

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ensias.mobile_basedeggplantcarediseasediagnosis.data.ImageResult
import com.ensias.mobile_basedeggplantcarediseasediagnosis.ui.theme.PrimaryGreen
import com.ensias.mobile_basedeggplantcarediseasediagnosis.ui.theme.PrimaryViolet
import com.ensias.mobilemangrove.data.AppDatabase
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun HistoryScreen(navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val database = remember { AppDatabase.getDatabase(context, coroutineScope) }
    val imageResultDao = database.imageResultDao()

    // State to hold the list of image results
    var imageResults by remember { mutableStateOf<List<ImageResult>>(emptyList()) }

    // Launch a coroutine to fetch data from the database
    coroutineScope.launch {
        imageResultDao.getAllImageResult().collect { results ->
            imageResults = results
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
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Row(
                Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    painter = painterResource(id = R.drawable.arrow),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            navController.navigate(Routes.homePage)
                        }
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "Diagnose History", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        HistoryContent(imageResults, navController)
    }
}

@Composable
fun HistoryContent(imageResults: List<ImageResult>, navController: NavController) {
    LazyColumn {
        items(imageResults) { result ->
            Spacer(modifier = Modifier.height(8.dp))
            HistoryItemCard(result, navController)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun HistoryItemCard(result: ImageResult, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // Navigate to the details screen and pass the ImageResult entity
                navController.navigate("ItemPage/${result.id}")
            }
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            // Convert ByteArray to Bitmap and then to ImageBitmap
            val imageBitmap = remember(result.image) {
                BitmapFactory.decodeStream(ByteArrayInputStream(result.image)).asImageBitmap()
            }

            // Display the image with an aspect ratio
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Image(
                    bitmap = imageBitmap,
                    contentDescription = result.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(15.dp))
            Column {
                Text(text = "ID: ${result.id}", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Name: ${result.name}", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}