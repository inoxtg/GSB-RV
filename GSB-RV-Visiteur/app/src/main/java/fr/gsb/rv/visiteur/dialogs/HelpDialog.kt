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
                + " Accèder à vos anciens rapports.\n"
                + "Saisir :\n"
                + " Créer un nouveau rapport.")
            .setCancelable(true)
            .create()

    companion object {
        const val TAG = "HelpDialog"
    }
}
