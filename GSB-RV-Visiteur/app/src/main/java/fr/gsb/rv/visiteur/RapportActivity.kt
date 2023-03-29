package fr.gsb.rv.visiteur

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import fr.gsb.rv.visiteur.entites.RapportVisite
import fr.gsb.rv.visiteur.technique.SessionRapport
import java.io.Serializable

class RapportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rapport)

        var leRapport = SessionRapport.getLeRapport()
        Log.v("Rapport choisis", leRapport.toString())
    }
}