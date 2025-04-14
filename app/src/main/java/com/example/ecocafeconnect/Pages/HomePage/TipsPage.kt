package com.example.ecocafeconnect.Pages.RSTips

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.ecocafeconnect.AuthViewModel
import com.example.ecocafeconnect.HomeScreen
import com.example.ecocafeconnect.Item
import com.example.ecocafeconnect.Screens
import com.example.ecocafeconnect.ui.theme.bebasFamily

@Composable
fun TipsPage(modifier: Modifier = Modifier,navController: NavController,authViewModel: AuthViewModel) {
    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
    ) {

        itemsIndexed(HomeScreen.items) {
                index, item ->
            Text(text = " ")
            Spacer(modifier = Modifier.height(8.dp))
            ColumnItem(item = item)
            Spacer(modifier = Modifier.height(8.dp))

        }

    }

}

@Composable
fun ColumnItem(item: Item) {
    Card(
        modifier = Modifier.padding(5.dp)
    ) {
        Column(
            modifier = Modifier
                .height(490.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                painter = painterResource(id = item.image),
                contentDescription = item.title,
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = item.title, fontWeight = FontWeight.Bold, fontFamily = bebasFamily, fontSize = 30.sp)

        }
        Text(modifier = Modifier.padding(5.dp), text = item.textTitle)

    }
    Text(text = "\n")
    }

