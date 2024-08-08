package com.example.myapplication.kunde


// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class KundeRepository(private val kundeDao: KundeDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    //val allKunden: Flow<List<Kunde>> = kundeDao.getAllKunden()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    //@WorkerThread
    suspend fun insert(kunde: Kunde) {
        kundeDao.insertKunde(kunde)
    }

    suspend fun delete(kunde: Kunde){
        kundeDao.removeKunde(kunde)
    }

    suspend fun update(kunde: Kunde){
        kundeDao.updateKunde(kunde)
    }



    fun getAllKunden() = kundeDao.getAllKunden()
}