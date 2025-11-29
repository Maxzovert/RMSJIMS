package com.example.rmsjims.ui.screens.staff

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.example.rmsjims.R
import com.example.rmsjims.data.model.UiState
import com.example.rmsjims.data.model.UserRole
import com.example.rmsjims.data.schema.Facilities
import com.example.rmsjims.navigation.Screen
import com.example.rmsjims.ui.components.AppButton
import com.example.rmsjims.ui.components.AppCategoryIcon
import com.example.rmsjims.ui.components.AppCircularIcon
import com.example.rmsjims.ui.components.AppTextField
import com.example.rmsjims.ui.components.AppDropDownTextField
import com.example.rmsjims.ui.components.CustomLabel
import com.example.rmsjims.ui.components.CustomTopBar
import com.example.rmsjims.ui.theme.onSurfaceVariant
import com.example.rmsjims.ui.theme.circularBoxColor
import com.example.rmsjims.ui.theme.onSurfaceColor
import com.example.rmsjims.ui.theme.editCardTextColor
import com.example.rmsjims.ui.theme.primaryColor
import com.example.rmsjims.ui.theme.errorColor
import com.example.rmsjims.ui.theme.whiteColor
import com.example.rmsjims.ui.theme.cardColor
import com.example.rmsjims.ui.theme.chipColor
import com.example.rmsjims.util.ResponsiveLayout
import com.example.rmsjims.util.pxToDp
import com.example.rmsjims.viewmodel.FacilitiesViewModel
import com.example.rmsjims.viewmodel.UserSessionViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPagerIndicator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.font.FontWeight
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProdDescScreen(
    sessionViewModel: UserSessionViewModel = koinViewModel(),
    navController: NavHostController
) {
    val userRole = sessionViewModel.userRole
    val isAdmin = userRole == UserRole.ADMIN
    val currentRoute = navController.currentBackStackEntry?.destination?.route
    val isEditMode = remember(currentRoute) { 
        currentRoute == Screen.ProductDescriptionEditScreen.route 
    }
    
    // Equipment data state (for edit mode)
    var equipmentName by remember { mutableStateOf("Canon EOS R50 V") }
    var equipmentBrand by remember { mutableStateOf("Canon") }
    var equipmentModel by remember { mutableStateOf("EOS R5 Mark II") }
    var equipmentStatus by remember { mutableStateOf("Available") }
    var assignedTo by remember { mutableStateOf("Not Assigned") }
    var equipmentCategory by remember { mutableStateOf("Cameras") }
    var equipmentLocation by remember { mutableStateOf("IDC, Photo Studio") }
    var equipmentTiming by remember { mutableStateOf("9:00 AM - 6:00 PM") }
    var hasUnsavedChanges by remember { mutableStateOf(false) }
    
    // Dialog and snackbar state
    var showDiscardDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    
    // Status options
    val statusOptions = listOf("Available", "In Use", "Under Maintenance", "Offline", "Needs Repair")
    val assignedToOptions = listOf("Not Assigned", "Staff", "Assistant")
    val categoryOptions = listOf("Computers", "Lab Equipment", "Tools", "Networking Devices", "Cameras", "Furniture")
    
    // Admin-specific state
    var assignedUser by remember { mutableStateOf("Dr. Ravi Kumar") }
    var assignedRole by remember { mutableStateOf("Staff") }
    var equipmentTag by remember { mutableStateOf("Available") }
    var showAdditionalInfo by remember { mutableStateOf(false) }
    var showGeneralInfo by remember { mutableStateOf(true) }
    var showLocationAssignment by remember { mutableStateOf(true) }
    var showBookings by remember { mutableStateOf(true) }
    var showMaintenance by remember { mutableStateOf(true) }
    
    // Sample data
    val usageHistory = remember {
        listOf(
            "Used by Dr. Ravi Kumar - 2024-11-20",
            "Used by Lab Assistant - 2024-11-18",
            "Used by Dr. Smith - 2024-11-15"
        )
    }
    val latestBookings = remember {
        listOf(
            "Booking #BK-001 - 2024-11-25 to 2024-11-30",
            "Booking #BK-002 - 2024-11-20 to 2024-11-22",
            "Booking #BK-003 - 2024-11-15 to 2024-11-18"
        )
    }
    val upcomingBookings = remember {
        listOf(
            "Booking #BK-004 - 2024-12-01 to 2024-12-05",
            "Booking #BK-005 - 2024-12-10 to 2024-12-15"
        )
    }
    val pastBookings = remember {
        listOf(
            "Booking #BK-001 - 2024-11-25 to 2024-11-30",
            "Booking #BK-002 - 2024-11-20 to 2024-11-22"
        )
    }
    val maintenanceHistory = remember {
        listOf(
            "Maintenance #MT-001 - 2024-11-15 - Completed",
            "Maintenance #MT-002 - 2024-10-10 - Completed"
        )
    }
    val scheduledMaintenance = remember {
        listOf(
            "Maintenance #MT-003 - Scheduled for 2024-12-15"
        )
    }
    val overdueTasks = remember {
        listOf(
            "Maintenance #MT-004 - Overdue since 2024-11-10"
        )
    }
    val assignmentHistory = remember {
        listOf(
            "Assigned to Dr. Ravi Kumar (Staff) - 2024-01-15 to Present",
            "Assigned to Lab Assistant - 2023-06-16 to 2024-01-14"
        )
    }

    val productImage = listOf(
        R.drawable.temp,
        R.drawable.temp,
        R.drawable.temp
    )

    val pagerState = rememberPagerState(pageCount = { productImage.size })
    val pagerInteractionSource = remember { MutableInteractionSource() }
    val pagerIsPressed by pagerInteractionSource.collectIsPressedAsState()
    val pagerIsDragged by pagerState.interactionSource.collectIsDraggedAsState()
    val autoAdvance = !pagerIsDragged && !pagerIsPressed

    LaunchedEffect(autoAdvance) {
        if (autoAdvance) {
            while (true) {
                delay(2000)
                val nextPage = (pagerState.currentPage + 1) % productImage.size
                pagerState.animateScrollToPage(page = nextPage)
            }
        }
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = if (isEditMode) "Edit Equipment" else "Camera",
                onNavigationClick = {
                    if (isEditMode && hasUnsavedChanges) {
                        showDiscardDialog = true
                    } else {
                        navController.popBackStack()
                    }
                }
            )
        },
        bottomBar = {
            if (userRole == UserRole.ASSISTANT){
                AppButton(
                    onClick = {
                        navController.navigate(Screen.CalendarScreen.route)
                    },
                    buttonText = "BOOK NOW",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = ResponsiveLayout.getHorizontalPadding(),
                            vertical = ResponsiveLayout.getResponsiveSize(12.dp, 16.dp, 20.dp)
                        )
                )
            }
            else{
                if (isEditMode) {
                    // Edit mode: Show Save button
                    AppButton(
                        onClick = {
                            // Save changes
                            hasUnsavedChanges = false
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Changes saved successfully.",
                                    duration = SnackbarDuration.Short
                                )
                            }
                            // Navigate back to read-only view
                            navController.popBackStack()
                        },
                        buttonText = "SAVE",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = ResponsiveLayout.getHorizontalPadding(),
                                vertical = ResponsiveLayout.getResponsiveSize(12.dp, 16.dp, 20.dp)
                            )
                    )
                } else {
                    // Read-only mode: Show Book and Edit (for admin) buttons
                    ActionCard(
                        onEditClick = {
                            if (isAdmin) {
                                navController.navigate(Screen.ProductDescriptionEditScreen.route)
                            }
                        },
                        onDeleteClick = {},
                        onBookClick = {
                            navController.navigate(Screen.CalendarScreen.route)
                        },
                        showEditButton = isAdmin,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = ResponsiveLayout.getHorizontalPadding(),
                                vertical = ResponsiveLayout.getResponsiveSize(12.dp, 16.dp, 20.dp)
                            )
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        containerColor = whiteColor
    ) { paddingValues ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(ResponsiveLayout.getHorizontalPadding())
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(ResponsiveLayout.getResponsiveSize(12.dp, 16.dp, 20.dp))
        ) {
            Spacer(modifier = Modifier.height(ResponsiveLayout.getResponsiveSize(20.dp, 24.dp, 28.dp)))
            ProductCarousel(
                images = productImage,
                pageInteractionSource = pagerInteractionSource,
                pagerState = pagerState
            )

            if (isEditMode) {
                // Edit mode: Show editable fields
                EditableProductDescriptionCard(
                    equipmentName = equipmentName,
                    onEquipmentNameChange = { equipmentName = it; hasUnsavedChanges = true },
                    equipmentBrand = equipmentBrand,
                    onEquipmentBrandChange = { equipmentBrand = it; hasUnsavedChanges = true },
                    equipmentModel = equipmentModel,
                    onEquipmentModelChange = { equipmentModel = it; hasUnsavedChanges = true },
                    equipmentStatus = equipmentStatus,
                    onEquipmentStatusChange = { equipmentStatus = it; hasUnsavedChanges = true },
                    assignedTo = assignedTo,
                    onAssignedToChange = { assignedTo = it; hasUnsavedChanges = true },
                    equipmentCategory = equipmentCategory,
                    onEquipmentCategoryChange = { equipmentCategory = it; hasUnsavedChanges = true },
                    equipmentLocation = equipmentLocation,
                    onEquipmentLocationChange = { equipmentLocation = it; hasUnsavedChanges = true },
                    equipmentTiming = equipmentTiming,
                    onEquipmentTimingChange = { equipmentTiming = it; hasUnsavedChanges = true },
                    statusOptions = statusOptions,
                    assignedToOptions = assignedToOptions,
                    categoryOptions = categoryOptions
                )
            } else {
                // Read-only mode: Show normal cards
                ProductDescriptionCard(modifier = Modifier)
            }

            InChargeCard()
            AdditionalInfoCard()
            UseCard()
            
            // Admin-only extra sections
            if (isAdmin && !isEditMode) {
                // Assignment Section
                AdminAssignmentSection(
                    assignedUser = assignedUser,
                    assignedRole = assignedRole,
                    onAssignedUserChange = { assignedUser = it },
                    onAssignedRoleChange = { assignedRole = it },
                    userOptions = listOf("Not Assigned", "Dr. Ravi Kumar", "Lab Assistant", "Dr. Smith", "Jane Doe"),
                    roleOptions = listOf("Staff", "Assistant")
                )
                
                // Equipment Tags
                AdminEquipmentTagsSection(
                    currentTag = equipmentTag,
                    onTagChange = { equipmentTag = it },
                    tagOptions = listOf("Available", "Under Maintenance", "Offline")
                )
                
                // Additional Information (Expandable)
                AdminAdditionalInformationCard(
                    expanded = showAdditionalInfo,
                    onExpandedChange = { showAdditionalInfo = it },
                    usageHistory = usageHistory,
                    latestBookings = latestBookings
                )
                
                // General Info Section
                AdminGeneralInfoSection(
                    expanded = showGeneralInfo,
                    onExpandedChange = { showGeneralInfo = it },
                    description = "State-of-the-art photography studio equipped with professional cameras, lighting equipment, and editing workstations.",
                    brand = equipmentBrand,
                    model = equipmentModel,
                    purchaseDate = "2024-01-15",
                    cost = "$5,000",
                    vendor = "Canon Inc.",
                    warrantyExpiry = "2027-01-15"
                )
                
                // Location & Assignment Section
                AdminLocationAssignmentSection(
                    expanded = showLocationAssignment,
                    onExpandedChange = { showLocationAssignment = it },
                    building = "Building A",
                    labRoom = "Lab 101",
                    assignedUser = assignedUser,
                    assignedRole = assignedRole,
                    assignmentHistory = assignmentHistory
                )
                
                // Bookings Section
                AdminBookingsSection(
                    expanded = showBookings,
                    onExpandedChange = { showBookings = it },
                    upcomingBookings = upcomingBookings,
                    pastBookings = pastBookings
                )
                
                // Maintenance Section
                AdminMaintenanceSection(
                    expanded = showMaintenance,
                    onExpandedChange = { showMaintenance = it },
                    maintenanceHistory = maintenanceHistory,
                    scheduledMaintenance = scheduledMaintenance,
                    overdueTasks = overdueTasks
                )
                
                // Delete Equipment Button
                AdminDeleteEquipmentButton(
                    onDeleteClick = { showDeleteDialog = true }
                )
            }
            
            // Add bottom padding for booking button
            Spacer(modifier = Modifier.height(ResponsiveLayout.getResponsiveSize(80.dp, 90.dp, 100.dp)))
        }
    }
    
    // Discard changes confirmation dialog
    if (showDiscardDialog) {
        DiscardChangesDialog(
            onSave = {
                showDiscardDialog = false
                hasUnsavedChanges = false
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Changes saved successfully.",
                        duration = SnackbarDuration.Short
                    )
                }
                navController.popBackStack()
            },
            onDiscard = {
                showDiscardDialog = false
                hasUnsavedChanges = false
                navController.popBackStack()
            },
            onCancel = {
                showDiscardDialog = false
            }
        )
    }
    
    // Delete equipment confirmation dialog
    if (showDeleteDialog) {
        DeleteEquipmentDialog(
            onConfirm = {
                showDeleteDialog = false
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Equipment deleted successfully.",
                        duration = SnackbarDuration.Short
                    )
                }
                // Navigate back after deletion
                navController.popBackStack()
            },
            onCancel = {
                showDeleteDialog = false
            }
        )
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun ProductCarousel(
    images : List<Int>,
    imageDescription : String = "Equipment images",
    contentScale: ContentScale = ContentScale.Crop,
    pagerState: PagerState,
    pageInteractionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    backgroundColor : Color = onSurfaceVariant,
    inactiveColor : Color = Color.DarkGray,
    activeColor : Color = primaryColor,
    indicatorShape: Shape = CircleShape,
    indicatorSize : Dp = ResponsiveLayout.getResponsiveSize(6.dp, 8.dp, 10.dp),
    isFav : Boolean = false
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(ResponsiveLayout.getResponsiveSize(200.dp, 240.dp, 280.dp)),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(ResponsiveLayout.getResponsivePadding(16.dp, 20.dp, 24.dp))
        ) {
            HorizontalPager(
                state = pagerState, // Use the passed pagerState
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clickable(
                        // interactionSource allows observing and controlling the visual state of the pager (e.g., pressed, hovered).
                        interactionSource = pageInteractionSource,
                        // indication provides visual feedback for interactions (e.g., ripples on click). LocalIndication.current uses the default indication provided by the theme.
                        indication = LocalIndication.current
                    ){}
            ) {
                    page ->
                AsyncImage(
                    model = images[page],
                    contentDescription = imageDescription,
                    contentScale = contentScale,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(pxToDp(16)))
            HorizontalPagerIndicator(
                pagerState = pagerState, // Use the passed pagerState
                pageCount = images.size,
                inactiveColor = inactiveColor,
                activeColor = activeColor,
                indicatorShape = indicatorShape,
                modifier = Modifier
                    .size(indicatorSize)
            )
            Spacer(modifier = Modifier.height(pxToDp(3)))
        }

    }
}

