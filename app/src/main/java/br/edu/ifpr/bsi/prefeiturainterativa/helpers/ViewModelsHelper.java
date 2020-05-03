package br.edu.ifpr.bsi.prefeiturainterativa.helpers;

import java.io.File;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViewModelsHelper extends ViewModel {
    private MutableLiveData<File> imagemCamera;

    public MutableLiveData<File> getImagemCamera() {
        if (imagemCamera == null)
            imagemCamera = new MutableLiveData<>();
        return imagemCamera;
    }
}
