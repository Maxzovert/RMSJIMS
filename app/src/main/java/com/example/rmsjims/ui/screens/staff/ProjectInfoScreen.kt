package com.example.rmsjims.ui.screens.staff

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.rmsjims.R
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
import com.example.rmsjims.util.pxToDp
import com.example.rmsjims.util.ResponsiveLayout
import com.example.rmsjims.viewmodel.BranchViewModel
import com.example.rmsjims.viewmodel.DepartmentViewModel
import org.koin.androidx.compose.koinViewModel

@SuppressLint("RememberReturnType")
@Composable
fun ProjectInfoScreen(
    navController: NavHostController,

){
    val branchViewModel : BranchViewModel = koinViewModel()
    val branchList = branchViewModel.branchName
    var value by remember { mutableStateOf("") }
    var selectedBranch by remember { mutableStateOf("") }

    val departmentViewModel : DepartmentViewModel = koinViewModel()
    val query = departmentViewModel.query
    val filteredDepartmentList = departmentViewModel.filteredDepartments

    // Team members state
    val teamMembers = remember { mutableStateListOf<String>() }
    var teamMemberInput by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Project Information",
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
            AppTextField(
                value = value,
                onValueChange = { value = it},
                placeholder = "Project Name"
            )

            AppTextField(
                value = value,
                onValueChange = { value = it},
                placeholder = "Guide Name"
            )

            AppTextField(
                value = value,
                onValueChange = { value = it},
                placeholder = "Project Description"
            )


            when (departmentViewModel.departmentState) {
                is UiState.Loading -> FilteredAppTextField(
                    modifier = Modifier.weight(1f),
                    value = "",
                    onValueChange = {},
                    placeholder = "Loading..."
                )
                is UiState.Error -> {
                    Log.e("ProjectInfoScreen", "Error loading departments")
                    Log.w("ProjectInfoScreen", "Using demo departments fallback due to error")
                    
                    val demoDepartments = getDemoDepartments()
                    FilteredAppTextField(
                        modifier = Modifier.weight(1f),
                        value = query,
                        onValueChange = { departmentViewModel.onQueryChange(it)},
                        placeholder = "Department",
                        items = if (query.isBlank()) demoDepartments else demoDepartments.filter { it.contains(query, ignoreCase = true) },
                        onItemSelected = {
                            departmentViewModel.onDepartmentSelected(it)
                        }
                    )
                }
                is UiState.Success -> {
                    // Use real data if available, otherwise fallback to demo data
                    val effectiveDepartments = if (filteredDepartmentList.isNotEmpty()) {
                        filteredDepartmentList
                    } else {
                        Log.w("ProjectInfoScreen", "Empty department list, using demo data fallback")
                        val demoDepartments = getDemoDepartments()
                        if (query.isBlank()) demoDepartments else demoDepartments.filter { it.contains(query, ignoreCase = true) }
                    }
                    
                    FilteredAppTextField(
                        value = query,
                        onValueChange = { departmentViewModel.onQueryChange(it)},
                        placeholder = "Department",
                        items = effectiveDepartments,
                        onItemSelected = {
                            departmentViewModel.onDepartmentSelected(it)
                        }
                    )
                }
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                AppDropDownTextField(
                    modifier = Modifier.weight(1f),
                    value = value,
                    onValueChange = { value = it},
                    placeholder = "Course Project",
                    items = listOf("Yes", "No")
                )


                when (branchViewModel.branchState) {
                    is UiState.Loading -> AppDropDownTextField(
                        modifier = Modifier.weight(1f),
                        value = "",
                        onValueChange = {},
                        placeholder = "Loading...",
                        items = emptyList()
                    )
                    is UiState.Error -> {
                        Log.e("ProjectInfoScreen", "Error loading branches")
                        Log.w("ProjectInfoScreen", "Using demo branches fallback due to error")
                        
                        val demoBranches = getDemoBranches()
                        AppDropDownTextField(
                            modifier = Modifier.weight(1f),
                            value = selectedBranch,
                            onValueChange = { selectedBranch = it },
                            placeholder = "Branch",
                            items = demoBranches
                        )
                    }
                    is UiState.Success -> {
                        // Use real data if available, otherwise fallback to demo data
                        val effectiveBranches = if (branchList.isNotEmpty()) {
                            branchList
                        } else {
                            Log.w("ProjectInfoScreen", "Empty branch list, using demo data fallback")
                            getDemoBranches()
                        }
                        
                        AppDropDownTextField(
                            modifier = Modifier.weight(1f),
                            value = selectedBranch,
                            onValueChange = { selectedBranch = it },
                            placeholder = "Branch",
                            items = effectiveBranches
                        )
                    }
                }
            }

            // Team Members Section (Optional)
            TeamMembersSection(
                teamMembers = teamMembers,
                inputValue = teamMemberInput,
                onInputChange = { teamMemberInput = it },
                onAddMember = {
                    if (teamMemberInput.isNotBlank() && !teamMembers.contains(teamMemberInput.trim())) {
                        teamMembers.add(teamMemberInput.trim())
                        teamMemberInput = ""
                    }
                },
                onRemoveMember = { teamMembers.remove(it) }
            )

            Spacer(modifier = Modifier.height(13.dp))
            Text(
                text = "Tell us about your project and where you’ll be using the equipment. Use the dropdown to select whether it’s a personal or course project.",
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


// Demo/fallback departments - used when Supabase returns empty or on error
@Composable
private fun getDemoDepartments(): List<String> {
    return remember {
        listOf(
            "Electronics & Communication Engineering",
            "Computer Science & Engineering",
            "Mechanical Engineering",
            "Civil Engineering",
            "Electrical Engineering",
            "Information Technology",
            "Aerospace Engineering",
            "Chemical Engineering",
            "Biotechnology",
            "Automobile Engineering"
        )
    }
}

// Demo/fallback branches - used when Supabase returns empty or on error
@Composable
private fun getDemoBranches(): List<String> {
    return remember {
        listOf(
            "Computer Science",
            "Electronics & Communication",
            "Mechanical",
            "Civil",
            "Electrical",
            "Information Technology",
            "Aerospace",
            "Chemical",
            "Biotechnology",
            "Automobile"
        )
    }
}

// Team Members Section Component
@Composable
fun TeamMembersSection(
    teamMembers: List<String>,
    inputValue: String,
    onInputChange: (String) -> Unit,
    onAddMember: () -> Unit,
    onRemoveMember: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(pxToDp(12))
    ) {
        // Label with optional indicator
        CustomLabel(
            header = "Team Members (Optional)",
            headerColor = onSurfaceColor.copy(0.9f),
            fontSize = 14.sp
        )

        // Input field with Add button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(pxToDp(8)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppTextField(
                modifier = Modifier.weight(1f),
                value = inputValue,
                onValueChange = onInputChange,
                placeholder = "Enter team member name"
            )
            IconButton(
                onClick = onAddMember,
                modifier = Modifier
                    .size(ResponsiveLayout.getResponsiveSize(52.dp, 60.dp, 68.dp))
                    .background(
                        color = primaryColor,
                        shape = RoundedCornerShape(pxToDp(8))
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Team Member",
                    tint = whiteColor,
                    modifier = Modifier.size(pxToDp(20))
                )
            }
        }

        // Display added team members as chips
        if (teamMembers.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(pxToDp(8)),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(teamMembers) { member ->
                    TeamMemberChip(
                        name = member,
                        onRemove = { onRemoveMember(member) }
                    )
                }
            }
        }
    }
}

@Composable
fun TeamMemberChip(
    name: String,
    onRemove: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                color = primaryColor.copy(alpha = 0.1f),
                shape = RoundedCornerShape(pxToDp(20))
            )
            .padding(
                horizontal = pxToDp(12),
                vertical = pxToDp(6)
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(pxToDp(6)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomLabel(
                header = name,
                fontSize = 13.sp,
                headerColor = onSurfaceColor,
                modifier = Modifier.padding(end = pxToDp(2))
            )
            Box(
                modifier = Modifier
                    .size(pxToDp(18))
                    .clickable { onRemove() }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove $name",
                    tint = primaryColor,
                    modifier = Modifier.size(pxToDp(16))
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProjectInfoScreenPreview() {
    // This preview will not have a real NavController or ViewModels,
    // so it's mainly for layout checking.
    ProjectInfoScreen(navController = androidx.navigation.compose.rememberNavController())
}

