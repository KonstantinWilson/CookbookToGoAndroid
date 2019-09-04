package de.pseudynom.cookbooktogo;

import java.io.Serializable;

/**
 * Created by Konstantin on 26.10.2018.
 */

public class Ingredient implements Serializable {
	private String name;
	private double amount;
	private String unit;
	private String comment;

	public Ingredient(String name, double amount, String unit){
		this.name = name;
		this.amount = amount;
		this.unit = unit;
	}

	public Ingredient(String form) throws IngredientException{
		String[] split = form.split("\t", -1);
		if(split.length != 4) throw new IngredientException("Wrong format: " + form);
		try{
			this.name = split[0];
			this.unit = split[1];
			this.amount = Double.parseDouble(split[2]);
			this.comment = split[3];
		}catch (Exception e){
			throw new IngredientException("Number conversion error");
		}
	}

	public Ingredient(String name, double amount, String unit, String comment){
		this.name = name;
		this.amount = amount;
		this.unit = unit;
		this.comment = comment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String format(){
		return String.format("%s\t%s\t%s\t%s", this.name, this.unit, Double.toString(this.amount), this.comment);
	}

	@Override
	public boolean equals(Object obj) {
		if(!obj.getClass().getName().equals(Ingredient.class.getName())) return false;
		Ingredient i = (Ingredient)obj;
		if(this.name.equalsIgnoreCase(i.getName()) && this.unit.equalsIgnoreCase(i.getUnit())) return true;
		return false;
	}

	public boolean deepEquals(Ingredient i){
		if(this.name.equalsIgnoreCase(i.getName())
				&& this.amount == i.getAmount()
				&& this.unit.equalsIgnoreCase(i.getUnit())
				&& this.comment.equalsIgnoreCase(i.getComment())) return true;
		return false;
	}

	public boolean add(Ingredient ingredient){
		if(equals(ingredient)){
			this.amount += ingredient.getAmount();
			if(this.comment.length() > 0 && ingredient.getComment().length() > 0) this.comment += ", ";
			this.comment += ingredient.getComment();
			return true;
		}
		return false;
	}
}
