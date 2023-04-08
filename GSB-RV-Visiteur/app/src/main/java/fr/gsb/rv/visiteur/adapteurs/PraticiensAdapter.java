package fr.gsb.rv.visiteur.adapteurs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import fr.gsb.rv.visiteur.entites.Motif;
import fr.gsb.rv.visiteur.entites.Praticien;

import java.util.List;

public class PraticiensAdapter extends BaseAdapter {
    private final List<Praticien> listPraticiens;
    private final Context context;

    public PraticiensAdapter(Context context, List listPraticiens){
        this.listPraticiens = listPraticiens;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listPraticiens.size();
    }

    @Override
    public Praticien getItem(int position) {
        return listPraticiens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder") View rowView = View.inflate(context, android.R.layout.simple_list_item_single_choice, null);
        Praticien pra = listPraticiens.get(position);
        ((TextView)rowView.findViewById(android.R.id.text1))
                .setText(
                        pra.getNom() + " " +
                        pra.getPrenom() + " " +
                        pra.getVille() + " " +
                        pra.getCp()
                );
        return rowView;
    }
}
