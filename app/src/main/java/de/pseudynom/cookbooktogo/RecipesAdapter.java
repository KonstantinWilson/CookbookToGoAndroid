package de.pseudynom.cookbooktogo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Konstantin on 26.10.2018.
 */

public class RecipesAdapter extends ArrayAdapter<Recipe>{
	public RecipesAdapter(Context context, ArrayList<Recipe> recipes){
		super(context, 0, recipes);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		Recipe recipe = getItem(position);

		if(convertView == null){
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.recipes_adapter, parent, false);
		}

		TextView tvName = (TextView)convertView.findViewById(R.id.tv_recipes_name);
		TextView tvTags = (TextView)convertView.findViewById(R.id.tv_recipes_tags);

		tvName.setText(recipe.getName());
		if(recipe.getTags() != null){
			String tags = "";
			ArrayList<String> tagList = recipe.getTags();
			for(int i = 0; i < tagList.size() && i < 10; i++){
				tags += tagList.get(i);
				if(i < tagList.size() - 1) tags += ", ";
			}
			tvTags.setText(tags);
		}
		else{
			tvTags.setText("");
		}

		return convertView;
	}
}