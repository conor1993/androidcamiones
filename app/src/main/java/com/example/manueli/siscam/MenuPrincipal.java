package com.example.manueli.siscam;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

public class MenuPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    int Taller=Variables.Taller;
    int Servicios=Variables.Servicio;
    int Cuadro=Variables.Cuadro;
    int Supervisor = Variables.Supervisor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.Salir) {
            MenuPrincipal.this.startActivity(new Intent(MenuPrincipal.this,Login.class));
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            finish();
            System.runFinalization();
            System.exit(0);
            MenuPrincipal.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.UnidadesEspera) {
            if (Cuadro == 1) {
                Intent in = new Intent (MenuPrincipal.this, UnidadesEspera.class);
                startActivity(in);
            }else
            {
                Toast.makeText(getApplicationContext(),
                        "No cuenta con el permiso para realizar esta actividad.",
                        Toast.LENGTH_LONG).show();
            }

        } else if (id == R.id.Taller) {
            if (Taller == 1) {

                Intent in = new Intent (MenuPrincipal.this, UnidadTaller.class);
                startActivity(in);
            }else
            {
                Toast.makeText(getApplicationContext(),
                        "No cuenta con el permiso para realizar esta actividad.",
                        Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.Servicio) {
            if (Servicios == 1) {
                Intent in = new Intent (MenuPrincipal.this, UnidadServicio.class);
                startActivity(in);
            }else
            {
                Toast.makeText(getApplicationContext(),
                        "No cuenta con el permiso para realizar esta actividad.",
                        Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.EnvioDeDatos) {

            if (Supervisor == 1) {

            Intent in = new Intent (MenuPrincipal.this, EnvioDeDatos.class);
            startActivity(in);
            }else{
              Toast.makeText(getApplicationContext(),
                    "No cuenta con el permiso para realizar esta actividad.",
                Toast.LENGTH_LONG).show();
            }

        } else if (id == R.id.SupervizorMaquila) {
            if (Supervisor == 1) {

             Intent in = new Intent (MenuPrincipal.this, Supervisor.class);
            startActivity(in);
            }else
            {
              Toast.makeText(getApplicationContext(),
                    "No cuenta con el permiso para realizar esta actividad.",
                  Toast.LENGTH_LONG).show();
            }

        } else if (id == R.id.Mapa) {
            // Intent in = new Intent (MenuPrincipal.this, EnvioDatosMaquila.class);
            //startActivity(in);
        } else if (id == R.id.ChoferMaquila) {
            Intent in = new Intent (MenuPrincipal.this, ChoferSalidaMaquiladora.class);
            startActivity(in);
        }else if (id == R.id.nav_share) {

        } else if (id == R.id.Salir) {
            MenuPrincipal.this.startActivity(new Intent(MenuPrincipal.this,Login.class));
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            finish();
            System.runFinalization();
            System.exit(0);
            MenuPrincipal.this.finish();
        }else if (id==R.id.Programacion){
            if (Servicios == 1) {
                Intent in = new Intent (MenuPrincipal.this, ProgramacionesDeServicios.class);
                startActivity(in);
            }else if(Taller == 1)
            {
                Intent in = new Intent (MenuPrincipal.this, ProgramacionesDeServicios.class);
                startActivity(in);
            }else if(Cuadro == 1)
            {
                Intent in = new Intent (MenuPrincipal.this, ProgramacionesDeServicios.class);
                startActivity(in);
            }
            else {
                Toast.makeText(getApplicationContext(),
                        "No cuenta con el permiso para realizar esta actividad.",
                        Toast.LENGTH_LONG).show();
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
