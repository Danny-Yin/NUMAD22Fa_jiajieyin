package edu.northeastern.NUMAD22Fa_jiajieyin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class NewActivity extends AppCompatActivity{

    public void MyonClick(View view) {
        int theId = view.getId();
        TextView OnClickText = (TextView) findViewById(R.id.textView2);
        switch (theId){
            case R.id.button3:
                OnClickText.setText("Pressed : A");
                break;
            case R.id.button4:
                OnClickText.setText("Pressed : B");
                break;
            case R.id.button5:
                OnClickText.setText("Pressed : C");
                break;
            case R.id.button6:
                OnClickText.setText("Pressed : D");
                break;
            case R.id.button7:
                OnClickText.setText("Pressed : E");
                break;
            case R.id.button8:
                OnClickText.setText("Pressed : F");
                break;
            default:
                break;
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newactivity);
    }


}