@Composable
fun ProductDescriptionCard(
    modifier: Modifier,
    shape: Shape = RectangleShape,
    cardContainerColor: Color = onSurfaceVariant
) {
    val facilitiesViewModel : FacilitiesViewModel = koinViewModel ()
    val facilitiesList = facilitiesViewModel.facilitiesState
    var isFavorite by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(pxToDp(190)),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = cardContainerColor
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(pxToDp(16))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(pxToDp(16))
            ) {
                CustomLabel(
                    header = "Canon EOS R50 V",
                    headerColor = onSurfaceColor,
                    fontSize = 16.sp,
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.height(pxToDp(3)))

                when(facilitiesList){
                    is UiState.Loading -> {
                        Text("Loading facilities")
                    }
                    is UiState.Error -> {
                        // Fallback to demo facility on error
                        Log.e("ProductDescriptionCard", "Error loading facilities", facilitiesList.exception)
                        Log.w("ProductDescriptionCard", "Using demo facility fallback")
                        val demoFacility = getDemoFacility()
                        InfoRow(label = "Brand", value = "Canon")
                        InfoRow(label = "Model", value = "EOS R5 Mark II")
                        InfoRow(label = "Location", value = demoFacility.location)
                        InfoRow(label = "Timing", value = demoFacility.timings)
                    }
                    is UiState.Success -> {
                        // Use real facility if available, otherwise fallback to demo facility
                        val currentFacility = if (facilitiesList.data.isNotEmpty()) {
                            facilitiesList.data.firstOrNull()
                        } else {
                            getDemoFacility()
                        }

                        if (currentFacility != null){
                            InfoRow(label = "Brand", value = "Canon")
                            InfoRow(label = "Model", value = "EOS R5 Mark II")
                            InfoRow(label = "Location", value = currentFacility.location)
                            InfoRow(label = "Timing", value = currentFacility.timings)
                        }
                    }
                }
            }
            AppCategoryIcon(
                painter = painterResource(R.drawable.ic_favorite),
                iconDescription = "Save Icon",
                tint = if (isFavorite) primaryColor else onSurfaceColor,
                modifier = Modifier
                    .padding(pxToDp(2))
                    .align(Alignment.TopEnd)
                    .clickable { isFavorite = !isFavorite },
                iconSize = pxToDp(20),
            )
        }
    }
}

    @Composable
    fun InfoRow(label: String, value: String) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(pxToDp(12)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomLabel(
                header = label,
                modifier = Modifier.weight(0.2f),
                headerColor = onSurfaceColor.copy(alpha = 0.5f),
                fontSize = 14.sp
            )
            CustomLabel(
                header = value,
                modifier = Modifier.weight(1f),
                headerColor = onSurfaceColor.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
        }
    }


