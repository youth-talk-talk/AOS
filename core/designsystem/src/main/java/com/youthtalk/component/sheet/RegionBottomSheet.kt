package com.youthtalk.component.sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.youth.app.core.designsystem.R
import com.youthtalk.component.button.CheckButton
import com.youthtalk.component.button.SelectedButton
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray100
import com.youthtalk.model.Region
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegionBottomSheet(modifier: Modifier = Modifier, region: Region?, onDismiss: () -> Unit, onClick: (Region?) -> Unit) {
    val regions = Region.entries.toList()
    var selectedRegion by remember {
        mutableStateOf(region)
    }
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    ModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        onDismissRequest = onDismiss,
        containerColor = gray10
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = stringResource(R.string.bottom_sheet_title),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = gray100
                )
            )
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items = regions) {
                    SelectedButton(
                        text = when (it) {
                            Region.ALL -> "전체 지역"
                            else -> it.region
                        },
                        isSelected = selectedRegion == it
                    ) {
                        selectedRegion = it
                    }
                }
            }

            CheckButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 26.dp),
                text = stringResource(R.string.bottom_sheet_button_text),
                isCheck = selectedRegion != null
            ) {
                scope.launch {
                    sheetState.hide()
                }
                onClick(selectedRegion)
                onDismiss()
            }
        }
    }
}
