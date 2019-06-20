package in.geekofia.blog.activities;

import androidx.annotation.NonNull;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

// Fragments
import in.geekofia.blog.BuildConfig;
import in.geekofia.blog.R;
import in.geekofia.blog.fragments.*;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private String versionName = BuildConfig.VERSION_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);

        navigationView.setNavigationItemSelectedListener(this);

        TextView appVersion = hView.findViewById(R.id.drawer_app_version);
        appVersion.setText("Version : " + versionName);

        TextView appDev = hView.findViewById(R.id.drawer_app_dev);
        appDev.setText("Developer : chankruze");

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PostsFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_posts);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_posts:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PostsFragment()).commit();
                break;
            case R.id.nav_categories:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CategoriesFragment()).commit();
                break;
            case R.id.nav_tags:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TagsFragment()).commit();
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_contact:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ContactFragment()).commit();
                break;
            case R.id.nav_author:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://github.com/GEEKOFIA/blog/blob/master/.github/CONTRIBUTING.md")));
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.nav_github:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://github.com/GEEKOFIA/blog/")));
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.nav_donate:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://p-y.tm/Bkbz-ER")));
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.nav_rate:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + getPackageName())));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                }
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                PostsFragment postsFragment = (PostsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                postsFragment.search(newText);

                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id) {
//            Toast.makeText(MainActivity.this, "Thanks for your $100 donation !", Toast.LENGTH_LONG).show();
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
