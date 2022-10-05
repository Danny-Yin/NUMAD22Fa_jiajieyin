package edu.northeastern.NUMAD22Fa_jiajieyin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LinkActivityMain extends AppCompatActivity {
    RecyclerView linkRecycleView;
    ArrayList<LinkItem> linkList = new ArrayList();
    LinkAdapter linkitemAdapter;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton addButton;
    EditText linkName;
    EditText linkURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.link_recycler);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        linkList = new ArrayList();
        linkitemAdapter = new LinkAdapter(linkList);

        linkRecycleView = findViewById(R.id.recyclerView);

        linkRecycleView.setHasFixedSize(true);

        linkRecycleView.setLayoutManager(new LinearLayoutManager(this));

        linkRecycleView.setAdapter(linkitemAdapter);

        addButton = (FloatingActionButton)findViewById(R.id.floatingActionButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                View view2 = LayoutInflater.from(view.getContext()).inflate(R.layout.link_item, linkRecycleView, false);

                TextView name = view2.findViewById(R.id.linkName);

                TextView url = view2.findViewById(R.id.linkURL);

                 */

                LayoutInflater layout_inf = LayoutInflater.from(getApplicationContext());

                View v = layout_inf.inflate(R.layout.dialog_link, null);

                alertDialog.setTitle("Add Data");
                EditText name = v.findViewById(R.id.linkName_D);
                EditText url = v.findViewById(R.id.linkURL_URL);
                alertDialog.setView(v);

               // alertDialog.setTitle("Add Data");

                //alertDialog.setView(v);

                alertDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Gson gson = new Gson();

                        try{
                            JSONObject jasonObject = new JSONObject();
                            jasonObject.put("linkName",name.getText().toString());
                            jasonObject.put("linkURL",url.getText().toString());
                            LinkItem item = gson.fromJson(String.valueOf(jasonObject),LinkItem.class);
                            linkList.add(item);
                            System.out.println(item.getLinkName());
                            linkitemAdapter.notifyDataSetChanged();
                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "The link was created successfully", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        } catch (JSONException e){
                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "The link was not created",Snackbar.LENGTH_LONG);
                            snackbar.show();
                            e.printStackTrace();
                        }
                    }
                });

                alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                alertDialog.show();



            }
        });





    }


}
