package com.example.manueli.siscam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class verificarasignacionruta extends AppCompatActivity {

    JSONObject jsonObject = new JSONObject();
    private EjecutarDatostask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificarasignacionruta);

    }

    public class EjecutarDatostask extends AsyncTask<String,String, JSONObject>{
        ProgressDialog progress;
        verificarasignacionruta act;

        public EjecutarDatostask(ProgressDialog progress, verificarasignacionruta act) {
            this.progress = progress;
            this.act = act;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            progress.show();
            jsonObject = null;

            return jsonObject;
        }
        @Override
        protected void onPostExecute(final JSONObject success) {
            progress.dismiss();


            JSONArray comics= new JSONArray();
            try{

            }catch(Exception e){
                e.printStackTrace();
            }

        }

        @Override
        protected void onCancelled() {

        }
    }

}
