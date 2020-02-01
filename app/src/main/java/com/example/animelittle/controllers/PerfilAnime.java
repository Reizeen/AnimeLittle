package com.example.animelittle.controllers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.animelittle.R;
import com.example.animelittle.model.Anime;

import java.io.ByteArrayInputStream;

public class PerfilAnime extends AppCompatActivity {

    private Anime anime;
    private TextView titulo;
    private TextView info;
    private TextView estreno;
    private ImageView imagen;
    private boolean fav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_anime);

        titulo = findViewById(R.id.idTituloPerfil);
        estreno = findViewById(R.id.idEstrenoPerfil);
        info = findViewById(R.id.idInfoPerfil);
        imagen = findViewById(R.id.idImagenPerfil);

        anime = (Anime) getIntent().getSerializableExtra("anime");
        cargarDatosAnime();
    }

    /**
     * Cargar datos del anime
     */
    public void cargarDatosAnime(){
        titulo.setText(anime.getTitulo());
        estreno.setText("Año de estreno: " + anime.getEstreno());
        info.setText(anime.getInfo());

        ByteArrayInputStream bais = new ByteArrayInputStream(anime.getFoto());
        Bitmap foto = BitmapFactory.decodeStream(bais);
        imagen.setImageBitmap(foto);

        fav = anime.getFavorito();
    }

    /**
     * Crea el menu
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_perfil_anime, menu);
        return true;
    }

    /**
     * Metodo onClick del menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.infoWeb:
                verWebInfo();
                return true;
            case R.id.compartir:
                compartirInfo();
                return true;
            case R.id.email:
                enviarCorreo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Cargat datos del anime después de ser modificado
     * @param requestCode
     * @param resultCode
     * @param code
     */
    public void onActivityResult(int requestCode, int resultCode, Intent code) {
        if (requestCode == 101 && resultCode == RESULT_OK) {
            Anime animeMod = (Anime) code.getSerializableExtra("anime");
            anime = animeMod;
            Log.i(null, "onActivityResult: " + animeMod.getTitulo());
            cargarDatosAnime();
        }
    }


    /**
     * Ir a la pagina de la fuente
     */
    private void verWebInfo() {
        String enlace = anime.getUrl();
        if (!enlace.startsWith("https://") && !enlace.startsWith("http://"))
            enlace = "http://" + enlace;

        Uri uri = Uri.parse(enlace);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }


    /**
     * Comaprtir descripcion
     */
    private void compartirInfo() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, anime.getInfo());
        startActivity(Intent.createChooser(intent, "Compartir en"));
    }

    /**
     * Enviar correo a traves de un intent especifico para ello.
     */
    private void enviarCorreo() {
        String[] TO = {""};
        String[] CC = {""};

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "INFO ANIME");
        emailIntent.putExtra(Intent.EXTRA_TEXT, anime.getInfo());

        try {
            startActivity(Intent.createChooser(emailIntent, "Enviar mail..."));

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(PerfilAnime.this, "No hay un cliente de correo electrónico instalado.", Toast.LENGTH_SHORT).show();
        }
    }

}
