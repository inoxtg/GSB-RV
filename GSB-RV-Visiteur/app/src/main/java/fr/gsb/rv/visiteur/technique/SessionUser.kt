package fr.gsb.rv.visiteur.technique
import fr.gsb.rv.visiteur.entites.Visiteur
class SessionUser
    private constructor(
        private var leVisiteur: Visiteur?
    ) {

    companion object {
        private var sessionUser: SessionUser? = null

        fun ouvrir(visiteur: Visiteur) {
            sessionUser = SessionUser(visiteur)
        }
        fun getLevisiteur(): Visiteur {
            return sessionUser?.leVisiteur!!
        }

        fun fermer() {
            sessionUser = null
        }
    }
    override fun toString(): String {
        return "Session{leVisiteur=$leVisiteur, session=$sessionUser}"
    }
}