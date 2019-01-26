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
import com.atox.network.gui.FeirinhaCustomAdapter;
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
        inicializarVariaveis(view);
        carregaRecycleViewFeirinhas();
        carregaLocaisProximos(view);
        return view;
    }

    public Long inserirReceita(int tipo, String nomeReceita, String ingredientes, String modoPreparo,
                               String outrasInformacoes) throws ExecutionException, InterruptedException {
        List<SecaoReceita> secoesDaReceita = new ArrayList<>();
        SecaoReceita secaoIngredientes = montarSecaoReceita("Ingredientes", ingredientes);
        secoesDaReceita.add(secaoIngredientes);
        SecaoReceita secaoModoPreparo = montarSecaoReceita("Modo de preparo", modoPreparo);
        secoesDaReceita.add(secaoModoPreparo);

        SecaoReceita secaoOutrasInfo = montarSecaoReceita("Outras informações", outrasInformacoes);
        secoesDaReceita.add(secaoOutrasInfo);

        Receita novaReceita = montarReceita(tipo, nomeReceita, secoesDaReceita);
        Long idNovaReceita = receitaNegocio.cadastrarSemUsuario(novaReceita);

        return idNovaReceita;
    }

    public Receita montarReceita(int tipo, String nome, List<SecaoReceita> secoesReceita){
        Receita receita = new Receita();
        receita.setTipo(tipo);
        receita.setNome(nome);
        receita.setSecoes(secoesReceita);
        return receita;
    }

    public SecaoReceita montarSecaoReceita(String tituloSecao, String conteudoSecao){
        SecaoReceita secao = new SecaoReceita();
        secao.setNome(tituloSecao);
        secao.setConteudo(conteudoSecao);
        return secao;
    }

    public void inicializarVariaveis(View view){
        sessaoUsuario = SessaoUsuario.getInstance();
        textViewNomeUsuario = (TextView) view.findViewById(R.id.textViewMsgBoasVindas);
        customRecycleViewFeirinha = (RecyclerView) view.findViewById(R.id.customRecyclerViewFeirinha);
        customRecyclerViewOrganicNearbyPlaces = (RecyclerView) view.findViewById(R.id.customRecyclerViewOrganicNearbyPlaces);
        receitaNegocio = new ReceitaNegocio(this.getActivity());
        Pessoa pessoaLogada = sessaoUsuario.getPessoaLogada();
        if (pessoaLogada != null){
            textViewNomeUsuario.setText(String.format("%s %s", view.getContext().getResources().getString(R.string.texto_bemvindo), pessoaLogada.getNome()));
        }

    }



    public void carregaLocaisProximos(View view){
        LinearLayoutManager recyclerLayoutManager =
                new LinearLayoutManager(view.getContext());
        customRecyclerViewOrganicNearbyPlaces.setLayoutManager(recyclerLayoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(customRecyclerViewOrganicNearbyPlaces.getContext(),
                        recyclerLayoutManager.getOrientation());
        customRecyclerViewOrganicNearbyPlaces.addItemDecoration(dividerItemDecoration);

        placeDetectionClient = Places.getPlaceDetectionClient(view.getContext(), null);

        getCurrentPlaceItems();
    }

    public void carregaRecycleViewFeirinhas(){
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
    }





    private void generateDataList(List<Feirinha> listaFeirinhas, RecyclerView recycleViewProdutor, Context context, FeirinhaCustomAdapter fAdapter) {
        customRecycleViewFeirinha = recycleViewProdutor;
        fAdapter = new FeirinhaCustomAdapter(context, listaFeirinhas);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        customRecycleViewFeirinha.setLayoutManager(layoutManager);
        customRecycleViewFeirinha.setAdapter(fAdapter);
    }


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
