package otus.homework.flowcats

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CatsViewModel(
    private val catsRepository: CatsRepository
) : ViewModel() {

    private val _catsLiveData = MutableLiveData<Fact>()
    val catsLiveData: LiveData<Fact> = _catsLiveData

    private val _catsState = MutableStateFlow<Result>(Result.Success(Fact("")))
    val catsState: StateFlow<Result> = _catsState

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    catsRepository.listenForCatFacts().collect {
                        _catsState.emit(Result.Success(it))
                    }
                } catch (ex: Exception) {
                    _catsState.emit(Result.Error(ex.message.toString()))
                }
            }
        }
    }
}

class CatsViewModelFactory(private val catsRepository: CatsRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        CatsViewModel(catsRepository) as T
}