package com.apps.kunalfarmah.coroutineexamples

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apps.kunalfarmah.coroutineexamples.ui.theme.CoroutineExamplesTheme
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoroutineExamplesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CoroutineExamples(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CoroutineExamples(modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()

    Column(modifier.fillMaxSize().padding(20.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "Examples of Coroutine concepts. Uses Log.d with the tag same as the button text")
        Spacer(Modifier.height(10.dp))
        Button(onClick = {CoroutineUtils.launch()}) {
            Text("Launch")
        }
        Spacer(Modifier.height(10.dp))
        Button(onClick = {CoroutineUtils.async()}) {
            Text("Async")
        }
        Spacer(Modifier.height(10.dp))
        Button(onClick = {
            CoroutineUtils.launchInsideLaunch()
        }) {
            Text("Launch inside launch")
        }
        Spacer(Modifier.height(10.dp))
        Button(onClick = {
            CoroutineUtils.asyncInsideLaunch()
        }) {
            Text("Async inside launch")
        }
        Spacer(Modifier.height(10.dp))
        Button(onClick = {
            CoroutineUtils.launchInsideAsync()
        }) {
            Text("Launch inside async")
        }
        Spacer(Modifier.height(10.dp))
        Button(onClick = {
            CoroutineUtils.asyncInsideAsync()
        }) {
            Text("Async inside async")
        }
        Spacer(Modifier.height(10.dp))
        Button(onClick = {
            CoroutineUtils.runBlockingExample()
        }) {
            Text("runBlocking")
        }
        Spacer(Modifier.height(10.dp))
        Button(onClick = {
            coroutineScope.launch {
                CoroutineUtils.coroutineScopeExample()
            }
        }) {
            Text("coroutineScope")
        }
        Button(onClick = {
            coroutineScope.launch {
                CoroutineUtils.supervisorScopeExample()
            }
        }) {
            Text("supervisorScope")
        }
        Spacer(Modifier.height(10.dp))
        Button(onClick = {
            CoroutineUtils.cancellationInLaunch()
        }) {
            Text("cancellation in launch")
        }
        Spacer(Modifier.height(10.dp))
        Button(onClick = {
            CoroutineUtils.cancellationInAsync()
        }) {
            Text("cancellation in async")
        }
        Spacer(Modifier.height(10.dp))
        Button(onClick = {
            coroutineScope.launch {
                CoroutineUtils.cancellationInSupervisorScope()
            }
        }) {
            Text("cancellation in launch in supervisorScope")
        }
        Spacer(Modifier.height(10.dp))
        Button(onClick = {
            coroutineScope.async {
                CoroutineUtils.cancellationInSupervisorScope()
            }
        }) {
            Text("cancellation in async in supervisorScope")
        }
        Spacer(Modifier.height(10.dp))
        Button(onClick = {
            CoroutineUtils.exceptionInCoroutineScopeAsParentLaunchingChildren()
        }) {
            Text("exception in coroutineScope parent")
        }
        Button(onClick = {
            CoroutineUtils.exceptionInSupervisorScopeAsParentLaunchingChildren()
        }) {
            Text("exception in supervisorScope parent")
        }
        Button(onClick = {
            CoroutineUtils.exceptionInCoroutineScopeChildrenInLaunch()
        }) {
            Text("exception in coroutineScope children via launch")
        }
        Spacer(Modifier.height(10.dp))
        Button(onClick = {
            CoroutineUtils.exceptionInCoroutineScopeChildrenInAsync()
        }) {
            Text("exception in coroutineScope children via async")
        }
        Spacer(Modifier.height(10.dp))
        Button(onClick = {
            coroutineScope.launch(Dispatchers.Default + CoroutineUtils.exceptionHandler + CoroutineName("launch")) {
                CoroutineUtils.exceptionInSuperVisorScopeAsChild()
            }
        }) {
            Text("exception in superVisorScope as child in launch")
        }
        Spacer(Modifier.height(10.dp))
        Button(onClick = {
            coroutineScope.async (Dispatchers.Default + CoroutineUtils.exceptionHandler + CoroutineName("async")) {
                CoroutineUtils.exceptionInSuperVisorScopeAsChild()
            }
        }) {
            Text("exception in superVisorScope as child in async")
        }
        Spacer(Modifier.height(10.dp))
        Button(onClick = {
            CoroutineUtils.exceptionInSuperVisorScopeChildrenInLaunch()
        }) {
            Text("exception in superVisorScope children via launch")
        }
        Spacer(Modifier.height(10.dp))
        Button(onClick = {
            CoroutineUtils.exceptionInSuperVisorScopeChildrenInAsync()
        }) {
            Text("exception in superVisorScope children via async")
        }

        Spacer(Modifier.height(10.dp))
        Button(onClick = {
            CoroutineUtils.exceptionInCoroutineScopeWithSupervisorJobAsContextAsParentInLaunch()
        }) {
            Text("exception in coroutineScope with supervisorJob as context as parent in launch")
        }
        Spacer(Modifier.height(10.dp))
        Button(onClick = {
            CoroutineUtils.exceptionInCoroutineScopeWithSupervisorJobAsContextAsParentInAsync()
        }) {
            Text("exception in coroutineScope with supervisorJob as context as parent in async")
        }
        Spacer(Modifier.height(10.dp))
        Button(onClick = {
            CoroutineUtils.exceptionInCoroutineScopeWithSupervisorJobAsContextAsChildInLaunch()
        }) {
            Text("exception in coroutineScope with supervisorJob as context as child in launch")
        }
        Spacer(Modifier.height(10.dp))
        Button(onClick = {
            CoroutineUtils.exceptionInCoroutineScopeWithSupervisorJobAsContextAsChildInAsync()
        }) {
            Text("exception in coroutineScope with supervisorJob as context as child in async")
        }
        Spacer(Modifier.height(10.dp))
    }
}
