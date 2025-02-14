package com.apps.kunalfarmah.coroutineexamples

import android.util.Log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope

object CoroutineUtils {

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.d("CoroutineUtils exceptionHandler", "exception ${throwable.javaClass.name} in ${coroutineContext[CoroutineName]?.name} running a ${coroutineContext[Job].toString()}  , ${throwable.message}")
    }

    fun launch(){
       CoroutineScope(Dispatchers.Main + exceptionHandler + CoroutineName("launch")).launch {
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
        CoroutineScope(Dispatchers.Main + exceptionHandler + CoroutineName("async")).async {
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
        CoroutineScope(Dispatchers.Main + exceptionHandler + CoroutineName("launch 1")).launch {
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
        CoroutineScope(Dispatchers.Main + exceptionHandler + CoroutineName("launch")).launch {
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
        CoroutineScope(Dispatchers.Main + exceptionHandler + CoroutineName("async")).async {
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
        CoroutineScope(Dispatchers.Main + exceptionHandler + CoroutineName("async 1")).async {
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
        CoroutineScope(Dispatchers.Main + exceptionHandler + CoroutineName("runBlocking")).launch {
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
            launch(Dispatchers.Main + exceptionHandler + CoroutineName("coroutineScoped")){
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
            launch(Dispatchers.Main + exceptionHandler + CoroutineName("supervisorScoped")){
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
        CoroutineScope(Dispatchers.Main + exceptionHandler + CoroutineName("launch")).launch {
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

    //generate empty functions for the rest of the methods
    fun cancellationInAsync() {
        CoroutineScope(Dispatchers.Main + exceptionHandler + CoroutineName("async")).async {
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


    // if the parent throws exception, all children will be cancelled
    fun exceptionInCoroutineScopeAsParentLaunchingChildren() {
        // exception will be caught by the exceptionHandler
        CoroutineScope(Dispatchers.Main + exceptionHandler + CoroutineName("launch")).launch {
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
    // please note that  CoroutineScope(SupervisorJob() + Dispatchers.Main + exceptionHandler + CoroutineName("launch")) DOES not launch the
    // coroutine in the supervisor scope
    fun exceptionInSupervisorScopeAsParentLaunchingChildren() {
        // exception will be caught by the exceptionHandler
        CoroutineScope(SupervisorJob() + Dispatchers.Main + exceptionHandler + CoroutineName("launch")).launch {
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


    fun exceptionInCoroutineScopeChildrenInLaunch() {
        // exception will be caught by the exceptionHandler
        CoroutineScope(Dispatchers.Main + exceptionHandler + CoroutineName("launch")).launch {
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
        val result = CoroutineScope(Dispatchers.Main + exceptionHandler + CoroutineName("launch")).async {
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
        /*CoroutineScope(Dispatchers.Main + exceptionHandler + CoroutineName("crash detector")).launch {
            Log.d("CoroutineUtils exceptionInCoroutineScopeAsParentInAsync", "Exception hasn't been caught yet, will be caught once await is called")
            // this will throw the exception that was contained in the async block
            result.await()
        }*/
    }


    fun exceptionInSuperVisorScopeChildrenInLaunch(){

        CoroutineScope(Dispatchers.Main + exceptionHandler + CoroutineName("launch")).launch {
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
        CoroutineScope(Dispatchers.Main + exceptionHandler + CoroutineName("launch")).async {
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


    fun exceptionInCoroutineScopeWithSupervisorJobAsContextAsParentInLaunch() {
    }

    fun exceptionInCoroutineScopeWithSupervisorJobAsContextAsParentInAsync() {
    }

    fun exceptionInCoroutineScopeWithSupervisorJobAsContextAsChildInLaunch() {
    }

    fun exceptionInCoroutineScopeWithSupervisorJobAsContextAsChildInAsync() {
    }






}