package com.example.medalgorithms.data

import kotlinx.coroutines.flow.Flow

class TemplateRepository(private val dao: TemplateDao) {
    fun observeByCategory(category: String): Flow<List<TemplateEntity>> = dao.observeByCategory(category)

    suspend fun getById(id: Long): TemplateEntity? = dao.getById(id)

    suspend fun upsert(entity: TemplateEntity): Long {
        return if (entity.id == 0L) {
            dao.insert(entity)
        } else {
            dao.update(entity)
            entity.id
        }
    }

    suspend fun delete(entity: TemplateEntity) = dao.delete(entity)
}
