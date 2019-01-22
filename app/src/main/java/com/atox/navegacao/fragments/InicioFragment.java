package com.atox.navegacao.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.atox.R;
import com.atox.navegacao.adapters.PlacesRecyclerViewAdapter;
import com.atox.network.dominio.Feirinha;
import com.atox.network.dominio.Produtor;
import com.atox.network.gui.FeirinhaCustomAdapter;
import com.atox.network.gui.ProdutorCustomAdapter;
import com.atox.network.infra.ObtemServicoDados;
import com.atox.network.infra.RetrofitInstanciaCliente;
import com.atox.receitas.dominio.Receita;
import com.atox.receitas.dominio.SecaoReceita;
import com.atox.receitas.negocio.ReceitaNegocio;
import com.atox.usuario.dominio.Pessoa;
import com.atox.usuario.dominio.SessaoUsuario;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InicioFragment extends Fragment {

    private static final String TAG = InicioFragment.class.getName();
    private SessaoUsuario sessaoUsuario;
    private TextView textViewNomeUsuario;
    // configurando a recycle view pra feirinhas
    private FeirinhaCustomAdapter fAdapter;
    private RecyclerView customRecycleViewFeirinha;


    // variaveis para configurar o lugares proximos
    private static final int LOC_REQ_CODE = 1;
    private List<Integer> placeTypes;
    protected GeoDataClient geoDataClient;
    protected PlaceDetectionClient placeDetectionClient;
    protected RecyclerView customRecyclerViewOrganicNearbyPlaces;
    private ReceitaNegocio receitaNegocio;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);
        sessaoUsuario = SessaoUsuario.getInstance();
        textViewNomeUsuario = (TextView) view.findViewById(R.id.textViewMsgBoasVindas);
        customRecycleViewFeirinha = (RecyclerView) view.findViewById(R.id.customRecyclerViewFeirinha);
        // criando a recycler view que ira mostrar as coisas
        customRecyclerViewOrganicNearbyPlaces = (RecyclerView) view.findViewById(R.id.customRecyclerViewOrganicNearbyPlaces);
        receitaNegocio = new ReceitaNegocio(this.getActivity());
        Pessoa pessoaLogada = sessaoUsuario.getPessoaLogada();
        if (pessoaLogada != null){
            textViewNomeUsuario.setText(view.getContext().getResources().getString(R.string.texto_bemvindo) +
                    " " +
                    pessoaLogada.getNome());
        }

        //codigo Rodrigo
        //testeInsercaoReceita();

        //codigo Andre
        testeInsercaoReceitaSemUsuario();




        /*Create handle for the RetrofitInstance interface*/
        ObtemServicoDados service = RetrofitInstanciaCliente.getRetrofitInstance().create(ObtemServicoDados.class);
        Call<List<Feirinha>> call = service.getAllFeirinhas();
        call.enqueue(new Callback<List<Feirinha>>() {
            @Override
            public void onResponse(Call<List<Feirinha>> call, Response<List<Feirinha>> response) {
                generateDataList(response.body(),customRecycleViewFeirinha, getContext(), fAdapter);
            }
            @Override
            public void onFailure(Call<List<Feirinha>> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

        // configurações do nearby places
        LinearLayoutManager recyclerLayoutManager =
                new LinearLayoutManager(view.getContext());
        customRecyclerViewOrganicNearbyPlaces.setLayoutManager(recyclerLayoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(customRecyclerViewOrganicNearbyPlaces.getContext(),
                        recyclerLayoutManager.getOrientation());
        customRecyclerViewOrganicNearbyPlaces.addItemDecoration(dividerItemDecoration);

        placeDetectionClient = Places.getPlaceDetectionClient(view.getContext(), null);

        getCurrentPlaceItems();
        // Inflate the layout for this fragment
        return view;
    }


    public void testeInsercaoReceitaSemUsuario(){
        SecaoReceita secaoIngredientes = new SecaoReceita();
        secaoIngredientes.setNome("Ingredientes");
        secaoIngredientes.setConteudo("Maionese\nMostarda\nKetchup.");

        SecaoReceita secaoModoPreparo = new SecaoReceita();
        secaoModoPreparo.setNome("Modo de preparo");
        secaoModoPreparo.setConteudo("1.Faca isso\n2.Faça aquilo\n3.Coloque no fogo");

        SecaoReceita secaoOutrasInfo = new SecaoReceita();
        secaoOutrasInfo.setNome("Outras informações");
        secaoOutrasInfo.setConteudo("Disponivel no site www.receitasloucas.com.br");

        List<SecaoReceita> secoesDaReceita = new ArrayList<>();
        secoesDaReceita.add(secaoIngredientes);
        secoesDaReceita.add(secaoModoPreparo);
        secoesDaReceita.add(secaoOutrasInfo);

        Receita novaReceita = new Receita();
        novaReceita.setNome("Empanado de nada");
        novaReceita.setSecoes(secoesDaReceita);

        Long idNovaReceita = receitaNegocio.cadastrarSemUsuario(novaReceita);
        Log.i(TAG, "id da receita " + novaReceita.getNome() + " : " + idNovaReceita);

        try {
            Receita receitaDoBanco = receitaNegocio.buscarReceitaPorId(idNovaReceita);
            Log.i(TAG, "Secao é null? " + (receitaDoBanco.getSecoes()== null));
        } catch (ExecutionException e) {
            Log.i(TAG, "DEU MERDA. " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            Log.i(TAG, "DEU MERDA 2. " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void testeInsercaoReceita(){
        //exemplo de como construir uma receita
        Receita novaReceita = new Receita();
        SecaoReceita secaoReceita = new SecaoReceita();
        secaoReceita.setNome("Como Fazer");
        secaoReceita.setConteudo("Faça dessa maneira");
        //secao é sempre uma lista, mesmo que haja apenas uma
        List<SecaoReceita> secoes = new ArrayList<>();
        secoes.add(secaoReceita);
        novaReceita.setNome("Frango Empanado");
        novaReceita.setSecoes(secoes);
        //exemplo de como construir uma receita
        //colocando a receita no banco
        //passar o id de usuário pra fazer o relacionamento das tabelas
        List<Long> longs = null;
        longs = receitaNegocio.cadastrar(sessaoUsuario.getUsuarioLogado().getUid(), novaReceita);
        //colocando a receita no banco
        //buscando a receita no banco (sempre retorna uma lista, mesmo que haja apenas uma)
        List<Receita> receitas = null;
        receitas = receitaNegocio.buscarReceitasDoUsuario(sessaoUsuario.getUsuarioLogado().getUid());
        if(receitas != null) {
            for (Receita receita : receitas) {
                Log.i("PERFILFRAGMENT", "Nome da receita: " + receita.getNome());
                Log.i("PERFILFRAGMENT", "nome da secao" + receita.getSecoes().get(0).getNome());
            }
        }
        //buscando a receita no banco
    }


    /*Method to generate List of data using RecyclerView with custom adapter*/
    private void generateDataList(List<Feirinha> listaFeirinhas, RecyclerView recycleViewProdutor, Context context, FeirinhaCustomAdapter fAdapter) {
        customRecycleViewFeirinha = recycleViewProdutor;
        fAdapter = new FeirinhaCustomAdapter(context, listaFeirinhas);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        customRecycleViewFeirinha.setLayoutManager(layoutManager);
        customRecycleViewFeirinha.setAdapter(fAdapter);
    }


    // metodos de acesso aos places
    private void getCurrentPlaceItems() {
        if (isLocationAccessPermitted()) {
            getCurrentPlaceData();
        } else {
            requestLocationAccessPermission();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentPlaceData() {
        Task<PlaceLikelihoodBufferResponse> placeResult = placeDetectionClient.
                getCurrentPlace(null);
        placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                Log.d(TAG, "current location places info");
                List<Place> placesList = new ArrayList<Place>();
                PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    placesList.add(placeLikelihood.getPlace().freeze());
                }
                likelyPlaces.release();

                List<Place> finalPlaces = new ArrayList<>();

                for(Place location : placesList) {
                    placeTypes = location.getPlaceTypes();
                    if(placeTypes.contains(Place.TYPE_RESTAURANT) || placeTypes.contains(Place.TYPE_GROCERY_OR_SUPERMARKET)) {
                        finalPlaces.add(location);
                    }
                }

                PlacesRecyclerViewAdapter recyclerViewAdapter = new
                        PlacesRecyclerViewAdapter(finalPlaces, getContext());
                customRecyclerViewOrganicNearbyPlaces.setAdapter(recyclerViewAdapter);
            }
        });
    }

    public boolean isLocationAccessPermitted() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    public void requestLocationAccessPermission() {
        ActivityCompat.requestPermissions((Activity) getContext(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOC_REQ_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOC_REQ_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                getCurrentPlaceData();
            }
        }
    }




}
