package com.example.chatuygulamasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class KayitActivity extends AppCompatActivity {

    private Button KayitOlusturmaButtonu;
    private EditText KullaniciMail;
    private EditText KullaniciSifre;
    private TextView ZatenHesabımVar;

    private ProgressDialog yukleniyorDialog;
    //firebase
    private DatabaseReference kokReference;
    private FirebaseAuth myetki;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit);

        //firebase
        myetki=FirebaseAuth.getInstance();
        kokReference= FirebaseDatabase.getInstance().getReference();

        //kontrol tanımlamaları
        KayitOlusturmaButtonu=findViewById(R.id.kayit_butonu);

        KullaniciMail=findViewById(R.id.kayit_email);
        KullaniciSifre=findViewById(R.id.kayit_sifre);

        ZatenHesabımVar=findViewById(R.id.zaten_hesap_var);

        yukleniyorDialog=new ProgressDialog(this);

        ZatenHesabımVar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginActivityIntent=new Intent(KayitActivity.this,LoginActivity.class);
                startActivity(loginActivityIntent);
            }
        });

        KayitOlusturmaButtonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YeniHesapOlustur();
            }
        });
    }

    private void YeniHesapOlustur() {
        String email=KullaniciMail.getText().toString();
        String sifre=KullaniciSifre.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"email boş olamaz",Toast.LENGTH_SHORT).show();

        }

        if(TextUtils.isEmpty(sifre)){
            Toast.makeText(this,"şifre boş olamaz",Toast.LENGTH_SHORT).show();

        }
        else
        {
            yukleniyorDialog.setTitle("Yeni Hesap oluşturuluyor");
            yukleniyorDialog.setMessage("Lütfen Bekleyin...");
            yukleniyorDialog.setCanceledOnTouchOutside(true);
            yukleniyorDialog.show();

          myetki.createUserWithEmailAndPassword(email,sifre)
                  .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                      @Override
                      public void onComplete(@NonNull Task<AuthResult> task) {
                          if(task.isSuccessful()){

                              String mevcutKullaniciId=myetki.getCurrentUser().getUid();
                              kokReference.child("Kullanicilar").child(mevcutKullaniciId).setValue("");

                              Intent anasayfa=new Intent(KayitActivity.this,MainActivity.class);
                              anasayfa.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                              startActivity(anasayfa);
                              finish();

                              Toast.makeText(KayitActivity.this,"Yeni hesap başarı ile oluşturuldu",Toast.LENGTH_SHORT).show();
                              yukleniyorDialog.dismiss();
                          }
                          else{
                              String mesaj=task.getException().toString();
                              Toast.makeText(KayitActivity.this,"Hata:"+mesaj+"Bilgilerinizi kontrol ediniz",Toast.LENGTH_LONG).show();
                              yukleniyorDialog.dismiss();

                          }

                      }
                  });
        }
    }
}