package com.example.policydetail.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.policydetail.model.PolicyDetailUiState
import com.youth.app.feature.policydetail.R
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray50
import com.youthtalk.model.PolicyDetail

@Composable
fun PolicyDetail(modifier: Modifier = Modifier, policyDetail: PolicyDetail) {
    Column(
        modifier = modifier.background(color = Color.White),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        with(policyDetail) {
            SummaryCheck(
                supportDetail = supportDetail,
                applyTerm = applyTerm,
                operateTerm = operationTerm,
            )
            ApplyUser(
                age = age,
                addrIncome = addrIncome,
                education = education,
                major = major,
                employment = employment,
                specialization = specialization,
                applyLimit = applLimit,
                addition = addition,
            )
            ApplyMethod(
                applyStep = applStep,
                evaluation = evaluation,
                applyUrl = applUrl,
                submitDoc = submitDoc,
            )
            MoreScreen(
                etc = etc,
                hostDep = hostDep,
                operatingOrg = operatingOrg,
                refUrl1 = refUrl1,
                refUrl2 = refUrl2,
            )
        }
    }
}

@Composable
private fun MoreScreen(etc: String, hostDep: String, operatingOrg: String, refUrl1: String, refUrl2: String) {
    var isExpand by remember {
        mutableStateOf(false)
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            PolicyIconTitle(
                modifier = Modifier.weight(1f),
                icon = painterResource(id = R.drawable.add_icon),
                title = stringResource(id = R.string.apply_more_title),
            )
            Icon(
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) { isExpand = !isExpand },
                painter = if (isExpand) painterResource(id = R.drawable.up_icon) else painterResource(id = R.drawable.down_icon),
                contentDescription = "열기/닫기",
                tint = Color.Black,
            )
        }

        AnimatedVisibility(visible = isExpand) {
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                EtcInfo(etc)
                OperateInfo(hostDep, operatingOrg)
                RefSiteInfo(refUrl1, refUrl2)
            }
        }
    }
}

@Composable
private fun RefSiteInfo(refUrl1: String, refUrl2: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = stringResource(id = R.string.apply_more_ref_site),
            style = MaterialTheme.typography.titleLarge.copy(
                color = gray50,
            ),
        )

        Text(
            text = "-사업관련 참고 사이트1",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.W400,
                color = Color.Black,
            ),
        )

        Text(
            text = "    ${Typography.middleDot} $refUrl1",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.W400,
                color = Color.Black,
                lineBreak = LineBreak.Paragraph,
            ),
        )

        Text(
            text = "-사업관련 참고 사이트 2",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.W400,
                color = Color.Black,
            ),
        )
        Text(
            text = "    ${Typography.middleDot} $refUrl2",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.W400,
                color = Color.Black,
                lineBreak = LineBreak.Paragraph,
            ),
        )
    }
}

@Composable
private fun OperateInfo(hostDep: String, operatingOrg: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = stringResource(id = R.string.apply_more_host_dep),
            style = MaterialTheme.typography.titleLarge.copy(
                color = gray50,
            ),
        )

        Text(
            text = "-주관 기간: ${hostDep}\n-운영기관: $operatingOrg",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.W400,
                color = Color.Black,
            ),
        )
    }
}

@Composable
private fun EtcInfo(etc: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = stringResource(id = R.string.apply_more_etc),
            style = MaterialTheme.typography.titleLarge.copy(
                color = gray50,
            ),
        )

        Text(
            text = etc,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.W400,
                color = Color.Black,
            ),
        )
    }
}

@Composable
private fun ApplyMethod(applyStep: String, evaluation: String, applyUrl: String, submitDoc: String) {
    var isExpand by remember {
        mutableStateOf(false)
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            PolicyIconTitle(
                modifier = Modifier.weight(1f),
                icon = painterResource(id = R.drawable.question_icon),
                title = stringResource(id = R.string.apply_method_title),
            )
            Icon(
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) { isExpand = !isExpand },
                painter = if (isExpand) painterResource(id = R.drawable.up_icon) else painterResource(id = R.drawable.down_icon),
                contentDescription = "열기/닫기",
                tint = Color.Black,
            )
        }

        AnimatedVisibility(visible = isExpand) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                DetailedConditions(
                    title = stringResource(id = R.string.apply_method_apply_step),
                    content = applyStep,
                )

                DetailedConditions(
                    title = stringResource(id = R.string.apply_method_apply_evaluation),
                    content = evaluation,
                )

                DetailedConditions(
                    title = stringResource(id = R.string.apply_method_apply_url),
                    content = applyUrl,
                )

                DetailedConditions(
                    title = stringResource(id = R.string.apply_method_submit_doc),
                    content = submitDoc,
                )
            }
        }
    }
}

