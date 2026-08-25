package com.example.platemate.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.example.platemate.R

@Composable
fun RecipeCard(
    title: String,
    category: String?,
    imageUrl: String? = null,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Image section
            if (imageUrl != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                ) {
                    var isLoading by remember { mutableStateOf(true) }
                    var isError by remember { mutableStateOf(false) }

                    AsyncImage(
                        model = imageUrl,
                        contentDescription = title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        onLoading = {
                            isLoading = true
                            isError = false
                        },
                        onSuccess = {
                            isLoading = false
                            isError = false
                        },
                        onError = {
                            isLoading = false
                            isError = true
                        }
                    )

                    // Loading indicator
                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    // Error state (image failed to load)
                    if (isError) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_broken_image),
                                    contentDescription = "Image failed to load",
                                    modifier = Modifier.size(48.dp),
                                    tint = Color.Gray
                                )
                                Text(
                                    text = "Image unavailable",
                                    color = Color.Gray,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Text content
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = title,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = category ?: "Unknown category",
                    color = Color.Gray
                )
            }
        }
    }
}