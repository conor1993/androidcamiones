package com.example.manueli.siscam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

public class verificarasignacionruta extends AppCompatActivity {


    private EjecutarDatostask mAuthTask = null;
    obtenerverificarasignacionruta  rutas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificarasignacionruta);
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



           // JSONObject rutas = new JSONObject();
            //rutas = success;
            try{
               // Iterator<?> ruta  = success.keys();
               // while(ruta.hasNext()){
                    //String key = (String)ruta.next();
                   // rutas = success.getJSONObject(key);
                    System.out.println(success);
                //}

            }catch(Exception e){
                e.printStackTrace();
            }

        }


        @Override
        protected void onCancelled() {

        }
    }

}
