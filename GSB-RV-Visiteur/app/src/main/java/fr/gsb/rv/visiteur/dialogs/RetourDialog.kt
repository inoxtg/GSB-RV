package fr.gsb.rv.visiteur.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import fr.gsb.rv.visiteur.ConsulterActivity
import fr.gsb.rv.visiteur.MainActivity
import fr.gsb.rv.visiteur.technique.SessionRapport
import fr.gsb.rv.visiteur.technique.SessionUser
import kotlin.system.exitProcess


class RetourDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage(
                "Voulez vous vraiment vous revenir en arrière ?")
            .setPositiveButton("Oui") { _,_ ->
                exitProcess(-1)
            }
            .setNegativeButton("Non"){ _,_ -> }
            .setCancelable(true)
            .create()

    companion object {
        const val TAG = "RetourDialog"
    }
}
