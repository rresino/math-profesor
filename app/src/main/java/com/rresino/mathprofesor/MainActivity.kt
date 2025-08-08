package com.rresino.mathprofesor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rresino.mathprofesor.ui.theme.MathProfesorTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MathProfesorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DivisionExerciseApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun DivisionExerciseApp(modifier: Modifier = Modifier) {
    var dividend by remember { mutableStateOf(0) }
    var divisor by remember { mutableStateOf(0) }
    var userQuotient by remember { mutableStateOf("") }
    var userRemainder by remember { mutableStateOf("") }
    var feedback by remember { mutableStateOf("") }
    var showAnswer by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf(false) }

    // Generate new division problem
    fun generateNewProblem() {
        divisor = Random.nextInt(2, 10) // Divisor between 2 and 9
        val quotient = Random.nextInt(1, 15) // Quotient between 1 and 14
        val remainder = Random.nextInt(0, divisor) // Remainder less than divisor
        dividend = quotient * divisor + remainder
        userQuotient = ""
        userRemainder = ""
        feedback = ""
        showAnswer = false
        isCorrect = false
    }

    // Initialize with first problem
    LaunchedEffect(Unit) {
        generateNewProblem()
    }

    fun checkAnswer() {
        val enteredQuotient = userQuotient.toIntOrNull()
        val enteredRemainder = userRemainder.toIntOrNull()
        
        if (enteredQuotient != null && enteredRemainder != null) {
            val correctQuotient = dividend / divisor
            val correctRemainder = dividend % divisor
            
            if (enteredQuotient == correctQuotient && enteredRemainder == correctRemainder) {
                feedback = "¡Muy bien! ¡Sigue así!"
                isCorrect = true
                showAnswer = true
            } else {
                feedback = "¡Repasa el resultado! ¡Tú puedes!"
                isCorrect = false
                showAnswer = true
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Title
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Division problem
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.division_problem),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Text(
                    text = "$dividend ÷ $divisor = ?",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Input fields
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Quotient input
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(id = R.string.quotient_label),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = userQuotient,
                    onValueChange = { userQuotient = it },
                    placeholder = { Text(stringResource(id = R.string.enter_quotient)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            // Remainder input
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(id = R.string.remainder_label),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = userRemainder,
                    onValueChange = { userRemainder = it },
                    placeholder = { Text(stringResource(id = R.string.enter_remainder)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }

        // Check answer button
        if (!showAnswer || !isCorrect) {
            Button(
                onClick = { checkAnswer() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = userQuotient.isNotEmpty() && userRemainder.isNotEmpty()
            ) {
                Text(
                    text = stringResource(id = R.string.check_answer),
                    fontSize = 16.sp
                )
            }
        }

        // Feedback message
        if (feedback.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCorrect) 
                        MaterialTheme.colorScheme.primaryContainer 
                    else 
                        MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = feedback,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    color = if (isCorrect) 
                        MaterialTheme.colorScheme.onPrimaryContainer 
                    else 
                        MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // New problem button (only shown after correct answer)
        if (showAnswer && isCorrect) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { generateNewProblem() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.new_problem),
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DivisionExercisePreview() {
    MathProfesorTheme {
        DivisionExerciseApp()
    }
}