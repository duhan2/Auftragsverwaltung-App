package com.example.myapplication.archiv

import com.example.myapplication.kunde.Kunde

class ArchivChanges {

    var id = -1
    var name = ""
    private var telNummer = ""
    var auftragsliste = mutableListOf<Kunde>()

    fun setfromobj(archiv: Archiv) {
        this.id = archiv.id
        this.name = archiv.name
        this.telNummer = archiv.telNummer
        this.auftragsliste = archiv.auftragsliste.toMutableList()
    }

    fun createobj(): Archiv {
        return Archiv(this.id, this.name, this.telNummer, this.auftragsliste)
    }
}