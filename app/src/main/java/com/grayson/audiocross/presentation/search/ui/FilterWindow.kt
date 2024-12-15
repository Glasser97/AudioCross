package com.grayson.audiocross.presentation.search.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Checkbox
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grayson.audiocross.R
import com.grayson.audiocross.domain.albumlist.base.OrderBy
import com.grayson.audiocross.domain.albumlist.base.SortMethod
import com.grayson.audiocross.presentation.albumlist.model.AlbumListFilterParam
import com.grayson.audiocross.presentation.search.model.FilterUIItem
import com.grayson.audiocross.presentation.search.model.FilterUIItem.Companion.filterUIItems
import com.grayson.audiocross.ui.theme.AudioCrossTheme


@Composable
fun FilterWindow(
    modifier: Modifier = Modifier,
    filterUIItems: List<FilterUIItem>,
    filterParam: AlbumListFilterParam,
    onUpdateFilterParam: (AlbumListFilterParam) -> Unit
) {
    Column(
        modifier = modifier
            .wrapContentHeight()
            .padding(end = 8.dp)
            .width(IntrinsicSize.Max)
    ) {
        Row(
            modifier = Modifier.clickable {
                onUpdateFilterParam(filterParam.copy(hasSubtitle = !filterParam.hasSubtitle))
            }.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Checkbox(
                checked = filterParam.hasSubtitle,
                modifier = Modifier.padding(8.dp),
                onCheckedChange = null
            )
            Text(
                text = stringResource(id = R.string.has_subtitles)
            )
        }
        Row(
            modifier = Modifier.clickable {
                onUpdateFilterParam(filterParam.copy(sortMethod = if (filterParam.sortMethod != SortMethod.ASCENDING) SortMethod.ASCENDING else SortMethod.DESCENDING))
            }.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Checkbox(
                checked = filterParam.sortMethod == SortMethod.ASCENDING,
                modifier = Modifier.padding(8.dp),
                onCheckedChange = null
            )
            Text(
                text = stringResource(id = R.string.is_ascending)
            )
        }
        filterUIItems.forEach { filterItem ->
            Row(
                modifier = Modifier.clickable {
                    onUpdateFilterParam(filterParam.copy(orderBy = filterItem.orderBy))
                }.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                RadioButton(
                    selected = filterParam.orderBy == filterItem.orderBy,
                    modifier = Modifier.padding(8.dp),
                    onClick = null
                )
                Text(
                    text = stringResource(filterItem.description)
                )
            }
        }
    }
}

@Preview
@Composable
private fun FilterWindowPreview() {
    AudioCrossTheme {
        FilterWindow(
            modifier = Modifier,
            filterUIItems = filterUIItems,
            filterParam = AlbumListFilterParam(
                orderBy = OrderBy.CREATED_DATE,
                sortMethod = SortMethod.DESCENDING,
                hasSubtitle = true
            ),
            onUpdateFilterParam = {}
        )
    }
}