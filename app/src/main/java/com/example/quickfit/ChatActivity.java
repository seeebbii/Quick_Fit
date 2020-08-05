package com.example.quickfit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.quickfit.Services.ChatAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.quickfit.DashboardActivity.*;

public class ChatActivity extends AppCompatActivity {

    EditText message;
    Button sendbtn;
    ListView chatlist;
    ArrayList<Object> users = new ArrayList<Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        message = findViewById(R.id.msg);
        sendbtn = findViewById(R.id.sendbtn);
        chatlist = findViewById(R.id.chatlist);


        AndroidNetworking.get("http://sania.co.uk/quick_fix/chat/MyData.php?id=" +CURRENT_USER.getId())
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);
                                final String name = object.getString("msg");
                                final Boolean isAdmin = object.getBoolean("admin");
                                users.add(new Object() {
                                    public final String msg = name;
                                    public final Boolean IsAdmin = isAdmin;
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        ChatAdapter adapter = new ChatAdapter(ChatActivity.this,users);
                        ListView listView = (ListView) findViewById(R.id.chatlist);
                        listView.setAdapter(adapter);

                    }
                    @Override
                    public void onError(ANError error) {
                    }
                });



        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AndroidNetworking.post("http://sania.co.uk/quick_fix/chat/Send.php")
                        //Add the User ID in the below  line |  Replace  23  !!!!!!
                        .addBodyParameter("id", CURRENT_USER.getId() + "")
                        .addBodyParameter("msg", message.getText().toString().trim()).setTag("test")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                users.add(new Object() {
                                    public final String msg = message.getText().toString().trim();
                                    public final Boolean IsAdmin = false;
                                });
                                ChatAdapter adapter = new ChatAdapter(ChatActivity.this,users);
                                ListView listView = (ListView) findViewById(R.id.chatlist);
                                listView.setAdapter(adapter);
                                Toast.makeText(ChatActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onError(ANError error) {

                                Toast.makeText(ChatActivity.this, "Message Failed " + error.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });

            }
        });
    }
}