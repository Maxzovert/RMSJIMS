package com.example.rmsjims.ui.screens.staff

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.rmsjims.R
import com.example.rmsjims.data.model.BookingPriority
import com.example.rmsjims.data.model.UiState
import com.example.rmsjims.navigation.Screen
import com.example.rmsjims.ui.components.AppButton
import com.example.rmsjims.ui.components.AppDropDownTextField
import com.example.rmsjims.ui.components.AppTextField
import com.example.rmsjims.ui.components.CustomLabel
import com.example.rmsjims.ui.components.CustomTopBar
import com.example.rmsjims.ui.components.FilteredAppTextField
import com.example.rmsjims.ui.theme.cardColor
import com.example.rmsjims.ui.theme.onSurfaceColor
import com.example.rmsjims.ui.theme.primaryColor
import com.example.rmsjims.ui.theme.whiteColor
import com.example.rmsjims.util.ResponsiveLayout
import com.example.rmsjims.util.pxToDp
import com.example.rmsjims.viewmodel.BranchViewModel
import com.example.rmsjims.viewmodel.DepartmentViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
@SuppressLint("RememberReturnType")
fun RaiseTicketScreen(
    navController: NavHostController,
    ){
    val branchViewModel : BranchViewModel = koinViewModel()
    val branchList = branchViewModel.branchName
    var resourceName by remember { mutableStateOf("") }
    var issueDescription by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf<BookingPriority?>(null) }
    var selectedBranch by remember { mutableStateOf("") }
    var expectedResolutionDate by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    val departmentViewModel : DepartmentViewModel = koinViewModel()
    val query = departmentViewModel.query
    val filteredDepartmentList = departmentViewModel.filteredDepartments

    // Image upload state - storing URIs as strings for now (UI only)
    val selectedImages = remember { mutableStateListOf<String>() }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Report an Issue",
                onNavigationClick = {
                    navController.popBackStack()
                },
                navController = navController
            )
        },
        containerColor = whiteColor,
        bottomBar = {
            AppButton(
                buttonText = "SUBMIT",
                onClick = {
                    navController.navigate(Screen.BookingsScreen.route)
                },
                modifier = Modifier.padding(ResponsiveLayout.getHorizontalPadding())
            )
        }
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .padding(ResponsiveLayout.getHorizontalPadding())
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(ResponsiveLayout.getResponsivePadding(13.dp, 16.dp, 20.dp))
        ){
            Spacer(modifier = Modifier.height(ResponsiveLayout.getResponsivePadding(20.dp, 24.dp, 28.dp)))
            
            // Resource Name
            AppTextField(
                value = resourceName,
                onValueChange = { resourceName = it},
                placeholder = "Resource Name"
            )

            // Issue Description - Multi-line
            AppTextField(
                value = issueDescription,
                onValueChange = { issueDescription = it},
                placeholder = "Issue Description",
                minLines = 3,
                maxLines = 5
            )

            // Priority Selection - Segmented Button Style
            PrioritySelector(
                selectedPriority = selectedPriority,
                onPrioritySelected = { selectedPriority = it }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                when (branchViewModel.branchState) {
                    is UiState.Loading -> AppDropDownTextField(
                        modifier = Modifier.weight(1f),
                        value = "",
                        onValueChange = {},
                        placeholder = "Loading...",
                        items = emptyList()
                    )
                    is UiState.Error -> AppDropDownTextField(
                        modifier = Modifier.weight(1f),
                        value = "",
                        onValueChange = {},
                        placeholder = "Department",
                        items = emptyList()
                    )
                    is UiState.Success -> AppDropDownTextField(
                        modifier = Modifier.weight(1f),
                        value = selectedBranch,
                        onValueChange = { selectedBranch = it },
                        placeholder = "Category",
                        items = branchList
                    )
                }

            }

            // Image/Screenshot Upload Section
            ImageUploadSection(
                selectedImages = selectedImages,
                onImageAdd = { imageUri -> 
                    if (!selectedImages.contains(imageUri)) {
                        selectedImages.add(imageUri)
                    }
                },
                onImageRemove = { imageUri -> selectedImages.remove(imageUri) }
            )

            Divider(thickness = 1.dp, color = onSurfaceColor.copy(0.2f))

            // Resource Location - Optional, Auto-populated
            when (departmentViewModel.departmentState) {
                is UiState.Loading -> FilteredAppTextField(
                    modifier = Modifier.weight(1f),
                    value = "",
                    onValueChange = {},
                    placeholder = "Loading..."
                )
                is UiState.Error -> FilteredAppTextField(
                    modifier = Modifier.weight(1f),
                    value = "",
                    onValueChange = {},
                    placeholder = "Resource Location"
                )
                is UiState.Success -> FilteredAppTextField(
                    value = query,
                    onValueChange = { departmentViewModel.onQueryChange(it)},
                    placeholder = "Resource Location (Optional)",
                    items = filteredDepartmentList,
                    onItemSelected = {
                        departmentViewModel.onDepartmentSelected(it)
                    }
                )
            }

            // Expected Resolution Date - Date Picker
            DatePickerField(
                value = expectedResolutionDate,
                placeholder = "Expected Resolution Date",
                onDateClick = { 
                    // In a real app, this would open a date picker dialog
                    // For now, using a simple text input as placeholder
                    showDatePicker = true
                }
            )

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Provide the issue details and select the appropriate category and priority from the dropdowns",
                color = onSurfaceColor.copy(0.9f),
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth(),
                letterSpacing = 0.3.sp,
                lineHeight = 20.sp,
                softWrap = true,
                fontFamily =  FontFamily(Font(R.font.font_alliance_regular_two)),
            )

        }
    }
}

