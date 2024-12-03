package com.example.pago.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.pago.IMAGE_URL
import com.example.pago.R
import com.example.pago.presentation.state.PersonItem

@Composable
fun MainScreen(
    personItems: List<PersonItem>,
    onPersonClicked: (person: PersonItem) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (personItems.isEmpty()) {
            NoItemsHomeScreen { }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                itemsIndexed(personItems) { index, person ->
                    PersonTile(
                        person = person,
                        showLogo = index % 2 == 0,
                        onPersonClicked = onPersonClicked
                    )
                    if (index < personItems.size - 1) {
                        HorizontalDivider(
                            thickness = 2.dp,
                            color = Color.LightGray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NoItemsHomeScreen(onClickRefresh: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.ic_empty_items),
            contentDescription = null
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.home_screen_no_items_label),
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )
            Button(onClick = onClickRefresh) {
                Text(text = stringResource(R.string.refresh_label))
            }
        }
    }
}

@Composable
fun PersonTile(
    person: PersonItem,
    showLogo: Boolean,
    onPersonClicked: (person: PersonItem) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPersonClicked(person) }
            .padding(vertical = 30.dp, horizontal = 20.dp)
            .height(70.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showLogo) PersonLogo() else PersonInitials(person)
        Text(
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = person.name,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Start,
            fontSize = 20.sp,
            modifier = Modifier
                .weight(1f)
                .padding(start = 15.dp, end = 5.dp)
        )
        Image(
            painter = painterResource(R.drawable.ic_chevron), contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(25.dp)
        )
    }
}

@Composable
private fun PersonInitials(person: PersonItem) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .clip(CircleShape)
            .background(Color.Gray)
            .aspectRatio(1.0f)
    ) {
        val initials = person.name
            .split(' ')
            .mapNotNull { it.firstOrNull()?.toString() }
            .reduce { acc, s -> if (acc.length < 2) acc + s else acc }
        Text(
            textAlign = TextAlign.Center,
            text = initials,
            color = Color.White,
            fontSize = 24.sp,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight(align = Alignment.CenterVertically)
                .clip(CircleShape)
        )
    }
}

@Composable
private fun PersonLogo() {
    val url = IMAGE_URL
    Image(
        painter = rememberAsyncImagePainter(
            model = url,
            placeholder = painterResource(id = R.drawable.ic_person_generic),
            fallback = painterResource(id = R.drawable.ic_person_generic),
            error = painterResource(id = R.drawable.ic_person_generic)
        ), contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .clip(CircleShape)
            .fillMaxHeight()
            .aspectRatio(1.0f)
    )
}

@Preview
@Composable
private fun PersonTilePreview() {
    val person = PersonItem(
        id = -1,
        name = "Aurelian Ticusan",
        status = "active",
        gender = "Female",
        email = "example@google.com"
    )
    Column {
        PersonTile(person = person, true) {

        }
        PersonTile(person = person, false) {

        }
    }
}