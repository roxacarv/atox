package com.atox.http.gui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atox.R;
import com.atox.http.dominio.Produtor;

import java.util.List;

public class ProdutorCustomAdapter extends RecyclerView.Adapter<ProdutorCustomAdapter.CustomViewHolder>{
    private List<Produtor> dataList;
    private Context context;

    public ProdutorCustomAdapter(Context context,List<Produtor> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        TextView txtName;
        TextView txtCidade;
        TextView txtCooperativa;
        TextView txtEmail;

        CustomViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            txtName = mView.findViewById(R.id.textViewNomeProdutor);
            txtCidade = mView.findViewById(R.id.textViewCidadeProdutor);
            txtCooperativa = mView.findViewById(R.id.textViewCooperativa);
            txtEmail = mView.findViewById(R.id.textViewEmailProdutor);

        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_produtor, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        holder.txtName.setText(dataList.get(position).getNome());
        holder.txtCidade.setText(dataList.get(position).getCidade());
        holder.txtCooperativa.setText(dataList.get(position).getCooperativa());
        holder.txtEmail.setText(dataList.get(position).getEmail());

        /*Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(dataList.get(position).getThumbnailUrl())
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.ic_launcher_background)
                .into(holder.coverImage);*/

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
