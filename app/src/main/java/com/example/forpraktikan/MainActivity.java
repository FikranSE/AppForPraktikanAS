package com.example.forpraktikan;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView profilImage;
    private ImageView taskImage;
    private ImageView praktikanImage;
    private ImageView asistenImage;
    private ImageView pengumumanImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        Log.d(TAG, token);
                        Toast.makeText(MainActivity.this, "Your Device Registration Token is" + token, Toast.LENGTH_SHORT).show();
                    }
                });

        profilImage = (ImageView) findViewById(R.id.profilImage);
        taskImage = (ImageView) findViewById(R.id.taskImage);
        praktikanImage = (ImageView) findViewById(R.id.praktikanImage);
        asistenImage = (ImageView) findViewById(R.id.asistenImage);
        pengumumanImage = (ImageView) findViewById(R.id.pengumumanImage);

        profilImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openProfileActivity();
            }
        });

        taskImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openListTaskActivity();
            }
        });

        praktikanImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openListPraktikanActivity();
            }
        });

        asistenImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openListAsistenActivity();
            }
        });

        pengumumanImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openListPengumumanActivity();
            }
        });
    }
    public void openProfileActivity(){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void openListTaskActivity(){
        Intent intent = new Intent(this, ListTaskActivity.class);
        startActivity(intent);
    }

    public void openListPraktikanActivity(){
        Intent intent = new Intent(this, ListPraktikanActivity.class);
        startActivity(intent);
    }

    public void openListAsistenActivity(){
        Intent intent = new Intent(this, ListAsistenActivity.class);
        startActivity(intent);
    }

    public void openListPengumumanActivity(){
        Intent intent = new Intent(this, NotificationActivity.class);
        startActivity(intent);
    }
}
