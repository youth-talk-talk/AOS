package com.core.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.core.login.LoginViewModel
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationScreen(viewModel: LoginViewModel, onBack: () -> Unit) {
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            ) {
                focusManager.clearFocus()
            }
            .padding(horizontal = 16.dp),
    ) {
        LoginAppBar(
            onClickBack = {
                when (type) {
                    SignType.NICK_NAME -> onBack()
                    SignType.REGION -> {
                        type = SignType.NICK_NAME
                    }
                }
            },
        )

        when (type) {
            SignType.NICK_NAME -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = stringResource(R.string.nickname_title),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = gray100,
                        ),
                    )

                    Text(
                        text = stringResource(R.string.nickname_sub_title),
                        style = MaterialTheme.typography.displayMedium.copy(
                            color = gray80,
                        ),
                    )
                }

                InputTextField(
                    modifier = Modifier.padding(top = 16.dp),
                    hint = "닉네임을 입력해 주세요",
                    text = nickname,
                    onTextChange = { if (it.length <= 8) nickname = it },
                    onCheckFilter = {
                        it.length > 8
                    },
                )

                Spacer(Modifier.weight(1f))
            }
            SignType.REGION -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = stringResource(R.string.region_title),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = gray100,
                        ),
                    )

                    Text(
                        text = stringResource(R.string.region_sub_title),
                        style = MaterialTheme.typography.displayMedium.copy(
                            color = gray80,
                        ),
                    )
                }

                RegionDropDown(
                    modifier = Modifier.padding(top = 40.dp),
                    select = region,
                    hint = "지역을 선택해 주세요",
                    onSelect = {
                        Timber.i("onSelect 클릭")
                        bottomSheet = true
                    },
                )

                Spacer(Modifier.weight(1f))
            }
        }

        if (bottomSheet) {
            val regions = stringArrayResource(R.array.regions).toList()
            var selectedRegion by remember {
                mutableStateOf("")
            }
            val scope = rememberCoroutineScope()
            val sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true,
            )
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = { bottomSheet = false },
                containerColor = gray10,
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp),
                ) {
                    Text(
                        "지역을 선택해 주세요",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = gray100,
                        ),
                    )
                    LazyVerticalGrid(
                        modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(items = regions) {
                            SelectedButton(
                                text = it,
                                isSelected = selectedRegion == it,
                            ) {
                                selectedRegion = it
                            }
                        }
                    }

                    CheckButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 26.dp),
                        text = "적용하기",
                        isCheck = selectedRegion.isNotEmpty(),
                    ) {
                        Timber.i("Check Rigion : $selectedRegion")
                        region = selectedRegion
                        scope.launch {
                            sheetState.hide()
                            bottomSheet = false
                        }
                    }
                }
            }
        }

        CheckButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 4.dp,
                    vertical = 26.dp,
                ),
            isCheck = if (type == SignType.NICK_NAME) nickname.isNotEmpty() else true,
            text = if (type == SignType.NICK_NAME) stringResource(R.string.next) else stringResource(R.string.done),
        ) {
            when (type) {
                SignType.NICK_NAME -> type = SignType.REGION
                SignType.REGION -> TODO()
            }
        }
    }
}

@Composable
fun InformationScreen(text: String, onValueChange: (String) -> Unit, onClickSign: (String, String) -> Unit, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(horizontal = 16.dp),
    ) {
        LoginAppBar(
            onClickBack = onBack,
        )
        Text(
            text = stringResource(R.string.nickname_title),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = gray100,
            ),
        )

        Text(
            text = stringResource(R.string.nickname_title),
            style = MaterialTheme.typography.displayMedium.copy(
                color = gray80,
            ),
        )

        InputTextField(
            hint = "닉네임을 입력해 주세요",
            text = text,
            onTextChange = onValueChange,
        )
    }
}

@Preview
@Composable
private fun InformationScreenPreview() {
    YongProjectTheme {
        InformationScreen(
            text = "",
            onValueChange = {},
            onClickSign = { _, _ -> },
            onBack = {},
        )
    }
}
