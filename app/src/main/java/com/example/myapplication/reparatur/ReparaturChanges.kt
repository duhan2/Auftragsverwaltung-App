package com.example.myapplication.reparatur

import androidx.annotation.RequiresApi
import com.example.myapplication.kunde.Kunde

class ReparaturChanges {

    var kundenid: Int = 0
    var numberinput: String = ""
    var nameinput: String = ""
    var auftragsstatus: String = ""
    var gesamtpreis: Float = 0.00F
    var extrasachen: String = ""
    var eingangsdatum: String = ""
    var archivid: Int = -1
    var editparam: String = ""

    //eventuell macht das probleme
    var gesamtreps = mutableListOf<Reparatur>()

    @RequiresApi(34)
    fun setfromKundeobj(kunde: Kunde) {
        this.kundenid = kunde.id
        this.nameinput = kunde.name
        this.numberinput = kunde.telNummer
        this.auftragsstatus = kunde.status
        this.gesamtpreis = kunde.gesPreis
        this.extrasachen = kunde.extras
        this.eingangsdatum = kunde.eingansdatum
        this.archivid = kunde.archivid

        //!Kann sein dass die Elemente kopiert werden müssen und nicht refrerenziert werden dürfen!
        this.gesamtreps = kunde.reparaturliste.toMutableList()
    }

    @RequiresApi(34)
    fun createKundenobj(): Kunde {
        return Kunde(
            this.kundenid,
            this.nameinput,
            this.gesamtpreis,
            this.numberinput,
            this.auftragsstatus,
            this.gesamtreps,
            this.extrasachen,
            this.eingangsdatum,
            this.archivid
        )
    }

    fun resetchanges() {
        this.kundenid = 0
        this.numberinput = ""
        this.gesamtreps.clear()
        this.nameinput = ""
        this.extrasachen = ""
        this.eingangsdatum = ""
        this.auftragsstatus = ""
        this.archivid = -1
        this.editparam = ""
    }
}