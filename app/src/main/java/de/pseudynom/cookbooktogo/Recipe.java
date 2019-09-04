package de.pseudynom.cookbooktogo;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Konstantin on 26.10.2018.
 */

public class Recipe implements Serializable{
	private int id = 0;
	private String name = null;
	private String creator = null;
	private String source = null;
	private ArrayList<String> tags = null;
	private ArrayList<Ingredient> ingredients = null;
	private ArrayList<String> steps = null;
	private Drawable image;

	public Recipe(int id, String name){
		this.id = id;
		this.name = name;
	}

	public Recipe(int id, String name, String creator){
		this.id = id;
		this.name = name;
		this.creator = creator;
	}

	public Recipe(int id, String name, ArrayList<String> tags){
		this.id = id;
		this.name = name;
		this.creator = creator;
		this.tags = tags;
	}

	public Recipe(int id, String name, String creator, ArrayList<String> tags){
		this.id = id;
		this.name = name;
		this.creator = creator;
		this.tags = tags;
	}

	public Recipe(int id, String name, String creator, ArrayList<String> tags, ArrayList<Ingredient> ingredients, ArrayList<String> steps){
		this.id = id;
		this.name = name;
		this.creator = creator;
		this.tags = tags;
		this.ingredients = ingredients;
		this.steps = steps;
	}

	public Recipe(int id, String name, String creator, ArrayList<String> tags, ArrayList<Ingredient> ingredients, ArrayList<String> steps, Drawable image){
		this.id = id;
		this.name = name;
		this.creator = creator;
		this.tags = tags;
		this.ingredients = ingredients;
		this.steps = steps;
		this.image = image;
	}

	public int getId(){
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public ArrayList<String> getTags() {
		return tags;
	}

	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}

	public ArrayList<Ingredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(ArrayList<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}

	public ArrayList<String> getSteps() {
		return steps;
	}

	public void setSteps(ArrayList<String> steps) {
		this.steps = steps;
	}

	public Drawable getImage() {
		return image;
	}

	public void setImage(Drawable image) {
		this.image = image;
	}
}
