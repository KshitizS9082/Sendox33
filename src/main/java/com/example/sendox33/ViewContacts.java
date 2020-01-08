package com.example.sendox33;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ViewContacts extends AppCompatActivity {
    private Button sendButton;
    private EditText UserName;
    private TextView statusHeading;
    private TextView statusTextView;
    private TextView errorMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contacts);
        sendButton =(Button) findViewById(R.id.searchButton);
        UserName = (EditText) findViewById(R.id.contactToSearch);
        statusHeading = (TextView) findViewById(R.id.viewContactsStatusHeading);
        statusTextView = (TextView) findViewById(R.id.viewContactsStatusTextView);
        errorMessage = (TextView)  findViewById(R.id.viewContactErrorMessageTextView);

        statusHeading.setTextColor(Color.rgb(10,140,10));
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Client client = new Client("10.145.126.103" , 5050);
                String query = "view_contact?" + "username=" + UserName.getText().toString();
                String response = client.sendQuery(query);
                if(response.equals("fail?")){
                    errorMessage.setText("Username typed doesnot exist");
                    errorMessage.setTextColor(Color.rgb(140,10,10));
                }else{
                    String[] arrOfStr = response.split("\\?", 2);
                    if(arrOfStr.length>1) {
                        statusTextView.setText(arrOfStr[1]);
                        errorMessage.setText("");
                    }
                }
            }
        });

    }
}
