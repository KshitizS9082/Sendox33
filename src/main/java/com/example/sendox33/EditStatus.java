package com.example.sendox33;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditStatus extends AppCompatActivity {

    private Button editStatusButton;
    private EditText editStatusTextFields;
    private TextView errorMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_status);

        editStatusTextFields = (EditText) findViewById(R.id.editStatusText);
        editStatusButton = (Button) findViewById(R.id.updateStatusButton);
        errorMessage = (TextView)  findViewById(R.id.editStatusErrorMessageTextView);

        editStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);

                Client client = new Client("10.145.126.103" , 5050);
                if(editStatusTextFields.getText().toString().contains("&")){
                    errorMessage.setText("Your status can't contain stray charachters like &,*,? etc.");
                    errorMessage.setTextColor(Color.rgb(140,10,10));
                }else{
                    String query = "edit_status?" + "username=" + sp.getString("username", null) + "&new_status=" + editStatusTextFields.getText().toString();
                    String response = client.sendQuery(query);
                    if(response.equals("success?")){
                        //redirect to UserPage
                        Intent intent = new Intent( EditStatus.this , UserPage.class);
                        startActivity(intent);
                    }else{
                        errorMessage.setText("Error updating status");
                        errorMessage.setTextColor(Color.rgb(140,10,10));
                    }
                }

            }
        });
    }
}
