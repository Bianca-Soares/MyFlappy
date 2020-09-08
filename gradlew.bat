package com.example.quizsimples;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
        SharedPreferences dadosQuiz = getSharedPreferences("dadosQuizApp", Context.MODE_PRIVATE