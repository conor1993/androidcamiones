package com.example.manueli.siscam;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Manueli on 13/05/2016.
 */

public class Conexion {

    //String ip = "192.168.1.18:41433";

    //Fuera de la red
    //String ip="integrasrv3";
    //Dentro la red
    String ip="integrabc.dyndns.org:41433";
    String classs = "net.sourceforge.jtds.jdbc.Driver";
    public static String Serguridad="IlSeguridadCamiones";
    //String db = "camlatinoamericano";
    String db ="ILSeguridadCamiones";
    String un = "sa";
    String password = "int3gr@";



    //CAMIONES;

               /*

    String ip="camlatinos.dyndns.org:41433";
    String classs = "net.sourceforge.jtds.jdbc.Driver";
    public static String Serguridad="IlSegLatinos";
    //String db = "Camlatinos";
    String db = "CamlatinosPrueba";
    String un = "integra";
    String password = "int3gr@";

           */

    @SuppressLint("NewApi")
    public Connection CONN() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try {
            Class.forName(classs);
            /*if (Variables.ConexionCliente != null) {
                //ConnURL = "jdbc:jtds:sqlserver://" + ip + ";"
                  //      + "databaseName=" + db + ";user=" + un + ";password="
                    //    + password + ";";
                return conn;
            }
            else{

            }*/
            ConnURL=Variables.ConexionEmpresa;
            conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            Log.e("ERRO", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERRO", e.getMessage());
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }

        return conn;
    }
    public Connection CON() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try {
            Class.forName(classs);

                ConnURL = "jdbc:jtds:sqlserver://" + ip + ";"
                        + "databaseName=" + db + ";user=" + un + ";password="
                        + password + ";";


            conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            Log.e("ERRO", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERRO", e.getMessage());
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }

        return conn;
    }
}
