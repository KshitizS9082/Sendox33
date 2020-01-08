package com.example.sendox33;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigInteger;

public class ComposeMessage extends AppCompatActivity {
    private Button sendButton;
    private EditText recieverName;
    private EditText message;
    private TextView errorMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_message);
        sendButton = (Button) findViewById(R.id.sendButton2);
        recieverName = (EditText) findViewById(R.id.recievertTextView);
        message = (EditText) findViewById(R.id.contentsOfMessageTextView);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessage();
            }
        });
    }
    void SendMessage(){
        recieverName = (EditText) findViewById(R.id.recievertTextView);
        message = (EditText) findViewById(R.id.contentsOfMessageTextView);
        errorMessage = (TextView) findViewById(R.id.composeMessageErrorMessageTextView);
        SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);

        Client client = new Client("10.145.126.103" , 5050);
        String query = "fetch_public_key?" + "username=" + recieverName.getText().toString();
        String publicKeyString = client.sendQuery(query);
        String[] publicKeyArr = publicKeyString.split("&", 2);
        System.out.println("publicKeyArr[0] = " + publicKeyArr[0]);
        BigInteger N = new BigInteger(publicKeyArr[0]);
        BigInteger E = new BigInteger(publicKeyArr[1]);
        RSAPublicKey publickey = new RSAPublicKey(N,E);
        byte[] tempBytes = publickey.encrypt(message.getText().toString().getBytes());
        String messageString = new String(tempBytes);
        System.out.println("messageString = " + messageString);
//        String query2 = "send_message?" + "username=" + sp.getString("username", null) + "&reciever=" + recieverName.getText().toString() + "&content=" + messageString;
        String query2 = "send_message?" + "username=" + sp.getString("username", null) + "&reciever=" + recieverName.getText().toString() + "&content=" + message.getText().toString();
        String response = client.sendQuery(query2);
        if(response.equals("success?")){
            errorMessage.setText("Message Sent!");
            errorMessage.setTextColor(Color.rgb(10,140,10));
        }else{
            errorMessage.setText("Message Sent Failed!");
            errorMessage.setTextColor(Color.rgb(140,100,10));
        }
    }
}
