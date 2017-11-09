package com.ivoanic.simpleweather;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity {
    @BindView(R.id.editTxtCity)
    EditText editTextView;
    @BindView(R.id.radioGroupUnits)
    RadioGroup radioGroupUnits;
    @BindView(R.id.radioButtonC)
    RadioButton radioButtonC;
    @BindView(R.id.radioButtonF)
    RadioButton radioButtonF;
    @BindView(R.id.linearLayoutMain)
    LinearLayout mainLinearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        readSettings(mainLinearLayout);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                saveSettings(editTextView);
                Toast.makeText(SettingsActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                NavUtils.navigateUpFromSameTask(this);
                return true;
            // Respond to a click on the "Back" btn
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveSettings (View view){
        String editText = editTextView.getText().toString();
        int radioButtonUnit = radioGroupUnits.indexOfChild(findViewById(radioGroupUnits.getCheckedRadioButtonId()));
        String units = "";
        switch (radioButtonUnit){
            case 0:
                units="c";
                break;
            case 1:
                units="f";
                break;
        }

        // Remove spaces from input text
        editText = editText.replaceAll("\\s+","");
        SharedPreferences sharedPrefs = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = sharedPrefs.edit();
        mEditor.putString("neznam",editText);
        mEditor.putString("units", units);
        mEditor.apply();
    }
    public void readSettings (View view){
        int index=-1;
        SharedPreferences sharedPrefs = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String city = sharedPrefs.getString("neznam","");
        String units = sharedPrefs.getString("units","");
        switch (units){
            case "c":
                index = 0;
                break;
            case "f":
                index = 1;
                break;
        }
        ((RadioButton) ((RadioGroup)findViewById(R.id.radioGroupUnits)).getChildAt(index)).setChecked(true);
        editTextView.setText(city);

    }

}
