package com.example.ecocafeconnect.Pages

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ecocafeconnect.AuthState
import com.example.ecocafeconnect.AuthViewModel
import com.example.ecocafeconnect.R
import kotlinx.coroutines.delay

@Composable
fun HomePage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
    val context = LocalContext.current

    // Fetch the saved email from SharedPreferences
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val savedEmail = sharedPreferences.getString("email", "") ?: ""
    val emailWithoutDomain = savedEmail.replace("@gmail.com", "")

    // Show different toast based on the saved email
    Toast.makeText(context, "Hello $emailWithoutDomain", Toast.LENGTH_SHORT).show()


    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                item { Spacer(modifier = Modifier.height(80.dp)) }

                // Show the waste tracker image only if the user is an admin
                if (savedEmail == "admin@gmail.com") {
                    item {
                        Image(
                            painter = painterResource(id = R.drawable.wastrack),
                            contentDescription = "waste",
                            modifier = Modifier
                                .fillMaxWidth()
                                .size(250.dp)
                                .clickable { navController.navigate("waste") }
                        )
                    }
                }

                item {
                    Image(painter = painterResource(id = R.drawable.wastats), contentDescription = "stats",
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(250.dp)
                            .clickable { navController.navigate("stats") })
                }

                item {
                    Carousel(modifier = Modifier.fillMaxWidth())
                }

                item {
                    Image(painter = painterResource(id = R.drawable.daba), contentDescription = "loyaltyProgram",
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(250.dp)
                            .clickable { navController.navigate("ads") })
                }

                item {
                    Image(painter = painterResource(id = R.drawable.article), contentDescription = "article",
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(250.dp)
                            .clickable { navController.navigate("tips") })
                }

                item { Text(text = " ") }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Carousel(modifier: Modifier = Modifier) {
    val images = listOf(
        R.drawable.tip4,
        R.drawable.tip5,
        R.drawable.tip1,
        R.drawable.tip2,
        R.drawable.tip3,
        R.drawable.tip6,
        R.drawable.tip7
    )

    val pagerState = rememberPagerState(pageCount = { images.size })

    Column(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) { page ->
            Image(
                painter = painterResource(id = images[page]),
                contentDescription = "Image $page",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
        }

        PagerIndicator(pagerState = pagerState, modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(top = 16.dp))
    }

    LaunchedEffect(Unit) {
        pagerState.animateScrollToPage(1)
        delay(3000)
        pagerState.animateScrollToPage(2)
        delay(3000)
        pagerState.animateScrollToPage(3)
        delay(3000)
        pagerState.animateScrollToPage(4)
        delay(3000)
        pagerState.animateScrollToPage(5)
        delay(3000)
        pagerState.animateScrollToPage(6)
        delay(3000)
        pagerState.animateScrollToPage(7)
        delay(3000)
        pagerState.animateScrollToPage(0)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerIndicator(pagerState: PagerState, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        val pageCount = pagerState.pageCount
        val currentPage = pagerState.currentPage

        for (i in 0 until pageCount) {
            Indicator(
                isSelected = i == currentPage,
                modifier = Modifier
                    .size(8.dp)
                    .padding(horizontal = 4.dp)
            )
        }
    }
}

@Composable
fun Indicator(isSelected: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(
                color = if (isSelected) Color.White else Color.LightGray,
                shape = RoundedCornerShape(50)
            )
    )
}
