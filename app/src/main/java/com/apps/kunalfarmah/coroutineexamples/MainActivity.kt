package com.apps.kunalfarmah.coroutineexamples

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
        Spacer(Modifier.height(20.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            CoroutineUtils.runBlockingExample()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "runBlocking")
        }
        Spacer(Modifier.height(20.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            coroutineScope.launch {
                CoroutineUtils.coroutineScopeExample()
            }
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "coroutineScope")
        }
        Spacer(Modifier.height(20.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(70.dp), onClick  = {
            coroutineScope.launch {
                CoroutineUtils.supervisorScopeExample()
            }
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "supervisorScope")
        }
        Spacer(Modifier.height(20.dp))
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
        Spacer(Modifier.height(20.dp))
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
        Spacer(Modifier.height(20.dp))
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
        Spacer(Modifier.height(20.dp))
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
        Spacer(Modifier.height(20.dp))
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
        Spacer(Modifier.height(20.dp))
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
        Spacer(Modifier.height(20.dp))
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
        Spacer(Modifier.height(20.dp))
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
        Spacer(Modifier.height(20.dp))
        Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in parent coroutineScope launching separate supervisor scopes")

        //coroutineScope.launch{
        // customSupervisorScope.launch{}
        // customSupervisorScope.launch{}
        // throw
        //}

        //coroutineScope.launch{
        // customSupervisorScope.launch{
        //  launch{}
        //  launch{}
        // }
        // throw
        //}

        //coroutineScope.launch{
        // customSupervisorScope.launch{
        //  customSupervisorScope.launch{}
        //  customSupervisorScope.launch{}
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
        //  supervisorScope{
        //      launch{}
        //  }
        //  supervisorScope{
        //      launch{}
        //  }
        // }
        // throw
        //}
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInParentCoroutineScopeBlockLaunchingSeparateSupervisorScopes0()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 1: launch with separate customSupervisorScope.launch calls")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInParentCoroutineScopeBlockLaunchingSeparateSupervisorScopes1()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 2: launch with separate launch calls inside a customSupervisorScope.launch call")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInParentCoroutineScopeBlockLaunchingSeparateSupervisorScopes2()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 3: launch with separate customSupervisorScope.launch calls inside a customSupervisorScope.launch call")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInParentCoroutineScopeBlockLaunchingSeparateSupervisorScopes3()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 4: launch with separate launch calls inside a one supervisorScope")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInParentCoroutineScopeBlockLaunchingSeparateSupervisorScopes4()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 5: launch with separate launch calls each inside a separate supervisorScopes")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInParentCoroutineScopeBlockLaunchingSeparateSupervisorScopes5()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 6: launch with separate launch calls each inside a separate supervisorScopes inside 1 supervisorScope")
        }



        Spacer(Modifier.height(20.dp))
        Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in async parent coroutineScope launching separate supervisor scopes")

        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncParentCoroutineScopeBlockLaunchingSeparateSupervisorScopes0()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 1: async with separate customSupervisorScope.launch calls")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncParentCoroutineScopeBlockLaunchingSeparateSupervisorScopes1()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 2: async with separate launch calls inside a customSupervisorScope.launch call")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncParentCoroutineScopeBlockLaunchingSeparateSupervisorScopes2()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 3: async with separate customSupervisorScope.launch calls inside a customSupervisorScope.launch call")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncParentCoroutineScopeBlockLaunchingSeparateSupervisorScopes3()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 4: async with separate launch calls inside a one supervisorScope")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncParentCoroutineScopeBlockLaunchingSeparateSupervisorScopes4()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 5: async with separate launch calls each inside a separate supervisorScopes")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncParentCoroutineScopeBlockLaunchingSeparateSupervisorScopes5()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 6: async with separate launch calls each inside a separate supervisorScopes inside 1 supervisorScope")
        }

        Spacer(Modifier.height(20.dp))

        /**
                coroutineScope.launch{
                 customSupervisorScope.launch{}
                 customSupervisorScope.launch{throw}
                }

                coroutineScope.launch{
                 customSupervisorScope.launch{}
                 customSupervisorScope.launch{try{throw}catch}
                }

                coroutineScope.launch{
                 customSupervisorScope.launch{
                  launch{}
                  launch{throw}
                 }
                }

                coroutineScope.launch{
                 customSupervisorScope.launch{
                  launch{}
                  launch{try{throw}catch}
                 }
                }

                coroutineScope.launch{
                 customSupervisorScope.launch{
                  customSupervisorScope.launch{}
                  customSupervisorScope.launch{throw}
                 }
                }

                coroutineScope.launch{
                 customSupervisorScope.launch{
                  customSupervisorScope.launch{}
                  customSupervisorScope.launch{try{throw}catch}
                 }
                }

                coroutineScope.launch{
                 supervisorScope{
                  launch{}
                  launch{throw}
                 }
                }

                coroutineScope.launch{
                 supervisorScope{
                 launch{}
                 }
                 supervisorScope{
                 launch{throw}
                 }
                }

                coroutineScope.launch{
                 supervisorScope{
                  supervisorScope{
                      launch{}
                  }
                  supervisorScope{
                      launch{throw}
                  }
                 }
                }
        */

        Spacer(Modifier.height(20.dp))

        Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in child supervisorScope launched in a launched coroutineScope")

        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInChildSupervisorScopeLaunchedInsideALaunchedCoroutineScope0()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 1: separate customSupervisorScope.launch with exception")
        }
        Spacer(Modifier.height(10.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInChildSupervisorScopeLaunchedInsideALaunchedCoroutineScope1()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 2: separate launch with exception in customSupervisorScope.launch")
        }
        Spacer(Modifier.height(10.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInChildSupervisorScopeLaunchedInsideALaunchedCoroutineScope2()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 3: separate customSupervisorScope.launch with exception inside 1 customSupervisorScope.launch")
        }
        Spacer(Modifier.height(10.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInChildSupervisorScopeLaunchedInsideALaunchedCoroutineScope3()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 4: separate launch with exception inside 1 supervisorScope")
        }
        Spacer(Modifier.height(10.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInChildSupervisorScopeLaunchedInsideALaunchedCoroutineScope4()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 5: separate supervisorScope.launch with exception")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInChildSupervisorScopeLaunchedInsideALaunchedCoroutineScope5()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 6: separate supervisorScope.launch with exception inside 1 supervisorScope")
        }
        Spacer(Modifier.height(20.dp))


        Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in async child supervisorScope in a launched coroutineScope")

        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncChildSupervisorScopeLaunchedInsideALaunchedCoroutineScope0()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 1: separate customSupervisorScope.async with exception")
        }
        Spacer(Modifier.height(10.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncChildSupervisorScopeLaunchedInsideALaunchedCoroutineScope1()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 2: separate async with exception in customSupervisorScope.async")
        }
        Spacer(Modifier.height(10.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncChildSupervisorScopeLaunchedInsideALaunchedCoroutineScope2()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 3: separate customSupervisorScope.async with exception inside 1 customSupervisorScope.async")
        }
        Spacer(Modifier.height(10.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncChildSupervisorScopeLaunchedInsideALaunchedCoroutineScope3()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 4: separate async with exception inside 1 supervisorScope")
        }
        Spacer(Modifier.height(10.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncChildSupervisorScopeLaunchedInsideALaunchedCoroutineScope4()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 5: separate supervisorScope.async with exception")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncChildSupervisorScopeLaunchedInsideALaunchedCoroutineScope5()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 6: separate supervisorScope.async with exception inside 1 supervisorScope")
        }
        Spacer(Modifier.height(20.dp))

        Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in child supervisorScope launched in an async coroutineScope")

        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInChildSupervisorScopeLaunchedInsideAsyncCoroutineScope0()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 1: separate customSupervisorScope.launch with exception")
        }
        Spacer(Modifier.height(10.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInChildSupervisorScopeLaunchedInsideAsyncCoroutineScope1()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 2: separate launch with exception in customSupervisorScope.launch")
        }
        Spacer(Modifier.height(10.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInChildSupervisorScopeLaunchedInsideAsyncCoroutineScope2()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 3: separate customSupervisorScope.launch with exception inside 1 customSupervisorScope.launch")
        }
        Spacer(Modifier.height(10.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInChildSupervisorScopeLaunchedInsideAsyncCoroutineScope3()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 4: separate launch with exception inside 1 supervisorScope")
        }
        Spacer(Modifier.height(10.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInChildSupervisorScopeLaunchedInsideAsyncCoroutineScope4()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 5: separate supervisorScope.launch with exception")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInChildSupervisorScopeLaunchedInsideAsyncCoroutineScope5()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 6: separate supervisorScope.launch with exception inside 1 supervisorScope")
        }
        Spacer(Modifier.height(20.dp))


        Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in async child supervisorScope in an async coroutineScope")

        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncChildSupervisorScopeLaunchedInsideAsyncCoroutineScope0()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 1: separate customSupervisorScope.async with exception")
        }
        Spacer(Modifier.height(10.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncChildSupervisorScopeLaunchedInsideAsyncCoroutineScope1()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 2: separate async with exception in customSupervisorScope.async")
        }
        Spacer(Modifier.height(10.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncChildSupervisorScopeLaunchedInsideAsyncCoroutineScope2()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 3: separate customSupervisorScope.async with exception inside 1 customSupervisorScope.async")
        }
        Spacer(Modifier.height(10.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncChildSupervisorScopeLaunchedInsideAsyncCoroutineScope3()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 4: separate async with exception inside 1 supervisorScope")
        }
        Spacer(Modifier.height(10.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncChildSupervisorScopeLaunchedInsideAsyncCoroutineScope4()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 5: separate supervisorScope.async with exception")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncChildSupervisorScopeLaunchedInsideAsyncCoroutineScope5()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 6: separate supervisorScope.async with exception inside 1 supervisorScope")
        }


        Spacer(Modifier.height(20.dp))



        /**
        customSupervisorScope.launch{
            customSupervisorScope.launch{}
            customSupervisorScope.launch{throw}
        }

        customSupervisorScope.launch{
            launch{}
            launch{throw}
        }

        customSupervisorScope.launch{
            supervisorScope{
                customSupervisorScope.launch{}
                customSupervisorScope.launch{throw}
            }
        }

        customSupervisorScope.launch{
            supervisorScope{
                launch{}
                launch{throw}
            }
        }


        supervisorScope{
            customSupervisorScope.launch{}
            customSupervisorScope.launch{throw}
        }

        supervisorScope{
            launch{}
            launch{throw}
        }

        supervisorScope{
            supervisorScope{
                launch{}
            }
            supervisorScope{
                launch{throw}
            }
        }

         */

        Spacer(Modifier.height(20.dp))

        Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in child supervisorScope launched in a launched supervisorScope")

        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInChildSupervisorScopeLaunchedInsideALaunchedSuperVisorScope0()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 1: separate customSupervisorScope.launch with exception inside a customSupervisorScope.launch")
        }
        Spacer(Modifier.height(10.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInChildSupervisorScopeLaunchedInsideALaunchedSuperVisorScope1()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 2: separate launch with exception in customSupervisorScope.launch")
        }
        Spacer(Modifier.height(10.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInChildSupervisorScopeLaunchedInsideALaunchedSuperVisorScope2()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 3: separate customSupervisorScope.launch with exception inside a supervisorScope launched by a customSupervisorScope")
        }
        Spacer(Modifier.height(10.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInChildSupervisorScopeLaunchedInsideALaunchedSuperVisorScope3()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 4: separate launch with exception inside a supervisorScope launched by a customSupervisorScope")
        }
        Spacer(Modifier.height(10.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInChildSupervisorScopeLaunchedInsideALaunchedSuperVisorScope4()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 5: separate customSupervisorScope.launch with exception inside a launched supervisorScope")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInChildSupervisorScopeLaunchedInsideALaunchedSuperVisorScope5()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 6: separate launch with exception inside a launched supervisorScope")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInChildSupervisorScopeLaunchedInsideALaunchedSuperVisorScope6()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 7: separate launch with exception inside its own supervisorScope nested in a launched supervisorScope")
        }
        Spacer(Modifier.height(20.dp))



        Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in child supervisorScope launched in an async supervisorScope")

        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInChildSupervisorScopeLaunchedInsideAsyncSuperVisorScope0()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 1: separate customSupervisorScope.launch with exception inside a customSupervisorScope.async")
        }
        Spacer(Modifier.height(10.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInChildSupervisorScopeLaunchedInsideAsyncSuperVisorScope1()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 2: separate launch with exception customSupervisorScope.async")
        }
        Spacer(Modifier.height(10.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInChildSupervisorScopeLaunchedInsideAsyncSuperVisorScope2()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 3: separate customSupervisorScope.launch with exception inside a supervisorScope in an async customSupervisorScope")
        }
        Spacer(Modifier.height(10.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInChildSupervisorScopeLaunchedInsideAsyncSuperVisorScope3()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 4: separate launch with exception inside a supervisorScope in an async customSupervisorScope")
        }
        Spacer(Modifier.height(10.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInChildSupervisorScopeLaunchedInsideAsyncSuperVisorScope4()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 5: separate customSupervisorScope.launch with exception inside an async supervisorScope")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInChildSupervisorScopeLaunchedInsideAsyncSuperVisorScope5()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 6: separate launch with exception inside an async supervisorScope")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInChildSupervisorScopeLaunchedInsideAsyncSuperVisorScope6()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 7: separate launch with exception inside its own supervisorScope nested in an async supervisorScope")
        }
        Spacer(Modifier.height(20.dp))


        Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in async child supervisorScope in a launched supervisorScope")

        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncChildSupervisorScopeInsideALaunchedSuperVisorScope0()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 1: separate customSupervisorScope.async with exception inside a customSupervisorScope.launch")
        }
        Spacer(Modifier.height(10.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncChildSupervisorScopeInsideALaunchedSuperVisorScope1()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 2: separate async with exception in customSupervisorScope.launch")
        }
        Spacer(Modifier.height(10.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncChildSupervisorScopeInsideALaunchedSuperVisorScope2()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 3: separate customSupervisorScope.async with exception inside a supervisorScope launched by a customSupervisorScope")
        }
        Spacer(Modifier.height(10.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncChildSupervisorScopeInsideALaunchedSuperVisorScope3()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 4: separate async with exception inside a supervisorScope launched by a customSupervisorScope")
        }
        Spacer(Modifier.height(10.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncChildSupervisorScopeInsideALaunchedSuperVisorScope4()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 5: separate customSupervisorScope.async with exception inside a launched supervisorScope")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncChildSupervisorScopeInsideALaunchedSuperVisorScope5()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 6: separate async with exception inside a launched supervisorScope")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncChildSupervisorScopeInsideALaunchedSuperVisorScope6()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 7: separate async with exception inside its own supervisorScope nested in a launched supervisorScope")
        }
        Spacer(Modifier.height(20.dp))


        Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "exception in async child supervisorScope in an async supervisorScope")

        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncChildSupervisorScopeInsideAsyncSuperVisorScope0()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 1: separate customSupervisorScope.async with exception inside a customSupervisorScope.async")
        }
        Spacer(Modifier.height(10.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncChildSupervisorScopeInsideAsyncSuperVisorScope1()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 2: separate async with exception in customSupervisorScope.async")
        }
        Spacer(Modifier.height(10.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncChildSupervisorScopeInsideAsyncSuperVisorScope2()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 3: separate customSupervisorScope.async with exception inside a supervisorScope in an async customSupervisorScope")
        }
        Spacer(Modifier.height(10.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncChildSupervisorScopeInsideAsyncSuperVisorScope3()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 4: separate async with exception inside a supervisorScope in an async customSupervisorScope")
        }
        Spacer(Modifier.height(10.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncChildSupervisorScopeInsideAsyncSuperVisorScope4()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 5: separate customSupervisorScope.async with exception inside an async supervisorScope")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncChildSupervisorScopeInsideAsyncSuperVisorScope5()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 6: separate async with exception inside an async supervisorScope")
        }
        Spacer(Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp), onClick  = {
            CoroutineUtils.exceptionInAsyncChildSupervisorScopeInsideAsyncSuperVisorScope6()
        }) {
            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "case 7: separate async with exception inside its own supervisorScope nested in an async supervisorScope")
        }
        Spacer(Modifier.height(20.dp))
    }
}
