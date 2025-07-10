package com.ensias.mobile_basedeggplantcarediseasediagnosis

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ensias.mobile_basedeggplantcarediseasediagnosis.ui.theme.PrimaryGreen
import com.ensias.mobile_basedeggplantcarediseasediagnosis.ui.theme.PrimaryViolet

@Composable
fun AboutScreen(navController: NavController) {


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    0f to PrimaryViolet,
                    1f to PrimaryGreen
                )
            )
            .systemBarsPadding()
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.arrow),
            contentDescription = "Back",
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.TopStart)
                .clickable { navController.navigate(Routes.homePage) }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(text = "Welcome to", color = Color.White, fontSize = 20.sp)
            Text(text = "MOBILE-BASED EGGPLANT LEAF DISEASE DIAGNOSIS USING DEEP LEARNING IMAGE CLASSIFICATION", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold, lineHeight = 30.sp)
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Version: 1.0", color = Color.White, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(30.dp))
            Text(text = "Our app was born as part of our capstone project, with a primary focus on eggplant disease detection. Recognizing the challenges faced by farmers in identifying plant ailments early, we set out to create a solution that utilizes cutting-edge machine learning and image recognition technology. This tool empowers users to take proactive measures, ensuring healthier plants and better crop yields.", color = Color.White, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Our mission is to make plant disease diagnosis accessible, accurate, and user-friendly, bridging the gap between advanced agricultural research and everyday farming practices.", color = Color.White, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Together, let’s cultivate a future of healthy plants and sustainable agriculture!", color = Color.White, fontSize = 18.sp)
        }
    }
}