// Priority Selector Component - Segmented Button Style
@Composable
fun PrioritySelector(
    selectedPriority: BookingPriority?,
    onPrioritySelected: (BookingPriority) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(pxToDp(8))
    ) {
        CustomLabel(
            header = "Priority",
            headerColor = onSurfaceColor.copy(0.9f),
            fontSize = 14.sp
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(pxToDp(8))
        ) {
            BookingPriority.values().forEach { priority ->
                PriorityChip(
                    priority = priority,
                    isSelected = selectedPriority == priority,
                    onClick = { onPrioritySelected(priority) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun PriorityChip(
    priority: BookingPriority,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(ResponsiveLayout.getResponsiveSize(44.dp, 52.dp, 60.dp))
            .clip(RoundedCornerShape(pxToDp(8)))
            .background(
                color = if (isSelected) priority.color.copy(alpha = 0.15f) else cardColor.copy(alpha = 0.3f),
                shape = RoundedCornerShape(pxToDp(8))
            )
            .border(
                width = if (isSelected) pxToDp(2) else pxToDp(1),
                color = if (isSelected) priority.color else cardColor.copy(alpha = 0.5f),
                shape = RoundedCornerShape(pxToDp(8))
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        CustomLabel(
            header = priority.label,
            fontSize = 14.sp,
            headerColor = if (isSelected) priority.color else onSurfaceColor.copy(0.7f)
        )
    }
}

// Image Upload Section Component
@Composable
fun ImageUploadSection(
    selectedImages: List<String>,
    onImageAdd: (String) -> Unit,
    onImageRemove: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(pxToDp(12))
    ) {
        CustomLabel(
            header = "Images / Screenshots (Optional)",
            headerColor = onSurfaceColor.copy(0.9f),
            fontSize = 14.sp
        )

        // Action Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(pxToDp(12))
        ) {
            ImageUploadButton(
                icon = painterResource(R.drawable.ic_camera),
                label = "Camera",
                onClick = { 
                    // In a real app, this would request camera permission and open camera
                    // For UI demonstration, adding a placeholder image URI
                    onImageAdd("camera_placeholder_${System.currentTimeMillis()}")
                },
                modifier = Modifier.weight(1f)
            )
            ImageUploadButton(
                icon = painterResource(R.drawable.ic_add),
                label = "Gallery",
                onClick = { 
                    // In a real app, this would request gallery permission and open gallery
                    // For UI demonstration, adding a placeholder image URI
                    onImageAdd("gallery_placeholder_${System.currentTimeMillis()}")
                },
                modifier = Modifier.weight(1f)
            )
        }

        // Image Preview Row
        if (selectedImages.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(pxToDp(12)),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(selectedImages) { index, imageUri ->
                    ImagePreviewItem(
                        imageUri = imageUri,
                        onRemove = { onImageRemove(imageUri) }
                    )
                }
            }
        }
    }
}

@Composable
fun ImageUploadButton(
    icon: Painter,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(ResponsiveLayout.getResponsiveSize(52.dp, 60.dp, 68.dp))
            .clip(RoundedCornerShape(pxToDp(8)))
            .background(
                color = primaryColor.copy(alpha = 0.1f),
                shape = RoundedCornerShape(pxToDp(8))
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(pxToDp(8)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = icon,
                contentDescription = label,
                tint = primaryColor,
                modifier = Modifier.size(pxToDp(20))
            )
            CustomLabel(
                header = label,
                fontSize = 14.sp,
                headerColor = primaryColor
            )
        }
    }
}

@Composable
fun ImagePreviewItem(
    imageUri: String,
    onRemove: () -> Unit
) {
    Box {
        Box(
            modifier = Modifier
                .width(ResponsiveLayout.getResponsiveSize(80.dp, 100.dp, 120.dp))
                .height(ResponsiveLayout.getResponsiveSize(80.dp, 100.dp, 120.dp))
                .clip(RoundedCornerShape(pxToDp(8)))
                .background(
                    color = cardColor.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(pxToDp(8))
                )
        ) {
            // In a real app, load actual image from URI
            // For demo, showing a placeholder
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = "Image Preview",
                    tint = cardColor,
                    modifier = Modifier.size(pxToDp(32))
                )
            }
        }
        
        // Remove Button
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(pxToDp(24))
                .clip(RoundedCornerShape(pxToDp(12)))
                .background(Color(0xCC000000))
                .clickable { onRemove() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove Image",
                tint = whiteColor,
                modifier = Modifier.size(pxToDp(16))
            )
        }
    }
}

// Date Picker Field Component
@Composable
fun DatePickerField(
    value: String,
    placeholder: String,
    onDateClick: () -> Unit
) {
    AppTextField(
        value = value,
        onValueChange = { },
        placeholder = placeholder,
        modifier = Modifier.clickable { onDateClick() }
    )
}
