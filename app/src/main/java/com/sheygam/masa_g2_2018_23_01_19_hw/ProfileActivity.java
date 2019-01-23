package com.sheygam.masa_g2_2018_23_01_19_hw;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {
    private LinearLayout editWrapper, textWrapper;
    private EditText inputName, inputLastName, inputEmail, inputPhone;
    private TextView nameTxt, lastNameTxt, emailTxt, phoneTxt;
    private String name, lastName, email, phone;
    private String currId;
    private MenuItem editItem, doneItem, logoutItem;
    private boolean isEdit = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadId();
        loadData();
        setContentView(R.layout.activity_profile);
        editWrapper = findViewById(R.id.edit_wrapper);
        textWrapper = findViewById(R.id.text_wrapper);
        inputName = findViewById(R.id.input_name);
        inputLastName = findViewById(R.id.input_last_name);
        inputEmail = findViewById(R.id.input_email);
        inputPhone = findViewById(R.id.input_phone);
        nameTxt = findViewById(R.id.name_txt);
        lastNameTxt = findViewById(R.id.last_name_txt);
        emailTxt = findViewById(R.id.email_txt);
        phoneTxt = findViewById(R.id.phone_txt);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        logoutItem = menu.findItem(R.id.logout_item);
        editItem = menu.findItem(R.id.edit_item);
        doneItem = menu.findItem(R.id.done_item);
        showPreviewMode();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout_item){
            logout();
        }else if(item.getItemId() == R.id.edit_item){
            showEditMode();
        }else if(item.getItemId() == R.id.done_item){
            name = inputName.getText().toString();
            lastName = inputLastName.getText().toString();
            email = inputEmail.getText().toString();
            phone = inputPhone.getText().toString();
            saveData();
            showPreviewMode();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(isEdit){
            showPreviewMode();
        }else {
            super.onBackPressed();
        }
    }

    private void loadId(){
        currId = getSharedPreferences("AUTH",MODE_PRIVATE)
                .getString("CURR",null);
        if(currId == null){
            Toast.makeText(this, "Something went wrong! Login again!", Toast.LENGTH_SHORT).show();
            logout();
        }
    }

    private void showEditMode(){
        editItem.setVisible(false);
        logoutItem.setVisible(false);
        doneItem.setVisible(true);
        inputName.setText(name);
        inputLastName.setText(lastName);
        inputEmail.setText(email);
        inputPhone.setText(phone);
        textWrapper.setVisibility(View.GONE);
        editWrapper.setVisibility(View.VISIBLE);
        isEdit = true;
    }

    private void showPreviewMode(){
        editItem.setVisible(true);
        doneItem.setVisible(false);
        logoutItem.setVisible(true);
        nameTxt.setText(name);
        lastNameTxt.setText(lastName);
        emailTxt.setText(email);
        phoneTxt.setText(phone);
        editWrapper.setVisibility(View.GONE);
        textWrapper.setVisibility(View.VISIBLE);
        isEdit = false;
    }

    private void loadData(){
        SharedPreferences sp = getSharedPreferences("DATA", MODE_PRIVATE);
        String res = sp.getString(currId,null);
        if(res == null){
            name = "";
            lastName = "";
            email = "";
            phone = "";
        }else{
            String[] arr = res.split(",");
            name = arr[0];
            lastName = arr[1];
            email = arr[2];
            phone = arr[3];
        }
    }

    private boolean validator(){
        return !name.isEmpty()
                && !lastName.isEmpty()
                && !email.isEmpty()
                && !phone.isEmpty();
    }

    private void saveData(){
        if(validator()){
            String res = name + "," + lastName + "," + email + "," + phone;
            boolean res1 = getSharedPreferences("DATA",MODE_PRIVATE)
                    .edit()
                    .putString(currId,res)
                    .commit();
        }else{
            Toast.makeText(this, "All fields need by fill!", Toast.LENGTH_SHORT).show();
        }
    }

    private void logout(){
        boolean res = getSharedPreferences("AUTH",MODE_PRIVATE)
                .edit()
                .remove("CURR")
                .commit();
        setResult(RESULT_OK);
        finish();
    }
}
