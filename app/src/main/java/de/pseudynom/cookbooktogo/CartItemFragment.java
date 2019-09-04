package de.pseudynom.cookbooktogo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartItemFragment extends Fragment {
	private static String INGREDIENT_KEY = "ingredient_key";
	private static String NEW_INGREDIENT_KEY = "newIngredient_key";
	private Ingredient mIngredient = null;
	private boolean mNewIngredient = false;

	Button btnDelete = null;
	EditText etName = null;
	EditText etAmount = null;
	EditText etUnit = null;
	EditText etComment = null;
	Button btnSave = null;

	public CartItemFragment() {
		// Required empty public constructor
	}

	public static CartItemFragment newInstance(Ingredient ingredient){
		return newInstance(ingredient, false);
	}

	public static CartItemFragment newInstance(Ingredient ingredient, boolean mNewIngredient){
		CartItemFragment fragment = new CartItemFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(INGREDIENT_KEY, ingredient);
		bundle.putBoolean(NEW_INGREDIENT_KEY, mNewIngredient);
		fragment.setArguments(bundle);

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_cart_item, container, false);

		mIngredient = (Ingredient)getArguments().getSerializable(INGREDIENT_KEY);
		mNewIngredient = getArguments().getBoolean(NEW_INGREDIENT_KEY);

		btnDelete = (Button)view.findViewById(R.id.btn_cart_delete);
		if(mNewIngredient) btnDelete.setVisibility(View.GONE);
		etName = (EditText)view.findViewById(R.id.et_cart_item_edit_name);
		etName.setText(mIngredient.getName());
		etAmount = (EditText)view.findViewById(R.id.et_cart_item_edit_amount);
		etAmount.setText(mIngredient.getAmount() + "");
		etUnit = (EditText)view.findViewById(R.id.et_cart_item_edit_unit);
		etUnit.setText(mIngredient.getUnit());
		etComment = (EditText)view.findViewById(R.id.et_cart_item_edit_comment);
		etComment.setText(mIngredient.getComment());
		btnSave = (Button)view.findViewById(R.id.btn_cart_save);
		if(mNewIngredient) btnSave.setText(getString(R.string.add));

		return view;
	}

	public void onSave(View view){
		MainActivity ma = (MainActivity) getActivity();
		if(mNewIngredient){
			mIngredient.setName(etName.getText().toString());
			mIngredient.setAmount(Double.parseDouble(etAmount.getText().toString()));
			mIngredient.setUnit(etUnit.getText().toString());
			mIngredient.setComment(etComment.getText().toString());

			Bundle bundle = new Bundle();
			bundle.putSerializable(CartFragment.NEW_INGREDIENT_KEY, mIngredient);
			ma.setParentFragmentArguments(bundle);
		}
		else {
			Ingredient newIngredient = new Ingredient(etName.getText().toString(), Double.parseDouble(etAmount.getText().toString()), etUnit.getText().toString(), etComment.getText().toString());

			Bundle bundle = new Bundle();
			bundle.putSerializable(CartFragment.OLD_INGREDIENT_KEY, mIngredient);
			bundle.putSerializable(CartFragment.NEW_INGREDIENT_KEY, newIngredient);
			ma.setParentFragmentArguments(bundle);
		}
		ma.switchToParentFragment();
	}

	public void onDelete(View view){
		//mIngredient = (Ingredient)getArguments().getSerializable(INGREDIENT_KEY);
		MainActivity ma = (MainActivity)getActivity();
		Bundle bundle = new Bundle();
		bundle.putSerializable(CartFragment.DELETE_INGREDIENT_KEY, mIngredient);
		ma.setParentFragmentArguments(bundle);
		ma.switchToParentFragment();
	}
}
