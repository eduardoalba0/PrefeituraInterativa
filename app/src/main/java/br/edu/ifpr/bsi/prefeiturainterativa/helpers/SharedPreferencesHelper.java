package br.edu.ifpr.bsi.prefeiturainterativa.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.edu.ifpr.bsi.prefeiturainterativa.model.Aviso;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Usuario;

public class SharedPreferencesHelper {
    private final String PREF_AVISOS = "AvisosPendentes",
            PREF_SOLICITACOES = "SolicitacoesPendentes",
            PREF_USUARIOS = "UsuariosAutenticados";

    private SharedPreferences preferences;
    private Gson gson;

    public SharedPreferencesHelper(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.gson = new Gson();
    }

    public void insertSolicitacao(Solicitacao solicitacao) {
        String json = preferences.getString(PREF_SOLICITACOES, "");
        List<Solicitacao> list = new ArrayList<>();
        if (json != null && !json.equals("") && !json.equals("[]"))
            Collections.addAll(list, gson.fromJson(json, Solicitacao[].class));
        list.add(solicitacao);
        preferences.edit().putString(PREF_SOLICITACOES, gson.toJson(list)).apply();
    }

    public void removeSolicitacao(Solicitacao solicitacao) {
        String json = preferences.getString(PREF_SOLICITACOES, "");
        if (json != null && !json.equals("") && !json.equals("[]")) {
            List<Solicitacao> list = new ArrayList<>();
            Collections.addAll(list, gson.fromJson(json, Solicitacao[].class));
            list.remove(solicitacao);
            preferences.edit().putString(PREF_SOLICITACOES, gson.toJson(list)).apply();
        }
    }

    public List<Solicitacao> getSolicitaoes() {
        String json = preferences.getString(PREF_SOLICITACOES, "");
        List<Solicitacao> list = new ArrayList<>();
        if (json != null && !json.equals("") && !json.equals("[]"))
            Collections.addAll(list, gson.fromJson(json, Solicitacao[].class));
        return list;
    }

    public void insertAviso(Aviso aviso) {
        String json = preferences.getString(PREF_AVISOS, "");
        List<Aviso> list = new ArrayList<>();
        list.add(aviso);
        if (json != null && !json.equals("") && !json.equals("[]"))
            Collections.addAll(list, gson.fromJson(json, Aviso[].class));
        preferences.edit().putString(PREF_AVISOS, gson.toJson(list)).apply();
    }

    public void removeAvisos(Aviso aviso) {
        String json = preferences.getString(PREF_AVISOS, "");
        if (json != null && !json.equals("") && !json.equals("[]")) {
            List<Aviso> list = new ArrayList<>();
            Collections.addAll(list, gson.fromJson(json, Aviso[].class));
            list.remove(aviso);
            preferences.edit().putString(PREF_AVISOS, gson.toJson(list)).apply();
        }
    }

    public void removeAvisos(Solicitacao solicitacao) {
        String json = preferences.getString(PREF_AVISOS, "");
        if (json != null && !json.equals("") && !json.equals("[]")) {
            List<Aviso> list = new ArrayList<>();
            List<Aviso> listRemover = new ArrayList<>();
            Collections.addAll(list, gson.fromJson(json, Aviso[].class));
            for (Aviso aviso : list) {
                if (aviso.getSolicitacao_ID().equals(solicitacao.get_ID())) {
                    listRemover.add(aviso);
                }
            }
            list.removeAll(listRemover);
            preferences.edit().putString(PREF_AVISOS, gson.toJson(list)).apply();
        }
    }

    public List<Aviso> getAvisos() {
        String json = preferences.getString(PREF_AVISOS, "");
        List<Aviso> list = new ArrayList<>();
        if (json != null && !json.equals("") && !json.equals("[]"))
            Collections.addAll(list, gson.fromJson(json, Aviso[].class));
        return list;
    }

    public boolean categoriaEquals(String solicitacaoID, String categoria) {
        String json = preferences.getString(PREF_AVISOS, "");
        if (json != null && !json.equals("") && !json.equals("[]")) {
            for (Aviso aviso : gson.fromJson(json, Aviso[].class)) {
                if (aviso.getSolicitacao_ID() != null
                        && aviso.getSolicitacao_ID().equals(solicitacaoID)
                        && aviso.getCategoria().equals(categoria))
                    return true;
            }
        }
        return false;
    }

    public boolean containsCategoria(String categoria) {
        String json = preferences.getString(PREF_AVISOS, "");
        if (json != null && !json.equals("") && !json.equals("[]")) {
            for (Aviso aviso : gson.fromJson(json, Aviso[].class)) {
                if (aviso.getCategoria().equals(categoria))
                    return true;
            }
        }
        return false;
    }

    public void insertUsuario(Usuario usuario) {
        String json = preferences.getString(PREF_USUARIOS, "");
        List<Usuario> list = new ArrayList<>();
        if (json != null && !json.equals("") && !json.equals("[]"))
            Collections.addAll(list, gson.fromJson(json, Usuario[].class));
        list.add(usuario);
        preferences.edit().putString(PREF_USUARIOS, gson.toJson(list)).apply();
    }

}
