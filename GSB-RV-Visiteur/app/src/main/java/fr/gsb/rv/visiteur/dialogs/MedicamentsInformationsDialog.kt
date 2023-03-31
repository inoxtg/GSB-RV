package fr.gsb.rv.visiteur.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import fr.gsb.rv.visiteur.entites.Medicament


class MedicamentsInformationsDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle(
                "Description du médicament : " + arguments?.getString("nom")
                        + "\n" + "\n"

            )
            .setMessage(
                "DEPOT LEGAL : " + arguments?.getString("depotLegal")?.lowercase()
                        + "\n" + "\n" +
                "NOM COMMERCIAL : " + arguments?.getString("nom")?.lowercase()
                        + "\n" + "\n" +
                "EFEFTS : " + arguments?.getString("effet")?.lowercase()
                        + "\n" + "\n" +
                "COMPOSITION : " + arguments?.getString("composition")?.lowercase()
                        + "\n" + "\n" +
                "CONTRE INDICATIONS : " + arguments?.getString("indication")?.lowercase()
            )
            .setPositiveButton("Ok") { _,_ ->
            }
            .setCancelable(true)
            .create()
    companion object {
        fun newInstance(medicament: Medicament): MedicamentsInformationsDialog {
            val dialog = MedicamentsInformationsDialog()
            val args = Bundle()
            args.putString("depotLegal", medicament.depotLegal)
            args.putString("nom", medicament.nom)
            args.putString("effet", medicament.effet)
            args.putString("composition", medicament.composition)
            args.putString("code", medicament.code)
            args.putString("indication", medicament.indication)

            dialog.arguments = args
            return dialog
        }

        const val TAG = "MedicamentsInformationsDialog"
    }
}
