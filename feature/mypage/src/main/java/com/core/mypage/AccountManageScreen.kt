package com.core.mypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.core.mypage.component.account.AccountTopBar
import com.core.mypage.component.account.BorderIconText
import com.core.navigation.SettingNavigation
import com.youth.app.feature.mypage.R
import com.youthtalk.component.CategoryButton
import com.youthtalk.component.CustomDialog
import com.youthtalk.component.RoundButton
import com.youthtalk.component.filter.FilterCategoryTitle
import com.youthtalk.component.filter.NonlazyGrid
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.util.clickableSingle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountManageScreen(modifier: Modifier = Modifier, navHost: NavHostController) {
    var isShowBottomSheet by remember {
        mutableStateOf(false)
    }
    var isShowLogoutDialog by remember {
        mutableStateOf(false)
    }
    var isShowDeleteAccount by remember {
        mutableStateOf(false)
    }
    val bottomState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        AccountTopBar(
            title = "계정관리",
            onBack = { navHost.popBackStack() },
        )
        HorizontalDivider()

        SettingScreen(
            modifier = Modifier
                .padding(top = 30.dp)
                .padding(horizontal = 17.dp),
            title = "닉네임 설정",
            name = "놀고픈 청년",
        ) {
            Image(
                modifier = Modifier.clickableSingle {
                    navHost.navigate(SettingNavigation.NicknameSetting.route)
                },
                painter = painterResource(id = R.drawable.edit_icon),
                contentDescription = "설정",
            )
        }

        SettingScreen(
            modifier = Modifier
                .padding(top = 30.dp)
                .padding(horizontal = 17.dp),
            title = "나의 지역 설정",
            name = "부산광역시",
        ) {
            Image(
                modifier = Modifier.clickableSingle {
                    isShowBottomSheet = true
                },
                painter = painterResource(id = R.drawable.region_setting_icon),
                contentDescription = "설정",
            )
        }

        DialogButton(
            onLogout = {
                isShowLogoutDialog = true
            },
            onDeleteAccount = {
                isShowDeleteAccount = true
            },
        )
    }

    RegionBottomSheet(isShowBottomSheet, bottomState) {
        isShowBottomSheet = false
    }

    if (isShowLogoutDialog) {
        CustomDialog(
            title = "정말로 로그아웃 하시겠습니까?",
            onCancel = { /*TODO*/ },
            onSuccess = { /*TODO*/ },
            onDismiss = {
                isShowLogoutDialog = false
            },
        )
    }

    if (isShowDeleteAccount) {
        CustomDialog(
            title = "정말로 탈퇴 하시겠습니까?",
            onCancel = { /*TODO*/ },
            onSuccess = { /*TODO*/ },
            onDismiss = {
                isShowDeleteAccount = false
            },
        )
    }
}

@Composable
private fun DialogButton(onLogout: () -> Unit, onDeleteAccount: () -> Unit) {
    Text(
        modifier = Modifier
            .padding(top = 24.dp)
            .padding(vertical = 13.dp, horizontal = 17.dp)
            .clickableSingle {
                onLogout()
            },
        text = "로그아웃",
        style = MaterialTheme.typography.titleLarge.copy(
            color = MaterialTheme.colorScheme.onSecondary,
        ),
    )
    Text(
        modifier = Modifier
            .padding(vertical = 13.dp, horizontal = 17.dp)
            .clickableSingle {
                onDeleteAccount()
            },
        text = "계정탈퇴",
        style = MaterialTheme.typography.titleLarge.copy(
            color = MaterialTheme.colorScheme.onSecondary,
        ),
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun RegionBottomSheet(isShowBottomSheet: Boolean, bottomState: SheetState, onDismiss: () -> Unit) {
    if (isShowBottomSheet) {
        ModalBottomSheet(
            modifier = Modifier
                .fillMaxHeight(1f),
            containerColor = Color.White,
            sheetState = bottomState,
            dragHandle = null,
            properties = ModalBottomSheetProperties(
                securePolicy = SecureFlagPolicy.SecureOn,
                isFocusable = true,
                shouldDismissOnBackPress = true,
            ),
            onDismissRequest = { onDismiss() },
        ) {
            FilterTopBar()
            FilterCategoryTitle(
                title = "지역선택",
            )
            val regions = stringArrayResource(id = R.array.regions).toList()
            NonlazyGrid(
                modifier = Modifier
                    .padding(horizontal = 11.5.dp, vertical = 8.dp),
                columns = 2,
                itemCount = regions.size,
            ) { index ->
                CategoryButton(
                    modifier = Modifier.padding(horizontal = 5.5.dp, vertical = 4.dp),
                    title = regions[index],
                    isSelected = false,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            RoundButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 17.dp, vertical = 13.dp),
                text = "적용하기",
                color = MaterialTheme.colorScheme.primary,
            ) {
            }
        }
    }
}

@Composable
private fun FilterTopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "지역설정",
            style = MaterialTheme.typography.labelMedium.copy(
                color = MaterialTheme.colorScheme.onSecondary,
            ),
        )

        Icon(
            modifier =
            Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 20.dp),
            painter = painterResource(R.drawable.close_circle_icon),
            contentDescription = "닫기",
            tint = MaterialTheme.colorScheme.onSecondary,
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
            .padding(vertical = 13.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "지역설정은 모든 카테고리에 적용됩니다.",
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onSecondary,
            ),
        )
    }
}

@Composable
private fun SettingScreen(modifier: Modifier = Modifier, title: String, name: String, icon: @Composable () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall.copy(
                color = MaterialTheme.colorScheme.onSecondary,
            ),
        )
        BorderIconText(
            title = name,
        ) {
            icon()
        }
    }
}

@Preview
@Composable
private fun AccountManageScreenPreview() {
    YongProjectTheme {
        AccountManageScreen(
            navHost = rememberNavController(),
        )
    }
}
