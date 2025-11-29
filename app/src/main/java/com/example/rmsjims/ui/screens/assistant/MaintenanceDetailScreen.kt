package com.example.rmsjims.ui.screens.assistant

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.rmsjims.R
import com.example.rmsjims.data.model.MaintenanceStatusType
import com.example.rmsjims.data.model.UrgencyType
import com.example.rmsjims.navigation.Screen
import com.example.rmsjims.ui.components.AppButton
import com.example.rmsjims.ui.components.AppCategoryIcon
import com.example.rmsjims.ui.components.CustomLabel
import com.example.rmsjims.ui.components.CustomTitle
import com.example.rmsjims.ui.components.CustomTopBar
import com.example.rmsjims.ui.screens.staff.InChargeRow
import com.example.rmsjims.ui.theme.cardColor
import com.example.rmsjims.ui.theme.onSurfaceColor
import com.example.rmsjims.ui.theme.onSurfaceVariant
import com.example.rmsjims.ui.theme.primaryColor
import com.example.rmsjims.ui.theme.whiteColor
import com.example.rmsjims.util.ResponsiveLayout
import com.example.rmsjims.util.pxToDp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPagerIndicator
import kotlinx.coroutines.delay

@Composable
fun MaintenanceDetailScreen(
    requestId: String,
    navController: NavHostController
) {
    // Placeholder maintenance request data - in real app, fetch by requestId
    val request = MaintenanceRequestData(
        id = requestId,
        equipmentName = "3D Printer XL",
        equipmentId = "EQ-2024-089",
        imageUrl = R.drawable.temp,
        facilityName = "Makerspace",
        location = "Lab C-301",
        status = MaintenanceStatusType.PENDING,
        requestedBy = "Dr. Ravi Kumar",
        requesterLocation = "Computer Science Department",
        requestDate = "2024-01-20",
        urgency = "High",
        description = "Printer head needs replacement. Calibration issues reported. The printer has been experiencing frequent jams and calibration errors.",
        estimatedCost = "₹15,000",
        deadline = "2024-01-25",
        requestType = "Issue",
        summary = "Printer head replacement required"
    )
    
    val machineImages = listOf(
        R.drawable.temp,
        R.drawable.temp,
        R.drawable.temp
    )
    
    val pagerState = rememberPagerState(pageCount = { machineImages.size })
    val pagerInteractionSource = remember { MutableInteractionSource() }
    val pagerIsPressed by pagerInteractionSource.collectIsPressedAsState()
    val pagerIsDragged by pagerState.interactionSource.collectIsDraggedAsState()
    val autoAdvance = !pagerIsDragged && !pagerIsPressed

    LaunchedEffect(autoAdvance) {
        if (autoAdvance) {
            while (true) {
                delay(2000)
                val nextPage = (pagerState.currentPage + 1) % machineImages.size
                pagerState.animateScrollToPage(page = nextPage)
            }
        }
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = request.equipmentName,
                onNavigationClick = {
                    navController.popBackStack()
                },
                navController = navController
            )
        },
        containerColor = whiteColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(ResponsiveLayout.getHorizontalPadding()),
            verticalArrangement = Arrangement.spacedBy(ResponsiveLayout.getResponsiveSize(12.dp, 16.dp, 20.dp))
        ) {
            Spacer(modifier = Modifier.height(ResponsiveLayout.getResponsiveSize(20.dp, 24.dp, 28.dp)))
            
            // Image Carousel
            MaintenanceImageCarousel(
                images = machineImages,
                pagerState = pagerState,
                pageInteractionSource = pagerInteractionSource
            )

            // Equipment Description Card
            MaintenanceEquipmentDescriptionCard(request = request)

            // Requester Info Card
            RequesterInfoCard(request = request)

            // Blue Accent Card
            BlueAccentCard(request = request)

            // Gray Info Card
            GrayInfoCard(request = request)
            
            // InCharge Card
            MaintenanceInChargeCard()
            
            // Additional Info Card
            MaintenanceAdditionalInfoCard(request = request)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MaintenanceImageCarousel(
    images: List<Int>,
    imageDescription: String = "Equipment images",
    contentScale: ContentScale = ContentScale.Crop,
    pagerState: PagerState,
    pageInteractionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    backgroundColor: Color = onSurfaceVariant
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(ResponsiveLayout.getResponsiveSize(200.dp, 240.dp, 280.dp)),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(ResponsiveLayout.getResponsivePadding(16.dp, 20.dp, 24.dp))
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clickable(
                        interactionSource = pageInteractionSource,
                        indication = LocalIndication.current
                    ) {}
            ) { page ->
                AsyncImage(
                    model = images[page],
                    contentDescription = imageDescription,
                    contentScale = contentScale,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(pxToDp(16)))
            HorizontalPagerIndicator(
                pagerState = pagerState,
                pageCount = images.size,
                inactiveColor = Color.DarkGray,
                activeColor = primaryColor,
                indicatorShape = CircleShape,
                modifier = Modifier.size(ResponsiveLayout.getResponsiveSize(6.dp, 8.dp, 10.dp))
            )
            Spacer(modifier = Modifier.height(pxToDp(3)))
        }
    }
}

