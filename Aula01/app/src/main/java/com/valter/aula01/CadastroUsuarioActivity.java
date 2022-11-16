package com.valter.aula01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CadastroUsuarioActivity extends AppCompatActivity {
    private EditText edtNomeCadastro, edtEmailCadastro, edtSenhaCadastro, edtConfirmarSenhaCadastro;
    private Button btnCadastrarUsuario;

    private String[] mensagens = {"Preencha todos os campos!", "Cadastro realizado com sucesso!"};
    private String usuarioID, nome, email, senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);
        setTitle("Cadastrar Usuário");
        iniciaComponentes();

        btnCadastrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtNomeCadastro.getText().toString().isEmpty() ||
                        edtEmailCadastro.getText().toString().isEmpty() ||
                        edtSenhaCadastro.getText().toString().isEmpty() ||
                        edtConfirmarSenhaCadastro.getText().toString().isEmpty())
                {
                    Snackbar snackbar= Snackbar.make(view, mensagens[0], Snackbar.LENGTH_SHORT);
                }
                else{
                    if(edtSenhaCadastro.getText().toString().equals(edtConfirmarSenhaCadastro.getText().toString())){
                        cadastrarUsuario(view);
                    }
                    else
                    {
                        Snackbar snackbar = Snackbar.make(view,"As senhas não coincidem.", Snackbar.LENGTH_SHORT);
                    }
                }
            }
        });
    }

    private void iniciaComponentes() {
        edtNomeCadastro = findViewById(R.id.edtNomeCadastro);
        edtEmailCadastro = findViewById(R.id.edtEmailCadastro);
        edtSenhaCadastro = findViewById(R.id.edtSenhaCadastro);
        edtConfirmarSenhaCadastro = findViewById(R.id.edtConfirmarSenhaCadastro);
        btnCadastrarUsuario = findViewById(R.id.btnCadastrarUsuario);
    }

    private void cadastrarUsuario(View view) {
        email = edtEmailCadastro.getText().toString();
        senha = edtSenhaCadastro.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    salvarDadosUsuario();
                    Snackbar snackbar = Snackbar.make(view, mensagens[1], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                } else {
                    String erro;
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erro = "Digite uma senha com no minimo 6 digitos";
                        Log.e("Erro", e.toString());
                    } catch (FirebaseAuthUserCollisionException e) {
                        erro = "Digite uma senha com no minimo 6 digitos";
                        Log.e("Erro", e.toString());
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erro = "E-mail inválido!";
                        Log.e("Erro", e.toString());
                    } catch (Exception e) {
                        erro = "Erro ao cadastrar usuário!";
                        Log.e("Erro", e.toString());
                    }
                    Snackbar snackbar = Snackbar.make(view, erro, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            }

            private void salvarDadosUsuario() {
                nome = edtNomeCadastro.getText().toString();
//                senha = edtSenhaCadastro.getText().toString();
//                email = edtEmailCadastro.getText().toString();
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                Map<String, Object> usuarios = new HashMap<>();
                usuarios.put("nome", nome);
//                usuarios.put("senha", senha);
//                usuarios.put("email", email);

                //Pega o usuário autenticado no banco
                usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);

                documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("db", "Sucesso ao salvar os dados!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("db", "Erro ao salvar os dados " + e.toString());
                    }
                });

            }
        });
    }
}