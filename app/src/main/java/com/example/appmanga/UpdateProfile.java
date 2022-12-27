package com.example.appmanga;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmanga.Activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdateProfile extends AppCompatActivity {

    private TextView tv_username, tv_userEmail;
    private EditText et_current_password, et_new_password, et_new_password_2;
    private ImageButton ib_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        initButton();
    }

    public void initButton() {
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_userEmail = (TextView) findViewById(R.id.tv_email);
        et_current_password = (EditText) findViewById(R.id.et_old_password);
        et_new_password = (EditText) findViewById(R.id.et_new_password);
        et_new_password_2 = (EditText) findViewById(R.id.et_new_password_2);
        ib_ok = (ImageButton) findViewById(R.id.ok_button);
        Intent intent = getIntent();
        String received_username = intent.getStringExtra("username");
        String received_user_email = intent.getStringExtra("user_email");
        tv_username.setText(received_username);
        tv_userEmail.setText(received_user_email);

        ib_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    public void changePassword() {
        if (et_current_password.getText().toString().trim().length() <= 0 || et_new_password.getText().toString().trim().length() <= 0 || et_new_password_2.getText().toString().trim().length() <= 0) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_LONG).show();
        } else if (!(et_new_password.getText().toString().equals(et_new_password_2.getText().toString()))){
            Toast.makeText(this, "Password mới không khớp", Toast.LENGTH_LONG).show();
        }
        else {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            AuthCredential credential = EmailAuthProvider.getCredential(tv_userEmail.getText().toString(),et_current_password.getText().toString());
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        user.updatePassword(et_new_password.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Thay đổi mật khẩu thành công", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Có lỗi xảy ra, vui lòng thử lại sau", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Mật khẩu cũ bạn nhập không hợp lệ", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}