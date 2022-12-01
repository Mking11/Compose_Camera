package com.mking1102.compose_camera.presentation.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.mking1102.compose_camera.camera.domain.models.CameraEvents
import com.mking1102.compose_camera.camera.domain.utils.Screens
import com.mking1102.compose_camera.presentation.CameraViewModel

@Composable
fun PicturePreview(
    navHostController: NavHostController,
    viewModel: CameraViewModel = hiltViewModel()
) {

    val picture = viewModel.state.collectAsState().value.picture
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()

    ) {
        val (image, row) = createRefs()




        AsyncImage(
            model = picture, contentDescription = "Captured Photo",
            modifier = Modifier
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    bottom.linkTo(row.top, margin = 4.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                    width = Dimension.matchParent
                },
            contentScale = ContentScale.FillHeight
        )

        Row(
            modifier = Modifier
                .constrainAs(row) {
                    bottom.linkTo(parent.bottom /*margin = 4.dp*/)
                    width = Dimension.wrapContent
                }
        ) {
            //clear button
            IconButton(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f),
                onClick = {
                    viewModel.handleEvents(CameraEvents.OpenCamera)
                },
                content = {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "check",
                        modifier = Modifier
                            .size(50.dp)
                            .padding(1.dp)
                            .border(1.dp, MaterialTheme.colors.onBackground, CircleShape),
                    )
                }
            )
            //check button
            IconButton(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f),
                onClick = {
                    navHostController.navigate(
                        Screens.CameraBaseScreen +
                                "?uri=${picture}"
                    ) {
                        popUpTo(Screens.SelectionScreen) {
                            inclusive = true
                        }
                    }
                },
                content = {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "arrow circle right",
                        modifier = Modifier
                            .size(50.dp)
                            .padding(1.dp)
                            .border(1.dp, color = MaterialTheme.colors.onBackground, CircleShape),
                    )
                }
            )
        }
    }
}