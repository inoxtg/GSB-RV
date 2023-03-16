package fr.gsb.rv.visiteur.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import kotlin.system.exitProcess


class DeconnectionDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage(
                "Voulez vous vraiment vous déconnecter ?")
            .setPositiveButton("Oui") { _,_ ->
                exitProcess(-1)
            }
            .setNegativeButton("Non"){ _,_ ->

            }
            .create()

    companion object {
        const val TAG = "DeconnectionDialog"
    }
}
