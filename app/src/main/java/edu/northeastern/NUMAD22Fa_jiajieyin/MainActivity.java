package edu.northeastern.NUMAD22Fa_jiajieyin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public void jiajieyin(View view){
        Toast.makeText(getApplicationContext(),"jiajie-yin Email:dannyyin45@gmail.com",Toast.LENGTH_SHORT).show();
    }

    public void Click_Click(View view){
        Intent intent = new Intent(this, NewActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}