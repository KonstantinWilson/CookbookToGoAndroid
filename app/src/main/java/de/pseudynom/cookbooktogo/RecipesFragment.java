package de.pseudynom.cookbooktogo;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class RecipesFragment extends Fragment implements Responsable {

	private LinearLayout llRecipes = null;
	private RecipeFragment recipeFragment = null;

	private ArrayList<Recipe> alRecipes = new ArrayList<Recipe>();

	public RecipesFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_recipes, container, false);


		llRecipes = (LinearLayout) v.findViewById(R.id.ll_recipes);

		/*
		Recipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Recipe r = alRecipes.get(i);

				recipeFragment = RecipeFragment.newInstance(r);
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, recipeFragment).addToBackStack(null).commit();
			}
		});
		*/

		HttpsRequest request = new HttpsRequest(this, getString(R.string.url_recipes), HttpsRequest.TYPE_JSON);
		request.execute();


		return v;
	}

	public void response(Object o){
		String s = (String)o;
		try {
			JSONObject json = new JSONObject(s);
			JSONArray jsonRecipes = json.getJSONArray("recipes");
			for(int i = 0; i < jsonRecipes.length(); i++){
				JSONObject jsonRecipe = jsonRecipes.getJSONObject(i);

				String tagList = jsonRecipe.getString("tagsList");
				ArrayList<String> tags = new ArrayList<String>();
				String[] tagArray = tagList.split(", ");
				for(int j = 0; j < tagArray.length && j < 10; j++){
					tags.add(tagArray[j]);
				}

				Recipe recipe = new Recipe(jsonRecipe.getInt("id"), jsonRecipe.getString("name"), tags);
				alRecipes.add(recipe);

				if(i > 0) llRecipes.addView(LayoutInflater.from(this.getContext()).inflate(R.layout.element_divider, llRecipes, false));

				ConstraintLayout clNewItem = (ConstraintLayout)LayoutInflater.from(getContext()).inflate(R.layout.recipes_adapter, llRecipes, false);
				clNewItem.setTag(new Integer(i));
				TextView tvName = clNewItem.findViewById(R.id.tv_recipes_name);
				tvName.setText(recipe.getName());
				TextView tvTags = clNewItem.findViewById(R.id.tv_recipes_tags);
				tvTags.setText(tagList);
				clNewItem.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						ArrayList<Fragment> oldFragment = (ArrayList<Fragment>)getActivity().getSupportFragmentManager().getFragments();
						FragmentActivity fa = getActivity();
						if(fa != null && oldFragment.size() > 0){
							int index = (Integer)view.getTag();

							Recipe r = alRecipes.get(index);
							recipeFragment = RecipeFragment.newInstance(r);
							getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, recipeFragment).addToBackStack(null).commit();
							((MainActivity)fa).setParentFragment(oldFragment.get(0));
						}
					}
				});
				llRecipes.addView(clNewItem);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public RecipeFragment getRecipeFragment(){
		return recipeFragment;
	}
}
