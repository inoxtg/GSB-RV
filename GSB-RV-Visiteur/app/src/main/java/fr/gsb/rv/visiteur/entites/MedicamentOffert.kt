package fr.gsb.rv.visiteur.entites

class MedicamentOffert(
    var nom: String,
    var quantite: Int
) {
    constructor(): this ("",-1)
    override fun toString(): String {
        return "MedicamentOffert(nom='$nom', quantite=$quantite)"
    }
}