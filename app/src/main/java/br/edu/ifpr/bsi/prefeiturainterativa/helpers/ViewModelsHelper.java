package br.edu.ifpr.bsi.prefeiturainterativa.helpers;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViewModelsHelper extends ViewModel {
    private MutableLiveData<String> imagemString;

    public MutableLiveData<String> getImagemString() {
        if (imagemString == null)
            imagemString = new MutableLiveData<>();
        return imagemString;
    }
}
