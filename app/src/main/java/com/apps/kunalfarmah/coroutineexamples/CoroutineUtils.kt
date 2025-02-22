package com.apps.kunalfarmah.coroutineexamples

import android.util.Log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope

@OptIn(ExperimentalCoroutinesApi::class)
object CoroutineUtils {

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.d("CoroutineUtils exceptionHandler", "exception ${throwable.javaClass.name} in ${coroutineContext[CoroutineName]?.name} running a ${coroutineContext[Job].toString()}  , ${throwable.message}")
    }

    private val customSupervisorScope = CoroutineScope(SupervisorJob() +  Dispatchers.Default + exceptionHandler + CoroutineName("supervisorJob"))

    fun launch(){
       CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).launch {
           Log.d("CoroutineUtils launch","started")
           delay(3000)
           Log.d("CoroutineUtils launch","finished")
       }.invokeOnCompletion{
           if(it==null){
               Log.d("CoroutineUtils launch","completed")
           }
       }
    }

    fun async() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("async")).async {
            Log.d("CoroutineUtils async","started")
            delay(3000)
            Log.d("CoroutineUtils async","finished")
            "completed"
        }.invokeOnCompletion{
            if(it == null){
                Log.d("CoroutineUtils async","completed")
            }
        }
    }

    private suspend fun blockingFunction(){
        Log.d("CoroutineUtils blockingFunction"," blockingFunction started")
        delay(5000)
        Log.d("CoroutineUtils blockingFunction","blockingFunction finished")
    }

    fun launchInsideLaunch(){
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch 1")).launch {
            Log.d("CoroutineUtils launchInsideLaunch","first started")
            delay(3000)
            // a launch block inside a launch block will not suspend the parent coroutine
            launch(CoroutineName("launch 2")) {
                Log.d("CoroutineUtils launchInsideLaunch","second started")
                delay(2000)
                Log.d("CoroutineUtils launchInsideLaunch","second finished")
            }.invokeOnCompletion{
                if(it == null){
                    Log.d("CoroutineUtils launchInsideLaunch","second completed")
                }
            }
            // a suspend function inside a launch block will suspend the parent coroutine
            blockingFunction()
            // a launch block inside a launch block will not suspend the parent coroutine
            launch(CoroutineName("launch 3")) {
                Log.d("CoroutineUtils launchInsideLaunch","third started")
                delay(4000)
                Log.d("CoroutineUtils launchInsideLaunch","third finished")
            }.invokeOnCompletion{
                if(it == null){
                    Log.d("CoroutineUtils launchInsideLaunch","third completed")
                }
            }
            Log.d("CoroutineUtils launchInsideLaunch","first finished")
        }.invokeOnCompletion{
            if(it == null){
                Log.d("CoroutineUtils launchInsideLaunch","completed")
            }
        }
    }

    fun asyncInsideLaunch(){
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).launch {
            Log.d("CoroutineUtils asyncInsideLaunch","started")
            delay(3000)
            val def1 = async(CoroutineName("async 1")){
                Log.d("CoroutineUtils asyncInsideLaunch","async 1 started")
                delay(2000)
                Log.d("CoroutineUtils asyncInsideLaunch","async 1 finished")
                "async 1 completed"
            }
            // a suspend function inside a async block will suspend the parent coroutine
            // uncommenting this will not launch async 2 till the function has finished execution
            // blockingFunction()
            val def2 = async(CoroutineName("async 2")){
                Log.d("CoroutineUtils asyncInsideLaunch","async 2 started")
                delay(4000)
                Log.d("CoroutineUtils asyncInsideLaunch","async 2 finished")
                "async 2 completed"
            }
            Log.d("CoroutineUtils asyncInsideLaunch","${def1.await()}, ${def2.await()}")
            Log.d("CoroutineUtils asyncInsideLaunch","finished")
        }.invokeOnCompletion{
            if(it == null){
                Log.d("CoroutineUtils asyncInsideLaunch","completed")
            }
        }
    }

    fun asyncInsideAsync(){
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("async")).async {
            Log.d("CoroutineUtils asyncInsideAsync","started")
            delay(3000)
            val def1 = async(CoroutineName("async 1")){
                Log.d("CoroutineUtils asyncInsideAsync","async 1 started")
                delay(2000)
                Log.d("CoroutineUtils asyncInsideAsync","async 1 finished")
                "async 1 completed"
            }
            val def2 = async(CoroutineName("async 2")){
                Log.d("CoroutineUtils asyncInsideAsync","async 2 started")
                delay(4000)
                Log.d("CoroutineUtils asyncInsideAsync","async 2 finished")
                "async 2 completed"
            }
            Log.d("CoroutineUtils asyncInsideAsync","${def1.await()}, ${def2.await()}")
            Log.d("CoroutineUtils asyncInsideAsync","finished")
        }.invokeOnCompletion{
            if(it == null){
                Log.d("CoroutineUtils asyncInsideAsync","completed")
            }
        }
    }

    fun launchInsideAsync(){
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("async 1")).async {
            Log.d("CoroutineUtils launchInsideAsync","first started")
            delay(3000)
            launch(CoroutineName("launch 2")) {
                Log.d("CoroutineUtils launchInsideAsync","second started")
                delay(2000)
                Log.d("CoroutineUtils launchInsideAsync","second finished")
            }.invokeOnCompletion{
                if(it == null){
                    Log.d("CoroutineUtils launchInsideAsync","second completed")
                }
            }
            launch(CoroutineName("launch 3")) {
                Log.d("CoroutineUtils launchInsideAsync","third started")
                delay(4000)
                Log.d("CoroutineUtils launchInsideAsync","third finished")
            }.invokeOnCompletion{
                if(it == null){
                    Log.d("CoroutineUtils launchInsideAsync","third completed")
                }
            }
            Log.d("CoroutineUtils launchInsideAsync","first finished")
            "async completed"
        }.invokeOnCompletion{
            if(it == null){
                Log.d("CoroutineUtils launchInsideAsync","completed")
            }
        }
    }

    fun runBlockingExample(){
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("runBlocking")).launch {
            Log.d("CoroutineUtils runBlocking","runBlocking started")
            // this will prevent the parent coroutine to finish till its scope has completed
            // similar to calling blockingFunction
            runBlocking {
                coroutineScope {
                    launch(CoroutineName("launch 1")) {
                        Log.d("CoroutineUtils runBlocking", "launch 1 started")
                        delay(2000)
                        Log.d("CoroutineUtils runBlocking", "launch 1 finished")
                    }
                }
            }
            Log.d("CoroutineUtils runBlocking", "runBlocking finished")
        }.invokeOnCompletion {
            if(it == null){
                Log.d("CoroutineUtils runBlocking","runBlocking completed")
            }
        }
    }

    suspend fun coroutineScopeExample(){
        coroutineScope {
            launch(Dispatchers.Default + exceptionHandler + CoroutineName("coroutineScoped")){
                Log.d("CoroutineUtils coroutineScope","started")
                delay(2000)
                Log.d("CoroutineUtils coroutineScope","finished")
            }.invokeOnCompletion{
                if(it == null){
                    Log.d("CoroutineUtils coroutineScope","completed")
                }
            }
        }
    }

    suspend fun supervisorScopeExample(){
        supervisorScope {
            launch(Dispatchers.Default + exceptionHandler + CoroutineName("supervisorScoped")){
                Log.d("CoroutineUtils supervisorScope","started")
                delay(2000)
                Log.d("CoroutineUtils supervisorScope","finished")
            }.invokeOnCompletion{
                if(it == null){
                    Log.d("CoroutineUtils supervisorScope","completed")
                }
            }
        }
    }

    fun cancellationInLaunch() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).launch {
            Log.d("CoroutineUtils cancellationInLaunch","started")
            delay(2000)
            val child1 = launch(CoroutineName("child 1")) {
                Log.d("CoroutineUtils cancellationInLaunch","child 1 started")
                delay(2000)
                Log.d("CoroutineUtils cancellationInLaunch","child 1 finished")
            }
            val child2 = launch(CoroutineName("child 2")) {
                Log.d("CoroutineUtils cancellationInLaunch","child 2 started")
                delay(5000)
                Log.d("CoroutineUtils cancellationInLaunch","child 2 finished")
            }
            val child3 = launch(CoroutineName("child 3")) {
                Log.d("CoroutineUtils cancellationInLaunch","child 3 started")
                delay(6000)
                Log.d("CoroutineUtils cancellationInLaunch","child 3 finished")
            }
            child1.invokeOnCompletion { e1 ->
                if(e1 == null){
                    Log.d("CoroutineUtils cancellationInLaunch","child 1 completed")
                }
                else {
                    Log.d("CoroutineUtils cancellationInLaunch", "child 1 cancelled ${e1.message}")
                }
            }
            child2.invokeOnCompletion { e2 ->
                    if(e2 == null){
                        Log.d("CoroutineUtils cancellationInLaunch","child 2 completed")
                    }
                    else{
                        Log.d("CoroutineUtils cancellationInLaunch","child 2 cancelled ${e2.message}")
                    }
            }
            child3.invokeOnCompletion { e3 ->
                if(e3 == null){
                    Log.d("CoroutineUtils cancellationInLaunch","child 3 completed")
                }
                else {
                    Log.d("CoroutineUtils cancellationInLaunch", "child 3 cancelled ${e3.message}")
                }
            }
            delay(3000)
            // this will stop child 2 while child 1 and 3 will complete
            child2.cancel(CancellationException("Cancelling child 2"))
            Log.d("CoroutineUtils cancellationInLaunch","finished")
        }.invokeOnCompletion {
            if(it == null){
                Log.d("CoroutineUtils cancellationInLaunch","completed")
            }
        }
    }

    fun cancellationInAsync() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("async")).async {
            Log.d("CoroutineUtils cancellationInLaunch","started")
            delay(2000)
            val child1 = launch(CoroutineName("child 1")) {
                Log.d("CoroutineUtils cancellationInLaunch","child 1 started")
                delay(2000)
                Log.d("CoroutineUtils cancellationInLaunch","child 1 finished")
            }
            val child2 = launch(CoroutineName("child 2")) {
                Log.d("CoroutineUtils cancellationInLaunch","child 2 started")
                delay(5000)
                Log.d("CoroutineUtils cancellationInLaunch","child 2 finished")
            }
            val child3 = launch(CoroutineName("child 3")) {
                Log.d("CoroutineUtils cancellationInLaunch","child 3 started")
                delay(6000)
                Log.d("CoroutineUtils cancellationInLaunch","child 3 finished")
            }
            child1.invokeOnCompletion { e1 ->
                if(e1 == null){
                    Log.d("CoroutineUtils cancellationInLaunch","child 1 completed")
                }
                else {
                    Log.d("CoroutineUtils cancellationInLaunch", "child 1 cancelled ${e1.message}")
                }
            }
            child2.invokeOnCompletion { e2 ->
                if(e2 == null){
                    Log.d("CoroutineUtils cancellationInLaunch","child 2 completed")
                }
                else{
                    Log.d("CoroutineUtils cancellationInLaunch","child 2 cancelled ${e2.message}")
                }
            }
            child3.invokeOnCompletion { e3 ->
                if(e3 == null){
                    Log.d("CoroutineUtils cancellationInLaunch","child 3 completed")
                }
                else {
                    Log.d("CoroutineUtils cancellationInLaunch", "child 3 cancelled ${e3.message}")
                }
            }
            delay(3000)
            // this will stop child 2 while child 1 and 3 will complete
            child2.cancel(CancellationException("Cancelling child 2"))
            Log.d("CoroutineUtils cancellationInLaunch","finished")
        }.invokeOnCompletion {
            if(it == null){
                Log.d("CoroutineUtils cancellationInLaunch","completed")
            }
        }
    }

    suspend fun cancellationInSupervisorScope() {
        supervisorScope {
            Log.d("CoroutineUtils cancellationInLaunch", "supervisorScope started")
            delay(2000)
            val child1 = launch(CoroutineName("child 1")) {
                Log.d("CoroutineUtils cancellationInLaunch", "child 1 started")
                delay(2000)
                Log.d("CoroutineUtils cancellationInLaunch", "child 1 finished")
            }
            val child2 = launch(CoroutineName("child 2")) {
                Log.d("CoroutineUtils cancellationInLaunch", "child 2 started")
                delay(5000)
                Log.d("CoroutineUtils cancellationInLaunch", "child 2 finished")
            }
            val child3 = launch(CoroutineName("child 3")) {
                Log.d("CoroutineUtils cancellationInLaunch", "child 3 started")
                delay(6000)
                Log.d("CoroutineUtils cancellationInLaunch", "child 3 finished")
            }
            child1.invokeOnCompletion { e1 ->
                if (e1 == null) {
                    Log.d("CoroutineUtils cancellationInLaunch", "child 1 completed")
                } else {
                    Log.d("CoroutineUtils cancellationInLaunch", "child 1 cancelled ${e1.message}")
                }
            }
            child2.invokeOnCompletion { e2 ->
                if (e2 == null) {
                    Log.d("CoroutineUtils cancellationInLaunch", "child 2 completed")
                } else {
                    Log.d("CoroutineUtils cancellationInLaunch", "child 2 cancelled ${e2.message}")
                }
            }
            child3.invokeOnCompletion { e3 ->
                if (e3 == null) {
                    Log.d("CoroutineUtils cancellationInLaunch", "child 3 completed")
                } else {
                    Log.d("CoroutineUtils cancellationInLaunch", "child 3 cancelled ${e3.message}")
                }
            }
            delay(3000)
            // this will stop child 2 while child 1 and 3 will complete
            child2.cancel(CancellationException("Cancelling child 2"))
            Log.d("CoroutineUtils cancellationInLaunch", "supervisorScope finished")
        }
    }

    // if the parent throws exception, all children will be cancelled and exception will be caught
    fun exceptionInCoroutineScopeAsParentLaunchingChildren() {
        // exception will be caught by the exceptionHandler
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).launch {
            Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "started")
            delay(2000)
            val child1 = launch(CoroutineName("child 1")) {
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 1 started")
                delay(2000)
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 1 finished")
            }
            val child2 = launch(CoroutineName("child 2")) {
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 2 started")
                delay(5000)
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 2 finished")
            }
            val child3 = launch(CoroutineName("child 3")) {
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 3 started")
                delay(6000)
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 3 finished")
            }
            child1.invokeOnCompletion { e1 ->
                if (e1 == null) {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 1 completed")
                } else {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 1 cancelled ${e1.message}")
                }
            }
            child2.invokeOnCompletion { e2 ->
                if (e2 == null) {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 2 completed")
                } else {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 2 cancelled ${e2.message}")
                }
            }
            child3.invokeOnCompletion { e3 ->
                if (e3 == null) {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 3 completed")
                } else {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 3 cancelled ${e3.message}")
                }
            }
            delay(3000)
            // this will cancel child 2 and child 3 while child 1 successfully completed
            throw IllegalStateException("parent crashed :(")
            Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "finished")

        }
    }

    // if the parent throws exception, all children will be cancelled even if the parent is a supervisor job
    // please note that  CoroutineScope(SupervisorJob() + Dispatchers.Default + exceptionHandler + CoroutineName("launch")) DOES not launch the
    // coroutine in the supervisor scope
    fun exceptionInSupervisorScopeAsParentLaunchingChildren() {
        // exception will be caught by the exceptionHandler
        CoroutineScope(SupervisorJob() + Dispatchers.Default + exceptionHandler + CoroutineName("launch")).launch {
            Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "started")
            delay(2000)
            val child1 = launch(CoroutineName("child 1")) {
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 1 started")
                delay(2000)
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 1 finished")
            }
            val child2 = launch(CoroutineName("child 2")) {
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 2 started")
                delay(5000)
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 2 finished")
            }
            val child3 = launch(CoroutineName("child 3")) {
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 3 started")
                delay(6000)
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 3 finished")
            }
            child1.invokeOnCompletion { e1 ->
                if (e1 == null) {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 1 completed")
                } else {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 1 cancelled ${e1.message}")
                }
            }
            child2.invokeOnCompletion { e2 ->
                if (e2 == null) {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 2 completed")
                } else {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 2 cancelled ${e2.message}")
                }
            }
            child3.invokeOnCompletion { e3 ->
                if (e3 == null) {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 3 completed")
                } else {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 3 cancelled ${e3.message}")
                }
            }
            delay(3000)
            // this will cancel child 2 and child 3 while child 1 successfully completed
            throw IllegalStateException("parent crashed :(")
            Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "finished")

        }
    }

    // if the parent throws exception, all children will be cancelled by exception will only be caught on calling await
    fun exceptionInCoroutineScopeAsAsyncParentLaunchingChildren() {
        // exception will be caught by the exceptionHandler
        val res = CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).async {
            Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "started")
            delay(2000)
            val child1 = launch(CoroutineName("child 1")) {
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 1 started")
                delay(2000)
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 1 finished")
            }
            val child2 = launch(CoroutineName("child 2")) {
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 2 started")
                delay(5000)
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 2 finished")
            }
            val child3 = launch(CoroutineName("child 3")) {
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 3 started")
                delay(6000)
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 3 finished")
            }
            child1.invokeOnCompletion { e1 ->
                if (e1 == null) {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 1 completed")
                } else {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 1 cancelled ${e1.message}")
                }
            }
            child2.invokeOnCompletion { e2 ->
                if (e2 == null) {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 2 completed")
                } else {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 2 cancelled ${e2.message}")
                }
            }
            child3.invokeOnCompletion { e3 ->
                if (e3 == null) {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 3 completed")
                } else {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 3 cancelled ${e3.message}")
                }
            }
            delay(3000)
            // this will cancel child 2 and child 3 while child 1 successfully completed
            throw IllegalStateException("parent crashed :(")
            Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "finished")

        }

        // uncomment to catch exception
        /*CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).launch {
            res.await()
        }*/
    }

    // if the parent throws exception, all children will be cancelled even if the parent is a supervisor job
    // please note that  CoroutineScope(SupervisorJob() + Dispatchers.Default + exceptionHandler + CoroutineName("launch")) will still propagate error upwards
    fun exceptionInSupervisorScopeAsAsyncParentLaunchingChildren() {
        // exception will be caught by the exceptionHandler
        val res = CoroutineScope(SupervisorJob() + Dispatchers.Default + exceptionHandler + CoroutineName("launch")).async {
            Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "started")
            delay(2000)
            val child1 = launch(CoroutineName("child 1")) {
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 1 started")
                delay(2000)
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 1 finished")
            }
            val child2 = launch(CoroutineName("child 2")) {
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 2 started")
                delay(5000)
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 2 finished")
            }
            val child3 = launch(CoroutineName("child 3")) {
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 3 started")
                delay(6000)
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 3 finished")
            }
            child1.invokeOnCompletion { e1 ->
                if (e1 == null) {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 1 completed")
                } else {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 1 cancelled ${e1.message}")
                }
            }
            child2.invokeOnCompletion { e2 ->
                if (e2 == null) {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 2 completed")
                } else {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 2 cancelled ${e2.message}")
                }
            }
            child3.invokeOnCompletion { e3 ->
                if (e3 == null) {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 3 completed")
                } else {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "child 3 cancelled ${e3.message}")
                }
            }
            delay(3000)
            // this will cancel child 2 and child 3 while child 1 successfully completed
            throw IllegalStateException("parent crashed :(")
            Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentLaunchingChildren", "finished")

            // uncomment to catch exception
            /*CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).launch {
                res.await()
            }*/

        }
    }


    fun exceptionInCoroutineScopeChildrenInLaunch() {
        // exception will be caught by the exceptionHandler
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).launch {
            Log.d("CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch", "started")
            delay(2000)
            val child1 = launch(CoroutineName("child 1")) {
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch", "child 1 started")
                delay(2000)
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch", "child 1 finished")
            }
            val child2 = launch(CoroutineName("child 2")) {
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch", "child 2 started")
                delay(5000)
                // this will cancel child 2 and child 3 while child 1 successfully completed
                throw IllegalStateException("child 2 crashed :(")
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch", "child 2 finished")
            }
            val child3 = launch(CoroutineName("child 3")) {
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch", "child 3 started")
                delay(6000)
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch", "child 3 finished")
            }
            child1.invokeOnCompletion { e1 ->
                if (e1 == null) {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch", "child 1 completed")
                } else {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch", "child 1 cancelled ${e1.message}")
                }
            }
            child2.invokeOnCompletion { e2 ->
                if (e2 == null) {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch", "child 2 completed")
                } else {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch", "child 2 cancelled ${e2.message}")
                }
            }
            child3.invokeOnCompletion { e3 ->
                if (e3 == null) {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch", "child 3 completed")
                } else {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch", "child 3 cancelled ${e3.message}")
                }
            }
            Log.d("CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch", "finished")

        }
    }

    fun exceptionInCoroutineScopeChildrenInAsync() {
        // one misconception is that in async exceptions will not affect other children
        // In reality children and the parent job will get cancelled normally
        // It is just that the exception will not be caught till await is called on the deferred
        val result = CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).async {
            Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentInAsync", "started")
            delay(2000)
            val child1 = launch(CoroutineName("child 1")) {
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentInAsync", "child 1 started")
                delay(2000)
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentInAsync", "child 1 finished")
            }
            val child2 = launch(CoroutineName("child 2")) {
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentInAsync", "child 2 started")
                delay(5000)
                // this will cancel child 2 and child 3 while child 1 successfully completed
                throw IllegalStateException("child 2 crashed :(")
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentInAsync", "child 2 finished")
            }
            val child3 = launch(CoroutineName("child 3")) {
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentInAsync", "child 3 started")
                delay(6000)
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentInAsync", "child 3 finished")
            }
            child1.invokeOnCompletion { e1 ->
                if (e1 == null) {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentInAsync", "child 1 completed")
                } else {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentInAsync", "child 1 cancelled ${e1.message}")
                }
            }
            child2.invokeOnCompletion { e2 ->
                if (e2 == null) {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentInAsync", "child 2 completed")
                } else {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentInAsync", "child 2 cancelled ${e2.message}")
                }
            }
            child3.invokeOnCompletion { e3 ->
                if (e3 == null) {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentInAsync", "child 3 completed")
                } else {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentInAsync", "child 3 cancelled ${e3.message}")
                }
            }
            Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentInAsync", "finished")

        }

        // uncomment this to catch the exception that was contained in the async block
        /*CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("crash detector")).launch {
            Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentInAsync", "Exception hasn't been caught yet, will be caught once await is called")
            // this will throw the exception that was contained in the async block
            result.await()
        }*/
    }

    fun exceptionInCoroutineScopeAsyncChildrenInLaunch() {
        // exception will be caught by the exceptionHandler
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).launch {
            Log.d("CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch", "started")
            delay(2000)
            val child1 = async(CoroutineName("child 1")) {
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch", "child 1 started")
                delay(2000)
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch", "child 1 finished")
            }
            val child2 = async(CoroutineName("child 2")) {
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch", "child 2 started")
                delay(5000)
                // this will cancel child 2 and child 3 while child 1 successfully completed
                // the exception will not be caught until await is called
                throw IllegalStateException("child 2 crashed :(")
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch", "child 2 finished")
            }
            val child3 = async(CoroutineName("child 3")) {
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch", "child 3 started")
                delay(6000)
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch", "child 3 finished")
            }
            child1.invokeOnCompletion { e1 ->
                if (e1 == null) {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch", "child 1 completed")
                } else {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch", "child 1 cancelled ${e1.message}")
                }
            }
            child2.invokeOnCompletion { e2 ->
                if (e2 == null) {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch", "child 2 completed")
                } else {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch", "child 2 cancelled ${e2.message}")
                }
            }
            child3.invokeOnCompletion { e3 ->
                if (e3 == null) {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch", "child 3 completed")
                } else {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch", "child 3 cancelled ${e3.message}")
                }
            }
            // uncomment to catch exception
            // child2.await()
            Log.d("CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch", "finished")

        }
    }

    fun exceptionInCoroutineScopeAsyncChildrenInAsync() {
        // one misconception is that in async exceptions will not affect other children
        // In reality children and the parent job will get cancelled normally
        // It is just that the exception will not be caught till await is called on the deferred
        val result = CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).async {
            Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentInAsync", "started")
            delay(2000)
            val child1 = async(CoroutineName("child 1")) {
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentInAsync", "child 1 started")
                delay(2000)
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentInAsync", "child 1 finished")
            }
            val child2 = async(CoroutineName("child 2")) {
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentInAsync", "child 2 started")
                delay(5000)
                // this will cancel child 2 and child 3 while child 1 successfully completed
                // the exception will not be caught until await is called
                throw IllegalStateException("child 2 crashed :(")
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentInAsync", "child 2 finished")
            }
            val child3 = async(CoroutineName("child 3")) {
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentInAsync", "child 3 started")
                delay(6000)
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentInAsync", "child 3 finished")
            }
            child1.invokeOnCompletion { e1 ->
                if (e1 == null) {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentInAsync", "child 1 completed")
                } else {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentInAsync", "child 1 cancelled ${e1.message}")
                }
            }
            child2.invokeOnCompletion { e2 ->
                if (e2 == null) {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentInAsync", "child 2 completed")
                } else {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentInAsync", "child 2 cancelled ${e2.message}")
                }
            }
            child3.invokeOnCompletion { e3 ->
                if (e3 == null) {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentInAsync", "child 3 completed")
                } else {
                    Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentInAsync", "child 3 cancelled ${e3.message}")
                }
            }
            // uncomment to catch exception
            // child2.await()
            Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentInAsync", "finished")

        }

        // uncomment this to catch the exception that was contained in the async block
        /*CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("crash detector")).launch {
            Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentInAsync", "Exception hasn't been caught yet, will be caught once await is called")
            // this will throw the exception that was contained in the async block
            result.await()
        }*/
    }


    suspend fun exceptionInSuperVisorScopeAsChild() {
        // even if it is a supervisorJob, parent's exception will cancel all children
        supervisorScope {
            Log.d("CoroutineUtils exceptionInSuperVisorScopeAsChild", "started")
            delay(2000)
            val child1 = launch(CoroutineName("child 1")) {
                Log.d("CoroutineUtils exceptionInSuperVisorScopeAsChild", "child 1 started")
                delay(2000)
                Log.d("CoroutineUtils exceptionInSuperVisorScopeAsChild", "child 1 finished")
            }
            val child2 = launch(CoroutineName("child 2")) {
                Log.d("CoroutineUtils exceptionInSuperVisorScopeAsChild", "child 2 started")
                delay(5000)
                Log.d("CoroutineUtils exceptionInSuperVisorScopeAsChild", "child 2 finished")
            }
            val child3 = launch(CoroutineName("child 3")) {
                Log.d("CoroutineUtils exceptionInSuperVisorScopeAsChild", "child 3 started")
                delay(6000)
                Log.d("CoroutineUtils exceptionInSuperVisorScopeAsChild", "child 3 finished")
            }
            child1.invokeOnCompletion { e1 ->
                if (e1 == null) {
                    Log.d("CoroutineUtils exceptionInSuperVisorScopeAsChild", "child 1 completed")
                } else {
                    Log.d("CoroutineUtils exceptionInSuperVisorScopeAsChild", "child 1 cancelled ${e1.message}")
                }
            }
            child2.invokeOnCompletion { e2 ->
                if (e2 == null) {
                    Log.d("CoroutineUtils exceptionInSuperVisorScopeAsChild", "child 2 completed")
                } else {
                    Log.d("CoroutineUtils exceptionInSuperVisorScopeAsChild", "child 2 cancelled ${e2.message}")
                }
            }
            child3.invokeOnCompletion { e3 ->
                if (e3 == null) {
                    Log.d("CoroutineUtils exceptionInSuperVisorScopeAsChild", "child 3 completed")
                } else {
                    Log.d("CoroutineUtils exceptionInSuperVisorScopeAsChild", "child 3 cancelled ${e3.message}")
                }
            }
            delay(3000)
            // this will cancel child 2 and child 3 while child 1 successfully completed
            throw IllegalStateException("parent crashed :(")
            Log.d("CoroutineUtils exceptionInSuperVisorScopeAsChild", "finished")
        }
    }



    fun exceptionInSuperVisorScopeChildrenInLaunch(){

        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).launch {
            supervisorScope {
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch", "started")
                delay(2000)
                val child1 = launch(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                        "child 1 started"
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                        "child 1 finished"
                    )
                }
                val child2 = launch(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                        "child 2 started"
                    )
                    delay(5000)
                    // this will cancel child 2 while child 1 and 3 will successfully complete
                    throw IllegalStateException("child 2 crashed :(")
                    Log.d(
                        "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                        "child 2 finished"
                    )
                }
                val child3 = launch(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                        "child 3 started"
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch", "finished")
            }

        }

    }

    fun exceptionInSuperVisorScopeChildrenInAsync(){
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).async {
            supervisorScope {
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch", "started")
                delay(2000)
                val child1 = launch(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                        "child 1 started"
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                        "child 1 finished"
                    )
                }
                val child2 = launch(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                        "child 2 started"
                    )
                    delay(5000)
                    // this will cancel child 2 while child 1 and 3 will successfully complete
                    throw IllegalStateException("child 2 crashed :(")
                    Log.d(
                        "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                        "child 2 finished"
                    )
                }
                val child3 = launch(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                        "child 3 started"
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch", "finished")
            }

        }
    }


    // async exception handling is different in supervisor scope
    // if await() throws an exception, even supervisorScope will get cancelled
    fun exceptionInSuperVisorScopeAsyncChildrenInLaunch(){

        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).launch {
            supervisorScope {
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch", "started")
                delay(2000)
                val child1 = async(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                        "child 1 started"
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                        "child 1 finished"
                    )
                }
                val child2 = async(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                        "child 2 started"
                    )
                    delay(5000)
                    // this will cancel child 2 while child 1 and 3 will successfully complete
                    // exception will not be caught till await is called
                    throw IllegalStateException("child 2 crashed :(")
                    Log.d(
                        "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                        "child 2 finished"
                    )
                }
                val child3 = async(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                        "child 3 started"
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
                 // uncomment to catch exception
                 // this will cancel the parent as well as the exception has now propagated up
                 // child2.await()
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch", "finished")
            }

        }

    }

    // calling await on a throwing async block will cancel the parent and all children
    // the exception will only be caught when we call await on the parent after calling await on the throwing async block
    fun exceptionInSuperVisorScopeAsyncChildrenInAsync(){
        val res = CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).async {
            supervisorScope {
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch", "started")
                delay(2000)
                val child1 = async(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                        "child 1 started"
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                        "child 1 finished"
                    )
                }
                val child2 = async(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                        "child 2 started"
                    )
                    delay(5000)
                    // this will cancel child 2 while child 1 and 3 will successfully complete
                    // exception will not be thrown until await is called on child 2
                    // exception will not be caught till await is called on the parent
                    throw IllegalStateException("child 2 crashed :(")
                    Log.d(
                        "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                        "child 2 finished"
                    )
                }
                val child3 = async(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                        "child 3 started"
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
                // uncomment to throw exception
                // it will cancel child 3 as well as the parent
                // child2.await()
                Log.d("CoroutineUtils exceptionInCoroutineScopeAsChildInLaunch", "finished")
            }

        }

        // uncomment to catch the exception
        // will only be caught if await was called on child2
        /*CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("crash detector")).launch {
            // this will throw the exception that was contained in the async block
            res.await()
        }*/

    }




    fun exceptionInCoroutineScopeWithSupervisorJobAsContextAsParentInLaunch() {
    }

    fun exceptionInCoroutineScopeWithSupervisorJobAsContextAsParentInAsync() {
    }

    fun exceptionInCoroutineScopeWithSupervisorJobAsContextAsChildInLaunch() {
    }

    fun exceptionInCoroutineScopeWithSupervisorJobAsContextAsChildInAsync() {
    }

    // As we are launching separate supervisor scopes, they will not be affected by the parent coroutine crashing as they are not inheriting the parent coroutine context
    // but a supervisorJob
    fun exceptionInParentCoroutineScopeBlockLaunchingSeparateSupervisorScopes0() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).launch {
            Log.d(
                "CoroutineUtils case 1",
                "launch with separate customSupervisorScope.launch blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            delay(2000)
            val child1 = customSupervisorScope.launch(CoroutineName("child 1")) {
                Log.d(
                    "CoroutineUtils case 1",
                    "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                Log.d(
                    "CoroutineUtils case 1",
                    "child 1 finished"
                )
            }
            val child2 = customSupervisorScope.launch(CoroutineName("child 2")) {
                Log.d(
                    "CoroutineUtils case 1",
                    "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(5000)
                Log.d(
                    "CoroutineUtils case 1",
                    "child 2 finished"
                )
            }
            val child3 = customSupervisorScope.launch(CoroutineName("child 3")) {
                Log.d(
                    "CoroutineUtils case 1",
                    "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(6000)
                Log.d(
                    "CoroutineUtils case 1",
                    "child 3 finished"
                )
            }
            child1.invokeOnCompletion { e1 ->
                if (e1 == null) {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 1 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 1 cancelled ${e1.message}"
                    )
                }
            }
            child2.invokeOnCompletion { e2 ->
                if (e2 == null) {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 2 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 2 cancelled ${e2.message}"
                    )
                }
            }
            child3.invokeOnCompletion { e3 ->
                if (e3 == null) {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 3 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 3 cancelled ${e3.message}"
                    )
                }
            }

            delay(4000)
            // this will have no effect on children
            throw IllegalStateException("parent crashed :(")
            Log.d(
                "CoroutineUtils case 1",
                "launch with separate customSupervisorScope.launch blocks finished"
            )
        }
    }

    // As we launched a separate supervisor scope, its parent is no longer the parent coroutine but a supervisorJob
    fun exceptionInParentCoroutineScopeBlockLaunchingSeparateSupervisorScopes1() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).launch {
            Log.d(
                "CoroutineUtils case 2",
                "launch with customSupervisorScope.launch started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            customSupervisorScope.launch {
                Log.d(
                    "CoroutineUtils case 2",
                    "customSupervisorScope with launch blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                val child1 = launch(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 1 finished"
                    )
                }
                val child2 = launch(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(5000)
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 2 finished"
                    )
                }
                val child3 = launch(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 2",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 2",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 2",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 2",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 2",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 2",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
            }
            delay(5000)
            // this will have no effect on children as they have different parent
            throw IllegalStateException("parent crashed :(")
            Log.d(
                "CoroutineUtils case 2",
                "customSupervisorScope with launch blocks finished"
            )
        }
    }

    fun exceptionInParentCoroutineScopeBlockLaunchingSeparateSupervisorScopes2() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).launch {
            Log.d(
                "CoroutineUtils case 3",
                "launch with customSupervisorScope.launch launching separate customSupervisorScopes started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            customSupervisorScope.launch {
                Log.d(
                    "CoroutineUtils case 3",
                    "customSupervisorScope with customSupervisorScope.launch blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                val child1 = customSupervisorScope.launch(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 1 finished"
                    )
                }
                val child2 = customSupervisorScope.launch(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(5000)
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 2 finished"
                    )
                }
                val child3 = customSupervisorScope.launch(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
            }
            delay(5000)
            // this will have no effect on children as they have a different parent
            throw IllegalStateException("parent crashed :(")
            Log.d(
                "CoroutineUtils case 3",
                "customSupervisorScope with customSupervisorScope.launch blocks finished"
            )
        }
    }

    // supervisorScope suspends the block and waits for children to complete
    fun exceptionInParentCoroutineScopeBlockLaunchingSeparateSupervisorScopes3() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).launch {
            Log.d(
                "CoroutineUtils case 4",
                "launch with supervisorScope started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            supervisorScope {
                Log.d(
                    "CoroutineUtils case 4",
                    "supervisorScope with launch blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                val child1 = launch(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 1 finished"
                    )
                }
                val child2 = launch(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(5000)
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 2 finished"
                    )
                }
                val child3 = launch(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
            }
            delay(5000)
            // this will have no effect on children as the launch block was suspended till now
            throw IllegalStateException("parent crashed :(")
            Log.d(
                "CoroutineUtils case 4",
                "supervisorScope with launch blocks finished"
            )
        }
    }

    // this will run the code sequentially as supervisorScope is a suspending function
    fun exceptionInParentCoroutineScopeBlockLaunchingSeparateSupervisorScopes4() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).launch {
            Log.d(
                "CoroutineUtils case 5",
                "launch with supervisorScope.launch blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            delay(2000)
            // this suspends the launch block
            val child1 = supervisorScope {
                launch(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 finished"
                    )
                }
            }
            // this suspends the launch block
            val child2 = supervisorScope {
                launch(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(5000)
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 finished"
                    )
                }
            }
            // this suspends the launch block
            val child3 = supervisorScope {
                launch(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 finished"
                    )
                }
            }
            child1.invokeOnCompletion { e1 ->
                if (e1 == null) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 cancelled ${e1.message}"
                    )
                }
            }
            child2.invokeOnCompletion { e2 ->
                if (e2 == null) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 cancelled ${e2.message}"
                    )
                }
            }
            child3.invokeOnCompletion { e3 ->
                if (e3 == null) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 cancelled ${e3.message}"
                    )
                }
            }
            delay(4000)
            // this will have no effect on children as the block was suspended till now
            throw IllegalStateException("parent crashed :(")
            Log.d(
                "CoroutineUtils case 5",
                "launch with supervisorScope.launch blocks finished"
            )
        }
    }

    // this will run the each block sequentially as supervisorScope is a suspending function

    fun exceptionInParentCoroutineScopeBlockLaunchingSeparateSupervisorScopes5() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).launch {
            Log.d(
                "CoroutineUtils case 6",
                "launch with nested supervisorScope blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            supervisorScope {
                Log.d(
                    "CoroutineUtils case 6",
                    "supervisorScope with supervisorScope.launch blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                val child1 = supervisorScope {
                    launch(CoroutineName("child 1")) {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                        )
                        delay(2000)
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 1 finished"
                        )
                    }
                }
                val child2 = supervisorScope {
                    launch(CoroutineName("child 2")) {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                        )
                        delay(5000)
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 2 finished"
                        )
                    }
                }
                val child3 = supervisorScope {
                    launch(CoroutineName("child 3")) {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                        )
                        delay(6000)
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 3 finished"
                        )
                    }
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
            }
            delay(5000)
            // this will have no effect as the block is suspended till now
            throw IllegalStateException("parent crashed :(")
            Log.d(
                "CoroutineUtils case 6",
                "supervisorScope with supervisorScope.launch blocks finished"
            )
        }
    }




    // As we are launching separate supervisor scopes, they will not be affected by the parent coroutine crashing as they are not inheriting the parent coroutine context
    // but a supervisorJob
    fun exceptionInAsyncParentCoroutineScopeBlockLaunchingSeparateSupervisorScopes0() {
        val res = CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("async")).async {
            Log.d(
                "CoroutineUtils case 1",
                "async with separate customSupervisorScope.launch blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            delay(2000)
            val child1 = customSupervisorScope.launch(CoroutineName("child 1")) {
                Log.d(
                    "CoroutineUtils case 1",
                    "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                Log.d(
                    "CoroutineUtils case 1",
                    "child 1 finished"
                )
            }
            val child2 = customSupervisorScope.launch(CoroutineName("child 2")) {
                Log.d(
                    "CoroutineUtils case 1",
                    "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(5000)
                Log.d(
                    "CoroutineUtils case 1",
                    "child 2 finished"
                )
            }
            val child3 = customSupervisorScope.launch(CoroutineName("child 3")) {
                Log.d(
                    "CoroutineUtils case 1",
                    "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(6000)
                Log.d(
                    "CoroutineUtils case 1",
                    "child 3 finished"
                )
            }
            child1.invokeOnCompletion { e1 ->
                if (e1 == null) {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 1 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 1 cancelled ${e1.message}"
                    )
                }
            }
            child2.invokeOnCompletion { e2 ->
                if (e2 == null) {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 2 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 2 cancelled ${e2.message}"
                    )
                }
            }
            child3.invokeOnCompletion { e3 ->
                if (e3 == null) {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 3 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 3 cancelled ${e3.message}"
                    )
                }
            }

            delay(4000)
            // this will have no effect on children
            throw IllegalStateException("parent crashed :(")
            Log.d(
                "CoroutineUtils case 1",
                "launch with separate customSupervisorScope.launch blocks finished"
            )
        }
        // uncomment to catch exception
        /*CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("crash collector")).launch {
            res.await()
        }*/
    }

    // As we launched a separate supervisor scope, its parent is no longer the parent coroutine but a supervisorJob
    fun exceptionInAsyncParentCoroutineScopeBlockLaunchingSeparateSupervisorScopes1() {
        val res = CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).async {
            Log.d(
                "CoroutineUtils case 2",
                "async with customSupervisorScope.launch started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            customSupervisorScope.launch {
                Log.d(
                    "CoroutineUtils case 2",
                    "customSupervisorScope with launch blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                val child1 = launch(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 1 finished"
                    )
                }
                val child2 = launch(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(5000)
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 2 finished"
                    )
                }
                val child3 = launch(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 2",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 2",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 2",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 2",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 2",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 2",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
            }
            delay(5000)
            // this will have no effect on children as they have different parent
            throw IllegalStateException("parent crashed :(")
            Log.d(
                "CoroutineUtils case 2",
                "customSupervisorScope with launch blocks finished"
            )
        }
        // uncomment to catch exception
        /*CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("crash collector")).launch {
            res.await()
        }*/
    }

    fun exceptionInAsyncParentCoroutineScopeBlockLaunchingSeparateSupervisorScopes2() {
        val res = CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("async")).async {
            Log.d(
                "CoroutineUtils case 3",
                "async with customSupervisorScope.launch launching separate customSupervisorScopes started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            customSupervisorScope.launch {
                Log.d(
                    "CoroutineUtils case 3",
                    "customSupervisorScope with customSupervisorScope.launch blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                val child1 = customSupervisorScope.launch(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 1 finished"
                    )
                }
                val child2 = customSupervisorScope.launch(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(5000)
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 2 finished"
                    )
                }
                val child3 = customSupervisorScope.launch(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
            }
            delay(5000)
            // this will have no effect on children as they have a different parent
            throw IllegalStateException("parent crashed :(")
            Log.d(
                "CoroutineUtils case 3",
                "customSupervisorScope with customSupervisorScope.launch blocks finished"
            )
        }
        // uncomment to catch exception
        /*CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("crash collector")).launch {
            res.await()
        }*/
    }

    // supervisorScope suspends the block and waits for children to complete
    fun exceptionInAsyncParentCoroutineScopeBlockLaunchingSeparateSupervisorScopes3() {
        val res = CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("async")).async {
            Log.d(
                "CoroutineUtils case 4",
                "async with supervisorScope started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            supervisorScope {
                Log.d(
                    "CoroutineUtils case 4",
                    "supervisorScope with launch blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                val child1 = launch(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 1 finished"
                    )
                }
                val child2 = launch(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(5000)
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 2 finished"
                    )
                }
                val child3 = launch(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
            }
            delay(5000)
            // this will have no effect on children as the launch block was suspended till now
            throw IllegalStateException("parent crashed :(")
            Log.d(
                "CoroutineUtils case 4",
                "supervisorScope with launch blocks finished"
            )
        }
        // uncomment to catch exception
        /*CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("crash collector")).launch {
            res.await()
        }*/
    }

    // this will run the code sequentially as supervisorScope is a suspending function
    fun exceptionInAsyncParentCoroutineScopeBlockLaunchingSeparateSupervisorScopes4() {
        val res = CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("async")).async {
            Log.d(
                "CoroutineUtils case 5",
                "async with supervisorScope.launch blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            delay(2000)
            // this suspends the launch block
            val child1 = supervisorScope {
                launch(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 finished"
                    )
                }
            }
            // this suspends the launch block
            val child2 = supervisorScope {
                launch(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(5000)
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 finished"
                    )
                }
            }
            // this suspends the launch block
            val child3 = supervisorScope {
                launch(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 finished"
                    )
                }
            }
            child1.invokeOnCompletion { e1 ->
                if (e1 == null) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 cancelled ${e1.message}"
                    )
                }
            }
            child2.invokeOnCompletion { e2 ->
                if (e2 == null) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 cancelled ${e2.message}"
                    )
                }
            }
            child3.invokeOnCompletion { e3 ->
                if (e3 == null) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 cancelled ${e3.message}"
                    )
                }
            }
            delay(4000)
            // this will have no effect on children as the block was suspended till now
            throw IllegalStateException("parent crashed :(")
            Log.d(
                "CoroutineUtils case 5",
                "launch with supervisorScope.launch blocks finished"
            )
        }
        // uncomment to catch exception
        /*CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("crash collector")).launch {
            res.await()
        }*/
    }

    // this will run the each block sequentially as supervisorScope is a suspending function
    fun exceptionInAsyncParentCoroutineScopeBlockLaunchingSeparateSupervisorScopes5() {
        val res = CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("async")).async {
            Log.d(
                "CoroutineUtils case 6",
                "async with nested supervisorScope blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            supervisorScope {
                Log.d(
                    "CoroutineUtils case 6",
                    "supervisorScope with supervisorScope.launch blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                val child1 = supervisorScope {
                    launch(CoroutineName("child 1")) {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                        )
                        delay(2000)
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 1 finished"
                        )
                    }
                }
                val child2 = supervisorScope {
                    launch(CoroutineName("child 2")) {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                        )
                        delay(5000)
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 2 finished"
                        )
                    }
                }
                val child3 = supervisorScope {
                    launch(CoroutineName("child 3")) {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                        )
                        delay(6000)
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 3 finished"
                        )
                    }
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
            }
            delay(5000)
            // this will have no effect as the block is suspended till now
            throw IllegalStateException("parent crashed :(")
            Log.d(
                "CoroutineUtils case 6",
                "supervisorScope with supervisorScope.launch blocks finished"
            )
        }
        // uncomment to catch exception
        /*CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("crash collector")).launch {
            res.await()
        }*/
    }



    // As we are launching separate supervisor scopes, they will not be affected by the parent coroutine crashing as they are not inheriting the parent coroutine context
    // but a supervisorJob
    fun exceptionInChildSupervisorScopeLaunchedInsideALaunchedCoroutineScope0() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).launch {
            Log.d(
                "CoroutineUtils case 1",
                "launch with separate customSupervisorScope.launch blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            delay(2000)
            val child1 = customSupervisorScope.launch(CoroutineName("child 1")) {
                Log.d(
                    "CoroutineUtils case 1",
                    "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                Log.d(
                    "CoroutineUtils case 1",
                    "child 1 finished"
                )
            }
            val child2 = customSupervisorScope.launch(CoroutineName("child 2")) {
                Log.d(
                    "CoroutineUtils case 1",
                    "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                // will only cancel child 2 as the children now inherit a supervisorJob
                delay(5000)
                throw IllegalStateException("child2 crashed :(")
                Log.d(
                    "CoroutineUtils case 1",
                    "child 2 finished"
                )
            }
            val child3 = customSupervisorScope.launch(CoroutineName("child 3")) {
                Log.d(
                    "CoroutineUtils case 1",
                    "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(6000)
                Log.d(
                    "CoroutineUtils case 1",
                    "child 3 finished"
                )
            }
            child1.invokeOnCompletion { e1 ->
                if (e1 == null) {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 1 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 1 cancelled ${e1.message}"
                    )
                }
            }
            child2.invokeOnCompletion { e2 ->
                if (e2 == null) {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 2 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 2 cancelled ${e2.message}"
                    )
                }
            }
            child3.invokeOnCompletion { e3 ->
                if (e3 == null) {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 3 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 3 cancelled ${e3.message}"
                    )
                }
            }
            delay(9000)
            Log.d(
                "CoroutineUtils case 1",
                "launch with separate customSupervisorScope.launch blocks finished"
            )
        }.invokeOnCompletion {
            if(it!=null){
                Log.d(
                    "CoroutineUtils case 1",
                    "launch cancelled ${it.message}"
                )
            }
        }
    }

    // As we launched a separate supervisor scope, its parent is no longer the parent coroutine but a supervisorJob
    // but its children are still inheriting standalone job
    fun exceptionInChildSupervisorScopeLaunchedInsideALaunchedCoroutineScope1() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).launch {
            Log.d(
                "CoroutineUtils case 2",
                "launch with customSupervisorScope.launch started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            // this block will have supervisorJob
            customSupervisorScope.launch {
                Log.d(
                    "CoroutineUtils case 2",
                    "customSupervisorScope with launch blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                // these will still be standalone job
                val child1 = launch(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 1 finished"
                    )
                }
                val child2 = launch(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(5000)
                    // this will cancel child 2 and 3 as they are still inheriting the parent coroutine context
                    throw IllegalStateException("child2 crashed :(")
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 2 finished"
                    )
                }
                val child3 = launch(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 2",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 2",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 2",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 2",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 2",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 2",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
            }
            delay(9000)
            Log.d(
                "CoroutineUtils case 2",
                "customSupervisorScope with launch blocks finished"
            )
        }.invokeOnCompletion {
            if(it!=null){
                Log.d(
                    "CoroutineUtils case 2",
                    "launch cancelled ${it.message}"
                )
            }
        }
    }

    fun exceptionInChildSupervisorScopeLaunchedInsideALaunchedCoroutineScope2() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).launch {
            Log.d(
                "CoroutineUtils case 3",
                "launch with customSupervisorScope.launch launching separate customSupervisorScopes started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            customSupervisorScope.launch {
                Log.d(
                    "CoroutineUtils case 3",
                    "customSupervisorScope with customSupervisorScope.launch blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                val child1 = customSupervisorScope.launch(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 1 finished"
                    )
                }
                val child2 = customSupervisorScope.launch(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(5000)
                    // this will only cancel child 2 as the children themselves run in a supervisorJob
                    // but the exception will crash the launch block as customSupervisorScope does not stop exception propagation
                    throw IllegalStateException("child2 crashed :(")
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 2 finished"
                    )
                }
                val child3 = customSupervisorScope.launch(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
            }
            delay(10000)
            Log.d(
                "CoroutineUtils case 3",
                "customSupervisorScope with customSupervisorScope.launch blocks finished"
            )
        }.invokeOnCompletion {
            if(it!=null){
                Log.d(
                    "CoroutineUtils case 3",
                    "launch cancelled ${it.message}"
                )
            }
        }
    }

    /** Important considerations
     *
     * First Function: exceptionInChildSupervisorScopeLaunchedInsideALaunchedCoroutineScope0()
     * In this function:
     *
     * Parent Coroutine: A coroutine is launched with a custom name "launch" and an exception handler.
     *
     * Child Coroutines: Three child coroutines (child1, child2, child3) are launched using customSupervisorScope.
     *
     * Exception Handling:
     *
     * child2 throws an IllegalStateException.
     *
     * child1 and child3 continue running because they are in a supervisor scope.
     *
     * Final Log: The final log statement is printed because the parent coroutine and supervisor scope handle the exception without terminating.
     *
     * Second Function: exceptionInChildSupervisorScopeLaunchedInsideALaunchedCoroutineScope1()
     * In this function:
     *
     * Parent Coroutine: A coroutine is launched with a custom name "launch" and an exception handler.
     *
     * Custom Supervisor Scope: Inside the parent coroutine, a customSupervisorScope is launched.
     *
     * Child Coroutines: Three child coroutines (child1, child2, child3) are launched within the customSupervisorScope.
     *
     * Exception Handling:
     *
     * child2 throws an IllegalStateException, which cancels both child2 and child3 because they inherit the parent context.
     *
     * child1 continues running.
     *
     * Final Log: The final log statement is printed because the parent coroutine and supervisor scope handle the exception without terminating.
     *
     * Third Function: exceptionInChildSupervisorScopeLaunchedInsideALaunchedCoroutineScope2()
     * In this function:
     *
     * Parent Coroutine: A coroutine is launched with a custom name "launch" and an exception handler.
     *
     * Nested Supervisor Scopes: Inside the parent coroutine, a customSupervisorScope is launched, and within it, each child coroutine (child1, child2, child3) is launched using a separate customSupervisorScope.
     *
     * Exception Handling:
     *
     * child2 throws an IllegalStateException.
     *
     * This exception is not handled within the inner customSupervisorScope, causing the entire block to terminate.
     *
     * Final Log: The final log statement is not printed because the unhandled exception in child2 causes the termination of the entire customSupervisorScope block, skipping the remaining code.
     *
     * Conclusion
     * The key difference in the third function is the use of nested SupervisorScope. When an exception occurs in child2, it propagates and crashes the entire customSupervisorScope block due to unhandled exception propagation. This is why the final log statement is not printed.
     */

    // supervisorScope suspends the block and waits for children to complete
    fun exceptionInChildSupervisorScopeLaunchedInsideALaunchedCoroutineScope3() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).launch {
            Log.d(
                "CoroutineUtils case 4",
                "launch with supervisorScope started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            supervisorScope {
                Log.d(
                    "CoroutineUtils case 4",
                    "supervisorScope with launch blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                val child1 = launch(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 1 finished"
                    )
                }
                val child2 = launch(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(5000)
                    // this will only cancel child 2 as the children themselves run in a supervisorJob
                    throw IllegalStateException("child2 crashed :(")
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 2 finished"
                    )
                }
                val child3 = launch(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
            }
            Log.d(
                "CoroutineUtils case 4",
                "supervisorScope with launch blocks finished"
            )
        }.invokeOnCompletion {
            if(it!=null){
                Log.d(
                    "CoroutineUtils case 4",
                    "launch cancelled ${it.message}"
                )
            }
        }
    }

    // this will run the code sequentially as supervisorScope is a suspending function
    fun exceptionInChildSupervisorScopeLaunchedInsideALaunchedCoroutineScope4() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).launch {
            Log.d(
                "CoroutineUtils case 5",
                "launch with supervisorScope.launch blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            delay(2000)
            // this suspends the launch block
            val child1 = supervisorScope {
                launch(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 finished"
                    )
                }
            }
            // this suspends the launch block
            val child2 = supervisorScope {
                launch(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    // this will only cancel child 2 as the children inherit a supervisorJob
                    delay(5000)
                    throw IllegalStateException("child2 crashed :(")
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 finished"
                    )
                }
            }
            // this suspends the launch block
            val child3 = supervisorScope {
                launch(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 finished"
                    )
                }
            }
            child1.invokeOnCompletion { e1 ->
                if (e1 == null) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 cancelled ${e1.message}"
                    )
                }
            }
            child2.invokeOnCompletion { e2 ->
                if (e2 == null) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 cancelled ${e2.message}"
                    )
                }
            }
            child3.invokeOnCompletion { e3 ->
                if (e3 == null) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 cancelled ${e3.message}"
                    )
                }
            }
            Log.d(
                "CoroutineUtils case 5",
                "launch with supervisorScope.launch blocks finished"
            )
        }.invokeOnCompletion {
            if(it!=null){
                Log.d(
                    "CoroutineUtils case 5",
                    "launch cancelled ${it.message}"
                )
            }
        }
    }

    // this will run the each block sequentially as supervisorScope is a suspending function

    fun exceptionInChildSupervisorScopeLaunchedInsideALaunchedCoroutineScope5() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).launch {
            Log.d(
                "CoroutineUtils case 6",
                "launch with nested supervisorScope blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            supervisorScope {
                Log.d(
                    "CoroutineUtils case 6",
                    "supervisorScope with supervisorScope.launch blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                val child1 = supervisorScope {
                    launch(CoroutineName("child 1")) {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                        )
                        delay(2000)
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 1 finished"
                        )
                    }
                }
                val child2 = supervisorScope {
                    launch(CoroutineName("child 2")) {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                        )
                        delay(5000)
                        // this will only cancel child 2 as the children themselves run in a supervisorJob
                        throw IllegalStateException("child2 crashed :(")
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 2 finished"
                        )
                    }
                }
                val child3 = supervisorScope {
                    launch(CoroutineName("child 3")) {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                        )
                        delay(6000)
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 3 finished"
                        )
                    }
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
            }
            Log.d(
                "CoroutineUtils case 6",
                "supervisorScope with supervisorScope.launch blocks finished"
            )
        }.invokeOnCompletion {
            if(it!=null){
                Log.d(
                    "CoroutineUtils case 6",
                    "launch cancelled ${it.message}"
                )
            }
        }
    }


    // As we are launching separate supervisor scopes, they will not be affected by the parent coroutine crashing as they are not inheriting the parent coroutine context
    // but a supervisorJob
    fun exceptionInAsyncChildSupervisorScopeLaunchedInsideALaunchedCoroutineScope0() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).launch {
            Log.d(
                "CoroutineUtils case 1",
                "launch with separate customSupervisorScope.launch blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            delay(2000)
            val child1 = customSupervisorScope.async(CoroutineName("child 1")) {
                Log.d(
                    "CoroutineUtils case 1",
                    "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                Log.d(
                    "CoroutineUtils case 1",
                    "child 1 finished"
                )
            }
            val child2 = customSupervisorScope.async(CoroutineName("child 2")) {
                Log.d(
                    "CoroutineUtils case 1",
                    "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                // will only cancel child 2 as the children now inherit a supervisorJob
                delay(5000)
                throw IllegalStateException("child2 crashed :(")
                Log.d(
                    "CoroutineUtils case 1",
                    "child 2 finished"
                )
            }
            val child3 = customSupervisorScope.async(CoroutineName("child 3")) {
                Log.d(
                    "CoroutineUtils case 1",
                    "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(6000)
                Log.d(
                    "CoroutineUtils case 1",
                    "child 3 finished"
                )
            }
            child1.invokeOnCompletion { e1 ->
                if (e1 == null) {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 1 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 1 cancelled ${e1.message}"
                    )
                }
            }
            child2.invokeOnCompletion { e2 ->
                if (e2 == null) {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 2 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 2 cancelled ${e2.message}"
                    )
                }
            }
            child3.invokeOnCompletion { e3 ->
                if (e3 == null) {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 3 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 3 cancelled ${e3.message}"
                    )
                }
            }
            // uncomment to catch exception, parent will get cancelled but child 3 will complete
            // child2.await()
            delay(9000)
            Log.d(
                "CoroutineUtils case 1",
                "launch with separate customSupervisorScope.launch blocks finished"
            )
        }.invokeOnCompletion {
            if(it!=null){
                Log.d(
                    "CoroutineUtils case 1",
                    "launch cancelled ${it.message}"
                )
            }
        }
    }

    // As we launched a separate supervisor scope, its parent is no longer the parent coroutine but a supervisorJob
    // but its children are still inheriting standalone job
    fun exceptionInAsyncChildSupervisorScopeLaunchedInsideALaunchedCoroutineScope1() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).launch {
            Log.d(
                "CoroutineUtils case 2",
                "launch with customSupervisorScope.launch started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            // this block will have supervisorJob
            val job = customSupervisorScope.async {
                Log.d(
                    "CoroutineUtils case 2",
                    "customSupervisorScope with launch blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                // these will still be standalone job
                val child1 = async(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 1 finished"
                    )
                }
                val child2 = async(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(5000)
                    // this will cancel child 2 and 3 as they are still inheriting the parent coroutine context
                    throw IllegalStateException("child2 crashed :(")
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 2 finished"
                    )
                }
                val child3 = async(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 2",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 2",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 2",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 2",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 2",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 2",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
                 // this will have no affect as the children as cancelled
                 // child2.await()
            }
            delay(9000)
            Log.d(
                "CoroutineUtils case 2",
                "customSupervisorScope with launch blocks finished"
            )
        }.invokeOnCompletion {
            if(it!=null){
                Log.d(
                    "CoroutineUtils case 2",
                    "launch cancelled ${it.message}"
                )
            }
        }
    }

    fun exceptionInAsyncChildSupervisorScopeLaunchedInsideALaunchedCoroutineScope2() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).launch {
            Log.d(
                "CoroutineUtils case 3",
                "launch with customSupervisorScope.launch launching separate customSupervisorScopes started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            val job = customSupervisorScope.async {
                Log.d(
                    "CoroutineUtils case 3",
                    "customSupervisorScope with customSupervisorScope.launch blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                val child1 = customSupervisorScope.async(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 1 finished"
                    )
                }
                val child2 = customSupervisorScope.async(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(5000)
                    // this will only cancel child 2 as the children themselves run in a supervisorJob
                    // but the exception will crash the launch block as customSupervisorScope does not stop exception propagation
                    throw IllegalStateException("child2 crashed :(")
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 2 finished"
                    )
                }
                val child3 = customSupervisorScope.async(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
                // uncomment to catch exception
                // child2.await()
            }
            // uncomment to catch exception, child 3 will also get cancelled as parent will get cancelled
            // child2.await()
            delay(10000)
            Log.d(
                "CoroutineUtils case 3",
                "customSupervisorScope with customSupervisorScope.launch blocks finished"
            )
        }.invokeOnCompletion {
            if(it!=null){
                Log.d(
                    "CoroutineUtils case 3",
                    "launch cancelled ${it.message}"
                )
            }
        }
    }

    // supervisorScope suspends the block and waits for children to complete
    fun exceptionInAsyncChildSupervisorScopeLaunchedInsideALaunchedCoroutineScope3() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).launch {
            Log.d(
                "CoroutineUtils case 4",
                "launch with supervisorScope started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            supervisorScope {
                Log.d(
                    "CoroutineUtils case 4",
                    "supervisorScope with launch blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                val child1 = async(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 1 finished"
                    )
                }
                val child2 = async(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(5000)
                    // this will only cancel child 2 as the children themselves run in a supervisorJob
                    throw IllegalStateException("child2 crashed :(")
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 2 finished"
                    )
                }
                val child3 = async(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
                // uncomment to catch exception, child 3 will also get cancelled as parent will get cancelled
                // child2.await()
            }

            Log.d(
                "CoroutineUtils case 4",
                "supervisorScope with launch blocks finished"
            )
        }.invokeOnCompletion {
            if(it!=null){
                Log.d(
                    "CoroutineUtils case 4",
                    "launch cancelled ${it.message}"
                )
            }
        }
    }

    // this will run the code sequentially as supervisorScope is a suspending function
    fun exceptionInAsyncChildSupervisorScopeLaunchedInsideALaunchedCoroutineScope4() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).launch {
            Log.d(
                "CoroutineUtils case 5",
                "launch with supervisorScope.launch blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            delay(2000)
            // this suspends the launch block
            val child1 = supervisorScope {
                async(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 finished"
                    )
                }
            }
            // this suspends the launch block
            val child2 = supervisorScope {
                async(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    // this will only cancel child 2 as the children inherit a supervisorJob
                    delay(5000)
                    throw IllegalStateException("child2 crashed :(")
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 finished"
                    )
                }
            }
            // this suspends the launch block
            val child3 = supervisorScope {
                async(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 finished"
                    )
                }
            }
            child1.invokeOnCompletion { e1 ->
                if (e1 == null) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 cancelled ${e1.message}"
                    )
                }
            }
            child2.invokeOnCompletion { e2 ->
                if (e2 == null) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 cancelled ${e2.message}"
                    )
                }
            }
            child3.invokeOnCompletion { e3 ->
                if (e3 == null) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 cancelled ${e3.message}"
                    )
                }
            }
            // uncomment to catch exception, parent will get cancelled
            // child2.await()
            Log.d(
                "CoroutineUtils case 5",
                "launch with supervisorScope.launch blocks finished"
            )
        }.invokeOnCompletion {
            if(it!=null){
                Log.d(
                    "CoroutineUtils case 5",
                    "launch cancelled ${it.message}"
                )
            }
        }
    }

    // this will run the each block sequentially as supervisorScope is a suspending function

    fun exceptionInAsyncChildSupervisorScopeLaunchedInsideALaunchedCoroutineScope5() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).launch {
            Log.d(
                "CoroutineUtils case 6",
                "launch with nested supervisorScope blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            supervisorScope {
                Log.d(
                    "CoroutineUtils case 6",
                    "supervisorScope with supervisorScope.launch blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                val child1 = supervisorScope {
                    async(CoroutineName("child 1")) {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                        )
                        delay(2000)
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 1 finished"
                        )
                    }
                }
                val child2 = supervisorScope {
                    async(CoroutineName("child 2")) {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                        )
                        delay(5000)
                        // this will only cancel child 2 as the children themselves run in a supervisorJob
                        throw IllegalStateException("child2 crashed :(")
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 2 finished"
                        )
                    }
                }
                val child3 = supervisorScope {
                    async(CoroutineName("child 3")) {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                        )
                        delay(6000)
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 3 finished"
                        )
                    }
                }


                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
                // uncomment to catch exception, parent will get cancelled
                // child2.await()
            }

            Log.d(
                "CoroutineUtils case 6",
                "supervisorScope with supervisorScope.launch blocks finished"
            )
        }.invokeOnCompletion {
            if(it!=null){
                Log.d(
                    "CoroutineUtils case 6",
                    "launch cancelled ${it.message}"
                )
            }
        }
    }



    // As we are launching separate supervisor scopes, they will not be affected by the parent coroutine crashing as they are not inheriting the parent coroutine context
    // but a supervisorJob
    fun exceptionInChildSupervisorScopeLaunchedInsideAsyncCoroutineScope0() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("async")).async {
            Log.d(
                "CoroutineUtils case 1",
                "async with separate customSupervisorScope.async blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            delay(2000)
            val child1 = customSupervisorScope.launch(CoroutineName("child 1")) {
                Log.d(
                    "CoroutineUtils case 1",
                    "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                Log.d(
                    "CoroutineUtils case 1",
                    "child 1 finished"
                )
            }
            val child2 = customSupervisorScope.launch(CoroutineName("child 2")) {
                Log.d(
                    "CoroutineUtils case 1",
                    "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                // will only cancel child 2 as the children now inherit a supervisorJob
                delay(5000)
                throw IllegalStateException("child2 crashed :(")
                Log.d(
                    "CoroutineUtils case 1",
                    "child 2 finished"
                )
            }
            val child3 = customSupervisorScope.launch(CoroutineName("child 3")) {
                Log.d(
                    "CoroutineUtils case 1",
                    "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(6000)
                Log.d(
                    "CoroutineUtils case 1",
                    "child 3 finished"
                )
            }
            child1.invokeOnCompletion { e1 ->
                if (e1 == null) {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 1 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 1 cancelled ${e1.message}"
                    )
                }
            }
            child2.invokeOnCompletion { e2 ->
                if (e2 == null) {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 2 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 2 cancelled ${e2.message}"
                    )
                }
            }
            child3.invokeOnCompletion { e3 ->
                if (e3 == null) {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 3 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 3 cancelled ${e3.message}"
                    )
                }
            }
            delay(9000)
            Log.d(
                "CoroutineUtils case 1",
                "async with separate customSupervisorScope.async blocks finished"
            )
        }.invokeOnCompletion {
            if(it!=null){
                Log.d(
                    "CoroutineUtils case 1",
                    "async cancelled ${it.message}"
                )
            }
        }
    }

    // As we launched a separate supervisor scope, its parent is no longer the parent coroutine but a supervisorJob
    // but its children are still inheriting standalone job
    fun exceptionInChildSupervisorScopeLaunchedInsideAsyncCoroutineScope1() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("async")).async {
            Log.d(
                "CoroutineUtils case 2",
                "async with customSupervisorScope.async started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            // this block will have supervisorJob
            customSupervisorScope.launch {
                Log.d(
                    "CoroutineUtils case 2",
                    "customSupervisorScope with async blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                // these will still be standalone job
                val child1 = launch(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 1 finished"
                    )
                }
                val child2 = launch(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(5000)
                    // this will cancel child 2 and 3 as they are still inheriting the parent coroutine context
                    throw IllegalStateException("child2 crashed :(")
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 2 finished"
                    )
                }
                val child3 = launch(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 2",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 2",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 2",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 2",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 2",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 2",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
            }
            delay(9000)
            Log.d(
                "CoroutineUtils case 2",
                "customSupervisorScope with async blocks finished"
            )
        }.invokeOnCompletion {
            if(it!=null){
                Log.d(
                    "CoroutineUtils case 2",
                    "async cancelled ${it.message}"
                )
            }
        }
    }

    fun exceptionInChildSupervisorScopeLaunchedInsideAsyncCoroutineScope2() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("async")).async {
            Log.d(
                "CoroutineUtils case 3",
                "async with customSupervisorScope.async launching separate customSupervisorScopes started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            customSupervisorScope.launch {
                Log.d(
                    "CoroutineUtils case 3",
                    "customSupervisorScope with customSupervisorScope.async blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                val child1 = customSupervisorScope.launch(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 1 finished"
                    )
                }
                val child2 = customSupervisorScope.launch(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(5000)
                    // this will only cancel child 2 as the children themselves run in a supervisorJob
                    // but the exception will crash the launch block as customSupervisorScope does not stop exception propagation
                    throw IllegalStateException("child2 crashed :(")
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 2 finished"
                    )
                }
                val child3 = customSupervisorScope.launch(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
            }
            delay(10000)
            Log.d(
                "CoroutineUtils case 3",
                "customSupervisorScope with customSupervisorScope.async blocks finished"
            )
        }.invokeOnCompletion {
            if(it!=null){
                Log.d(
                    "CoroutineUtils case 3",
                    "async cancelled ${it.message}"
                )
            }
        }
    }

    /** Important considerations
     *
     * First Function: exceptionInChildSupervisorScopeLaunchedInsideALaunchedCoroutineScope0()
     * In this function:
     *
     * Parent Coroutine: A coroutine is launched with a custom name "launch" and an exception handler.
     *
     * Child Coroutines: Three child coroutines (child1, child2, child3) are launched using customSupervisorScope.
     *
     * Exception Handling:
     *
     * child2 throws an IllegalStateException.
     *
     * child1 and child3 continue running because they are in a supervisor scope.
     *
     * Final Log: The final log statement is printed because the parent coroutine and supervisor scope handle the exception without terminating.
     *
     * Second Function: exceptionInChildSupervisorScopeLaunchedInsideALaunchedCoroutineScope1()
     * In this function:
     *
     * Parent Coroutine: A coroutine is launched with a custom name "launch" and an exception handler.
     *
     * Custom Supervisor Scope: Inside the parent coroutine, a customSupervisorScope is launched.
     *
     * Child Coroutines: Three child coroutines (child1, child2, child3) are launched within the customSupervisorScope.
     *
     * Exception Handling:
     *
     * child2 throws an IllegalStateException, which cancels both child2 and child3 because they inherit the parent context.
     *
     * child1 continues running.
     *
     * Final Log: The final log statement is printed because the parent coroutine and supervisor scope handle the exception without terminating.
     *
     * Third Function: exceptionInChildSupervisorScopeLaunchedInsideALaunchedCoroutineScope2()
     * In this function:
     *
     * Parent Coroutine: A coroutine is launched with a custom name "launch" and an exception handler.
     *
     * Nested Supervisor Scopes: Inside the parent coroutine, a customSupervisorScope is launched, and within it, each child coroutine (child1, child2, child3) is launched using a separate customSupervisorScope.
     *
     * Exception Handling:
     *
     * child2 throws an IllegalStateException.
     *
     * This exception is not handled within the inner customSupervisorScope, causing the entire block to terminate.
     *
     * Final Log: The final log statement is not printed because the unhandled exception in child2 causes the termination of the entire customSupervisorScope block, skipping the remaining code.
     *
     * Conclusion
     * The key difference in the third function is the use of nested SupervisorScope. When an exception occurs in child2, it propagates and crashes the entire customSupervisorScope block due to unhandled exception propagation. This is why the final log statement is not printed.
     */

    // supervisorScope suspends the block and waits for children to complete
    fun exceptionInChildSupervisorScopeLaunchedInsideAsyncCoroutineScope3() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).async {
            Log.d(
                "CoroutineUtils case 4",
                "async with supervisorScope started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            supervisorScope {
                Log.d(
                    "CoroutineUtils case 4",
                    "supervisorScope with async blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                val child1 = launch(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 1 finished"
                    )
                }
                val child2 = launch(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(5000)
                    // this will only cancel child 2 as the children themselves run in a supervisorJob
                    throw IllegalStateException("child2 crashed :(")
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 2 finished"
                    )
                }
                val child3 = launch(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
            }
            Log.d(
                "CoroutineUtils case 4",
                "supervisorScope with async blocks finished"
            )
        }.invokeOnCompletion {
            if(it!=null){
                Log.d(
                    "CoroutineUtils case 4",
                    "async cancelled ${it.message}"
                )
            }
        }
    }

    // this will run the code sequentially as supervisorScope is a suspending function
    fun exceptionInChildSupervisorScopeLaunchedInsideAsyncCoroutineScope4() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("async")).async {
            Log.d(
                "CoroutineUtils case 5",
                "async with supervisorScope.async blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            delay(2000)
            // this suspends the launch block
            val child1 = supervisorScope {
                launch(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 finished"
                    )
                }
            }
            // this suspends the launch block
            val child2 = supervisorScope {
                launch(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    // this will only cancel child 2 as the children inherit a supervisorJob
                    delay(5000)
                    throw IllegalStateException("child2 crashed :(")
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 finished"
                    )
                }
            }
            // this suspends the launch block
            val child3 = supervisorScope {
                launch(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 finished"
                    )
                }
            }
            child1.invokeOnCompletion { e1 ->
                if (e1 == null) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 cancelled ${e1.message}"
                    )
                }
            }
            child2.invokeOnCompletion { e2 ->
                if (e2 == null) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 cancelled ${e2.message}"
                    )
                }
            }
            child3.invokeOnCompletion { e3 ->
                if (e3 == null) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 cancelled ${e3.message}"
                    )
                }
            }
            Log.d(
                "CoroutineUtils case 5",
                "v with supervisorScope.launch blocks finished"
            )
        }.invokeOnCompletion {
            if(it!=null){
                Log.d(
                    "CoroutineUtils case 5",
                    "async cancelled ${it.message}"
                )
            }
        }
    }

    // this will run the each block sequentially as supervisorScope is a suspending function

    fun exceptionInChildSupervisorScopeLaunchedInsideAsyncCoroutineScope5() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("async")).async {
            Log.d(
                "CoroutineUtils case 6",
                "async with nested supervisorScope blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            supervisorScope {
                Log.d(
                    "CoroutineUtils case 6",
                    "supervisorScope with supervisorScope.async blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                val child1 = supervisorScope {
                    launch(CoroutineName("child 1")) {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                        )
                        delay(2000)
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 1 finished"
                        )
                    }
                }
                val child2 = supervisorScope {
                    launch(CoroutineName("child 2")) {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                        )
                        delay(5000)
                        // this will only cancel child 2 as the children themselves run in a supervisorJob
                        throw IllegalStateException("child2 crashed :(")
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 2 finished"
                        )
                    }
                }
                val child3 = supervisorScope {
                    launch(CoroutineName("child 3")) {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                        )
                        delay(6000)
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 3 finished"
                        )
                    }
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
            }
            Log.d(
                "CoroutineUtils case 6",
                "supervisorScope with supervisorScope.async blocks finished"
            )
        }.invokeOnCompletion {
            if(it!=null){
                Log.d(
                    "CoroutineUtils case 6",
                    "async cancelled ${it.message}"
                )
            }
        }
    }



    // As we are launching separate supervisor scopes, they will not be affected by the parent coroutine crashing as they are not inheriting the parent coroutine context
    // but a supervisorJob
    fun exceptionInAsyncChildSupervisorScopeLaunchedInsideAsyncCoroutineScope0() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("async")).async {
            Log.d(
                "CoroutineUtils case 1",
                "async with separate customSupervisorScope.async blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            delay(2000)
            val child1 = customSupervisorScope.async(CoroutineName("child 1")) {
                Log.d(
                    "CoroutineUtils case 1",
                    "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                Log.d(
                    "CoroutineUtils case 1",
                    "child 1 finished"
                )
            }
            val child2 = customSupervisorScope.async(CoroutineName("child 2")) {
                Log.d(
                    "CoroutineUtils case 1",
                    "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                // will only cancel child 2 as the children now inherit a supervisorJob
                delay(5000)
                throw IllegalStateException("child2 crashed :(")
                Log.d(
                    "CoroutineUtils case 1",
                    "child 2 finished"
                )
            }
            val child3 = customSupervisorScope.async(CoroutineName("child 3")) {
                Log.d(
                    "CoroutineUtils case 1",
                    "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(6000)
                Log.d(
                    "CoroutineUtils case 1",
                    "child 3 finished"
                )
            }
            child1.invokeOnCompletion { e1 ->
                if (e1 == null) {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 1 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 1 cancelled ${e1.message}"
                    )
                }
            }
            child2.invokeOnCompletion { e2 ->
                if (e2 == null) {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 2 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 2 cancelled ${e2.message}"
                    )
                }
            }
            child3.invokeOnCompletion { e3 ->
                if (e3 == null) {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 3 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 3 cancelled ${e3.message}"
                    )
                }
            }
            // uncomment to catch exception, parent will get cancelled but child 3 will complete
            // child2.await()
            delay(9000)
            Log.d(
                "CoroutineUtils case 1",
                "async with separate customSupervisorScope.launch blocks finished"
            )
        }.invokeOnCompletion {
            if(it!=null){
                Log.d(
                    "CoroutineUtils case 1",
                    "async cancelled ${it.message}"
                )
            }
        }
    }

    // As we launched a separate supervisor scope, its parent is no longer the parent coroutine but a supervisorJob
    // but its children are still inheriting standalone job
    fun exceptionInAsyncChildSupervisorScopeLaunchedInsideAsyncCoroutineScope1() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("async")).async {
            Log.d(
                "CoroutineUtils case 2",
                "async with customSupervisorScope.launch started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            // this block will have supervisorJob
            val job = customSupervisorScope.async {
                Log.d(
                    "CoroutineUtils case 2",
                    "customSupervisorScope with async blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                // these will still be standalone job
                val child1 = async(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 1 finished"
                    )
                }
                val child2 = async(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(5000)
                    // this will cancel child 2 and 3 as they are still inheriting the parent coroutine context
                    throw IllegalStateException("child2 crashed :(")
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 2 finished"
                    )
                }
                val child3 = async(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 2",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 2",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 2",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 2",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 2",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 2",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
                // this will have no affect as the children as cancelled
                // child2.await()
            }
            delay(9000)
            Log.d(
                "CoroutineUtils case 2",
                "customSupervisorScope with async blocks finished"
            )
        }.invokeOnCompletion {
            if(it!=null){
                Log.d(
                    "CoroutineUtils case 2",
                    "async cancelled ${it.message}"
                )
            }
        }
    }

    fun exceptionInAsyncChildSupervisorScopeLaunchedInsideAsyncCoroutineScope2() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("async")).async {
            Log.d(
                "CoroutineUtils case 3",
                "async with customSupervisorScope.async launching separate customSupervisorScopes started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            val job = customSupervisorScope.async {
                Log.d(
                    "CoroutineUtils case 3",
                    "customSupervisorScope with customSupervisorScope.async blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                val child1 = customSupervisorScope.async(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 1 finished"
                    )
                }
                val child2 = customSupervisorScope.async(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(5000)
                    // this will only cancel child 2 as the children themselves run in a supervisorJob
                    // but the exception will crash the launch block as customSupervisorScope does not stop exception propagation
                    throw IllegalStateException("child2 crashed :(")
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 2 finished"
                    )
                }
                val child3 = customSupervisorScope.async(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
                // uncomment to catch exception
                // child2.await()
            }
            // uncomment to catch exception, child 3 will also get cancelled as parent will get cancelled
            // child2.await()
            delay(10000)
            Log.d(
                "CoroutineUtils case 3",
                "customSupervisorScope with customSupervisorScope.async blocks finished"
            )
        }.invokeOnCompletion {
            if(it!=null){
                Log.d(
                    "CoroutineUtils case 3",
                    "async cancelled ${it.message}"
                )
            }
        }
    }

    // supervisorScope suspends the block and waits for children to complete
    fun exceptionInAsyncChildSupervisorScopeLaunchedInsideAsyncCoroutineScope3() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("async")).async {
            Log.d(
                "CoroutineUtils case 4",
                "async with supervisorScope started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            supervisorScope {
                Log.d(
                    "CoroutineUtils case 4",
                    "supervisorScope with async blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                val child1 = async(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 1 finished"
                    )
                }
                val child2 = async(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(5000)
                    // this will only cancel child 2 as the children themselves run in a supervisorJob
                    throw IllegalStateException("child2 crashed :(")
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 2 finished"
                    )
                }
                val child3 = async(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
                // uncomment to catch exception, child 3 will also get cancelled as parent will get cancelled
                // child2.await()
            }

            Log.d(
                "CoroutineUtils case 4",
                "supervisorScope with async blocks finished"
            )
        }.invokeOnCompletion {
            if(it!=null){
                Log.d(
                    "CoroutineUtils case 4",
                    "async cancelled ${it.message}"
                )
            }
        }
    }

    // this will run the code sequentially as supervisorScope is a suspending function
    fun exceptionInAsyncChildSupervisorScopeLaunchedInsideAsyncCoroutineScope4() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("async")).async {
            Log.d(
                "CoroutineUtils case 5",
                "launch with supervisorScope.launch blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            delay(2000)
            // this suspends the launch block
            val child1 = supervisorScope {
                async(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 finished"
                    )
                }
            }
            // this suspends the launch block
            val child2 = supervisorScope {
                async(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    // this will only cancel child 2 as the children inherit a supervisorJob
                    delay(5000)
                    throw IllegalStateException("child2 crashed :(")
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 finished"
                    )
                }
            }
            // this suspends the launch block
            val child3 = supervisorScope {
                async(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 finished"
                    )
                }
            }
            child1.invokeOnCompletion { e1 ->
                if (e1 == null) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 cancelled ${e1.message}"
                    )
                }
            }
            child2.invokeOnCompletion { e2 ->
                if (e2 == null) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 cancelled ${e2.message}"
                    )
                }
            }
            child3.invokeOnCompletion { e3 ->
                if (e3 == null) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 cancelled ${e3.message}"
                    )
                }
            }
            // uncomment to catch exception, parent will get cancelled
            // child2.await()
            Log.d(
                "CoroutineUtils case 5",
                "async with supervisorScope.launch blocks finished"
            )
        }.invokeOnCompletion {
            if(it!=null){
                Log.d(
                    "CoroutineUtils case 5",
                    "async cancelled ${it.message}"
                )
            }
        }
    }

    // this will run the each block sequentially as supervisorScope is a suspending function

    fun exceptionInAsyncChildSupervisorScopeLaunchedInsideAsyncCoroutineScope5() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("async")).async {
            Log.d(
                "CoroutineUtils case 6",
                "async with nested supervisorScope blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            supervisorScope {
                Log.d(
                    "CoroutineUtils case 6",
                    "supervisorScope with supervisorScope.launch blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                val child1 = supervisorScope {
                    async(CoroutineName("child 1")) {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                        )
                        delay(2000)
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 1 finished"
                        )
                    }
                }
                val child2 = supervisorScope {
                    async(CoroutineName("child 2")) {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                        )
                        delay(5000)
                        // this will only cancel child 2 as the children themselves run in a supervisorJob
                        throw IllegalStateException("child2 crashed :(")
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 2 finished"
                        )
                    }
                }
                val child3 = supervisorScope {
                    async(CoroutineName("child 3")) {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                        )
                        delay(6000)
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 3 finished"
                        )
                    }
                }


                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 6",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
                // uncomment to catch exception, parent will get cancelled
                // child2.await()
            }

            Log.d(
                "CoroutineUtils case 6",
                "supervisorScope with supervisorScope.launch blocks finished"
            )
        }.invokeOnCompletion {
            if(it!=null){
                Log.d(
                    "CoroutineUtils case 6",
                    "async cancelled ${it.message}"
                )
            }
        }
    }


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

    fun exceptionInChildSupervisorScopeLaunchedInsideALaunchedSuperVisorScope0() {

        customSupervisorScope.launch {
            Log.d(
                "CoroutineUtils case 1",
                "customSupervisorScope.launch launching separate customSupervisorScopes started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            delay(2000)
            val child1 = customSupervisorScope.launch(CoroutineName("child 1")) {
                Log.d(
                    "CoroutineUtils case 1",
                    "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                Log.d(
                    "CoroutineUtils case 1",
                    "child 1 finished"
                )
            }
            val child2 = customSupervisorScope.launch(CoroutineName("child 2")) {
                Log.d(
                    "CoroutineUtils case 1",
                    "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(5000)
                // this will only cancel child 2 as the children themselves run in a supervisorJob
                // but the exception will crash the launch block as customSupervisorScope does not stop exception propagation
                throw IllegalStateException("child2 crashed :(")
                Log.d(
                    "CoroutineUtils case 1",
                    "child 2 finished"
                )
            }
            val child3 = customSupervisorScope.launch(CoroutineName("child 3")) {
                Log.d(
                    "CoroutineUtils case 1",
                    "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(6000)
                Log.d(
                    "CoroutineUtils case 1",
                    "child 3 finished"
                )
            }
            child1.invokeOnCompletion { e1 ->
                if (e1 == null) {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 1 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 1 cancelled ${e1.message}"
                    )
                }
            }
            child2.invokeOnCompletion { e2 ->
                if (e2 == null) {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 2 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 2 cancelled ${e2.message}"
                    )
                }
            }
            child3.invokeOnCompletion { e3 ->
                if (e3 == null) {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 3 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 3 cancelled ${e3.message}"
                    )
                }
            }
            delay(10000)
            Log.d(
                "CoroutineUtils case 1",
                "customSupervisorScope.launch with customSupervisorScope.launch blocks finished"
            )
        }.invokeOnCompletion {
            if (it != null) {
                Log.d(
                    "CoroutineUtils case 1",
                    "launch cancelled ${it.message}"
                )
            }
        }
    }

    fun exceptionInChildSupervisorScopeLaunchedInsideALaunchedSuperVisorScope1() {
        customSupervisorScope.launch {
            Log.d(
                "CoroutineUtils case 2",
                "customSupervisorScope.launch launching separate launch blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            delay(2000)
            val child1 = launch(CoroutineName("child 1")) {
                Log.d(
                    "CoroutineUtils case 2",
                    "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                Log.d(
                    "CoroutineUtils case 2",
                    "child 1 finished"
                )
            }
            val child2 = launch(CoroutineName("child 2")) {
                Log.d(
                    "CoroutineUtils case 2",
                    "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(5000)
                // this will only cancel child 2 as the children themselves run in a supervisorJob
                // but the exception will crash the launch block as customSupervisorScope does not stop exception propagation
                throw IllegalStateException("child2 crashed :(")
                Log.d(
                    "CoroutineUtils case 2",
                    "child 2 finished"
                )
            }
            val child3 = launch(CoroutineName("child 3")) {
                Log.d(
                    "CoroutineUtils case 2",
                    "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(6000)
                Log.d(
                    "CoroutineUtils case 2",
                    "child 3 finished"
                )
            }
            child1.invokeOnCompletion { e1 ->
                if (e1 == null) {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 1 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 1 cancelled ${e1.message}"
                    )
                }
            }
            child2.invokeOnCompletion { e2 ->
                if (e2 == null) {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 2 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 2 cancelled ${e2.message}"
                    )
                }
            }
            child3.invokeOnCompletion { e3 ->
                if (e3 == null) {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 3 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 3 cancelled ${e3.message}"
                    )
                }
            }
            delay(10000)
            Log.d(
                "CoroutineUtils case 2",
                "customSupervisorScope.launch with launch blocks finished"
            )
        }.invokeOnCompletion {
            if (it != null) {
                Log.d(
                    "CoroutineUtils case 2",
                    "launch cancelled ${it.message}"
                )
            }
        }
    }

    fun exceptionInChildSupervisorScopeLaunchedInsideALaunchedSuperVisorScope2() {
        customSupervisorScope.launch {
            Log.d(
                "CoroutineUtils case 3",
                "customSupervisorScope.launch launching separate customSupervisorScopes in a supervisorScope started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            supervisorScope {
                delay(2000)
                val child1 = customSupervisorScope.launch(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 1 finished"
                    )
                }
                val child2 = customSupervisorScope.launch(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(5000)
                    // this will only cancel child 2 as the children themselves run in a supervisorJob
                    // but the exception will crash the launch block as customSupervisorScope does not stop exception propagation
                    throw IllegalStateException("child2 crashed :(")
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 2 finished"
                    )
                }
                val child3 = customSupervisorScope.launch(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
            }
            Log.d(
                "CoroutineUtils case 3",
                "customSupervisorScope.launch launching separate customSupervisorScopes in a supervisorScope finished"
            )
        }.invokeOnCompletion {
            if (it != null) {
                Log.d(
                    "CoroutineUtils case 3",
                    "launch cancelled ${it.message}"
                )
            }
        }
    }

    fun exceptionInChildSupervisorScopeLaunchedInsideALaunchedSuperVisorScope3() {
        customSupervisorScope.launch {
            Log.d(
                "CoroutineUtils case 4",
                "customSupervisorScope.launch launching separate launch blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            supervisorScope {
                delay(2000)
                val child1 = launch(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 1 finished"
                    )
                }
                val child2 = launch(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(5000)
                    // this will only cancel child 2 as the children themselves run in a supervisorJob
                    // but the exception will crash the launch block as customSupervisorScope does not stop exception propagation
                    throw IllegalStateException("child2 crashed :(")
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 2 finished"
                    )
                }
                val child3 = launch(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
            }
            Log.d(
                "CoroutineUtils case 4",
                "customSupervisorScope.launch with launch blocks finished"
            )
        }.invokeOnCompletion {
            if (it != null) {
                Log.d(
                    "CoroutineUtils case 4",
                    "launch cancelled ${it.message}"
                )
            }
        }
    }

    fun exceptionInChildSupervisorScopeLaunchedInsideALaunchedSuperVisorScope4() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).launch {
            Log.d(
                "CoroutineUtils case 5",
                "launch with separate customSupervisorScopes in a supervisorScope started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            supervisorScope {
                delay(2000)
                val child1 = customSupervisorScope.launch(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 finished"
                    )
                }
                val child2 = customSupervisorScope.launch(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(5000)
                    // this will only cancel child 2 as the children themselves run in a supervisorJob
                    // but the exception will crash the launch block as customSupervisorScope does not stop exception propagation
                    throw IllegalStateException("child2 crashed :(")
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 finished"
                    )
                }
                val child3 = customSupervisorScope.launch(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
            }
            Log.d(
                "CoroutineUtils case 5",
                "launch with separate customSupervisorScopes in a supervisorScope finished"
            )
        }.invokeOnCompletion {
            if (it != null) {
                Log.d(
                    "CoroutineUtils case 5",
                    "launch cancelled ${it.message}"
                )
            }
        }
    }

    fun exceptionInChildSupervisorScopeLaunchedInsideALaunchedSuperVisorScope5() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).launch {
            Log.d(
                "CoroutineUtils case 5",
                "launch with supervisorScope started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            supervisorScope {
                Log.d(
                    "CoroutineUtils case 5",
                    "supervisorScope with launch blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                val child1 = launch(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 finished"
                    )
                }
                val child2 = launch(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(5000)
                    // this will only cancel child 2 as the children themselves run in a supervisorJob
                    throw IllegalStateException("child2 crashed :(")
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 finished"
                    )
                }
                val child3 = launch(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
            }
            Log.d(
                "CoroutineUtils case 5",
                "launch with supervisorScope blocks finished"
            )
        }.invokeOnCompletion {
            if(it!=null){
                Log.d(
                    "CoroutineUtils case 5",
                    "launch cancelled ${it.message}"
                )
            }
        }
    }

    fun exceptionInChildSupervisorScopeLaunchedInsideALaunchedSuperVisorScope6() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).launch {
            Log.d(
                "CoroutineUtils case 7",
                "launch with nested supervisorScope blocks with launch blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            supervisorScope {
                Log.d(
                    "CoroutineUtils case 7",
                    "supervisorScope with supervisorScope.launch blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                val child1 = supervisorScope {
                    launch(CoroutineName("child 1")) {
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                        )
                        delay(2000)
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 1 finished"
                        )
                    }
                }
                val child2 = supervisorScope {
                    launch(CoroutineName("child 2")) {
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                        )
                        delay(5000)
                        // this will only cancel child 2 as the children themselves run in a supervisorJob
                        throw IllegalStateException("child2 crashed :(")
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 2 finished"
                        )
                    }
                }
                val child3 = supervisorScope {
                    launch(CoroutineName("child 3")) {
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                        )
                        delay(6000)
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 3 finished"
                        )
                    }
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
            }

            Log.d(
                "CoroutineUtils case 7",
                "launch with nested supervisorScope blocks with launch blocks finished"
            )
        }.invokeOnCompletion {
            if(it!=null){
                Log.d(
                    "CoroutineUtils case 7",
                    "launch cancelled ${it.message}"
                )
            }
        }
    }



    fun exceptionInChildSupervisorScopeLaunchedInsideAsyncSuperVisorScope0() {

        customSupervisorScope.async {
            Log.d(
                "CoroutineUtils case 1",
                "customSupervisorScope.async launching separate customSupervisorScopes started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            delay(2000)
            val child1 = customSupervisorScope.launch(CoroutineName("child 1")) {
                Log.d(
                    "CoroutineUtils case 1",
                    "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                Log.d(
                    "CoroutineUtils case 1",
                    "child 1 finished"
                )
            }
            val child2 = customSupervisorScope.launch(CoroutineName("child 2")) {
                Log.d(
                    "CoroutineUtils case 1",
                    "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(5000)
                // this will only cancel child 2 as the children themselves run in a supervisorJob
                // but the exception will crash the launch block as customSupervisorScope does not stop exception propagation
                throw IllegalStateException("child2 crashed :(")
                Log.d(
                    "CoroutineUtils case 1",
                    "child 2 finished"
                )
            }
            val child3 = customSupervisorScope.launch(CoroutineName("child 3")) {
                Log.d(
                    "CoroutineUtils case 1",
                    "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(6000)
                Log.d(
                    "CoroutineUtils case 1",
                    "child 3 finished"
                )
            }
            child1.invokeOnCompletion { e1 ->
                if (e1 == null) {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 1 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 1 cancelled ${e1.message}"
                    )
                }
            }
            child2.invokeOnCompletion { e2 ->
                if (e2 == null) {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 2 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 2 cancelled ${e2.message}"
                    )
                }
            }
            child3.invokeOnCompletion { e3 ->
                if (e3 == null) {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 3 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 3 cancelled ${e3.message}"
                    )
                }
            }
            delay(10000)
            Log.d(
                "CoroutineUtils case 1",
                "customSupervisorScope.async with customSupervisorScope.launch blocks finished"
            )
        }.invokeOnCompletion {
            if (it != null) {
                Log.d(
                    "CoroutineUtils case 1",
                    "async cancelled ${it.message}"
                )
            }
        }
    }

    fun exceptionInChildSupervisorScopeLaunchedInsideAsyncSuperVisorScope1() {
        customSupervisorScope.async {
            Log.d(
                "CoroutineUtils case 2",
                "customSupervisorScope.async launching separate launch blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            delay(2000)
            val child1 = launch(CoroutineName("child 1")) {
                Log.d(
                    "CoroutineUtils case 2",
                    "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                Log.d(
                    "CoroutineUtils case 2",
                    "child 1 finished"
                )
            }
            val child2 = launch(CoroutineName("child 2")) {
                Log.d(
                    "CoroutineUtils case 2",
                    "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(5000)
                // this will only cancel child 2 as the children themselves run in a supervisorJob
                // but the exception will crash the launch block as customSupervisorScope does not stop exception propagation
                throw IllegalStateException("child2 crashed :(")
                Log.d(
                    "CoroutineUtils case 2",
                    "child 2 finished"
                )
            }
            val child3 = launch(CoroutineName("child 3")) {
                Log.d(
                    "CoroutineUtils case 2",
                    "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(6000)
                Log.d(
                    "CoroutineUtils case 2",
                    "child 3 finished"
                )
            }
            child1.invokeOnCompletion { e1 ->
                if (e1 == null) {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 1 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 1 cancelled ${e1.message}"
                    )
                }
            }
            child2.invokeOnCompletion { e2 ->
                if (e2 == null) {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 2 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 2 cancelled ${e2.message}"
                    )
                }
            }
            child3.invokeOnCompletion { e3 ->
                if (e3 == null) {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 3 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 3 cancelled ${e3.message}"
                    )
                }
            }
            delay(10000)
            Log.d(
                "CoroutineUtils case 2",
                "customSupervisorScope.async with launch blocks finished"
            )
        }.invokeOnCompletion {
            if (it != null) {
                Log.d(
                    "CoroutineUtils case 2",
                    "async cancelled ${it.message}"
                )
            }
        }
    }

    fun exceptionInChildSupervisorScopeLaunchedInsideAsyncSuperVisorScope2() {
        customSupervisorScope.async {
            Log.d(
                "CoroutineUtils case 3",
                "customSupervisorScope.async launching separate customSupervisorScopes in a supervisorScope started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            supervisorScope {
                delay(2000)
                val child1 = customSupervisorScope.launch(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 1 finished"
                    )
                }
                val child2 = customSupervisorScope.launch(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(5000)
                    // this will only cancel child 2 as the children themselves run in a supervisorJob
                    // but the exception will crash the launch block as customSupervisorScope does not stop exception propagation
                    throw IllegalStateException("child2 crashed :(")
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 2 finished"
                    )
                }
                val child3 = customSupervisorScope.launch(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
            }
            Log.d(
                "CoroutineUtils case 3",
                "customSupervisorScope.async launching separate customSupervisorScopes in a supervisorScope finished"
            )
        }.invokeOnCompletion {
            if (it != null) {
                Log.d(
                    "CoroutineUtils case 3",
                    "async cancelled ${it.message}"
                )
            }
        }
    }

    fun exceptionInChildSupervisorScopeLaunchedInsideAsyncSuperVisorScope3() {
        customSupervisorScope.async {
            Log.d(
                "CoroutineUtils case 4",
                "customSupervisorScope.async launching separate launch blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            supervisorScope {
                delay(2000)
                val child1 = launch(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 1 finished"
                    )
                }
                val child2 = launch(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(5000)
                    // this will only cancel child 2 as the children themselves run in a supervisorJob
                    // but the exception will crash the launch block as customSupervisorScope does not stop exception propagation
                    throw IllegalStateException("child2 crashed :(")
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 2 finished"
                    )
                }
                val child3 = launch(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
            }
            Log.d(
                "CoroutineUtils case 4",
                "customSupervisorScope.async with launch blocks finished"
            )
        }.invokeOnCompletion {
            if (it != null) {
                Log.d(
                    "CoroutineUtils case 4",
                    "async cancelled ${it.message}"
                )
            }
        }
    }

    fun exceptionInChildSupervisorScopeLaunchedInsideAsyncSuperVisorScope4() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("async")).async {
            Log.d(
                "CoroutineUtils case 5",
                "async with separate customSupervisorScopes in a supervisorScope started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            supervisorScope {
                delay(2000)
                val child1 = customSupervisorScope.launch(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 finished"
                    )
                }
                val child2 = customSupervisorScope.launch(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(5000)
                    // this will only cancel child 2 as the children themselves run in a supervisorJob
                    // but the exception will crash the launch block as customSupervisorScope does not stop exception propagation
                    throw IllegalStateException("child2 crashed :(")
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 finished"
                    )
                }
                val child3 = customSupervisorScope.launch(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
            }
            Log.d(
                "CoroutineUtils case 5",
                "async with separate customSupervisorScopes in a supervisorScope finished"
            )
        }.invokeOnCompletion {
            if (it != null) {
                Log.d(
                    "CoroutineUtils case 5",
                    "async cancelled ${it.message}"
                )
            }
        }
    }

    fun exceptionInChildSupervisorScopeLaunchedInsideAsyncSuperVisorScope5() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("async")).async {
            Log.d(
                "CoroutineUtils case 5",
                "async with supervisorScope started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            supervisorScope {
                Log.d(
                    "CoroutineUtils case 5",
                    "supervisorScope with launch blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                val child1 = launch(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 finished"
                    )
                }
                val child2 = launch(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(5000)
                    // this will only cancel child 2 as the children themselves run in a supervisorJob
                    throw IllegalStateException("child2 crashed :(")
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 finished"
                    )
                }
                val child3 = launch(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
            }
            Log.d(
                "CoroutineUtils case 5",
                "async with supervisorScope blocks finished"
            )
        }.invokeOnCompletion {
            if(it!=null){
                Log.d(
                    "CoroutineUtils case 5",
                    "async cancelled ${it.message}"
                )
            }
        }
    }

    fun exceptionInChildSupervisorScopeLaunchedInsideAsyncSuperVisorScope6() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("async")).async {
            Log.d(
                "CoroutineUtils case 7",
                "async with nested supervisorScope blocks with launch blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            supervisorScope {
                Log.d(
                    "CoroutineUtils case 7",
                    "supervisorScope with supervisorScope.launch blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                val child1 = supervisorScope {
                    launch(CoroutineName("child 1")) {
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                        )
                        delay(2000)
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 1 finished"
                        )
                    }
                }
                val child2 = supervisorScope {
                    launch(CoroutineName("child 2")) {
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                        )
                        delay(5000)
                        // this will only cancel child 2 as the children themselves run in a supervisorJob
                        throw IllegalStateException("child2 crashed :(")
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 2 finished"
                        )
                    }
                }
                val child3 = supervisorScope {
                    launch(CoroutineName("child 3")) {
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                        )
                        delay(6000)
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 3 finished"
                        )
                    }
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
            }

            Log.d(
                "CoroutineUtils case 7",
                "async with nested supervisorScope blocks with launch blocks finished"
            )
        }.invokeOnCompletion {
            if(it!=null){
                Log.d(
                    "CoroutineUtils case 7",
                    "async cancelled ${it.message}"
                )
            }
        }
    }



    fun exceptionInAsyncChildSupervisorScopeInsideALaunchedSuperVisorScope0() {

        customSupervisorScope.launch {
            Log.d(
                "CoroutineUtils case 1",
                "customSupervisorScope.launch with separate async customSupervisorScopes started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            delay(2000)
            val child1 = customSupervisorScope.async(CoroutineName("child 1")) {
                Log.d(
                    "CoroutineUtils case 1",
                    "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                Log.d(
                    "CoroutineUtils case 1",
                    "child 1 finished"
                )
            }
            val child2 = customSupervisorScope.async(CoroutineName("child 2")) {
                Log.d(
                    "CoroutineUtils case 1",
                    "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(5000)
                // this will only cancel child 2 as the children themselves run in a supervisorJob
                // but the exception will crash the launch block as customSupervisorScope does not stop exception propagation
                throw IllegalStateException("child2 crashed :(")
                Log.d(
                    "CoroutineUtils case 1",
                    "child 2 finished"
                )
            }
            val child3 = customSupervisorScope.async(CoroutineName("child 3")) {
                Log.d(
                    "CoroutineUtils case 1",
                    "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(6000)
                Log.d(
                    "CoroutineUtils case 1",
                    "child 3 finished"
                )
            }
            child1.invokeOnCompletion { e1 ->
                if (e1 == null) {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 1 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 1 cancelled ${e1.message}"
                    )
                }
            }
            child2.invokeOnCompletion { e2 ->
                if (e2 == null) {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 2 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 2 cancelled ${e2.message}"
                    )
                }
            }
            child3.invokeOnCompletion { e3 ->
                if (e3 == null) {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 3 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 3 cancelled ${e3.message}"
                    )
                }
            }
            delay(10000)
            Log.d(
                "CoroutineUtils case 1",
                "customSupervisorScope.launch with separate async customSupervisorScopes finished"
            )
        }.invokeOnCompletion {
            if (it != null) {
                Log.d(
                    "CoroutineUtils case 1",
                    "launch cancelled ${it.message}"
                )
            }
        }
    }

    fun exceptionInAsyncChildSupervisorScopeInsideALaunchedSuperVisorScope1() {
        customSupervisorScope.launch {
            Log.d(
                "CoroutineUtils case 2",
                "customSupervisorScope.launch with separate async blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            delay(2000)
            val child1 = async(CoroutineName("child 1")) {
                Log.d(
                    "CoroutineUtils case 2",
                    "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                Log.d(
                    "CoroutineUtils case 2",
                    "child 1 finished"
                )
            }
            val child2 = async(CoroutineName("child 2")) {
                Log.d(
                    "CoroutineUtils case 2",
                    "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(5000)
                // this will only cancel child 2 as the children themselves run in a supervisorJob
                // but the exception will crash the launch block as customSupervisorScope does not stop exception propagation
                throw IllegalStateException("child2 crashed :(")
                Log.d(
                    "CoroutineUtils case 2",
                    "child 2 finished"
                )
            }
            val child3 = async(CoroutineName("child 3")) {
                Log.d(
                    "CoroutineUtils case 2",
                    "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(6000)
                Log.d(
                    "CoroutineUtils case 2",
                    "child 3 finished"
                )
            }
            child1.invokeOnCompletion { e1 ->
                if (e1 == null) {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 1 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 1 cancelled ${e1.message}"
                    )
                }
            }
            child2.invokeOnCompletion { e2 ->
                if (e2 == null) {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 2 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 2 cancelled ${e2.message}"
                    )
                }
            }
            child3.invokeOnCompletion { e3 ->
                if (e3 == null) {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 3 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 3 cancelled ${e3.message}"
                    )
                }
            }
            delay(10000)
            Log.d(
                "CoroutineUtils case 2",
                "customSupervisorScope.launch with async blocks finished"
            )
        }.invokeOnCompletion {
            if (it != null) {
                Log.d(
                    "CoroutineUtils case 2",
                    "launch cancelled ${it.message}"
                )
            }
        }
    }

    fun exceptionInAsyncChildSupervisorScopeInsideALaunchedSuperVisorScope2() {
        customSupervisorScope.launch {
            Log.d(
                "CoroutineUtils case 3",
                "customSupervisorScope.launch launching separate async customSupervisorScopes in a supervisorScope started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            supervisorScope {
                delay(2000)
                val child1 = customSupervisorScope.async(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 1 finished"
                    )
                }
                val child2 = customSupervisorScope.async(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(5000)
                    // this will only cancel child 2 as the children themselves run in a supervisorJob
                    // but the exception will crash the launch block as customSupervisorScope does not stop exception propagation
                    throw IllegalStateException("child2 crashed :(")
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 2 finished"
                    )
                }
                val child3 = customSupervisorScope.async(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
            }
            Log.d(
                "CoroutineUtils case 3",
                "customSupervisorScope.launch launching separate async customSupervisorScopes in a supervisorScope finished"
            )
        }.invokeOnCompletion {
            if (it != null) {
                Log.d(
                    "CoroutineUtils case 3",
                    "launch cancelled ${it.message}"
                )
            }
        }
    }

    fun exceptionInAsyncChildSupervisorScopeInsideALaunchedSuperVisorScope3() {
        customSupervisorScope.launch {
            Log.d(
                "CoroutineUtils case 4",
                "customSupervisorScope.launch with separate async blocks in a supervisorScope started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            supervisorScope {
                delay(2000)
                val child1 = async(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 1 finished"
                    )
                }
                val child2 = async(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(5000)
                    // this will only cancel child 2 as the children themselves run in a supervisorJob
                    // but the exception will crash the launch block as customSupervisorScope does not stop exception propagation
                    throw IllegalStateException("child2 crashed :(")
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 2 finished"
                    )
                }
                val child3 = async(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
            }
            Log.d(
                "CoroutineUtils case 4",
                "customSupervisorScope.launch with separate async blocks in a supervisorScope finished"
            )
        }.invokeOnCompletion {
            if (it != null) {
                Log.d(
                    "CoroutineUtils case 4",
                    "launch cancelled ${it.message}"
                )
            }
        }
    }

    fun exceptionInAsyncChildSupervisorScopeInsideALaunchedSuperVisorScope4() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).launch {
            Log.d(
                "CoroutineUtils case 5",
                "launch with separate async customSupervisorScopes in a supervisorScope started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            supervisorScope {
                delay(2000)
                val child1 = customSupervisorScope.async(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 finished"
                    )
                }
                val child2 = customSupervisorScope.async(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(5000)
                    // this will only cancel child 2 as the children themselves run in a supervisorJob
                    // but the exception will crash the launch block as customSupervisorScope does not stop exception propagation
                    throw IllegalStateException("child2 crashed :(")
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 finished"
                    )
                }
                val child3 = customSupervisorScope.async(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
            }
            Log.d(
                "CoroutineUtils case 5",
                "launch with separate async customSupervisorScopes in a supervisorScope finished"
            )
        }.invokeOnCompletion {
            if (it != null) {
                Log.d(
                    "CoroutineUtils case 5",
                    "launch cancelled ${it.message}"
                )
            }
        }
    }

    fun exceptionInAsyncChildSupervisorScopeInsideALaunchedSuperVisorScope5() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).launch {
            Log.d(
                "CoroutineUtils case 5",
                "launch with supervisorScope async blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            supervisorScope {
                Log.d(
                    "CoroutineUtils case 5",
                    "supervisorScope with async blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                val child1 = async(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 finished"
                    )
                }
                val child2 = async(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(5000)
                    // this will only cancel child 2 as the children themselves run in a supervisorJob
                    throw IllegalStateException("child2 crashed :(")
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 finished"
                    )
                }
                val child3 = async(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
            }
            Log.d(
                "CoroutineUtils case 5",
                "launch with supervisorScope async blocks finished"
            )
        }.invokeOnCompletion {
            if(it!=null){
                Log.d(
                    "CoroutineUtils case 5",
                    "launch cancelled ${it.message}"
                )
            }
        }
    }

    fun exceptionInAsyncChildSupervisorScopeInsideALaunchedSuperVisorScope6() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("launch")).launch {
            Log.d(
                "CoroutineUtils case 7",
                "launch with nested supervisorScope blocks with async blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            supervisorScope {
                Log.d(
                    "CoroutineUtils case 7",
                    "supervisorScope with supervisorScope.async blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                val child1 = supervisorScope {
                    async(CoroutineName("child 1")) {
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                        )
                        delay(2000)
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 1 finished"
                        )
                    }
                }
                val child2 = supervisorScope {
                    async(CoroutineName("child 2")) {
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                        )
                        delay(5000)
                        // this will only cancel child 2 as the children themselves run in a supervisorJob
                        throw IllegalStateException("child2 crashed :(")
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 2 finished"
                        )
                    }
                }
                val child3 = supervisorScope {
                    async(CoroutineName("child 3")) {
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                        )
                        delay(6000)
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 3 finished"
                        )
                    }
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
            }

            Log.d(
                "CoroutineUtils case 7",
                "launch with nested supervisorScope blocks with async blocks finished"
            )
        }.invokeOnCompletion {
            if(it!=null){
                Log.d(
                    "CoroutineUtils case 7",
                    "launch cancelled ${it.message}"
                )
            }
        }
    }


    fun exceptionInAsyncChildSupervisorScopeInsideAsyncSuperVisorScope0() {

        customSupervisorScope.async {
            Log.d(
                "CoroutineUtils case 1",
                "customSupervisorScope.async with separate async customSupervisorScopes started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            delay(2000)
            val child1 = customSupervisorScope.async(CoroutineName("child 1")) {
                Log.d(
                    "CoroutineUtils case 1",
                    "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                Log.d(
                    "CoroutineUtils case 1",
                    "child 1 finished"
                )
            }
            val child2 = customSupervisorScope.async(CoroutineName("child 2")) {
                Log.d(
                    "CoroutineUtils case 1",
                    "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(5000)
                // this will only cancel child 2 as the children themselves run in a supervisorJob
                // but the exception will crash the launch block as customSupervisorScope does not stop exception propagation
                throw IllegalStateException("child2 crashed :(")
                Log.d(
                    "CoroutineUtils case 1",
                    "child 2 finished"
                )
            }
            val child3 = customSupervisorScope.async(CoroutineName("child 3")) {
                Log.d(
                    "CoroutineUtils case 1",
                    "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(6000)
                Log.d(
                    "CoroutineUtils case 1",
                    "child 3 finished"
                )
            }
            child1.invokeOnCompletion { e1 ->
                if (e1 == null) {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 1 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 1 cancelled ${e1.message}"
                    )
                }
            }
            child2.invokeOnCompletion { e2 ->
                if (e2 == null) {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 2 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 2 cancelled ${e2.message}"
                    )
                }
            }
            child3.invokeOnCompletion { e3 ->
                if (e3 == null) {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 3 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 1",
                        "child 3 cancelled ${e3.message}"
                    )
                }
            }
            delay(10000)
            Log.d(
                "CoroutineUtils case 1",
                "customSupervisorScope.async with separate async customSupervisorScopes finished"
            )
        }.invokeOnCompletion {
            if (it != null) {
                Log.d(
                    "CoroutineUtils case 1",
                    "async cancelled ${it.message}"
                )
            }
        }
    }

    fun exceptionInAsyncChildSupervisorScopeInsideAsyncSuperVisorScope1() {
        customSupervisorScope.async {
            Log.d(
                "CoroutineUtils case 2",
                "customSupervisorScope.async with separate async blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            delay(2000)
            val child1 = async(CoroutineName("child 1")) {
                Log.d(
                    "CoroutineUtils case 2",
                    "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                Log.d(
                    "CoroutineUtils case 2",
                    "child 1 finished"
                )
            }
            val child2 = async(CoroutineName("child 2")) {
                Log.d(
                    "CoroutineUtils case 2",
                    "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(5000)
                // this will only cancel child 2 as the children themselves run in a supervisorJob
                // but the exception will crash the launch block as customSupervisorScope does not stop exception propagation
                throw IllegalStateException("child2 crashed :(")
                Log.d(
                    "CoroutineUtils case 2",
                    "child 2 finished"
                )
            }
            val child3 = async(CoroutineName("child 3")) {
                Log.d(
                    "CoroutineUtils case 2",
                    "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(6000)
                Log.d(
                    "CoroutineUtils case 2",
                    "child 3 finished"
                )
            }
            child1.invokeOnCompletion { e1 ->
                if (e1 == null) {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 1 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 1 cancelled ${e1.message}"
                    )
                }
            }
            child2.invokeOnCompletion { e2 ->
                if (e2 == null) {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 2 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 2 cancelled ${e2.message}"
                    )
                }
            }
            child3.invokeOnCompletion { e3 ->
                if (e3 == null) {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 3 completed"
                    )
                } else {
                    Log.d(
                        "CoroutineUtils case 2",
                        "child 3 cancelled ${e3.message}"
                    )
                }
            }
            delay(10000)
            Log.d(
                "CoroutineUtils case 2",
                "customSupervisorScope.async with async blocks finished"
            )
        }.invokeOnCompletion {
            if (it != null) {
                Log.d(
                    "CoroutineUtils case 2",
                    "launch cancelled ${it.message}"
                )
            }
        }
    }

    fun exceptionInAsyncChildSupervisorScopeInsideAsyncSuperVisorScope2() {
        customSupervisorScope.async {
            Log.d(
                "CoroutineUtils case 3",
                "customSupervisorScope.async launching separate async customSupervisorScopes in a supervisorScope started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            supervisorScope {
                delay(2000)
                val child1 = customSupervisorScope.async(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 1 finished"
                    )
                }
                val child2 = customSupervisorScope.async(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(5000)
                    // this will only cancel child 2 as the children themselves run in a supervisorJob
                    // but the exception will crash the launch block as customSupervisorScope does not stop exception propagation
                    throw IllegalStateException("child2 crashed :(")
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 2 finished"
                    )
                }
                val child3 = customSupervisorScope.async(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 3",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 3",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
            }
            Log.d(
                "CoroutineUtils case 3",
                "customSupervisorScope.async launching separate async customSupervisorScopes in a supervisorScope finished"
            )
        }.invokeOnCompletion {
            if (it != null) {
                Log.d(
                    "CoroutineUtils case 3",
                    "async cancelled ${it.message}"
                )
            }
        }
    }

    fun exceptionInAsyncChildSupervisorScopeInsideAsyncSuperVisorScope3() {
        customSupervisorScope.async {
            Log.d(
                "CoroutineUtils case 4",
                "customSupervisorScope.async with separate async blocks in a supervisorScope started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            supervisorScope {
                delay(2000)
                val child1 = async(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 1 finished"
                    )
                }
                val child2 = async(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(5000)
                    // this will only cancel child 2 as the children themselves run in a supervisorJob
                    // but the exception will crash the launch block as customSupervisorScope does not stop exception propagation
                    throw IllegalStateException("child2 crashed :(")
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 2 finished"
                    )
                }
                val child3 = async(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 4",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 4",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
            }
            Log.d(
                "CoroutineUtils case 4",
                "customSupervisorScope.async with separate async blocks in a supervisorScope finished"
            )
        }.invokeOnCompletion {
            if (it != null) {
                Log.d(
                    "CoroutineUtils case 4",
                    "async cancelled ${it.message}"
                )
            }
        }
    }

    fun exceptionInAsyncChildSupervisorScopeInsideAsyncSuperVisorScope4() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("async")).async {
            Log.d(
                "CoroutineUtils case 5",
                "async with separate async customSupervisorScopes in a supervisorScope started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            supervisorScope {
                delay(2000)
                val child1 = customSupervisorScope.async(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 finished"
                    )
                }
                val child2 = customSupervisorScope.async(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(5000)
                    // this will only cancel child 2 as the children themselves run in a supervisorJob
                    // but the exception will crash the launch block as customSupervisorScope does not stop exception propagation
                    throw IllegalStateException("child2 crashed :(")
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 finished"
                    )
                }
                val child3 = customSupervisorScope.async(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
            }
            Log.d(
                "CoroutineUtils case 5",
                "async with separate async customSupervisorScopes in a supervisorScope finished"
            )
        }.invokeOnCompletion {
            if (it != null) {
                Log.d(
                    "CoroutineUtils case 5",
                    "async cancelled ${it.message}"
                )
            }
        }
    }

    fun exceptionInAsyncChildSupervisorScopeInsideAsyncSuperVisorScope5() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("async")).async {
            Log.d(
                "CoroutineUtils case 5",
                "async with supervisorScope async blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            supervisorScope {
                Log.d(
                    "CoroutineUtils case 5",
                    "supervisorScope with async blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                val child1 = async(CoroutineName("child 1")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(2000)
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 1 finished"
                    )
                }
                val child2 = async(CoroutineName("child 2")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(5000)
                    // this will only cancel child 2 as the children themselves run in a supervisorJob
                    throw IllegalStateException("child2 crashed :(")
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 2 finished"
                    )
                }
                val child3 = async(CoroutineName("child 3")) {
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                    )
                    delay(6000)
                    Log.d(
                        "CoroutineUtils case 5",
                        "child 3 finished"
                    )
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 5",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
            }
            Log.d(
                "CoroutineUtils case 5",
                "async with supervisorScope async blocks finished"
            )
        }.invokeOnCompletion {
            if(it!=null){
                Log.d(
                    "CoroutineUtils case 5",
                    "async cancelled ${it.message}"
                )
            }
        }
    }

    fun exceptionInAsyncChildSupervisorScopeInsideAsyncSuperVisorScope6() {
        CoroutineScope(Dispatchers.Default + exceptionHandler + CoroutineName("async")).async {
            Log.d(
                "CoroutineUtils case 7",
                "async with nested supervisorScope blocks with async blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
            )
            supervisorScope {
                Log.d(
                    "CoroutineUtils case 7",
                    "supervisorScope with supervisorScope.async blocks started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                )
                delay(2000)
                val child1 = supervisorScope {
                    async(CoroutineName("child 1")) {
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 1 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                        )
                        delay(2000)
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 1 finished"
                        )
                    }
                }
                val child2 = supervisorScope {
                    async(CoroutineName("child 2")) {
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 2 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                        )
                        delay(5000)
                        // this will only cancel child 2 as the children themselves run in a supervisorJob
                        throw IllegalStateException("child2 crashed :(")
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 2 finished"
                        )
                    }
                }
                val child3 = supervisorScope {
                    async(CoroutineName("child 3")) {
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 3 started in ${coroutineContext[Job].toString()} with parent ${coroutineContext[Job]?.parent} "
                        )
                        delay(6000)
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 3 finished"
                        )
                    }
                }
                child1.invokeOnCompletion { e1 ->
                    if (e1 == null) {
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 1 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 1 cancelled ${e1.message}"
                        )
                    }
                }
                child2.invokeOnCompletion { e2 ->
                    if (e2 == null) {
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 2 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 2 cancelled ${e2.message}"
                        )
                    }
                }
                child3.invokeOnCompletion { e3 ->
                    if (e3 == null) {
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 3 completed"
                        )
                    } else {
                        Log.d(
                            "CoroutineUtils case 7",
                            "child 3 cancelled ${e3.message}"
                        )
                    }
                }
            }

            Log.d(
                "CoroutineUtils case 7",
                "async with nested supervisorScope blocks with async blocks finished"
            )
        }.invokeOnCompletion {
            if(it!=null){
                Log.d(
                    "CoroutineUtils case 7",
                    "async cancelled ${it.message}"
                )
            }
        }
    }


}