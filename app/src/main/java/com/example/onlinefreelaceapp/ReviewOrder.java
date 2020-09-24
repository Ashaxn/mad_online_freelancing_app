package com.example.onlinefreelaceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinefreelaceapp.DataBase.DBHelper;

import java.net.MalformedURLException;
import java.net.URL;

public class ReviewOrder extends AppCompatActivity {

    EditText email,requirement,driveLink;
    TextView total_price,subtotal,service_charge;
    Button addOrder;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_order);

        email = (EditText) findViewById(R.id.txtWorkingEmailOrder);
        requirement = (EditText) findViewById(R.id.txtOrderRequirement);
        driveLink = (EditText) findViewById(R.id.linkOrderResource);
        total_price = (TextView) findViewById(R.id.txtOrderTotal);
        subtotal = (TextView) findViewById(R.id.txtOrderSubTotal);
        service_charge = (TextView) findViewById(R.id.txtOrderServiceCharge);

        addOrder = (Button) findViewById(R.id.btnAddOrder);

        Intent intentUsername = getIntent();
        String user = intentUsername.getStringExtra("username");

        DB = new DBHelper(this);

//        String txtEmail = DB.getUserEmail(user);

  //      email.setText(txtEmail);

        addOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = getIntent();
                String buyer = intent1.getStringExtra("username");
                String seller = "charuka11";
                int gigId = 12;
                String workingEmail = email.getText().toString();
                String orderReq = requirement.getText().toString();
                String resourceLink = driveLink.getText().toString();


                int totPrice = Integer.parseInt(total_price.getText().toString());
                int subTotal = Integer.parseInt(subtotal.getText().toString());
                int serviceCharge = Integer.parseInt(service_charge.getText().toString());

                if(workingEmail.equals("")) {
                    Toast.makeText(ReviewOrder.this,"Please Enter Working Email",Toast.LENGTH_SHORT).show();
                }else if(orderReq.equals("")) {
                    Toast.makeText(ReviewOrder.this,"Please Enter Order Requirement",Toast.LENGTH_SHORT).show();
                }else {

                    if(URLUtil.isValidUrl(resourceLink) && Patterns.WEB_URL.matcher(resourceLink).matches() || resourceLink.equals("")) {
                        Boolean insert = DB.insertOrder(workingEmail, orderReq, resourceLink, subTotal, totPrice, serviceCharge, buyer, seller, gigId);
                        if (insert == true) {
                            Toast.makeText(ReviewOrder.this, "Order Successful!", Toast.LENGTH_SHORT).show();
                            Intent intentPaymentPage = new Intent(getApplicationContext(), OrderSuccess.class);
                            intentPaymentPage.putExtra("username",getIntent().getStringExtra("username"));
                            intentPaymentPage.putExtra("buyerUsername", buyer);
                            intentPaymentPage.putExtra("sellerUsername", seller);
                            intentPaymentPage.putExtra("paymentTotal", totPrice);
                            intentPaymentPage.putExtra("PaymentSub", subTotal);
                            startActivity(intentPaymentPage);
                        } else {
                            Toast.makeText(ReviewOrder.this, "Order Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(ReviewOrder.this, "Please Enter Valid URL!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }



}