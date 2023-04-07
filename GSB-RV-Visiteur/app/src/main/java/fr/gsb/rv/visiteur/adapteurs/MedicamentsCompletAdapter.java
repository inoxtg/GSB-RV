package fr.gsb.rv.visiteur.adapteurs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import fr.gsb.rv.visiteur.R;
import fr.gsb.rv.visiteur.entites.Medicament;
import fr.gsb.rv.visiteur.entites.MedicamentOffert;
import fr.gsb.rv.visiteur.entites.RapportVisite;

import java.util.List;

public class MedicamentsCompletAdapter extends BaseAdapter {

    private List<Medicament> listMedicaments;
    private Context context;

    public MedicamentsCompletAdapter(Context context, List listMedicaments){
        this.listMedicaments = listMedicaments;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listMedicaments.size();
    }

    @Override
    public Medicament getItem(int position) {
        return listMedicaments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = View.inflate(context, R.layout.list_medicaments_offerts, null);
        Medicament med = listMedicaments.get(position);
        ((TextView)rowView.findViewById(R.id.textMedicaments))
                .setText(med.getNom()
                        + "  -  " + med.getDepotLegal());
        return rowView;
    }
}
