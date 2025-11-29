package com.example.rmsjims.ui.screens.assistant

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rmsjims.navigation.Screen
import com.example.rmsjims.ui.components.AppButton
import com.example.rmsjims.ui.components.AppDropDownTextField
import com.example.rmsjims.ui.components.AppTextField
import com.example.rmsjims.ui.components.CustomLabel
import com.example.rmsjims.ui.components.CustomTopBar
import com.example.rmsjims.ui.theme.cardColor
import com.example.rmsjims.ui.theme.onSurfaceColor
import com.example.rmsjims.ui.theme.onSurfaceVariant
import com.example.rmsjims.ui.theme.primaryColor
import com.example.rmsjims.ui.theme.whiteColor
import com.example.rmsjims.util.ResponsiveLayout
import com.example.rmsjims.util.pxToDp

@SuppressLint("RememberReturnType")
@Composable
fun NewEquipmentScreen(
    navController: NavHostController
) {
    // Form state
    var equipmentName by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var inChargeProfessor by remember { mutableStateOf("") }
    var inChargeAssistant by remember { mutableStateOf("") }
    var additionalInfo by remember { mutableStateOf("") }
    var link by remember { mutableStateOf("") }
    var generalCategory by remember { mutableStateOf("") }
    var subCategory by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var purchaseRate by remember { mutableStateOf("") }
    var vendor by remember { mutableStateOf("") }
    var warrantyExpiryDate by remember { mutableStateOf("") }
    
    // Image state
    var selectedImageUri by remember { mutableStateOf<String?>(null) }
    var warrantyImageUri by remember { mutableStateOf<String?>(null) }
    
    // Category options
    val generalCategoryOptions = listOf(
        "Computers",
        "Lab Equipment",
        "Tools",
        "Networking Devices",
        "Cameras",
        "Furniture"
    )
    
    val subCategoryOptions = remember(generalCategory) {
        when (generalCategory) {
            "Computers" -> listOf("Desktop", "Laptop", "Server", "Workstation")
            "Lab Equipment" -> listOf("Microscope", "Centrifuge", "Spectrometer", "Incubator")
            "Tools" -> listOf("Power Tools", "Hand Tools", "Measuring Tools")
            "Networking Devices" -> listOf("Router", "Switch", "Access Point", "Modem")
            "Cameras" -> listOf("DSLR", "Mirrorless", "Action Camera", "Security Camera")
            "Furniture" -> listOf("Desk", "Chair", "Cabinet", "Shelf")
            else -> emptyList()
        }
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Add Equipment",
                onNavigationClick = {
                    navController.popBackStack()
                },
                navController = navController
            )
        },
        containerColor = whiteColor,
        bottomBar = {
            AppButton(
                buttonText = "CONFIRM",
                onClick = {
                    // Handle equipment creation
                    navController.popBackStack()
                },
                modifier = Modifier.padding(ResponsiveLayout.getHorizontalPadding())
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(ResponsiveLayout.getHorizontalPadding())
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(ResponsiveLayout.getResponsivePadding(13.dp, 16.dp, 20.dp))
        ) {
            Spacer(modifier = Modifier.height(ResponsiveLayout.getResponsivePadding(20.dp, 24.dp, 28.dp)))
            
            // Image field with icon
            ImageField(
                selectedImageUri = selectedImageUri,
                onImageSelected = { selectedImageUri = it }
            )
            
            AppTextField(
                value = equipmentName,
                onValueChange = { equipmentName = it },
                placeholder = "Equipment Name"
            )

            AppTextField(
                value = brand,
                onValueChange = { brand = it },
                placeholder = "Brand"
            )

            AppTextField(
                value = model,
                onValueChange = { model = it },
                placeholder = "Model"
            )

            AppTextField(
                value = location,
                onValueChange = { location = it },
                placeholder = "Location"
            )

            AppTextField(
                value = inChargeProfessor,
                onValueChange = { inChargeProfessor = it },
                placeholder = "In-Charge (Professor)"
            )

            AppTextField(
                value = inChargeAssistant,
                onValueChange = { inChargeAssistant = it },
                placeholder = "In-Charge (Assistant)"
            )

            AppTextField(
                value = additionalInfo,
                onValueChange = { additionalInfo = it },
                placeholder = "Additional Information",
                minLines = 3,
                maxLines = 3
            )

            AppTextField(
                value = link,
                onValueChange = { link = it },
                placeholder = "Link (How to use video)"
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                AppDropDownTextField(
                    modifier = Modifier.weight(1f),
                    value = generalCategory,
                    onValueChange = { generalCategory = it },
                    placeholder = "General Category",
                    items = generalCategoryOptions
                )

                AppDropDownTextField(
                    modifier = Modifier.weight(1f),
                    value = subCategory,
                    onValueChange = { subCategory = it },
                    placeholder = "Sub Category",
                    items = subCategoryOptions
                )
            }

            // Divider Section - Additional Info
            Spacer(modifier = Modifier.height(ResponsiveLayout.getResponsivePadding(8.dp, 10.dp, 12.dp)))
            Divider(
                thickness = pxToDp(1),
                color = onSurfaceColor.copy(alpha = 0.2f)
            )
            Spacer(modifier = Modifier.height(ResponsiveLayout.getResponsivePadding(8.dp, 10.dp, 12.dp)))

            AppTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = "Description",
                minLines = 3,
                maxLines = 5
            )

            AppTextField(
                value = purchaseRate,
                onValueChange = { purchaseRate = it },
                placeholder = "Purchase Rate / Cost"
            )

            AppTextField(
                value = vendor,
                onValueChange = { vendor = it },
                placeholder = "Vendor"
            )

            // Warranty Expiry Date with image icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(pxToDp(8)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppTextField(
                    value = warrantyExpiryDate,
                    onValueChange = { warrantyExpiryDate = it },
                    placeholder = "Warranty Expiry Date",
                    modifier = Modifier.weight(1f)
                )
                
                Box(
                    modifier = Modifier
                        .size(ResponsiveLayout.getResponsiveSize(52.dp, 60.dp, 68.dp))
                        .background(
                            color = onSurfaceVariant,
                            shape = RoundedCornerShape(pxToDp(8))
                        )
                        .clickable {
                            // Open gallery to select warranty image
                            warrantyImageUri = "warranty_image_${System.currentTimeMillis()}"
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Warranty Image",
                        tint = primaryColor,
                        modifier = Modifier.size(pxToDp(24))
                    )
                }
            }

            Spacer(modifier = Modifier.height(ResponsiveLayout.getResponsivePadding(13.dp, 16.dp, 20.dp)))
        }
    }
}

@Composable
fun ImageField(
    selectedImageUri: String?,
    onImageSelected: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(ResponsiveLayout.getResponsiveSize(120.dp, 140.dp, 160.dp))
            .background(
                color = onSurfaceVariant,
                shape = RoundedCornerShape(pxToDp(8))
            )
            .border(
                width = pxToDp(1),
                color = cardColor.copy(alpha = 0.5f),
                shape = RoundedCornerShape(pxToDp(8))
            )
            .clickable {
                // Open gallery to select image
                onImageSelected("image_${System.currentTimeMillis()}")
            },
        contentAlignment = Alignment.Center
    ) {
        if (selectedImageUri != null) {
            // Show selected image preview (placeholder for now)
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CustomLabel(
                    header = "Image Selected",
                    headerColor = primaryColor,
                    fontSize = 14.sp
                )
            }
        } else {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Image",
                tint = primaryColor,
                modifier = Modifier.size(pxToDp(32))
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewEquipmentScreenPreview() {
    val navController = rememberNavController()
    NewEquipmentScreen(navController = navController)
}
