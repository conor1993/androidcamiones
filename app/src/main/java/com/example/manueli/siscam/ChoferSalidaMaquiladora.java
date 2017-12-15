package com.example.manueli.siscam;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


public class ChoferSalidaMaquiladora extends ActionBarActivity implements View.OnClickListener {

    Conexion Conexion;
    ConexionClientes ConexionClientes;
    PreparedStatement stmt;
    ResultSet rs;
    EditText edtCantidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chofer_salida_maquiladora);

        Conexion = new Conexion();
        Button  IdBoton = (Button) findViewById(R.id.IdBoton);

        IdBoton.setOnClickListener(this);
    }
    @Override
    public void onClick (View v)
    {
        if (v.getId()==R.id.IdBoton){


        }


    }

}
