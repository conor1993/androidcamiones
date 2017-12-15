package com.example.manueli.siscam;

import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
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
import java.util.ArrayList;

import android.content.DialogInterface;
import java.sql.Statement;

public class Supervisor extends ActionBarActivity implements View.OnClickListener{

    ConexionClientes ConexionClientes;
    PreparedStatement stmt;
    ResultSet rs;
    String Valor;
    int ValidarCambio=0,CambioUnidad=0,Cambiar=0,UnidadSinRuta=0,InsertRuta=0;
    int Entrar=0;
    Spinner spRutas;
    Button Boton,btnCambiar;
    //EditText edtUnidad,edtUnidad2,edtPosicion,edtCantidad;
    EditText edtUnidad,edtPosicion,edtCantidad;
    TextView txtCantidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        ConexionClientes = new ConexionClientes();
        spRutas = (Spinner) findViewById(R.id.spRutas);
        edtUnidad  = (EditText) findViewById(R.id.edtUnidad);
        //edtUnidad2  = (EditText) findViewById(R.id.edtUnidad2);
        edtPosicion  = (EditText) findViewById(R.id.edtPosicion);
        edtCantidad  = (EditText) findViewById(R.id.edtCantidad);
        txtCantidad=(TextView) findViewById(R.id.txtCantidad);


        edtCantidad.setVisibility(View.INVISIBLE);
        txtCantidad.setVisibility(View.INVISIBLE);

        Boton = (Button) findViewById(R.id.IdBoton);
        btnCambiar = (Button) findViewById(R.id.btnCambiar);



        Boton.setOnClickListener(this);
        btnCambiar.setOnClickListener(this);

        spRutas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                String Seleccionado = spRutas.getSelectedItem().toString();

