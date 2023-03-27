package fr.gsb.rv.visiteur.adapteurs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import fr.gsb.rv.visiteur.R;
import fr.gsb.rv.visiteur.entites.RapportVisite;

import java.util.List;

public class RapportAdapter extends BaseAdapter {

    private List<RapportVisite> listRapports;
    private Context context;

    public RapportAdapter(Context context, List listRapports){
        this.listRapports = listRapports;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listRapports.size();
    }

    @Override
    public Object getItem(int position) {
        return listRapports.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = View.inflate(context, R.layout.list_rapports_visite, null);
        RapportVisite rv = listRapports.get(position);
        ((TextView)rowView.findViewById(android.R.id.text1))
                .setText(rv.getNumero()
                        + "  -  " + rv.getDateVisite()
                        + "  -  " + rv.getLePraticien().getNom());
        return rowView;
    }
}
