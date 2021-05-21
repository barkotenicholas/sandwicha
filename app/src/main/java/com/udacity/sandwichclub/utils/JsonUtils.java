package com.udacity.sandwichclub.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.udacity.sandwichclub.R;
import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    private static final String LOG_TAG = JsonUtils.class.getSimpleName();

    /**
     * Return the Sandwich object after parsing the json and extracting information from it.
     * @param context of the calling object
     * @param json consisting of json string
     **/
    public static Sandwich parseSandwichJson(Context context, String json) {

        //Create an empty Sandwich object
        //TODO : Default sandwich image, in case no network
        Sandwich details = new Sandwich();

        //Try to parse the Json. In case of errors, a JSONException may be thrown.
        //Catch it and print the details and handle the scenario
        try{
            JSONObject root = new JSONObject(json);

            //1. Retrieve, set the name details
            JSONObject name = root.optJSONObject("name");
            details.setMainName(name.optString("mainName"));

            //Retrieve, set the AKA details - AKA is a list
            List<String> akaList = new ArrayList<>();
            JSONArray akaJsonArray =
                    name.getJSONArray("alsoKnownAs");
            for (int i=0; i<akaJsonArray.length(); i++){
                akaList.add(akaJsonArray.get(i).toString());
            }
            details.setAlsoKnownAs(akaList);

            //2. Retrieve, set the place of origin details
            details.setPlaceOfOrigin(root.optString("placeOfOrigin"));

            //3. Retrieve, set the description details
            details.setDescription(root.optString("description"));

            //4. Retrieve, set the image details
            details.setImage(root.optString("image"));

            //5. Retrieve and set the ingredients details  - it is a list
            List<String> ingredientsList = new ArrayList<String>();
            JSONArray ingredientsJsonArray =
                    root.optJSONArray("ingredients");
            for (int i=0; i<ingredientsJsonArray.length(); i++) {
                ingredientsList.add(ingredientsJsonArray.getString(i));
            }
            details.setIngredients(ingredientsList);

        }catch (JSONException e) {
            //This prevents the app from crashing
            //Without the Json parsing, we cannot show the detailed activity
            Log.e(LOG_TAG, "Problem when parsing the Json information, e");
            e.printStackTrace();
            //TODO - need to test
            Toast.makeText(context, "Uh-oh, there was a problem parsing the details of the sandwich",
                    Toast.LENGTH_SHORT).show();
            return null;
        }
        return details;
    }
}
