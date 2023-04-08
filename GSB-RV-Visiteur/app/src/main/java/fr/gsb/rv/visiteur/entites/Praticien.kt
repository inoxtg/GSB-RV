package fr.gsb.rv.visiteur.entites

class Praticien (
    var numero: Int,
    var nom: String,
    var prenom: String,
    var ville: String,
    var cp: String,
){
    constructor() : this(-1,"", "", "", "")
    override fun toString(): String {
        return "Praticien(numero='$numero', nom='$nom', prenom='$prenom', ville='$ville', cp='$cp')"
    }
}