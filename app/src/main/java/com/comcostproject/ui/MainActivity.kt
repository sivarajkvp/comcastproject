package com.comcostproject.ui.theme


import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.comcostproject.R
import com.comcostproject.data.model.AnimalDisplayModel
import com.comcostproject.data.model.ResourceState
import com.comcostproject.ui.theme.ComCostProjectTheme
import com.comcostproject.ui.viewmodel.AnimalViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: AnimalViewModel = hiltViewModel()
            ComCostProjectTheme {
                AnimalScreen(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalScreen(viewModel: AnimalViewModel) {
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Animal Explorer", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.DarkGray)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(dimensionResource(R.dimen._16dp))
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                label = { Text("Search by name or common name") },
                textStyle = TextStyle.Default,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen._16dp)))

            // Animal List
            AnimalListScreen(viewModel)
        }
    }
}

@Composable
fun AnimalListScreen(viewModel: AnimalViewModel = hiltViewModel()) {
    val animalState by viewModel.animalData.collectAsState()
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE


    when (animalState) {
        is ResourceState.Loading -> Text("Loading...")
        is ResourceState.Error -> Text("Error: ${(animalState as ResourceState.Error).error}")
        is ResourceState.Success -> {
            val animals = (animalState as ResourceState.Success<List<AnimalDisplayModel>>).data
            if (isLandscape) {
                // Horizontal scrolling in landscape
                LazyRow(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen._16dp))
                ) {
                    items(animals) { animal ->
                        AnimalItem(animal, Modifier.width(dimensionResource(R.dimen._250dp)))
                    }
                }
            } else {
                // Vertical scrolling in portrait
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = dimensionResource(R.dimen._8dp))
                ) {
                    items(animals) { animal ->
                        AnimalItem(animal)
                    }
                }
            }
        }
    }
}

@Composable
fun AnimalItem(animal: AnimalDisplayModel, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(dimensionResource(R.dimen._8dp)),
        shape = RoundedCornerShape(dimensionResource(R.dimen._12dp)),
        elevation = cardElevation(defaultElevation = dimensionResource(R.dimen._8dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(R.dimen._16dp))
                .fillMaxWidth()
        ) {
            Text(text = animal.name, fontWeight = FontWeight.W700, color = Color.Red)
            Text(text = animal.phylum, color = Color.Gray)
            Text(text = animal.scientificName, color = Color.Gray)
            Text(text = animal.extraDetail1, fontWeight = FontWeight.Medium)
            Text(text = animal.extraDetail2, fontWeight = FontWeight.Medium)
        }
    }
    Spacer(modifier = Modifier.height(dimensionResource(R.dimen._16dp)))
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AnimalListScreen()
}