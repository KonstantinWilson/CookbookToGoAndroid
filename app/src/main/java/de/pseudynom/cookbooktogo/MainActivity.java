package de.pseudynom.cookbooktogo;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements Serializable {
	DrawerLayout drawerLayout = null;

	RecipesFragment recipesFragment = null;
	RecipeFragment recipeFragment = null;
	CartFragment cartFragment = null;

	Fragment parentFragment = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(true);
		actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white);



		NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(
				new NavigationView.OnNavigationItemSelectedListener() {
					@Override
					public boolean onNavigationItemSelected(MenuItem menuItem) {
						menuItem.setChecked(true);
						drawerLayout.closeDrawers();

						if(menuItem.getItemId() == R.id.nav_cart){
							parentFragment = null;
							cartFragment = new CartFragment();
							FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
							transaction.replace(R.id.fragment_container, cartFragment);
							transaction.addToBackStack(null);
							transaction.commit();
						}
						else if(menuItem.getItemId() == R.id.nav_recipes){
							parentFragment = null;
							recipesFragment = new RecipesFragment();
							FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
							transaction.replace(R.id.fragment_container, recipesFragment);
							transaction.addToBackStack(null);
							transaction.commit();
						}
						else if(menuItem.getItemId() == R.id.nav_settings){
							parentFragment = null;
							SettingsFragment newFrag = new SettingsFragment();
							FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
							transaction.replace(R.id.fragment_container, newFrag);
							transaction.addToBackStack(null);
							transaction.commit();
						}

						return true;
					}
				});

		cartFragment = new CartFragment();
		cartFragment.setArguments(getIntent().getExtras());
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, cartFragment).commit();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				if(parentFragment == null) drawerLayout.openDrawer(GravityCompat.START);
				else switchToParentFragment();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void setParentFragment(Fragment fragment){
		if(fragment != null){
			this.parentFragment = fragment;
			getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
		}
	}

	public void setParentFragmentArguments(Bundle bundle){
		parentFragment.setArguments(bundle);
	}

	public void switchToParentFragment(){
		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, parentFragment).addToBackStack(null).commit();
		this.parentFragment = null;
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white);
	}

	public void onClickAddToCart(View view) {
		recipeFragment = recipesFragment.getRecipeFragment();
		if(recipeFragment != null) recipeFragment.onClickAddToCart(view);
	}

	public void onDelete(View view){
		if(cartFragment != null){
			if(cartFragment.getCartItemFragment() == null) cartFragment.onDelete(view);
			else cartFragment.getCartItemFragment().onDelete(view);
		}
	}

	public void onSave(View view){
		if(cartFragment != null){
			if(cartFragment.getCartItemFragment() != null) cartFragment.getCartItemFragment().onSave(view);
		}
	}

	public void onAddIngredient(View view){
		if(cartFragment != null) cartFragment.onAddIngredient(view);
	}
}
