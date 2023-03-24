package fr.gsb.rv.visiteur.entites

class Praticien (
    var nom: String,
    var prenom: String,
    var ville: String,
    var cp: String,
){
    constructor() : this("", "", "", "")
    override fun toString(): String {
        return "Praticien(nom='$nom', prenom='$prenom', ville='$ville', cp='$cp')"
    }
}