@Composable
private fun ApplyUser(
    age: String,
    addrIncome: String,
    education: String,
    major: String,
    employment: String,
    specialization: String,
    applyLimit: String,
    addition: String,
) {
    var isExpand by remember {
        mutableStateOf(false)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            PolicyIconTitle(
                modifier = Modifier.weight(1f),
                icon = painterResource(id = R.drawable.person_icon),
                title = stringResource(id = R.string.apply_user_title),
            )
            Icon(
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) { isExpand = !isExpand },
                painter = if (isExpand) painterResource(id = R.drawable.up_icon) else painterResource(id = R.drawable.down_icon),
                contentDescription = "열기/닫기",
                tint = Color.Black,
            )
        }

        AnimatedVisibility(visible = isExpand) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                DetailedConditions(
                    title = stringResource(id = R.string.age),
                    content = age,
                )

                DetailedConditions(
                    title = stringResource(id = R.string.addr_income),
                    content = addrIncome,
                )

                DetailedConditions(
                    title = stringResource(id = R.string.education),
                    content = education,
                )

                DetailedConditions(
                    title = stringResource(id = R.string.major),
                    content = major,
                )

                DetailedConditions(
                    title = stringResource(id = R.string.employment),
                    content = employment,
                )

                DetailedConditions(
                    title = stringResource(id = R.string.specialization),
                    content = specialization,
                )

                DetailedConditions(
                    title = stringResource(id = R.string.apply_limit),
                    content = applyLimit,
                )

                DetailedConditions(
                    title = stringResource(id = R.string.addition),
                    content = addition,
                )
            }
        }
    }
}

@Composable
private fun SummaryCheck(supportDetail: String, applyTerm: String, operateTerm: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        PolicyIconTitle(
            icon = painterResource(id = R.drawable.apply_icon),
            title = stringResource(id = R.string.apply_summary_title),
        )

        PolicyContent(
            contentTitle = stringResource(id = R.string.apply_content_title),
            contentDetail = supportDetail,
        )

        PolicyContent(
            contentTitle = stringResource(id = R.string.apply_date_title),
            contentDetail = applyTerm,
        )

        PolicyContent(
            modifier = Modifier.padding(bottom = 9.dp),
            contentTitle = stringResource(id = R.string.apply_operate_title),
            contentDetail = operateTerm.ifEmpty { "-" },
        )
    }
}

@Composable
private fun DetailedConditions(modifier: Modifier = Modifier, title: String, content: String) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                color = gray50,
            ),
        )

        Text(
            modifier = Modifier.weight(4f),
            text = content,
            style = MaterialTheme.typography.headlineLarge.copy(
                lineHeight = 20.sp,
            ),
        )
    }
}

@Composable
private fun PolicyIconTitle(modifier: Modifier = Modifier, icon: Painter, title: String) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            painter = icon,
            contentDescription = "정책 요약 아이콘",
            tint = MaterialTheme.colorScheme.primary,
        )

        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
fun PolicyContent(modifier: Modifier = Modifier, contentTitle: String, contentDetail: String) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = contentTitle,
            style = MaterialTheme.typography.headlineLarge.copy(
                color = gray50,
            ),
        )

        Text(
            text = contentDetail,
            style = MaterialTheme.typography.headlineLarge,
        )
    }
}

@Preview
@Composable
private fun PolicyDetailPreview() {
    val defaultPolicyDetail =
        PolicyDetailUiState.Success.defaultDetail
    YongProjectTheme {
        PolicyDetail(
            policyDetail = defaultPolicyDetail,
        )
    }
}
