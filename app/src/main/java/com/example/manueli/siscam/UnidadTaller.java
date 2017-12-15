package com.example.manueli.siscam;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import google.zxing.integration.android.IntentIntegrator;
import google.zxing.integration.android.IntentResult;

public class UnidadTaller extends ActionBarActivity implements View.OnClickListener{

    Spinner spServicios,spMecanicos,spProveedor,spOrigen;
    Conexion Conexion;
    PreparedStatement stmt;
    ResultSet rs;
    String Mecanico,TextChofer,TextPlacas;
    String Valor;
    EditText edtUnidad,edtKilometros,edtCosto, edtObservacionServicio,edtObservacionProveedor;
    TextView TvUnidad,TvPlacas, TvChofer;
    Button Boton;
    ImageButton imgButtonUnidad;
    int CodUnidad,Scan,CodChofer,CodServicio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unidad_taller);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);




        Conexion = new Conexion();
        spServicios = (Spinner) findViewById(R.id.spServicios);
        spMecanicos = (Spinner) findViewById(R.id.spMecanicos);
        spProveedor = (Spinner) findViewById(R.id.spProveedor);
        spOrigen = (Spinner) findViewById(R.id.spOrigen);
        Boton = (Button) findViewById(R.id.IdButton);

        edtUnidad  = (EditText) findViewById(R.id.edtUnidad);
        edtKilometros  = (EditText) findViewById(R.id.edtKilometros);
        edtCosto = (EditText) findViewById(R.id.edtCosto);
        edtObservacionServicio = (EditText) findViewById(R.id.edtObservacion);
        edtObservacionProveedor = (EditText) findViewById(R.id.edtObservacionP);

        TvPlacas = (TextView) findViewById(R.id.TvPlacas2);
        TvChofer = (TextView) findViewById(R.id.TvChofer);
        TvUnidad = (TextView) findViewById(R.id.TvUnidad);
        imgButtonUnidad = (ImageButton) findViewById(R.id.imgButtonUnidad);

        spServicios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int position,long id){
                String Seleccionado = spServicios.getSelectedItem().toString();
                if (Seleccionado != "Seleccionar Mantenimiento.."){
                    try{
                        Connection con = Conexion.CONN();
                        if (con == null) {
                            Toast.makeText(getApplicationContext(),
                                    "Sin Conexion a Base de Datos",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            String query2 = "select distinct P.Apodo as nombre from CatServPersonal SP inner join CatServicios S on SP.CodServicio=S.Codigo " +
                                    "inner join CatPersonal P on SP.CodPersonal=P.CodPersonal where S.Descripcion='"+ Seleccionado +"' and (P.Tipo='M' or P.Tipo='CM' or P.Tipo='AM')";
                            stmt = con.prepareStatement(query2);
                            rs = stmt.executeQuery();
                            ArrayList<String> data2 = new ArrayList<String>();
                            data2.add(0,"Seleccionar Mecanico..");
                            while (rs.next()) {
                                String id2 = rs.getString("Nombre");
                                data2.add(id2);
                            }

                            String[] array2 = data2.toArray(new String[0]);
                            ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(UnidadTaller.this, android.R.layout.simple_expandable_list_item_1, data2);
                            // ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data2);
                            spMecanicos.setAdapter(NoCoreAdapter2);
                            spMecanicos.setSelection(((ArrayAdapter)spMecanicos.getAdapter()).getPosition(Mecanico));

                        }
                    }catch (Exception e) {
                        //Log.e("ERRO", e.getMessage());
                        Toast.makeText(getApplicationContext(),
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    //spMecanicos.setSelection((spMecanicos.getAdapter()).getItem("Seleccionar Mecanico.."));

                    ArrayList<String> data2 = new ArrayList<String>();
                    data2.add(0,"Seleccionar Mecanico..");
                    String[] array2 = data2.toArray(new String[0]);
                    ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(UnidadTaller.this, android.R.layout.simple_expandable_list_item_1, data2);
                    // ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data2);
                    spMecanicos.setAdapter(NoCoreAdapter2);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }
        });

        try {
            Connection con = Conexion.CONN();
            if (con == null) {
                Toast.makeText(getApplicationContext(),
                        "Sin Conexion a Base de Datos",
                        Toast.LENGTH_LONG).show();
            } else {
                String query = "select   Descripcion  from CatServicios where tipo=0 order by Descripcion";
                stmt = con.prepareStatement(query);
                rs = stmt.executeQuery();
                ArrayList<String> data = new ArrayList<String>();
                data.add(0,"Seleccionar Servicio..");
                while (rs.next()) {
                    String id = rs.getString("Descripcion");
                    data.add(id);
                }
                String[] array = data.toArray(new String[0]);
                ArrayAdapter NoCoreAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
                spServicios.setAdapter(NoCoreAdapter);

                /*
                String query2 = "select distinct Apodo as Nombre from CatPersonal P inner join CatServPersonal "+
                        "SP on SP.CodPersonal=P.CodPersonal inner join CatServicios S on S.Codigo=SP.CodServicio where P.Tipo='M' and S.Tipo=0 order by P.Apodo ";
                stmt = con.prepareStatement(query2);
                rs = stmt.executeQuery();
                ArrayList<String> data2 = new ArrayList<String>();
                data2.add(0,"Seleccionar Mecanico..");
                while (rs.next()) {
                    String id2 = rs.getString("Nombre");
                    data2.add(id2);
                }
                String[] array2 = data2.toArray(new String[0]);
                ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data2);
                spMecanicos.setAdapter(NoCoreAdapter2);
                */
                String query3 = "select NombreCorto  from Proveedores_Cat ";
                stmt = con.prepareStatement(query3);
                rs = stmt.executeQuery();
                ArrayList<String> data3 = new ArrayList<String>();
                data3.add(0,"Seleccionar Proveedor..");
                while (rs.next()) {
                    String id3 = rs.getString("NombreCorto");
                    data3.add(id3);
                }
                String[] array3 = data3.toArray(new String[0]);
                ArrayAdapter NoCoreAdapter3 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data3);
                spProveedor.setAdapter(NoCoreAdapter3);

                String query4 = "select * from CatOrigenRepTaller";
                stmt = con.prepareStatement(query4);
                rs = stmt.executeQuery();
                ArrayList<String> data4 = new ArrayList<String>();
                data4.add(0,"Seleccionar Origen..");
                while (rs.next()) {
                    String id4 = rs.getString("Descripcion");
                    data4.add(id4);
                }
                String[] array4 = data4.toArray(new String[0]);

                ArrayAdapter NoCoreAdapter4 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data4);
                spOrigen.setAdapter(NoCoreAdapter4);
            }
        } catch (Exception e) {
            //Log.e("ERRO", e.getMessage());
            Toast.makeText(getApplicationContext(),
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Boton.setOnClickListener(OnClick);
        imgButtonUnidad.setOnClickListener(this);


        spMecanicos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String Seleccionado = spMecanicos.getSelectedItem().toString();
                if (Seleccionado != "Seleccionar Mecanico..") {
                    spProveedor.setSelection(((ArrayAdapter)spProveedor.getAdapter()).getPosition("Seleccionar Proveedor.."));
                    edtCosto.setText("");
                    edtObservacionProveedor.setText("");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spProveedor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String Seleccionado = spProveedor.getSelectedItem().toString();
                //Pasar valor a codigoservicio dependiendo de que servicio tome
                if (Seleccionado != "Seleccionar Proveedor..") {
                    spMecanicos.setSelection(((ArrayAdapter)spMecanicos.getAdapter()).getPosition("Seleccionar Mecanico.."));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
       edtKilometros.setOnKeyListener(new View.OnKeyListener() {
           public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
               if ((keyevent.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_ENTER)) {


                   InputMethodManager inputMethodManager1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

                   inputMethodManager1.hideSoftInputFromWindow(edtKilometros.getWindowToken(), 0);

                   return true;
               }
               return false;
           }
       });
        edtCosto.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                if ((keyevent.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_ENTER)) {


                    InputMethodManager inputMethodManager1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputMethodManager1.hideSoftInputFromWindow(edtCosto.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });
        edtObservacionProveedor.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                if ((keyevent.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_ENTER)) {


                    InputMethodManager inputMethodManager1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputMethodManager1.hideSoftInputFromWindow(edtObservacionProveedor.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });
        edtObservacionServicio.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                if ((keyevent.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_ENTER)) {


                    InputMethodManager inputMethodManager1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputMethodManager1.hideSoftInputFromWindow(edtObservacionServicio.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });
        //Enter...
        edtUnidad.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {

                if ((keyevent.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    String Unidad = edtUnidad.getText().toString();

                    try {

                        Connection con = Conexion.CONN();
                        if (con == null) {
                            Toast.makeText(getApplicationContext(),
                                    "Sin Conexion a Base de Datos",
                                    Toast.LENGTH_LONG).show();
                        } else {

                            CodServicio =0;
                            //1
                            String query ="if exists(select CME.clave as Unidad,CME.Placas,P.CodPersonal,P.Apodo as Chofer from MqCatMaqyEquipo CME inner join MqUnidadEspera UE on  CME.Codigo=UE.CodUnidad  " +
                                    "inner join CatPersonal P on P.CodPersonal=UE.CodChofer  where  CME.CodigoBarras='"+ Unidad +"' and (UE.Tipo=1 or UE.Estatus=0 or Convert(date,FechaEntrada)<=Convert(date,Getdate()))  and (P.Tipo='C' or P.Tipo='CM' or P.Tipo='AC')) " +
                                    " begin " +
                                    // " select CME.clave as Unidad,CME.Placas,P.CodPersonal,P.Apodo as Chofer from MqCatMaqyEquipo CME inner join MqUnidadEspera UE on  CME.Codigo=UE.CodUnidad " +
                                    //" inner join CatPersonal P on P.CodPersonal=UE.CodChofer  where  CME.CodigoBarras='"+ Unidad +"' and (P.Tipo='C' or P.Tipo='CM' or P.Tipo='AC')" +
                                    "if  exists(select * from MqAsignacionServicios MAS inner join MqCatMaqyEquipo U on MAS.CodMaquinaria=U.Codigo " +
                                    "where U.CodigoBarras='"+ Unidad +"' and Convert(date,MAS.FechaSalida)=Convert(date,'1999-01-01')) " +
                                    "begin " +
                                    "select '' unidad,'' placas,'' codpersonal,'' chofer " +
                                    "end " +
                                    "else " +
                                    "begin " +
                                    "select CME.clave as Unidad,CME.Placas,P.CodPersonal,P.Apodo as Chofer from MqCatMaqyEquipo CME inner join MqUnidadEspera UE on  CME.Codigo=UE.CodUnidad " +
                                    "inner join CatPersonal P on P.CodPersonal=UE.CodChofer  where  CME.CodigoBarras='"+ Unidad +"' and (P.Tipo='C' or P.Tipo='CM' or P.Tipo='AC') " +
                                    "end " +
                                    " end" +
                                    " else"+
                                    " begin"+
                                    " if exists(select * from MqAsignacionServicios MAS inner join MqCatMaqyEquipo U on MAS.CodMaquinaria=U.Codigo "+
                                    " where U.CodigoBarras='"+ Unidad +"' and Convert(date,MAS.FechaSalida)=Convert(date,'1999-01-01'))"+
                                    " begin"+
                                    " select '' unidad,'' placas,'' codpersonal,'' chofer  "+
                                    " end"+
                                    " else"+
                                    " begin"+
                                    " if exists(select * from MqAsignacionServicios MAS inner join MqCatMaqyEquipo U on MAS.CodMaquinaria=U.Codigo"+
                                    " where U.CodigoBarras='"+ Unidad +"' and Convert(date,MAS.FechaSalida)=Convert(date,getdate()))"+
                                    " begin"+
                                    " Select U.Clave as unidad,U.Placas,P.CodPersonal,P.Apodo as Chofer from MqAsignacionServicios MAS inner join MqCatMaqyEquipo U on MAS.CodMaquinaria=U.Codigo"+
                                    " inner join CatPersonal P on P.CodPersonal=MAS.CodChofer where U.CodigoBarras='"+ Unidad +"' and (P.Tipo='C' or P.Tipo='CM' or P.Tipo='AC') and Convert(date,MAS.FechaSalida)=Convert(date,getdate())"+
                                    " end"+
                                    " else"+
                                    " begin"+
                                    " select '' unidad,'' placas,'' codpersonal,'' chofer "+
                                    " end"+
                                    " end"+
                                    " end";
                            Statement stmt = con.createStatement();
                            ResultSet rs = stmt.executeQuery(query);

                            if (rs.next()) {
                                try{
                                    TvUnidad.setText("Unidad: "+ rs.getString("Unidad"));
                                    TvChofer.setText("Chofer: " + rs.getString("Chofer"));
                                    TvPlacas.setText("Placas: " + rs.getString("Placas"));
                                    CodChofer = rs.getInt("CodPersonal");
                                    spServicios.setSelection(((ArrayAdapter)spServicios.getAdapter()).getPosition("Seleccionar Servicio.."));
                                    edtObservacionServicio.setText("");
                                    spOrigen.setSelection(((ArrayAdapter)spOrigen.getAdapter()).getPosition("Seleccionar Origen.."));
                                    Mecanico = "Seleccionar Mecanico..";
                                    spProveedor.setSelection(((ArrayAdapter)spProveedor.getAdapter()).getPosition("Seleccionar Proveedor.."));
                                    edtCosto.setText("");
                                    edtKilometros.setText("");
                                    edtObservacionProveedor.setText("");
                                    Boton.setText("Entrada");
                                    edtKilometros.requestFocus();
                                }catch (Exception e){
                                    //2
                                    //String query2="select count(*) as total from MqAsignacionServicios MAS inner join MqCatMaqyEquipo U on U.Codigo=MAS.CodMaquinaria where " +
                                    //"MAS.FechaSalida='1999-01-01' and U.CodigoBarras='"+ Unidad +"'";
                                    String query2="select U.Clave as Unidad,U.Placas,P.CodPersonal,P.Apodo as Chofer,S.Descripcion from MqAsignacionServicios MAS inner join MqCatMaqyEquipo U on U.Codigo=MAS.CodMaquinaria "+
                                            "inner join CatPersonal P on P.CodPersonal=MAS.CodPersonal "+
                                            "inner join CatServicios S on S.Codigo=MAS.CodSevicio  "+
                                            "where MAS.FechaSalida='1999-01-01' and U.CodigoBarras='"+ Unidad +"' group by U.Clave,U.Placas,P.Apodo,P.CodPersonal,S.Descripcion";
                                    Statement stmt2 = con.createStatement();
                                    ResultSet rs2 = stmt2.executeQuery(query2);
                                    int Tamaño =0;

                                    while (rs2.next()) {

                                        TvUnidad.setText("Unidad: "+ rs2.getString("Unidad"));
                                        TvChofer.setText("Chofer: " + rs2.getString("Chofer"));
                                        TvPlacas.setText("Placas: " + rs2.getString("Placas"));
                                        CodChofer = rs2.getInt("CodPersonal");
                                        Tamaño++;
                                    }
                                    if (Tamaño > 0) {
                                        //3
                                        query = "select S.Descripcion from MqAsignacionServicios MAS inner join MqCatMaqyEquipo U on U.Codigo=MAS.CodMaquinaria "+
                                                "inner join CatServicios S on S.Codigo=MAS.CodSevicio "+
                                                "where MAS.FechaSalida='1999-01-01' and U.CodigoBarras='"+ Unidad +"' ";
                                        stmt = con.createStatement();
                                        rs = stmt.executeQuery(query);
                                        final CharSequence[] items = new String[Tamaño];
                                        int i =0;
                                        while (rs.next()) {
                                            items[i] = rs.getString("descripcion");
                                            i++;
                                        }
                                        AlertDialog.Builder dialog = new AlertDialog.Builder(UnidadTaller.this);
                                        final ArrayList seletedItems = new ArrayList();
                                        dialog.setTitle("Servicios en proceso");
                                        dialog.setMultiChoiceItems(items, null,
                                                new DialogInterface.OnMultiChoiceClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialog, int indexSelected,
                                                                        boolean isChecked) {
                                                        if (isChecked) {
                                                            seletedItems.add(items[indexSelected]);
                                                   /* String cadena = (String)items[indexSelected];
                                                    String a=seletedItems.toString();
                                                    Toast.makeText(getApplicationContext(),
                                                            a,
                                                            Toast.LENGTH_LONG).show();*/

                                                            Valor =  seletedItems.toString();
                                                        } else if (seletedItems.contains(items[indexSelected])) {
                                                            seletedItems.remove(items[indexSelected]);
                                                            Valor =  seletedItems.toString();

                                                        }
                                                    }
                                                })
                                                .setPositiveButton("   Salida", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int id) {

                                                        try{
                                                            Connection con = Conexion.CONN();
                                                            if (con == null) {
                                                                Toast.makeText(getApplicationContext(),
                                                                        "Sin Conexion a Base de Datos",
                                                                        Toast.LENGTH_LONG).show();
                                                            } else {

                                                                String query = "exec Sp_SalidaServiciosAndroid '"+ Valor +"','"+ edtUnidad.getText().toString() +"'" ;


                                                                Statement stmt = con.createStatement();
                                                                stmt.executeUpdate(query);
                                                                CodServicio=0;
                                                                edtUnidad.setText("");
                                                                spServicios.setSelection(((ArrayAdapter)spServicios.getAdapter()).getPosition("Seleccionar Servicio.."));
                                                                edtObservacionServicio.setText("");
                                                                spOrigen.setSelection(((ArrayAdapter)spOrigen.getAdapter()).getPosition("Seleccionar Origen.."));
                                                                //spMecanicos.setSelection(((ArrayAdapter)spMecanicos.getAdapter()).getPosition("Seleccionar Mecanico.."));
                                                                ArrayList<String> data2 = new ArrayList<String>();
                                                                data2.add(0,"Seleccionar Mecanico..");
                                                                String[] array2 = data2.toArray(new String[0]);
                                                                ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(UnidadTaller.this, android.R.layout.simple_expandable_list_item_1, data2);

                                                                spProveedor.setSelection(((ArrayAdapter)spProveedor.getAdapter()).getPosition("Seleccionar Proveedor.."));
                                                                TvUnidad.setText("Unidad: ");
                                                                TvChofer.setText("Chofer: ");
                                                                TvPlacas.setText("Placas: ");
                                                                edtCosto.setText("");
                                                                edtKilometros.setText("");
                                                                edtObservacionProveedor.setText("");
                                                                Boton.setText("Entrada");
                                                                CodChofer = 0;
                                                                edtUnidad.requestFocus();
                                                            }
                                                        } catch (Exception e) {
                                                            //Log.e("ERRO", e.getMessage());
                                                            Toast.makeText(getApplicationContext(),
                                                                    "Error: " + e.getMessage(),
                                                                    Toast.LENGTH_LONG).show();
                                                            edtUnidad.setText("");
                                                        }
                                                    }
                                                })
                                                .setNegativeButton("Nuevo  ", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        CodServicio = 0;
                                                        spServicios.setSelection(((ArrayAdapter) spServicios.getAdapter()).getPosition("Seleccionar Servicio.."));
                                                        edtObservacionServicio.setText("");
                                                        spOrigen.setSelection(((ArrayAdapter) spOrigen.getAdapter()).getPosition("Seleccionar Origen.."));
                                                        Mecanico = "Seleccionar Mecanico..";
                                                        spProveedor.setSelection(((ArrayAdapter) spProveedor.getAdapter()).getPosition("Seleccionar Proveedor.."));
                                                        edtCosto.setText("");
                                                        edtKilometros.setText("");
                                                        edtObservacionProveedor.setText("");
                                                        Boton.setText("Entrada");
                                                        edtKilometros.requestFocus();
                                                    }
                                                });
                                        dialog.show();
                                    }else{
                                        Toast.makeText(getApplicationContext(),
                                                "La unidad no se encuentra ni en taller ni en el cuadro, no podra realizar servicio.",
                                                Toast.LENGTH_LONG).show();
                                        edtUnidad.setText("");
                                    }
                                }
                            }
                            else {
                                //2
                                String query2="select U.Clave as Unidad,U.Placas,P.CodPersonal,P.Apodo as Chofer,S.Descripcion from MqAsignacionServicios MAS inner join MqCatMaqyEquipo U on U.Codigo=MAS.CodMaquinaria "+
                                        "left join CatPersonal P on P.CodPersonal=MAS.CodPersonal "+
                                        "inner join CatServicios S on S.Codigo=MAS.CodSevicio  "+
                                        "where MAS.FechaSalida='1999-01-01' and U.CodigoBarras='"+ Unidad +"' group by U.Clave,U.Placas,P.Apodo,P.CodPersonal,S.Descripcion";
                                Statement stmt2 = con.createStatement();
                                ResultSet rs2 = stmt2.executeQuery(query2);
                                int Tamaño =0;

                                while (rs2.next()) {


                                    TvUnidad.setText("Unidad: "+ rs2.getString("Unidad"));
                                    TvChofer.setText("Chofer: " + rs2.getString("Chofer"));
                                    TvPlacas.setText("Placas: " + rs2.getString("Placas"));
                                    CodChofer = rs2.getInt("CodPersonal");
                                    Tamaño++;
                                }
                                if (Tamaño > 0) {
                                    //3
                                    query = "select S.Descripcion from MqAsignacionServicios MAS inner join MqCatMaqyEquipo U on U.Codigo=MAS.CodMaquinaria "+
                                            "inner join CatServicios S on S.Codigo=MAS.CodSevicio "+
                                            "where MAS.FechaSalida='1999-01-01' and U.CodigoBarras='"+ Unidad +"' ";
                                    stmt = con.createStatement();
                                    rs = stmt.executeQuery(query);
                                    final CharSequence[] items = new String[Tamaño];
                                    int i =0;
                                    while (rs.next()) {
                                        items[i] = rs.getString("descripcion");
                                        i++;
                                    }
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(UnidadTaller.this);
                                    final ArrayList seletedItems = new ArrayList();
                                    dialog.setTitle("Servicios en proceso");
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
                                            .setPositiveButton("   Salida", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int id) {

                                                    try{
                                                        Connection con = Conexion.CONN();
                                                        if (con == null) {
                                                            Toast.makeText(getApplicationContext(),
                                                                    "Sin Conexion a Base de Datos",
                                                                    Toast.LENGTH_LONG).show();
                                                        } else {

                                                            String query = "exec Sp_SalidaServiciosAndroid '"+ Valor +"','"+ edtUnidad.getText().toString() +"'" ;


                                                            Statement stmt = con.createStatement();
                                                            stmt.executeUpdate(query);
                                                            CodServicio=0;
                                                            edtUnidad.setText("");
                                                            spServicios.setSelection(((ArrayAdapter)spServicios.getAdapter()).getPosition("Seleccionar Servicio.."));
                                                            edtObservacionServicio.setText("");
                                                            spOrigen.setSelection(((ArrayAdapter)spOrigen.getAdapter()).getPosition("Seleccionar Origen.."));
                                                            //spMecanicos.setSelection(((ArrayAdapter)spMecanicos.getAdapter()).getPosition("Seleccionar Mecanico.."));
                                                            ArrayList<String> data2 = new ArrayList<String>();
                                                            data2.add(0,"Seleccionar Mecanico..");
                                                            String[] array2 = data2.toArray(new String[0]);
                                                            ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(UnidadTaller.this, android.R.layout.simple_expandable_list_item_1, data2);

                                                            spProveedor.setSelection(((ArrayAdapter)spProveedor.getAdapter()).getPosition("Seleccionar Proveedor.."));
                                                            TvUnidad.setText("Unidad: ");
                                                            TvChofer.setText("Chofer: ");
                                                            TvPlacas.setText("Placas: ");
                                                            edtCosto.setText("");
                                                            edtKilometros.setText("");
                                                            edtObservacionProveedor.setText("");
                                                            Boton.setText("Entrada");
                                                            CodChofer = 0;
                                                            edtUnidad.requestFocus();
                                                        }

                                                    } catch (Exception e) {
                                                        //Log.e("ERRO", e.getMessage());
                                                        Toast.makeText(getApplicationContext(),
                                                                "Error: " + e.getMessage(),
                                                                Toast.LENGTH_LONG).show();
                                                        edtUnidad.setText("");
                                                    }
                                                }
                                            })
                                            .setNegativeButton("Nuevo  ", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int id) {
                                                    CodServicio = 0;
                                                    spServicios.setSelection(((ArrayAdapter) spServicios.getAdapter()).getPosition("Seleccionar Servicio.."));
                                                    edtObservacionServicio.setText("");
                                                    spOrigen.setSelection(((ArrayAdapter) spOrigen.getAdapter()).getPosition("Seleccionar Origen.."));
                                                    Mecanico = "Seleccionar Mecanico..";
                                                    spProveedor.setSelection(((ArrayAdapter) spProveedor.getAdapter()).getPosition("Seleccionar Proveedor.."));
                                                    edtCosto.setText("");
                                                    edtKilometros.setText("");
                                                    edtObservacionProveedor.setText("");
                                                    Boton.setText("Entrada");
                                                    edtKilometros.requestFocus();
                                                }
                                            });
                                    dialog.show();
                                }else{
                                    Toast.makeText(getApplicationContext(),
                                            "La unidad no se encuentra ni en taller ni en el cuadro, no podra realizar servicio.",
                                            Toast.LENGTH_LONG).show();
                                    edtUnidad.setText("");
                                }
                            }
                        }
                        InputMethodManager inputMethodManager1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

                        inputMethodManager1.hideSoftInputFromWindow(edtUnidad.getWindowToken(), 0);

                    } catch (Exception e) {
                        //Log.e("ERRO", e.getMessage());
                        Toast.makeText(getApplicationContext(),
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                    return true;
                }
                return false;
            }
        });
    }
    public void onClick(View v){
        //Se responde al evento click
        if(v.getId()==R.id.imgButtonUnidad){
            //Se instancia un objeto de la clase IntentIntegrator
            //IntentIntegrator scanIntegrator = new IntentIntegrator(this);

            IntentIntegrator scanIntegrator=new IntentIntegrator(this);
            //Se procede con el proceso de scaneo
            scanIntegrator.initiateScan();



        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //Se obtiene el resultado del proceso de scaneo y se parsea
        //IntentResult

        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);


        if (scanningResult != null) {
            //Quiere decir que se obtuvo resultado pro lo tanto:
            //Desplegamos en pantalla el contenido del código de barra scaneado
            String scanContent = scanningResult.getContents();
            edtUnidad.setText(scanContent);
            String Unidad = edtUnidad.getText().toString();
            try{
                Connection con = Conexion.CONN();
                if (con == null) {
                    Toast.makeText(getApplicationContext(),
                            "Sin Conexion a Base de Datos",
                            Toast.LENGTH_LONG).show();
                } else {

                    CodServicio =0;
                    //1
                    /*String query ="if exists(select CME.clave as Unidad,CME.Placas,P.CodPersonal,P.Apodo as Chofer from MqCatMaqyEquipo CME inner join MqUnidadEspera UE on  CME.Codigo=UE.CodUnidad  " +
                            "inner join CatPersonal P on P.CodPersonal=UE.CodChofer  where  CME.CodigoBarras='"+ Unidad +"' and (UE.Tipo=0 or Convert(date,FechaEntrada)=Convert(date,Getdate()))  and (P.Tipo='C' or P.Tipo='CM' or P.Tipo='AC')) " +
                            " begin" +
                            " select CME.clave as Unidad,CME.Placas,P.CodPersonal,P.Apodo as Chofer from MqCatMaqyEquipo CME inner join MqUnidadEspera UE on  CME.Codigo=UE.CodUnidad " +
                            " inner join CatPersonal P on P.CodPersonal=UE.CodChofer  where  CME.CodigoBarras='"+ Unidad +"' and (P.Tipo='C' or P.Tipo='CM' or P.Tipo='AC')" +
                            " end" +
                            " else"+
                            " begin"+
                            " if exists(select * from MqAsignacionServicios MAS inner join MqCatMaqyEquipo U on MAS.CodMaquinaria=U.Codigo "+
                            " where U.CodigoBarras='"+ Unidad +"' and Convert(date,MAS.FechaSalida)=Convert(date,'1999-01-01'))"+
                            " begin"+
                            " select '' unidad,'' placas,'' codpersonal,'' chofer  "+
                            " end"+
                            " else"+
                            " begin"+
                            " if exists(select * from MqAsignacionServicios MAS inner join MqCatMaqyEquipo U on MAS.CodMaquinaria=U.Codigo"+
                            " where U.CodigoBarras='"+ Unidad +"' and Convert(date,MAS.FechaSalida)=Convert(date,getdate()))"+
                            " begin"+
                            " Select U.Clave as unidad,U.Placas,P.CodPersonal,P.Apodo as Chofer from MqAsignacionServicios MAS inner join MqCatMaqyEquipo U on MAS.CodMaquinaria=U.Codigo"+
                            " inner join CatPersonal P on P.CodPersonal=MAS.CodChofer where U.CodigoBarras='"+ Unidad +"' and (P.Tipo='C' or P.Tipo='CM' or P.Tipo='AC') and Convert(date,MAS.FechaSalida)=Convert(date,getdate())"+
                            " end"+
                            " else"+
                            " begin"+
                            " select '' unidad,'' placas,'' codpersonal,'' chofer "+
                            " end"+
                            " end"+
                            " end";*/
                    String query ="if exists(select CME.clave as Unidad,CME.Placas,P.CodPersonal,P.Apodo as Chofer from MqCatMaqyEquipo CME inner join MqUnidadEspera UE on  CME.Codigo=UE.CodUnidad  " +
                            "inner join CatPersonal P on P.CodPersonal=UE.CodChofer  where  CME.CodigoBarras='"+ Unidad +"' and (UE.Tipo=1 or UE.Estatus=0 or Convert(date,FechaEntrada)<=Convert(date,Getdate()))  and (P.Tipo='C' or P.Tipo='CM' or P.Tipo='AC')) " +
                            " begin " +
                            // " select CME.clave as Unidad,CME.Placas,P.CodPersonal,P.Apodo as Chofer from MqCatMaqyEquipo CME inner join MqUnidadEspera UE on  CME.Codigo=UE.CodUnidad " +
                            //" inner join CatPersonal P on P.CodPersonal=UE.CodChofer  where  CME.CodigoBarras='"+ Unidad +"' and (P.Tipo='C' or P.Tipo='CM' or P.Tipo='AC')" +
                            "if  exists(select * from MqAsignacionServicios MAS inner join MqCatMaqyEquipo U on MAS.CodMaquinaria=U.Codigo " +
                            "where U.CodigoBarras='"+ Unidad +"' and Convert(date,MAS.FechaSalida)=Convert(date,'1999-01-01')) " +
                            "begin " +
                            "select '' unidad,'' placas,'' codpersonal,'' chofer " +
                            "end " +
                            "else " +
                            "begin " +
                            "select CME.clave as Unidad,CME.Placas,P.CodPersonal,P.Apodo as Chofer from MqCatMaqyEquipo CME inner join MqUnidadEspera UE on  CME.Codigo=UE.CodUnidad " +
                            "inner join CatPersonal P on P.CodPersonal=UE.CodChofer  where  CME.CodigoBarras='"+ Unidad +"' and (P.Tipo='C' or P.Tipo='CM' or P.Tipo='AC') " +
                            "end " +
                            " end" +
                            " else"+
                            " begin"+
                            " if exists(select * from MqAsignacionServicios MAS inner join MqCatMaqyEquipo U on MAS.CodMaquinaria=U.Codigo "+
                            " where U.CodigoBarras='"+ Unidad +"' and Convert(date,MAS.FechaSalida)=Convert(date,'1999-01-01'))"+
                            " begin"+
                            " select '' unidad,'' placas,'' codpersonal,'' chofer  "+
                            " end"+
                            " else"+
                            " begin"+
                            " if exists(select * from MqAsignacionServicios MAS inner join MqCatMaqyEquipo U on MAS.CodMaquinaria=U.Codigo"+
                            " where U.CodigoBarras='"+ Unidad +"' and Convert(date,MAS.FechaSalida)=Convert(date,getdate()))"+
                            " begin"+
                            " Select U.Clave as unidad,U.Placas,P.CodPersonal,P.Apodo as Chofer from MqAsignacionServicios MAS inner join MqCatMaqyEquipo U on MAS.CodMaquinaria=U.Codigo"+
                            " inner join CatPersonal P on P.CodPersonal=MAS.CodChofer where U.CodigoBarras='"+ Unidad +"' and (P.Tipo='C' or P.Tipo='CM' or P.Tipo='AC') and Convert(date,MAS.FechaSalida)=Convert(date,getdate())"+
                            " end"+
                            " else"+
                            " begin"+
                            " select '' unidad,'' placas,'' codpersonal,'' chofer "+
                            " end"+
                            " end"+
                            " end";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    if (rs.next()) {
                        try{
                            TvUnidad.setText("Unidad: "+ rs.getString("Unidad"));
                            TvChofer.setText("Chofer: " + rs.getString("Chofer"));
                            TvPlacas.setText("Placas: " + rs.getString("Placas"));
                            CodChofer = rs.getInt("CodPersonal");
                            spServicios.setSelection(((ArrayAdapter)spServicios.getAdapter()).getPosition("Seleccionar Servicio.."));
                            edtObservacionServicio.setText("");
                            spOrigen.setSelection(((ArrayAdapter)spOrigen.getAdapter()).getPosition("Seleccionar Origen.."));
                            Mecanico = "Seleccionar Mecanico..";
                            spProveedor.setSelection(((ArrayAdapter)spProveedor.getAdapter()).getPosition("Seleccionar Proveedor.."));
                            edtCosto.setText("");
                            edtKilometros.setText("");
                            edtObservacionProveedor.setText("");
                            Boton.setText("Entrada");
                            edtKilometros.requestFocus();
                        }catch (Exception e){
                            //2
                            //String query2="select count(*) as total from MqAsignacionServicios MAS inner join MqCatMaqyEquipo U on U.Codigo=MAS.CodMaquinaria where " +
                            //"MAS.FechaSalida='1999-01-01' and U.CodigoBarras='"+ Unidad +"'";
                            String query2="select U.Clave as Unidad,U.Placas,P.CodPersonal,P.Apodo as Chofer,S.Descripcion from MqAsignacionServicios MAS inner join MqCatMaqyEquipo U on U.Codigo=MAS.CodMaquinaria "+
                                    "inner join CatPersonal P on P.CodPersonal=MAS.CodPersonal "+
                                    "inner join CatServicios S on S.Codigo=MAS.CodSevicio  "+
                                    "where MAS.FechaSalida='1999-01-01' and U.CodigoBarras='"+ Unidad +"' group by U.Clave,U.Placas,P.Apodo,P.CodPersonal,S.Descripcion";
                            Statement stmt2 = con.createStatement();
                            ResultSet rs2 = stmt2.executeQuery(query2);
                            int Tamaño =0;

                            while (rs2.next()) {


                                TvUnidad.setText("Unidad: "+ rs2.getString("Unidad"));
                                TvChofer.setText("Chofer: " + rs2.getString("Chofer"));
                                TvPlacas.setText("Placas: " + rs2.getString("Placas"));
                                CodChofer = rs2.getInt("CodPersonal");
                                Tamaño++;
                            }
                            if (Tamaño > 0) {
                                //3
                                query = "select S.Descripcion from MqAsignacionServicios MAS inner join MqCatMaqyEquipo U on U.Codigo=MAS.CodMaquinaria "+
                                        "inner join CatServicios S on S.Codigo=MAS.CodSevicio "+
                                        "where MAS.FechaSalida='1999-01-01' and U.CodigoBarras='"+ Unidad +"' ";
                                stmt = con.createStatement();
                                rs = stmt.executeQuery(query);
                                final CharSequence[] items = new String[Tamaño];
                                int i =0;
                                while (rs.next()) {
                                    items[i] = rs.getString("descripcion");
                                    i++;
                                }
                                AlertDialog.Builder dialog = new AlertDialog.Builder(UnidadTaller.this);
                                final ArrayList seletedItems = new ArrayList();
                                dialog.setTitle("Servicios en proceso");
                                dialog.setMultiChoiceItems(items, null,
                                        new DialogInterface.OnMultiChoiceClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int indexSelected,
                                                                boolean isChecked) {
                                                if (isChecked) {
                                                    seletedItems.add(items[indexSelected]);
                                                   /* String cadena = (String)items[indexSelected];
                                                    String a=seletedItems.toString();
                                                    Toast.makeText(getApplicationContext(),
                                                            a,
                                                            Toast.LENGTH_LONG).show();*/

                                                    Valor =  seletedItems.toString();
                                                } else if (seletedItems.contains(items[indexSelected])) {
                                                    seletedItems.remove(items[indexSelected]);
                                                    Valor =  seletedItems.toString();

                                                }
                                            }
                                        })
                                        .setPositiveButton("   Salida", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {

                                                try{
                                                    Connection con = Conexion.CONN();
                                                    if (con == null) {
                                                        Toast.makeText(getApplicationContext(),
                                                                "Sin Conexion a Base de Datos",
                                                                Toast.LENGTH_LONG).show();
                                                    } else {

                                                        String query = "exec Sp_SalidaServiciosAndroid '"+ Valor +"','"+ edtUnidad.getText().toString() +"'" ;


                                                        Statement stmt = con.createStatement();
                                                        stmt.executeUpdate(query);
                                                        CodServicio=0;
                                                        edtUnidad.setText("");
                                                        spServicios.setSelection(((ArrayAdapter)spServicios.getAdapter()).getPosition("Seleccionar Servicio.."));
                                                        edtObservacionServicio.setText("");
                                                        spOrigen.setSelection(((ArrayAdapter)spOrigen.getAdapter()).getPosition("Seleccionar Origen.."));
                                                        //spMecanicos.setSelection(((ArrayAdapter)spMecanicos.getAdapter()).getPosition("Seleccionar Mecanico.."));
                                                        ArrayList<String> data2 = new ArrayList<String>();
                                                        data2.add(0,"Seleccionar Mecanico..");
                                                        String[] array2 = data2.toArray(new String[0]);
                                                        ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(UnidadTaller.this, android.R.layout.simple_expandable_list_item_1, data2);

                                                        spProveedor.setSelection(((ArrayAdapter)spProveedor.getAdapter()).getPosition("Seleccionar Proveedor.."));
                                                        TvUnidad.setText("Unidad: ");
                                                        TvChofer.setText("Chofer: ");
                                                        TvPlacas.setText("Placas: ");
                                                        edtCosto.setText("");
                                                        edtKilometros.setText("");
                                                        edtObservacionProveedor.setText("");
                                                        Boton.setText("Entrada");
                                                        CodChofer = 0;
                                                        edtUnidad.requestFocus();
                                                    }
                                                } catch (Exception e) {
                                                    //Log.e("ERRO", e.getMessage());
                                                    Toast.makeText(getApplicationContext(),
                                                            "Error: " + e.getMessage(),
                                                            Toast.LENGTH_LONG).show();
                                                    edtUnidad.setText("");
                                                }
                                            }
                                        })
                                        .setNegativeButton("Nuevo  ", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                                CodServicio = 0;
                                                spServicios.setSelection(((ArrayAdapter) spServicios.getAdapter()).getPosition("Seleccionar Servicio.."));
                                                edtObservacionServicio.setText("");
                                                spOrigen.setSelection(((ArrayAdapter) spOrigen.getAdapter()).getPosition("Seleccionar Origen.."));
                                                Mecanico = "Seleccionar Mecanico..";
                                                spProveedor.setSelection(((ArrayAdapter) spProveedor.getAdapter()).getPosition("Seleccionar Proveedor.."));
                                                edtCosto.setText("");
                                                edtKilometros.setText("");
                                                edtObservacionProveedor.setText("");
                                                Boton.setText("Entrada");
                                                edtKilometros.requestFocus();
                                            }
                                        });
                                dialog.show();
                            }else{
                                Toast.makeText(getApplicationContext(),
                                        "La unidad no se encuentra ni en taller ni en el cuadro, no podra realizar servicio.",
                                        Toast.LENGTH_LONG).show();
                                edtUnidad.setText("");
                            }
                        }
                    }
                    else {
                        //2
                        String query2="select U.Clave as Unidad,U.Placas,P.CodPersonal,P.Apodo as Chofer,S.Descripcion from MqAsignacionServicios MAS inner join MqCatMaqyEquipo U on U.Codigo=MAS.CodMaquinaria "+
                                "inner join CatPersonal P on P.CodPersonal=MAS.CodPersonal "+
                                "inner join CatServicios S on S.Codigo=MAS.CodSevicio  "+
                                "where MAS.FechaSalida='1999-01-01' and U.CodigoBarras='"+ Unidad +"' group by U.Clave,U.Placas,P.Apodo,P.CodPersonal,S.Descripcion";
                        Statement stmt2 = con.createStatement();
                        ResultSet rs2 = stmt2.executeQuery(query2);
                        int Tamaño =0;

                        while (rs2.next()) {


                            TvUnidad.setText("Unidad: "+ rs2.getString("Unidad"));
                            TvChofer.setText("Chofer: " + rs2.getString("Chofer"));
                            TvPlacas.setText("Placas: " + rs2.getString("Placas"));
                            CodChofer = rs2.getInt("CodPersonal");
                            Tamaño++;
                        }
                        if (Tamaño > 0) {
                            //3
                            query = "select S.Descripcion from MqAsignacionServicios MAS inner join MqCatMaqyEquipo U on U.Codigo=MAS.CodMaquinaria "+
                                    "inner join CatServicios S on S.Codigo=MAS.CodSevicio "+
                                    "where MAS.FechaSalida='1999-01-01' and U.CodigoBarras='"+ Unidad +"' ";
                            stmt = con.createStatement();
                            rs = stmt.executeQuery(query);
                            final CharSequence[] items = new String[Tamaño];
                            int i =0;
                            while (rs.next()) {
                                items[i] = rs.getString("descripcion");
                                i++;
                            }
                            AlertDialog.Builder dialog = new AlertDialog.Builder(UnidadTaller.this);
                            final ArrayList seletedItems = new ArrayList();
                            dialog.setTitle("Servicios en proceso");
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
                                    .setPositiveButton("   Salida", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {

                                            try{
                                                Connection con = Conexion.CONN();
                                                if (con == null) {
                                                    Toast.makeText(getApplicationContext(),
                                                            "Sin Conexion a Base de Datos",
                                                            Toast.LENGTH_LONG).show();
                                                } else {

                                                    String query = "exec Sp_SalidaServiciosAndroid '"+ Valor +"','"+ edtUnidad.getText().toString() +"'" ;


                                                    Statement stmt = con.createStatement();
                                                    stmt.executeUpdate(query);
                                                    CodServicio=0;
                                                    edtUnidad.setText("");
                                                    spServicios.setSelection(((ArrayAdapter)spServicios.getAdapter()).getPosition("Seleccionar Servicio.."));
                                                    edtObservacionServicio.setText("");
                                                    spOrigen.setSelection(((ArrayAdapter)spOrigen.getAdapter()).getPosition("Seleccionar Origen.."));
                                                    //spMecanicos.setSelection(((ArrayAdapter)spMecanicos.getAdapter()).getPosition("Seleccionar Mecanico.."));
                                                    ArrayList<String> data2 = new ArrayList<String>();
                                                    data2.add(0,"Seleccionar Mecanico..");
                                                    String[] array2 = data2.toArray(new String[0]);
                                                    ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(UnidadTaller.this, android.R.layout.simple_expandable_list_item_1, data2);

                                                    spProveedor.setSelection(((ArrayAdapter)spProveedor.getAdapter()).getPosition("Seleccionar Proveedor.."));
                                                    TvUnidad.setText("Unidad: ");
                                                    TvChofer.setText("Chofer: ");
                                                    TvPlacas.setText("Placas: ");
                                                    edtCosto.setText("");
                                                    edtKilometros.setText("");
                                                    edtObservacionProveedor.setText("");
                                                    Boton.setText("Entrada");
                                                    CodChofer = 0;
                                                    edtUnidad.requestFocus();
                                                }

                                            } catch (Exception e) {
                                                //Log.e("ERRO", e.getMessage());
                                                Toast.makeText(getApplicationContext(),
                                                        "Error: " + e.getMessage(),
                                                        Toast.LENGTH_LONG).show();
                                                edtUnidad.setText("");
                                            }
                                        }
                                    })
                                    .setNegativeButton("Nuevo  ", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            CodServicio = 0;
                                            spServicios.setSelection(((ArrayAdapter) spServicios.getAdapter()).getPosition("Seleccionar Servicio.."));
                                            edtObservacionServicio.setText("");
                                            spOrigen.setSelection(((ArrayAdapter) spOrigen.getAdapter()).getPosition("Seleccionar Origen.."));
                                            Mecanico = "Seleccionar Mecanico..";
                                            spProveedor.setSelection(((ArrayAdapter) spProveedor.getAdapter()).getPosition("Seleccionar Proveedor.."));
                                            edtCosto.setText("");
                                            edtKilometros.setText("");
                                            edtObservacionProveedor.setText("");
                                            Boton.setText("Entrada");
                                            edtKilometros.requestFocus();
                                        }
                                    });
                            dialog.show();
                        }else{
                            Toast.makeText(getApplicationContext(),
                                    "La unidad no se encuentra ni en taller ni en el cuadro, no podra realizar servicio.",
                                    Toast.LENGTH_LONG).show();
                            edtUnidad.setText("");
                        }
                    }
                }
                InputMethodManager inputMethodManager1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager1.hideSoftInputFromWindow(edtUnidad.getWindowToken(), 0);
            } catch (Exception e) {
                //Log.e("ERRO", e.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Error: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }else{
            //Quiere decir que NO se obtuvo resultado
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No se ha recibido datos del scaneo!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    View.OnClickListener OnClick =
            new View.OnClickListener() {

                @Override
                public void onClick(View v) {




                    try {
                        Connection con = Conexion.CONN();
                        if (edtUnidad.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(),
                                    "Debe espesificar la unidad",
                                    Toast.LENGTH_LONG).show();
                            edtUnidad.requestFocus();

                        }else  if (spServicios.getSelectedItem().toString().equals("Seleccionar Servicio..")) {
                            Toast.makeText(getApplicationContext(),
                                    "Debe espesificar el servicio",
                                    Toast.LENGTH_LONG).show();
                        }else  if ((spMecanicos.getSelectedItem().toString() == ("Seleccionar Mecanico..")) && (spProveedor.getSelectedItem().toString() == ("Seleccionar Proveedor..") ))   {
                            Toast.makeText(getApplicationContext(),
                                    "Debe espesificar el mecanico/proveedor",
                                    Toast.LENGTH_LONG).show();
                        }else  if ((spProveedor.getSelectedItem().toString() != ("Seleccionar Proveedor.."))  && (edtCosto.getText().toString().equals("")))  {
                            Toast.makeText(getApplicationContext(),
                                    "Debe ingresar el costo",
                                    Toast.LENGTH_LONG).show();
                            edtCosto.requestFocus();
                        }else if(con == null) {
                            Toast.makeText(getApplicationContext(),
                                    "Sin conexion a base de datos",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            String Unidad = edtUnidad.getText().toString();
                            String Km = edtKilometros.getText().toString();
                            String Servicio = spServicios.getSelectedItem().toString();
                            String Mecanicos = spMecanicos.getSelectedItem().toString();
                            String Origen = spOrigen.getSelectedItem().toString();
                            String Proveedor = spProveedor.getSelectedItem().toString();
                            //String Choafer = TvChofer.getText().toString();
                            String Costo=edtCosto.getText().toString();
                            String ObservacionP = edtObservacionProveedor.getText().toString();
                            String ObservacionS = edtObservacionServicio.getText().toString();
                            //4
                            String query = "declare @CodChofer int, @CodProveedor int,@CodServicio int,@CodMecanico int,@CodOrigen int,@Unidad int;  " +
                                    "set @CodServicio=(select Codigo from CatServicios where Descripcion=RTRIM(LTRIM('"+ Servicio +"'))) " +
                                    "set @CodOrigen=(select CodOrigen from CatOrigenRepTaller where Descripcion='"+ Origen +"') " +
                                    "set @CodChofer = '"+ CodChofer +"'  " +
                                    "set @CodMecanico=isnull((Select Codpersonal from CatPersonal where  Apodo = '"+ Mecanicos +"' and (tipo='M' or tipo='CM' or tipo='AM')),0)  " +
                                    "set @CodProveedor = (select CodProveedor  from Proveedores_Cat  where NombreCorto = '" + Proveedor +  "')  " +
                                    "set @Unidad=(Select Codigo from MqCatMaqyEquipo where CodigoBarras='"+ Unidad +"')  " +
                                    //"if not exists(select * from MqAsignacionServicios where CodMaquinaria=@Unidad and CodSevicio=@CodServicio and Convert(date,FechaSalida)='1999-01-01') " +
                                    //  "begin " +
                                    //    "insert into MqAsignacionServicios values (@Unidad,@CodChofer,@CodProveedor,@CodServicio,@CodMecanico,0,'"+ Km +"',1,0,GETDATE(),  " +
                                    //  "'01-01-1999','"+ Costo +"',RTRIM(LTRIM('"+ObservacionP+"')),RTRIM(LTRIM('"+ObservacionS+"')),@CodOrigen)  " +
                                    //"Delete from MqUnidadEspera where  Convert(date,FechaEntrada)<=Convert(date,Getdate()) and CodUnidad=@Unidad " +
                                    //"End " +
                                    //"Else " +
                                    //  "begin " +
                                    //    "update MqAsignacionServicios set CodProveedor=@CodProveedor,CodPersonal=@CodMecanico,Km_uso='" + Km + "',CostoServicio='"+ Costo +"',CodOrigenRep=@CodOrigen,Concepto=RTRIM(LTRIM('" + ObservacionP + "')),Observacion=RTRIM(LTRIM('" + ObservacionS + "')) where CodMaquinaria=@Unidad and Convert(date,FechaSalida)='1999-01-01'  " +
                                    //"end";
                                    "if not exists(select * from MqAsignacionServicios where CodMaquinaria=@Unidad and CodSevicio=@CodServicio and Convert(date,FechaSalida)='1999-01-01') "+
                                    "begin "+
                                    //"if exists(select * from MqUnidadEspera where CodUnidad=@Unidad and (Tipo=1 or Estatus=0) and Convert(date,FechaEntrada) <= Convert(date,GetDate())) "+
                                    //"begin "+
                                    "if ((select CodServicio from MqUnidadEspera where CodUnidad=@Unidad and Tipo=1 and Convert(date,FechaEntrada) <= Convert(date,GetDate()))= @CodServicio) "+
                                    "begin "+
                                    "insert into MqAsignacionServicios values (@Unidad,@CodChofer,@CodProveedor,@CodServicio,@CodMecanico,0,'"+ Km +"',1,0,GETDATE(),  " +
                                    "'01-01-1999','"+ Costo +"',RTRIM(LTRIM('"+ObservacionP+"')),RTRIM(LTRIM('"+ObservacionS+"')),@CodOrigen,0)  " +
                                    "Delete from MqUnidadEspera where  Convert(date,FechaEntrada)<=Convert(date,Getdate()) and CodUnidad=@Unidad and CodServicio=@CodServicio "+
                                    "end "+
                                    "else "+
                                    "begin "+
                                    "insert into MqAsignacionServicios values (@Unidad,@CodChofer,@CodProveedor,@CodServicio,@CodMecanico,0,'"+ Km +"',1,0,GETDATE(),  " +
                                    "'01-01-1999','"+ Costo +"',RTRIM(LTRIM('"+ObservacionP+"')),RTRIM(LTRIM('"+ObservacionS+"')),@CodOrigen,0)  " +
                                    "update MqUnidadEspera set Estatus=0 where CodUnidad=@Unidad and Tipo=1 and Convert(date,FechaEntrada) <= Convert(date,GetDate()) "+
                                    "Delete from MqUnidadEspera where  Convert(date,FechaEntrada)<=Convert(date,Getdate()) and CodUnidad=@Unidad and Tipo=0 "+
                                    "end "+
                                    //"end "+
                                    "End " +
                                    "Else " +
                                    "begin " +
                                    "update MqAsignacionServicios set CodProveedor=@CodProveedor,CodPersonal=@CodMecanico,Km_uso='" + Km + "',CostoServicio='"+ Costo +"',CodOrigenRep=@CodOrigen,Concepto=RTRIM(LTRIM('" + ObservacionP + "')),Observacion=RTRIM(LTRIM('" + ObservacionS + "')) where CodMaquinaria=@Unidad and Convert(date,FechaSalida)='1999-01-01'  " +
                                    "update MqUnidadEspera set Estatus=0 where CodUnidad=@Unidad and Tipo=1 and Convert(date,FechaEntrada) <= Convert(date,GetDate()) "+
                                    "end";
                            Statement stmt = con.createStatement();
                            stmt.executeUpdate(query);

                            CodServicio=0;
                            edtUnidad.setText("");
                            spServicios.setSelection(((ArrayAdapter)spServicios.getAdapter()).getPosition("Seleccionar Servicio.."));
                            edtObservacionServicio.setText("");
                            spOrigen.setSelection(((ArrayAdapter)spOrigen.getAdapter()).getPosition("Seleccionar Origen.."));
                            //spMecanicos.setSelection(((ArrayAdapter)spMecanicos.getAdapter()).getPosition("Seleccionar Mecanico.."));
                            ArrayList<String> data2 = new ArrayList<String>();
                            data2.add(0,"Seleccionar Mecanico..");
                            String[] array2 = data2.toArray(new String[0]);
                            ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(UnidadTaller.this, android.R.layout.simple_expandable_list_item_1, data2);

                            spProveedor.setSelection(((ArrayAdapter)spProveedor.getAdapter()).getPosition("Seleccionar Proveedor.."));
                            TvUnidad.setText("Unidad: ");
                            TvChofer.setText("Chofer: ");
                            TvPlacas.setText("Placas: ");
                            edtCosto.setText("");
                            edtKilometros.setText("");
                            edtObservacionProveedor.setText("");
                            Boton.setText("Entrada");
                            CodChofer = 0;
                            edtUnidad.requestFocus();
                        }
                    } catch (Exception e) {
                        //Log.e("ERRO", e.getMessage());
                        Toast.makeText(getApplicationContext(),
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }

                }

            };

}
