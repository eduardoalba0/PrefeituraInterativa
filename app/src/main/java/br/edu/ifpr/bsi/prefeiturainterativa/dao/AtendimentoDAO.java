package br.edu.ifpr.bsi.prefeiturainterativa.dao;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Atendimento;
import br.edu.ifpr.bsi.prefeiturainterativa.util.FabricaFirebase;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class AtendimentoDAO {

    private Context context;
    private CollectionReference db;

    private Atendimento atendimento;
    private final List<Atendimento> atendimentos = new ArrayList<>();

    public AtendimentoDAO(Context context) {
        this.context = context;
        db = new FabricaFirebase(context).getDataBase().collection("Atendimentos");
    }

    public String inserir(Atendimento obj) {
        atendimento = new Atendimento();
        final SweetAlertDialog loading = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        loading.setTitleText("Gravando...");
        loading.show();
        db.add(obj).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading.dismiss();
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Erro!")
                        .setContentText("Ocorreu uma falha ao gravar o atendimento.")
                        .show();
                Log.e("ERRO", e.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                loading.dismiss();
                atendimento.set_ID(documentReference.getId());
                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Sucesso!")
                        .setContentText("Atendimento gravado com sucesso.")
                        .show();
            }
        });
        return atendimento.get_ID();
    }

    public String merge(Atendimento obj) {
        if (obj.get_ID() == null || obj.get_ID().equals("")) {
            return inserir(obj);
        }
        final SweetAlertDialog loading = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        loading.setTitleText("Alterando...");
        loading.show();
        db.document(obj.get_ID()).set(obj, SetOptions.merge()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading.dismiss();
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Erro!")
                        .setContentText("Ocorreu uma falha ao alterar o atendimento.")
                        .show();
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                loading.dismiss();
                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Sucesso!")
                        .setContentText("Atendimento alterado com sucesso.")
                        .show();
            }
        });
        return obj.get_ID();
    }

    public void remover(String _ID) {
        final SweetAlertDialog loading = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        loading.setTitleText("Removendo...");
        loading.show();
        db.document(_ID).delete().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading.dismiss();
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Erro!")
                        .setContentText("Ocorreu uma falha ao remover o atendimento.")
                        .show();
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                loading.dismiss();
                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Sucesso!")
                        .setContentText("Atendimento removido com sucesso.")
                        .show();
            }
        });
    }

    public Atendimento buscar(String _ID) {
        final SweetAlertDialog loading = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        loading.setTitleText("Buscando...");
        loading.show();
        db.document(_ID).get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading.dismiss();
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Erro!")
                        .setContentText("Ocorreu uma falha ao buscar o atendimento.")
                        .show();
            }
        }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                atendimento = new Atendimento();
                atendimento = documentSnapshot.toObject(Atendimento.class);
                loading.dismiss();
            }
        });
        return atendimento;
    }

    public List<Atendimento> listar() {
        final SweetAlertDialog loading = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        loading.setTitleText("Buscando...");
        loading.show();
        db.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    loading.dismiss();
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Erro!")
                            .setContentText("Ocorreu uma falha ao buscar o atendimento.")
                            .show();
                    return;
                }
                atendimentos.clear();
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    atendimento = new Atendimento();
                    documentSnapshot.toObject(Atendimento.class);
                    atendimentos.add(atendimento);
                }
                loading.dismiss();
            }
        });
        Log.e("Lista", "List" + atendimentos.size());
        return atendimentos;
    }

    public List<Atendimento> listar(String campo, String valor) {
        final SweetAlertDialog loading = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        loading.setTitleText("Buscando...");
        loading.show();
        db.whereGreaterThanOrEqualTo(campo, valor).whereLessThanOrEqualTo(campo, valor).get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading.dismiss();
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Erro!")
                        .setContentText("Ocorreu uma falha ao buscar o atendimento.")
                        .show();
            }
        }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                atendimentos.clear();
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    atendimento = new Atendimento();
                    atendimento = documentSnapshot.toObject(Atendimento.class);
                    atendimentos.add(atendimento);
                }
                loading.dismiss();
            }
        });
        return atendimentos;
    }

}
