package fr.gsb.rv.visiteur.entites

class Motif (
    var numero: Int,
    var libelle: String
) {
    constructor(): this(-1,"")

    override fun toString(): String {
        return "Motif(numero=$numero, libelle='$libelle')"
    }


}