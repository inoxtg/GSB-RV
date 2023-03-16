package fr.gsb.rv.visiteur.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class HelpDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage(
                  "Consulter :\n"
                + " vous permet d\'accèder à vos anciens rapports.\n"
                + "\nSaisir :\n"
                + " vous permet de créer un nouveau rapport.")
            .setCancelable(true)
            .create()

    companion object {
        const val TAG = "HelpDialog"
    }
}
