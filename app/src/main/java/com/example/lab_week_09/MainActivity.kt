package com.example.lab_week_09  // Change to your actual package name

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab_week_09.ui.theme.LAB_WEEK_09Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Jetpack Compose replaces setContentView() with setContent { }
        setContent {
            // Apply your custom Material 3 theme
            LAB_WEEK_09Theme {
                // Surface = background container that can hold UI
                Surface(
                    modifier = Modifier.fillMaxSize(), // Fill the entire screen
                    color = MaterialTheme.colorScheme.background // Use background color from theme
                ) {
                    // Create a list of names
                    val list = listOf("Tanu", "Tina", "Tono")

                    // Pass this list into our composable
                    Home(list)
                }
            }
        }
    }
}

//---------------------------------------------
// 🏠 Home Composable
//---------------------------------------------

// This composable now takes a parameter `items` (a list of strings)
// so we can display a dynamic list of names.
@Composable
fun Home(
    // The list of items we want to display
    items: List<String>
) {
    // LazyColumn is a vertically scrolling list that only
    // composes the visible items (like a RecyclerView)
    LazyColumn {
        // The first "item" in the LazyColumn is a header area
        // with a TextField and a Button.
        item {
            Column(
                // Add padding and make it fill available space
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                // Center align content horizontally
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Display a title text
                Text(
                    text = stringResource(id = R.string.enter_item)
                )

                // Input field (currently static, no real state handling yet)
                TextField(
                    value = "", // Current text value
                    onValueChange = {
                        // This is where we’d handle input changes (e.g., updating state)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number // Use numeric keyboard
                    )
                )

                // Button that reacts to clicks
                Button(onClick = {
                    // Code to run when button is clicked
                    // e.g., add new item to list (not implemented yet)
                }) {
                    Text(text = stringResource(id = R.string.button_click))
                }
            }
        }

        // Display the list of items passed in
        // `items(items)` = iterate over each string in the list
        items(items) { item ->
            Column(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Show each item’s text
                Text(text = item)
            }
        }
    }
}

//---------------------------------------------
// 👀 Preview Composable (for Android Studio Preview)
//---------------------------------------------

// We remove @Preview from Home() since it needs a list argument,
// and instead we create a PreviewHome() to pass sample data.
@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    LAB_WEEK_09Theme {
        Home(listOf("Tanu", "Tina", "Tono"))
    }
}
