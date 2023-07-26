package ir.dunijet.teamgit.data.repository

import ir.dunijet.teamgit.data.model.Blog
import ir.dunijet.teamgit.data.net.ApiService

class RetrofitBlogRepository(private val apiService: ApiService) :BlogRepository {

    override suspend fun getBlogs(): List<Blog> {
        return apiService.getBlogs().blogs
    }

}