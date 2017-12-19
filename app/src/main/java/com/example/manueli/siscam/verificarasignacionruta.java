package com.example.manueli.siscam;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class verificarasignacionruta extends AppCompatActivity {


    private EjecutarDatostask mAuthTask = null;
    obtenerverificarasignacionruta  rutas;
    RutasConfirmacion rutasobj;
    private ListView list;
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificarasignacionruta);
        list = (ListView) findViewById(R.id.listarutas);
        list.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        context = this;
        abrirMostrardatos();

    }


    private void abrirMostrardatos() {

        mAuthTask = null;
        mAuthTask = new EjecutarDatostask();
        mAuthTask.execute();
    }

    public class EjecutarDatostask extends AsyncTask<String,String, JSONObject>{


        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject jsonObject = new JSONObject();
            rutas = new obtenerverificarasignacionruta();
            jsonObject =  rutas.obtenerRutas();
            return jsonObject;
        }

        @Override
        protected void onPostExecute(final JSONObject success) {
            JSONObject rutas = new JSONObject();
            RutasConfirmacion rutasobj;
            ArrayList<RutasConfirmacion> Listarutas = new ArrayList<>();

            try{
                JSONArray datos = success.getJSONArray("datos");
                for (int i =0 ; i<datos.length();i++){
                    rutas = datos.getJSONObject(i);
                    rutasobj = new RutasConfirmacion(rutas.getString("CodRuta"),rutas.getString("Empresa"),rutas.getString("HoraIni"),rutas.getString("Ruta"),rutas.getString("Tipo"));
                    Listarutas.add(rutasobj);
                }

                ArrayAdapter<RutasConfirmacion> adaptador = new ArrayAdapter<RutasConfirmacion>(context, android.R.layout.simple_list_item_1,Listarutas);
                list.setAdapter(adaptador);
               //System.out.println(success);

            }catch(Exception e){
                e.printStackTrace();
            }

        }


        @Override
        protected void onCancelled() {

        }
    }

}
