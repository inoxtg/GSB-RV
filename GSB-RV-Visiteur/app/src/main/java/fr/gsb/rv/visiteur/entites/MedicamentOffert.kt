package fr.gsb.rv.visiteur.entites

class MedicamentOffert(
    var leMedicament: Medicament,
    var quantite: Int
) {
    constructor(): this (Medicament(),-1)
    override fun toString(): String {
        return "MedicamentOffert(nom='${leMedicament.toString()}', quantite=$quantite)"
    }
}