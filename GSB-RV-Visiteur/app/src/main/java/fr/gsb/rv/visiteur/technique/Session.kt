package fr.gsb.rv.visiteur.technique
import fr.gsb.rv.visiteur.entites.Visiteur
class Session
    private constructor(
        private var leVisiteur: Visiteur?
    ) {

    companion object {
        private var session: Session? = null

        fun getSession(): Session? {
            return session
        }
        fun ouvrir(visiteur: Visiteur) {
            session = Session(visiteur)
        }
        fun getLevisiteur(): Visiteur {
            return session?.leVisiteur!!
        }

        fun fermer() {
            session = null
        }
    }
    override fun toString(): String {
        return "Session{leVisiteur=$leVisiteur, session=$session}"
    }
}