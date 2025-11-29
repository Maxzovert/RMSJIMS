package com.example.rmsjims.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.rmsjims.R
import com.example.rmsjims.ui.theme.categoryIconColor
import com.example.rmsjims.ui.theme.labelColor
import com.example.rmsjims.ui.theme.navBackColor
import com.example.rmsjims.ui.theme.navLabelColor
import com.example.rmsjims.ui.theme.searchBarColor
import com.example.rmsjims.util.ResponsiveLayout

@Composable
fun AppCircularIcon(
    painter: Painter = painterResource(R.drawable.ic_reset),
    iconDescription: String = "Circular Icon",
    boxSize: Dp = ResponsiveLayout.getResponsiveSize(46.dp, 52.dp, 60.dp),
    boxShape: Shape = CircleShape,
    boxColor: Color = searchBarColor,
    iconPadding: Dp = ResponsiveLayout.getResponsivePadding(11.dp, 13.dp, 15.dp),
    iconSize: Dp = ResponsiveLayout.getResponsiveSize(22.dp, 24.dp, 28.dp),
    onClick: () -> Unit = {},
    tint: Color = labelColor,
    modifier: Modifier = Modifier
){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(boxSize)
            .clip(boxShape)
            .background(boxColor)
            .clickable { onClick() }
    ){
        Icon(
            painter = painter,
            contentDescription = iconDescription,
            modifier = Modifier
                .padding(iconPadding)
                .size(iconSize),
            tint = tint
        )
    }
}


@Composable
fun AppCategoryIcon(
    painter : Painter,
    iconDescription : String = "Equipment Category Icon",
    modifier: Modifier = Modifier,
    iconSize: Dp = ResponsiveLayout.getResponsiveSize(24.dp, 28.dp, 32.dp),
    tint : Color = categoryIconColor
){
    Icon(
        painter = painter,
        contentDescription = iconDescription,
        modifier = modifier
            .size(iconSize),
        tint = tint
    )
}

@Composable
fun AppNavIcon(
    painter : Painter,
    iconDescription : String = "Navigation Icon",
    modifier: Modifier = Modifier,
    iconSize : Dp = ResponsiveLayout.getResponsiveSize(24.dp, 28.dp, 32.dp),
    tint : Color = navLabelColor,
){
    Icon(
        painter = painter,
        contentDescription = iconDescription,
        modifier = modifier
            .size(iconSize),
        tint = tint,
    )
}

@Composable
fun AppNavBackIcon(
    painter : Painter = painterResource(R.drawable.ic_back),
    iconDescription : String = "Navigation Icon",
    modifier: Modifier = Modifier,
    size : Dp = ResponsiveLayout.getResponsiveSize(24.dp, 28.dp, 32.dp),
    tint : Color = navBackColor,
    onClick: () -> Unit
){
    Icon(
        painter = painter,
        contentDescription = iconDescription,
        modifier = modifier
            .size(size)
            .clickable{
                onClick
            },
        tint = tint
    )
}