package com.example.chatuygulamasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {



    //firebase
    private FirebaseUser mevcutKullanici;
    private FirebaseAuth myetki;
    private DatabaseReference kullanicilarReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //firebase
        myetki=FirebaseAuth.getInstance();
        mevcutKullanici=myetki.getCurrentUser();
        kullanicilarReference= FirebaseDatabase.getInstance().getReference();


    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mevcutKullanici==null){
            KullaniciyiLoginActivityeGonder();
        }
        else{
            KullanicininVarliginiDogrula();
        }
    }

    private void KullanicininVarliginiDogrula() {
        String mevcutKullaniciId=myetki.getCurrentUser().getUid();

        kullanicilarReference.child("Kullanicilar").child(mevcutKullaniciId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                if((datasnapshot.child("ad").exists())){
                    Toast.makeText(MainActivity.this,"Hoşgeldiniz",Toast.LENGTH_LONG).show();
                }
                else{
                    Intent ayarlar=new Intent(MainActivity.this,AyarlarActivity.class);
                    ayarlar.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(ayarlar);
                    //finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void KullaniciyiLoginActivityeGonder() {
        Intent loginIntent=new Intent(MainActivity.this,LoginActivity.class);
        startActivity(loginIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.secenekler_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId()==R.id.ana_arkadas_bulma_secenegi){

        }

        if(item.getItemId()==R.id.ana_ayarlar_secenegi) {

            Intent ayar=new Intent(MainActivity.this,AyarlarActivity.class);
            startActivity(ayar);
        }
        if(item.getItemId()==R.id.ana_cikis_secenegi){

            myetki.signOut();
            Intent giris=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(giris);

        }
            return true;
    }
}
