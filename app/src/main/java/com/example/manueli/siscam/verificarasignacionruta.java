package com.example.manueli.siscam;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class verificarasignacionruta extends AppCompatActivity {


    private EjecutarDatostask mAuthTask = null;
    obtenerverificarasignacionruta rutas;
    RutasConfirmacion rutasobj;
    private ListView list;
    private Button btnenviar;
    public Context context;
    RutaconfirmacionAdapter adapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificarasignacionruta);
        list = (ListView) findViewById(R.id.listarutas);
        btnenviar = (Button) findViewById(R.id.bntenviar);
        context = this;
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        abrirMostrardatos();
        agregaescuchas();
    }

    private void agregaescuchas() {
        btnenviar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<RutasConfirmacion, Boolean> checkedForruta = new HashMap<RutasConfirmacion, Boolean>();
                checkedForruta = adapter.obtenerchecados();
                procesarDatosCheckeados(checkedForruta);
            }


        });
    }


    private void procesarDatosCheckeados(HashMap<RutasConfirmacion, Boolean> checkedForruta) {
        RutasConfirmacion objtempruta;
        for (Map.Entry<RutasConfirmacion, Boolean> entry : checkedForruta.entrySet()) {
            objtempruta =  entry.getKey();
            System.out.println(objtempruta.CodRuta);
        }
    }

    private void abrirMostrardatos() {
        mAuthTask = null;
        mAuthTask = new EjecutarDatostask();
        mAuthTask.execute();
    }



    public class EjecutarDatostask extends AsyncTask<String, String, JSONObject> {


        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject jsonObject = new JSONObject();
            rutas = new obtenerverificarasignacionruta();
            jsonObject = rutas.obtenerRutas();
            return jsonObject;
        }

        @Override
        protected void onPostExecute(final JSONObject success) {
            JSONObject rutas = new JSONObject();
            RutasConfirmacion rutasobj;
            ArrayList<RutasConfirmacion> Listarutas = new ArrayList<>();


            try {
                JSONArray datos = success.getJSONArray("datos");
                for (int i = 0; i < datos.length(); i++) {
                    rutas = datos.getJSONObject(i);
                    rutasobj = new RutasConfirmacion(rutas.getString("CodRuta"), rutas.getString("Empresa"), rutas.getString("HoraIni"), rutas.getString("Ruta"), rutas.getString("Tipo"));
                    Listarutas.add(rutasobj);
                }

                adapter = new RutaconfirmacionAdapter(context, 0, Listarutas);
                list.setAdapter(adapter);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected void onCancelled() {

        }
    }

}
