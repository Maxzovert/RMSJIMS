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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.rmsjims.R
import com.example.rmsjims.data.model.MachineStatusType
import com.example.rmsjims.navigation.Screen
import com.example.rmsjims.ui.components.AppButton
import com.example.rmsjims.ui.components.AppCategoryIcon
import com.example.rmsjims.ui.components.AppCircularIcon
import com.example.rmsjims.ui.components.CustomLabel
import com.example.rmsjims.ui.components.CustomTopBar
import com.example.rmsjims.ui.components.EditButton
import com.example.rmsjims.ui.screens.staff.InChargeRow
import com.example.rmsjims.ui.theme.circularBoxColor
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
fun MachineDetailScreen(
    machineId: String,
    navController: NavHostController
) {
    // Placeholder machine data - in real app, fetch by machineId
    var machineStatus by remember { mutableStateOf(MachineStatusType.OPERATIONAL) }
    
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
    
    // Placeholder machine data
    val machine = MachineData(
        id = machineId,
        name = "High-Performance Server",
        statusType = machineStatus,
        location = "Lab A-101",
        imageUrl = R.drawable.temp,
        facilityName = "IDC, Photo Studio",
        lastMaintenance = "2024-01-10",
        nextMaintenance = "2024-02-10",
        brand = "Dell",
        model = "PowerEdge R740"
    )

    Scaffold(
        topBar = {
            CustomTopBar(
                title = machine.name,
                onNavigationClick = {
                    navController.popBackStack()
                }
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
            MachineImageCarousel(
                images = machineImages,
                pagerState = pagerState,
                pageInteractionSource = pagerInteractionSource
            )

            // Machine Description Card
            MachineDescriptionCard(
                machine = machine.copy(statusType = machineStatus),
                onStatusChange = { machineStatus = it }
            )

            // InCharge Card
            InChargeCard()

            // Additional Info Card
            AdditionalInfoCard(machine = machine)
            
            // Maintenance Info Card
            MaintenanceInfoCard(machine = machine)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MachineImageCarousel(
    images: List<Int>,
    imageDescription: String = "Machine images",
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
fun MachineDescriptionCard(
    machine: MachineData,
    onStatusChange: (MachineStatusType) -> Unit,
    shape: Shape = RectangleShape,
    cardContainerColor: Color = onSurfaceVariant
) {
    var showStatusDropdown by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth(),
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
                    header = machine.name,
                    headerColor = onSurfaceColor,
                    fontSize = 16.sp,
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.height(pxToDp(3)))
                
                InfoRow(label = "Brand", value = machine.brand)
                InfoRow(label = "Model", value = machine.model)
                InfoRow(label = "Location", value = machine.location)
                InfoRow(label = "Facility", value = machine.facilityName)
                
                // Editable Status
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(pxToDp(12)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomLabel(
                        header = "Status:",
                        modifier = Modifier.weight(0.2f),
                        headerColor = onSurfaceColor.copy(alpha = 0.5f),
                        fontSize = 14.sp
                    )
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        // Status Badge (Clickable)
                        Box(
                            modifier = Modifier
                                .clickable { showStatusDropdown = true }
                                .background(
                                    color = machine.statusType.color,
                                    shape = RoundedCornerShape(pxToDp(20))
                                )
                                .padding(horizontal = pxToDp(10), vertical = pxToDp(4))
                        ) {
                            CustomLabel(
                                header = machine.statusType.label,
                                fontSize = 14.sp,
                                headerColor = whiteColor
                            )
                        }
                        
                        // Status Dropdown Menu
                        DropdownMenu(
                            expanded = showStatusDropdown,
                            onDismissRequest = { showStatusDropdown = false }
                        ) {
                            MachineStatusType.values().forEach { status ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = status.label,
                                            color = status.color
                                        )
                                    },
                                    onClick = {
                                        onStatusChange(status)
                                        showStatusDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
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
fun AdditionalInfoCard(
    machine: MachineData,
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
                    CustomLabel(
                        header = "High-performance server for research and computation. Suitable for machine learning and data analysis tasks."
                    )
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
fun MaintenanceInfoCard(
    machine: MachineData,
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
                        header = "Maintenance Information",
                        headerColor = onSurfaceColor.copy(0.9f),
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(pxToDp(5)))
                    InfoRow(label = "Last Maintenance", value = machine.lastMaintenance)
                    InfoRow(label = "Next Maintenance", value = machine.nextMaintenance)
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    CustomLabel(
                        header = "Maintenance Information",
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

@Preview(showBackground = true)
@Composable
fun MachineDetailScreenPreview() {
    val navController = rememberNavController()
    MachineDetailScreen(machineId = "EQ-2024-001", navController = navController)
}

