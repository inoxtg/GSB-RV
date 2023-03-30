package fr.gsb.rv.visiteur.entites

class Medicament (
    var depotLegal: String,
    var nom: String,
    var code: String,
    var composition: String,
    var effet: String,
    var indication: String
) {
    constructor(): this("","","","","","")

    override fun toString(): String {
        return "Medicament(depotLegal='$depotLegal', nom='$nom', code='$code', composition='$composition', effet='$effet', indication='$indication')"
    }


}