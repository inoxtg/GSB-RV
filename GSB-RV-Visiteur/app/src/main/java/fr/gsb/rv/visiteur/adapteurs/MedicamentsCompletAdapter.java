package fr.gsb.rv.visiteur.adapteurs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
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

    private final List<Medicament> listMedicaments;
    private final Context context;

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
        @SuppressLint("ViewHolder") View rowView = View.inflate(context, android.R.layout.simple_list_item_single_choice, null);
        Medicament med = listMedicaments.get(position);
        ((TextView)rowView.findViewById(android.R.id.text1))
                .setText(med.getNom()
                        + "    " + med.getDepotLegal());
        return rowView;
    }
}
