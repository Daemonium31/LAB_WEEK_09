package com.example.lab_week_09 // change to your own package name

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.lab_week_09.ui.theme.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

//Main Activity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Here, we use setContent instead of setContentView
        setContent {
            //Here, we wrap our content with the theme
            //You can check out the LAB_WEEK_09Theme inside Theme.kt
            LAB_WEEK_09Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    //We use Modifier.fillMaxSize() to make the surface fill the whole screen
                    modifier = Modifier.fillMaxSize(),
                    //We use MaterialTheme.colorScheme.background to get the background color
                    //and set it as the color of the surface
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    //Here, we call the App composable instead of Home
                    App(
                        navController = navController
                    )
                }
            }
        }
    }
}

//Here, we create a composable function called App
//This will be the root composable of the app
@Composable
fun App(navController: NavHostController) {
    //Here, we use NavHost to create a navigation graph
    //We pass the navController as a parameter
    //We also set the startDestination to "home"
    //This means that the app will start with the Home composable
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        //Here, we create a route called "home"
        //We pass the Home composable as a parameter
        //This means that when the app navigates to "home",
        //the Home composable will be displayed
        composable("home") {
            //Here, we pass a lambda function that navigates to "resultContent"
            //and pass the listData as a parameter
            Home { navController.navigate("resultContent/?listData=$it") }
        }

        //Here, we create a route called "resultContent"
        //We pass the ResultContent composable as a parameter
        //This means that when the app navigates to "resultContent"
        //the ResultContent composable will be displayed
        //You can also define arguments for the route
        //Here, we define a String argument called "listData"
        //We use navArgument to define the argument
        //We use NavType.StringType to define the type of the argument
        composable(
            "resultContent/?listData={listData}",
            arguments = listOf(navArgument("listData") {
                type = NavType.StringType
            })
        ) {
            //Here, we pass the value of the argument to the ResultContent composable
            ResultContent(
                it.arguments?.getString("listData").orEmpty()
            )
        }
    }
}

//Declare a data class called Student
data class Student(
    var name: String
)

//Now let’s update the Home Composable and add 1 more parameter into it.
@Composable
fun Home(
    navigateFromHomeToResult: (String) -> Unit
) {
    //Here, we create a mutable state list of Student
    //We use remember to make the list remember its value
    //This is so that the list won't be recreated when the composable recomposes
    //We use mutableStateListOf to make the list mutable
    //This is so that we can add or remove items from the list
    //If you're still confused, this is basically the same concept as using
    //useState in React
    val listData = remember {
        mutableStateListOf(
            Student("Tanu"),
            Student("Tina"),
            Student("Tono")
        )
    }

    //Here, we create a mutable state of Student
    //This is so that we can get the value of the input field
    var inputField by remember { mutableStateOf(Student("")) }

    //We call the HomeContent composable
    //Here, we pass:
    //listData to show the list of items inside HomeContent
    //inputField to show the input field value inside HomeContent
    //A lambda function to update the value of the inputField
    //A lambda function to add the inputField to the listData
    HomeContent(
        listData,
        inputField,
        { input -> inputField = inputField.copy(name = input) },
        {
            // ✅ FIX: Prevent adding blank data to the list
            if (inputField.name.isNotBlank()) {
                listData.add(inputField)
                inputField = Student("")
            }
        },
        {
            // ✅ BONUS: Use Moshi to convert listData into JSON
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val type = Types.newParameterizedType(List::class.java, Student::class.java)
            val adapter = moshi.adapter<List<Student>>(type)
            val json = adapter.toJson(listData.toList())
            navigateFromHomeToResult(json)
        }
    )
}

//Also update the Home Content Composable and add 1 more parameter.
@Composable
fun HomeContent(
    listData: SnapshotStateList<Student>,
    inputField: Student,
    onInputValueChange: (String) -> Unit,
    onButtonClick: () -> Unit,
    navigateFromHomeToResult: () -> Unit
) {
    //Here, we use LazyColumn to display a list of items lazily
    LazyColumn {
        //Here, we use item to display an item inside the LazyColumn
        item {
            Column(
                //Modifier.padding(16.dp) is used to add padding to the Column
                //You can also use Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                //to add padding horizontally and vertically
                //or Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp)
                //to add padding to each side
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                //Alignment.CenterHorizontally is used to align the Column horizontally
                //You can also use verticalArrangement = Arrangement.Center to align the Column vertically
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OnBackgroundTitleText(
                    text = stringResource(id = R.string.enter_item)
                )

                TextField(
                    value = inputField.name,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    onValueChange = {
                        onInputValueChange(it)
                    }
                )

                Row {
                    PrimaryTextButton(text = stringResource(id = R.string.button_click)) {
                        onButtonClick()
                    }
                    PrimaryTextButton(text = stringResource(id = R.string.button_navigate)) {
                        navigateFromHomeToResult()
                    }
                }
            }
        }

        //Here, we use items to display a list of items inside the LazyColumn
        //This is the RecyclerView replacement
        //We pass the listData as a parameter
        items(listData) { item ->
            Column(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OnBackgroundItemText(text = item.name)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeContent() {
    LAB_WEEK_09Theme {
        // Mock data for preview
        val sampleList = remember {
            mutableStateListOf(
                Student("Tanu"),
                Student("Tina"),
                Student("Tono")
            )
        }
        val inputField = remember { Student("") }

        HomeContent(
            listData = sampleList,
            inputField = inputField,
            onInputValueChange = {},
            onButtonClick = {},
            navigateFromHomeToResult = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewResultContent() {
    LAB_WEEK_09Theme {
        // Create a fake JSON to simulate data coming from Home screen
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val type = Types.newParameterizedType(List::class.java, Student::class.java)
        val adapter = moshi.adapter<List<Student>>(type)
        val json = adapter.toJson(
            listOf(
                Student("Tanu"),
                Student("Tina"),
                Student("Tono")
            )
        )

        ResultContent(listData = json)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewApp() {
    LAB_WEEK_09Theme {
        // Create a dummy nav controller for preview
        val navController = rememberNavController()
        App(navController = navController)
    }
}

//Here, we create a composable function called ResultContent
//ResultContent accepts a String parameter called listData from the Home composable
//then displays the value of listData to the screen
@Composable
fun ResultContent(listData: String) {
    // ✅ BONUS: Decode JSON back to List<Student> using Moshi
    val moshi = remember { Moshi.Builder().add(KotlinJsonAdapterFactory()).build() }
    val type = Types.newParameterizedType(List::class.java, Student::class.java)
    val adapter = remember { moshi.adapter<List<Student>>(type) }
    val studentList = remember(listData) {
        adapter.fromJson(listData) ?: emptyList()
    }

    // ✅ Use Box to easily center the content vertically and horizontally
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            //Here, we call the OnBackgroundTitleText UI Element
            OnBackgroundTitleText(text = "Submitted Students")

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ Display parsed list nicely
            if (studentList.isNotEmpty()) {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.heightIn(max = 300.dp)
                ) {
                    items(studentList) { item ->
                        OnBackgroundItemText(text = item.name)
                    }
                }
            } else {
                OnBackgroundItemText(text = "No students submitted.")
            }
        }
    }
}
