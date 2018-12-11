package com.atox.network.gui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atox.R;
import com.atox.network.dominio.Produtor;

import java.util.List;

public class ProdutorCustomAdapter extends RecyclerView.Adapter<ProdutorCustomAdapter.CustomViewHolder>{
    private final List<Produtor> dataList;

    public ProdutorCustomAdapter(Context context,List<Produtor> dataList){
        this.dataList = dataList;
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {

        final View mView;

        final TextView txtName;
        final TextView txtCidade;
        final TextView txtCooperativa;
        final TextView txtEmail;

        CustomViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            txtName = mView.findViewById(R.id.textViewNomeProdutor);
            txtCidade = mView.findViewById(R.id.textViewCidadeProdutor);
            txtCooperativa = mView.findViewById(R.id.textViewCooperativa);
            txtEmail = mView.findViewById(R.id.textViewEmailProdutor);

        }
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_produtor, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        holder.txtName.setText(dataList.get(position).getNome().toUpperCase());
        holder.txtCidade.setText(dataList.get(position).getCidade());
        holder.txtCooperativa.setText(dataList.get(position).getCooperativa().toLowerCase());
        holder.txtEmail.setText(dataList.get(position).getEmail());

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
