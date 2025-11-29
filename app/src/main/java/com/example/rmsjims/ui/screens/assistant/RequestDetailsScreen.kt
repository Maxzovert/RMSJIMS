package com.example.rmsjims.ui.screens.assistant

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.rmsjims.R
import com.example.rmsjims.data.model.PropertyRequest
import com.example.rmsjims.data.model.PropertyRequestProvider
import com.example.rmsjims.data.model.RequestComment
import com.example.rmsjims.data.model.RequestPriority
import com.example.rmsjims.data.model.RequestStatus
import com.example.rmsjims.ui.components.AppButton
import com.example.rmsjims.ui.components.CustomLabel
import com.example.rmsjims.ui.components.CustomSmallLabel
import com.example.rmsjims.ui.components.CustomTopBar
import com.example.rmsjims.ui.theme.onSurfaceColor
import com.example.rmsjims.ui.theme.onSurfaceVariant
import com.example.rmsjims.ui.theme.primaryColor
import com.example.rmsjims.ui.theme.whiteColor
import com.example.rmsjims.util.ResponsiveLayout
import com.example.rmsjims.util.pxToDp

@Composable
fun RequestDetailsScreen(
    navController: NavHostController,
    requestId: String
) {
    val request = PropertyRequestProvider.sampleRequests().let { group ->
        (group.pendingApprovals + group.newRequests + group.priorityRequests + group.flaggedRequests)
            .firstOrNull { it.id == requestId }
    } ?: placeholderRequest()

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Request Details",
                onNavigationClick = { navController.popBackStack() },
                navController = navController
            )
        },
        containerColor = whiteColor
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(
                horizontal = ResponsiveLayout.getHorizontalPadding(),
                vertical = ResponsiveLayout.getVerticalPadding()
            ),
            verticalArrangement = Arrangement.spacedBy(ResponsiveLayout.getResponsivePadding(16.dp, 20.dp, 24.dp))
        ) {
            item {
                RequestSummaryCard(request = request)
            }

            item {
                RequestInfoSection(request = request)
            }

            item {
                ActionButtonRow(onVerify = {}, onApprove = {}, onReject = {})
            }

            item {
                ConversationHeader(count = request.comments.size)
            }

            items(request.comments) { comment ->
                CommentCard(comment = comment)
            }
        }
    }
}

@Composable
private fun RequestSummaryCard(request: PropertyRequest) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = onSurfaceVariant),
        shape = RoundedCornerShape(pxToDp(12))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = ResponsiveLayout.getResponsivePadding(16.dp, 20.dp, 24.dp),
                    vertical = ResponsiveLayout.getResponsivePadding(18.dp, 22.dp, 26.dp)
                ),
            verticalArrangement = Arrangement.spacedBy(pxToDp(12))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(pxToDp(12)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = request.imageRes),
                    contentDescription = request.machineName,
                    modifier = Modifier
                        .size(ResponsiveLayout.getResponsiveSize(64.dp, 72.dp, 84.dp))
                        .padding(end = pxToDp(4)),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(pxToDp(6))
                ) {
                    CustomLabel(
                        header = request.machineName,
                        headerColor = onSurfaceColor,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = ResponsiveLayout.getResponsiveFontSize(18.sp, 20.sp, 22.sp)
                    )
                    CustomSmallLabel(
                        header = request.department,
                        headerColor = onSurfaceColor.copy(alpha = 0.7f),
                        modifier = Modifier
                    )
                    CustomSmallLabel(
                        header = "Requested Slot: ${request.requestedSlot}",
                        headerColor = primaryColor,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

@Composable
private fun RequestInfoSection(request: PropertyRequest) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = onSurfaceVariant),
        shape = RoundedCornerShape(pxToDp(12))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = ResponsiveLayout.getResponsivePadding(18.dp, 22.dp, 26.dp),
                    vertical = ResponsiveLayout.getResponsivePadding(20.dp, 24.dp, 28.dp)
                ),
            verticalArrangement = Arrangement.spacedBy(pxToDp(10))
        ) {
            CustomLabel(
                header = "Request Information",
                headerColor = onSurfaceColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = ResponsiveLayout.getResponsiveFontSize(16.sp, 18.sp, 20.sp)
            )
            Divider(thickness = pxToDp(1), color = onSurfaceColor.copy(alpha = 0.1f))
            InfoRow(label = "Requested By", value = "${request.requestedBy} (${request.requestedDepartment})")
            InfoRow(label = "Requested Date", value = request.requestedDate)
            InfoRow(label = "Requested Slot", value = request.requestedSlot)
            InfoRow(label = "Priority", value = request.priority.name.replace('_', ' '))
            InfoRow(label = "Status", value = request.status.name.replace('_', ' '))
            CustomSmallLabel(
                header = "Description",
                headerColor = onSurfaceColor.copy(alpha = 0.7f),
                modifier = Modifier
            )
            CustomLabel(
                header = request.description,
                headerColor = onSurfaceColor,
                fontSize = ResponsiveLayout.getResponsiveFontSize(13.sp, 14.sp, 15.sp)
            )
        }
    }
}

