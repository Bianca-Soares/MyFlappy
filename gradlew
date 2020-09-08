package com.example.quizsimples;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    TextView textCont;
    TextView textPergunta;

    Button btnOp1;
    Button btnOp2;
    Button btnOp3;
    Button btnOp4;

    String opCerta;
    int contCertas = 0;
    int quizCont = 1;

    // lista que irá receber cada quiz da matriz
    ArrayList<ArrayList<String>> listaQuiz = new ArrayList<>();

    //Perguntas e Respostas
    String todosQuiz[][] ={

            {"  Você foi contratado recentemente para a Divisão de Tecnologia da Informação de uma empresa, e a chefia determinou a construção do Plano Diretor de Tecnologia da Informação (PDTI). A finalidade da construção do PDTI é:\n\n" +
                    "A) Obter o alinhamento estratégico de TI.\n" +
                    "B) Mapear os processos de Tecnologia da Informação.\n" +
                    "C) conhecer os custos pormenorizados dos ativos de TI.\n" +
                    "D) Conceber os projetos tecnológicos de infraestrutura.",
                    "A"},
            {"  O principal objetivo da governança de TI é garantir o alinhamento:\n\n" +
                    "A) do negócio à TI.\n" +
                    "B) da TI ao negócio.\n" +
                    "C) do negócio ao regulatório.\n" +
                    "D) da gestão de demandas à TI.","B"}
    };

    //quantidade de perguntas
    final  private int totalQuiz = todosQuiz.length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       //provedor de conteúdo sem BD
        SharedPreferences dadosQuiz = getSharedPreferences("dadosQuizApp", Context.MODE_PRIVATE);
        String dadosMatriz[][] ={
                {"pergunta","A"}
        };
        dadosMatriz[0][0]= dadosQuiz.getString("todasQuest","uma");
        SharedPreferences.Editor edi = dadosQuiz.edit();
        edi.put

        textCont = findViewById(R.id.textCont);
        textPergunta = findViewById(R.id.textPergunta);

        btnOp1 = findViewById(R.id.btnOp1);
        btnOp2 = findViewById(R.id.btnOp2);
c        btnOp4 = findViewById(R.id.btnOp4);

        for(int i = 0; i< todosQuiz.length; i++){

            ArrayList<String> quizAtual= new ArrayList<>();

            quizAtual.add(todosQuiz[i][0]);
            quizAtual.add(todosQuiz[i][1]);

            //liata de questionário recebe a perguntas com as alternativas e a alternativa certa
            listaQuiz.add(quizAtual);

        }
        exibirQuiz();
    }

    public void exibirQuiz(){
        //Atualizar a numeração do quiz
        textCont.setText("Q "+quizCont);

        //gerar numero aleatório entre 0 e total(size) de quiz
        Random random = new Random();
        int intRandom = random.nextInt(listaQuiz.size());

        //pegar o quiz de posição aleatória intRandom
        ArrayList<String> quiz = listaQuiz.get(intRandom);

        //Ordem dos elemetos {"Pergunta e alternativas", "opCerta"}
        //o texto da pergunta recebe a posição 0 da lista quiz
        textPergunta.setText(quiz.get(0)); // posição 0 pergunta

        opCerta = quiz.get(1); // posição 1 opção certa

        //Remover quiz respodido da lista de quiz
        listaQuiz.remove(intRandom);

    }
    //analizar resposta do botão clicado //todos os botões são check
    public void check(View view){
        //pegar a escolha do usúario
        Button btnClicado = findViewById(view.getId());//pega o id do botão que ativou o check
        String textBtn = btnClicado.getText().toString(); // pega o texto do botão

        String textAlerta;

        //compara texto do botão clicado com a ressposta certa
        if(textBtn.equals(opCerta)){
            textAlerta = "Certa Resposta!";
            contCertas ++;
        }else{
            textAlerta= "Resposta Certa ...";
        }

        //Criar alerta
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(textAlerta);
        builder.setMessage(opCerta);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(quizCont == totalQuiz){
                    //ir para tela resultado
                    Intent intent = new Intent(getApplicationContext(), TelaResultado.class);
                    intent.putExtra("TOTAL_DE_ACERTOS", contCertas);//enviar total de acertos para tela d resultado
                    intent.putExtra("TOTAL_DE_PERGUNTAS", totalQuiz);//enviar total de per
                    startActivity(intent);
                }else{
     