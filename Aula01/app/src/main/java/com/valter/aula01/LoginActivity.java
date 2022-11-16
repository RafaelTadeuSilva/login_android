package com.valter.aula01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ktx.Firebase;

public class LoginActivity extends AppCompatActivity {
    EditText edtUsuario, edtSenha;
    TextView tvCadastrousuario;
    Button btnEntrar;
    private String email, senha;
    private String[] mensagens = {"Preencha todos os campos!", "Login efetuado com sucesso!"};

//    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i("CicloVida", getClassName() + ".onCreate() chamado");

        setTitle("Tela Login");
//        mAuth = FirebaseAuth.getInstance();


        edtUsuario = findViewById(R.id.edtUsuario);
        edtSenha = findViewById(R.id.edtSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        tvCadastrousuario =findViewById(R.id.tvCadastrousuario);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email =  edtUsuario.getText().toString();
                senha = edtSenha.getText().toString();

                if(email.isEmpty() || senha.isEmpty()){
                    Snackbar snackbar = Snackbar.make(view,  mensagens[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
                else
                {
                    autenticaUsuario(view);
                }

//                if ((usuario.equals("comp")) && (senha.equals("123"))) {
//                    Intent intent = new Intent(LoginActivity.this, TelaFeedActivity.class);
//                    startActivity(intent);
//                }
//                else {
//                    Toast.makeText(getContext(), "Usuário ou senha incorretos!.", Toast.LENGTH_SHORT).show();
//                }
            }
        });
        tvCadastrousuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CadastroUsuarioActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void autenticaUsuario(View view){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    telaPrincipal();
                    Log.d("Login", "Login realizado com sucesso!");
                }
                else
                {
                    String erro;
                    try {
                        throw task.getException();
                    }catch (Exception e){
                        erro = "Erro ao autenticar usuário!";
                        Log.d("Login", e.toString());
                    }
                    Snackbar snackbar = Snackbar.make(view, erro, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            }




        });
    }

   /* public void onCLickBtnLogin(View view) {
        EditText edtUsuario = findViewById(R.id.edtUsuario);
        EditText edtSenha = findViewById(R.id.edtSenha);

        String usuario = edtUsuario.getText().toString();
        String senha = edtSenha.getText().toString();

        if (usuario.equals("comp") && senha.equals("123")) {
            Toast.makeText(this, "Bem-vindo! Login realizado.", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Usuário ou senha incorretos!.", Toast.LENGTH_SHORT).show();
        }
    }*/
    private void telaPrincipal(){
        Intent intent = new Intent(getContext(), TelaFeedActivity.class);
        startActivity(intent);
        finish();
    }

    private void telaCadastro(){
        Intent intent = new Intent(getContext(), CadastroUsuarioActivity.class);
        startActivity(intent);
        finish();
    }

    private Context getContext() {
        return this;
    }

    @Override
    protected void onStart() {
        super.onStart();

//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            reload();
//        }
        Log.i("CicloVida", getClassName() + ".onStart() chamado");
        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        if(usuarioAtual != null){
            telaPrincipal();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("CicloVida", getClassName() + ".onStop() chamado");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("CicloVida", getClassName() + ".onDestroy() chamado");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("CicloVida", getClassName() + ".onPause() chamado");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("CicloVida", getClassName() + ".onResume() chamado");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("CicloVida", getClassName() + ".onSaveInstanceState() chamado");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("CicloVida", getClassName() + ".onRestart() chamado");
    }

    private String getClassName() {
        String s = getClass().getName();
        return s.substring(s.lastIndexOf("."));
    }
}