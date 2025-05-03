package com.healthapi.repositories

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.healthapi.config.FileStorageProperties
import com.healthapi.exceptions.EntityNotFoundException
import com.healthapi.utils.LocalDateAdapter
import com.healthapi.utils.LocalDateTimeAdapter
import org.springframework.stereotype.Repository
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@Repository
abstract class JsonFileRepository<T : Any, ID>(
    private val fileStorageProperties: FileStorageProperties,
    private val entityClass: Class<T>,
    private val filePrefix: String
) {
    private val gson: Gson = GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()
    
    private val lock = ReentrantReadWriteLock()
    private val dataFile: File
    
    init {
        val dataDirectory = File(fileStorageProperties.dataLocation)
        if (!dataDirectory.exists()) {
            dataDirectory.mkdirs()
        }
        
        dataFile = File(dataDirectory, "$filePrefix.json")
        if (!dataFile.exists()) {
            dataFile.createNewFile()
            FileWriter(dataFile).use { writer ->
                gson.toJson(emptyList<T>(), writer)
            }
        }
    }
    
    abstract fun getId(entity: T): ID
    
    fun findAll(): List<T> {
        return lock.read {
            if (dataFile.length() == 0L) {
                emptyList()
            } else {
                FileReader(dataFile).use { reader ->
                    val listType: Type = TypeToken.getParameterized(List::class.java, entityClass).type
                    gson.fromJson(reader, listType) ?: emptyList()
                }
            }
        }
    }
    
    fun findById(id: ID): T {
        return lock.read {
            val entities = findAll()
            entities.find { getId(it) == id } ?: throw EntityNotFoundException("Entity with ID $id not found")
        }
    }
    
    fun save(entity: T): T {
        return lock.write {
            val entities = findAll().toMutableList()
            val entityId = getId(entity)
            val existingIndex = entities.indexOfFirst { getId(it) == entityId }
            
            if (existingIndex >= 0) {
                entities[existingIndex] = entity
            } else {
                entities.add(entity)
            }
            
            FileWriter(dataFile).use { writer ->
                gson.toJson(entities, writer)
            }
            
            entity
        }
    }
    
    fun saveAll(entities: List<T>): List<T> {
        return lock.write {
            val existingEntities = findAll().toMutableList()
            val entityMap = existingEntities.associateBy { getId(it) }.toMutableMap()
            
            entities.forEach { entity ->
                entityMap[getId(entity)] = entity
            }
            
            FileWriter(dataFile).use { writer ->
                gson.toJson(entityMap.values, writer)
            }
            
            entities
        }
    }
    
    fun deleteById(id: ID): Boolean {
        return lock.write {
            val entities = findAll().toMutableList()
            val initialSize = entities.size
            val filteredEntities = entities.filter { getId(it) != id }
            
            if (filteredEntities.size == initialSize) {
                return@write false
            }
            
            FileWriter(dataFile).use { writer ->
                gson.toJson(filteredEntities, writer)
            }
            
            true
        }
    }
    
    fun count(): Long {
        return lock.read {
            findAll().size.toLong()
        }
    }
}