@Composable
fun InChargeCard(
    modifier: Modifier = Modifier,
    containerColor : Color = onSurfaceVariant,
    shape: Shape = RectangleShape
) {
    val facilitiesViewModel : FacilitiesViewModel = koinViewModel()
    val facilitiesList = facilitiesViewModel.facilitiesState

    var expanded by remember { mutableStateOf(true) }
    val iconAlignment = if (expanded) Alignment.TopEnd else Alignment.CenterEnd

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = shape
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(pxToDp(16))
        ) {
            if (expanded) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(pxToDp(12)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    when(facilitiesList){
                        is UiState.Loading -> Text("Loading facilities")
                        is UiState.Error -> {
                            // Fallback to demo facility on error
                            Log.e("InChargeCard", "Error loading facilities", facilitiesList.exception)
                            Log.w("InChargeCard", "Using demo facility fallback")
                            val demoFacility = getDemoFacility()
                            CustomLabel(
                                header = "InCharge",
                                headerColor = onSurfaceColor.copy(0.9f),
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(pxToDp(5)))
                            InChargeRow(label = "Prof.", name = demoFacility.prof_incharge)
                            InChargeRow(
                                label = "Asst.",
                                name = demoFacility.lab_incharge,
                                icons = listOf(R.drawable.ic_mail, R.drawable.ic_call),
                                email = demoFacility.lab_incharge_email,
                                phone = demoFacility.lab_incharge_phone
                            )
                        }
                        is UiState.Success -> {
                            // Use real facility if available, otherwise fallback to demo facility
                            val currentFacility = if (facilitiesList.data.isNotEmpty()) {
                                facilitiesList.data.firstOrNull()
                            } else {
                                getDemoFacility()
                            }
                            if (currentFacility != null){
                                CustomLabel(
                                    header = "InCharge",
                                    headerColor = onSurfaceColor.copy(0.9f),
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.height(pxToDp(5)))
                                InChargeRow(label = "Prof.", name = currentFacility.prof_incharge)
                                InChargeRow(
                                    label = "Asst.",
                                    name = currentFacility.lab_incharge,
                                    icons = listOf(R.drawable.ic_mail, R.drawable.ic_call),
                                    email = currentFacility.lab_incharge_email,
                                    phone = currentFacility.lab_incharge_phone
                                )
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    CustomLabel(
                        header = "InCharge",
                        headerColor = onSurfaceColor.copy(0.9f),
                        fontSize = 16.sp
                    )
                }
            }

            AppCategoryIcon(
                painter = painterResource(
                    if (expanded) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down
                ),
                iconDescription = "Expand Icon",
                tint = onSurfaceColor,
                iconSize = pxToDp(20),
                modifier = Modifier.align(iconAlignment).padding(pxToDp(4))
            )
        }
    }
}


