package com.example.myapplication.archiv

class ArchivRepository(private val archivDao: ArchivDao) {
    @Suppress("RedundantSuspendModifier")
    //@WorkerThread
    suspend fun insert(archiv: Archiv) {
        archivDao.insertArchiv(archiv)
    }

    suspend fun delete(archiv: Archiv) {
        archivDao.removeArchiv(archiv)
    }

    suspend fun update(archiv: Archiv) {
        archivDao.updateArchiv(archiv)
    }

    fun getAllArchiv() = archivDao.getAllArchiv()
}