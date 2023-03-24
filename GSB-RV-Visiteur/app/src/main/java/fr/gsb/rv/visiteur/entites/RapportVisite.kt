package fr.gsb.rv.visiteur.entites

import java.util.Date

class RapportVisite (
    var leVisiteur: Visiteur,
    var lePraticien: Praticien,
    var numero: Int,
    var dateVisite: String,
    var bilan: String,
) {
    constructor(): this(Visiteur(), Praticien(), -1, "","")

    override fun toString(): String {
        return "RapportVisite(leVisiteur=$leVisiteur, lePraticien=$lePraticien, numero=$numero, dateVisite='$dateVisite', bilan='$bilan')"
    }

}