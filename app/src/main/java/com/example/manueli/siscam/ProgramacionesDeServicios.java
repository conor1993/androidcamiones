package com.example.manueli.siscam;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import google.zxing.integration.android.IntentIntegrator;
import google.zxing.integration.android.IntentResult;


public class ProgramacionesDeServicios extends ActionBarActivity implements View.OnClickListener{

    CalendarView calendarView;
    Spinner spServicios;
    Conexion Conexion;
    PreparedStatement stmt;
    ResultSet rs;
    ImageButton imgButtonUnidad;
    Button Boton;
    EditText edtUnidad,edtObservacion;
    TextView TvNumModelo,TvPlacas;
    int CodUnidad;
    String Fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programaciones_de_servicios);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Date date = new Date();
        DateFormat hourdateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Fecha= hourdateFormat.format(date).toString();

        Conexion = new Conexion();
        calendarView = (CalendarView) findViewById(R.id.calendarView);
        spServicios = (Spinner) findViewById(R.id.spServicios);
        imgButtonUnidad = (ImageButton) findViewById(R.id.imgButtonUnidad);
        Boton = (Button) findViewById(R.id.IdButton);
        edtUnidad  = (EditText) findViewById(R.id.edtUnidad);
        TvPlacas = (TextView) findViewById(R.id.TvPlacas);
        TvNumModelo=(TextView) findViewById(R.id.TvNumModelo);
        edtObservacion=(EditText) findViewById(R.id.edtObservacion);

        try {

            Connection con = Conexion.CONN();
            if (con == null) {
                Toast.makeText(getApplicationContext(),
                        "Sin Conexion a Base de Datos",
                        Toast.LENGTH_LONG).show();
            } else {
                String query = "select Descripcion  from CatServicios order by Descripcion";
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

            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        Boton.setOnClickListener(OnClick);
        imgButtonUnidad.setOnClickListener(this);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int año, int mes , int dia) {
                //dateDisplay.setText("Date: " + i2 + " / " + i1 + " / " + i);

                //Toast.makeText(getApplicationContext(), " " + dia + "/" + (mes + 1) + "/" + año, Toast.LENGTH_LONG).show();
                Fecha = dia + "/" + (mes + 1) +"/" + año;
                //Toast.makeText(getApplicationContext(), Fecha, Toast.LENGTH_LONG).show();
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

                            String query ="select * from  MqCatMaqyEquipo where CodigoBarras='"+ Unidad +"'";
                            Statement stmt = con.createStatement();
                            ResultSet rs = stmt.executeQuery(query);

                            if (rs.next()) {
                                TvNumModelo.setText("N° Eco: "+ rs.getString("clave") + "          Modelo: "+ rs.getString("Modelo"));
                                TvPlacas.setText("Placas: " + rs.getString("Placas"));
                                CodUnidad = rs.getInt("Codigo");

                            }
                            else {
                                TvNumModelo.setText("N° Eco:                   Modelo:");
                                TvPlacas.setText("Placas:");
                                CodUnidad = 0;
                                Toast.makeText(getApplicationContext(),
                                        "La unidad no fue encontrada.",
                                        Toast.LENGTH_LONG).show();
                                edtUnidad.setText("");
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

                    String query ="select * from  MqCatMaqyEquipo where CodigoBarras='"+ Unidad +"'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    if (rs.next()) {
                        TvNumModelo.setText("N° Eco: "+ rs.getString("clave") + "          Modelo: "+ rs.getString("Modelo"));
                        TvPlacas.setText("Placas: " + rs.getString("Placas"));
                        CodUnidad = rs.getInt("Codigo");

                    }
                    else {
                        TvNumModelo.setText("N° Eco:                   Modelo:");
                        TvPlacas.setText("Placas:");
                        CodUnidad = 0;
                        Toast.makeText(getApplicationContext(),
                                "La unidad no fue encontrada.",
                                Toast.LENGTH_LONG).show();
                        edtUnidad.setText("");
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
                        Date FechaActual = new Date();
                        //DateFormat hourdateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");


                       Date date1 =formatter.parse(Fecha);

                        //Fecha Actual Menor a Seleccionada
                        if (FechaActual.compareTo(date1) > 0) {
                            Toast.makeText(getApplicationContext(),
                                    "La fecha debe ser mayora la fecha actual",
                                    Toast.LENGTH_LONG).show();

                        }else{

                            Connection con = Conexion.CONN();
                            if (CodUnidad == 0) {
                                Toast.makeText(getApplicationContext(),
                                        "Debe espesificar la unidad",
                                        Toast.LENGTH_LONG).show();
                                edtUnidad.requestFocus();
                            //}else if (FechaB == FechaA){

                            }else  if (spServicios.getSelectedItem().toString().equals("Seleccionar Servicio..")) {
                                Toast.makeText(getApplicationContext(),
                                        "Debe espesificar el servicio",
                                        Toast.LENGTH_LONG).show();
                            }else if(con == null) {
                                Toast.makeText(getApplicationContext(),
                                        "Sin conexion a base de datos",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                String Observacion =edtObservacion.getText().toString();
                                String Servicio = spServicios.getSelectedItem().toString();
                                //6
                                String query = "declare @CodServicio int,@Visita int,@Chofer int; " +
                                        "Set @CodServicio = (Select Codigo from CatServicios where Descripcion='"+ Servicio +"') " +
                                        "Set @Visita= (select case tipo when 1 then 2 when 0 then 3 end as tipo from CatServicios where Codigo=@CodServicio) " +
                                        "Set @Chofer= (Select CodPersonal  from CatPersonal where CodUnidad="+ CodUnidad +") " +
                                        "if exists (select * from MqUnidadEspera where Convert(date,FechaEntrada)=Convert(date,'" + Fecha + "') and Tipo=1 and CodUnidad="+ CodUnidad +") " +
                                            "begin " +
                                                 "Update MqUnidadEspera set Visita=@Visita,CodChofer=@Chofer,Tipo=1,CodServicio=@CodServicio,Tramite=RTRIM(LTRIM('"+ Observacion +"')) where CodUnidad="+ CodUnidad +" " +
                                            "end " +
                                        "else " +
                                            "begin " +
                                                  "insert into MqUnidadEspera values (@Visita,"+ CodUnidad +",@Chofer,'"+ Fecha +"',1,@CodServicio,1,RTRIM(LTRIM('"+ Observacion +"'))) " +
                                            "end" ;
                                Statement stmt = con.createStatement();
                                stmt.executeUpdate(query);
                                TvPlacas.setText("Placas: ");
                                TvNumModelo.setText("N° Eco:                   Modelo:");
                                spServicios.setSelection(((ArrayAdapter)spServicios.getAdapter()).getPosition("Seleccionar Servicio.."));
                                CodUnidad = 0;
                                edtUnidad.setText("");
                                edtObservacion.setText("");
                                edtUnidad.requestFocus();
                            }
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