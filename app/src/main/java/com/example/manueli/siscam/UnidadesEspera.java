package com.example.manueli.siscam;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import google.zxing.integration.android.IntentIntegrator;
import google.zxing.integration.android.IntentResult;

public class UnidadesEspera extends ActionBarActivity implements View.OnClickListener{

    Conexion Conexion;
    EditText edtUnidad, edtUsuario,edtApodo;
    TextView TvDescripcion, /*TvModelo,*/ TvPlacas, TvClave, TvNombre,TvTramite;
    RadioButton RbTaller, RbServicio, RbAdmin,RbCuadro,RbSegCuadro;
    int CodUnidad,Scan,CodChofer;
    Button Boton;
    private TextView info;
    ImageButton imgButtonUnidad,imageButtonPersonal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unidades_espera);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        info = (TextView) findViewById(R.id.info);

        Conexion = new Conexion();
        Boton = (Button) findViewById(R.id.IdButton);
        edtUnidad = (EditText) findViewById(R.id.edtUnidad);
        edtUsuario = (EditText) findViewById(R.id.edtUsuario);
        edtApodo = (EditText) findViewById(R.id.edtApodo);
        TvDescripcion = (TextView) findViewById(R.id.TvDescripcion);
        //TvModelo = (TextView) findViewById(R.id.TvModelo);
        TvPlacas = (TextView) findViewById(R.id.TvPlacas);
        TvClave = (TextView) findViewById(R.id.TvClave);
        TvNombre = (TextView) findViewById(R.id.TvNombre);
        RbTaller = (RadioButton) findViewById(R.id.RbTaller);
        RbServicio = (RadioButton) findViewById(R.id.RbServicio);
        RbAdmin = (RadioButton) findViewById(R.id.RbAdmin);
        RbCuadro = (RadioButton) findViewById(R.id.RbCuadro);
        RbSegCuadro = (RadioButton) findViewById(R.id.RbSegCuadro);
        imgButtonUnidad = (ImageButton) findViewById(R.id.imgButtonUnidad);
        imageButtonPersonal=(ImageButton) findViewById(R.id.imgButtonPersonal);
        TvTramite = (EditText)findViewById(R.id.edtTramite);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        /*edtUnidad.setFocusableInTouchMode(true);
        edtUnidad.setFocusable(true);
        edtUnidad.requestFocus();*/


        //setOnClickListener(OnClick);
        Boton.setOnClickListener(OnClick);

        imgButtonUnidad.setOnClickListener(this);
        imageButtonPersonal.setOnClickListener(this);


        RbAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = (RbAdmin.isChecked());

                if (isChecked) {
                    RbSegCuadro.setSelected(false);
                    RbTaller.setSelected(false);
                    RbServicio.setSelected(false);
                    RbCuadro.setSelected(false);
                    TvTramite.setVisibility(View.VISIBLE);
                }

            }
        });
        RbTaller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = (RbTaller.isChecked());

                if (isChecked) {

                    if (Boton.getText().toString()!="Salida"){
                        /*RbAdmin.setSelected(false);
                        RbSegCuadro.setSelected(false);
                        RbServicio.setSelected(false);
                        RbCuadro.setSelected(false);*/
                        TvTramite.setVisibility(View.INVISIBLE);
                    }

                }

            }
        });
        RbServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = (RbServicio.isChecked());

                if (Boton.getText().toString()!="Salida"){
                   /* RbAdmin.setSelected(false);
                    RbTaller.setSelected(false);
                    RbSegCuadro.setSelected(false);
                    RbCuadro.setSelected(false);*/
                    TvTramite.setVisibility(View.INVISIBLE);

                }

            }
        });
        RbCuadro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isChecked = (RbCuadro.isChecked());

                if (isChecked) {
                    /*RbAdmin.setSelected(false);
                    RbTaller.setSelected(false);
                    RbServicio.setSelected(false);
                    RbSegCuadro.setSelected(false);*/
                    TvTramite.setVisibility(View.VISIBLE);

                }

            }
        });
        RbSegCuadro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isChecked = (RbSegCuadro.isChecked());

                if (isChecked) {
                   /* RbAdmin.setSelected(false);
                    RbTaller.setSelected(false);
                    RbServicio.setSelected(false);
                    RbCuadro.setSelected(false);*/
                    TvTramite.setVisibility(View.VISIBLE);

                }

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
                            if (Unidad.equals("")) {

                            }else{
                               /* String query="if  not exists(select UE.* from MqUnidadEspera UE inner join MqCatMaqyEquipo U on U.Codigo=UE.CodUnidad where UE.Estatus=0 and Convert(date,UE.FechaEntrada) <= Convert(date,GetDate()) and U.CodigoBarras='"+ Unidad +"') " +
                                                    "begin " +
                                                        "select U.Codigo,U.clave,U.Modelo,U.Placas,UE.Estatus,P.CodigoBarras,P.NumPersonal,P.Nombre,P.Apodo,UE.Visita,P.CodPersonal,UE.Tramite, " +
                                                        "case when MAs.Tipo='TAllER' then 1 when MAS.Tipo='MANTENIMIENTO' then 2 else 0 end as Tipo  " +
                                                        "from MqCatMaqyEquipo U left join CatPersonal P on P.CodUnidad=U.Codigo left outer join (select * from MqUnidadEspera where Tipo=0) UE on UE.CodUnidad=U.Codigo  " +
                                                        "left join (select MAS.CodMaquinaria ,case when MAS.EsReparacion=1 then 'TALLER' when MAS.EsMantenimiento=1 then 'MANTENIMIENTO'  " +
                                                        "end as Tipo  from MqAsignacionServicios MAS inner join MqCatMaqyEquipo U on U.Codigo=MAS.CodMaquinaria   where U.CodigoBarras='"+ Unidad +"' " +
                                                        "and Convert(date,MAS.FechaSalida)=Convert(date,'1999-01-01')) as MAS on MAS.CodMaquinaria=U.Codigo " +
                                                        "where    U.CodigoBarras='"+ Unidad +"'  " +
                                                    "end " +
                                            "else " +
                                                    "begin " +
                                                        "select U.Codigo,U.clave,U.Modelo,U.Placas,UE.Estatus,P.CodigoBarras,P.NumPersonal,P.Nombre,P.Apodo,UE.Visita,P.CodPersonal,UE.Tramite, " +
                                                        "case when MAs.Tipo='TAllER' then 1 when MAS.Tipo='MANTENIMIENTO' then 2 else 0 end as Tipo  " +
                                                        "from MqCatMaqyEquipo U left join CatPersonal P on P.CodUnidad=U.Codigo left outer join MqUnidadEspera UE on Ue.CodUnidad=U.Codigo  " +
                                                        "left join (select MAS.CodMaquinaria ,case when MAS.EsReparacion=1 then 'TALLER' when MAS.EsMantenimiento=1 then 'MANTENIMIENTO' " +
                                                        "end as Tipo  from MqAsignacionServicios MAS inner join MqCatMaqyEquipo U on U.Codigo=MAS.CodMaquinaria   where U.CodigoBarras='"+ Unidad +"'  " +
                                                        "and Convert(date,MAS.FechaSalida)=Convert(date,'1999-01-01')) as MAS on MAS.CodMaquinaria=U.Codigo  " +
                                                        "where    Convert(date,UE.FechaEntrada) <= Convert(date,GetDate()) and U.CodigoBarras='"+ Unidad +"' " +
                                                    "end "; */
                                String query="if  exists(select UE.* from MqUnidadEspera UE inner join MqCatMaqyEquipo U on U.Codigo=UE.CodUnidad where Convert(date,UE.FechaEntrada) <= Convert(date,GetDate()) and U.CodigoBarras='"+ Unidad +"') " +
                                        "begin " +
                                        "select top 1 U.Codigo,U.clave,U.Modelo,U.Placas,UE.Estatus,P.CodigoBarras,P.NumPersonal,P.Nombre,P.Apodo,UE.Visita,P.CodPersonal,UE.Tramite, " +
                                        "case when MAs.Tipo='TAllER' then 1 when MAS.Tipo='MANTENIMIENTO' then 2 else 0 end as Tipo  " +
                                        "from MqCatMaqyEquipo U left join CatPersonal P on P.CodUnidad=U.Codigo left outer join MqUnidadEspera UE on Ue.CodUnidad=U.Codigo  " +
                                        "left join (select MAS.CodMaquinaria ,case when MAS.EsReparacion=1 then 'TALLER' when MAS.EsMantenimiento=1 then 'MANTENIMIENTO' " +
                                        "end as Tipo  from MqAsignacionServicios MAS inner join MqCatMaqyEquipo U on U.Codigo=MAS.CodMaquinaria   where U.CodigoBarras='"+ Unidad +"'  " +
                                        "and Convert(date,MAS.FechaSalida)=Convert(date,'1999-01-01')) as MAS on MAS.CodMaquinaria=U.Codigo  " +
                                        "where  Convert(date,UE.FechaEntrada) <= Convert(date,GetDate()) and U.CodigoBarras='"+ Unidad +"' " +
                                        "end " +
                                        "else " +
                                        "begin " +
                                        "select top 1 U.Codigo,U.clave,U.Modelo,U.Placas,isnull(UE.Estatus,1) as Estatus,P.CodigoBarras,P.NumPersonal,P.Nombre,P.Apodo,UE.Visita,P.CodPersonal,UE.Tramite, " +
                                        "case when MAs.Tipo='TAllER' then 1 when MAS.Tipo='MANTENIMIENTO' then 2 else 0 end as Tipo  " +
                                        "from MqCatMaqyEquipo U left join CatPersonal P on P.CodUnidad=U.Codigo left outer join (select * from MqUnidadEspera where Tipo=0) UE on UE.CodUnidad=U.Codigo  " +
                                        "left join (select MAS.CodMaquinaria ,case when MAS.EsReparacion=1 then 'TALLER' when MAS.EsMantenimiento=1 then 'MANTENIMIENTO'  " +
                                        "end as Tipo  from MqAsignacionServicios MAS inner join MqCatMaqyEquipo U on U.Codigo=MAS.CodMaquinaria   where U.CodigoBarras='"+ Unidad +"' " +
                                        "and Convert(date,MAS.FechaSalida)=Convert(date,'1999-01-01')) as MAS on MAS.CodMaquinaria=U.Codigo " +
                                        "where  U.CodigoBarras='"+ Unidad +"'  " +
                                        "end ";

                                Statement stmt = con.createStatement();
                                ResultSet rs = stmt.executeQuery(query);
                                if (rs.next()) {
                                    if (rs.getInt("Tipo")==0)
                                    {
                                        TvDescripcion.setText("N° Eco: " + rs.getString("clave") + "            Modelo: " + rs.getString("Modelo"));
                                        TvPlacas.setText("Placas: " + rs.getString("Placas"));
                                        CodUnidad = rs.getInt("Codigo");
                                        if (rs.getString("CodigoBarras") != null) {
                                            CodChofer = rs.getInt("CodPersonal");
                                            edtUsuario.setText(rs.getString("CodigoBarras"));
                                            TvClave.setText("N° Empleado: " + rs.getString("NumPersonal"));
                                            TvNombre.setText("Chofer: " + rs.getString("Nombre"));
                                            edtApodo.setText(rs.getString("Apodo"));
                                        }else{
                                            TvClave.setText("N° Empleado: ");
                                            TvNombre.setText("Chofer: ");
                                            edtUsuario.setText("");
                                            edtApodo.setText("");
                                            edtUsuario.requestFocus();
                                        }
                                        int Visita = rs.getInt("Visita");
                                        String TipoServicio;
                                        if (Visita == 1) {
                                            RbAdmin.setChecked(true);
                                            TipoServicio = "Administracion";
                                            TvTramite.setText(rs.getString("Tramite"));
                                            TvTramite.setVisibility(View.VISIBLE);
                                        } else if (Visita == 2) {
                                            RbServicio.setChecked(true);
                                            TipoServicio = "Servicio";
                                            TvTramite.setVisibility(View.INVISIBLE);
                                        } else if (Visita == 3) {

                                            RbTaller.setChecked(true);
                                            TipoServicio = "Taller";
                                            TvTramite.setVisibility(View.INVISIBLE);
                                        }else if (Visita == 4) {
                                            RbCuadro.setChecked(true);
                                            TipoServicio = "Cuadro";
                                            TvTramite.setText(rs.getString("Tramite"));
                                            TvTramite.setVisibility(View.VISIBLE);
                                        }else if (Visita == 5) {
                                            RbSegCuadro.setChecked(true);
                                            TipoServicio = "10/90";
                                            TvTramite.setText(rs.getString("Tramite"));
                                            TvTramite.setVisibility(View.VISIBLE);
                                        }
                                        if (rs.getBoolean("Estatus") == false) {
                                            Boton.setText("Salida");
                                            TvTramite.setVisibility(View.VISIBLE);
                                            Boton.requestFocus();
                                        }
                                        else {
                                            Boton.setText("Entrada");
                                        }
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),
                                                "No han dado por terminado la reparacion o mantenimiento de la unidad, informe al encargado" ,
                                                Toast.LENGTH_LONG).show();

                                        TvClave.setText("N° Empleado: ");
                                        TvNombre.setText("Chofer: ");
                                        TvPlacas.setText("Placas: ");
                                        TvDescripcion.setText("N° Eco:                  Modelo:");
                                        TvTramite.setText("");
                                        TvTramite.setVisibility(View.INVISIBLE);
                                        edtUnidad.setText("");
                                        edtUsuario.setText("");
                                        edtApodo.setText("");
                                        edtUnidad.setText("");
                                        edtUnidad.requestFocus();
                                    }

                                } else {
                                    TvDescripcion.setText("N° Eco:                  Modelo:");
                                    TvPlacas.setText("Placas: ");
                                    Toast.makeText(getApplicationContext(),
                                            "No se encontraron registros",
                                            Toast.LENGTH_LONG).show();
                                    edtUnidad.setText("");
                                    edtUnidad.requestFocus();
                                }
                            }
                        }
                        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(edtApodo.getWindowToken(), 0);

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

        edtApodo.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                //If the keyevent is a key-down event on the "enter" button
                if ((keyevent.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    //...
                    // Perform your action on key press here
                    // ...
                    String Apodo = edtApodo.getText().toString();
                    try {

                        Connection con = Conexion.CONN();
                        if (con == null) {
                            Toast.makeText(getApplicationContext(),
                                    "Sin Conexion a Base de Datos",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            if (Apodo.equals("")) {

                            }else{
                                String query = "select CodPersonal,NumPersonal,Apodo,Nombre ,CodigoBarras from CatPersonal where (Tipo='C' or Tipo='CM' or Tipo='AC') and Apodo='" + Apodo + "'";
                                Statement stmt = con.createStatement();
                                ResultSet rs = stmt.executeQuery(query);
                                //pbbar.setVisibility(View.INVISIBLE);
                                if (rs.next()) {
                                    //res.getString("tm_nombrelinea");
                                    edtUsuario.setText(rs.getString("CodigoBarras"));
                                    TvClave.setText("N° Empleado: " + rs.getString("NumPersonal"));
                                    TvNombre.setText("Chofer: " + rs.getString("Apodo"));

                                    CodChofer = rs.getInt("CodPersonal");

                                } else {
                                    TvClave.setText("N° Empleado: ");
                                    TvNombre.setText("Chofer: ");
                                    Toast.makeText(getApplicationContext(),
                                            "No se encontraron registros",
                                            Toast.LENGTH_LONG).show();
                                    edtUsuario.setText("");
                                    edtApodo.setText("");
                                    edtUsuario.requestFocus();
                                }
                            }


                        }
                        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

                        inputMethodManager.hideSoftInputFromWindow(edtUsuario.getWindowToken(), 0);
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
        //Enter...
        edtUsuario.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                //If the keyevent is a key-down event on the "enter" button
                if ((keyevent.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    //...
                    // Perform your action on key press here
                    // ...
                    String Usuario = edtUsuario.getText().toString();
                    try {

                        Connection con = Conexion.CONN();
                        if (con == null) {
                            Toast.makeText(getApplicationContext(),
                                    "Sin Conexion a Base de Datos",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            if (Usuario.equals("")) {

                            }else{
                                String query = "select CodPersonal,NumPersonal,Apodo,Nombre from CatPersonal where (Tipo='C' or Tipo='CM' or Tipo='AC') and CodigoBarras='" + Usuario + "'";
                                Statement stmt = con.createStatement();
                                ResultSet rs = stmt.executeQuery(query);
                                //pbbar.setVisibility(View.INVISIBLE);
                                if (rs.next()) {
                                    //res.getString("tm_nombrelinea");
                                    TvClave.setText("N° Empleado: " + rs.getString("NumPersonal"));
                                    TvNombre.setText("Chofer: " + rs.getString("Nombre"));
                                    CodChofer = rs.getInt("CodPersonal");
                                    edtApodo.setText(rs.getString("Apodo"));

                                } else {
                                    TvClave.setText("N° Empleado: ");
                                    TvNombre.setText("Chofer: ");
                                    Toast.makeText(getApplicationContext(),
                                            "No se encontraron registros",
                                            Toast.LENGTH_LONG).show();
                                    edtUsuario.setText("");
                                    edtApodo.setText("");
                                    edtApodo.requestFocus();
                                }
                            }


                        }
                        //InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

                        //inputMethodManager.hideSoftInputFromWindow(edtUsuario.getWindowToken(), 0);
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

            Scan = 1;

        }else  if(v.getId()==R.id.imgButtonPersonal){
            //Se instancia un objeto de la clase IntentIntegrator
            //IntentIntegrator scanIntegrator = new IntentIntegrator(this);

            IntentIntegrator scanIntegrator=new IntentIntegrator(this);
            //Se procede con el proceso de scaneo
            scanIntegrator.initiateScan();
            Scan =2;

        }
    }
    /*public void onClick(View v){
        //Se responde al evento click
        if(v.getId()==R.id.imgButtonPersonal){
            //Se instancia un objeto de la clase IntentIntegrator
            //IntentIntegrator scanIntegrator = new IntentIntegrator(this);

            IntentIntegrator scanIntegrator=new IntentIntegrator(this);
            //Se procede con el proceso de scaneo
            scanIntegrator.initiateScan();
            Scan =2;
        }
    }*/
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //Se obtiene el resultado del proceso de scaneo y se parsea
        //IntentResult
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if(Scan ==1){
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
                        if (Unidad.equals("")) {

                        }else{
                               /*String query="select U.Codigo,U.clave,U.Modelo,U.Placas,UE.Estatus,P.CodigoBarras,P.NumPersonal,P.Nombre,P.Apodo,UE.Visita,P.CodPersonal,UE.Tramite,case when MAs.Tipo='TAller' then 1 when MAS.Tipo='Servicio' then 2 else 0 end as Tipo "+
                                        "from MqCatMaqyEquipo U left join CatPersonal P on P.CodUnidad=U.Codigo left outer join MqUnidadEspera UE on Ue.CodUnidad=U.Codigo "+
                                        "left join (select MAS.CodMaquinaria ,case when MAS.EsReparacion=1 then 'TALLER' when MAS.EsMantenimiento=1 then 'SERVICIO'  end as Tipo  "+
                                        "from MqAsignacionServicios MAS inner join MqCatMaqyEquipo U on U.Codigo=MAS.CodMaquinaria   "+
                                        "where U.CodigoBarras='"+ Unidad +"' and Convert(date,MAS.FechaSalida)=Convert(date,'1999-01-01')) as MAS on MAS.CodMaquinaria=U.Codigo "+
                                        " where U.Estatus='A' and U.CodigoBarras='"+ Unidad +"'";
                                        */
                            String query="if  exists(select UE.* from MqUnidadEspera UE inner join MqCatMaqyEquipo U on U.Codigo=UE.CodUnidad where UE.Tipo=1 and Convert(date,UE.FechaEntrada) <= Convert(date,GetDate()) and U.CodigoBarras='"+ Unidad +"') " +
                                    "begin " +
                                    "select U.Codigo,U.clave,U.Modelo,U.Placas,UE.Estatus,P.CodigoBarras,P.NumPersonal,P.Nombre,P.Apodo,UE.Visita,P.CodPersonal,UE.Tramite, " +
                                    "case when MAs.Tipo='TAllER' then 1 when MAS.Tipo='MANTENIMIENTO' then 2 else 0 end as Tipo  " +
                                    "from MqCatMaqyEquipo U left join CatPersonal P on P.CodUnidad=U.Codigo left outer join MqUnidadEspera UE on Ue.CodUnidad=U.Codigo  " +
                                    "left join (select MAS.CodMaquinaria ,case when MAS.EsReparacion=1 then 'TALLER' when MAS.EsMantenimiento=1 then 'MANTENIMIENTO' " +
                                    "end as Tipo  from MqAsignacionServicios MAS inner join MqCatMaqyEquipo U on U.Codigo=MAS.CodMaquinaria   where U.CodigoBarras='"+ Unidad +"'  " +
                                    "and Convert(date,MAS.FechaSalida)=Convert(date,'1999-01-01')) as MAS on MAS.CodMaquinaria=U.Codigo  " +
                                    "where U.Estatus='A' and UE.Tipo=1 and Convert(date,UE.FechaEntrada) <= Convert(date,GetDate()) and U.CodigoBarras='"+ Unidad +"' " +
                                    "end " +
                                    "else " +
                                    "begin " +
                                    "select U.Codigo,U.clave,U.Modelo,U.Placas,UE.Estatus,P.CodigoBarras,P.NumPersonal,P.Nombre,P.Apodo,UE.Visita,P.CodPersonal,UE.Tramite, " +
                                    "case when MAs.Tipo='TAllER' then 1 when MAS.Tipo='MANTENIMIENTO' then 2 else 0 end as Tipo  " +
                                    "from MqCatMaqyEquipo U left join CatPersonal P on P.CodUnidad=U.Codigo left outer join (select * from MqUnidadEspera where Tipo=0) UE on UE.CodUnidad=U.Codigo  " +
                                    "left join (select MAS.CodMaquinaria ,case when MAS.EsReparacion=1 then 'TALLER' when MAS.EsMantenimiento=1 then 'MANTENIMIENTO'  " +
                                    "end as Tipo  from MqAsignacionServicios MAS inner join MqCatMaqyEquipo U on U.Codigo=MAS.CodMaquinaria   where U.CodigoBarras='"+ Unidad +"' " +
                                    "and Convert(date,MAS.FechaSalida)=Convert(date,'1999-01-01')) as MAS on MAS.CodMaquinaria=U.Codigo " +
                                    "where U.Estatus='A' and  U.CodigoBarras='"+ Unidad +"'  " +
                                    "end ";

                            Statement stmt = con.createStatement();
                            ResultSet rs = stmt.executeQuery(query);
                            if (rs.next()) {
                                if (rs.getInt("Tipo")==0)
                                {
                                    TvDescripcion.setText("N° Eco: " + rs.getString("clave") + "            Modelo: " + rs.getString("Modelo"));
                                    //TvModelo.setText("Modelo: " + rs.getString("Modelo"));
                                    TvPlacas.setText("Placas: " + rs.getString("Placas"));
                                    CodUnidad = rs.getInt("Codigo");
                                    if (rs.getString("CodigoBarras") != null) {
                                        CodChofer = rs.getInt("CodPersonal");
                                        edtUsuario.setText(rs.getString("CodigoBarras"));
                                        TvClave.setText("N° Empleado: " + rs.getString("NumPersonal"));
                                        TvNombre.setText("Chofer: " + rs.getString("Nombre"));
                                        edtApodo.setText(rs.getString("Apodo"));
                                        //edtUsuario.setEnabled(false);
                                    }else{
                                        TvClave.setText("N° Empleado: ");
                                        TvNombre.setText("Chofer: ");
                                        edtUsuario.setText("");
                                        edtApodo.setText("");
                                        //edtUsuario.setEnabled(true);
                                        edtUsuario.requestFocus();
                                    }
                                    int Visita = rs.getInt("Visita");
                                    String TipoServicio;
                                    if (Visita == 1) {
                                        RbAdmin.setChecked(true);
                                        TipoServicio = "Administracion";
                                        TvTramite.setText(rs.getString("Tramite"));
                                        TvTramite.setVisibility(View.VISIBLE);
                                    } else if (Visita == 2) {
                                        RbServicio.setChecked(true);
                                        TipoServicio = "Servicio";
                                        TvTramite.setVisibility(View.INVISIBLE);
                                    } else if (Visita == 3) {

                                        RbTaller.setChecked(true);
                                        TipoServicio = "Taller";
                                        TvTramite.setVisibility(View.INVISIBLE);
                                    }else if (Visita == 4) {
                                        RbCuadro.setChecked(true);
                                        TipoServicio = "Cuadro";
                                        TvTramite.setText(rs.getString("Tramite"));
                                        TvTramite.setVisibility(View.VISIBLE);
                                    }else if (Visita == 5) {
                                        RbSegCuadro.setChecked(true);
                                        TipoServicio = "10/90";
                                        TvTramite.setText(rs.getString("Tramite"));
                                        TvTramite.setVisibility(View.VISIBLE);
                                    }

                                    if (rs.getBoolean("Estatus") == true) {
                                        Boton.setText("Salida");
                                        TvTramite.setVisibility(View.VISIBLE);
                                        Boton.requestFocus();
                                    }
                                    else {
                                        Boton.setText("Entrada");
                                    }
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),
                                            "No han dado por terminado la reparacion o mantenimiento de la unidad, informe al encargado" ,
                                            Toast.LENGTH_LONG).show();

                                    TvClave.setText("N° Empleado: ");
                                    TvNombre.setText("Chofer: ");
                                    TvPlacas.setText("Placas: ");
                                    //TvModelo.setText("Modelo: ");
                                    TvDescripcion.setText("N° Eco:                  Modelo:");
                                    TvTramite.setText("");
                                    TvTramite.setVisibility(View.INVISIBLE);
                                    edtUnidad.setText("");
                                    edtUsuario.setText("");
                                    edtApodo.setText("");
                                    edtUnidad.setText("");
                                    Boton.setText("Entrada");
                                    edtUnidad.requestFocus();
                                }

                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "No se encontraron registros",
                                        Toast.LENGTH_LONG).show();
                                TvClave.setText("N° Empleado: ");
                                TvNombre.setText("Chofer: ");
                                TvPlacas.setText("Placas: ");
                                //TvModelo.setText("Modelo: ");
                                TvDescripcion.setText("N° Eco:                  Modelo:");
                                TvTramite.setText("");
                                TvTramite.setVisibility(View.INVISIBLE);
                                edtUnidad.setText("");
                                edtUsuario.setText("");
                                edtApodo.setText("");
                                edtUnidad.setText("");
                                Boton.setText("Entrada");
                                edtUnidad.requestFocus();
                            }
                        }
                    }
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(edtApodo.getWindowToken(), 0);
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

        }else if(Scan == 2){

            String scanContent = scanningResult.getContents();

            edtUsuario.setText(scanContent);
            String Usuario = edtUsuario.getText().toString();
            try {

                Connection con = Conexion.CONN();
                if (con == null) {
                    Toast.makeText(getApplicationContext(),
                            "Sin Conexion a Base de Datos",
                            Toast.LENGTH_LONG).show();
                } else {
                    if (Usuario.equals("")) {


                    }else{
                        String query = "select CodPersonal,NumPersonal,Apodo,Nombre from CatPersonal where (C.Tipo='C' or C.Tipo='CM' or C.Tipo='AC') and CodigoBarras='" + Usuario + "'";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        //pbbar.setVisibility(View.INVISIBLE);
                        if (rs.next()) {
                            //res.getString("tm_nombrelinea");
                            TvClave.setText("N° Empleado: " + rs.getString("NumPersonal"));
                            TvNombre.setText("Chofer: " + rs.getString("Nombre"));
                            CodChofer = rs.getInt("CodPersonal");
                            edtApodo.setText(rs.getString("Apodo"));
                        } else {
                            TvClave.setText("N° Empleado: ");
                            TvNombre.setText("Chofer: ");
                            Toast.makeText(getApplicationContext(),
                                    "No se encontraron registros",
                                    Toast.LENGTH_LONG).show();
                            edtUsuario.setText("");
                            edtApodo.setText("");
                            edtUsuario.requestFocus();
                        }
                    }


                }
            } catch (Exception e) {
                //Log.e("ERRO", e.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Error: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }

    }


    //Botton....
    View.OnClickListener OnClick =
            new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    //String Text = TextBoton.toString();
                    try {
                        //Si los RadioButton estan seleccionados
                        int Visita = 0;
                        if (RbTaller.isChecked()) {
                            Visita = 3;
                        } else if (RbServicio.isChecked()) {
                            Visita = 2;
                        } else if (RbAdmin.isChecked()) {
                            Visita = 1;
                        }else if(RbCuadro.isChecked()){
                            Visita=4;
                        }else {
                            Visita=5;
                        }

                        if (Visita != 0) {
                            if (edtUnidad.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(),
                                        "Debe espesificar la unidad",
                                        Toast.LENGTH_LONG).show();
                                edtUnidad.requestFocus();

                            }else   if (edtUsuario.getText().toString().equals("") || edtApodo.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(),
                                        "Debe espesificar el chofer",
                                        Toast.LENGTH_LONG).show();
                                edtUsuario.requestFocus();
                            }
                            else {

                                Connection con = Conexion.CONN();
                                if (con == null) {
                                    Toast.makeText(getApplicationContext(),
                                            "Sin conexion a base de datos",
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    /*String query = "if exists(select * from MqUnidadEspera where Convert(date,FechaEntrada) <= Convert(date,GetDate()) and CodUnidad="+ CodUnidad +") " +
                                                        "begin " +
                                                            "if (" + Visita + " = 1 or "+ Visita +"=4 or "+ Visita +"=5) " +
                                                                "begin " +
                                                                    "Declare @Entrada dateTime; " +
                                                                    "set @Entrada=(Select FechaEntrada from MqUnidadEspera where Tipo=0 and CodUnidad="+ CodUnidad +") " +
                                                                    "insert into MqHistorialCuadro values("+ CodChofer +"," + CodUnidad +","+ Visita +",'"+ TvTramite.getText().toString() +"',@Entrada,GETDATE(),0) " +
                                                                    "delete from MqUnidadEspera where Visita="+ Visita +" and CodUnidad=" + CodUnidad + " " +
                                                                "end " +
                                                            "else " +
                                                                "begin " +
                                                                            "if ((select Tipo from MqUnidadEspera where Convert(date,FechaEntrada)=Convert(date,Getdate()) and CodUnidad="+ CodUnidad +")= 1) "+
                                                                                "begin " +
                                                                                        "Declare @Fecha date; " +
                                                                                        "set @Fecha=(Select Convert(date,FechaEntrada) from MqUnidadEspera where Tipo=1 and Convert(date,FechaEntrada)=Convert(date,Getdate()) and CodUnidad=" + CodUnidad + ") " +
                                                                                    "insert into MqHistorialCuadro values("+ CodChofer +"," + CodUnidad +","+ Visita +",'"+ TvTramite.getText().toString() +"',@Fecha,GETDATE(),1) " +
                                                                                    "Update MqUnidadEspera set CodChofer="+ CodChofer +",Tipo=0,Estatus=1,Tramite='' where Tipo=1 and Convert(date,FechaEntrada)=Convert(date,Getdate()) and CodUnidad="+ CodUnidad +" " +
                                                                                "end " +
                                                                            "else " +
                                                                                "begin " +
                                                                                    "Declare @Fecha2 dateTime; " +
                                                                                    "set @Fecha2=(Select FechaEntrada from MqUnidadEspera where Tipo=0 and CodUnidad="+ CodUnidad +") " +
                                                                                    "if not exists(select * from MqHistorialCuadro where CodUnidad="+ CodUnidad +" and Convert(date,FechaEntrada)=Convert(date,getdate())) " +
                                                                                            "begin "+
                                                                                                    "insert into MqHistorialCuadro values("+ CodChofer +"," + CodUnidad +","+ Visita +",'"+ TvTramite.getText().toString() +"',@Fecha2,GETDATE(),0) " +
                                                                                            "end "+
                                                                                    "delete from MqUnidadEspera where Tipo=0 and Convert(date,FechaEntrada)=@Fecha2 and CodUnidad="+ CodUnidad +"  "+
                                                                                "end " +
                                                                "End " +
                                                        "end " +
                                                "Else " +
                                                    "begin " +
                                                        "insert into MqUnidadEspera values ("+ Visita +","+ CodUnidad +","+ CodChofer +",GetDate(),0,0,0,'"+ TvTramite.getText().toString() +"') " +
                                                        "update catpersonal set CodUnidad=0 where codunidad= "+ CodUnidad +" " +
                                                        "update CatPersonal set CodUnidad=" + CodUnidad + " where CodPersonal=" + CodChofer + " " +
                                                    "End "; */
                                    String query = "if exists(select * from MqUnidadEspera where Convert(date,FechaEntrada) <= Convert(date,GetDate()) and CodUnidad="+ CodUnidad +") " +
                                            "begin " +
                                            "if (" + Visita + " = 1 or "+ Visita +"=4 or "+ Visita +"=5) " +
                                            "begin " +
                                            "Declare @Entrada dateTime; " +
                                            "set @Entrada=(Select FechaEntrada from MqUnidadEspera where Tipo=0 and CodUnidad="+ CodUnidad +") " +
                                            "insert into MqHistorialCuadro values("+ CodChofer +"," + CodUnidad +","+ Visita +",'"+ TvTramite.getText().toString() +"',@Entrada,GETDATE(),0) " +
                                            "delete from MqUnidadEspera where Visita="+ Visita +" and CodUnidad=" + CodUnidad + " " +
                                            "end " +
                                            "else " +
                                            "begin " +
                                            "if ((select estatus from mqunidadespera where  Convert(date,FechaEntrada) <= Convert(date,GetDate()) and CodUnidad="+ CodUnidad +")=1)" +
                                            "begin " +
                                            "Update MqUnidadespera set Estatus=0 where Convert(date,FechaEntrada) <= Convert(date,GetDate()) and CodUnidad="+ CodUnidad + " " +
                                            "end " +
                                            "else "+
                                            "begin "+
                                            "Declare @Entrada2 dateTime,@Tipo int=0; " +
                                            "set @Tipo=(Select Top 1 Tipo from MqUnidadEspera where Convert(date,FechaEntrada) <= Convert(date,GetDate()) and CodUnidad="+ CodUnidad +") "+
                                            "set @Entrada2=(Select Top 1 FechaEntrada from MqUnidadEspera where Convert(date,FechaEntrada) <= Convert(date,GetDate()) and CodUnidad="+ CodUnidad +") " +
                                            "insert into MqHistorialCuadro values("+ CodChofer +"," + CodUnidad +","+ Visita +",'"+ TvTramite.getText().toString() +"',@Entrada2,GETDATE(),@Tipo) " +
                                            "delete from MqUnidadEspera where Visita="+ Visita +" and CodUnidad=" + CodUnidad + " " +
                                            "end "+
                                            "end " +
                                            "end " +
                                            "Else " +
                                            "begin " +
                                            "insert into MqUnidadEspera values ("+ Visita +","+ CodUnidad +","+ CodChofer +",GetDate(),0,0,0,'"+ TvTramite.getText().toString() +"') " +
                                            "update catpersonal set CodUnidad=0 where codunidad= "+ CodUnidad +" " +
                                            "update CatPersonal set CodUnidad=" + CodUnidad + " where CodPersonal=" + CodChofer + " " +
                                            "End ";
                                    Statement stmt = con.createStatement();
                                    stmt.executeUpdate(query);
                                    TvClave.setText("N° Empleado: ");
                                    TvNombre.setText("Chofer: ");
                                    TvPlacas.setText("Placas: ");
                                    //TvModelo.setText("Modelo: ");
                                    TvDescripcion.setText("N° Eco:                  Modelo:");
                                    TvTramite.setText("");
                                    TvTramite.setVisibility(View.INVISIBLE);
                                    edtUnidad.setText("");
                                    edtUsuario.setText("");
                                    edtApodo.setText("");
                                    edtUsuario.setEnabled(true);
                                    Boton.setText("Entrada");
                                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                    inputMethodManager.hideSoftInputFromWindow(edtUnidad.getWindowToken(), 0);
                                    edtUnidad.requestFocus();
                                }
                            }

                            //Si no se encuentra la unidad especificada--Unidad.equals("")

                            //Si no estan seleccionado los RadioButtons que seleccione
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Seleccione el destino",
                                    Toast.LENGTH_LONG).show();

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
