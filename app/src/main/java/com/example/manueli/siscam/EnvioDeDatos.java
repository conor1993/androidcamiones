package com.example.manueli.siscam;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class EnvioDeDatos extends ActionBarActivity implements View.OnClickListener {

    Conexion Conexion;
    PreparedStatement stmt;
    ResultSet rs;
    ConexionClientes ConexionClientes;
    Spinner spEmpresa;
    EditText EdtEmpresa;
    int Empresa=Variables.Empresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envio_de_datos);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Conexion = new Conexion();
        ConexionClientes = new ConexionClientes();
        spEmpresa = (Spinner) findViewById(R.id.spEmpresa);
        EdtEmpresa = (EditText) findViewById(R.id.EdtEmpresa);

        Button  BtnObtener = (Button) findViewById(R.id.IdObtener);
        Button BtnEnviar = (Button) findViewById(R.id.IdEnviar);
        ImageButton imgButton = (ImageButton) findViewById(R.id.imgButtonConfig);
        ImageButton imgButtonEmpresa = (ImageButton) findViewById(R.id.imgButton);

        BtnObtener.setOnClickListener(this);
        BtnEnviar.setOnClickListener(this);
        imgButton.setOnClickListener(this);
        imgButtonEmpresa.setOnClickListener(this);

        spEmpresa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                String Seleccionado = spEmpresa.getSelectedItem().toString();
                if (Seleccionado != "Seleccionar Empresa.."){
                    try{
                        Connection con = Conexion.CONN();
                        if (con == null) {
                            Toast.makeText(getApplicationContext(),
                                    "Sin Conexion a Base de Datos",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            String query2 = "select Codigo,NomComercial,ConAndroid from CatEmpresasClientes where RazonSocial='"+ Seleccionado +"'";
                            //String query2 = "select Codigo,NomComercial from CatEmpresasClientes where RazonSocial='"+ Seleccionado +"'";
                            stmt = con.prepareStatement(query2);
                            rs = stmt.executeQuery();

                            if (rs.next()) {
                                Variables.Empresa = rs.getInt("Codigo");
                                Variables.ConexionCliente = rs.getString("ConAndroid");
                                EdtEmpresa.setText(rs.getString("NomComercial"));

                            }
                           else {
                                Variables.ConexionCliente="";
                                Toast.makeText(getApplicationContext(),
                                        "Ocurrio un problema, intente de nuevo",
                                        Toast.LENGTH_LONG).show();
                                EdtEmpresa.setText("");
                            }
                            if (Seleccionado == "Seleccionar Empresa.."){
                                Variables.Empresa = 0;
                                EdtEmpresa.setText("");
                            }

                        }
                    }catch (Exception e) {
                        Toast.makeText(getApplicationContext(),
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();

                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }
        });

        try{
            Connection con = Conexion.CONN();

            if (con == null) {
                Toast.makeText(getApplicationContext(),
                        "Sin Conexion a Base de Datos de Produccion",
                        Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),
                        "No tendra acceso a empresas, por lo tanto no podra realizar este proceso.",
                        Toast.LENGTH_LONG).show();
            } else {
                String query2 = "select RazonSocial from CatEmpresasClientes where Estatus=1 and  conexion <>'' order by RazonSocial";
                stmt = con.prepareStatement(query2);
                rs = stmt.executeQuery();
                ArrayList<String> data2 = new ArrayList<String>();
                data2.add(0,"Seleccionar Empresa..");

                while (rs.next()) {
                    String id2 = rs.getString("RazonSocial");
                    data2.add(id2);
                }

                String[] array3 = data2.toArray(new String[0]);
                ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data2);
                spEmpresa.setAdapter(NoCoreAdapter2);
            }
            if (Variables.Empresa > 0)
            {
                if (con == null) {
                    Toast.makeText(getApplicationContext(),
                            "Sin Conexion a Base de Datos",
                            Toast.LENGTH_LONG).show();
                } else {

                    String query2 = "select RazonSocial,NomComercial,ConAndroid from CatEmpresasClientes where Codigo="+ Empresa +"";
                    //String query2 = "select RazonSocial,NomComercial from CatEmpresasClientes where Codigo="+ Empresa +"";
                    stmt = con.prepareStatement(query2);
                    rs = stmt.executeQuery();
                    if (rs.next()) {
                        spEmpresa.setSelection(((ArrayAdapter) spEmpresa.getAdapter()).getPosition(rs.getString("Razonsocial") ));
                        EdtEmpresa.setText(rs.getString("NomComercial"));
                        Variables.ConexionCliente = rs.getString("ConAndroid");
                    }
                    else {
                        Variables.ConexionCliente = "";
                        Toast.makeText(getApplicationContext(),
                                "Seleccione la empresa",
                                Toast.LENGTH_LONG).show();
                    }

                }
            }

        }catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick (View v)
    {
        if (v.getId()==R.id.imgButton){
            try{
                Connection con = Conexion.CONN();
                if (con == null) {
                    Toast.makeText(getApplicationContext(),
                            "Sin Conexion a Base de Datos",
                            Toast.LENGTH_LONG).show();
                } else {
                    String query2 = "select Codigo,RazonSocial from CatEmpresasClientes where NomComercial='"+ EdtEmpresa.getText() +"'";
                    stmt = con.prepareStatement(query2);
                    rs = stmt.executeQuery();
                    if (rs.next()) {
                        Variables.Empresa = rs.getInt("Codigo");
                        spEmpresa.setSelection(((ArrayAdapter) spEmpresa.getAdapter()).getPosition(rs.getString("Razonsocial") ));
                    }
                    else {
                        Variables.ConexionCliente = "";
                        Toast.makeText(getApplicationContext(),
                                "No existe esta clave de empresa",
                                Toast.LENGTH_LONG).show();
                    }

                }
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "Error: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();

            }
        }
        if (Variables.Empresa > 0) {
        if (v.getId() == R.id.IdObtener) {

                try {
                    Toast.makeText(getApplicationContext(),
                            "Espere el mensaje de confirmacion",
                            Toast.LENGTH_LONG).show();
                    Connection con = Conexion.CONN();
                    if (con == null) {
                        Connection conn = ConexionClientes.CONNCli();
                        if (conn == null) {
                            Toast.makeText(getApplicationContext(),
                                    "Sin Conexion a Base de Datos",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            String query = "exec SpEnvioDeDatos 1";
                            Statement stmt = con.createStatement();
                            stmt.executeUpdate(query);

                        }
                    } else {
                        String query = "exec SpDatosProduccionMaquiladora 1,"+ Variables.Empresa +"";

                        Statement stmt = con.createStatement();
                        stmt.executeUpdate(query);

                    }
                    Toast.makeText(getApplicationContext(),
                            "Proceso finalizado",
                            Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

            }


        if (v.getId() == R.id.IdEnviar) {

                try {
                    Toast.makeText(getApplicationContext(),
                            "Espere el mensaje de confirmacion",
                            Toast.LENGTH_LONG).show();
                    int Bandera=0;
                    Connection conn = ConexionClientes.CONNCli();
                    if (conn != null){

                        String query = "exec SpEnvioDeDatos 2";
                        Statement stmt = conn.createStatement();
                        stmt.executeUpdate(query);

                    }
                    else{
                        Bandera = Bandera+1;
                    }

                    Connection con = Conexion.CONN();
                    if (con != null) {

                        String query = "exec SpDatosProduccionMaquiladora 2,"+ Variables.Empresa +"";
                        Statement stmt = con.createStatement();
                        stmt.executeUpdate(query);
                    }
                    else{
                        Bandera = Bandera+1;
                    }
                    if (Bandera==2) {
                        Toast.makeText(getApplicationContext(),
                                "Ocurrio un problema",
                                Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(),
                                "Proceso finalizado",
                                Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

        }
        }else{
            Toast.makeText(getApplicationContext(),
                    "Seleccione empresa..",
                    Toast.LENGTH_LONG).show();
        }

        if (v.getId()==R.id.imgButtonConfig){
            Intent in = new Intent (EnvioDeDatos.this, ConfigEnvioDeDatos.class);
            startActivity(in);

        }
    }

}
