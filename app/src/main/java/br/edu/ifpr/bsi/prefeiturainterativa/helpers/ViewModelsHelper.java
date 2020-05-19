package br.edu.ifpr.bsi.prefeiturainterativa.helpers;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Categoria;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Solicitacao;

public class ViewModelsHelper extends ViewModel {

    public ViewModelsHelper() {
        solicitacaoLiveData = new MutableLiveData<>();
        imagemLiveData = new MutableLiveData<>();
        imagensLiveData = new MutableLiveData<>();
        categoriasLiveData = new MutableLiveData<>();
        solicitacao = new Solicitacao();
        imagens = new ArrayList<>();
        categorias = new ArrayList<>();
        solicitacaoLiveData.postValue(solicitacao);
        imagensLiveData.postValue(imagens);
        categoriasLiveData.postValue(categorias);
    }

    private Solicitacao solicitacao;
    private List<Uri> imagens;
    private List<Categoria> categorias;

    private MutableLiveData<Solicitacao> solicitacaoLiveData;
    private MutableLiveData<Uri> imagemLiveData;
    private MutableLiveData<List<Uri>> imagensLiveData;
    private MutableLiveData<List<Categoria>> categoriasLiveData;

    public MutableLiveData<Solicitacao> getSolictacao() {
        return solicitacaoLiveData;
    }

    public Solicitacao getObjetoSolicitacao() {
        return solicitacao;
    }

    public MutableLiveData<Uri> getImagem() {
        return imagemLiveData;
    }


    public MutableLiveData<List<Uri>> getImagens() {
        return imagensLiveData;
    }

    public void postSolicitacao(Solicitacao solicitacao) {
        this.solicitacao = solicitacao;
        solicitacaoLiveData.postValue(solicitacao);
    }

    public void addImage(Uri imagem) {
        imagens.add(imagem);
        imagensLiveData.postValue(imagens);
    }

    public void removeImage(int index) {
        imagens.remove(index);
        imagensLiveData.postValue(imagens);
    }

    public List<Uri> getListImagens() {
        return imagens;
    }

    public MutableLiveData<List<Categoria>> getCategorias() {
        return categoriasLiveData;
    }

    public void addCategoria(Categoria categoria) {
        categorias.add(categoria);
        categoriasLiveData.postValue(categorias);
    }

    public void removeCategoria(int index) {
        categorias.remove(index);
        categoriasLiveData.postValue(categorias);
    }

    public List<Categoria> getListCategorias() {
        return categorias;
    }
}
