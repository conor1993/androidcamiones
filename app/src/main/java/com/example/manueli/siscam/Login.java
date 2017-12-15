package com.example.manueli.siscam;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class Login extends AppCompatActivity {

    Conexion Conexion;

    EditText edtUsuario,edtContraseña,edtClave;
    TextView TxvClave;
    ProgressBar pbbar;
    Button BtnClave,button;
    ImageButton BtnConfig;
    int Taller=0;
    int Servicios=0;
    int Cuadro=0;
    int Bandera=0;
    int Supervisor=0;
    static final int READ_BLOCK_SIZE = 100;

    /*SQLiteHelper usdbh =
            new SQLiteHelper(this, "ddconexionclientes", null, 1);*/
    /*SQLiteDatabase db = usdbh.getWritableDatabase();*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Variables
        Conexion = new Conexion();
        button = (Button)findViewById(R.id.IdButton );
        BtnClave = (Button)findViewById(R.id.btnClave );
        BtnConfig= (ImageButton)findViewById(R.id.BtnConfig);
        edtUsuario = (EditText) findViewById(R.id.edtUsuario);
        edtContraseña = (EditText) findViewById(R.id.edtContraseña);
        edtClave = (EditText) findViewById(R.id.edtClave);
        TxvClave = (TextView) findViewById(R.id.TvCve);
        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        pbbar.setVisibility(View.INVISIBLE);



        button.setOnClickListener(OnClick);
        BtnClave.setOnClickListener(OnClick);
        BtnConfig.setOnClickListener(OnClick);

        File file;
        BufferedReader input = null;
        file = null;
        try {
            file = new File(getCacheDir(), "Clave"); // Pass getFilesDir() and "MyFile" to read file

            input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            StringBuffer buffer = new StringBuffer();
            if ((line = input.readLine()) != null) {
                buffer.append(line);

                Connection con = Conexion.CON();
                if (con == null) {
                    Toast.makeText(getApplicationContext(),
                            "Sin Conexion a Base de Datos",
                            Toast.LENGTH_LONG).show();
                } else {
                    String query = "select * from ConexionEmpresas where Clave='"+ buffer.toString() +"'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    if (rs.next()) {
                        if(rs.getBoolean("Estatus")==false){
                            Toast.makeText(getApplicationContext(),
                                    "La empresa se encuentra dada de baja",
                                    Toast.LENGTH_SHORT).show();
                            Variables.ConexionEmpresa="";
                            Variables.BDSeguridad ="";
                            edtClave.setText("");
                        }else{
                            Variables.ConexionEmpresa = rs.getString("Conexion");
                            Variables.BDSeguridad = rs.getString("BDSeguridad");
                        }

                    }else{
                        Toast.makeText(getApplicationContext(),
                                "La clave ingresada es incorrecta",
                                Toast.LENGTH_LONG).show();
                        edtClave.setText("");
                    }

                }
                edtClave.setVisibility(View.INVISIBLE);
                TxvClave.setVisibility(View.INVISIBLE);
                BtnClave.setVisibility(View.INVISIBLE);
            }
        }catch (Exception e) {
            edtClave.setVisibility(View.VISIBLE);
            TxvClave.setVisibility(View.VISIBLE);
            BtnClave.setVisibility(View.VISIBLE);
        }
        /*
        input = null;
        file = null;
        try {
            file = new File(getCacheDir(), "bdseguridad1"); // Pass getFilesDir() and "MyFile" to read file

            input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            StringBuffer buffer = new StringBuffer();
            if ((line = input.readLine()) != null) {
                buffer.append(line);

                Variables.BDSeguridad = buffer.toString();

                edtClave.setVisibility(View.INVISIBLE);
                TxvClave.setVisibility(View.INVISIBLE);
                BtnClave.setVisibility(View.INVISIBLE);
            } else {
                edtClave.setVisibility(View.VISIBLE);
                TxvClave.setVisibility(View.VISIBLE);
                BtnClave.setVisibility(View.VISIBLE);
            }
        }catch (Exception e) {
            edtClave.setVisibility(View.VISIBLE);
            TxvClave.setVisibility(View.VISIBLE);
            BtnClave.setVisibility(View.VISIBLE);
        }
        */
        /*
        try{
            Toast.makeText(getApplicationContext(),
                    "1",
                    Toast.LENGTH_LONG).show();

            File dbtest =new File("/data/data/com.example.manueli.siscam/databases/ddconexionclientes");
            if(dbtest.exists())
            {
                //exist
                Toast.makeText(getApplicationContext(), "si existe!!!", Toast.LENGTH_SHORT).show();
                edtClave.setVisibility(View.INVISIBLE);
                TxvClave.setVisibility(View.INVISIBLE);
                BtnClave.setVisibility(View.INVISIBLE);
            }
            else
            {
                Toast.makeText(getApplicationContext(),
                        "2",
                        Toast.LENGTH_LONG).show();
                SQLiteHelper usdbh =
                        new SQLiteHelper(this, "ddconexionclientes", null, 1);

                SQLiteDatabase db = usdbh.getWritableDatabase();

                //Si hemos abierto correctamente la base de datos
                if(db != null)
                {
                    Toast.makeText(getApplicationContext(),
                            "3",
                            Toast.LENGTH_LONG).show();
                    //Insertamos 5 usuarios de ejemplo
                    try{
                        String sqlCreate = "CREATE TABLE conecionclientes (empresa TEXT, conexion TEXT,seguridad TEXT)";
                        db.execSQL(sqlCreate);

                    }catch (Exception e) {
                        //Log.e("ERRO", e.getMessage());
                        Toast.makeText(getApplicationContext(),
                                "Error: "+ e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                    Toast.makeText(getApplicationContext(),
                            "4",
                            Toast.LENGTH_LONG).show();
                    //Cerramos la base de datos
                    db.close();
                }
                //  doesn't exist


                Toast.makeText(getApplicationContext(), "Inserte la clave correspondiente a su empresa", Toast.LENGTH_SHORT).show();
                edtClave.setVisibility(View.VISIBLE);
                TxvClave.setVisibility(View.VISIBLE);
                BtnClave.setVisibility(View.VISIBLE);
            }

            Variables.ConexionEmpresa = "";
            Variables.BDSeguridad ="";
            String s="";
            String s2="";

        }catch (Exception e) {
            //Log.e("ERRO", e.getMessage());
            Toast.makeText(getApplicationContext(),
                    "Error: "+ e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
      */

        edtContraseña.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {

                if ((keyevent.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    pbbar.setVisibility(View.VISIBLE);
                    String Usuario = edtUsuario.getText().toString();
                    String Contraseña = edtContraseña.getText().toString();
                    try {

                        if (Usuario.equals("") || Contraseña.equals("")){
                            Toast.makeText(getApplicationContext(),
                                    "Ingrese Usuario y Contraseña",
                                    Toast.LENGTH_LONG).show();
                            edtUsuario.requestFocus();
                        }else{

                            Connection con = Conexion.CONN();
                            if (con == null) {
                                Toast.makeText(getApplicationContext(),
                                        "Sin Conexion a Base de Datos",
                                        Toast.LENGTH_LONG).show();
                            } else {

                                String query = "select distinct UA.IdArea,A.Descripcion as DescripcionArea from " + Variables.BDSeguridad + ".dbo.UsuarioAreas UA inner join " + Variables.BDSeguridad + ".dbo.Usuarios U on U.usrndx=UA.IdUsuario " +
                                        "inner join CatAreas A on UA.IdArea=A.id  where usrid='" + Usuario + "' and usrpas2=dbo.Fn_DesencriptacionContraseñaAndroid('" + Contraseña + "')";


                                //String query = "select distinct UA.IdArea,A.Descripcion as DescripcionArea from "+ Conexion.Serguridad +".dbo.UsuarioAreas UA inner join "+ Conexion.Serguridad +".dbo.Usuarios U on U.usrndx=UA.IdUsuario " +
                                  //      "inner join CatAreas A on UA.IdArea=A.id  where usrid='"+ Usuario +"' and usrpas2=dbo.Fn_DesencriptacionContraseñaAndroid('"+ Contraseña +"')";
                                Statement stmt = con.createStatement();
                                ResultSet rs = stmt.executeQuery(query);

                                pbbar.setVisibility(View.INVISIBLE);

                                Bandera = 0;
                                while (rs.next()){
                                    Bandera = 1;
                                    if (rs.getString("DescripcionArea").equals("TALLER")) {
                                        Taller=1;
                                        Variables.Taller=1;
                                    }else if (rs.getString("DescripcionArea").equals("SERVICIO")){
                                        Servicios=1;
                                        Variables.Servicio=1;
                                    }else if (rs.getString("DescripcionArea").equals("CUADRO")){
                                        Cuadro = 1;
                                        Variables.Cuadro=1;
                                    }else if (rs.getString("DescripcionArea").equals("SUPERVISOR")){
                                        Supervisor = 1;
                                        Variables.Supervisor=1;
                                    }
                                }
                                if (Bandera == 1){
                                    //Login.this.startActivity(new Intent(Login.this,MenuPrincipal.class));
                                    Intent i = new Intent (Login.this, MenuPrincipal.class);
                                    i.putExtra("T", Taller);
                                    i.putExtra("S", Servicios);
                                    i.putExtra("C", Cuadro);
                                    i.putExtra("Su",Supervisor);
                                    startActivity(i);
                                    edtUsuario.setText("");
                                    edtContraseña.setText("");
                                } else{
                                    Toast.makeText(getApplicationContext(),
                                            "Usuario o Contraseña Incorrecta",
                                            Toast.LENGTH_LONG).show();
                                    edtUsuario.setText("");
                                    edtContraseña.setText("");
                                    edtUsuario.requestFocus();
                                }
                            }
                        }

                    }catch (Exception e) {
                        //Log.e("ERRO", e.getMessage());
                        Toast.makeText(getApplicationContext(),
                                "Error: "+ e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                    edtUsuario.setFocusableInTouchMode(true);
                    edtUsuario.setFocusable(true);
                    pbbar.setVisibility(View.INVISIBLE);
                    edtUsuario.requestFocus();
                }

                return false;


            }
        });

    }
    View.OnClickListener OnClick =
            new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.IdButton) {
                        String Usuario = edtUsuario.getText().toString();
                        String Contraseña = edtContraseña.getText().toString();
                        pbbar.setVisibility(View.VISIBLE);
                        try {
                            if (Usuario.equals("") || Contraseña.equals("")) {
                                Toast.makeText(getApplicationContext(),
                                        "Ingrese Usuario y Contraseña",
                                        Toast.LENGTH_LONG).show();
                                edtUsuario.requestFocus();
                            } else {
                                Connection con = Conexion.CONN();
                                if (con == null) {
                                    Toast.makeText(getApplicationContext(),
                                            "Sin Conexion a Base de Datos",
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    String query = "select distinct UA.IdArea,A.Descripcion as DescripcionArea from " + Variables.BDSeguridad + ".dbo.UsuarioAreas UA inner join " + Variables.BDSeguridad + ".dbo.Usuarios U on U.usrndx=UA.IdUsuario " +
                                           "inner join CatAreas A on UA.IdArea=A.id  where usrid='" + Usuario + "' and usrpas2=dbo.Fn_DesencriptacionContraseñaAndroid('" + Contraseña + "')";

                                    //String query = "select distinct UA.IdArea,A.Descripcion as DescripcionArea from " + Conexion.Serguridad + ".dbo.UsuarioAreas UA inner join " + Conexion.Serguridad + ".dbo.Usuarios U on U.usrndx=UA.IdUsuario " +
                                      //      "inner join CatAreas A on UA.IdArea=A.id  where usrid='" + Usuario + "' and usrpas2=dbo.Fn_DesencriptacionContraseñaAndroid('" + Contraseña + "')";

                                    Statement stmt = con.createStatement();
                                    ResultSet rs = stmt.executeQuery(query);

                                    //pbbar.setVisibility(View.INVISIBLE);
                                /*if (rs.next()){

                                    Toast.makeText(getApplicationContext(),
                                            rs.getString("usrid") ,
                                            Toast.LENGTH_LONG).show();


                                }else{
                                    Toast.makeText(getApplicationContext(),
                                            "Usuario o Contraseña Incorrecta",
                                            Toast.LENGTH_LONG).show();
                                    edtUsuario.setText("");
                                    edtContraseña.setText("");
                                    edtUsuario.requestFocus();
                                }*/
                                    Bandera = 0;
                                    while (rs.next()) {
                                        Bandera = 1;
                                        if (rs.getString("DescripcionArea").equals("TALLER")) {
                                            Taller = 1;
                                            Variables.Taller = 1;
                                        } else if (rs.getString("DescripcionArea").equals("SERVICIO")) {
                                            Servicios = 1;
                                            Variables.Servicio = 1;
                                        } else if (rs.getString("DescripcionArea").equals("CUADRO")) {
                                            Cuadro = 1;
                                            Variables.Cuadro = 1;
                                        } else if (rs.getString("DescripcionArea").equals("SUPERVISOR")) {
                                            Supervisor = 1;
                                            Variables.Supervisor = 1;
                                        }

                                    }
                                    if (Bandera == 1) {
                                        //Login.this.startActivity(new Intent(Login.this,MenuPrincipal.class));
                                        Intent i = new Intent(Login.this, MenuPrincipal.class);
                                        i.putExtra("T", Taller);
                                        i.putExtra("S", Servicios);
                                        i.putExtra("C", Cuadro);
                                        i.putExtra("Su", Supervisor);
                                        startActivity(i);
                                        edtUsuario.setText("");
                                        edtContraseña.setText("");
                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                "Usuario o Contraseña Incorrecta",
                                                Toast.LENGTH_LONG).show();
                                        edtUsuario.setText("");
                                        edtContraseña.setText("");
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
                        pbbar.setVisibility(View.INVISIBLE);
                        edtUsuario.requestFocus();
                    }

                    if (v.getId() == R.id.BtnConfig){
                        edtClave.setVisibility(View.VISIBLE);
                        TxvClave.setVisibility(View.VISIBLE);
                        BtnClave.setVisibility(View.VISIBLE);
                    }

                    if (v.getId() == R.id.btnClave) {



                        try {
                                String str = edtClave.getText().toString();
                                Connection con = Conexion.CON();

                                if (con == null) {
                                    Toast.makeText(getApplicationContext(),
                                            "Sin Conexion a Base de Datos",
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    String query = "select * from ConexionEmpresas where Clave='"+ str +"'";
                                    Statement stmt = con.createStatement();
                                    ResultSet rs = stmt.executeQuery(query);

                                    if (rs.next()) {
                                        if(rs.getBoolean("Estatus")==false){
                                            Toast.makeText(getApplicationContext(),
                                                    "La empresa se encuentra dada de baja",
                                                    Toast.LENGTH_SHORT).show();
                                            Variables.ConexionEmpresa="";
                                            Variables.BDSeguridad ="";
                                            edtClave.setText("");
                                        }else{


                                            File file;
                                            BufferedReader input = null;
                                            file = null;
                                            try {

                                                file = new File(getCacheDir(), "Clave"); // Pass getFilesDir() and "MyFile" to read file

                                                input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                                                String line;
                                                StringBuffer buffer = new StringBuffer();
                                                if ((line = input.readLine()) != null) {
                                                    buffer.append(line);
                                                    Variables.ConexionEmpresa = rs.getString("Conexion");
                                                    Variables.BDSeguridad = rs.getString("BDSeguridad");
                                                }
                                            } catch (IOException e) {

                                                String content = str;
                                                FileOutputStream outputStream;
                                                try {
                                                    file = new File(getCacheDir(), "Clave");
                                                    outputStream = new FileOutputStream(file);
                                                    outputStream.write(content.getBytes());
                                                    outputStream.close();
                                                    Variables.ConexionEmpresa = rs.getString("Conexion");
                                                    Variables.BDSeguridad = rs.getString("BDSeguridad");

                                                } catch (IOException ex) {
                                                    Toast.makeText(getBaseContext(),"Error al intentar guardar la empresa", Toast.LENGTH_SHORT).show();
                                                }
                                            }

/*
                                            input = null;
                                            file = null;
                                            try {

                                                file = new File(getCacheDir(), "bdseguridad1"); // Pass getFilesDir() and "MyFile" to read file

                                                input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                                                String line;
                                                StringBuffer buffer = new StringBuffer();
                                                if ((line = input.readLine()) != null) {
                                                    buffer.append(line);
                                                    Variables.BDSeguridad = buffer.toString();
                                                }
                                            } catch (IOException e) {

                                                String content = rs.getString("BDSeguridad");
                                                FileOutputStream outputStream;
                                                try {
                                                    file = new File(getCacheDir(), "bdseguridad1");
                                                    outputStream = new FileOutputStream(file);
                                                    outputStream.write(content.getBytes());
                                                    outputStream.close();
                                                    Variables.BDSeguridad = content.toString();
                                                } catch (IOException ex) {
                                                    Toast.makeText(getBaseContext(),"Error al intentar guardar la empresa", Toast.LENGTH_SHORT).show();
                                                }
                                            }

*/
                                            // Mostramos que se ha guardado
                                            //Toast.makeText(getBaseContext(), "Guardado", Toast.LENGTH_SHORT).show();
                                            edtClave.setVisibility(View.INVISIBLE);
                                            TxvClave.setVisibility(View.INVISIBLE);
                                            BtnClave.setVisibility(View.INVISIBLE);
                                            edtUsuario.setFocusable(true);

                                        }

                                    }else{
                                        Toast.makeText(getApplicationContext(),
                                                "La clave ingresada es incorrecta",
                                                Toast.LENGTH_LONG).show();
                                        edtClave.setText("");
                                    }

                                }


                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                    }

                }

            };


}
