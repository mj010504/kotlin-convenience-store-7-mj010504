package store.utils

object ErrorHandler {
    private const val ERROR_LABLE = "[ERROR]"

    fun <T> inputErrorHandler(
        message : String,
        function: () -> T
    ) : T {
        println("$ERROR_LABLE $message")
        return function()
    }
}