package com.example.manueli.siscam;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



public class RutaconfirmacionAdapter extends ArrayAdapter<RutasConfirmacion> {

    List<RutasConfirmacion> linkedList;
    private boolean[] checkBoxState = null;
    private HashMap<RutasConfirmacion, Boolean> checkedForruta = new HashMap<RutasConfirmacion, Boolean>();

    public RutaconfirmacionAdapter(Context context, int resource, List<RutasConfirmacion> RutasConfirmacion) {
        super(context, resource, RutasConfirmacion);
        linkedList =RutasConfirmacion;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        RutasConfirmacion ruta = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_rutasconfirmar, parent, false);
            holder = new ViewHolder();
            holder.selectionBox = (CheckBox) convertView.findViewById(R.id.chkruta);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // Lookup view for data population
        TextView tvcodruta = (TextView) convertView.findViewById(R.id.CodRuta);
        TextView tvempresa = (TextView) convertView.findViewById(R.id.Empresa);
        TextView tvHoraIni = (TextView) convertView.findViewById(R.id.HoraIni);
        TextView tvRuta = (TextView) convertView.findViewById(R.id.Ruta);
        TextView tvTipo = (TextView) convertView.findViewById(R.id.Tipo);
        CheckBox chkselect = (CheckBox) convertView.findViewById(R.id.chkruta);

        chkselect.setChecked(false);

        // Populate the data into the template view using the data object

        tvcodruta.setText(ruta.CodRuta);
        tvempresa.setText(ruta.Empresa);
        tvHoraIni.setText(ruta.HoraIni);
        tvRuta.setText(ruta.Ruta);
        tvTipo.setText(ruta.Tipo);

        final RutasConfirmacion rut = linkedList.get(position);
        checkBoxState = new boolean[linkedList.size()];

        if(checkBoxState != null)
            holder.selectionBox.setChecked(checkBoxState[position]);

        holder.selectionBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()) {
                    checkBoxState[position] = true;
                    ischecked(position,true);
                }
                else {
                    checkBoxState[position] = false;
                    ischecked(position,false);
                }
            }
        });

        if (checkedForruta.get(rut) != null) {
            holder.selectionBox.setChecked(checkedForruta.get(rut));
        }

        /**Set tag to all checkBox**/
        holder.selectionBox.setTag(rut);

        return convertView;
    }

    private class ViewHolder {
        TextView title;
        CheckBox selectionBox;
    }
    public void ischecked(int position,boolean flag )
    {
        checkedForruta.put(this.linkedList.get(position), flag);
    }


    public HashMap<RutasConfirmacion, Boolean> obtenerchecados(){
        return checkedForruta;

    }


}
