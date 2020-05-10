package br.edu.ifpr.bsi.prefeiturainterativa.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Categoria;

public class ViewModelsHelper extends ViewModel {

    public ViewModelsHelper() {
        imagemLiveData = new MutableLiveData<>();
        imagensLiveData = new MutableLiveData<>();
        categoriasLiveData = new MutableLiveData<>();
        imagens = new ArrayList<>();
        categorias = new ArrayList<>();
        imagensLiveData.postValue(imagens);
        categoriasLiveData.postValue(categorias);
    }

    private List<String> imagens;
    private List<Categoria> categorias;

    private MutableLiveData<String> imagemLiveData;
    private MutableLiveData<List<String>> imagensLiveData;
    private MutableLiveData<List<Categoria>> categoriasLiveData;


    public MutableLiveData<String> getImagemString() {
        return imagemLiveData;
    }

    public MutableLiveData<List<String>> getImagensString() {
        return imagensLiveData;
    }


    public void addImage(String string) {
        imagens.add(string);
        imagensLiveData.postValue(imagens);
    }

    public void removeImage(int index) {
        imagens.remove(index);
        imagensLiveData.postValue(imagens);
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
        Collections.sort(categorias, (categoria, t1) -> categoria.getDescricao().compareTo(t1.getDescricao()));
        categoriasLiveData.postValue(categorias);
    }

    public List<Categoria> getListCategorias() {
        return categorias;
    }
}
