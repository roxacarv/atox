package com.atox.navegacao.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.atox.R;
import com.atox.navegacao.fragments.InicioFragment;
import com.atox.navegacao.fragments.PerfilFragment;
import com.atox.navegacao.fragments.ProdutoresFragment;
import com.atox.usuario.dominio.SessaoUsuario;

public class MenuActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = MenuActivity.class.getName();
    private SessaoUsuario sessaoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //loading the default fragment
        loadFragment(new InicioFragment());
        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        sessaoUsuario = SessaoUsuario.getSessao();
        Log.i(TAG, "Nome da pessoa: " + sessaoUsuario.getPessoaLogada().getNome());
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_inicio:
                fragment = new InicioFragment();
                break;

            case R.id.navigation_produtores:
                fragment = new ProdutoresFragment();
                break;

            case R.id.navigation_perfil:
                fragment = new PerfilFragment();
                break;
        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

}
