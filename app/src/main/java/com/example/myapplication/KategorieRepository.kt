package com.example.myapplication

class KategorieRepository(private val kategorieDao: KategorieDao) {

    @Suppress("RedundantSupressModifier")
    suspend fun insert(kategorie: Kategorie){
        kategorieDao.insertKategorie(kategorie)
    }

    suspend fun delete(kategorie: Kategorie){
        kategorieDao.removeKategorie(kategorie)
    }

    suspend fun update(kategorie: Kategorie){
        kategorieDao.updateKategorie(kategorie)
    }


    fun getAllKategorien() = kategorieDao.getAllKategorien()

}