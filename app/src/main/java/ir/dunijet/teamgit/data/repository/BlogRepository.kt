package ir.dunijet.teamgit.data.repository

import ir.dunijet.teamgit.data.model.Blog

interface BlogRepository {
    suspend fun getBlogs() :List<Blog>
}