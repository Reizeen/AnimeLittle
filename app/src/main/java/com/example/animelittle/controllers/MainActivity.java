package com.example.animelittle.controllers;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animelittle.R;
import com.example.animelittle.adapters.AdapterAnimes;
import com.example.animelittle.model.Anime;
import com.example.animelittle.model.AnimeConstantes;

public class MainActivity extends AppCompatActivity {

    private AdapterAnimes adapterAnimes;
    private RecyclerView recyclerAnimes;

    private ProgressDialog pd;
    private MiAsyncTask miAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerAnimes = findViewById(R.id.idRecyclerView);
        recyclerAnimes.setLayoutManager(new LinearLayoutManager(this));

        adapterAnimes = new AdapterAnimes(this, selectAnimes());
        if (adapterAnimes.getItemCount() != 0){
            iniciarAsyncTask();
            recyclerAnimes.setAdapter(adapterAnimes);
            abrePerfilAnime(adapterAnimes);
        }
    }

    private Cursor selectAnimes() {
        //Columnas de la tabla a recuperar
        String[] projection = new String[] {
                AnimeConstantes.ID,
                AnimeConstantes.TITULO,
                AnimeConstantes.FAVORITO,
                AnimeConstantes.ESTRENO,
                AnimeConstantes.IMAGEN,
                AnimeConstantes.URL_WEB,
                AnimeConstantes.INFO_DESCRIPCION };

        Uri uri = Uri.parse("content://com.example.animeinfo.contentproviders/animes");
        ContentResolver cr = getContentResolver();

        //Hacemos la consulta
        Cursor cur = cr.query(uri,
                projection, //Columnas a devolver
                null,       //Condición de la query
                null,       //Argumentos variables de la query
                null);      //Orden de los resultados

        return cur;
    }

    /**
     * Abre la actividad del perfil de un item del RecyclerView
     */
    public void abrePerfilAnime(final AdapterAnimes adapterAnimes) {
        adapterAnimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Anime anime = adapterAnimes.obtenerAnime(recyclerAnimes.getChildAdapterPosition(view));
                Intent intencion = new Intent(getApplicationContext(), PerfilAnime.class);
                intencion.putExtra("anime", anime);
                startActivityForResult(intencion, 101);
            }
        });
    }

    /** ==================================================================
     *  ==================== FUNCIONALIDAD AsyncTask =====================
     *  ================================================================== /

     /**
     * Mostrar un ProgressDialog para la ejecucción del AsyncTask
     */
    public void iniciarAsyncTask(){
        // Initialize a new instance of progress dialog
        pd = new ProgressDialog(MainActivity.this);

        // Set progress dialog style spinner
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        // Set the progress dialog title and message
        pd.setMessage("Cargando...");
        pd.setCancelable(true);
        pd.setMax(100);

        miAsyncTask = new MiAsyncTask();
        miAsyncTask.execute();
    }

    /**
     * Clase AsyncTask
     * Utilizado para ejecutar operaciones en segundo plano,
     * en este caso para visualizar los datos del RecyclerView
     */
    private class MiAsyncTask extends AsyncTask<Void, Integer, Boolean> {

        /**
         * Método llamado antes de iniciar el procesamiento en segundo plano.
         * Establecemos un OnCenclListener por si queremos cancelar nuestro AsyncTask
         * Mostramos el ProgressDialog
         */
        @Override
        protected void onPreExecute() {
            pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    MiAsyncTask.this.cancel(true);
                }
            });

            pd.setProgress(0);
            pd.show();
        }

        /**
         * En este método se define el código que se ejecutará en segundo plano.
         * Recibe como parámetros los declarados al llamar al método execute(Params).
         * Ejecutará un bucle con un slepp tantas veces como items tenga el adaptador.
         * Cuanto más items más tardará en cargar.
         * Si cancelamos, terminará el bucle.
         * @param voids
         * @return
         */
        @Override
        protected Boolean doInBackground(Void... voids) {
            for (int i = 0; i < adapterAnimes.getItemCount(); i++){
                try {
                    Thread.sleep(1000);
                } catch(InterruptedException e) {}

                if(isCancelled())
                    break;
            }

            return true;
        }

        /**
         * Este método es llamado por publishProgress(), dentro de doInBackground(Params)
         * El uso es  para actualizar el porcentaje de ProgressDialog.
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            int progreso = values[0].intValue();
            pd.setProgress(progreso);
        }

        /**
         * Este método es llamado tras finalizar doInBackground(Params).
         * Recibe como parámetro el resultado devuelto por doInBackground(Params).
         * @param result
         */
        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                pd.dismiss();
                Toast.makeText(MainActivity.this, "Datos cargados!", Toast.LENGTH_SHORT).show();
            }
        }

        /**
         * Se ejecutará cuando se cancele la ejecución de la tarea antes de su finalización normal.
         */
        @Override
        protected void onCancelled() {
            Toast.makeText(MainActivity.this, "Carga cancelada!", Toast.LENGTH_SHORT).show();
        }
    }
}