package fr.gsb.rv.visiteur.entites

class RapportVisite(
    var leVisiteur: Visiteur,
    var lePraticien: Praticien,
    var numero: Int,
    var dateVisite: String,
    var dateRedac: String,
    var bilan: String,
    var motif: String,
    var coefConfiance: Int,
    var lu: Boolean
) {
    constructor(): this(Visiteur(), Praticien(), -1, "","","","",-1,false)

    override fun toString(): String {
        return "RapportVisite(" +
                "leVisiteur=$leVisiteur, " +
                "lePraticien=$lePraticien," +
                " numero=$numero," +
                " dateVisite='$dateVisite', " +
                "dateRedac='$dateRedac', " +
                "bilan='$bilan', " +
                "motif='$motif', " +
                "coefConfiance=$coefConfiance, " +
                "lu=$lu)"
    }


}