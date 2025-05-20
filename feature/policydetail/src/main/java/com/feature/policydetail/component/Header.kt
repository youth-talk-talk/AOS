package com.feature.policydetail.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.youth.app.feature.policydetail.R
import com.youthtalk.component.tag.KeywordTag
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray100
import com.youthtalk.designsystem.gray30
import com.youthtalk.designsystem.gray40
import com.youthtalk.designsystem.gray80
import com.youthtalk.designsystem.gray90
import com.youthtalk.model.PolicyDetail
import com.youthtalk.model.typeenum.Category
import com.youthtalk.model.typeenum.Region

fun LazyListScope.header(isExpand: Boolean, policyDetail: PolicyDetail, onClickExpand: () -> Unit, onClickLink: (String) -> Unit) {
    item {
        PolicyTitle(
            title = policyDetail.title ?: "",
            recruitmentType = policyDetail.recruitmentType,
            region = policyDetail.region,
            category = policyDetail.category
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 30.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "한 눈에 보는 정책 요약",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Image(
                        painter = painterResource(R.drawable.policy_summary_title),
                        contentDescription = "요약 이미지"
                    )
                }

                Text(
                    text = policyDetail.recruitmentType ?: "마감",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.errorContainer
                    )
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = gray30,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(13.dp)
                ) {
                    policyDetail.hostDep?.let { host ->
                        PolicySummaryContent(
                            title = "주관 기관",
                            content = host
                        )
                    }

                    policyDetail.category?.let { category ->
                        PolicySummaryContent(
                            title = "정책 분야",
                            content = category.categoryName
                        )
                    }

                    policyDetail.applyTerm?.let { term ->
                        PolicySummaryContent(
                            title = "신청 기간",
                            content = term
                        )
                    }

                    policyDetail.introduction?.let { introduction ->
                        PolicySummaryContentExpand(
                            isExpand = isExpand,
                            introduction = introduction,
                            onClick = onClickExpand
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .border(
                            width = 1.dp,
                            color = gray40,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.warning),
                            contentDescription = "주의"
                        )
                        Text(
                            text = "알려드립니다!",
                            style = MaterialTheme.typography.displayMedium
                        )
                    }

                    Text(
                        text = "상시모집의 경우 주관부처의 사업여부에 따라 조기 마감될 수 있습니다. 반드시 사이트를 통한 사업실행 여부를 확인해 주세요.",
                        style = MaterialTheme.typography.displaySmall.copy(
                            color = gray80
                        )
                    )
                }

                policyDetail.applUrl?.let { url ->
                    LinkButton(
                        onClickLink = { onClickLink(url) }
                    )
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(bottom = 20.dp),
            color = gray30,
            thickness = 10.dp
        )
    }
}

@Composable
fun LinkButton(modifier: Modifier = Modifier, onClickLink: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = gray40,
                shape = RoundedCornerShape(6.dp)
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClickLink()
            }
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "사이트에서 자세히 보기",
                style = MaterialTheme.typography.displayMedium
            )

            Image(
                painter = painterResource(R.drawable.link),
                contentDescription = "링크"
            )
        }
    }
}

@Composable
fun PolicySummaryContent(modifier: Modifier = Modifier, title: String, content: String) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.displayMedium.copy(
                color = gray80
            )
        )

        Text(
            modifier = Modifier.weight(1f),
            text = content,
            style = MaterialTheme.typography.displayMedium.copy(
                color = gray90,
                textAlign = TextAlign.End
            )
        )
    }
}

@Composable
fun PolicySummaryContentExpand(modifier: Modifier = Modifier, introduction: String, isExpand: Boolean, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onClick()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "정책 요약",
                style = MaterialTheme.typography.displayMedium.copy(
                    color = gray80
                )
            )

            Image(
                modifier = Modifier.size(20.dp),
                painter = painterResource(if (isExpand) R.drawable.arrowup else R.drawable.arrowdown),
                contentDescription = "위로 화살표",
                colorFilter = ColorFilter.tint(color = gray100)
            )
        }

        AnimatedVisibility(visible = isExpand) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .background(
                        color = gray10,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = introduction,
                    style = MaterialTheme.typography.displayMedium.copy(
                        color = gray90
                    )
                )
            }
        }
    }
}

@Composable
fun PolicyTitle(modifier: Modifier = Modifier, title: String, recruitmentType: String?, region: Region?, category: Category?) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            recruitmentType?.let { type ->
                KeywordTag(
                    text = type,
                    backgroundColor = gray90,
                    textColor = gray10
                )
            }

            region?.let {
                KeywordTag(
                    text = "${it.region}시"
                )
            }

            category?.let {
                KeywordTag(
                    text = "${it.categoryName}분야"
                )
            }
        }

        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
