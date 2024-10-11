package com.grayson.audiocross.presentation.albumlist.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grayson.audiocross.R
import com.grayson.audiocross.domain.login.model.User
import com.grayson.audiocross.domain.login.model.isLogin
import com.grayson.audiocross.ui.theme.AudioCrossTheme

@Composable
fun DrawerContent(
    modifier: Modifier = Modifier,
    userInfo: User? = null,
    onClickUserItem: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .width(240.dp)
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        UserItem(
            userInfo = userInfo,
            onClick = onClickUserItem
        )
    }
}

@Composable
fun UserItem(
    modifier: Modifier = Modifier, userInfo: User? = null, onClick: () -> Unit = {}
) {
    val isLogin = userInfo.isLogin()

    Row(
        modifier = modifier
            .clickable { onClick() }
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val icon = if (isLogin) {
            painterResource(id = R.drawable.icon_logout_24)
        } else {
            painterResource(id = R.drawable.icon_login_24)
        }

        Icon(
            painter = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )

        if (isLogin) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(id = R.string.logout),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = userInfo?.username ?: "",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        } else {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.login),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Start
            )
        }
    }
}


@Preview
@Composable
private fun DrawerLayout() {
    AudioCrossTheme {
        DrawerContent()
    }
}