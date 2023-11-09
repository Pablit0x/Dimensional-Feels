package com.ps.dimensional_feels.presentation.screens.write

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ps.dimensional_feels.R
import com.ps.dimensional_feels.model.Diary
import com.ps.dimensional_feels.model.GalleryImage
import com.ps.dimensional_feels.model.GalleryState
import com.ps.dimensional_feels.model.Mood
import com.ps.dimensional_feels.model.RickAndMortyCharacters
import com.ps.dimensional_feels.model.getMoodByPosition
import com.ps.dimensional_feels.presentation.components.GalleryUploader
import io.realm.kotlin.ext.toRealmList

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WriteContent(
    uiState: WriteUiState,
    galleryState: GalleryState,
    paddingValues: PaddingValues,
    pagerState: PagerState,
    pageNumber: Int,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onSavedClicked: (Diary) -> Unit,
    onImageSelected: (Uri) -> Unit,
    onImageClicked: (GalleryImage, Int) -> Unit,
    onChangeCharacter: (RickAndMortyCharacters) -> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = uiState.description) {
        scrollState.scrollTo(scrollState.maxValue)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .padding(
                top = paddingValues.calculateTopPadding()
            )
            .padding(horizontal = 24.dp)
            .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .weight(1f)
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            HorizontalPager(
                state = pagerState,
                verticalAlignment = Alignment.CenterVertically,

                ) { page ->
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    AsyncImage(
                        modifier = Modifier.size(120.dp),
                        model = ImageRequest.Builder(context).data(
                            getMoodByPosition(
                                character = uiState.characters, position = pageNumber
                            ).icon
                        ).crossfade(true).build(),
                        contentDescription = stringResource(id = R.string.mood_icon)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val interactionSource = remember { MutableInteractionSource() }

                    Icon(imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = null,
                        modifier = Modifier.clickable(
                            interactionSource = interactionSource, indication = null
                        ) {
                            when (uiState.characters) {
                                RickAndMortyCharacters.Rick -> onChangeCharacter(
                                    RickAndMortyCharacters.Jerry
                                )

                                RickAndMortyCharacters.Morty -> onChangeCharacter(
                                    RickAndMortyCharacters.Rick
                                )

                                RickAndMortyCharacters.Beth -> onChangeCharacter(
                                    RickAndMortyCharacters.Morty
                                )

                                RickAndMortyCharacters.Summer -> onChangeCharacter(
                                    RickAndMortyCharacters.Beth
                                )

                                RickAndMortyCharacters.Jerry -> onChangeCharacter(
                                    RickAndMortyCharacters.Summer
                                )
                            }
                        })
                    Text(
                        text = uiState.characters.name,
                        modifier = Modifier.width(80.dp),
                        textAlign = TextAlign.Center
                    )
                    Icon(imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                        modifier = Modifier.clickable(
                            interactionSource = interactionSource, indication = null
                        ) {
                            when (uiState.characters) {
                                RickAndMortyCharacters.Rick -> onChangeCharacter(
                                    RickAndMortyCharacters.Morty
                                )

                                RickAndMortyCharacters.Morty -> onChangeCharacter(
                                    RickAndMortyCharacters.Beth
                                )

                                RickAndMortyCharacters.Beth -> onChangeCharacter(
                                    RickAndMortyCharacters.Summer
                                )

                                RickAndMortyCharacters.Summer -> onChangeCharacter(
                                    RickAndMortyCharacters.Jerry
                                )

                                RickAndMortyCharacters.Jerry -> onChangeCharacter(
                                    RickAndMortyCharacters.Rick
                                )
                            }
                        })
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.title,
                onValueChange = onTitleChanged,
                placeholder = {
                    Text(text = stringResource(id = R.string.title_placeholder))
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                label = { Text(text = stringResource(id = R.string.title_label)) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Unspecified,
                    disabledIndicatorColor = Color.Unspecified,
                    unfocusedIndicatorColor = Color.Unspecified,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                ),
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }),
                maxLines = 1,
                singleLine = true
            )
            Spacer(modifier = Modifier.height(4.dp))
            Divider()
            Spacer(modifier = Modifier.height(4.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.description,
                onValueChange = onDescriptionChanged,
                placeholder = {
                    Text(text = stringResource(id = R.string.description_placeholder))
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Unspecified,
                    disabledIndicatorColor = Color.Unspecified,
                    unfocusedIndicatorColor = Color.Unspecified,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.clearFocus()
                })
            )
        }
        Column(verticalArrangement = Arrangement.Bottom) {
            Spacer(modifier = Modifier.height(12.dp))
            GalleryUploader(galleryState = galleryState,
                onAddClicked = { focusManager.clearFocus() },
                onImageSelected = onImageSelected,
                onImageClicked = { galleryImage, index ->
                    onImageClicked(galleryImage, index)
                })
            Spacer(modifier = Modifier.height(12.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp), onClick = {
                    if (uiState.title.isNotEmpty() && uiState.description.isNotEmpty()) {
                        onSavedClicked(Diary().apply {
                            this.title = uiState.title
                            this.description = uiState.description
                            this.character = uiState.characters.name
                            this.images =
                                galleryState.images.map { it.remoteImagePath }.toRealmList()
                        })
                    } else {
                        Toast.makeText(
                            context,
                            context.getString(R.string.fields_cannot_be_empty),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, shape = Shapes().small
            ) {
                Text(
                    text = if (uiState.selectedDiaryId == null) stringResource(id = R.string.save) else stringResource(
                        id = R.string.update
                    )
                )
            }
        }
    }
}