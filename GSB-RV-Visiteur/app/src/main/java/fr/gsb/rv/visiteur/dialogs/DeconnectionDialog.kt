package fr.gsb.rv.visiteur.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import fr.gsb.rv.visiteur.ConsulterActivity
import fr.gsb.rv.visiteur.MainActivity
import fr.gsb.rv.visiteur.technique.Session
import kotlin.system.exitProcess


class DeconnectionDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage(
                "Voulez vous vraiment vous déconnecter ?")
            .setPositiveButton("Oui") { _,_ ->
                val i = Intent(this.context,  MainActivity::class.java)
                Session.fermer()
                startActivity(i)
            }
            .setNegativeButton("Non"){ _,_ -> }
            .setCancelable(true)
            .create()

    companion object {
        const val TAG = "DeconnectionDialog"
    }
}
