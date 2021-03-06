package com.example.onlinefreelaceapp.Common.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.onlinefreelaceapp.Become_A_Seller;
import com.example.onlinefreelaceapp.Common.RequestPostUserView;
import com.example.onlinefreelaceapp.DataBase.DBHelper;
import com.example.onlinefreelaceapp.HelperClasses.Constants;
import com.example.onlinefreelaceapp.HelperClasses.PrefManager;
import com.example.onlinefreelaceapp.R;
import com.example.onlinefreelaceapp.UserDashboard;

public class Login extends AppCompatActivity {


    Button tempbt;
    EditText usernameLogin,passwordLogin;
    Button btnLogin,createAccount;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);


        //tempbt=findViewById(R.id.temporybtn);
        usernameLogin = (EditText) findViewById(R.id.loginUserName);
        passwordLogin = (EditText) findViewById(R.id.loginPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        createAccount = (Button) findViewById(R.id.createAccountPage);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SignUp.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        DB = new DBHelper(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = usernameLogin.getText().toString();
                String pass = passwordLogin.getText().toString();

                if(username.equals("")||pass.equals("")) {
                    Toast.makeText(Login.this,"Please Enter Required Fields",Toast.LENGTH_SHORT).show();
                }else {
                    Boolean check = DB.checkUsernamePassword(username,pass);
                    if(check==true) {
                        new PrefManager(Login.this).setPreference(Constants.CURRENT_USER,DB.getFullName(username));
                        Toast.makeText(Login.this,"Login Successful",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), UserDashboard.class);
                        intent.putExtra("username",username);
                        intent.putExtra("password",pass);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }else {
                        Toast.makeText(Login.this,"Login Failed",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }


}