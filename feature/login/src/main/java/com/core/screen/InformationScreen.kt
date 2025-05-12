package com.core.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.core.model.signup.SignType
import com.youth.app.feature.login.R
import com.youthtalk.component.button.CheckButton
import com.youthtalk.component.button.SelectedButton
import com.youthtalk.component.dropdown.RegionDropDown
import com.youthtalk.component.inputfield.InputTextField
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray100
import com.youthtalk.designsystem.gray80
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun InformationScreen(loading: Boolean, onBack: () -> Unit, signUp: (String, String) -> Unit) {
    var nickname by remember {
        mutableStateOf("")
    }
    var region by remember {
        mutableStateOf("")
    }
    var type by remember {
        mutableStateOf(SignType.NICK_NAME)
    }
    var bottomSheet by remember {
        mutableStateOf(false)
    }
    val focusManager = LocalFocusManager.current

    BackHandler {
        when (type) {
            SignType.NICK_NAME -> onBack()
            SignType.REGION -> {
                type = SignType.NICK_NAME
            }
        }
    }

    if (loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
            }
            .padding(horizontal = 16.dp)
    ) {
        LoginAppBar(
            onClickBack = {
                when (type) {
                    SignType.NICK_NAME -> onBack()
                    SignType.REGION -> {
                        type = SignType.NICK_NAME
                    }
                }
            }
        )

        InputTypeScreen(
            modifier = Modifier.weight(1f),
            type = type,
            region = region,
            nickname = nickname,
            onSelect = {
                bottomSheet = true
            },
            onChangeNickname = {
                if (it.length <= 8) nickname = it
            }
        )

        CheckButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 4.dp,
                    vertical = 26.dp
                ),
            isCheck = if (type == SignType.NICK_NAME) nickname.isNotEmpty() else nickname.isNotEmpty() && region.isNotEmpty(),
            text = if (type == SignType.NICK_NAME) stringResource(R.string.next) else stringResource(R.string.done)
        ) {
            Timber.i("checkButton")
            when (type) {
                SignType.NICK_NAME -> type = SignType.REGION
                SignType.REGION -> signUp(nickname, region)
            }
        }
    }

    if (bottomSheet) {
        RegionBottomSheet(
            region = region,
            onDismiss = {
                bottomSheet = false
            },
            onClick = {
                region = it
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegionBottomSheet(modifier: Modifier = Modifier, region: String, onDismiss: () -> Unit, onClick: (String) -> Unit) {
    val regions = stringArrayResource(R.array.regions).toList()
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
                        text = it,
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
                isCheck = selectedRegion.isNotEmpty()
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

@Composable
fun InputTypeScreen(
    modifier: Modifier = Modifier,
    type: SignType,
    nickname: String,
    region: String,
    onChangeNickname: (String) -> Unit,
    onSelect: () -> Unit
) {
    Crossfade(
        modifier = modifier,
        targetState = type,
        animationSpec = tween()
    ) { signType ->
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            when (signType) {
                SignType.REGION -> {
                    InputTitle(
                        title = stringResource(R.string.region_title),
                        subTitle = stringResource(R.string.region_sub_title)
                    )

                    RegionDropDown(
                        modifier = Modifier.padding(top = 40.dp),
                        select = region,
                        hint = stringResource(R.string.input_region_hint),
                        onSelect = onSelect
                    )
                }

                SignType.NICK_NAME -> {
                    InputTitle(
                        title = stringResource(R.string.nickname_title),
                        subTitle = stringResource(R.string.nickname_sub_title)
                    )

                    InputTextField(
                        modifier = Modifier.padding(top = 16.dp),
                        hint = stringResource(R.string.input_nickname_hint),
                        text = nickname,
                        onTextChange = onChangeNickname,
                        onCheckFilter = {
                            it.length > 8
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun InputTitle(title: String, subTitle: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.bodyMedium.copy(
            color = gray100
        )
    )

    Text(
        text = subTitle,
        style = MaterialTheme.typography.displayMedium.copy(
            color = gray80
        )
    )
}

@Preview
@Composable
private fun InformationScreenPreview() {
    YongProjectTheme {
        InformationScreen(
            loading = false,
            onBack = {},
            signUp = { nickname, region ->
            }
        )
    }
}

@Preview
@Composable
private fun InformationScreenLoadingPreview() {
    YongProjectTheme {
        InformationScreen(
            loading = true,
            onBack = {},
            signUp = { nickname, region ->
            }
        )
    }
}
