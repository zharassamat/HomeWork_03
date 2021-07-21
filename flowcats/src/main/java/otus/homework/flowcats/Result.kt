package otus.homework.flowcats

sealed class Result {
    object Initial : Result()
    data class Success(val data: Fact) : Result()
    data class Error(var message: String) : Result()
}