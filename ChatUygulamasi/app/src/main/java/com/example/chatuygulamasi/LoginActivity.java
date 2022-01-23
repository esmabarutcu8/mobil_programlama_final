package com.example.chatuygulamasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button ayarlar;
    private Button girisButonu;
    private EditText kullaniciMail;
     private EditText kullaniciSifre;
    private TextView YeniHesapAlma;
//firebase
    private FirebaseAuth myetki;
//progress dialog
    ProgressDialog girisDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //kontrol tanımlamaları
        girisButonu=findViewById(R.id.giris_butonu);
        kullaniciMail=findViewById(R.id.giris_email);
        kullaniciSifre=findViewById(R.id.giris_sifre);
        YeniHesapAlma=findViewById(R.id.yeni_hesap_alma);
        //progress
        girisDialog=new ProgressDialog(this);
        //firebase
        myetki=FirebaseAuth.getInstance();


        YeniHesapAlma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kayitActivityIntent=new Intent(LoginActivity.this,KayitActivity.class);
                startActivity(kayitActivityIntent);
            }
        });

        girisButonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                KullaniciyaGirisİzniVer();
            }
        });

    }

    private void KullaniciyaGirisİzniVer() {
        String email=kullaniciMail.getText().toString();
        String sifre=kullaniciSifre.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"email boş olamaz",Toast.LENGTH_SHORT).show();

        }

        if(TextUtils.isEmpty(sifre)){
            Toast.makeText(this,"şifre boş olamaz",Toast.LENGTH_SHORT).show();

        }
        else{
            //progress
            girisDialog.setTitle("Giriş yapılıyor");
            girisDialog.setMessage("Lütfen bekleyin...");
            girisDialog.setCanceledOnTouchOutside(true);
            girisDialog.show();

            //Giriş
            myetki.signInWithEmailAndPassword(email,sifre)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                Intent anasayfa=new Intent(LoginActivity.this,MainActivity.class);
                                anasayfa.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(anasayfa);
                                finish();



                                Toast.makeText(LoginActivity.this,"Giriş Başarılı",Toast.LENGTH_SHORT).show();
                                girisDialog.dismiss();

                            }
                            else{
                                String mesaj=task.getException().toString();
                                Toast.makeText(LoginActivity.this,"Hata"+mesaj+"Bilgilerinizi kontrol edin",Toast.LENGTH_LONG).show();
                                    girisDialog.dismiss();
                            }

                        }
                    });
        }
    }


    private void KullaniciyiAnaActivityeGonder() {
        Intent AnaAktiviteIntent=new Intent(LoginActivity.this,MainActivity.class);
        startActivity(AnaAktiviteIntent);
        //finish();
    }
}