@Composable
fun InChargeRow(
    label: String,
    name: String,
    icons: List<Int> = listOf(R.drawable.ic_mail),
    email: String? = null,
    phone: String? = null
) {
    val context = LocalContext.current
    
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        CustomLabel(
            header = label,
            headerColor = onSurfaceColor.copy(alpha = 0.5f),
            fontSize = 14.sp,
            modifier = Modifier.weight(0.2f)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(pxToDp(16)),
            modifier = Modifier
                .weight(0.9f)
        ) {
            CustomLabel(
                header = name,
                headerColor = onSurfaceColor.copy(alpha = 0.8f),
                fontSize = 14.sp,
                modifier = Modifier.padding(pxToDp(10))
            )

            icons.forEach { iconRes ->
                val adjustedIconSize = if (iconRes == R.drawable.ic_call) pxToDp(16) else pxToDp(20)
                AppCircularIcon(
                    painter = painterResource(iconRes),
                    boxSize = pxToDp(28),
                    iconPadding = pxToDp(4),
                    iconSize = adjustedIconSize,
                    tint = primaryColor,
                    boxColor = circularBoxColor,
                    onClick = {
                        when (iconRes) {
                            R.drawable.ic_mail -> {
                                email?.let { emailAddress ->
                                    // Copy email to clipboard
                                    val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Email", emailAddress)
                                    clipboard.setPrimaryClip(clip)
                                    
                                    // Open mail app with email in 'To' field
                                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                                        data = Uri.parse("mailto:$emailAddress")
                                    }
                                    context.startActivity(intent)
                                }
                            }
                            R.drawable.ic_call -> {
                                phone?.let { phoneNumber ->
                                    // Copy phone to clipboard
                                    val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Phone", phoneNumber)
                                    clipboard.setPrimaryClip(clip)
                                    
                                    // Open dialer app with number on keypad
                                    val intent = Intent(Intent.ACTION_DIAL).apply {
                                        data = Uri.parse("tel:$phoneNumber")
                                    }
                                    context.startActivity(intent)
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun AdditionalInfoCard(
    modifier: Modifier = Modifier,
    containerColor : Color = onSurfaceVariant
) {
    val facilitiesViewModel : FacilitiesViewModel = koinViewModel()
    val facilitiesList = facilitiesViewModel.facilitiesState

    var expanded by remember { mutableStateOf(true) }
    val iconAlignment = if (expanded) Alignment.TopEnd else Alignment.CenterEnd

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(pxToDp(16))
        ) {
            if (expanded) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(pxToDp(12)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    when(facilitiesList){
                        is UiState.Loading -> Text("Loading facilities")
                        is UiState.Error -> {
                            // Fallback to demo facility on error
                            Log.e("AdditionalInfoCard", "Error loading facilities", facilitiesList.exception)
                            Log.w("AdditionalInfoCard", "Using demo facility fallback")
                            val demoFacility = getDemoFacility()
                            CustomLabel(
                                header = "Additional Information",
                                headerColor = onSurfaceColor.copy(0.9f),
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(pxToDp(5)))
                            CustomLabel(
                                header = demoFacility.description ?: "No Description found"
                            )
                        }
                        is UiState.Success -> {
                            // Use real facility if available, otherwise fallback to demo facility
                            val currentFacility = if (facilitiesList.data.isNotEmpty()) {
                                facilitiesList.data.firstOrNull()
                            } else {
                                getDemoFacility()
                            }
                            if (currentFacility != null){
                                CustomLabel(
                                    header = "Additional Information",
                                    headerColor = onSurfaceColor.copy(0.9f),
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.height(pxToDp(5)))
                                CustomLabel(
                                    header = currentFacility.description ?: "No Description found"
                                )
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    CustomLabel(
                        header = "Additional Information",
                        headerColor = onSurfaceColor.copy(0.9f),
                        fontSize = 16.sp
                    )
                }
            }

            AppCategoryIcon(
                painter = painterResource(
                    if (expanded) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down
                ),
                iconDescription = "Expand Icon",
                tint = onSurfaceColor,
                iconSize = pxToDp(20),
                modifier = Modifier.align(iconAlignment).padding(pxToDp(4))
            )
        }
    }
}


@Composable
fun UseCard(
    modifier: Modifier = Modifier,
    containerColor: Color = onSurfaceVariant
) {
    var expanded by remember { mutableStateOf(true) }
    val iconAlignment = if (expanded) Alignment.TopEnd else Alignment.CenterEnd

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(pxToDp(16))
        ) {
            if (expanded) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(pxToDp(8)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CustomLabel(
                        header = "How to use",
                        headerColor = onSurfaceColor.copy(0.9f),
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(pxToDp(5)))
                    // Add the actual usage instructions here
                    Text("No usage instructions available") // Placeholder
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    CustomLabel(
                        header = "How to use",
                        headerColor = onSurfaceColor.copy(0.9f),
                        fontSize = 16.sp
                    )
                }
            }

            AppCategoryIcon(
                painter = painterResource(
                    if (expanded) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down
                ),
                iconDescription = "Expand Icon",
                tint = onSurfaceColor,
                iconSize = pxToDp(20),
                modifier = Modifier.align(iconAlignment).padding(pxToDp(4))
            )
        }
    }
}

@Composable
fun ActionCard(
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onBookClick: () -> Unit,
    showEditButton: Boolean = false,
    modifier: Modifier
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = whiteColor
        )
    ) {
        Column(
            modifier = Modifier.padding(pxToDp(12)),
            verticalArrangement = Arrangement.spacedBy(pxToDp(12))
        ) {
            AppButton(
                onClick = onBookClick,
                buttonText = "BOOK",
                modifier = Modifier.fillMaxWidth()
            )
            if (showEditButton) {
                AppButton(
                    onClick = onEditClick,
                    buttonText = "EDIT",
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = cardColor,
                    contentColor = onSurfaceColor
                )
            }
        }
    }
}


// Demo/fallback facility - used when Supabase returns empty or on error
@Composable
private fun getDemoFacility(): Facilities {
    return remember {
        Facilities(
            id = 1,
            name = "Photo Studio",
            department_id = 1,
            type = "Studio",
            location = "IDC, Photo Studio",
            timings = "9:00 AM - 6:00 PM",
            lab_incharge = "Akash Kumar Swami",
            lab_incharge_phone = "+91 9876543210",
            lab_incharge_email = "akash.swami@example.com",
            prof_incharge = "Sumant Rao",
            prof_incharge_email = "sumant.rao@example.com",
            description = "State-of-the-art photography studio equipped with professional cameras, lighting equipment, and editing workstations. Available for student projects and research activities.",
            createdAt = "2024-01-01T00:00:00Z",
            branch_id = 1
        )
    }
}

@Composable
fun EditableProductDescriptionCard(
    equipmentName: String,
    onEquipmentNameChange: (String) -> Unit,
    equipmentBrand: String,
    onEquipmentBrandChange: (String) -> Unit,
    equipmentModel: String,
    onEquipmentModelChange: (String) -> Unit,
    equipmentStatus: String,
    onEquipmentStatusChange: (String) -> Unit,
    assignedTo: String,
    onAssignedToChange: (String) -> Unit,
    equipmentCategory: String,
    onEquipmentCategoryChange: (String) -> Unit,
    equipmentLocation: String,
    onEquipmentLocationChange: (String) -> Unit,
    equipmentTiming: String,
    onEquipmentTimingChange: (String) -> Unit,
    statusOptions: List<String>,
    assignedToOptions: List<String>,
    categoryOptions: List<String>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(pxToDp(400)),
        colors = CardDefaults.cardColors(
            containerColor = onSurfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(pxToDp(16)),
            verticalArrangement = Arrangement.spacedBy(pxToDp(16))
        ) {
            CustomLabel(
                header = "Equipment Details",
                headerColor = onSurfaceColor,
                fontSize = 16.sp,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.height(pxToDp(8)))
            
            AppTextField(
                value = equipmentName,
                onValueChange = onEquipmentNameChange,
                placeholder = "Equipment Name"
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(pxToDp(12))
            ) {
                AppTextField(
                    value = equipmentBrand,
                    onValueChange = onEquipmentBrandChange,
                    placeholder = "Brand",
                    modifier = Modifier.weight(1f)
                )
                AppTextField(
                    value = equipmentModel,
                    onValueChange = onEquipmentModelChange,
                    placeholder = "Model",
                    modifier = Modifier.weight(1f)
                )
            }
            
            AppDropDownTextField(
                value = equipmentStatus,
                items = statusOptions,
                onValueChange = onEquipmentStatusChange,
                placeholder = "Status"
            )
            
            AppDropDownTextField(
                value = assignedTo,
                items = assignedToOptions,
                onValueChange = onAssignedToChange,
                placeholder = "Assigned To"
            )
            
            AppDropDownTextField(
                value = equipmentCategory,
                items = categoryOptions,
                onValueChange = onEquipmentCategoryChange,
                placeholder = "Category"
            )
            
            AppTextField(
                value = equipmentLocation,
                onValueChange = onEquipmentLocationChange,
                placeholder = "Location"
            )
            
            AppTextField(
                value = equipmentTiming,
                onValueChange = onEquipmentTimingChange,
                placeholder = "Timing"
            )
        }
    }
}

@Composable
fun AdminEquipmentMetadataCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = onSurfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(pxToDp(16)),
            verticalArrangement = Arrangement.spacedBy(pxToDp(12))
        ) {
            CustomLabel(
                header = "Equipment Metadata",
                headerColor = onSurfaceColor.copy(0.9f),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(pxToDp(5)))
            InfoRow(label = "Equipment ID", value = "EQ-2024-001")
            InfoRow(label = "Serial Number", value = "SN-123456789")
            InfoRow(label = "Purchase Date", value = "2024-01-15")
            InfoRow(label = "Warranty Expiry", value = "2027-01-15")
            InfoRow(label = "Last Maintenance", value = "2024-11-15")
        }
    }
}

