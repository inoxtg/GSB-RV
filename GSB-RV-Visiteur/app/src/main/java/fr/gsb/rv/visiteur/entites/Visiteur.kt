package fr.gsb.rv.visiteur.entites

class Visiteur (
    var matricule: String,
    var password: String,
    var prenom: String,
    var nom: String
) {
    constructor() : this("", "", "", "")
    override fun toString(): String {
        return "Visiteur(matricule='$matricule', password='$password', prenom='$prenom', nom='$nom')"
    }


}