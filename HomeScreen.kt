package com.example.ecocafeconnect

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ecocafeconnect.Pages.HomePage
import com.example.ecocafeconnect.Pages.HomePage.AdsScreen
import com.example.ecocafeconnect.Pages.HomePage.LoyaltyProgram
import com.example.ecocafeconnect.Pages.HomePage.StatisticsScreen
import com.example.ecocafeconnect.Pages.SettingsScreen
import com.example.ecocafeconnect.Pages.HomePage.WasteTracker
import com.example.ecocafeconnect.Pages.LoginScreen
import com.example.ecocafeconnect.Pages.RSTips.TipsPage
import com.example.ecocafeconnect.Pages.SignupPage
import com.example.ecocafeconnect.ui.theme.Blue50
import com.example.ecocafeconnect.ui.theme.EcoCafeConnectTheme
import com.example.ecocafeconnect.wasteTracker.WasteEntryListViewModel
import kotlinx.coroutines.launch

class HomeScreen : ComponentActivity() {

    companion object {
        val items = listOf(
            Item(
                title = "Recycled coffee grounds can be used to make stronger concrete",
                image = R.drawable.t1,
                textTitle = "Concrete can be made 29 per cent stronger by incorporating recycled coffee grounds.\n" +
                        "\n" +
                        "An estimated 18 million tonnes of spent coffee grounds are produced globally each year, with most ending up in landfill. Their decomposition in landfill releases methane, which has a global warming effect 21 times stronger than that of carbon dioxide.\n" +
                        "\n" +
                        "Rajeev Roychand at RMIT University in Melbourne, Australia, and his colleagues wondered if they could keep coffee grounds out of landfill by finding new uses for them in building materials.\n" +
                        "\n" +
                        "They collected used coffee grounds from several local cafes and investigated whether they could be used to replace some of the sand that is typically incorporated into concrete as filler.\n" +
                        "\n" +
                        "In an unmodified state, the spent coffee grounds were found to weaken concrete when they replaced the sand component.\n" +
                        "\n" +
                        "However, they became more useful when the researchers heated them in a 350°C furnace for 2 hours in the absence of oxygen to create a charcoal-like substance called biochar."
            ),
            Item(
                title = "Environmental Issues in the Philippines",
                image = R.drawable.t2,
                textTitle = "Environmental sustainability in the Philippines has long been a critical concern, as the country grapples with the consequences of climate change. Located in Southeast Asia, along the typhoon belt in the Pacific, the archipelagic nation is among the countries most affected by this global phenomenon. Rising temperatures and sea levels have caused problems for its coastal areas and agricultural lands, threatening food security and livelihoods.\n" +
                        "\n" +
                        "In fact, according to the Philippine Statistics Authority, from 2010 to 2019, the damage caused by natural and extreme events and disasters in the country totalled PHP 463 billion (USD 8.27 billion), with agriculture accounting for 62.7% of the total at PHP 290 billion.\n" +
                "\n" + "As the challenges of environmental degradation continue to mount, the importance of environmental sustainability in the Philippines has become an increasingly prominent concern for the Filipino people. In light of this, Standard Insights has surveyed the local population to learn more about their thoughts and opinions on environmental issues. Discover the results and our analysis below."
            ),
            Item(
                title = "Plastic Recycling in the Philippines",
                image = R.drawable.t3,
                textTitle = "School is a perfect venue for students taught proper waste segregation at a very young age. The Department of Environment and Natural Resources, together with the Department of Education strengthen the campaign for the solution to the long-term problem of waste by including 5Rs – Reduce, reuse, recycle, recover and repair – in Science lesson. They aim to promote a massive awareness of how to lessen, if not abolish, waste disposal problems in the Philippines. Academic institutions should make sure to instil the value of being a responsible citizen. These youth may also be instruments in disseminating information by educating their own family.\n" +
                        "\n" +
                        "Recycling programs in the Philippines\n" +
                        "To serve as models, well-known corporations and companies have taken steps to address the issue, start recycling initiatives and different projects in the country.\n" +
                        "\n" +
                        "Here is one example:\n" +
                        "\n" +
                        "Philippine Alliance for Recycling and Materials Sustainability (PARMS)\n" +
                        "Members include Mondelez Philippines, Coca-Cola FEMSA Philippines, Liwayway Marketing Corporation, Pepsi-Cola Products Philippines, Unilever, Universal Robina Corporation, Nestlé Philippines, Monde Nissin Corporation, and Procter & Gamble Philippines. Part of the alliance as well are big groups like the Philippine Chamber of Commerce and Industry, and the Philippine Plastics Industry Association.\n" +
                        "\n" +
                        "PARMS in partnership with the local government unit recently started their project to build a residual plastic recycling facility in Parañaque. They’ve begun to collect plastic waste from schools in the city. The facility will use a clean technology that can process 150 metric tonnes of waste per year, to be converted into pallets, school chairs and eco-bricks or recycled building bricks used in turn to improve the schools’ facilities."
            ),
            Item(
                title = "Recycling for Change",
                image = R.drawable.t4,
                textTitle = "By transforming trash into rewards, this initiative is reshaping people's perceptions about waste.\n" +
                        "\n" +
                        "Ensuring the proper segregation of different types of waste is key to ensure that more waste is recycled and does not end up in landfills or nature. Recyclable exchange stations, where plastic waste is exchanged for coupons or cash is one of the solutions to encourage people to better segregate. The port of Cagayan de Oro is piloting this solution with WWF and BEST.\n" +
                        "\n" +
                        "Picture this: your discarded plastic bottles, old newspapers, and seemingly useless tin cans becoming part of a unique program where they are not only saved from piling up in landfills, but are transformed into valuable assets. This is happening right now in the Philippines, thanks to an innovative program run by Basic Environment Systems and Technology Inc. (BEST).\n" +
                        "\n" +
                        "In the Philippines, over five million tons of trash go into landfills every year. This is a waste of a lot of potential energy, space and resources that could be reused or upcycled into new things. This is something Vince Mercado, President and Managing Director of Extra Philippines, and his mother company, BEST, is eager to change through their “Trash to Cash” program. Since its launch in 2001, they have been challenging the status quo, viewing waste not as an inconvenience, but as a valuable asset that can benefit us all.\n" +
                        "\n" +
                        "By combining customer loyalty and waste disposal management, this groundbreaking program is not just about waste reduction. It is about changing the very way we see and handle waste. By transforming trash into rewards, this initiative is reshaping people's perceptions about waste. Each recyclable material is given a specific number of points, which can be traded for various items, such as goods, services, basic necessities, and even online coupons for bills and shopping. “Cash back is really driven by giving people the capability to earn additional income and to be able to see waste differently,” says Razon."
            ),
            Item(
                title = "How Do We Achieve Sustainability in the Philippines? Simple Answer: Embrace a Net-zero Lifestyle",
                image = R.drawable.t5,
                textTitle = "The Current State of Sustainability in the Philippines\n" +
                        "The Philippines, unfortunately, ranks low in terms of environmental sustainability. According to the Environmental Performance Index (EPI), the country placed 158th out of 180 nations. Poor waste management, deforestation, and air pollution are significant issues facing the country.\n" +
                        "\n" +
                        "This calls for urgent action and a collective effort from all sectors of society to turn the tide towards sustainable living. However, there is hope, as the country has shown increasing awareness of the need for change.\n" +
                        "\n" +
                        "Policy and Regulations: What’s Being Done to Promote Net-Zero Living\n" +
                        "The Philippine government has taken significant steps to promote sustainable practices and encourage a net-zero lifestyle. Policies and regulations such as the Renewable Energy Act in 2008, Climate Change Act in 2009, and National Greening Program in 2011 are in place.\n" +
                        "\n" +
                        "They support the transition towards renewable energy sources, reduce carbon emissions, and promote reforestation efforts. These initiatives aim to create a conducive environment for sustainable development and encourage the adoption of greener practices by individuals, communities, and businesses.\n" +
                        "\n" +
                        "Innovative Technologies and Designs for Net-Zero Homes\n" +
                        "Innovative technologies and designs play a crucial role in pursuing a sustainable lifestyle. Net-zero homes completely offset their carbon emissions both from embodied energy and operational energy over their lifetime. These homes incorporate a variety of technologies, including solar panels, rainwater harvesting systems, energy-efficient appliances, and smart home automation.\n" +
                        "\n" +
                        "Additionally, green building practices, such as using sustainable materials and maximizing natural light and ventilation, are embraced to create eco-friendly and energy-efficient living spaces.\n" +
                        "\n" +
                        "The Role of Private and Public Sectors in Net-Zero Solutions\n" +
                        "Achieving net-zero living requires collaboration between the private and public sectors. Private companies are increasingly investing in renewable energy projects and implementing sustainable practices.\n" +
                        "\n" +
                        "Government agencies, on the other hand, provide incentives, grants, and support for green initiatives. The Department of Environment and Natural Resources has relentlessly encouraged Filipinos to live more sustainably for the future of our environment.\n" +
                        "\n" +
                        "Collaboration between these sectors helps drive innovation, create sustainable jobs, and accelerate the transition to a low-carbon economy.\n" +
                        "\n" +
                        "Pinoys Can Live Sustainable Lifestyles\n" +
                        "The Philippines has always proved its commitment to sustainability through enforcing different initiatives and legislation about climate change, environmental sustainability, and saving the environment in general. Filipinos have always seen Mother Nature as their top supporter in sustaining lifestyles in the Philippines, so that’s why they are often willing to give back to nature for the future generations."
            ),
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val authViewModel: AuthViewModel by viewModels()
        setContent {
            EcoCafeConnectTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyAppNavigation(modifier = Modifier.padding(innerPadding), authViewModel = authViewModel,)
                }
                LearnNavDrawer(navController) // Pass the navController here
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnNavDrawer(navController: NavController){
    val navigationController = rememberNavController()
    val couroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val context = LocalContext.current // Get the context here

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet {
                Box(modifier = Modifier
                    .background(Blue50)
                    .fillMaxWidth()
                    .height(150.dp)){
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(painter = painterResource(id = R.drawable.a), contentDescription = "logo",
                            modifier =  Modifier.fillMaxWidth())
                    }
                }
                Divider()
                NavigationDrawerItem(label = { Text(text = "Home", color = Blue50, fontWeight = FontWeight.SemiBold) },
                    selected = false,
                    icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "homeScreen", tint = Blue50)},
                    onClick = {
                        couroutineScope.launch {
                            drawerState.close()
                        }
                        navigationController.navigate("homepage"){
                            popUpTo(0)
                        }
                    })
                NavigationDrawerItem(label = { Text(text = "Logout", color = Blue50, fontWeight = FontWeight.SemiBold) },
                    selected = false,
                    icon = { Icon(imageVector = Icons.Default.Settings, contentDescription = "settings", tint = Blue50)},
                    onClick = {
                        couroutineScope.launch {
                            drawerState.close()
                        }
                        navigationController.navigate("settings"){
                            popUpTo(0)
                        }
                    })
            }
        },
    ) {
        Scaffold(
            topBar = {
                val coroutineScope = rememberCoroutineScope()
                TopAppBar(
                    title = {
                        Row(
                            modifier = Modifier
                                .padding(start = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ecocafe),
                                contentDescription = "EcoCafe Connect Logo",
                                modifier = Modifier
                                    .size(40.dp)
                            )

                            Text(
                                text = "EcoCafe Connect",
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Blue50,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    ),
                    navigationIcon = {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                Icons.Rounded.Menu, contentDescription = "MenuButton"
                            )

                        }
                    },
                )
            }
        ) {
            NavHost(navController = navigationController,
                startDestination = Screens.HomePage.screens ){
                composable(Screens.HomePage.screens){ HomePage(
                    navController = navigationController,
                    authViewModel = AuthViewModel()
                ) }
                composable(Screens.SettingsScreen.screens){
                    SettingsScreen(
                        navController = navigationController,
                        authViewModel = AuthViewModel(),
                        context = context // Pass context here
                    )
                }
                composable(Screens.LoginScreen.screens){ LoginScreen(
                    navController = navigationController,
                    authViewModel = AuthViewModel()
                ) }
                composable(Screens.SignupPage.screens){ SignupPage(
                    navController = navigationController,
                    authViewModel = AuthViewModel()
                ) }
                composable(Screens.WasteTracker.screens){ WasteTracker(
                    navController = navigationController,
                    authViewModel = AuthViewModel(),
                    viewModel = WasteEntryListViewModel()
                ) }
                composable(Screens.AdsScreen.screens){ AdsScreen(
                    navController = navigationController,
                    authViewModel = AuthViewModel()
                ) }
                composable(Screens.LoyaltyProgram.screens){ LoyaltyProgram(
                    navController = navigationController,
                    authViewModel = AuthViewModel()
                ) }
                composable(Screens.TipsPage.screens){ TipsPage(
                    navController = navigationController,
                    authViewModel = AuthViewModel()
                ) }
                composable(Screens.StatisticsScreen.screens){ StatisticsScreen(
                    navController = navigationController,
                    authViewModel = AuthViewModel(),
                    viewModel = WasteEntryListViewModel()
                ) }
            }
        }
    }
}

@Composable
fun navDrawer(modifier: Modifier = Modifier) {

}
