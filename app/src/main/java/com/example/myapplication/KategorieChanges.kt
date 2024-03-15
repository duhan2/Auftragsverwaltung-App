package com.example.myapplication

class KategorieChanges {
    var kategoriename: String = "leer"
    var reparaturname: String = "leer"
    var reparaturpreis: Float = 0.0f
    var repid: Int = -1

    fun resetchanges(){
        this.kategoriename = "leer"
        this.reparaturname = "leer"
        this.reparaturpreis = 0.0f
        this.repid = -1
    }
    fun resetkatname(){
        this.kategoriename = "leer"
    }
    fun resetrepname(){
        this.reparaturname = "leer"
    }
    fun resetreppreis(){
        this.reparaturpreis = 0.0f
    }
    fun resetrepid(){
        this.repid = -1
    }
}