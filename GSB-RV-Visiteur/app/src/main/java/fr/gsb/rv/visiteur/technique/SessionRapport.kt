package fr.gsb.rv.visiteur.technique
import fr.gsb.rv.visiteur.entites.RapportVisite
class SessionRapport
    private constructor(
        private var leRapport: RapportVisite?
    ) {

    companion object {
        private var sessionRapport: SessionRapport? = null

        fun ouvrir(rapportVisite: RapportVisite) {
            sessionRapport = SessionRapport(rapportVisite)
        }
        fun getLeRapport(): RapportVisite {
            return sessionRapport?.leRapport!!
        }

        fun fermer() {
            sessionRapport = null
        }
    }
}