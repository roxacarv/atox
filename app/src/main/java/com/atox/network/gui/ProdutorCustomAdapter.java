package com.atox.network.gui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atox.R;
import com.atox.network.dominio.Produtor;

import java.util.List;

public class ProdutorCustomAdapter extends RecyclerView.Adapter<ProdutorCustomAdapter.ProdutorViewHolder>{
    private List<Produtor> dataList;
    private Context context;

    public ProdutorCustomAdapter(Context context,List<Produtor> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    static class ProdutorViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        TextView txtName;
        TextView txtCidade;
        TextView txtCooperativa;
        TextView txtEmail;

        ProdutorViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            txtName = mView.findViewById(R.id.textViewNomeProdutor);
            txtCidade = mView.findViewById(R.id.textViewCidadeProdutor);
            txtCooperativa = mView.findViewById(R.id.textViewCooperativa);
            txtEmail = mView.findViewById(R.id.textViewEmailProdutor);

        }
    }

    @Override
    public ProdutorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_produtor, parent, false);
        return new ProdutorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProdutorViewHolder holder, int position) {

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
