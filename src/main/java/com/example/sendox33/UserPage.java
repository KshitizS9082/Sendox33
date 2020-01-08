package com.example.sendox33;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
//import android.support.design.widget.

public class UserPage extends AppCompatActivity {
    private Button myInboxButton;
    private Button composeMesageButton;
    private Button deleteAccountButton;
    private Button viewContactsButton;
    private Button editStatusButton;
    private int numberOfTimeDeletePressed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        myInboxButton = (Button)findViewById(R.id.myInbox);
        composeMesageButton = (Button)findViewById(R.id.composeMessage);
        deleteAccountButton = (Button)findViewById(R.id.deleteAccount);
        viewContactsButton = (Button)findViewById(R.id.viewContact);
        editStatusButton = (Button)findViewById(R.id.editStatus);

        myInboxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserPage.this , Inbox.class);
                startActivity(intent);
            }
        });
        composeMesageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserPage.this , ComposeMessage.class);
                startActivity(intent);
            }
        });
        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccountButton.setText("Are you sure you wanna delete this account?");
                if(numberOfTimeDeletePressed==2){
                    //TODO: delete all the data
                    SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
                    System.out.println("name earlier = " + sp.getString("username", null));

                    Client client = new Client("Apples-MacBook-Pro.local" , 5050);
                    String query = "delete_account?" + "username=" + sp.getString("username", null) + "&password=" + sp.getString("hashedPassword", null);
                    String response = client.sendQuery(query);
                    if (response.equals("success?")){
                        SharedPreferences.Editor ed = sp.edit();
                        ed.clear();
                        ed.commit();
                        System.out.println("name after = " + sp.getString("username", null));
                        Intent intent = new Intent(UserPage.this , MainActivity.class);
                        startActivity(intent);
                    }else{
                        //TODO: delete failed
                        deleteAccountButton.setText("Network error, Are you sure you wanna delete this account?");
                        numberOfTimeDeletePressed = 0;
                    }
                }

                if(numberOfTimeDeletePressed<2){
                    numberOfTimeDeletePressed+=1;
                }

            }
        });
        viewContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserPage.this , ViewContacts.class);
                startActivity(intent);
            }
        });
        editStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserPage.this , EditStatus.class);
                startActivity(intent);
            }
        });
    }
}
