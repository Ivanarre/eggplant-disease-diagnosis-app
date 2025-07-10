package com.ensias.mobile_basedeggplantcarediseasediagnosis

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ensias.mobile_basedeggplantcarediseasediagnosis.R
import com.ensias.mobile_basedeggplantcarediseasediagnosis.Routes
import com.ensias.mobile_basedeggplantcarediseasediagnosis.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(navController: NavController) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        visible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A237E),  // Deep blue
                        Color(0xFF4CAF50)   // Green
                    ),
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            )
            .systemBarsPadding()
    ) {
        // History button
        SmallFloatingActionButton(
            onClick = { navController.navigate(Routes.diagnoseHistoryPage) },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            containerColor = Color.White.copy(alpha = 0.2f),
            contentColor = Color.White
        ) {
            Icon(
                painter = painterResource(id = R.drawable.history),
                contentDescription = "History",
                modifier = Modifier.size(24.dp)
            )
        }

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn() + slideInVertically(
                initialOffsetY = { it / 2 }
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                
                // App Logo
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.1f))
                        .padding(16.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Welcome Text
                Text(
                    text = "Welcome to Eggplant Care",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Your plant health assistant",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
                )

                // Action Cards
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = Color.White.copy(alpha = 0.1f)
                    ),
                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = 8.dp
                    )
                ) {
                    ActionButton(
                        onClick = { navController.navigate(Routes.plantPage) },
                        text = "My Plant",
                        icon = R.drawable.plant
                    )
                }

                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = Color.White.copy(alpha = 0.1f)
                    ),
                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = 8.dp
                    )
                ) {
                    ActionButton(
                        onClick = { navController.navigate(Routes.aboutPage) },
                        text = "About Us",
                        icon = R.drawable.logo
                    )
                }
            }
        }
    }
}

@Composable
fun ActionButton(onClick: () -> Unit, text: String, icon: Int) {
    Surface(
        onClick = onClick,
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            
            Column {
                Text(
                    text = text,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = if(text == "My Plant") "Check your plant's health" else "Learn more about us",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }
        }
    }
}
