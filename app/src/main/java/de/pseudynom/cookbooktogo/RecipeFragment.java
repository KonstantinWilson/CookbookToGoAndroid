package de.pseudynom.cookbooktogo;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeFragment extends Fragment implements Responsable{
	private static String RECIPE_KEY = "recipe_key";
	private Recipe mRecipe;

	private TextView tvName = null;
	private TextView tvCreator = null;
	private TextView tvSource = null;
	private TextView tvTags = null;
	private ImageView ivImage = null;
	private LinearLayout llIngredients = null;
	private LinearLayout llSteps = null;

	public RecipeFragment() {
		// Required empty public constructor
	}

	public static RecipeFragment newInstance(Recipe recipe){
		RecipeFragment fragment = new RecipeFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(RECIPE_KEY, recipe);
		fragment.setArguments(bundle);

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_recipe, container, false);

		// Get Widgets
		tvName = (TextView)v.findViewById(R.id.tv_recipe_name);
		tvCreator = (TextView)v.findViewById(R.id.tv_recipe_creator);
		tvSource = (TextView)v.findViewById(R.id.tv_recipe_source);
		tvTags = (TextView)v.findViewById(R.id.tv_recipe_tags);
		ivImage = (ImageView)v.findViewById(R.id.iv_recipe_image);
		llIngredients = (LinearLayout) v.findViewById(R.id.ll_recipe_ingredients);
		llSteps = (LinearLayout)v.findViewById(R.id.ll_recipe_steps);

		mRecipe = (Recipe)getArguments().getSerializable(RECIPE_KEY);
		String url = String.format(getString(R.string.url_recipe), mRecipe.getId());
		System.out.println("URL: " + url);

		HttpsRequest request = new HttpsRequest(this, url, HttpsRequest.TYPE_JSON);
		request.execute();

		return v;
	}

	public void response(Object o){
		if(o == null){
			return;
		}

		String className = o.getClass().getName();
		System.out.println("RESPONSE: " + o.getClass().getName());
		if(ArrayList.class.isAssignableFrom(o.getClass())){
			System.out.println("CHECK FOUR DOUBLES");

			ArrayList<Ingredient> ingredients = (ArrayList<Ingredient>)o;
			ArrayList<Ingredient> recipeIngredients = mRecipe.getIngredients();

			for(int i = 0; i < recipeIngredients.size(); i++){
				Ingredient ing = recipeIngredients.get(i);
				boolean match = false;
				for(int j = 0; j < ingredients.size(); j++){
					if(ing.equals(ingredients.get(j))){
						ingredients.get(j).add(recipeIngredients.get(i));
						match = true;
					}
				}
				if(!match) ingredients.add(ing);
			}

			FileManager fm = new FileManager(this, FileManager.MODE_WRITE_INGREDIENTS);
			fm.execute(getContext().getFilesDir().toString(), getString(R.string.filename_cart), ingredients);
		}
		else if(className.equals(String.class.getName())){
			String result = (String)o;

			try{
				JSONObject jsonRecipe = new JSONObject(result);

				// Name
				tvName.setText(mRecipe.getName());

				// Creator
				if(mRecipe.getCreator() == null || mRecipe.getCreator().length() <= 0) tvCreator.setVisibility(View.GONE);
				else{
					tvCreator.setVisibility(View.VISIBLE);
					tvCreator.setText(mRecipe.getCreator());
				}

				// Source
				mRecipe.setSource(jsonRecipe.getString("source"));
				if(mRecipe.getSource() == null || mRecipe.getSource().length() <= 0) tvSource.setVisibility(View.GONE);
				else{
					tvSource.setVisibility(View.VISIBLE);
					tvSource.setText(mRecipe.getSource());
				}

				// Tags
				ArrayList<String> tags = new ArrayList<String>();
				JSONArray jsonTags = jsonRecipe.getJSONArray("tags");
				String tagsString = "";
				for(int i = 0; i < jsonTags.length(); i++){
					tags.add(jsonTags.getJSONObject(i).getString("tag"));
					tagsString += tags.get(i);
					if(i < jsonTags.length() - 1) tagsString += ", ";
				}
				if(tagsString.length() <= 0) tvTags.setVisibility(View.GONE);
				else{
					tvTags.setVisibility(View.VISIBLE);
					tvTags.setText(tagsString);
				}

				// Image
				String imageName = jsonRecipe.getString("image");
				if(imageName != null && !imageName.equalsIgnoreCase("null") && imageName.length() > 0){
					System.out.println("IMAGE: " + imageName + " (" + imageName.length() + ")");
					HttpsRequest request = new HttpsRequest(this, getString(R.string.url_images) + imageName, HttpsRequest.TYPE_BITMAP);
					request.execute();
				}
				else{
					mRecipe.setImage(null);
					ivImage.setVisibility(View.GONE);
				}

				// Ingredients
				ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
				JSONArray jsonIngredients = jsonRecipe.getJSONArray("ingredients");
				for(int i = 0; i < jsonIngredients.length(); i++){
					if(i > 0) llIngredients.addView(LayoutInflater.from(this.getContext()).inflate(R.layout.element_divider, llIngredients, false));
					JSONObject jsonIngredient = jsonIngredients.getJSONObject(i);
					Ingredient newIngredient = new Ingredient(jsonIngredient.getString("name"), jsonIngredient.getDouble("amount"), jsonIngredient.getString("unit"), jsonIngredient.getString("comment"));
					ingredients.add(newIngredient);

					ConstraintLayout llTemp = (ConstraintLayout)LayoutInflater.from(getContext()).inflate(R.layout.ingredients_element, llSteps, false);
					((TextView)llTemp.findViewById(R.id.tv_ingredients_name)).setText(newIngredient.getName());
					((TextView)llTemp.findViewById(R.id.tv_ingredients_amount)).setText(newIngredient.getAmount() + "");
					((TextView)llTemp.findViewById(R.id.tv_ingredients_unit)).setText(newIngredient.getUnit());
					((TextView)llTemp.findViewById(R.id.tv_ingredients_comment)).setText(newIngredient.getComment());
					llIngredients.addView(llTemp);
				}
				mRecipe.setIngredients(ingredients);

				// Steps
				ArrayList<String> steps = new ArrayList<String>();
				JSONArray jsonSteps = jsonRecipe.getJSONArray("steps");
				for(int i = 0; i < jsonSteps.length(); i++){
					if(i > 0) llSteps.addView(LayoutInflater.from(this.getContext()).inflate(R.layout.element_divider, llSteps, false));
					JSONObject jsonStep = jsonSteps.getJSONObject(i);
					steps.add(jsonStep.getString("description"));

					TextView tvNewStep = new TextView(this.getContext());
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
					lp.setMargins(0, 8, 0, 8);
					tvNewStep.setLayoutParams(lp);
					tvNewStep.setText(steps.get(i));
					llSteps.addView(tvNewStep);
				}
				mRecipe.setSteps(steps);
			}
			catch (Exception e){
				e.printStackTrace();
			}

			tvSource.setText(mRecipe.getSource());
			if(mRecipe.getImage() != null){
				ivImage.setImageDrawable(mRecipe.getImage());
				System.out.println("IMAGE: " + mRecipe.getImage().toString());
			}
		}
		else if(className.equals(Drawable.class.getName()) || className.equals(BitmapDrawable.class.getName())) {
			System.out.println("DRAWABLE-------------------");
			mRecipe.setImage((Drawable)o);
			if(mRecipe.getImage() != null){
				//ivImage.setImageDrawable(mRecipe.getImage());
				System.out.println("IMAGE+: " + mRecipe.getImage().getMinimumHeight());
			}
			else System.out.println("IMAGE-: null");
		}
		else if(className.equals(Bitmap.class.getName())) {
			System.out.println("BITMAP-------------------");
			//mRecipe.setImage((Drawable)o);
			if((Bitmap)o != null){
				ivImage.setVisibility(View.VISIBLE);
				ivImage.setImageBitmap((Bitmap)o);
				//System.out.println("IMAGE+: " + mRecipe.getImage().getMinimumHeight());
			}
			else System.out.println("IMAGE-: null");
		}
		else if(className.equals(Exception.class.getName())) {
			System.out.println("EXCEPTION: " + ((Exception)o).toString());
		}
	}

	@Override
	public void onPause(){
		super.onPause();
		System.out.println("PAUSE");
		ivImage.setImageDrawable(null);
		ivImage.setImageBitmap(null);
	}

	public void onClickAddToCart(View view){

		FileManager fm = new FileManager(this, FileManager.MODE_READ_INGREDIENTS);
		fm.execute(getContext().getFilesDir().toString(), getString(R.string.filename_cart));

		if(Button.class.isAssignableFrom(view.getClass())){
			Button b = (Button)view;
			b.setEnabled(false);
			b.setText(getString(R.string.recipe_added_to_cart));
		}
	}
}
