package com.atox.http.gui;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.atox.R;
import com.atox.http.gui.fragments.HomeFragment;
import com.atox.http.gui.fragments.NotificationsFragment;
import com.atox.http.gui.fragments.ProfileFragment;

public class ProdutorActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtor);

        //loading the default fragment
        loadFragment(new HomeFragment());

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_inicio:
                fragment = new HomeFragment();
                break;

            /*case R.id.navigation_dashboard:
                fragment = new DashboardFragment();
                break;*/

            case R.id.navigation_notificacoes:
                fragment = new NotificationsFragment();
                break;

            case R.id.navigation_perfil:
                fragment = new ProfileFragment();
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
