package com.example.pago.presentation.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.pago.IMAGE_URL
import com.example.pago.R
import com.example.pago.presentation.state.PostItem
import com.example.pago.presentation.state.UiState
import com.example.pago.presentation.ui.theme.Gray10

@Composable
fun PersonDetailsScreen(
    personDetailsViewModel: PersonDetailsViewModel = hiltViewModel(),
    id: Int,
    personName: String,
    personEmail: String
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        personDetailsViewModel.initialize(id)

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            PersonDetailsHeader(personName, personEmail)
            PersonDetailsPosts(personDetailsViewModel)
        }

    }
}

@Composable
private fun PersonDetailsHeader(personName: String, personEmail: String) {
    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = IMAGE_URL,
                placeholder = painterResource(id = R.drawable.ic_person_generic),
                fallback = painterResource(id = R.drawable.ic_person_generic),
                error = painterResource(id = R.drawable.ic_person_generic)
            ), contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .size(40.dp)
                .aspectRatio(1.0f)
        )
        Spacer(modifier = Modifier.size(10.dp))
        Text(
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = personName,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier,
            fontSize = 16.sp
        )
        Text(
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = personEmail,
            color = Color.Gray,
            modifier = Modifier,
            fontSize = 14.sp
        )
    }
}

@Composable
fun PersonDetailsPosts(personDetailsViewModel: PersonDetailsViewModel) {
    when (val state = personDetailsViewModel.postsState.collectAsState().value) {
        is UiState.Loading -> {

        }

        is UiState.Loaded -> {
            PersonPosts(state.data)
        }

        is UiState.Error -> {

        }
    }
}

@Composable
fun PersonPosts(posts: List<PostItem>) {
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(posts) { index, post ->
            PostTile(
                postItem = post,
                onPostClicked = {
                    val message = "Clicked on post number ${post.id}"
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            )
            if (index < posts.size - 1) {
                HorizontalDivider(
                    thickness = 2.dp,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun PostTile(
    postItem: PostItem,
    onPostClicked: (post: PostItem) -> Unit
) {
    Column(
        modifier = Modifier
            .background(color = Gray10)
            .fillMaxWidth()
            .padding(20.dp)
            .clickable { onPostClicked(postItem) }
    ) {
        Text(
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = postItem.title,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.size(5.dp))
        Text(
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            text = postItem.body,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier,
            lineHeight = 18.sp,
            fontSize = 14.sp
        )
    }
}

@Preview
@Composable
private fun PersonDetailsHeaderPreview() {
    PersonDetailsHeader(
        personName = "Aurelian Ticusan",
        personEmail = "aurelian.ticusan@pago.com"
    )
}

@Preview
@Composable
private fun PostTilePreview() {
    val postItem = PostItem(
        id = -1,
        userId = -1,
        title = "Ce mai mănâncă milenialii?",
        body = "Am dat peste o știre cum că milenialii trebuie să se informeze înainte să lorem ipsum dolor estos eles lorot kara."
    )
    PostTile(postItem) {

    }
}