@Composable
fun MaintenanceEquipmentDescriptionCard(
    request: MaintenanceRequestData,
    shape: Shape = RectangleShape,
    cardContainerColor: Color = onSurfaceVariant
) {
    Card(
        modifier = Modifier
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
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(pxToDp(16))
            ) {
                CustomLabel(
                    header = request.equipmentName,
                    headerColor = onSurfaceColor,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(pxToDp(3)))
                
                MaintenanceInfoRow(label = "Equipment ID", value = request.equipmentId)
                MaintenanceInfoRow(label = "Location", value = request.location)
                MaintenanceInfoRow(label = "Facility", value = request.facilityName)
                MaintenanceInfoRow(label = "Request Date", value = request.requestDate)
            }
        }
    }
}

@Composable
fun RequesterInfoCard(
    request: MaintenanceRequestData,
    modifier: Modifier = Modifier,
    containerColor: Color = onSurfaceVariant,
    shape: Shape = RectangleShape
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = shape
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(pxToDp(16))
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(pxToDp(12)),
                modifier = Modifier.fillMaxWidth()
            ) {
                CustomLabel(
                    header = "Requester Info",
                    headerColor = onSurfaceColor.copy(0.9f),
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(pxToDp(5)))
                MaintenanceInfoRow(label = "Name", value = request.requestedBy)
                MaintenanceInfoRow(label = "Location", value = request.requesterLocation)
            }
        }
    }
}

@Composable
fun BlueAccentCard(
    request: MaintenanceRequestData,
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = primaryColor.copy(alpha = 0.1f)
        ),
        shape = shape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(pxToDp(16)),
            verticalArrangement = Arrangement.spacedBy(pxToDp(12))
        ) {
            CustomLabel(
                header = "#${request.requestType}",
                headerColor = primaryColor,
                fontSize = 14.sp,
                modifier = Modifier
            )
            CustomTitle(
                header = request.summary,
                headerColor = onSurfaceColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLine = 2
            )
            CustomLabel(
                header = request.description,
                headerColor = onSurfaceColor.copy(alpha = 0.7f),
                fontSize = 14.sp,
                modifier = Modifier,
                maxLine = 5
            )
        }
    }
}

@Composable
fun GrayInfoCard(
    request: MaintenanceRequestData,
    modifier: Modifier = Modifier,
    containerColor: Color = onSurfaceVariant,
    shape: Shape = RectangleShape
) {
    val urgencyType = when (request.urgency) {
        "High" -> UrgencyType.HIGH
        "Medium" -> UrgencyType.MEDIUM
        "Low" -> UrgencyType.LOW
        else -> UrgencyType.MEDIUM
    }
    
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = shape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(pxToDp(16)),
            verticalArrangement = Arrangement.spacedBy(pxToDp(16))
        ) {
            // Urgency and Deadline Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Urgency Tag with Flag Icon
                Row(
                    horizontalArrangement = Arrangement.spacedBy(pxToDp(8)),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(
                            color = urgencyType.color.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(pxToDp(12))
                        )
                        .padding(
                            horizontal = pxToDp(12),
                            vertical = pxToDp(8)
                        )
                ) {
                    AppCategoryIcon(
                        painter = painterResource(R.drawable.ic_priority_flag),
                        iconDescription = "Urgency",
                        iconSize = pxToDp(16),
                        tint = urgencyType.color
                    )
                    CustomLabel(
                        header = "Urgency: ${urgencyType.label}",
                        headerColor = urgencyType.color,
                        fontSize = 14.sp
                    )
                }
                
                // Clock Icon with Deadline
                Row(
                    horizontalArrangement = Arrangement.spacedBy(pxToDp(8)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppCategoryIcon(
                        painter = painterResource(R.drawable.ic_assigned_time),
                        iconDescription = "start",
                        iconSize = pxToDp(16),
                        tint = onSurfaceColor.copy(alpha = 0.7f)
                    )
                    CustomLabel(
                        header = request.deadline,
                        headerColor = onSurfaceColor.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                }
            }
            
            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(pxToDp(13))
            ) {
                AppButton(
                    buttonText = "Reject",
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    containerColor = cardColor,
                    contentColor = onSurfaceColor
                )
                AppButton(
                    buttonText = "Approve",
                    onClick = { },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun MaintenanceInChargeCard(
    modifier: Modifier = Modifier,
    containerColor: Color = onSurfaceVariant,
    shape: Shape = RectangleShape
) {
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
                    CustomLabel(
                        header = "InCharge",
                        headerColor = onSurfaceColor.copy(0.9f),
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(pxToDp(5)))
                    InChargeRow(label = "Prof.", name = "Dr. Sumant Rao")
                    InChargeRow(
                        label = "Asst.",
                        name = "Akash Kumar Swami",
                        icons = listOf(R.drawable.ic_mail, R.drawable.ic_call),
                        email = "akash.swami@example.com",
                        phone = "+91 9876543210"
                    )
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
fun MaintenanceAdditionalInfoCard(
    request: MaintenanceRequestData,
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
                    verticalArrangement = Arrangement.spacedBy(pxToDp(12)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CustomLabel(
                        header = "Additional Information",
                        headerColor = onSurfaceColor.copy(0.9f),
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(pxToDp(5)))
                    MaintenanceInfoRow(label = "Estimated Cost", value = request.estimatedCost)
                    MaintenanceInfoRow(label = "Request ID", value = request.id)
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
fun MaintenanceInfoRow(label: String, value: String) {
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

@Preview(showBackground = true)
@Composable
fun MaintenanceDetailScreenPreview() {
    val navController = rememberNavController()
    MaintenanceDetailScreen(requestId = "MAINT-001", navController = navController)
}

