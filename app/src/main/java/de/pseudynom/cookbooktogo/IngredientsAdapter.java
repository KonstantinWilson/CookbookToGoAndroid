package de.pseudynom.cookbooktogo;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.time.format.TextStyle;
import java.util.ArrayList;

import de.pseudynom.cookbooktogo.Ingredient;
import de.pseudynom.cookbooktogo.R;

public class IngredientsAdapter extends ArrayAdapter<Ingredient> {
	public IngredientsAdapter(Context context, ArrayList<Ingredient> ingredients){
		super(context, 0, ingredients);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Ingredient ingredient = getItem(position);

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.ingredients_adapter, parent, false);
		}

		TextView tvName = (TextView)convertView.findViewById(R.id.tv_ingredients_name);
		tvName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12.0f);
		TextView tvAmount = (TextView)convertView.findViewById(R.id.tv_ingredients_amount);
		tvAmount.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12.0f);
		TextView tvUnit = (TextView)convertView.findViewById(R.id.tv_ingredients_unit);
		tvUnit.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12.0f);
		TextView tvComment = (TextView)convertView.findViewById(R.id.tv_ingredients_comment);
		tvComment.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12.0f);

		tvName.setText(ingredient.getName());
		tvAmount.setText(ingredient.getAmount() + "");
		tvUnit.setText(ingredient.getUnit());
		tvComment.setText(ingredient.getComment());

		return convertView;
	}
}
