package com.atox.infra.negocio;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.atox.atoxlogs.AtoxLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Ilya Gazman on 3/6/2016.
 */
public class ArmazenarImagem {

    private String nomeDiretorio = "images";
    private String nomeArquivo = "image.png";
    private Context context;
    private boolean externo;
    private AtoxLog atoxLog;

    public ArmazenarImagem(Context context) {
        this.context = context;
    }

    public ArmazenarImagem setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
        return this;
    }

    public ArmazenarImagem setExterno(boolean externo) {
        this.externo = externo;
        return this;
    }

    public ArmazenarImagem setNomeDiretorio(String nomeDiretorio) {
        this.nomeDiretorio = nomeDiretorio;
        return this;
    }

    public String getNomeDiretorio() {
        return nomeDiretorio;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void salvar(Bitmap bitmapImage) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(criarArquivo());
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (Exception e) {
            atoxLog = new AtoxLog();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                atoxLog = new AtoxLog();
            }
        }
    }

    @NonNull
    private File criarArquivo() {
        File diretorio;
        if(externo){
            diretorio = getAlbumStorageDir(nomeDiretorio);
        }
        else {
            diretorio = context.getDir(nomeDiretorio, Context.MODE_PRIVATE);
        }
        if(!diretorio.exists() && !diretorio.mkdirs()){
            Log.e("ArmazenarImagem","Erro ao criar diretório " + diretorio);
        }

        return new File(diretorio, nomeArquivo);
    }

    private File getAlbumStorageDir(String albumName) {
        return new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
    }

    public static boolean armazenamentoExternoPodeSerEscrito() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static boolean armazenamentoExternoPodeSerLido() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public Bitmap carregar() {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(criarArquivo());
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            atoxLog = new AtoxLog();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                atoxLog = new AtoxLog();
            }
        }
        return null;
    }
}
