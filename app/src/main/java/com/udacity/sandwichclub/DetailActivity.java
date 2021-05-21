package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();
    private BillingProcessor billingProcessor;

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);
        button = findViewById(R.id.but);
        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                billingProcessor.subscribe(DetailActivity.this,"acup");

            }
        });

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }
        billingProcessor = new BillingProcessor(this,"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAimYOGnBuxZXnU5GCiXsaWdSFW3ToKhiEOB25l1GvbGAVKdOksfAfkWFbi3aFz39Xpl61Ef7K/0kmUcb2yYBA4olyW8rFhlpRtIi1s4oIm1ZIaWUZ730jnejctr8XWVEFFCtnLbh9gS1wuzB4txu5xM1mjs3rQAZ1jO7NL96s1wwoFm30a9iNPxsUcEHTF/Dho+ufvXKnAGu8/SqVm3erQFzL0sTST/AY4Yw4o2ViDxqqe2l69GlJgYu9T7ccf/ZahQM25bS4v71iD5LrRMwQjDc4528UbWn6iqJCsKeS8cCICc3Oj5CLTJ/Pb12DbvfKkbdf0/LwQpn8HDguH9zhCQIDAQAB" , null, new BillingProcessor.IBillingHandler() {
            @Override
            public void onProductPurchased(String productId, TransactionDetails details) {
                Toast.makeText(DetailActivity.this,"purchased",Toast.LENGTH_LONG);

            }

            @Override
            public void onPurchaseHistoryRestored() {

            }

            @Override
            public void onBillingError(int errorCode, Throwable error) {

            }

            @Override
            public void onBillingInitialized() {

            }
        });


        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(this, json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }




    private void populateUI(Sandwich currentSandwich) {

        TextView placeOfOriginTv = findViewById(R.id.origin_tv);
        placeOfOriginTv.setText(currentSandwich.getPlaceOfOrigin());

        TextView alsoKnownAsTv = findViewById(R.id.also_known_tv);

        int size = currentSandwich.getAlsoKnownAs().size();
        for (int j=0;  j< size; j++) {
            alsoKnownAsTv.append(currentSandwich.getAlsoKnownAs().get(j));
            if (j < size - 1) {
                alsoKnownAsTv.append("\n");
            }
        }

        TextView descriptionTv = findViewById(R.id.description_tv);
        descriptionTv.setText(currentSandwich.getDescription());


        TextView ingredientsTv = findViewById(R.id.ingredients_tv);
        size = currentSandwich.getIngredients().size();
        for (int j=0;  j< size; j++) {
            ingredientsTv.append(currentSandwich.getIngredients().get(j));
            if (j < size - 1) {
                ingredientsTv.append("\n");
            }
        }

    }
}
