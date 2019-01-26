package com.atox.navegacao.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.atox.R;
import com.atox.navegacao.fragments.InicioFragment;
import com.atox.navegacao.fragments.PerfilFragment;
import com.atox.navegacao.fragments.ProdutoresFragment;
import com.atox.usuario.dominio.SessaoUsuario;

public class MenuActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{


    private SessaoUsuario sessaoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        loadFragment(new InicioFragment());
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        this.sessaoUsuario = SessaoUsuario.getInstance();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            default:
                break;
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
