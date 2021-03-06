package com.example.onlinefreelaceapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.onlinefreelaceapp.DataBase.DBHelper;
import com.example.onlinefreelaceapp.adapter.GigHolder;

public class ReviewOrder extends AppCompatActivity {

    EditText email,requirement,driveLink;
    TextView total_price,subtotal,service_charge,title,description,byseller;
    Button addOrder;
    DBHelper DB;
    ImageView imageView;

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
        title = (TextView) findViewById(R.id.gigtitleorderreview);
        description = (TextView) findViewById(R.id.gigdescriptionorder);
        byseller = (TextView) findViewById(R.id.orderBySeller);
        imageView = (ImageView) findViewById(R.id.gigimageorder);

        DB = new DBHelper(this);


        int gigidfromIntent = Integer.parseInt(getIntent().getStringExtra("gigid"));

         System.out.println(gigidfromIntent);

        final GigHolder gigHolder = DB.getGigsFromPrimaryKey(gigidfromIntent);

        imageView.setImageURI(gigHolder.getImage());

        byseller.setText("Gig Posted By "+getIntent().getStringExtra("seller"));

        subtotal.setText(getIntent().getStringExtra("amountOne"));
        service_charge.setText(getIntent().getStringExtra("amountTwo"));

        int amount_one = Integer.parseInt(getIntent().getStringExtra("amountOne"));
        int amount_two = Integer.parseInt(getIntent().getStringExtra("amountTwo"));
        int total_amount = getTotalPrice(amount_one,amount_two);

        String totalAmount = Integer.toString(total_amount);

        total_price.setText(totalAmount);

      //  imageView.setImageURI(getIntent().getStringExtra("image"));

        title.setText(getIntent().getStringExtra("gigTitle"));
        description.setText(getIntent().getStringExtra("desciption"));


        addOrder = (Button) findViewById(R.id.btnAddOrder);

        Intent intentUsername = getIntent();
        String user = intentUsername.getStringExtra("username");



          String txtEmail = DB.getUserEmailAddress(getIntent().getStringExtra("username"));
          email.setText(txtEmail);

        //String txtEmail = DB.getUserEmail(user);

        //email.setText(txtEmail);

        addOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = getIntent();
                String buyer = intent1.getStringExtra("username");
                String seller = getIntent().getStringExtra("seller");
             //   int gigId = 12;
                int gigId = Integer.parseInt(getIntent().getStringExtra("gigid"));
                String workingEmail = email.getText().toString();
                String orderReq = requirement.getText().toString();
                String resourceLink = driveLink.getText().toString();

                long publishDate = System.currentTimeMillis();


                int subTotal = Integer.parseInt(subtotal.getText().toString());
                int serviceCharge = Integer.parseInt(service_charge.getText().toString());

                int totPrice = getTotalPrice(subTotal,serviceCharge);

                Orders order = new Orders(subTotal,serviceCharge,totPrice,gigId,workingEmail,resourceLink,orderReq,buyer,seller,publishDate,0);



                if(order.getWork_email().equals("")) {
                    Toast.makeText(ReviewOrder.this,"Please Enter Working Email",Toast.LENGTH_SHORT).show();
                }else if(order.getReq().equals("")) {
                    Toast.makeText(ReviewOrder.this,"Please Enter Order Requirement",Toast.LENGTH_SHORT).show();
                }else {

                    if(URLUtil.isValidUrl(order.getResource()) && Patterns.WEB_URL.matcher(order.getResource()).matches() || order.getResource().equals("")) {
                        Boolean insert = DB.insertOrder(order);
                        if (insert == true) {
                            Toast.makeText(ReviewOrder.this, "Order Successful!", Toast.LENGTH_SHORT).show();
                            Intent intentPaymentPage = new Intent(getApplicationContext(), OrderSuccess.class);
                            intentPaymentPage.putExtra("username",getIntent().getStringExtra("username"));
                            intentPaymentPage.putExtra("buyerUsername", order.getBuyer());
                            intentPaymentPage.putExtra("sellerUsername", order.getSeller());
                            intentPaymentPage.putExtra("paymentTotal", order.getTotal());
                            intentPaymentPage.putExtra("PaymentSub", order.getSubTot());
                            intentPaymentPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

    public int getTotalPrice(int a,int b) {
        return a+b;
    }




}