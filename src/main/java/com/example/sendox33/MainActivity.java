package com.example.sendox33;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText userName;
    private EditText emailAddressField;
    private EditText passwordField;
    private EditText confirmPasswordField;
    private Button signUp;
    private Button existingUser;
    private int counter=5;
    Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        firstName = (EditText)findViewById(R.id.firstNameLabel);
        lastName = (EditText)findViewById(R.id.lastNameLabel);
        userName = (EditText)findViewById(R.id.userNameLabel);
        emailAddressField = (EditText)findViewById(R.id.emailAddressLabel);
        passwordField = (EditText)findViewById(R.id.passwordLabel);
        confirmPasswordField = (EditText)findViewById(R.id.confirmPasswordLabel);
        signUp = (Button) findViewById(R.id.signUpButton);
        existingUser = (Button) findViewById(R.id.loginPageLabel);
        Log.d("my Tag" , "before setOnClickListener");

        SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
        String temp0 = sp.getString("username", null);
        System.out.println("temp0 = " + temp0);
        if(temp0!=null)redirectToUserPage();;

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                redirectToUserPage();
//                Log.d("my Tag" , "before newClient");
//                client = new Client("10.0.2.2",5050);
//                Log.d("my Tag" , "after newClient");
//                String temp = client.sendQuery("fsome randomshityymessage");
//                Log.d("my Tag" , "after randomshutttymessage");
//                redirectToUserPage();

                signUp(firstName.getText().toString(),
                        lastName.getText().toString(),
                        userName.getText().toString(),
                        emailAddressField.getText().toString(),
                        passwordField.getText().toString(),
                        confirmPasswordField.getText().toString());
            }
        });
    }

    private void signUp(String firstName,String lastName,String username,String emailAddress,String password,String confirmPassword){
        if(!password.equals(confirmPassword)){
            passwordField = (EditText)findViewById(R.id.userNameLabel);
            passwordField.setHint("password should be equalt to Confirmpassword ");
            passwordField.setHintTextColor(Color.rgb(140,10,10));
            confirmPasswordField = (EditText)findViewById(R.id.userNameLabel);
            confirmPasswordField.setHint("password should be equalt to Confirmpassword");
            confirmPasswordField.setHintTextColor(Color.rgb(140,10,10));
        }
        try {
            client = new Client("10.145.126.103" , 5050);
            String query = "SignUp?" + "firstName="+ firstName + "&lastName=" + lastName + "&username=" + username + "&emailAddress=" + emailAddress + "&password=" + SHA256(password);
            String response = client.sendQuery(query);

            String[] arrOfStr;
            if(response.contains("?")){
                arrOfStr = response.split("\\?", 2);

                HashMap<String, String> signin_values = new HashMap<>();
                signin_values.put("status", arrOfStr[0]);
                String[] secondArrOfStr = arrOfStr[1].split("&");
                for(int i=0; i< secondArrOfStr.length; i++){
                    // System.out.println("secondArrOfStr[i] = " + secondArrOfStr[i]);
                    String[] temp = secondArrOfStr[i].split("=",2);
                    if(temp.length >=2){
                        signin_values.put(temp[0],temp[1]);
                    }
                    // System.out.println(hashMap.get("password"));
                }
                System.out.println("status = " + signin_values.get("status"));
                System.out.println("type = " + signin_values.get("type"));

                if( signin_values!=null ){
                    if (signin_values.get("status").equals("fail")) {
                        //Unsucessful SignUp!
                        if(signin_values.get("type").equals("user name already exists")){
                            System.out.println("entered case where user name already exists!");
                            userName = (EditText)findViewById(R.id.userNameLabel);
                            userName.setHint("user name already exist");
                            userName.setText("");
                            userName.setHintTextColor(Color.rgb(140,10,10));
                        }else if(signin_values.get("type").equals("incorrect email")){
                            System.out.println("entered case where incorrect email!");
                            emailAddressField = (EditText)findViewById(R.id.emailAddressLabel);
                            emailAddressField.setHint("incorrect email");
                            emailAddressField.setText("");
                            emailAddressField.setHintTextColor(Color.rgb(140,10,10));
                        }else{
                            System.out.println("entered case where failure but not specified type!");

                            ((EditText)findViewById(R.id.firstNameLabel)).setHintTextColor(Color.rgb(140,10,10));
                            ((EditText)findViewById(R.id.lastNameLabel)).setHintTextColor(Color.rgb(140,10,10));
                            ((EditText)findViewById(R.id.emailAddressLabel)).setHintTextColor(Color.rgb(140,10,10));
                            ((EditText)findViewById(R.id.userNameLabel)).setHintTextColor(Color.rgb(140,10,10));
                            ((EditText)findViewById(R.id.passwordLabel)).setHintTextColor(Color.rgb(140,10,10));
                            ((EditText)findViewById(R.id.confirmPasswordLabel)).setHintTextColor(Color.rgb(140,10,10));

                            ((EditText)findViewById(R.id.firstNameLabel)).setText("");
                            ((EditText)findViewById(R.id.lastNameLabel)).setText("");
                            ((EditText)findViewById(R.id.emailAddressLabel)).setText("");
                            ((EditText)findViewById(R.id.userNameLabel)).setText("");
                            ((EditText)findViewById(R.id.passwordLabel)).setText("");
                            ((EditText)findViewById(R.id.confirmPasswordLabel)).setText("");
                        }
                    }else if (signin_values.get("status").equals("success")) {
                        System.out.println("entered case where success");
                        SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
                        SharedPreferences.Editor ed = sp.edit();
                        ed.putString("username", username);
                        ed.putString("hashedPassword", SHA256(password));
                        ed.putString("firstName", firstName);
                        ed.putString("lastName", lastName);
                        ed.putString("emailAddress", emailAddress);
                        ed.commit();
                        RSAgenerator key = new RSAgenerator();
                        RSAPublicKey pu = key.pub;
                        RSAPrivateKey pr = key.pri;
                        ed.putString("n", pr.getmodulus().toString());
                        ed.putString("e", pr.getexponent().toString());
                        ed.putString("d", pr.getd().toString());
                        ed.commit();
                        client = new Client("10.145.126.103" , 5050);
                        String query2 = "addpublic_key?" + "n="+ pu.getmodulus().toString() + "&e=" + pu.getexponent().toString() + "&username=" + username;
                        String response2 = client.sendQuery(query2);
                        redirectToUserPage();

//                    Intent intent = new  Intent(MainActivity.this , secondActivity.class);
//                    startActivity(intent);
                    }
                }else{
                    //Unsucessful Login! - due to netwrok error
                    ((TextView)findViewById(R.id.errorLabel)).setText("Network Error");

                }
            }else{
                ((TextView)findViewById(R.id.errorLabel)).setText("ERROR:contains no ? in query");
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String SHA256(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] messageDigest = md.digest(input.getBytes("UTF-8"));
        BigInteger no = new BigInteger(1, messageDigest);
        String hashtext = no.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }

    private void redirectToUserPage(){
        Intent intent = new Intent(this , UserPage.class);
        startActivity(intent);
    }
        void calledFunction(){
        Log.d("my Tag" , "before new Cient");
        client = new Client("10.145.126.103" , 5050);
        Log.d("my Tag" , "after new Cient");
//        client.query = "edit_status?name=ramLal&password=ajeebHash";
        String response = client.sendQuery("edit_status?name=ramDasLal&password=ajeebHash");
        Log.d("my Tag" , "after sendQuery");
    }

}
