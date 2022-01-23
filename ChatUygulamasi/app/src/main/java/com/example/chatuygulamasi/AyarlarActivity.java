package com.example.chatuygulamasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AyarlarActivity extends AppCompatActivity {

    private Button hesapAyarlariniGuncelleme;
    private EditText kullaniciAdi;
    private EditText kullaniciDurumu;
    private ImageView kullaniciProfilResmi;


    //firabase
    private FirebaseAuth mYetki;
    private DatabaseReference veriYolu;
    private String mevcutKullaniciId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayarlar);

        //firebase
        mYetki=FirebaseAuth.getInstance();
        veriYolu= FirebaseDatabase.getInstance().getReference();
        mevcutKullaniciId=mYetki.getCurrentUser().getUid();

        //kontrol tanımlamaları
        hesapAyarlariniGuncelleme=findViewById(R.id.ayarlari_guncelleme_butonu);
        kullaniciAdi=findViewById(R.id.kullanici_adi_ayarla);
        kullaniciProfilResmi=findViewById(R.id.profil_resmi_ayarla);

        hesapAyarlariniGuncelleme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AyarlariGuncelle();
            }
        });
    }
    private void AyarlariGuncelle() {
        String kullaniciAdiAyarla=kullaniciAdi.getText().toString();
        String KullaniciDurumuAyarla=kullaniciDurumu.getText().toString();

        if(TextUtils.isEmpty(kullaniciAdiAyarla)){
            Toast.makeText(this,"Lütfen adınızı yazın!",Toast.LENGTH_LONG).show();

        }
        if(TextUtils.isEmpty(KullaniciDurumuAyarla)){
            Toast.makeText(this,"Lütfen durumunuzu yazın!",Toast.LENGTH_LONG).show();

        }
        else {
            HashMap<String,String> ProfilHaritasi = new HashMap<>();
            ProfilHaritasi.put("uid", mevcutKullaniciId);
            ProfilHaritasi.put("ad", kullaniciAdiAyarla);
            ProfilHaritasi.put("durum", KullaniciDurumuAyarla);

            veriYolu.child("Kullanicilar").child(mevcutKullaniciId).setValue(ProfilHaritasi)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(AyarlarActivity.this,"Profiliniz başarılı bir şekilde güncellendi",Toast.LENGTH_SHORT).show();

                                Intent anasayfa=new Intent(AyarlarActivity.this,MainActivity.class);
                                anasayfa.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(anasayfa);
                                finish();
                            }
                            else{
                                String mesaj=task.getException().toString();
                                Toast.makeText(AyarlarActivity.this,"Hata"+mesaj,Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    }
}