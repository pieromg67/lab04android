package com.example.moviecounter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.moviecounter.ui.theme.MovieCounterTheme

// Modelo de datos para la reseña
data class MovieReview(
    val title: String,
    val rating: Int,
    val comment: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieCounterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MovieReviewScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MovieReviewScreen(modifier: Modifier = Modifier) {
    // Estados persitentes para el formulario
    var title by rememberSaveable { mutableStateOf("") }
    var comment by rememberSaveable { mutableStateOf("") }
    var rating by rememberSaveable { mutableStateOf(5) }

    // Estado de lista observable para almacenar las reseñas agregadas
    val reviews = remember { mutableStateListOf<MovieReview>() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Reseña de Películas (Proyecto Final)",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Formulario de entrada
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Título de la Película") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = comment,
            onValueChange = { comment = it },
            label = { Text("Comentario u Opinión") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Selector de Calificación con botones de texto
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Calificación: ", fontWeight = FontWeight.Medium)
            for (i in 1..5) {
                if (i == rating) {
                    Button(
                        onClick = { rating = i },
                        modifier = Modifier.padding(horizontal = 2.dp)
                    ) {
                        Text("$i ★")
                    }
                } else {
                    OutlinedButton(
                        onClick = { rating = i },
                        modifier = Modifier.padding(horizontal = 2.dp)
                    ) {
                        Text("$i ★")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (title.isNotBlank() && comment.isNotBlank()) {
                    reviews.add(MovieReview(title, rating, comment))
                    title = ""
                    comment = ""
                    rating = 5
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Reseña")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Reseñas Guardadas (${reviews.size}):",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Lista dinámica de reseñas usando LazyColumn
        LazyColumn {
            items(reviews) { review ->
                ReviewCard(review)
            }
        }
    }
}

@Composable
fun ReviewCard(review: MovieReview) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = review.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "★ ${review.rating}/5",
                    color = Color(0xFFFF8F00),
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = review.comment, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieReviewScreenPreview() {
    MovieCounterTheme {
        MovieReviewScreen()
    }
}