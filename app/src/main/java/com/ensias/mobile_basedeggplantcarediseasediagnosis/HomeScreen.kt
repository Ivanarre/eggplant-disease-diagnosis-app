package com.androidlead.loginappui.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ensias.mobile_basedeggplantcarediseasediagnosis.R
import com.ensias.mobile_basedeggplantcarediseasediagnosis.Routes
import com.ensias.mobile_basedeggplantcarediseasediagnosis.ui.theme.PrimaryGreen
import com.ensias.mobile_basedeggplantcarediseasediagnosis.ui.theme.PrimaryGreenDark
import com.ensias.mobile_basedeggplantcarediseasediagnosis.ui.theme.PrimaryViolet

@Composable
fun HomeScreen(navController: NavController) {
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
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .size(400.dp)

            )

            ActionButton(onClick = { navController.navigate(Routes.plantPage) }, "My Plant")

            ActionButton(onClick = { navController.navigate(Routes.aboutPage) }, "About Us")
        }


        IconButton(
            onClick = { navController.navigate(Routes.diagnoseHistoryPage) },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(48.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.history),
                contentDescription = "Diagnose History",
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
fun ActionButton(onClick: () -> Unit, text: String) {
    ElevatedButton(
        elevation = ButtonDefaults.elevatedButtonElevation(5.dp),
        onClick = onClick,
        modifier = Modifier
            .width(220.dp)
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
