package com.atox.navegacao.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.atox.R;

import static android.app.Activity.RESULT_OK;

public class EditarFragment extends Fragment {

    private static final String TAG = InicioFragment.class.getName();
    ImageView image;


    public View onCreate(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        image = (ImageView) view.findViewById(R.id.imageView2);
        return view;

    }

    public void onclick(View view){
        carregarImagem();
    }

    private void carregarImagem() {
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent, "selecione a aplicação"), 10);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RESULT_OK){
            Uri path = data.getData();
            image.setImageURI(path);
        }
    }
}
