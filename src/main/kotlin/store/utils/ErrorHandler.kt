package store.utils

object ErrorHandler {
    private const val ERROR_LABLE = "[ERROR]"
    private const val RETRY_INPUT_SCRIPT = "다시 입력해 주세요."

    fun getErrorMessage(message : String) : String = "$ERROR_LABLE $message $RETRY_INPUT_SCRIPT "
}