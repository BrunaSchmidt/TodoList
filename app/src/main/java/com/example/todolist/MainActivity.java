package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.security.PrivateKey;
import java.util.ArrayList;

public class MainActivity extends Activity {

    private EditText textoTarefa;
    private Button botaoAdicionar;
    private ListView listaDeTarefas;
    private SQLiteDatabase bancoDados;

    private ArrayAdapter<String> itensAdaptador;
    private ArrayList<String> itens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{

        //Recuperar componentes
        textoTarefa = (EditText) findViewById(R.id.textoId);
        botaoAdicionar = (Button) findViewById(R.id.botaoAdicionarId);


        //Banco de Dados
        bancoDados = openOrCreateDatabase("apptarefas", MODE_PRIVATE, null);

        //tabela tarefas
        bancoDados.execSQL("CREATE TABLE IF NOT EXISTS tarefas(id INTEGER PRIMARY KEY AUTOINCREMENT, tarefa VARCHAR) ");

        botaoAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    String textoDigitado = textoTarefa.getText().toString();
                    salvarTarefa(textoDigitado);

                }
            });

            //recuperar Tarefas
            recuperarTarefas();

        }catch( Exception e){
            e.printStackTrace();
        }
    }

    private void salvarTarefa(String texto){

        try{

            if(texto.equals("")){
                Toast.makeText(MainActivity.this, "Digite uma tarefa", Toast.LENGTH_SHORT).show();
            }else{
                bancoDados.execSQL("INSERT INTO tarefas (tarefa) VALUES('"+texto+"')");
                Toast.makeText(MainActivity.this, "Tarefa salva com sucesso!", Toast.LENGTH_SHORT).show();

            }

        }catch(Exception e){
            e.printStackTrace();
        }


    }

    private void recuperarTarefas(){
        try{

            //Recupera as tarefas
            Cursor cursor = bancoDados.rawQuery("SELECT * FROM tarefas ORDER BY id DESC", null);

            //recuperar os ids das colunas
            int indiceColunaId = cursor.getColumnIndex("id");
            int indiceColunaTarefa = cursor.getColumnIndex("tarefa");

            //lista
            listaDeTarefas = (ListView) findViewById(R.id.listViewId);

            //criar adaptador
            itens = new ArrayList<String>();
            itensAdaptador = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_list_item_2,
                    android.R.id.text2,
                    itens);
            listaDeTarefas.setAdapter(itensAdaptador);


            //listar tarefas
            cursor.moveToFirst();
            while (cursor != null){

                Log.i("Resultado", "Tarefa: "+ cursor.getString(indiceColunaTarefa));
                itens.add(cursor.getString(indiceColunaId));
                cursor.moveToNext();
            }

        }catch(Exception e){
            e.printStackTrace();
        }


    }

}
