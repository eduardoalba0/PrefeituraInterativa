package br.edu.ifpr.bsi.prefeiturainterativa.helpers;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViewModelsHelper extends ViewModel {

    public ViewModelsHelper() {
        imagemLiveData = new MutableLiveData<>();
        imagensLiveData = new MutableLiveData<>();
        imagens = new ArrayList<>();
        imagensLiveData.postValue(imagens);
    }

    private List<String> imagens;

    private MutableLiveData<String> imagemLiveData;
    private MutableLiveData<List<String>> imagensLiveData;


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
}
