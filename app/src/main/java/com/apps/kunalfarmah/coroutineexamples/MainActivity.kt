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
import androidx.compose.foundation.layout.size
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
import kotlinx.coroutines.delay
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

    Column(modifier
        .fillMaxSize()
        .padding(20.dp)
        .verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "Examples of Coroutine concepts. Uses Log.d with the tag same as the button text")
        Spacer(Modifier.height(20.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp)
            .size(70.dp), onClick = {CoroutineUtils.launch()}) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "launch")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {CoroutineUtils.async()}) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "async")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.launchInsideLaunch()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "launch inside launch")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.asyncInsideLaunch()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "async inside launch")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.launchInsideAsync()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "launch inside async")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.asyncInsideAsync()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "async inside async")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.runBlockingExample()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "runBlocking")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            coroutineScope.launch {
                CoroutineUtils.coroutineScopeExample()
            }
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "coroutineScope")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            coroutineScope.launch {
                CoroutineUtils.supervisorScopeExample()
            }
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "supervisorScope")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.cancellationInLaunch()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "cancellation in launch")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.cancellationInAsync()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "cancellation in async")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            coroutineScope.launch {
                CoroutineUtils.cancellationInSupervisorScope()
            }
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "cancellation in launch in supervisorScope")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            coroutineScope.async {
                CoroutineUtils.cancellationInSupervisorScope()
            }
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "cancellation in async in supervisorScope")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.exceptionInCoroutineScopeAsParentLaunchingChildren()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in coroutineScope parent")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.exceptionInCoroutineScopeAsAsyncParentLaunchingChildren()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in coroutineScope as async parent")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.exceptionInSupervisorScopeAsParentLaunchingChildren()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in supervisorJob parent")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.exceptionInSupervisorScopeAsAsyncParentLaunchingChildren()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in supervisorJob as async parent")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.exceptionInCoroutineScopeChildrenInLaunch()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in coroutineScope children via launch")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.exceptionInCoroutineScopeChildrenInAsync()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in coroutineScope children via async")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.exceptionInCoroutineScopeAsyncChildrenInLaunch()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in coroutineScope async children via launch")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.exceptionInCoroutineScopeAsyncChildrenInAsync()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in coroutineScope async children via async")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            coroutineScope.launch(Dispatchers.Default + CoroutineUtils.exceptionHandler + CoroutineName("launch")) {
                CoroutineUtils.exceptionInSuperVisorScopeAsChild()
            }
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in superVisorScope as child in launch")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp)
            .height(30.dp), onClick  = {
            val res = coroutineScope.async (Dispatchers.Default + CoroutineUtils.exceptionHandler + CoroutineName("async")) {
                // the exception will not be caught till we call await
                CoroutineUtils.exceptionInSuperVisorScopeAsChild()

            }
            // uncomment to catch exception
           /* coroutineScope.launch(Dispatchers.Default + CoroutineUtils.exceptionHandler + CoroutineName("crash handler")) {
                res.await()
            }*/
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in superVisorScope as child in async")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.exceptionInSuperVisorScopeChildrenInLaunch()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in superVisorScope children via launch")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.exceptionInSuperVisorScopeChildrenInAsync()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in superVisorScope children via async")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.exceptionInSuperVisorScopeAsyncChildrenInLaunch()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in superVisorScope async children via launch")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.exceptionInSuperVisorScopeAsyncChildrenInAsync()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in superVisorScope async children via async")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.exceptionInParentCoroutineScopeBlockLaunchingSeparateSupervisorScopes()
        }) {
            //coroutineScope.launch{
            // customSupervisorScope.launch{}
            // customSupervisorScope.launch{}
            // throw
            //}

            //coroutineScope.launch{
            // supervisorScope{
            // launch{}
            // }
            // supervisorScope{
            // launch{}
            // }
            // throw
            //}

            //coroutineScope.launch{
            // supervisorScope{
            //  launch{}
            //  launch{}
            //  throw
            // }
            //}

            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in parent coroutineScope launching separate supervisor scopes")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.exceptionInParentAsyncCoroutineScopeLaunchingSeparateSupervisorScopes()
        }) {
            //coroutineScope.async{
            // supervisorScope.launch{}
            // supervisorScope.launch{}
            // throw
            //}
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in async parent coroutineScope launching separate supervisor scopes")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.exceptionInParentCoroutineScopeWithSeparateAsyncSupervisorScopes()
        }) {
            //coroutineScope.launch{
            // supervisorScope.async{}
            // supervisorScope.async{//throw}
            // throw
            //}
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in parent coroutineScope with separate async supervisor scopes")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncParentCoroutineScopeWithSeparateAsyncSupervisorScopes()
        }) {
            //coroutineScope.async{
            // supervisorScope.async{}
            // supervisorScope.async{}
            // throw
            //}
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in parent async coroutineScope with separate async supervisor scopes")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.exceptionInChildSupervisorScopeLaunchedInsideALaunchedCoroutineScope()
        }) {
            //coroutineScope.launch{
            // supervisorScope.launch{}
            // supervisorScope.launch{//throw}
            //}
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in child supervisorScope launched inside a launched coroutineScope")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncChildSupervisorScopeInsideALaunchedCoroutineScope()
        }) {
            //coroutineScope.launch{
            // supervisorScope.async{}
            // supervisorScope.async{// throw}
            //}
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in async child supervisorScope inside a launched coroutineScope")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.exceptionInChildSupervisorScopeLaunchedInsideAnAsyncCoroutineScope()
        }) {
            //coroutineScope.async{
            // supervisorScope.launch{}
            // supervisorScope.launch{// throw}
            //}
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in child supervisorScope launched inside an async coroutineScope")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncChildSupervisorScopeLaunchedInsideAnAsyncCoroutineScope()
        }) {
            //coroutineScope.async{
            // supervisorScope.async{}
            // supervisorScope.async{// throw}
            //}
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in async child supervisorScope inside an async coroutineScope")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.exceptionInParentSupervisorScopeInsideALaunchBlockLaunchingSeparateSupervisorScopes()
        }) {
            //superVisorScope.launch{
               // supervisorScope.launch{}
               // supervisorScope.launch{}
               // throw
            //}
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in parent supervisorScope inside a launch block launching separate supervisor scopes")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.exceptionInParentSupervisorScopeInsideALaunchBlockWithSeparateAsyncSupervisorScopes()
        }) {
            //superVisorScope.launch{
            // supervisorScope.async{}
            // supervisorScope.async{}
            // throw
            //}
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in parent supervisorScope inside a launch block with separate async supervisor scopes")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.exceptionInParentSupervisorScopeInsideAnAsyncBlockLaunchingSeparateSupervisorScopes()
        }) {
            //superVisorScope.async{
            // supervisorScope.launch{}
            // supervisorScope.launch{}
            // throw
            //}
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in parent supervisorScope inside an async block launching separate supervisor scopes")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.exceptionInParentSupervisorScopeInsideAnAsyncBlockWithSeparateAyncSupervisorScopes()
        }) {
            //superVisorScope.async{
            // supervisorScope.async{}
            // supervisorScope.async{}
            // throw
            //}
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in parent supervisorScope inside an async block with separate async supervisor scopes")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.exceptionInChildSupervisorScopeLaunchedInsideALaunchedSupervisorScope()
        }) {
            //superVisorScope.launch{
            // supervisorScope.launch{}
            // supervisorScope.launch{//throw}
            //}
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in child supervisorScope launched inside a launched supervisorScope")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncChildSupervisorScopeLaunchedInsideALaunchedSupervisorScope()
        }) {
            //superVisorScope.launch{
            // supervisorScope.async{}
            // supervisorScope.async{// throw}
            //}
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in async child supervisorScope inside a launched supervisorScope")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.exceptionInChildSupervisorScopeLaunchedInsideAnAsyncSupervisorScope()
        }) {
            //superVisorScope.async{
            // supervisorScope.launch{}
            // supervisorScope.launch{// throw}
            //}
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in child supervisorScope launched inside an async supervisorScope")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncChildSupervisorScopeLaunchedInsideAnAsyncSupervisorScope()
        }) {
            //superVisorScope.async{
            // supervisorScope.async{}
            // supervisorScope.async{// throw}
            //}
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in async child supervisorScope inside an async supervisor scope")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.exceptionInCoroutineScopeWithSupervisorJobAsContextAsParentInLaunch()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in coroutineScope with supervisorJob as context as parent in launch")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.exceptionInCoroutineScopeWithSupervisorJobAsContextAsParentInLaunch()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in coroutineScope with supervisorJob as context as parent in launch")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.exceptionInCoroutineScopeWithSupervisorJobAsContextAsParentInAsync()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in coroutineScope with supervisorJob as context as parent in async")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.exceptionInCoroutineScopeWithSupervisorJobAsContextAsChildInLaunch()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in coroutineScope with supervisorJob as context as child in launch")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.exceptionInCoroutineScopeWithSupervisorJobAsContextAsChildInAsync()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in coroutineScope with supervisorJob as context as child in async")
        }
        Spacer(Modifier.height(10.dp))
    }
}
