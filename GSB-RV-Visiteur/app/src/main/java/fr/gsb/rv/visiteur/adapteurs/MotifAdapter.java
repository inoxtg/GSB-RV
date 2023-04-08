package fr.gsb.rv.visiteur.adapteurs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import fr.gsb.rv.visiteur.entites.Motif;

import java.util.List;
public class MotifAdapter extends BaseAdapter {
    private final List<Motif> listMotifs;
    private final Context context;

    public MotifAdapter(Context context, List listMotifs){
        this.listMotifs = listMotifs;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listMotifs.size();
    }

    @Override
    public Motif getItem(int position) {
        return listMotifs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder") View rowView = View.inflate(context, android.R.layout.simple_list_item_single_choice, null);
        Motif mot = listMotifs.get(position);
        ((TextView)rowView.findViewById(android.R.id.text1))
                .setText(mot.getLibelle());
        return rowView;
    }
}