                if (Seleccionado != "Seleccionar Ruta.." ) {
                    try{
                        Connection con = ConexionClientes.CONNCli();
                        if (con == null) {
                            Toast.makeText(getApplicationContext(),
                                    "Sin Conexion a Base de Datos",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            if(UnidadSinRuta==1){
                                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Supervisor.this);
                                dialogo1.setTitle("Atencion..");
                                dialogo1.setMessage("¿Cambio de Unidad?");
                                dialogo1.setCancelable(false);
                                dialogo1.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogo1, int id) {
                                        //aceptar();
                                        try{
                                            Connection con = ConexionClientes.CONNCli();
                                            if (con == null) {
                                                Toast.makeText(getApplicationContext(),
                                                        "Sin Conexion a Base de Datos",
                                                        Toast.LENGTH_LONG).show();
                                            } else {
                                                String query2 = "select Unidad,case when Convert(varchar(10),Posicion)='0' then '' else Convert(varchar(10),Posicion) end as Posicion,Convert(varchar(1),Estatus) as Estatus,CodUnidad from RutasGeneradas where Ruta='"+ spRutas.getSelectedItem().toString() +"' and Estatus <> 2";
                                                stmt = con.prepareStatement(query2);
                                                rs = stmt.executeQuery();
                                                int Cantidad=0;
                                               // String UnidadEncontrada="";
                                                while (rs.next()) {
                                                   // UnidadEncontrada = rs.getString("Unidad");
                                                    if(ValidarCambio==1){
                                                        CambioUnidad=rs.getInt("CodUnidad");
                                                    }else{
                                                        edtUnidad.setText(rs.getString("Unidad"));
                                                    }
                                                    //edtUnidad.setText(rs.getString("Unidad"));
                                                    //edtUnidad2.setText("");
                                                    //edtPosicion.setText(rs.getString("Posicion"));
                                                    if (rs.getString("Posicion").toString().equals("")) {
                                                        edtPosicion.setText("");
                                                    }else{
                                                        if (rs.getString("Posicion").toString().equals("")) {
                                                            edtPosicion.setText("");
                                                        }else{
                                                            if(rs.getString("Posicion")=="0"){
                                                                edtPosicion.setText("");
                                                            }else{
                                                                edtPosicion.setText(rs.getString("Posicion"));
                                                            }
                                                        }
                                                    }
                                                    edtCantidad.setVisibility(View.INVISIBLE);
                                                   // Boton.setText("Llegó");
                                                    txtCantidad.setVisibility(View.INVISIBLE);
                                                    if (rs.getString("Estatus").toString().equals("0")) {
                                                        edtCantidad.setVisibility(View.INVISIBLE);
                                                        Boton.setText("Llegó");
                                                        txtCantidad.setVisibility(View.INVISIBLE);

                                                    }else {

                                                        edtCantidad.setVisibility(View.VISIBLE);
                                                        Boton.setText("Se Fue");
                                                        txtCantidad.setVisibility(View.VISIBLE);
                                                    }
                                                    Cantidad++;
                                                }

                                                if (Cantidad > 1){
                                                    //edtUnidad.setText("");
                                                    edtPosicion.setText("");
                                                    edtCantidad.setText("");
                                                    query2 = "select Unidad from RutasGeneradas where Ruta='" + spRutas.getSelectedItem().toString() + "' and Estatus <> 2";
                                                    stmt = con.prepareStatement(query2);
                                                    rs = stmt.executeQuery();
                                                    final CharSequence[] items = new String[Cantidad];
                                                    int i =0;
                                                    while (rs.next()) {
                                                        items[i] = rs.getString("Unidad");
                                                        i++;
                                                    }
                                                    AlertDialog.Builder dialog = new AlertDialog.Builder(Supervisor.this);
                                                    final ArrayList seletedItems = new ArrayList();
                                                    dialog.setTitle("Seleccione unidad");
                                                    dialog.setMultiChoiceItems(items, null,

                                                            new DialogInterface.OnMultiChoiceClickListener() {

                                                                @Override
                                                                public void onClick(DialogInterface dialog, int indexSelected,
                                                                                    boolean isChecked) {
                                                                    if (isChecked) {
                                                                        seletedItems.add(items[indexSelected]);
                                                                        Valor =  seletedItems.toString();
                                                                    } else if (seletedItems.contains(items[indexSelected])) {
                                                                        seletedItems.remove(items[indexSelected]);
                                                                        Valor =  seletedItems.toString();
                                                                    }
                                                                }
                                                            })

                                                            .setNegativeButton("Cancelar  ", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    edtUnidad.setText("");
                                                                    edtUnidad.setText("");
                                                                    edtPosicion.setText("");
                                                                    edtCantidad.setVisibility(View.INVISIBLE);
                                                                    edtCantidad.setText("");
                                                                    txtCantidad.setVisibility(View.INVISIBLE);
                                                                    spRutas.setSelection(((ArrayAdapter) spRutas.getAdapter()).getPosition("Seleccionar Ruta.."));
                                                                }
                                                            })
                                                            .setPositiveButton("   Seleccionar", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    try{
                                                                        Connection con = ConexionClientes.CONNCli();
                                                                        if (con == null) {
                                                                            Toast.makeText(getApplicationContext(),
                                                                                    "Sin Conexion a Base de Datos",
                                                                                    Toast.LENGTH_LONG).show();
                                                                        } else {
                                                                            String query = "select Unidad,case when Convert(varchar(10),Posicion)='0' then '' else Convert(varchar(10),Posicion) end as Posicion,Convert(varchar(1),Estatus) as Estatus,CodUnidad from RutasGeneradas where Ruta='"+ spRutas.getSelectedItem().toString() +"' and Unidad=SUBSTRING (SUBSTRING('" + Valor +"', 2, 500), 1, Len(SUBSTRING('" + Valor + "', 2, 500)) - 1 ) and Estatus <> 2" ;
                                                                            stmt = con.prepareStatement(query);
                                                                            rs = stmt.executeQuery();
                                                                            while (rs.next()) {
                                                                                if(ValidarCambio==1){
                                                                                    CambioUnidad=rs.getInt("CodUnidad");
                                                                                }else{
                                                                                    edtUnidad.setText(rs.getString("Unidad"));
                                                                                }
                                                                                //edtUnidad2.setText("");

                                                                                if (rs.getString("Posicion").toString().equals("")) {
                                                                                    edtPosicion.setText("");
                                                                                }else{
                                                                                    if(rs.getString("Posicion")=="0"){
                                                                                        edtPosicion.setText("");
                                                                                    }else{
                                                                                        edtPosicion.setText(rs.getString("Posicion"));
                                                                                    }
                                                                                }
                                                                                if (rs.getString("Estatus").toString().equals("0")) {
                                                                                    edtCantidad.setVisibility(View.INVISIBLE);
                                                                                    Boton.setText("Llegó");
                                                                                    txtCantidad.setVisibility(View.INVISIBLE);

                                                                                }else {

                                                                                    edtCantidad.setVisibility(View.VISIBLE);
                                                                                    Boton.setText("Se Fue");
                                                                                    txtCantidad.setVisibility(View.VISIBLE);
                                                                                }
                                                                            }
                                                                        }
                                                                    } catch (Exception e) {
                                                                        Toast.makeText(getApplicationContext(),
                                                                                "Error: " + e.getMessage(),
                                                                                Toast.LENGTH_LONG).show();
                                                                        if(ValidarCambio==0){

                                                                            edtUnidad.setText("");
                                                                            edtPosicion.setText("");
                                                                            edtCantidad.setText("");
                                                                            spRutas.setSelection(((ArrayAdapter) spRutas.getAdapter()).getPosition("Seleccionar Ruta.."));
                                                                        }

                                                                    }
                                                                }
                                                            });
                                                    dialog.show();
                                                }
                                                if (edtCantidad.getVisibility() == View.INVISIBLE){
                                                    edtPosicion.requestFocus();
                                                }else{
                                                    edtCantidad.requestFocus();
                                                }
                                            }

                                            Boton.setText("Guardar");
                                        }catch (Exception e) {
                                            Toast.makeText(getApplicationContext(),
                                                    "Error: " + e.getMessage(),
                                                    Toast.LENGTH_LONG).show();
                                            edtUnidad.setText("");
                                            edtUnidad.setText("");
                                            edtPosicion.setText("");
                                            edtCantidad.setText("");
                                            spRutas.setSelection(((ArrayAdapter) spRutas.getAdapter()).getPosition("Seleccionar Ruta.."));
                                            edtUnidad.requestFocus();
                                        }

                                    }
                                });
                                dialogo1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogo1, int id) {
                                        //cancelar();
                                        ValidarCambio=1;
                                        CambioUnidad =0;
                                        UnidadSinRuta=0;
                                        InsertRuta=1;
                                        edtPosicion.setText("");
                                        edtCantidad.setVisibility(View.INVISIBLE);
                                        edtCantidad.setText("");
                                        txtCantidad.setVisibility(View.INVISIBLE);
                                        Boton.setText("Guardar");
                                        //spRutas.setSelection(((ArrayAdapter)spRutas.getAdapter()).getPosition("Seleccionar Ruta.."));
                                        edtUnidad.requestFocus();
                                    }
                                });

                                dialogo1.show();
                            }else{
                                if(Cambiar==0){
                                    String query2 = "select Unidad,case when Convert(varchar(10),Posicion)='0' then '' else Convert(varchar(10),Posicion) end as Posicion,Convert(varchar(1),Estatus) as Estatus from RutasGeneradas where Ruta='"+ Seleccionado +"' and Estatus <> 2";
                                    stmt = con.prepareStatement(query2);
                                    rs = stmt.executeQuery();
                                    int Cantidad=0;
                                    String UnidadEncontrada="";
                                    while (rs.next()) {
                                        UnidadEncontrada = rs.getString("Unidad");
                                        //edtUnidad.setText(rs.getString("Unidad"));
                                        //edtUnidad2.setText("");
                                        //edtPosicion.setText(rs.getString("Posicion"));
                                        if (rs.getString("Posicion").toString().equals("")) {
                                            edtPosicion.setText("");
                                        }else{
                                            if(rs.getString("Posicion")=="0"){
                                                edtPosicion.setText("");
                                            }else{
                                                edtPosicion.setText(rs.getString("Posicion"));
                                            }
                                        }
                                        edtCantidad.setVisibility(View.INVISIBLE);
                                        // Boton.setText("Llegó");
                                        txtCantidad.setVisibility(View.INVISIBLE);
                                        if (rs.getString("Estatus").toString().equals("0")) {
                                            edtCantidad.setVisibility(View.INVISIBLE);
                                            Boton.setText("Llegó");
                                            txtCantidad.setVisibility(View.INVISIBLE);

                                        }else {

                                            edtCantidad.setVisibility(View.VISIBLE);
                                            Boton.setText("Se Fue");
                                            txtCantidad.setVisibility(View.VISIBLE);
                                        }
                                        Cantidad++;
                                    }

                                    if (Cantidad > 1){
                                        //edtUnidad.setText("");
                                        edtPosicion.setText("");
                                        edtCantidad.setText("");
                                        query2 = "select Unidad from RutasGeneradas where Ruta='" + Seleccionado + "' and Estatus <> 2";
                                        stmt = con.prepareStatement(query2);
                                        rs = stmt.executeQuery();
                                        final CharSequence[] items = new String[Cantidad];
                                        int i =0;
                                        while (rs.next()) {
                                            items[i] = rs.getString("Unidad");
                                            i++;
                                        }
                                        AlertDialog.Builder dialog = new AlertDialog.Builder(Supervisor.this);
                                        final ArrayList seletedItems = new ArrayList();
                                        dialog.setTitle("Seleccione unidad");
                                        dialog.setMultiChoiceItems(items, null,

                                                new DialogInterface.OnMultiChoiceClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialog, int indexSelected,
                                                                        boolean isChecked) {
                                                        if (isChecked) {
                                                            seletedItems.add(items[indexSelected]);
                                                            Valor =  seletedItems.toString();
                                                        } else if (seletedItems.contains(items[indexSelected])) {
                                                            seletedItems.remove(items[indexSelected]);
                                                            Valor =  seletedItems.toString();
                                                        }
                                                    }
                                                })

                                                .setNegativeButton("Cancelar  ", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        edtUnidad.setText("");
                                                        edtUnidad.setText("");
                                                        edtPosicion.setText("");
                                                        edtCantidad.setVisibility(View.INVISIBLE);
                                                        edtCantidad.setText("");
                                                        txtCantidad.setVisibility(View.INVISIBLE);
                                                        spRutas.setSelection(((ArrayAdapter) spRutas.getAdapter()).getPosition("Seleccionar Ruta.."));
                                                    }
                                                })
                                                .setPositiveButton("   Seleccionar", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        try{
                                                            Connection con = ConexionClientes.CONNCli();
                                                            if (con == null) {
                                                                Toast.makeText(getApplicationContext(),
                                                                        "Sin Conexion a Base de Datos",
                                                                        Toast.LENGTH_LONG).show();
                                                            } else {
                                                                String query = "select Unidad,case when Convert(varchar(10),Posicion)='0' then '' else Convert(varchar(10),Posicion) end as Posicion,Estatus,CodUnidad from RutasGeneradas where Ruta='"+ spRutas.getSelectedItem().toString() +"' and Unidad=SUBSTRING (SUBSTRING('" + Valor +"', 2, 500), 1, Len(SUBSTRING('" + Valor + "', 2, 500)) - 1 ) and Estatus <> 2" ;
                                                                stmt = con.prepareStatement(query);
                                                                rs = stmt.executeQuery();
                                                                while (rs.next()) {
                                                                    if(ValidarCambio==1){
                                                                        CambioUnidad=rs.getInt("CodUnidad");
                                                                    }else{
                                                                        edtUnidad.setText(rs.getString("Unidad"));
                                                                    }
                                                                    //edtUnidad2.setText("");

                                                                    if (rs.getString("Posicion").toString().equals("")) {
                                                                        edtPosicion.setText("");
                                                                    }else{
                                                                        if(rs.getString("Posicion")=="0"){
                                                                            edtPosicion.setText("");
                                                                        }else{
                                                                            edtPosicion.setText(rs.getString("Posicion"));
                                                                        }
                                                                    }
                                                                    if(rs.getString("Estatus").toString().equals("1")){
                                                                        edtCantidad.setVisibility(View.VISIBLE);
                                                                        Boton.setText("Se Fue");
                                                                        txtCantidad.setVisibility(View.VISIBLE);
                                                                    }else{
                                                                        Boton.setText("Llegó");
                                                                    }
                                                                    if (edtCantidad.getVisibility() == View.INVISIBLE){
                                                                        edtPosicion.requestFocus();
                                                                    }else{
                                                                        edtCantidad.requestFocus();
                                                                    }
                                                                }
                                                            }
                                                        } catch (Exception e) {
                                                            Toast.makeText(getApplicationContext(),
                                                                    "Error: " + e.getMessage(),
                                                                    Toast.LENGTH_LONG).show();
                                                            if(ValidarCambio==0){

                                                                edtUnidad.setText("");
                                                                edtPosicion.setText("");
                                                                edtCantidad.setText("");
                                                                spRutas.setSelection(((ArrayAdapter) spRutas.getAdapter()).getPosition("Seleccionar Ruta.."));
                                                            }

                                                        }
                                                    }
                                                });
                                        dialog.show();
                                    }else {
                                        //UnidadEncontrada = rs.getString("Unidad");
                                        edtUnidad.setText(UnidadEncontrada);
                                    }
                                }

                            }




                        }
                    }catch (Exception e) {
                        Toast.makeText(getApplicationContext(),
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                        edtUnidad.setText("");
                        InsertRuta=0;
                        edtUnidad.setText("");
                        edtPosicion.setText("");
                        edtCantidad.setText("");
                        spRutas.setSelection(((ArrayAdapter) spRutas.getAdapter()).getPosition("Seleccionar Ruta.."));
                    }
                    finally {
                        if (edtCantidad.getVisibility() == View.INVISIBLE){
                            edtPosicion.requestFocus();
                        }else{
                            edtCantidad.requestFocus();
                        }
                    }
                }
                Cambiar=0;
                Entrar = 0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }
        });




        try{
            Connection con = ConexionClientes.CONNCli();
            if (con == null) {
                Toast.makeText(getApplicationContext(),
                        "Sin Conexion a Base de Datos",
                        Toast.LENGTH_LONG).show();
            } else {
                String query2 = "select distinct Ruta from RutasGeneradas where Convert(date,Fecha)=Convert(date,getdate()) and Estatus <> 2";
                stmt = con.prepareStatement(query2);
                rs = stmt.executeQuery();
                ArrayList<String> data2 = new ArrayList<String>();
                data2.add(0,"Seleccionar Ruta..");

                while (rs.next()) {
                    String id2 = rs.getString("Ruta");
                    data2.add(id2);
                }

                String[] array3 = data2.toArray(new String[0]);
                ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data2);
                spRutas.setAdapter(NoCoreAdapter2);

            }
        }catch (Exception e) {

            Toast.makeText(getApplicationContext(),
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        edtUnidad.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {

                if ((keyevent.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    try {
                        if (edtUnidad.getText().toString() != "") {
                            if(edtPosicion.getText().toString() !="") {
                                Connection con = ConexionClientes.CONNCli();
                                if (con == null) {
                                    Toast.makeText(getApplicationContext(),
                                            "Sin Conexion a Base de Datos",
                                            Toast.LENGTH_LONG).show();
                                } else {

                    /*
                    String query = "select codigo,Unidad from Unidades where Unidad='"+ edtUnidad2.getText().toString() +"'";
                    stmt = con.prepareStatement(query);
                    rs = stmt.executeQuery();

                    if (rs.next()) {
                        query = "update rutasGeneradas set CodUnidad="+ rs.getString("codigo") +",Unidad='"+ rs.getString("Unidad") +"' where Unidad='"+ edtUnidad.getText().toString() +"' and Ruta='"+ spRutas.getSelectedItem().toString() +"'";
                        Statement stmt = con.createStatement();
                        stmt.executeUpdate(query);
                        //edtUnidad.setText(edtUnidad2.getText());
                        //edtUnidad2.setText("");
                        edtPosicion.requestFocus();
                    }else{
                        Toast.makeText(getApplicationContext(),
                                "La unidad no existe",
                                Toast.LENGTH_LONG).show();
                        //edtUnidad2.setText("");
                        //edtUnidad2.requestFocus();
                    }
                    */
                                    Cambiar=1;
                                    UnidadSinRuta=0;
                                    String query = "select * from RutasGeneradas where Unidad='"+ edtUnidad.getText().toString() +"' and Estatus <> 2";

                                    stmt = con.prepareStatement(query);
                                    rs = stmt.executeQuery();
                                    if (rs.next()) {
                                        spRutas.setSelection(((ArrayAdapter) spRutas.getAdapter()).getPosition(rs.getString("Ruta") ));
                                        if (rs.getString("Estatus").toString().equals("0")) {

                                            Boton.setText("Llegó");

                                        }else {
                                            if (rs.getString("Posicion").toString().equals("")) {
                                                edtPosicion.setText("");
                                            }else{
                                                if(rs.getString("Posicion").toString().equals("0")){
                                                    edtPosicion.setText("");
                                                }else{
                                                    edtPosicion.setText(rs.getString("Posicion"));
                                                }
                                            }
                                            edtCantidad.setVisibility(View.VISIBLE);
                                            Boton.setText("Se Fue");
                                            txtCantidad.setVisibility(View.VISIBLE);
                                        }
                                    }else{
                                        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Supervisor.this);
                                        dialogo1.setTitle("Atencion..");
                                        dialogo1.setMessage("La unidad no tiene ruta asignada");
                                        dialogo1.setMessage("¿Desea Crear o Modificar?");
                                        dialogo1.setCancelable(false);
                                        dialogo1.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialogo1, int id) {
                                                //aceptar();
                                                UnidadSinRuta=1;
                                                ValidarCambio=1;
                                                spRutas.setSelection(((ArrayAdapter)spRutas.getAdapter()).getPosition("Seleccionar Ruta.."));
                                                edtPosicion.setText("");
                                                edtCantidad.setVisibility(View.INVISIBLE);
                                                edtCantidad.setText("");
                                                txtCantidad.setVisibility(View.INVISIBLE);
                                                Boton.setText("Llegó");
                                                txtCantidad.requestFocus();
                                                InputMethodManager inputMethodManager1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

                                                inputMethodManager1.hideSoftInputFromWindow(edtUnidad.getWindowToken(), 0);

                                            }
                                        });
                                        dialogo1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialogo1, int id) {
                                                //cancelar();
                                                ValidarCambio=0;
                                                CambioUnidad =0;
                                                InsertRuta=0;
                                                Entrar = 0;
                                                UnidadSinRuta=0;
                                                edtUnidad.setText("");
                                                edtPosicion.setText("");
                                                edtCantidad.setVisibility(View.INVISIBLE);
                                                edtCantidad.setText("");
                                                txtCantidad.setVisibility(View.INVISIBLE);
                                                Boton.setText("Llegó");
                                                spRutas.setSelection(((ArrayAdapter)spRutas.getAdapter()).getPosition("Seleccionar Ruta.."));
                                                edtUnidad.requestFocus();
                                            }
                                        });

                                        dialogo1.show();

                                    }


                                }
                            }else{
                                Toast.makeText(getApplicationContext(),
                                        "Ingrese posicion / ubicacion de unidad",
                                        Toast.LENGTH_LONG).show();
                            }

                        }

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(),
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                        ValidarCambio=0;
                        CambioUnidad =0;
                        InsertRuta=0;
                        Entrar = 0;
                        UnidadSinRuta=0;
                        edtUnidad.setText("");
                        edtPosicion.setText("");
                        edtCantidad.setVisibility(View.INVISIBLE);
                        edtCantidad.setText("");
                        txtCantidad.setVisibility(View.INVISIBLE);
                        Boton.setText("Llegó");
                        spRutas.setSelection(((ArrayAdapter)spRutas.getAdapter()).getPosition("Seleccionar Ruta.."));
                        edtPosicion.requestFocus();
                    }finally {
                        //edtUnidad2.setText("");
                        //edtUnidad.setText("");
                        //edtPosicion.setText("");
                        //edtCantidad.setText("");
                        //Boton.setText("Llegó");
                        //spRutas.setSelection(((ArrayAdapter)spRutas.getAdapter()).getPosition("Seleccionar Ruta.."));

                        if (edtCantidad.getVisibility() == View.INVISIBLE){
                            edtPosicion.requestFocus();
                        }else{
                            edtCantidad.requestFocus();
                        }
                    }
                }
                return false;
            }
        });

        edtPosicion.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {

                if ((keyevent.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (edtPosicion.getText().toString() != ""){
                        try {
                            if (spRutas.getSelectedItem().toString().equals("Seleccionar Ruta..")) {
                            //}else if (edtCantidad.getVisibility() == View.INVISIBLE){
                              //  txtCantidad.requestFocus();
                            }else {
                                Connection con = ConexionClientes.CONNCli();
                                if (con == null) {
                                    Toast.makeText(getApplicationContext(),
                                            "Sin Conexion a Base de Datos",
                                            Toast.LENGTH_LONG).show();
                                }else{
                                    if (Boton.getText().toString().equals("Se Fue")) {
                                        String query = "update rutasGeneradas set Estatus=2,Cantidad='"+ edtCantidad.getText().toString() +"' where Unidad='" + edtUnidad.getText().toString() + "' and Ruta='" + spRutas.getSelectedItem().toString() + "'";
                                        Statement stmt = con.createStatement();
                                        stmt.executeUpdate(query);
                                    } else {
                                        String query = "";
                                        if(ValidarCambio==1) {
                                            if (InsertRuta ==1){
                                                if(edtPosicion.getText().toString().equals("")){
                                                    query = "declare @tabla table(CodRuta int,Ruta varchar(max),CodUnidad int,Unidad varchar(20),CodChofer int,Chofer varchar(max),HoraInicio varchar(50),HoraFinal varchar(50),Tipo bit,Posicion int,Estatus tinyint,Fecha datetime,Historial bit,Cantidad int,Empresa varchar(max)) " +
                                                            "insert into @tabla select top 1 * from RutasGeneradas where Ruta='"+ spRutas.getSelectedItem().toString() +"' "+
                                                            "update @tabla set Posicion=0,Cantidad=0,Estatus=0,CodUnidad=(select Codigo from Unidades where Unidad='"+ edtUnidad.getText().toString() +"'),Unidad='"+ edtUnidad.getText().toString() +"' "+
                                                            "insert into RutasGeneradas select * from @tabla";
                                                }else{
                                                    query = "declare @tabla table(CodRuta int,Ruta varchar(max),CodUnidad int,Unidad varchar(20),CodChofer int,Chofer varchar(max),HoraInicio varchar(50),HoraFinal varchar(50),Tipo bit,Posicion int,Estatus tinyint,Fecha datetime,Historial bit,Cantidad int,Empresa varchar(max)) " +
                                                            "insert into @tabla select top 1 * from RutasGeneradas where Ruta='"+ spRutas.getSelectedItem().toString() +"' "+
                                                            "update @tabla set Posicion="+ edtPosicion.getText().toString() +",Cantidad=0,Estatus=1,CodUnidad=(select Codigo from Unidades where Unidad='"+ edtUnidad.getText().toString() +"'),Unidad='"+ edtUnidad.getText().toString() +"' "+
                                                            "insert into RutasGeneradas select * from @tabla";
                                                }
                                            }else{
                                                if(edtPosicion.getText().toString().equals("")){
                                                    query = "declare @CodUnidad int=(select Codigo from Unidades where Unidad='" + edtUnidad.getText().toString() + "') "+
                                                            "update rutasGeneradas set CodUnidad=@CodUnidad,Unidad='"+ edtUnidad.getText().toString() +"' where CodUnidad='" + CambioUnidad + "' and Ruta='" + spRutas.getSelectedItem().toString() + "'";

                                                }else{
                                                    query = "declare @CodUnidad int=(select Codigo from Unidades where Unidad='" + edtUnidad.getText().toString() + "') "+
                                                            "update rutasGeneradas set CodUnidad=@CodUnidad,Unidad='"+ edtUnidad.getText().toString() +"',Estatus=1,Posicion='"+ edtPosicion.getText().toString() +"' where CodUnidad='" + CambioUnidad + "' and Ruta='" + spRutas.getSelectedItem().toString() + "'";
                                                }
                                            }
                                        }else{
                                            query = "update rutasGeneradas set Estatus=1,Posicion='"+ edtPosicion.getText().toString() +"' where Unidad='" + edtUnidad.getText().toString() + "' and Ruta='" + spRutas.getSelectedItem().toString() + "'";
                                        }
                                        Statement stmt = con.createStatement();
                                        stmt.executeUpdate(query);
                                    }
                                }
                            }
                        }catch (Exception e) {
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }finally {
                            ValidarCambio=0;
                            CambioUnidad =0;
                            InsertRuta=0;
                            Entrar = 0;
                            UnidadSinRuta=0;
                            edtUnidad.setText("");
                            edtPosicion.setText("");
                            edtCantidad.setVisibility(View.INVISIBLE);
                            edtCantidad.setText("");
                            txtCantidad.setVisibility(View.INVISIBLE);
                            Boton.setText("Llegó");
                            spRutas.setSelection(((ArrayAdapter)spRutas.getAdapter()).getPosition("Seleccionar Ruta.."));

                            InputMethodManager inputMethodManager1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

                            inputMethodManager1.hideSoftInputFromWindow(edtPosicion.getWindowToken(), 0);


                            edtUnidad.requestFocus();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),
                                "Ingrese posicion / ubicacion de unidad",
                                Toast.LENGTH_LONG).show();
                    }
                }
                return false;
            }
        });

        edtCantidad.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {

                if ((keyevent.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (edtCantidad.getText().toString() != ""){
                        try {
                            if (spRutas.getSelectedItem().toString().equals("Seleccionar Ruta..")) {
                                //}else if (edtCantidad.getVisibility() == View.INVISIBLE){
                                //  txtCantidad.requestFocus();
                            }else {
                                Connection con = ConexionClientes.CONNCli();
                                if (con == null) {
                                    Toast.makeText(getApplicationContext(),
                                            "Sin Conexion a Base de Datos",
                                            Toast.LENGTH_LONG).show();
                                }else{
                                    if (Boton.getText().toString().equals("Se Fue")) {
                                        String query = "update rutasGeneradas set Estatus=2,Cantidad='"+ edtCantidad.getText().toString() +"' where Unidad='" + edtUnidad.getText().toString() + "' and Ruta='" + spRutas.getSelectedItem().toString() + "'";
                                        Statement stmt = con.createStatement();
                                        stmt.executeUpdate(query);
                                    } else {
                                        String query = "";
                                        if(ValidarCambio==1) {
                                            if (InsertRuta ==1){
                                                if(edtPosicion.getText().toString().equals("")){
                                                    query = "declare @tabla table(CodRuta int,Ruta varchar(max),CodUnidad int,Unidad varchar(20),CodChofer int,Chofer varchar(max),HoraInicio varchar(50),HoraFinal varchar(50),Tipo bit,Posicion int,Estatus tinyint,Fecha datetime,Historial bit,Cantidad int,Empresa varchar(max)) " +
                                                            "insert into @tabla select top 1 * from RutasGeneradas where Ruta='"+ spRutas.getSelectedItem().toString() +"' "+
                                                            "update @tabla set Posicion=0,Cantidad=0,Estatus=0,CodUnidad=(select Codigo from Unidades where Unidad='"+ edtUnidad.getText().toString() +"'),Unidad='"+ edtUnidad.getText().toString() +"' "+
                                                            "insert into RutasGeneradas select * from @tabla";
                                                }else{
                                                    query = "declare @tabla table(CodRuta int,Ruta varchar(max),CodUnidad int,Unidad varchar(20),CodChofer int,Chofer varchar(max),HoraInicio varchar(50),HoraFinal varchar(50),Tipo bit,Posicion int,Estatus tinyint,Fecha datetime,Historial bit,Cantidad int,Empresa varchar(max)) " +
                                                            "insert into @tabla select top 1 * from RutasGeneradas where Ruta='"+ spRutas.getSelectedItem().toString() +"' "+
                                                            "update @tabla set Posicion="+ edtPosicion.getText().toString() +",Cantidad=0,Estatus=1,CodUnidad=(select Codigo from Unidades where Unidad='"+ edtUnidad.getText().toString() +"'),Unidad='"+ edtUnidad.getText().toString() +"' "+
                                                            "insert into RutasGeneradas select * from @tabla";
                                                }
                                            }else{
                                                if(edtPosicion.getText().toString().equals("")){
                                                    query = "declare @CodUnidad int=(select Codigo from Unidades where Unidad='" + edtUnidad.getText().toString() + "') "+
                                                            "update rutasGeneradas set CodUnidad=@CodUnidad,Unidad='"+ edtUnidad.getText().toString() +"' where CodUnidad='" + CambioUnidad + "' and Ruta='" + spRutas.getSelectedItem().toString() + "'";

                                                }else{
                                                    query = "declare @CodUnidad int=(select Codigo from Unidades where Unidad='" + edtUnidad.getText().toString() + "') "+
                                                            "update rutasGeneradas set CodUnidad=@CodUnidad,Unidad='"+ edtUnidad.getText().toString() +"',Estatus=1,Posicion='"+ edtPosicion.getText().toString() +"' where CodUnidad='" + CambioUnidad + "' and Ruta='" + spRutas.getSelectedItem().toString() + "'";
                                                }
                                            }
                                        }else{
                                            query = "update rutasGeneradas set Estatus=1,Posicion='"+ edtPosicion.getText().toString() +"' where Unidad='" + edtUnidad.getText().toString() + "' and Ruta='" + spRutas.getSelectedItem().toString() + "'";
                                        }
                                        Statement stmt = con.createStatement();
                                        stmt.executeUpdate(query);
                                    }
                                }
                            }
                        }catch (Exception e) {
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }finally {
                            ValidarCambio=0;
                            CambioUnidad =0;
                            InsertRuta=0;
                            Entrar = 0;
                            UnidadSinRuta=0;
                            edtUnidad.setText("");
                            edtPosicion.setText("");
                            edtCantidad.setVisibility(View.INVISIBLE);
                            edtCantidad.setText("");
                            txtCantidad.setVisibility(View.INVISIBLE);
                            Boton.setText("Llegó");
                            spRutas.setSelection(((ArrayAdapter)spRutas.getAdapter()).getPosition("Seleccionar Ruta.."));

                            InputMethodManager inputMethodManager1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

                            inputMethodManager1.hideSoftInputFromWindow(edtCantidad.getWindowToken(), 0);

                            edtUnidad.requestFocus();
                        }
                    }

                }
                return false;
            }
        });

    }


    @Override
    public void onClick (View v)
    {

        if (v.getId() == R.id.btnCambiar) {
            try {
                if (edtUnidad.getText().toString() != "") {
                    if(edtPosicion.getText().toString() !="") {
                        Connection con = ConexionClientes.CONNCli();
                        if (con == null) {
                            Toast.makeText(getApplicationContext(),
                                    "Sin Conexion a Base de Datos",
                                    Toast.LENGTH_LONG).show();
                        } else {

                    /*
                    String query = "select codigo,Unidad from Unidades where Unidad='"+ edtUnidad2.getText().toString() +"'";
                    stmt = con.prepareStatement(query);
                    rs = stmt.executeQuery();

                    if (rs.next()) {
                        query = "update rutasGeneradas set CodUnidad="+ rs.getString("codigo") +",Unidad='"+ rs.getString("Unidad") +"' where Unidad='"+ edtUnidad.getText().toString() +"' and Ruta='"+ spRutas.getSelectedItem().toString() +"'";
                        Statement stmt = con.createStatement();
                        stmt.executeUpdate(query);
                        //edtUnidad.setText(edtUnidad2.getText());
                        //edtUnidad2.setText("");
                        edtPosicion.requestFocus();
                    }else{
                        Toast.makeText(getApplicationContext(),
                                "La unidad no existe",
                                Toast.LENGTH_LONG).show();
                        //edtUnidad2.setText("");
                        //edtUnidad2.requestFocus();
                    }
                    */
                            Cambiar=1;
                            UnidadSinRuta=0;
                            String query = "select * from RutasGeneradas where Unidad='"+ edtUnidad.getText().toString() +"' and Estatus <> 2";

                            stmt = con.prepareStatement(query);
                            rs = stmt.executeQuery();
                            if (rs.next()) {
                                spRutas.setSelection(((ArrayAdapter) spRutas.getAdapter()).getPosition(rs.getString("Ruta") ));
                                if (rs.getString("Estatus").toString().equals("0")) {

                                    Boton.setText("Llegó");

                                }else {
                                    if (rs.getString("Posicion").toString().equals("")) {
                                        edtPosicion.setText("");
                                    }else{
                                        if(rs.getString("Posicion").toString().equals("0")){
                                            edtPosicion.setText("");
                                        }else{
                                            edtPosicion.setText(rs.getString("Posicion"));
                                        }
                                    }
                                    edtCantidad.setVisibility(View.VISIBLE);
                                    Boton.setText("Se Fue");
                                    txtCantidad.setVisibility(View.VISIBLE);
                                }
                            }else{
                               /* Toast.makeText(getApplicationContext(),
                                        "La unidad no tiene ruta asignada.",
                                        Toast.LENGTH_LONG).show();
                                UnidadSinRuta=1;
                                ValidarCambio=1;
                                spRutas.setSelection(((ArrayAdapter)spRutas.getAdapter()).getPosition("Seleccionar Ruta.."));
                                edtPosicion.setText("");
                                edtCantidad.setVisibility(View.INVISIBLE);
                                edtCantidad.setText("");
                                txtCantidad.setVisibility(View.INVISIBLE);
                                Boton.setText("Llegó");
                                txtCantidad.requestFocus();
                                InputMethodManager inputMethodManager1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

                                inputMethodManager1.hideSoftInputFromWindow(edtUnidad.getWindowToken(), 0);
                                */
                                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Supervisor.this);
                                dialogo1.setTitle("Atencion..");
                                dialogo1.setMessage("La unidad no tiene ruta asignada");
                                dialogo1.setMessage("¿Desea Crear o Modificar?");
                                dialogo1.setCancelable(false);
                                dialogo1.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogo1, int id) {
                                        //aceptar();
                                        UnidadSinRuta=1;
                                        ValidarCambio=1;
                                        spRutas.setSelection(((ArrayAdapter)spRutas.getAdapter()).getPosition("Seleccionar Ruta.."));
                                        edtPosicion.setText("");
                                        edtCantidad.setVisibility(View.INVISIBLE);
                                        edtCantidad.setText("");
                                        txtCantidad.setVisibility(View.INVISIBLE);
                                        Boton.setText("Llegó");
                                        txtCantidad.requestFocus();
                                        InputMethodManager inputMethodManager1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

                                        inputMethodManager1.hideSoftInputFromWindow(edtUnidad.getWindowToken(), 0);

                                    }
                                });
                                dialogo1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogo1, int id) {
                                        //cancelar();
                                        ValidarCambio=0;
                                        CambioUnidad =0;
                                        InsertRuta=0;
                                        Entrar = 0;
                                        UnidadSinRuta=0;
                                        edtUnidad.setText("");
                                        edtPosicion.setText("");
                                        edtCantidad.setVisibility(View.INVISIBLE);
                                        edtCantidad.setText("");
                                        txtCantidad.setVisibility(View.INVISIBLE);
                                        Boton.setText("Llegó");
                                        spRutas.setSelection(((ArrayAdapter)spRutas.getAdapter()).getPosition("Seleccionar Ruta.."));
                                        edtUnidad.requestFocus();
                                    }
                                });

                                dialogo1.show();
                            }


                        }
                    }else{
                        Toast.makeText(getApplicationContext(),
                                "Ingrese posicion / ubicacion de unidad",
                                Toast.LENGTH_LONG).show();
                    }

                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "Error: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
                ValidarCambio=0;
                CambioUnidad =0;
                InsertRuta=0;
                Entrar = 0;
                UnidadSinRuta=0;
                edtUnidad.setText("");
                edtPosicion.setText("");
                edtCantidad.setVisibility(View.INVISIBLE);
                edtCantidad.setText("");
                txtCantidad.setVisibility(View.INVISIBLE);
                Boton.setText("Llegó");
                spRutas.setSelection(((ArrayAdapter)spRutas.getAdapter()).getPosition("Seleccionar Ruta.."));
                edtPosicion.requestFocus();
            }finally {
                //edtUnidad2.setText("");
                //edtUnidad.setText("");
                //edtPosicion.setText("");
                //edtCantidad.setText("");
                //Boton.setText("Llegó");
                //spRutas.setSelection(((ArrayAdapter)spRutas.getAdapter()).getPosition("Seleccionar Ruta.."));

                if (edtCantidad.getVisibility() == View.INVISIBLE){
                    edtPosicion.requestFocus();
                }else{
                    edtCantidad.requestFocus();
                }
            }
        }

        if (v.getId() == R.id.IdBoton) {
            try {
                if (spRutas.getSelectedItem().toString().equals("Seleccionar Ruta..")) {

                }else {

                    Connection con = ConexionClientes.CONNCli();

                    if (con == null) {
                        Toast.makeText(getApplicationContext(),
                                "Sin Conexion a Base de Datos",
                                Toast.LENGTH_LONG).show();
                    }else{
                        if (Boton.getText().toString().equals("Se Fue")) {
                            //checar si la ruta solo tiene una unidad, si existen 2 unidades que seleccione por que unidad cambiar

                            String query = "update rutasGeneradas set Estatus=2,Cantidad='"+ edtCantidad.getText().toString() +"' where Unidad='" + edtUnidad.getText().toString() + "' and Ruta='" + spRutas.getSelectedItem().toString() + "'";
                            Statement stmt = con.createStatement();
                            stmt.executeUpdate(query);
                        } else {
                            String query = "";
                            if(ValidarCambio==1) {
                                if (InsertRuta ==1){
                                    if(edtPosicion.getText().toString().equals("")){
                                        query = "declare @tabla table(CodRuta int,Ruta varchar(max),CodUnidad int,Unidad varchar(20),CodChofer int,Chofer varchar(max),HoraInicio varchar(50),HoraFinal varchar(50),Tipo bit,Posicion int,Estatus tinyint,Fecha datetime,Historial bit,Cantidad int,Empresa varchar(max)) " +
                                        "insert into @tabla select top 1 * from RutasGeneradas where Ruta='"+ spRutas.getSelectedItem().toString() +"' "+
                                        "update @tabla set Posicion=0,Cantidad=0,Estatus=0,CodUnidad=(select Codigo from Unidades where Unidad='"+ edtUnidad.getText().toString() +"'),Unidad='"+ edtUnidad.getText().toString() +"' "+
                                        "insert into RutasGeneradas select * from @tabla";
                                    }else{
                                        query = "declare @tabla table(CodRuta int,Ruta varchar(max),CodUnidad int,Unidad varchar(20),CodChofer int,Chofer varchar(max),HoraInicio varchar(50),HoraFinal varchar(50),Tipo bit,Posicion int,Estatus tinyint,Fecha datetime,Historial bit,Cantidad int,Empresa varchar(max)) " +
                                                "insert into @tabla select top 1 * from RutasGeneradas where Ruta='"+ spRutas.getSelectedItem().toString() +"' "+
                                                "update @tabla set Posicion="+ edtPosicion.getText().toString() +",Cantidad=0,Estatus=1,CodUnidad=(select Codigo from Unidades where Unidad='"+ edtUnidad.getText().toString() +"'),Unidad='"+ edtUnidad.getText().toString() +"' "+
                                                "insert into RutasGeneradas select * from @tabla";
                                    }
                                }else{
                                    if(edtPosicion.getText().toString().equals("")){
                                        query = "declare @CodUnidad int=(select Codigo from Unidades where Unidad='" + edtUnidad.getText().toString() + "') "+
                                                "update rutasGeneradas set CodUnidad=@CodUnidad,Unidad='"+ edtUnidad.getText().toString() +"' where CodUnidad='" + CambioUnidad + "' and Ruta='" + spRutas.getSelectedItem().toString() + "'";

                                    }else{
                                        query = "declare @CodUnidad int=(select Codigo from Unidades where Unidad='" + edtUnidad.getText().toString() + "') "+
                                                "update rutasGeneradas set CodUnidad=@CodUnidad,Unidad='"+ edtUnidad.getText().toString() +"',Estatus=1,Posicion='"+ edtPosicion.getText().toString() +"' where CodUnidad='" + CambioUnidad + "' and Ruta='" + spRutas.getSelectedItem().toString() + "'";
                                    }
                                }


                            }else{
                                query = "update rutasGeneradas set Estatus=1,Posicion='"+ edtPosicion.getText().toString() +"' where Unidad='" + edtUnidad.getText().toString() + "' and Ruta='" + spRutas.getSelectedItem().toString() + "'";

                            }


                            Statement stmt = con.createStatement();
                            stmt.executeUpdate(query);

                        }
                    }
                }
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "Error: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }finally {
                //edtUnidad2.setText("");
                ValidarCambio=0;
                CambioUnidad =0;
                InsertRuta=0;
                Entrar = 0;
                UnidadSinRuta=0;
                edtUnidad.setText("");
                edtPosicion.setText("");
                edtCantidad.setVisibility(View.INVISIBLE);
                edtCantidad.setText("");
                txtCantidad.setVisibility(View.INVISIBLE);
                Boton.setText("Llegó");
                spRutas.setSelection(((ArrayAdapter)spRutas.getAdapter()).getPosition("Seleccionar Ruta.."));
                edtUnidad.requestFocus();
            }
        }
    }

}
