package com.example.sendox33;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigInteger;

public class Inbox extends AppCompatActivity {
    private Button[] myInboxButtons = new Button[5];
    private Button prevButton;
    private Button nextButton;
    private int currentPageIndex=1;
    private int LastPage = 2;
    private TextView pageNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        myInboxButtons[0] = (Button)findViewById(R.id.button);
        myInboxButtons[1] = (Button)findViewById(R.id.button2);
        myInboxButtons[2] = (Button)findViewById(R.id.button3);
        myInboxButtons[3] = (Button)findViewById(R.id.button4);
        myInboxButtons[4] = (Button)findViewById(R.id.button5);
        prevButton = (Button)findViewById(R.id.previousPageButton);
        nextButton = (Button)findViewById(R.id.nextPageButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPageIndex+=1;
                if(currentPageIndex>LastPage)LastPage--;
                setButtonText(currentPageIndex);
            }
        });
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPageIndex>1){
                    currentPageIndex+=-1;
                }
                setButtonText(currentPageIndex);
            }
        });
        setButtonText(currentPageIndex);


    }

    void setButtonText(int pageIndex){
        pageNumber = (TextView) findViewById(R.id.PageNumber);
        pageNumber.setText("Pg." + pageIndex);

        SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
        Client client = new Client("10.145.126.103" , 5050);
        String query = "load_inbox?" + "username=" +sp.getString("username", null) + "&index=" + pageIndex;
        String response = client.sendQuery(query);
        System.out.println("query = " + query);
        System.out.println("response = " + response);
        for(int i=0;i<5;i++)myInboxButtons[i].setText("");
        if(response.contains("?")) {
            String[] arrOfStr = response.split("\\?", 2);
            if(arrOfStr.length>1){
                String[] secondArrOfStr = arrOfStr[1].split("&#@!@#&");
                for(int i=0;i<secondArrOfStr.length&&i<5;i++){
                    String[] temp = secondArrOfStr[i].split("&#@=@#&", 2);
                    if(temp.length>1){
                        SharedPreferences sp2 = getSharedPreferences("LOGIN", MODE_PRIVATE);
                        BigInteger N = new BigInteger(sp2.getString("n",null));
                        BigInteger E = new BigInteger(sp2.getString("e",null));
                        BigInteger D = new BigInteger(sp2.getString("d",null));
                        RSAPrivateKey pr = new RSAPrivateKey(E, D, N);
//                        myInboxButtons[i].setText( temp[0] + ":" + pr.decrypt(temp[1].getBytes()).toString() );
                        myInboxButtons[i].setText( temp[0] + ":" + temp[1] );
                        System.out.println("set message = " + i + temp[0] + ":" + temp[1]);
                    }
                }
            }
        }
        if(myInboxButtons[4].getText().toString().equals(""))LastPage=pageIndex;
    }
}
