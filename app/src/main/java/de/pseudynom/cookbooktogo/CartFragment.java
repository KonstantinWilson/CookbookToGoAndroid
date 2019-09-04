package de.pseudynom.cookbooktogo;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment implements Responsable{
	public static String DELETE_INGREDIENT_KEY = "delete_ingredient_key";
	public static String OLD_INGREDIENT_KEY = "old_ingredient_key";
	public static String NEW_INGREDIENT_KEY = "new_ingredient_key";
	private Ingredient mDeleteIngredient;
	private Ingredient mOldIngredient;
	private Ingredient mNewIngredient;

	private LinearLayout llCartList = null;
	private ArrayList<Ingredient> alList = null;
	private CartItemFragment cartItemFragment = null;


	public CartFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		FileManager fm = new FileManager(this, FileManager.MODE_READ_INGREDIENTS);
		fm.execute(getContext().getFilesDir().toString(), getString(R.string.filename_cart));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		cartItemFragment = null;
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_cart, container, false);

		if(getArguments() != null) {
			mDeleteIngredient = (Ingredient) getArguments().getSerializable(DELETE_INGREDIENT_KEY);
			mOldIngredient = (Ingredient) getArguments().getSerializable(OLD_INGREDIENT_KEY);
			mNewIngredient = (Ingredient) getArguments().getSerializable(NEW_INGREDIENT_KEY);
		}

		llCartList = (LinearLayout)view.findViewById(R.id.ll_cart_list);

		if(mDeleteIngredient == null && mOldIngredient == null && mNewIngredient == null){
		}
		else{
			if(mDeleteIngredient != null){
				deleteIngredient(mDeleteIngredient);
			}
			else if(mOldIngredient != null && mNewIngredient != null){
				replaceIngredient(mOldIngredient, mNewIngredient);
			}
			else if(mOldIngredient == null && mNewIngredient != null){
				addIngredient(mNewIngredient);
			}
		}
		if(alList != null) fillList();

		return view;
	}

	public void replaceIngredient(Ingredient oldI, Ingredient newI){
		for(int i = 0; i < alList.size(); i++){
			if(alList.get(i).deepEquals(oldI)){
				alList.set(i, newI);
			}
		}
		FileManager fm = new FileManager(this,FileManager.MODE_WRITE_INGREDIENTS);
		fm.execute(getContext().getFilesDir().toString(), getString(R.string.filename_cart), alList);
	}

	public void deleteIngredient(Ingredient deleteI){
		for(int i = 0; i < alList.size(); i++){
			if(alList.get(i).deepEquals(deleteI)){
				alList.remove(i);
			}
		}
		FileManager fm = new FileManager(this,FileManager.MODE_WRITE_INGREDIENTS);
		fm.execute(getContext().getFilesDir().toString(), getString(R.string.filename_cart), alList);
	}

	public void addIngredient(Ingredient newI){
		alList.add(newI);
		FileManager fm = new FileManager(this,FileManager.MODE_WRITE_INGREDIENTS);
		fm.execute(getContext().getFilesDir().toString(), getString(R.string.filename_cart), alList);
	}

	public void onDelete(View view){
		ConstraintLayout clListItem = (ConstraintLayout)view.getParent();
		deleteIngredient((Ingredient)clListItem.getTag());
		fillList();
	}

	private void fillList(){
		llCartList.removeAllViews();
		for (int i = 0; i < alList.size(); i++) {
			Ingredient ingredient = alList.get(i);

			if (i > 0)
				llCartList.addView(LayoutInflater.from(this.getContext()).inflate(R.layout.element_divider, llCartList, false));

			ConstraintLayout clListItem = (ConstraintLayout) LayoutInflater.from(getContext()).inflate(R.layout.cart_element, llCartList, false);
			clListItem.setTag(ingredient);

			TextView etName = (TextView) clListItem.findViewById(R.id.tv_cart_name);
			etName.setText(ingredient.getName());
			TextView etAmount = (TextView) clListItem.findViewById(R.id.tv_cart_amount);
			etAmount.setText(ingredient.getAmount() + "");
			TextView etUnit = (TextView) clListItem.findViewById(R.id.tv_cart_unit);
			etUnit.setText(ingredient.getUnit());
			TextView etComment = (TextView) clListItem.findViewById(R.id.tv_cart_comment);
			etComment.setText(ingredient.getComment());

			clListItem.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View view) {
					ArrayList<Fragment> oldFragment = (ArrayList<Fragment>) getActivity().getSupportFragmentManager().getFragments();
					FragmentActivity fa = getActivity();
					if (fa != null && oldFragment.size() > 0) {
						cartItemFragment = CartItemFragment.newInstance((Ingredient) view.getTag());
						fa.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, cartItemFragment).addToBackStack(null).commit();
						((MainActivity) fa).setParentFragment(oldFragment.get(0));
					}
					return false;
				}
			});
			llCartList.addView(clListItem);
		}
	}

	@Override
	public void response(Object o) {
		if(Exception.class.isAssignableFrom(o.getClass())){
			((Exception)o).printStackTrace();
		}
		else if(ArrayList.class.isAssignableFrom(o.getClass())) {
			alList = (ArrayList<Ingredient>) o;

			if(llCartList != null) fillList();
		}
	}

	public void onAddIngredient(View view){
		ArrayList<Fragment> oldFragment = (ArrayList<Fragment>) getActivity().getSupportFragmentManager().getFragments();
		FragmentActivity fa = getActivity();
		if (fa != null && oldFragment.size() > 0) {
			cartItemFragment = CartItemFragment.newInstance(new Ingredient("", 0.0, ""), true);
			fa.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, cartItemFragment).addToBackStack(null).commit();
			((MainActivity) fa).setParentFragment(oldFragment.get(0));
		}
	}

	public CartItemFragment getCartItemFragment() {
		return cartItemFragment;
	}
}
