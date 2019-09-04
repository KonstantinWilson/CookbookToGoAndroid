package de.pseudynom.cookbooktogo;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class FileManager extends AsyncTask<Object, Void, Object> {
	public static final int MODE_READ_INGREDIENTS = 1;
	public static final int MODE_WRITE_INGREDIENTS = 2;
	public static final int MODE_APPEND_INGREDIENTS = 3;

	private int mode = 0;
	private Responsable responsable;

	public FileManager(Responsable responsable, int mode){
		this.responsable = responsable;
		this.mode = mode;
	}

	@Override
	public Object doInBackground(Object... args){
		if(this.mode == FileManager.MODE_READ_INGREDIENTS){
			if(args.length != 2
					|| !args[0].getClass().getName().equals(String.class.getName())
					|| !args[1].getClass().getName().equals(String.class.getName())){
				return null;
			}

			return readIngredients((String)args[0], (String)args[1]);
		}
		else if(this.mode == FileManager.MODE_WRITE_INGREDIENTS){
			if(args.length != 3
					|| !String.class.isAssignableFrom(args[0].getClass())
					|| !String.class.isAssignableFrom(args[1].getClass())
					|| !ArrayList.class.isAssignableFrom(args[2].getClass())){
				return null;
			}
			return this.writeIngredients((String)args[0], (String)args[1], (ArrayList<Ingredient>) args[2]);
		}
		return null;
	}

	@Override
	protected void onPostExecute(Object result){
		responsable.response(result);
	}

	public Object readIngredients(String directory, String filename){
		File f = new File(directory, filename);
		ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();

		try{
			if(!f.exists()) f.createNewFile();
			FileInputStream fis = new FileInputStream(f);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line = null;
			do{
				line = br.readLine();

				if(line != null){
					try{
						Ingredient i = new Ingredient(line);
						ingredients.add(i);
					}
					catch (IngredientException ie){

					}
				}
			}while(line != null);
			br.close();
			fis.close();
		}
		catch (Exception e){
			e.printStackTrace();
			return e;
		}

		return ingredients;
	}

	public Boolean writeIngredients(String directory, String filename, ArrayList<Ingredient> ingredients){
		System.out.println("WRITE " + ingredients.size() + " LINES");
		File fCart = new File(directory, filename);
		try{
			if(!fCart.exists()) fCart.createNewFile();

			FileOutputStream fos = new FileOutputStream(fCart);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

			for(int i = 0; i < ingredients.size(); i++){
				Ingredient ing = ingredients.get(i);
				bw.write(String.format("%s\t%s\t%s\t%s", ing.getName(), ing.getUnit(), Double.toString(ing.getAmount()), ing.getComment()));
				bw.newLine();
				bw.flush();
			}
			bw.close();
			fos.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return new Boolean(true);
	}

	/*
	public static void appendIngredients(String directory, String filename, ArrayList<Ingredient> ingredients){
		File fCart = new File(directory, filename);
		try{
			if(!fCart.exists()) fCart.createNewFile();

			FileOutputStream fos = new FileOutputStream(fCart);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

			for(int i = 0; i < ingredients.size(); i++){
				Ingredient ing = ingredients.get(i);
				bw.append(String.format("%s\t%s\t%s\t%s", ing.getName(), ing.getUnit(), Double.toString(ing.getAmount()), ing.getComment()));
				bw.newLine();
				bw.flush();
			}
			bw.close();
			fos.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	*/
}
