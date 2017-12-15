package com.example.manueli.siscam;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ConfigEnvioDeDatos extends ActionBarActivity implements View.OnClickListener {

    Spinner spOpciones,spRutas;
    PreparedStatement stmt;
    ResultSet rs;
    ConexionClientes ConexionClientes;
    EditText EdtUnidad;
    TextView TvUnidad,TvRutas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_envio_de_datos);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        ConexionClientes = new ConexionClientes();
        spOpciones= (Spinner) findViewById(R.id.spOpciones);
        spRutas= (Spinner) findViewById(R.id.spRutas);
        final Button BtnBoton = (Button) findViewById(R.id.IdBoton);
        final TextView TvMensaje=(TextView) findViewById(R.id.TvMensaje);
        EdtUnidad =(EditText) findViewById(R.id.EdtUnidad);
        TvRutas=(TextView) findViewById(R.id.TvRutas);
        TvUnidad=(TextView) findViewById(R.id.TvUnidad);

        TvMensaje.setVisibility(View.INVISIBLE);
        BtnBoton.setVisibility(View.INVISIBLE);
        spRutas.setVisibility(View.INVISIBLE);
        BtnBoton.setOnClickListener(this);

        spOpciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int position,long id){



                String Seleccionado = spOpciones.getSelectedItem().toString();
                if (Seleccionado != "Seleccionar opcion..") {
                    try {
                        if (TvMensaje.getVisibility() != view.VISIBLE) {
                            TvMensaje.setVisibility(View.VISIBLE);
                            BtnBoton.setVisibility(View.VISIBLE);
                        }
                        if (Seleccionado =="Eliminar rutas.."){

                            TvMensaje.setText("Al precionar el boton eliminar, se eliminaran las rutas que no han tenido modificaciones.");
                            BtnBoton.setText("Eliminar");
                            spRutas.setVisibility(View.INVISIBLE);
                            TvRutas.setVisibility(View.INVISIBLE);
                            EdtUnidad.setVisibility(View.INVISIBLE);
                            TvUnidad.setVisibility(View.INVISIBLE);

                        }
                        if (Seleccionado =="Eliminar una ruta en especifica.."){
                            TvMensaje.setText("Debe seleccionar una ruta para despues precionar el boton eliminar para descartar esa ruta en caso de no ocuparse.");
                            BtnBoton.setText("Eliminar");
                            spRutas.setVisibility(View.VISIBLE);
                            TvRutas.setVisibility(View.VISIBLE);
                            EdtUnidad.setVisibility(View.INVISIBLE);
                            TvUnidad.setVisibility(View.INVISIBLE);
                        }
                        if (Seleccionado =="Agregar ruta.."){
                            TvMensaje.setText("Para agregar ruta debe seleccionarla y asignarle una unidad para despues precionar el boton guardar.");
                            BtnBoton.setText("Guardar");
                            spRutas.setVisibility(View.VISIBLE);
                            TvRutas.setVisibility(View.VISIBLE);
                            EdtUnidad.setVisibility(View.VISIBLE);
                            TvUnidad.setVisibility(View.VISIBLE);

                        }


                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(),
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    } finally {

                    }
                }else {
                    TvMensaje.setVisibility(View.INVISIBLE);
                    BtnBoton.setVisibility(View.INVISIBLE);
                    spRutas.setVisibility(View.INVISIBLE);
                    TvRutas.setVisibility(View.INVISIBLE);
                    EdtUnidad.setVisibility(View.INVISIBLE);
                    TvUnidad.setVisibility(View.INVISIBLE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }
        });

        ArrayList<String> Opciones = new ArrayList<String>();
        Opciones.add(0,"Seleccionar opcion..");
        Opciones.add("Eliminar rutas..");
        Opciones.add("Eliminar una ruta en especifica..");
        Opciones.add("Agregar ruta..");

        //String[] array2 = Opciones.toArray(new String[0]);
        ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(ConfigEnvioDeDatos.this, android.R.layout.simple_expandable_list_item_1, Opciones);

        spOpciones.setAdapter(NoCoreAdapter2);
        spOpciones.setSelection(((ArrayAdapter)spOpciones.getAdapter()).getPosition(Opciones));

        try {
            Connection con = ConexionClientes.CONNCli();
            if (con == null) {
                Toast.makeText(getApplicationContext(),
                        "Sin Conexion a Base de Datos",
                        Toast.LENGTH_LONG).show();
            } else {
                String query = "select Ruta from RutasGeneradas order by Ruta";
                stmt = con.prepareStatement(query);
                rs = stmt.executeQuery();
                ArrayList<String> data = new ArrayList<String>();
                data.add(0,"Seleccione Ruta..");
                while (rs.next()) {
                    String id = rs.getString("Ruta");
                    data.add(id);
                }
                String[] array = data.toArray(new String[0]);
                ArrayAdapter NoCoreAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
                spRutas.setAdapter(NoCoreAdapter);
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick (View v)
    {
        if (v.getId() == R.id.IdBoton) {

            try {
                String Seleccionado = spOpciones.getSelectedItem().toString();
                if (Seleccionado != "Seleccionar opcion..") {
                    if (Seleccionado =="Eliminar rutas.."){

                        Connection con = ConexionClientes.CONNCli();
                        if (con == null) {
                            Toast.makeText(getApplicationContext(),
                                    "Sin Conexion a Base de Datos",
                                    Toast.LENGTH_LONG).show();
                        }else{
                            String query = "delete from RutasGeneradas where Estatus=0";
                            Statement stmt = con.createStatement();
                            stmt.executeUpdate(query);
                        }
                    }
                    if (Seleccionado =="Eliminar una ruta en especifica.."){
                        Connection con = ConexionClientes.CONNCli();
                        if (con == null) {
                            Toast.makeText(getApplicationContext(),
                                    "Sin Conexion a Base de Datos",
                                    Toast.LENGTH_LONG).show();
                        }else{
                            String query = "delete from RutasGeneradas where Ruta='' and Unidad=''";
                            Statement stmt = con.createStatement();
                            stmt.executeUpdate(query);
                        }
                    }
                    if (Seleccionado =="Agregar ruta.."){
                        Connection con = ConexionClientes.CONNCli();
                        if (con == null) {
                            Toast.makeText(getApplicationContext(),
                                    "Sin Conexion a Base de Datos",
                                    Toast.LENGTH_LONG).show();
                        }else{
                            String query = "";
                            Statement stmt = con.createStatement();
                            stmt.executeUpdate(query);
                        }
                    }

                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "Error: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }

        }


    }

}
