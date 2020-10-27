package com.rishi.fooddelivery.Classes;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rishi.fooddelivery.Model.User;
import com.rishi.fooddelivery.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUp extends AppCompatActivity {

    MaterialEditText edtPhone,edtName,edtPassword;
    Button btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        edtName     = (MaterialEditText)findViewById(R.id.enterName);
        edtPhone    = (MaterialEditText)findViewById(R.id.enterPhone);
        edtPassword = (MaterialEditText)findViewById(R.id.enterPassword);

        btnSignUp   = (Button)findViewById(R.id.btnSignUp1);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog mDialog = new ProgressDialog(com.rishi.fooddelivery.Classes.SignUp.this);

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                            mDialog.dismiss();
                            Toast.makeText(com.rishi.fooddelivery.Classes.SignUp.this, "Sign Up Sucessful!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            User user = new User(edtName.getText().toString(),edtPassword.getText().toString());
                            table_user.child(edtPhone.getText().toString()).setValue(user);
                            Toast.makeText(com.rishi.fooddelivery.Classes.SignUp.this, "Sign Up Sucessful!", Toast.LENGTH_SHORT).show();
                            Intent homeIntent = new Intent(com.rishi.fooddelivery.Classes.SignUp.this, com.rishi.fooddelivery.Classes.SignIn.class);
                            startActivity(homeIntent);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
