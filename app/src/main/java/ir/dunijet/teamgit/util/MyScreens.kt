package ir.dunijet.teamgit.util

sealed class MyScreens(val route: String) {
    object HomeScreen : MyScreens("homeScreen")
}