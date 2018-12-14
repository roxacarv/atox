package com.atox.network.gui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atox.R;
import com.atox.network.dominio.Feirinha;

import java.util.List;

public class FeirinhaCustomAdapter extends RecyclerView.Adapter<FeirinhaCustomAdapter.FeirinhaViewHolder> {

    private List<Feirinha> listaFeirinhas;
    private Context context;

    public FeirinhaCustomAdapter(Context context, List<Feirinha> listaFeirinhas){
        this.context = context;
        this.listaFeirinhas = listaFeirinhas;
    }

    static class FeirinhaViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        TextView txtNomeFeirinha;
        TextView txtEndereco;
        TextView txtDia;

        FeirinhaViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            txtNomeFeirinha = mView.findViewById(R.id.textViewNomeFeirinha);
            txtEndereco = mView.findViewById(R.id.textViewLocalFeirinha);
            txtDia = mView.findViewById(R.id.textViewHorarioFuncionamento);



        }
    }

    @Override
    public FeirinhaViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_feirinha, parent, false);
        return  new FeirinhaViewHolder(view);

    }

    @Override
    public void onBindViewHolder(FeirinhaViewHolder holder, int position){


        Feirinha objetoFeirinha = listaFeirinhas.get(position);


        holder.txtNomeFeirinha.setText(objetoFeirinha.getNome());
        holder.txtEndereco.setText(objetoFeirinha.getEndereco());
        holder.txtDia.setText(objetoFeirinha.getDia()+
                " - " + objetoFeirinha.getHorarioInicio()
                +" até " + objetoFeirinha.getHorarioFim() );
    }

    @Override
    public int getItemCount() {
        return listaFeirinhas.size();
    }
}
