package com.ensias.mobile_basedeggplantcarediseasediagnosis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import com.ensias.mobile_basedeggplantcarediseasediagnosis.ui.theme.MOBILEBASEDEGGPLANTCAREDISEASEDIAGNOSISTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MOBILEBASEDEGGPLANTCAREDISEASEDIAGNOSISTheme {

                   AppNavigation()

            }
        }
    }
}