//@Composable
//private fun InfoRow(label: String, value: String) {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        CustomSmallLabel(
//            header = label,
//            headerColor = onSurfaceColor.copy(alpha = 0.7f),
//            modifier = Modifier
//        )
//        CustomLabel(
//            header = value,
//            headerColor = onSurfaceColor,
//            fontSize = ResponsiveLayout.getResponsiveFontSize(13.sp, 14.sp, 15.sp)
//        )
//    }
//}

@Composable
private fun ActionButtonRow(
    onVerify: () -> Unit,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(pxToDp(12))
    ) {
        AppButton(
            buttonText = "Approve",
            onClick = onApprove,
            modifier = Modifier.weight(1f)
        )
        AppButton(
            buttonText = "Reject",
            onClick = onReject,
            modifier = Modifier.weight(1f)
        )
        AppButton(
            buttonText = "Request Verification",
            onClick = onVerify,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ConversationHeader(count: Int) {
    CustomLabel(
        header = "Discussion ($count)",
        headerColor = onSurfaceColor,
        fontWeight = FontWeight.SemiBold,
        fontSize = ResponsiveLayout.getResponsiveFontSize(16.sp, 18.sp, 20.sp)
    )
}

@Composable
private fun CommentCard(comment: RequestComment) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = onSurfaceVariant),
        shape = RoundedCornerShape(pxToDp(12))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = ResponsiveLayout.getResponsivePadding(16.dp, 20.dp, 24.dp),
                    vertical = ResponsiveLayout.getResponsivePadding(16.dp, 20.dp, 24.dp)
                ),
            verticalArrangement = Arrangement.spacedBy(pxToDp(6))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomLabel(
                    header = comment.author,
                    headerColor = onSurfaceColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = ResponsiveLayout.getResponsiveFontSize(14.sp, 15.sp, 16.sp)
                )
                CustomSmallLabel(
                    header = comment.timestamp,
                    headerColor = onSurfaceColor.copy(alpha = 0.6f),
                    modifier = Modifier
                )
            }
            CustomSmallLabel(
                header = comment.role,
                headerColor = primaryColor,
                modifier = Modifier
            )
            CustomLabel(
                header = comment.message,
                headerColor = onSurfaceColor,
                fontSize = ResponsiveLayout.getResponsiveFontSize(13.sp, 14.sp, 15.sp)
            )
        }
    }
}

private fun placeholderRequest(): PropertyRequest = PropertyRequest(
    id = "NA",
    machineName = "Unknown Request",
    department = "--",
    requestedSlot = "--",
    requestedBy = "--",
    requestedDepartment = "--",
    requestedDate = "--",
    description = "No additional details available for this request.",
    priority = RequestPriority.LOW,
    status = RequestStatus.PENDING,
    comments = emptyList()
)
