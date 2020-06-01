package br.edu.ifpr.bsi.prefeiturainterativa.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.controller.ActivitySolicitacaoVisualizar;
import br.edu.ifpr.bsi.prefeiturainterativa.helpers.SharedPreferencesHelper;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Aviso;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AvisoAdapter extends RecyclerView.Adapter<AvisoAdapter.ViewHolder> {

    private Activity context;
    private List<Aviso> avisos;

    public AvisoAdapter(Activity context, List<Aviso> avisos) {
        this.context = context;
        this.avisos = avisos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_avisos, parent, false);
        return new AvisoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(avisos.get(position));
    }

    @Override
    public int getItemCount() {
        return avisos.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Aviso aviso;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @OnClick({R.id.edl_titulo, R.id.edt_corpo})
        @Override
        public void onClick(View view) {
            if (aviso != null) {
                new SharedPreferencesHelper(context).removeAvisos(aviso);
                Intent intent = new Intent(context, ActivitySolicitacaoVisualizar.class);
                intent.putExtra("Aviso", aviso);
                context.startActivity(intent);
            }

        }

        public void setData(Aviso aviso) {
            this.aviso = aviso;
            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, new Locale("pt", "BR"));
            edl_titulo.setHelperText(df.format(aviso.getData()));
            edl_titulo.setHint(aviso.getCategoria());
            edt_corpo.setText(aviso.getCorpo());
        }

        @BindView(R.id.edl_titulo)
        TextInputLayout edl_titulo;

        @BindView(R.id.edt_corpo)
        TextInputEditText edt_corpo;
    }
}
