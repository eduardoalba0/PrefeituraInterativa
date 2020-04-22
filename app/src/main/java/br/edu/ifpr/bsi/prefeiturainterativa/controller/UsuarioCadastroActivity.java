package br.edu.ifpr.bsi.prefeiturainterativa.controller;

import androidx.appcompat.app.AppCompatActivity;
import br.edu.ifpr.bsi.prefeiturainterativa.R;

import android.os.Bundle;
import android.transition.Fade;

public class UsuarioCadastroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_cadastro);
        startAnimation();
        //senha deve ter no minimo 6 caracteres para nao dar exception
    }
    public void startAnimation() {
        Fade fade = new Fade();
        fade.setDuration(1500);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);
    }

}
