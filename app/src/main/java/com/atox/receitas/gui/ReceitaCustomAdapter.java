package com.atox.receitas.gui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atox.R;
import com.atox.receitas.dominio.Receita;

import java.util.List;

public class ReceitaCustomAdapter extends RecyclerView.Adapter<ReceitaCustomAdapter.ReceitaViewHolder> {

    private List<Receita> listaReceita;
    private Context context;

    public ReceitaCustomAdapter(Context context, List<Receita> listaReceita){
        this.context = context;
        this.listaReceita = listaReceita;
    }

    static class ReceitaViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        TextView txtNomeReceita;

        ReceitaViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            txtNomeReceita = mView.findViewById(R.id.textViewNomeReceita);
        }
    }

    @Override
    public ReceitaCustomAdapter.ReceitaViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_receitas, parent, false);
        return  new ReceitaCustomAdapter.ReceitaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReceitaCustomAdapter.ReceitaViewHolder holder, int position){
        Receita objetoReceita = listaReceita.get(position);
        holder.txtNomeReceita.setText(objetoReceita.getNome());
    }

    @Override
    public int getItemCount() {
        return listaReceita.size();
    }
}
