package com.example.manueli.siscam;

/**
 * Created by integra-des03 on 22/12/2016.
 */
import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionClientes {

    //CAMIONES;



/*
    String ip="camlatinos.dyndns.org:41433";
    String classs = "net.sourceforge.jtds.jdbc.Driver";
    public static String Serguridad="IlSegLatinos";
    String db = "CamLatinos";
    //String db = "CamlatinosPrueba";
    String un = "integra";
    String password = "int3gr@";
*/
String classs = "net.sourceforge.jtds.jdbc.Driver";


    @SuppressLint("NewApi")
    public Connection CONNCli() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try {
            Class.forName(classs);
            ConnURL = Variables.ConexionCliente;
            //ConnURL="jdbc:jtds:sqlserver://" + ip + ";"
              //      + "databaseName=" + db + ";user=" + un + ";password="
                //    + password + ";";


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
