package ir.dunijet.teamgit.ui.fetures.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.dunijet.teamgit.data.model.Blog
import ir.dunijet.teamgit.data.repository.BlogRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val blogRepository: BlogRepository) : ViewModel() {
    private val _blogs = MutableStateFlow<List<Blog>>(emptyList())
    private val _isLoading = MutableStateFlow(false)

    val blogs: StateFlow<List<Blog>> = _blogs
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchBlogs()
    }

    fun fetchBlogs() {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val blogList = blogRepository.getBlogs()
                _blogs.emit( blogList.shuffled() )
            } catch (ex: Exception) {
                Log.v("TeamGitLog", ex.message ?: "exception in fetchBlogs()")
            }

            delay(200)
            _isLoading.value = false
        }
    }

}