@Composable
fun AdminAssignmentHistoryCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = onSurfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(pxToDp(16)),
            verticalArrangement = Arrangement.spacedBy(pxToDp(12))
        ) {
            CustomLabel(
                header = "Assignment History",
                headerColor = onSurfaceColor.copy(0.9f),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(pxToDp(5)))
            CustomLabel(
                header = "• Assigned to Dr. Ravi Kumar (2024-01-15 to 2024-06-15)",
                headerColor = onSurfaceColor.copy(0.7f),
                fontSize = 14.sp
            )
            CustomLabel(
                header = "• Assigned to Lab Assistant (2024-06-16 to Present)",
                headerColor = onSurfaceColor.copy(0.7f),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun DiscardChangesDialog(
    onSave: () -> Unit,
    onDiscard: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = {
            CustomLabel(
                header = "Unsaved Changes",
                fontSize = ResponsiveLayout.getResponsiveFontSize(18.sp, 20.sp, 22.sp),
                headerColor = onSurfaceColor
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(pxToDp(12))
            ) {
                CustomLabel(
                    header = "Discard changes or save before exiting?",
                    fontSize = ResponsiveLayout.getResponsiveFontSize(14.sp, 16.sp, 18.sp),
                    headerColor = onSurfaceColor.copy(0.8f)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(pxToDp(8))
                ) {
                    AppButton(
                        buttonText = "Save",
                        onClick = onSave,
                        modifier = Modifier.weight(1f)
                    )
                    AppButton(
                        buttonText = "Discard",
                        onClick = onDiscard,
                        containerColor = errorColor,
                        modifier = Modifier.weight(1f)
                    )
                    AppButton(
                        buttonText = "Cancel",
                        onClick = onCancel,
                        containerColor = cardColor,
                        contentColor = onSurfaceColor,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {},
        dismissButton = {},
        containerColor = whiteColor
    )
}

@Composable
fun AdminAssignmentSection(
    assignedUser: String,
    assignedRole: String,
    onAssignedUserChange: (String) -> Unit,
    onAssignedRoleChange: (String) -> Unit,
    userOptions: List<String>,
    roleOptions: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = onSurfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(pxToDp(16)),
            verticalArrangement = Arrangement.spacedBy(pxToDp(12))
        ) {
            CustomLabel(
                header = "Assignment",
                headerColor = onSurfaceColor.copy(0.9f),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(pxToDp(5)))
            
            if (assignedUser == "Not Assigned") {
                CustomLabel(
                    header = "Equipment is currently unassigned",
                    headerColor = onSurfaceColor.copy(0.7f),
                    fontSize = 14.sp
                )
            } else {
                InfoRow(label = "Assigned To", value = assignedUser)
                InfoRow(label = "Role", value = assignedRole)
            }
            
            Spacer(modifier = Modifier.height(pxToDp(8)))
            Divider(thickness = pxToDp(1), color = cardColor)
            Spacer(modifier = Modifier.height(pxToDp(8)))
            
            CustomLabel(
                header = "Reassign Equipment",
                headerColor = onSurfaceColor.copy(0.9f),
                fontSize = 14.sp
            )
            
            AppDropDownTextField(
                value = assignedUser,
                items = userOptions,
                onValueChange = onAssignedUserChange,
                placeholder = "Select User"
            )
            
            if (assignedUser != "Not Assigned") {
                AppDropDownTextField(
                    value = assignedRole,
                    items = roleOptions,
                    onValueChange = onAssignedRoleChange,
                    placeholder = "Select Role"
                )
            }
        }
    }
}

@Composable
fun AdminEquipmentTagsSection(
    currentTag: String,
    onTagChange: (String) -> Unit,
    tagOptions: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = onSurfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(pxToDp(16)),
            verticalArrangement = Arrangement.spacedBy(pxToDp(12))
        ) {
            CustomLabel(
                header = "Equipment Status Tag",
                headerColor = onSurfaceColor.copy(0.9f),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(pxToDp(5)))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(pxToDp(8))
            ) {
                tagOptions.forEach { tag ->
                    val isSelected = currentTag == tag
                    val tagColor = when (tag) {
                        "Available" -> Color(0xFF26BB64)
                        "Under Maintenance" -> Color(0xFFE67824)
                        "Offline" -> Color(0xFFE64646)
                        else -> primaryColor
                    }
                    
                    Box(
                        modifier = Modifier
                            .clickable { onTagChange(tag) }
                            .background(
                                color = if (isSelected) tagColor.copy(alpha = 0.2f) else Color.Transparent,
                                shape = RoundedCornerShape(pxToDp(20))
                            )
                            .border(
                                width = pxToDp(1),
                                color = if (isSelected) tagColor else chipColor,
                                shape = RoundedCornerShape(pxToDp(20))
                            )
                            .padding(
                                horizontal = ResponsiveLayout.getResponsivePadding(12.dp, 14.dp, 16.dp),
                                vertical = ResponsiveLayout.getResponsivePadding(6.dp, 8.dp, 10.dp)
                            )
                    ) {
                        CustomLabel(
                            header = tag,
                            headerColor = if (isSelected) tagColor else onSurfaceColor,
                            fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 13.sp, 14.sp),
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AdminAdditionalInformationCard(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    usageHistory: List<String>,
    latestBookings: List<String>
) {
    val iconAlignment = if (expanded) Alignment.TopEnd else Alignment.CenterEnd
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onExpandedChange(!expanded) },
        colors = CardDefaults.cardColors(containerColor = onSurfaceVariant)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(pxToDp(16))
        ) {
            if (expanded) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(pxToDp(12)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CustomLabel(
                        header = "Additional Information",
                        headerColor = onSurfaceColor.copy(0.9f),
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(pxToDp(5)))
                    
                    CustomLabel(
                        header = "Usage History",
                        headerColor = onSurfaceColor.copy(0.9f),
                        fontSize = 14.sp
                    )
                    usageHistory.take(3).forEach { entry ->
                        CustomLabel(
                            header = "• $entry",
                            headerColor = onSurfaceColor.copy(0.7f),
                            fontSize = 13.sp,
                            modifier = Modifier.padding(start = pxToDp(8))
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(pxToDp(8)))
                    Divider(thickness = pxToDp(1), color = cardColor)
                    Spacer(modifier = Modifier.height(pxToDp(8)))
                    
                    CustomLabel(
                        header = "Latest Bookings",
                        headerColor = onSurfaceColor.copy(0.9f),
                        fontSize = 14.sp
                    )
                    latestBookings.take(3).forEach { booking ->
                        CustomLabel(
                            header = "• $booking",
                            headerColor = onSurfaceColor.copy(0.7f),
                            fontSize = 13.sp,
                            modifier = Modifier.padding(start = pxToDp(8))
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    CustomLabel(
                        header = "Additional Information",
                        headerColor = onSurfaceColor.copy(0.9f),
                        fontSize = 16.sp
                    )
                }
            }
            
            AppCategoryIcon(
                painter = painterResource(
                    if (expanded) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down
                ),
                iconDescription = "Expand Icon",
                tint = onSurfaceColor,
                iconSize = pxToDp(20),
                modifier = Modifier.align(iconAlignment).padding(pxToDp(4))
            )
        }
    }
}

@Composable
fun AdminGeneralInfoSection(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    description: String,
    brand: String,
    model: String,
    purchaseDate: String,
    cost: String,
    vendor: String,
    warrantyExpiry: String
) {
    val iconAlignment = if (expanded) Alignment.TopEnd else Alignment.CenterEnd
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onExpandedChange(!expanded) },
        colors = CardDefaults.cardColors(containerColor = onSurfaceVariant)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(pxToDp(16))
        ) {
            if (expanded) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(pxToDp(12)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CustomLabel(
                        header = "General Information",
                        headerColor = onSurfaceColor.copy(0.9f),
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(pxToDp(5)))
                    
                    CustomLabel(
                        header = "Description",
                        headerColor = onSurfaceColor.copy(0.9f),
                        fontSize = 14.sp
                    )
                    CustomLabel(
                        header = description,
                        headerColor = onSurfaceColor.copy(0.7f),
                        fontSize = 13.sp
                    )
                    
                    Spacer(modifier = Modifier.height(pxToDp(8)))
                    Divider(thickness = pxToDp(1), color = cardColor)
                    Spacer(modifier = Modifier.height(pxToDp(8)))
                    
                    InfoRow(label = "Brand", value = brand)
                    InfoRow(label = "Model", value = model)
                    InfoRow(label = "Purchase Date", value = purchaseDate)
                    InfoRow(label = "Cost", value = cost)
                    InfoRow(label = "Vendor", value = vendor)
                    InfoRow(label = "Warranty Expiry", value = warrantyExpiry)
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    CustomLabel(
                        header = "General Information",
                        headerColor = onSurfaceColor.copy(0.9f),
                        fontSize = 16.sp
                    )
                }
            }
            
            AppCategoryIcon(
                painter = painterResource(
                    if (expanded) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down
                ),
                iconDescription = "Expand Icon",
                tint = onSurfaceColor,
                iconSize = pxToDp(20),
                modifier = Modifier.align(iconAlignment).padding(pxToDp(4))
            )
        }
    }
}

@Composable
fun AdminLocationAssignmentSection(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    building: String,
    labRoom: String,
    assignedUser: String,
    assignedRole: String,
    assignmentHistory: List<String>
) {
    val iconAlignment = if (expanded) Alignment.TopEnd else Alignment.CenterEnd
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onExpandedChange(!expanded) },
        colors = CardDefaults.cardColors(containerColor = onSurfaceVariant)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(pxToDp(16))
        ) {
            if (expanded) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(pxToDp(12)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CustomLabel(
                        header = "Location & Assignment",
                        headerColor = onSurfaceColor.copy(0.9f),
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(pxToDp(5)))
                    
                    InfoRow(label = "Building", value = building)
                    InfoRow(label = "Lab / Room", value = labRoom)
                    InfoRow(label = "Assigned User", value = assignedUser)
                    InfoRow(label = "Assigned Role", value = assignedRole)
                    
                    Spacer(modifier = Modifier.height(pxToDp(8)))
                    Divider(thickness = pxToDp(1), color = cardColor)
                    Spacer(modifier = Modifier.height(pxToDp(8)))
                    
                    CustomLabel(
                        header = "Assignment History",
                        headerColor = onSurfaceColor.copy(0.9f),
                        fontSize = 14.sp
                    )
                    assignmentHistory.forEach { history ->
                        CustomLabel(
                            header = "• $history",
                            headerColor = onSurfaceColor.copy(0.7f),
                            fontSize = 13.sp,
                            modifier = Modifier.padding(start = pxToDp(8))
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    CustomLabel(
                        header = "Location & Assignment",
                        headerColor = onSurfaceColor.copy(0.9f),
                        fontSize = 16.sp
                    )
                }
            }
            
            AppCategoryIcon(
                painter = painterResource(
                    if (expanded) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down
                ),
                iconDescription = "Expand Icon",
                tint = onSurfaceColor,
                iconSize = pxToDp(20),
                modifier = Modifier.align(iconAlignment).padding(pxToDp(4))
            )
        }
    }
}

@Composable
fun AdminBookingsSection(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    upcomingBookings: List<String>,
    pastBookings: List<String>
) {
    val iconAlignment = if (expanded) Alignment.TopEnd else Alignment.CenterEnd
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onExpandedChange(!expanded) },
        colors = CardDefaults.cardColors(containerColor = onSurfaceVariant)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(pxToDp(16))
        ) {
            if (expanded) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(pxToDp(12)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CustomLabel(
                        header = "Bookings",
                        headerColor = onSurfaceColor.copy(0.9f),
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(pxToDp(5)))
                    
                    CustomLabel(
                        header = "Upcoming Bookings",
                        headerColor = onSurfaceColor.copy(0.9f),
                        fontSize = 14.sp
                    )
                    if (upcomingBookings.isEmpty()) {
                        CustomLabel(
                            header = "No upcoming bookings",
                            headerColor = onSurfaceColor.copy(0.6f),
                            fontSize = 13.sp
                        )
                    } else {
                        upcomingBookings.forEach { booking ->
                            CustomLabel(
                                header = "• $booking",
                                headerColor = onSurfaceColor.copy(0.7f),
                                fontSize = 13.sp,
                                modifier = Modifier.padding(start = pxToDp(8))
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(pxToDp(8)))
                    Divider(thickness = pxToDp(1), color = cardColor)
                    Spacer(modifier = Modifier.height(pxToDp(8)))
                    
                    CustomLabel(
                        header = "Past Bookings",
                        headerColor = onSurfaceColor.copy(0.9f),
                        fontSize = 14.sp
                    )
                    if (pastBookings.isEmpty()) {
                        CustomLabel(
                            header = "No past bookings",
                            headerColor = onSurfaceColor.copy(0.6f),
                            fontSize = 13.sp
                        )
                    } else {
                        pastBookings.forEach { booking ->
                            CustomLabel(
                                header = "• $booking",
                                headerColor = onSurfaceColor.copy(0.7f),
                                fontSize = 13.sp,
                                modifier = Modifier.padding(start = pxToDp(8))
                            )
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    CustomLabel(
                        header = "Bookings",
                        headerColor = onSurfaceColor.copy(0.9f),
                        fontSize = 16.sp
                    )
                }
            }
            
            AppCategoryIcon(
                painter = painterResource(
                    if (expanded) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down
                ),
                iconDescription = "Expand Icon",
                tint = onSurfaceColor,
                iconSize = pxToDp(20),
                modifier = Modifier.align(iconAlignment).padding(pxToDp(4))
            )
        }
    }
}

@Composable
fun AdminMaintenanceSection(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    maintenanceHistory: List<String>,
    scheduledMaintenance: List<String>,
    overdueTasks: List<String>
) {
    val iconAlignment = if (expanded) Alignment.TopEnd else Alignment.CenterEnd
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onExpandedChange(!expanded) },
        colors = CardDefaults.cardColors(containerColor = onSurfaceVariant)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(pxToDp(16))
        ) {
            if (expanded) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(pxToDp(12)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CustomLabel(
                        header = "Maintenance",
                        headerColor = onSurfaceColor.copy(0.9f),
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(pxToDp(5)))
                    
                    CustomLabel(
                        header = "Maintenance History",
                        headerColor = onSurfaceColor.copy(0.9f),
                        fontSize = 14.sp
                    )
                    if (maintenanceHistory.isEmpty()) {
                        CustomLabel(
                            header = "No maintenance history",
                            headerColor = onSurfaceColor.copy(0.6f),
                            fontSize = 13.sp
                        )
                    } else {
                        maintenanceHistory.forEach { history ->
                            CustomLabel(
                                header = "• $history",
                                headerColor = onSurfaceColor.copy(0.7f),
                                fontSize = 13.sp,
                                modifier = Modifier.padding(start = pxToDp(8))
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(pxToDp(8)))
                    Divider(thickness = pxToDp(1), color = cardColor)
                    Spacer(modifier = Modifier.height(pxToDp(8)))
                    
                    CustomLabel(
                        header = "Scheduled Maintenance",
                        headerColor = onSurfaceColor.copy(0.9f),
                        fontSize = 14.sp
                    )
                    if (scheduledMaintenance.isEmpty()) {
                        CustomLabel(
                            header = "No scheduled maintenance",
                            headerColor = onSurfaceColor.copy(0.6f),
                            fontSize = 13.sp
                        )
                    } else {
                        scheduledMaintenance.forEach { scheduled ->
                            CustomLabel(
                                header = "• $scheduled",
                                headerColor = onSurfaceColor.copy(0.7f),
                                fontSize = 13.sp,
                                modifier = Modifier.padding(start = pxToDp(8))
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(pxToDp(8)))
                    Divider(thickness = pxToDp(1), color = cardColor)
                    Spacer(modifier = Modifier.height(pxToDp(8)))
                    
                    CustomLabel(
                        header = "Overdue Tasks",
                        headerColor = errorColor,
                        fontSize = 14.sp
                    )
                    if (overdueTasks.isEmpty()) {
                        CustomLabel(
                            header = "No overdue tasks",
                            headerColor = onSurfaceColor.copy(0.6f),
                            fontSize = 13.sp
                        )
                    } else {
                        overdueTasks.forEach { overdue ->
                            CustomLabel(
                                header = "• $overdue",
                                headerColor = errorColor.copy(alpha = 0.8f),
                                fontSize = 13.sp,
                                modifier = Modifier.padding(start = pxToDp(8))
                            )
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    CustomLabel(
                        header = "Maintenance",
                        headerColor = onSurfaceColor.copy(0.9f),
                        fontSize = 16.sp
                    )
                }
            }
            
            AppCategoryIcon(
                painter = painterResource(
                    if (expanded) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down
                ),
                iconDescription = "Expand Icon",
                tint = onSurfaceColor,
                iconSize = pxToDp(20),
                modifier = Modifier.align(iconAlignment).padding(pxToDp(4))
            )
        }
    }
}

@Composable
fun AdminDeleteEquipmentButton(
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = onSurfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(pxToDp(16)),
            verticalArrangement = Arrangement.spacedBy(pxToDp(12))
        ) {
            AppButton(
                onClick = onDeleteClick,
                buttonText = "DELETE EQUIPMENT",
                modifier = Modifier.fillMaxWidth(),
                containerColor = errorColor
            )
        }
    }
}

@Composable
fun DeleteEquipmentDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = {
            CustomLabel(
                header = "Delete Equipment",
                fontSize = ResponsiveLayout.getResponsiveFontSize(18.sp, 20.sp, 22.sp),
                headerColor = onSurfaceColor
            )
        },
        text = {
            CustomLabel(
                header = "Are you sure you want to delete this equipment? This action cannot be undone.",
                fontSize = ResponsiveLayout.getResponsiveFontSize(14.sp, 16.sp, 18.sp),
                headerColor = onSurfaceColor.copy(0.8f)
            )
        },
        confirmButton = {
            AppButton(
                buttonText = "Delete",
                onClick = onConfirm,
                containerColor = errorColor
            )
        },
        dismissButton = {
            AppButton(
                buttonText = "Cancel",
                onClick = onCancel,
                containerColor = cardColor,
                contentColor = onSurfaceColor
            )
        },
        containerColor = whiteColor
    )
}

@Preview(showBackground = true)
@Composable
fun ProductCarouselPreview() {
    val productImage = listOf(
        R.drawable.temp,
        R.drawable.temp,
        R.drawable.temp
    )
    val pagerState = rememberPagerState(pageCount = { productImage.size })
////    ProductCarousel(
////        images = productImage,
////        pagerState = pagerState
////    )
//    ProdDescScreen()
////    InChargeCard()
////    ProductDescriptionCard(modifier = Modifier)
//}
////  ProdDescScreen()
////  ProductCarousel(
////      images = productImage,
////      pagerState = pagerState
////  )
////  CollapsingCard()
}
