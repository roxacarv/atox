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
    private RecyclerView customRecycleViewFeirinha;



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

    public Receita buscarReceitaPorId(Long idReceita) throws ExecutionException, InterruptedException {
        Receita receitaNoBanco = receitaNegocio.buscarReceitaPorId(idReceita);
        return receitaNoBanco;
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
            Log.i(TAG, "Execution Exception " + e.getMessage());
        } catch (InterruptedException e) {
            Log.i(TAG, "Interrupted Exception " + e.getMessage());
            Thread.currentThread().interrupt();

        }
    }

    public void inicializarVariaveis(View view){
        sessaoUsuario = SessaoUsuario.getInstance();
        textViewNomeUsuario = (TextView) view.findViewById(R.id.textViewMsgBoasVindas);
        customRecycleViewFeirinha = (RecyclerView) view.findViewById(R.id.customRecyclerViewFeirinha);
        customRecyclerViewOrganicNearbyPlaces = (RecyclerView) view.findViewById(R.id.customRecyclerViewOrganicNearbyPlaces);
        receitaNegocio = new ReceitaNegocio(this.getActivity());
        Pessoa pessoaLogada = sessaoUsuario.getPessoaLogada();
        if (pessoaLogada != null){
            textViewNomeUsuario.setText(view.getContext().getResources().getString(R.string.texto_bemvindo) +
                    " " +
                    pessoaLogada.getNome());
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
                generateDataList(response.body(),customRecycleViewFeirinha, getContext());
            }
            @Override
            public void onFailure(Call<List<Feirinha>> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }






    private void generateDataList(List<Feirinha> listaFeirinhas, RecyclerView recycleViewProdutor, Context context) {
        customRecycleViewFeirinha = recycleViewProdutor;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        customRecycleViewFeirinha.setLayoutManager(layoutManager);
        customRecycleViewFeirinha.setAdapter(new FeirinhaCustomAdapter(context, listaFeirinhas));
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



    public void inserirReceitasNoBD() throws ExecutionException, InterruptedException {

        inserirReceita(0, "Frango Grelhado com Salada de Chuchu",
                "- 1 kg de chuchu descascado e fatiado finamente\n- Sal a gosto\n- Azeite\n- Hortelã picadinha\n- Pimenta dedo-de-moça sem sementes e picadinha a gosto\n- 50 g de bacon picadinho\n- 3 rodelas de abacaxi (+/- 2 cm) sem o miolo, cortadas em cubos pequenos\n- Hortelã picadinha a gosto\n- 100g de manteiga\n- 2 colheres (sopa) de tempero de limão com pimenta\n- 1 fio de azeite\n- 800g de filé de frango (4 peitos) temperado com sal e azeite\n- 200ml de água aproximadamente\n" +
                        "TEMPERO DE LIMÃO COM PIMENTA (LEMON PEPPER)\n" +
                        "- 1 colher (sopa) de raspas da casca de limão\n- 1⁄2 colher (chá) de pimenta-do-reino moída\n- 1 colher (chá) de glutamato monossódico\n- 1 colher (café) de cúrcuma",
                "Em uma peneira coloque 1 kg de chuchu descascado e fatiado finamente, sal a gosto e deixe por pelo menos 30 minutos para desidratarEm seguida transfira o chuchu para um pano e seque bemColoque numa tigela e tempere com azeite, hortelã picadinha e pimenta dedo-de-moça sem sementes e picadinha a gostoReserveEm uma frigideira antiaderente refogue 50 g de bacon picadinho e quando estiver dourado retire e reserveNa mesma frigideira, doure 3 rodelas de abacaxi (+/- 2 cm) sem o miolo, cortadas em cubos pequenos, junte o bacon frito, hortelã picadinha a gosto e reserveEm uma tigela misture 100 g de manteiga e 1 colher (sopa) de tempero seco de limão com pimentaReserve na geladeiraNa mesma frigideira aqueça 1 fio de azeite e grelhe 800 g de filé de frango temperado com sal e azeite, pingando água se necessário para não queimarQuando o filé estiver cozido, retire da frigideira e jogue água para soltar o \"agarradinho\" do fundo da frigideiraDespeje, aos poucos, esse caldo sobre a manteiga temperada (reservada acima), misturando bem até obter um molho cremosoFatie na diagonal os filés de frango grelhados e despeje o molho sobre elesSirva com o abacaxi refogado com bacon e a salada de chuchu\n" +
                        "TEMPERO DE LIMÃO COM PIMENTA (LEMON PEPPER)\n" +
                        "Coloque sobre uma assadeira, 1 folha de papel alumínio e 1 colher (sopa) de raspas da casca de limãoLeve ao forno baixo pré-aquecido a 100°C por +/- 15 minutos, mexendo de vez em quandoRetire do fornoEm uma tigela, coloque ½ colher (chá) pimenta-do-reino moída, 1 colher (chá) de glutamato monossódico, 1 colher (café) de cúrcuma, as raspas de limão (reservadas acima), misture e utilize em seguida.",
                "Rende 4 porções"
        );
        inserirReceita(0,
                "Caldo de Frango e Calabresa da Sogra",
                "- 1 fio de óleo\n- 1 cebola média picada\n- 1 pimentão verde pequeno em cubos pequenos\n- 3 dentes de alho picados\n- 1⁄2 kg de linguiça calabresa cortada em pequenos cubos\n- 1 kg de aipim cozido\n- Água do cozimento do peito de frango e do aipim\n- 3 cubos de caldo de galinha caipira\n- 1 peito de frango cozido e desfiado\n- 4 folhas de couve cortadas em tiras finas\n- Coentro e cebolinha picados a gosto\n- 2 sachês de tempero pronto com colorau\n- Queijo ralado e limão para acompanhar o caldo",
                "Em uma panela grande, em fogo médio, coloque 1 fio de óleo e refogue 1 cebola média picada e 1 pimentão verde pequeno em cubos pequenos.\n" +
                        "Adicione 3 dentes de alho picados, ½ kg de linguiça calabresa cortada em pequenos cubos, misture e deixe fritar.\n" +
                        "Coloque no liquidificador 1 kg de aipim cozido, água do cozimento do peito de frango e do aipim, 3 cubos de caldo de galinha caipira e bata bem até ficar homogêneo.\n" +
                        "Transfira a mistura para a panela com a linguiça refogada e misture bem.\n" +
                        "Acrescente 1 peito de frango cozido e desfiado, 4 folhas de couve cortadas em tiras finas, coentro e cebolinha picados a gosto, misture bem e sirva em seguida com queijo ralado e limão a gosto.",
                null);
        inserirReceita(0,
                "Rolê de Frango",
                "RECHEIO\n" +
                        "- 2 colheres (sopa) de azeite\n- 1 colher (café) de alho\n- 1⁄2 xícara (chá) de cenoura ralada no ralo grosso (60 g)\n- 1⁄2 xícara (chá) de abobrinha ralada no ralo grosso (60 g)\n- Sal a gosto\n" +
                        "FRANGO\n" +
                        "- 200 g de peito de frango\n- Sal, açafrão e noz moscada a gosto\n- 1 colher (sopa) de azeite\n- 1 colher (café) de açúcar\n" +
                        "TOMATES\n" +
                        "-1 colher (sopa) de azeite\n- 1 colher (café) de gengibre picado\n- 1 colher (café) de alho picado\n- 1 pitada de pimenta calabresa\n- 300 g de tomate-cereja cortado ao meio\n- Folhas de coentro picadas a gosto\n" +
                        "FAROFA CROCANTE\n" +
                        "- 2 colheres (chá) de azeite\n- 1 colher (chá) de alho picado\n- 25 g de amêndoas laminadas picadas (¼ xícara de chá)\n- 3 fatias de pão de forma integral sem casca ralado no ralo grosso (1 xícara de chá)\n- Sal e pimenta-do-reino a gosto",
                "RECHEIO\n" +
                        "Em uma frigideira em fogo médio com 2 colheres (sopa) de azeite refogue 1 colher (café) de alho.Adicione ½ xícara (chá) de cenoura ralada no ralo grosso e refogue por cerca de 3 minutos.Acrescente ½ xícara (chá) de abobrinha ralada no ralo grosso, misture e tempere com sal a gosto.Retire do fogo e reserve.\n" +
                        "FRANGO\n" +
                        "Com uma faca corte 200 g de peito de frango ao meio no sentido do comprimento.Em seguida, tempere com sal, açafrão, noz moscada a gosto.Coloque as fatias de frango sobre um saco plástico e com um batedor de carne bata bem até ficar bem fino e formar um retângulo (cerca de 20 cm X 25 cm).Coloque em uma das pontas do retângulo de frango 1 xícara (chá) do recheio de legumes (reservado acima) e enrole como um rocambole, fechando bem.Em seguida, com ajuda do saco plástico, modele formando um retângulo e com a faca corte ao meio.Em uma frigideira com 1 colher (sopa) de azeite coloque 1 colher (café) de açúcar e deixe até dourar.\n" +
                        "Adicione os rolês de frango e frite todos os lados até dourar. Retire do fogo e reserve.\n" +
                        "TOMATES\n" +
                        "Coloque na mesma frigideira 1 colher (sopa) de azeite, 1 colher (café) de gengibre picado, 1 colher (café) de alho picado, 1 pitada de pimenta calabresa e leve ao fogo médio até dourar.\n" +
                        "Adicione 300 g de tomate-cereja cortado ao meio, folhas de coentro picadas a gosto, misture e retire do fogo.\n" +
                        "MONTAGEM\n" +
                        "Em um prato de servir coloque uma porção tomate refogado, o rolê de frango grelhado e a farofa crocante. Sirva em seguida.\n" +
                        "FAROFA CROCANTE\n" +
                        "Em uma frigideira em fogo baixo com 2 colheres (chá) de azeite refogue 1 colher (chá) de alho picado até começar a dourar.\n" +
                        "Adicione 25g de amêndoas laminadas picadas, 3 fatias de pão de forma integral sem casca ralado no ralo grosso, sal a gosto e deixe no fogo até secar (cerca de 10 minutos).\n" +
                        "Retire do fogo e utilize em seguida.",
                null);
        inserirReceita(0,
                "Frango ao Curry Verde",
                "1 fio de óleo de coc\n" +
                        "½ colher (sopa) de pasta de curry verde\n" +
                        "1 Kg de filé de peito de frango em cubos temperados com sal e pimenta-do-reino moída\n" +
                        "200g de cogumelos Paris lavados e fatiados\n" +
                        "600ml de leite de coco light\n" +
                        "Suco de 1 limão\n" +
                        "colher (sopa) de açúcar mascavo ou branco\n" +
                        "pedaço pequeno de gengibre\n" +
                        "Folhas de limão (se tiver de limão kafir, melhor)\n" +
                        "Folhas de coentro picadas\n" +
                        "Sal e pimenta-do-reino moída a gosto\n" +
                        "1 abacaxi com casca cortado ao meio e escavado",
                "Aqueça uma panela de fundo grosso ou wok em fogo baixo, com o óleo de coco e 1/2 colher (sopa) de pasta de curry.Coloque o frango e doure.Adicione 200 g de cogumelos Paris lavados e fatiados e deixe fritar por 1 a 2 minutos.Adicione 200 ml de leite de coco.Deixe apurar por mais 2 minutos.Complete com o restante do leite de coco.Deixe apurar.Adicione suco de 1 limão, 1 colher (sopa) de açúcar mascavo.Com dois infusores de chá, coloque o gengibre picado em um, e as folhas de limão no outro e coloque-os no molho para que aromatizem o curry.Tempere com folhas de coentro picadas, sal e pimenta-do-reino moída a gosto.Sirva o frango ao curry dentro de uma metade escavada de abacaxi e o arroz à parte.",
                null);
        inserirReceita(0,
                "Empanado de Frango",
                "- 500 g de sobrecoxa desossada e sem pele\n" +
                        "- 300 g de linguiça toscana de frango debulhada\n" +
                        "- Tempero completo a gosto\n" +
                        "- 1 ½ xícara (chá) de farinha de trigo (240 g)\n" +
                        "- 1/3 xícara (chá) de amido de milho (35 g)\n" +
                        "- 1 colher (chá) de fermento em pó\n" +
                        "- Sal a gosto\n" +
                        "- 2 xícaras (chá) de cerveja clara (480 ml)\n" +
                        "- Farinha panko a gosto",
                "- Em um processador coloque 500 g de sobrecoxa desossada e sem pele, 300 g de linguiça toscana de frango debulhada e pulse até ficar em pequenos pedaços. Desligue o processador.\n" +
                        "- Transfira a mistura processada para uma tigela, coloque tempero completo a gosto e misture. Com as mãos molhadas, pegue pequenas porções de massa e modele no formato desejado. Leve para o freezer até firmar.\n" +
                        "- Em uma tigela coloque 1 ½ xícara (chá) de farinha de trigo, 1/3 xícara (chá) de amido de milho, 1 colher (chá) de fermento em pó, sal a gosto e misture. Adicione 2 xícaras (chá) de cerveja clara e misture bem.\n" +
                        "- Retire os pedaços de frango do freezer, passe na mistura de cerveja (reservada acima) e em seguida na farinha panko. Frite em óleo quente até dourar. Retire e escorra em papel absorvente. Sirva em seguida.",
                "Rendimento: 25 unidades de 25 g");
        inserirReceita(0,
                "Bolinho de Frango de Itapetininga",
                "- 1 kg de farinha de milho- 3 colheres de polvilho azedo- 2 peitos de frango cozidos em tempero e caldo de galinha- Caldo do cozimento do frango- Cheiro-verde picado- Água",
                "1 - Em uma bacia coloque a farinha de milho, o cheiro-verde e um pouco de água. Acrescente o caldo de cozimento do frango e misture até obter uma massa homogênea e lisa. Deixe descansar um pouco.2 - Acrescente o polvilho dissolvido em um copo de água, amasse bem e faça bolinhos. Recheie com o frango desfiado. Se quiser, pode colocar mais cheiro-verde. Frite em óleo bem quente, escorra e sirva.",
                null);
        inserirReceita(0,
                "Frango ao Curry",
                "Molho\n- ½ colher (sopa) de manteiga\n- ½ colher (sopa) de óleo\n- 2 xícaras (chá) de cebola picada (300g)\n- 3 dentes de alho picados grosseiramente\n- ½ xícara (chá) de alho-poró picado (50g)\n- ½ xícara (chá) de cebolinha verde (a parte branca) picada (40g)\n- 1 pimenta dedo-de-moça sem sementes picada\n- 1 xícara (chá) de castanha de caju (150g)\n- 2 colheres (chá) de curry em pó\n- 4 xícaras (chá) de água (1 litro)\n- 2 colheres (chá) de sal\n" +
                        "Frango\n- 1 fio de óleo\n- ½ kg de peito de frango cortado em cubos e temperado com sal e pimenta-do-reino (2 xícaras de chá)\n- 1 xícara (chá) de molho de curry (feito acima)\n- Cerca de 1 xícara (chá) de água",
                "Molho1 – Em uma panela em fogo médio com ½ colher (sopa) de manteiga e ½ colher (sopa) de óleo, refogue 2 xícaras (chá) de cebola picada, 3 dentes de alho picados grosseiramente, ½ xícara (chá) de alho-poró picado, ½ xícara (chá) de cebolinha verde (a parte branca) picada, 1 pimenta dedo-de-moça sem sementes picada até dourar levemente e o fundo da panela ficar dourado (cerca de 2 minutos).\n" +
                        "2 – Adicione 1 xícara (chá) de castanha de caju, 2 colheres (chá) de curry em pó e refogue rapidamente (30 segundos). Acrescente 4 xícaras (chá) de água, 2 colheres (chá) de sal e após levantar fervura deixe cozinhando por 10 minutos.\n" +
                        "3 – Transfira a mistura para um liquidificador e bata BEM até formar uma mistura homogênea (cerca de 1 minuto). Desligue o liquidificador e reserve. (OBS: Este molho rende 6 xícaras de chá)\n" +
                        "Frango4 – Em uma frigideira com 1 fio de óleo doure ½ kg de peito de frango cortado em cubos e temperado com sal e pimenta-do-reino. Adicione 1 xícara (chá) de molho curry (feito acima), +/- 1 xícara (chá) de água, aos poucos, mexendo sempre, até a textura de molho desejada, e deixe cozinhar por mais ou menos 2 minutos. Desligue o fogo e sirva em seguida com legumes grelhados.",
                "Rende 4 porções");
        inserirReceita(0,
                "Frango Assado ao Leite",
                "- 6 dentes de alho levemente amassados\n" +
                        "- 20 pimentas-do-reino em grãos levemente amassadas\n" +
                        "- ½ maço de sálvia\n" +
                        "- 1 litro de leite\n" +
                        "- 1 xícara (chá) de sal (100g)\n" +
                        "- 1 frango inteiro\n" +
                        "- 1 limão siciliano cortado em rodelas",
                "1 – Em uma frigideira coloque 6 dentes de alho levemente amassados, 20 pimentas-do-reino em grãos levemente amassadas, ½ maço de sálvia e leve ao fogo médio para aquecer e liberar os aromas (cerca de 1 minuto).\n" +
                        "2 - Transfira para uma tigela, adicione 1 litro de leite, 1 xícara (chá) de sal e misture até dissolver o sal. Coloque nesta marinada 1 frango inteiro com o peito para baixo e deixe na geladeira de um dia para o outro.\n" +
                        "3 - Depois deste tempo retire o frango da marinada e peneire o leite. Coloque os temperos que ficaram na peneira e 1 limão siciliano cortado em rodelas dentro do frango. Com um papel absorvente seque a parte de fora do frango e em seguida amarre o buraco com um barbante.\n" +
                        "4 - Coloque uma assadeira no forno a 220°C por mais ou menos15 minutos. Retire a assadeira do forno, regue 1 fio de azeite, coloque o frango com o peito para cima e volte novamente a assadeira ao forno por 40 minutos. Desligue o forno e sem abrir a porta deixe o frango por mais 40 minutos lá dentro. Abra o forno e transfira o liquido que se formou na assadeira para uma molheira, retirando o excesso de gordura. Sirva o frango em seguida com arroz.",
                "Rendimento: 4 porções");
        inserirReceita(0,
                "Frango à Kiev",
                "- 2 filés de peitos de frango sem osso (+/- 400 g)\n- sal e pimenta-do-reino moída a gosto\n- 30 g de manteiga de ervas (+/- 1 colher de sopa)\n- farinha de trigo\n- 3 ovos batidos\n- 6 fatias de pão de forma integral ralado (180 g)\n- ¼ xícara (chá) de azeite\n" +
                        "Manteiga de ervas\n" +
                        "150g de manteiga em temperatura ambiente\n- 2 colheres (sopa) de salsinha bem picadinha\n- ½ colher (sopa) de alecrim picadinho\n- ½ colher (sopa) de tomilho picadinho",
                "1 - Com uma faca corte 2 filés de peitos de frango sem osso no sentido do comprimento sem separar as metades. Tempere com sal e pimenta-do-reino moída a gosto. No meio de cada metade de filé coloque 15 g de manteiga de ervas, dobre os peitos de frango escondendo bem a manteiga. Em seguida, passe os filés na farinha de trigo, em 3 ovos batidos e em seguida em 6 fatias de pão de forma integral ralado.\n" +
                        "2 - Em uma frigideira com ¼ xícara (chá) de azeite doure os peitos de frango (já empanados) por 1 minuto de cada lado. Retire do fogo e transfira-os para uma grade sobre uma assadeira e leve ao forno alto pré-aquecido a 200°C por 10 minutos. Retire do forno e sirva em seguida.\n" +
                        "Manteiga de ervas\n" +
                        "1 - Em uma tigela amasse 150g de manteiga em temperatura ambiente junto com 2 colheres (sopa) de salsinha bem picadinha,  ½ colher  (sopa) de alecrim picadinho e ½ colher (sopa) de tomilho picadinho até que todos os ingredientes estejam bem incorporados. Forme um rolinho e embrulhe num papel filme. Vá usando esta manteiga para dar sabor a grelhados, purê de batata, macarrão e torradas.",
                "Rende duas porções");
        inserirReceita(0,
                "Frango à Milanesa Cremoso",
                "1 peito de frango sem osso (+/- ½ kg)\n" +
                        "sal e pimenta-do-reino a gosto\n" +
                        "1 xícara (chá) de maionese (220 g)\n" +
                        "1/3 xícara (chá) de sopa de cebola (45 g)\n" +
                        "2 ovos\n" +
                        "1 pacote de salgadinho de milho triturado (170 g)",
                "1 - Com uma faca corte o peito de frango em filés. Em seguida, com um batedor, bata bem os filés de frango até ficarem bem finos e tempere com sal e pimenta-do-reino a gosto.\n" +
                        "2 - Numa tigela coloque 1 xícara (chá) de maionese, 1/3 xícara (chá) de sopa de cebola e misture. Reserve ¼ xícara (chá) de maionese com creme de cebola.\n" +
                        "3 - Pegue 1 filé de frango batido, coloque 1 colher (sopa) da mistura de maionese no centro do filé, dobre e junte bem as pontas para cima para não abrir. Repita o mesmo procedimento até terminarem os ingredientes.\n" +
                        "4 - Coloque numa outra tigela ¼ xícara chá de maionese com creme de cebola (reservada acima), 2 ovos e misture.\n" +
                        "5 - Passe os filés de frango recheados na mistura de maionese com ovos e em seguida em 1 pacote de salgadinho de milho triturado. Frite os filés de frango empanados até dourar. Retire e escorra em papel absorvente. Sirva em seguida com salada.",
                "Rende 4 porções");
        inserirReceita(0,
                "Fricassé de Frango",
                "2 coxas e sobrecoxas de frango sem osso, temperadas com sal e pimenta-do-reino a gosto\n" +
                        "3 colheres (sopa) de azeite\n" +
                        "1 talo de alho poró cortado em tirinhas\n" +
                        "2 dentes de alho amassados\n" +
                        "1 talo de salsão cortado em tirinhas\n" +
                        "1 cebola roxa cortada em tirinhas\n" +
                        "200g de champignon fresco fatiado\n" +
                        "1 pedaço de gengibre com casca ralado\n" +
                        "2 xicaras (chá) de creme de leite fresco\n" +
                        "100 ml de vinho branco suco de 1 limão siciliano\n" +
                        "salsinha picadinha a gosto\n" +
                        "raspas da casca de 1 limão siciliano",
                "1 – Numa frigideira anti-aderente com 1 fio de azeite grelhe 2 coxas e sobrecoxas de frango sem osso temperadas com sal e pimenta-do-reino a gosto. Assim que estiverem douradas dos dois lados, retire da frigideira e reserve. Na mesma frigideira acrescente 3 colheres (sopa) de azeite, 1 talo de alho poró cortado em tirinhas, 2 dentes de alho amassados, 1 talo de salsão cortado em tirinhas, 1 cebola roxa cortada em tirinhas, 200g de champignon fresco fatiado e refogue por 5 minutos.\n" +
                        "2 - Com a ajuda de um pano, esprema todo o suco de 1 pedaço de gengibre com casca ralado.\n" +
                        "3 – Retire as peles das coxas e sobrecoxas, corte em pedaços de sua preferência, volte para a frigideira, com os legumes (refogados acima), acrescente 100 ml de vinho branco, suco de 1 limão siciliano, o caldo do gengibre, 2 xicaras (chá) de creme de leite fresco e deixe cozinhar por 10 minutos. Salpique salsinha a gosto, raspas da casca de 1 limão siciliano, misture e sirva em seguida com arroz basmati.",
                null);
        inserirReceita(0,
                "Frango Recheado de Manteiga Composta",
                "Para o fundo do risoto\n" +
                        "1 cebola média\n" +
                        "1 cenoura média\n" +
                        "4 talos de salsão\n" +
                        "1/3 do talo de alho poró\n" +
                        "5 talos de salsinha\n" +
                        "1 ramo de tomilho fresco\n" +
                        "1 folha de louro\n" +
                        "6 grãos de pimenta preta inteira\n" +
                        "1 dente de alho\n" +
                        "4 unidades de cravo da índia\n" +
                        "3 litros de água\n" +
                        "2 tabletes de caldo de galinha\n" +
                        " \n" +
                        "Para o risoto\n" +
                        "500g de arroz arbóreo\n" +
                        "4 col. sopa de azeite\n" +
                        "180g de manteiga sem sal\n" +
                        "200 ml de vinho branco\n" +
                        "200g de queijo brie\n" +
                        "Sal e pimenta a gosto\n" +
                        "Fundo pré-preparado\n" +
                        " \n" +
                        "Manteiga Composta\n" +
                        "60g de manteiga sem sal\n" +
                        "50g de alho\n" +
                        "Tomilho fresco\n" +
                        "Salsa picada\n" +
                        "Sal a gosto\n" +
                        " \n" +
                        "Frango\n" +
                        "5 porções de filé de frango\n" +
                        "1 ovo\n" +
                        "1 col. sopa de leite\n" +
                        "Farinha de trigo\n" +
                        "Farinha de rosca\n" +
                        "Sal e pimenta a gosto\n" +
                        "400g de gordura vegetal hidrogenada para fritar",
                "Colocar a cebola, cenoura, salsão, alho poró, e salsinha inteiros numa panela grande.\n" +
                        "Fazer um sache com o tomilho, louro, pimenta preta, alho e cravo da índia em um pedaço de tule e também colocar na panela. Cobrir tudo com a água e levar ao fogo. Após ferver por 1 hora, desligar e adicionar os tabletes de caldo de galinha. Coar e reservar.\n" +
                        " \n" +
                        "Para o risoto\n" +
                        "Mantenha o fundo de legumes quente.\n" +
                        "Picar a cebola em cubinhos pequenos e suar numa panela com o azeite.\n" +
                        "Adicionar o arroz arbóreo. Após o arroz estar todo azeitado, colocar o vinho branco e deixar reduzir até quase secar. Aos poucos ir adicionando o fundo de legumes e cozinhar em fogo alto, mexendo sempre.\n" +
                        "Quando atingir o ponto de cozimento desejado, desligar o fogo, adicionar a manteiga gelada e o queijo brie picados em cubos, mexendo bem para juntar os ingredientes. Temperar com sal e pimenta a gosto.\n" +
                        "Manteiga Composta\n" +
                        "Amassar o alho com o sal e adicionar os outros ingredientes.\n" +
                        "Após obter uma pasta, levar à geladeira.\n" +
                        " \n" +
                        "Frango\n" +
                        "Corta os filés de frango em borboleta, lavar e enxugar bem.\n" +
                        "Temperar com sal e pimenta.\n" +
                        "Colocar a manteiga composta no meio do filé e fechar bem (com auxílio de palitos).\n" +
                        "Polvilhar o frango recheado com farinha de trigo, passar na mistura de ovo com leite, empanar na farinha de rosca e fritar na gordura vegetal hidrogenada bem quente por imersão.\n" +
                        "Dourar os dois lados e se necessário, levar ao forno para acabar o cozimento.\n" +
                        "Servir um risoto em porção central e alta no prato e apoiar o frango em cima.",
                null);

        //receitas de carne
        inserirReceita(1,
                "Baguete com carne moída",
                "300g de carne moída\n- 3 gomos de linguiça toscana sem pele (aproximadamente 280g)\n- 1 xícara (chá) de queijo minas padrão ralado no ralo grosso (aproximadamente 120g)\n- 1 sachê de tempero de carne\n- Pimenta caiena a gosto\n- Salsinha picadinha a gosto\n- Sal a gosto\n- 1 baguete",
                "Em uma tigela, coloque 300 g de carne moída, 3 gomos de linguiça toscana sem pele, 1 xícara (chá) de queijo minas padrão ralado no ralo grosso, 1 sachê de tempero de carne, pimenta caiena, salsinha picadinha e sal a gosto.\n" +
                        "Misture bem e reserve.\n" +
                        "Com uma faca de serra corte as pontas de uma baguete.\n" +
                        "Sobre uma superfície, coloque a baguete em pé.\n" +
                        "Em seguida, corte em 3 partes no sentido da largura.\n" +
                        "Com a ajuda do cabo de uma colher de pau, empurre o miolo para as laterais da baguete.\n" +
                        "Coloque uma porção da mistura de carne com queijo dentro de cada pedaço de baguete apertando bem.\n" +
                        "Em seguida, enrole cada pedaço de baguete em um papel filme e leve ao freezer até firmar (+/- 3 horas).\n" +
                        "Repita o mesmo procedimento com o restante da baguete cortada.\n" +
                        "Retire os pedaços de baguetes do freezer.\n" +
                        "Com uma faca de serra, corte fatias de mais ou menos 1 cm.\n" +
                        "Em seguida, grelhe dos dois lados em uma frigideira untada com azeite em fogo médio, com cuidado para não queimar o pão.\n" +
                        "Sirva em seguida.",
                null);
        inserirReceita(1,
                "Carne econômica de Natal",
                "- 6 folhas de sálvia\n- 1 ramo de alecrim picado\n- Sal e pimenta-do-reino a gosto\n- Sementes de erva doce (para a carne de porco)\n- 3 kg de costela\n- 1 ½ kg copa lombo\n- 2 colheres (sopa) de óleo aromatizado com alho\n- Suco de 1 limão siciliano\n- Vinho tinto a gosto (ou vinagre, ou aceto balsâmico)\n- Batatas cortadas em tiras\n- Óleo\n- 6 conchas de caldo de frango (ou caldo de carne)",
                "1 - Em uma tigela coloque 6 folhas de sálvia, 1 ramo de alecrim picado, sal e pimenta-do-reino a gosto e misture. Divida o tempero em 2 partes. Numa parte adicione sementes de erva doce.\n" +
                        "2 - Com uma faca fure 3 kg de costela e 1 ½ kg copa lombo. Em seguida passe os temperos (misturados acima) dentro dos furos e envolta das carnes. Regue as carnes com 2 colheres (sopa) de óleo aromatizado com alho e massageie bem. Regue com suco de 1 limão e vinho tinto a gosto (ou vinagre, ou aceto balsâmico). Coloque cada carne dentro de um saco plástico e deixe marinar dentro da geladeira de um dia para o outro.\n" +
                        "3 - Numa assadeira forre o fundo com batatas cortadas em tiras e sobre as batatas coloque a costela e o copa lombo e cubra com papel manteiga. Regue com óleo e 6 conchas de caldo de frango (para hidratar as carnes). Cubra a assadeira com papel alumínio e leve ao forno médio pré-aquecido a 180°C por +/- 3 horas.\n" +
                        "Outras informações:\n" +
                        "Carnes (preços médios):- 1kg e meio de copa lombo: (R$ 15,40)- 3 kg de costela bovina: R$ 60- Total: R$ 75,40\n" +
                        "Compare os preços- Média de preço das carnes no mercado- Copa Lombo: R$14 a R$ 22 o quilo- Tender: R$ 30 a R$ 50 o quilo- Costela bovina: R$15 a R$ 25 o quilo- Bacalhau: R$ 50 a R$ 90 reais o quilo.\n" +
                        "Economia- Para se ter a ideia da economia, 1 quilo de copa lombo está custando entre R$ 14 e R$ 22, dependendo do mercado que você comprar, enquanto que o quilo do tender é encontrado por R$ 30 a R$ 50. Ou seja, a copa lombo sai pela metade do preço do tender. Já a costela bovina custa entre R$15 a R$25. O bacalhau está saindo entre R$50 e R$90 o quilo, o que significa que a costela custa apenas 30% do valor do bacalhau.",
                null);
        inserirReceita(1,
                "Pastel de carne",
                "- 1 lata de creme de leite com soro (300g)\n- 2 ¼ xícaras (chá) de farinha de trigo (320g)\n- Sal a gosto\n- Recheio a gosto",
                "1 - Em uma tigela, coloque 1 lata de creme de leite com soro, 2 ¼ xícaras (chá) de farinha de trigo, sal a gosto e misture bem até formar uma massa homogênea. Coloque a massa em uma superfície lisa e enfarinhada e amasse bem com as mãos. Enrole em um filme plástico e leve para gelar por 30 minutos.2 - Transfira a massa (feita acima) para uma superfície lisa e enfarinhada. Com um rolo, abra a massa bem fina e corte discos (7cm de diâmetro). Coloque uma porção de recheio sobre cada disco de massa, dobre ao meio em meia lua e feche apertando as bordas com um garfo. Frite os pastéis em óleo quente até dourar. Retire e escorra em papel absorvente. Sirva em seguida.",
                null);
        inserirReceita(1,
                "Nhoque de banana-da-terra com carne de porco",
                "Nhoque de Banana-da-terra\n" +
                        "\n- 4 bananas-da-terra grandes\n- 1 cebola pequena\n- 3 dentes de alho\n- 1 gema de ovo\n- 2 colheres de sopa de azeite\n- 2 colheres de sopa de manteiga\n- 2 colheres sopa de amido + 1 xícara de água\n- 1 colher de chá de urucum em pó\n- 100g de queijo parmesão ralado\n- Sal\n" +
                        "Carne de Porco na Panela\n" +
                        "- 500g carne de pernil cortados em cubos\n- 2 xícaras de água\n- ½ limão (suco)\n- 1 pimenta vermelha\n- 2 tomates\n- Coentro\n- 2 dentes de alho\n- 1 cebola pequena\n- 1 tablete de caldo de carne dissolvido em água\n- ¼ de pimentão\n- 1 colher de sopa de açúcar\n- 2 colheres de sopa de creme de leite\n- Cominho em pó\n- Pimenta moída\n- Canela em pó\n- Sal",
                "Nhoque de Banana-da-terra\n" +
                        "Coloque para assar no forno as bananas inteiras com a casca por 10/15 minutos. Amasse a polpa e reserve. Refogue com azeite e manteiga o alho e a cebola picada. Quando as cebolas estiverem transparentes, adicione a banana amassada, a gema, o amido dissolvido em água, o urucum, o queijo parmesão e o sal. Mexa bem e deixe assar até a massa ficar consistente. Deixe esfriar na geladeira por 15 minutos e faça bolinhas ou corte no formato que desejar.  \n" +
                        " \n" +
                        "Carne de Porco na Panela\n" +
                        "Pique todos os ingredientes, como a pimenta, os tomates, o coentro, o alho, a cebola e o pimentão. Coloque tudo na panela de pressão, junto com a carne de porco em cubos, a água, o suco do limão, o tablete dissolvido, o açúcar, o creme de leite e os temperos. Feche e deixe em fogo médio cerca de 40 minutos. Se precisar, abra a panela e certifique-se de que a carne desmanchou até ficar totalmente desfiada.\n" +
                        " \n" +
                        "Montagem:Coloque os nhoques quentes num refratário pequeno e adicione a carne de porco por cima com o caldo para manter o nhoque que quente.",
                null);
        inserirReceita(1,
                "Barrinha de Carne",
                "3 xícaras (chá) de carne moída temperada com sal e pimenta-do-reino a gosto e refogada (390g)\n" +
                        "½ xícara (chá) de bacon picadinho (100g)\n" +
                        "1 xícara (chá) de muçarela ralada no ralo fino (100g)\n" +
                        "1 xícara (chá) de queijo parmesão ralado no ralo fino (80g)\n" +
                        "1 ovo\n" +
                        "cheiro verde picadinho a gosto\n" +
                        "10 fatias de pão de forma sem casca ralado no ralo grosso misturadas com 1 sachê de tempero sabor carne (ou sabor de sua preferência) (3 ½ xícaras de chá)\n" +
                        " \n" +
                        "Molho Defumado\n" +
                        "½ xícara (chá) de maionese\n" +
                        "½ xícara (chá) de leite\n" +
                        "1 xícara (chá) de molho barbecue\n" +
                        " \n" +
                        "Molho Picante\n" +
                        "½ xícara (chá) de maionese\n" +
                        "½ xícara (chá) de leite\n" +
                        "½ xícara (chá) de molho de pimenta",
                "1 - Numa tigela coloque 3 xícaras (chá) de carne moída temperada com sal e pimenta-do-reino a gosto e refogada, ½ xícara (chá) de bacon picadinho, 1 xícara (chá) de muçarela ralada no ralo fino, 1 xícara (chá) de queijo parmesão ralado no ralo fino, 1 ovo, cheiro verde picadinho a gosto e misture bem.\n" +
                        "2 - Coloque no fundo de uma assadeira (30 cm X 18 cm) bem untada com azeite 10 fatias de pão de forma sem casca ralado misturadas com 1 sachê de tempero sabor carne (ou sabor de sua preferência) e com as mãos pressione bem. Adicione a mistura de carne (feita acima) e pressione bem. Leve para assar em forno alto pré-aquecido a 200°C por +/- 15 minutos. Retire do forno, desenforme numa outra assadeira e leve novamente ao forno por + 5 minutos. Retire do forno, corte em palitos (2 cm X 6 cm) e sirva em seguida com salada verde e molho de sua preferência.",
                "1 - Numa tigela coloque 3 xícaras (chá) de carne moída temperada com sal e pimenta-do-reino a gosto e refogada, ½ xícara (chá) de bacon picadinho, 1 xícara (chá) de muçarela ralada no ralo fino, 1 xícara (chá) de queijo parmesão ralado no ralo fino, 1 ovo, cheiro verde picadinho a gosto e misture bem.\n" +
                        "2 - Coloque no fundo de uma assadeira (30 cm X 18 cm) bem untada com azeite 10 fatias de pão de forma sem casca ralado misturadas com 1 sachê de tempero sabor carne (ou sabor de sua preferência) e com as mãos pressione bem. Adicione a mistura de carne (feita acima) e pressione bem. Leve para assar em forno alto pré-aquecido a 200°C por +/- 15 minutos. Retire do forno, desenforme numa outra assadeira e leve novamente ao forno por + 5 minutos. Retire do forno, corte em palitos (2 cm X 6 cm) e sirva em seguida com salada verde e molho de sua preferência.");
        inserirReceita(1,
                "Sanduíche de Carne",
                "2 kg de carne moída (pá ou acém)\n" +
                        "1 pacote de sopa de cebola (68 g)\n" +
                        "sal e pimenta-do-reino moída a gosto\n" +
                        "250 g de muçarela em fatias\n" +
                        "250 g de bacon em fatias\n" +
                        " \n" +
                        "Molho Especial\n" +
                        "4 colheres (sopa) de pepino em conserva picadinho\n" +
                        "2 colheres (sopa) de farinha de trigo\n" +
                        "6 colheres (sopa) de catchup\n" +
                        "1 xícara (chá) de maionese\n" +
                        "50g de manteiga\n" +
                        "50g de requeijão\n" +
                        "400 ml de leite",
                "1 - Numa tigela tempere 2 kg de carne moída com 1 pacote de sopa de cebola, sal e pimenta-do-reino moída a gosto e divida em duas partes.\n" +
                        "2 - Numa assadeira retangular (40 cm X 31 cm) forrada com saco plástico espalhe metade da carne moída temperada, depois distribua 250 g de muçarela fatiada, cubra com o restante da carne moída (também aberta sobre um saco plástico), distribua sobre esta carne 250 g de bacon em fatias e com as mãos pressione levemente para que o bacon fique aderido na carne. Vire a assadeira com a carne sobre uma tábua e com uma faca corte a carne moída recheada em 15 pedaços.\n" +
                        "3 - Aqueça em fogo médio uma frigideira untada com óleo e grelhe primeiro os sanduiches de carne moída recheada com a parte do bacon para baixo por 2 minutos. Depois vire os sanduiches e grelhe do outro lado por mais 2 minutos. Sirva em seguida com o molho especial (receita abaixo).\n" +
                        "Outras informações:\n" +
                        "1 - Numa panela coloque 4 colheres (sopa) de pepino em conserva picadinho, 2 colheres (sopa) de farinha de trigo, 6 colheres (sopa) de catchup, 1 xícara (chá) de maionese, 50 g de manteiga, 50 g de requeijão, 400 ml de leite, misture e leve ao fogo médio, mexendo sempre, até formar um creme. Retire do fogo e leve para gelar.",
                "Rende 15 porções");
        inserirReceita(1,
                "Carne moída à moda Árabe",
                "1 berinjela média com casca\n" +
                        "1 colher (sopa) de óleo\n" +
                        "½ xícara (chá) de cebola picada\n" +
                        "½ colher (sopa) de alho picado\n" +
                        "¼ xícara (chá) de extrato de tomate (65g)\n" +
                        "½ pimenta dedo-de-moça com sementes picadinha sal, pimenta síria e cominho a gosto\n" +
                        "½ kg de carne moída\n" +
                        "¾ xícara (chá) de polpa de tomate (420g)\n" +
                        " \n" +
                        "Legumes à Moda Árabe \n" +
                        "1 colher (sopa) de óleo\n" +
                        "½ xícara (chá) de cebola picada\n" +
                        "½ colher (sopa) de alho picado\n" +
                        "100g de chuchu em cubos médios\n" +
                        "100g de cenoura em cubos médios\n" +
                        "50g de vagem em cubos médios\n" +
                        "¼ xícara (chá) de extrato de tomate (65g)\n" +
                        "½ pimenta dedo-de-moça com sementes picadinha sal, pimenta síria e cominho a gosto\n" +
                        "½ xícara (chá) de água\n" +
                        "100g de abobrinha em cubos médios\n" +
                        "3 tomates sem pele e sem sementes picados (2 xícaras de chá)\n" +
                        "¾ xícara (chá) de polpa de tomate (420g)\n" +
                        "6 ovos azeite e salsinha picada a gosto\n" +
                        "1 lata de tomate pelati (400g)\n" +
                        "6 ovos\n" +
                        "azeite e salsinha picada a gosto",
                "1 - Com um garfo fure 1 berinjela média com casca. Coloque a berinjela diretamente na chama do fogão e deixe assar, virando de vez em quando, até ficar bem cozida (+/- 10 minutos). (OBS: a casca queima e fica bem preta). Retire do fogo e deixe esfriar. Com a faca retire toda a casca e reserve a polpa. (OBS: deve resultar em +/- 130g de polpa de berinjela assada ou ½ xícara de chá)\n" +
                        "2 - Numa frigideira com 1 colher (sopa) de óleo refogue ½ xícara (chá) de cebola picada e ½ colher (sopa) de alho picado. Adicione ¼ xícara (chá) de extrato de tomate, ½ pimenta dedo-de-moça com sementes picadinha, sal, pimenta síria e cominho a gosto, ½ kg de carne moída, misture até a carne secar (+/- 15 minutos).\n" +
                        "3 - Acrescente ¾ xícara (chá) de polpa de tomate, 1 lata de tomate pelati, ½ xícara (chá) de polpa da berinjela assada (130g), misture, abaixe o fogo, tampe a frigideira e deixe cozinhar por +/- 20 minutos ou até o molho encorpar.\n" +
                        "4 - Destampe a frigideira, coloque 6 ovos sobre a carne, mexendo levemente com um garfo nas claras dos ovos para que eles entrem no molho, tampe a frigideira novamente e deixe cozinhar por cerca de 5 minutos, apenas para cozinhar as claras e deixar as gemas levemente cozidas. Retire do fogo, regue azeite e salpique salsinha picada a gosto. Sirva em seguida.\n" +
                        " \n" +
                        "Legumes à Moda Árabe\n" +
                        "1 -Numa frigideira com 1 colher (sopa) de óleo refogue ½ xícara (chá) de cebola picada e ½ colher (sopa) de alho picado. Adicione 100 g de chuchu em cubos médios, 100 g de cenoura em cubos médios, 50 g de vagem em cubos médios, ¼ xícara (chá) de extrato de tomate, ½ pimenta dedo-de-moça com sementes picadinha, sal, pimenta síria e cominho a gosto e ½ xícara (chá) de água e deixe cozinhar até que os legumes fiquem macios. (+/- 10 minutos).\n" +
                        "2 - Acrescente 100 g de abobrinha em cubos médios, 3 tomates sem pele e sem sementes picados e ¾ xícara (chá) de polpa de tomate, misture, abaixe o fogo, tampe a frigideira e deixe cozinhar por +/- 5 minutos ou até o molho encorpar.\n" +
                        "3 - Destampe a frigideira, coloque 6 ovos sobre os legumes, mexendo levemente com um garfo nas claras dos ovos para que eles entrem no molho, tampe a frigideira novamente e deixe cozinhar por cerca de 5 minutos, apenas para cozinhar as claras e deixar as gemas levemente cozidas. Retire do fogo, regue azeite e salpique salsinha picada a gosto. Sirva em seguida.",
                "Rende 4 porções");
        inserirReceita(1,
                "Carne Moída de Férias",
                "2kg de carne moída (acém ou pá)\n" +
                        "1 colher (sopa) de molho inglês\n" +
                        "1 colher (sopa) de tempero completo com pimenta\n" +
                        "2 tabletes de caldo de carne\n" +
                        "2 colheres (sopa) de azeite\n" +
                        " \n" +
                        "Bolonhesa \n" +
                        "1 lata de extrato de tomate (340g)\n" +
                        "½ xícara (chá) de vinho tinto\n" +
                        "1 xícara (chá) de água\n" +
                        "manjericão picado a gosto\n" +
                        " \n" +
                        "Recheio para pastel, pastel de forno, panqueca\n" +
                        "2 colheres (sopa) de azeite\n" +
                        "4 dentes de alho amassados (1 colher de sopa)\n" +
                        "1 cebola pequena picadinha (½ xícara de chá)\n" +
                        "2 tomates sem pele e sem sementes picados (1 xícara de chá)\n" +
                        "2 colheres (chá) de molho de soja\n" +
                        "ovo cozido, azeitona, tomate sem sementes e cheiro verde picados a gosto",
                "1 – Numa tigela coloque 2 kg de carne moída, 1 colher (sopa) de molho inglês, 1 colher (sopa) de tempero completo com pimenta, 2 tabletes de caldo de carne e misture bem. Coloque a mistura numa panela de pressão com 2 colheres (sopa) de azeite, leve ao fogo médio e após pegar pressão deixe por 30 minutos.\n" +
                        "2 – Desligue o fogo, retire a pressão e com uma escumadeira transfira a carne para a tigela da batedeira e bata até desfazer os blocos de carne (+/- 1 minuto).\n" +
                        "Outras informações:\n" +
                        "3 – Separe metade da carne (feita acima) e leve novamente para a panela de pressão com o caldo que sobrou na panela. Acrescente 1 lata de extrato de tomate (340 g), ½ xícara (chá) de vinho tinto, 1 xícara (chá) de água, tampe a panela e leve novamente ao fogo e após pegar pressão, abaixe o fogo e deixe por mais 15 minutos. Retire a pressão, acrescente manjericão picado a gosto e sirva em seguida com a massa de sua preferência.\n" +
                        "4 – Numa panela com 2 colheres (sopa) de azeite refogue 4 dentes de alho amassados, 1 cebola pequena picadinha até dourar levemente. Adicione a outra metade da carne, 2 tomates sem pele e sem sementes picados, 2 colheres (chá) de molho de soja e refogue por +/- 5 minutos. Desligue o fogo, acrescente ovo cozido, azeitona, tomate sem sementes e cheiro verde picados a gosto, misture e utilize em seguida.",
                "Rende 6 porções");
        inserirReceita(1,
                "Carne de Porco com Abacaxi no Forno",
                "800g de filé de porco sem osso\n" +
                        "sal e pimenta-do-reino a gosto\n" +
                        "1 lata de abacaxi em conserva (450g)\n" +
                        "200g de queijo prato ralado no ralo grosso\n" +
                        "5 batatas\n" +
                        "3 colheres (sopa) de azeite\n" +
                        "1 cebola picada\n" +
                        "200g de cogumelo em conserva\n" +
                        "1 caixinha de creme de leite (200g)\n" +
                        "100 g de queijo prato ralado no ralo grosso",
                "1 - Com uma faca corte 800g de filé de porco sem osso em bifes e com um martelo de bife bata na carne ligeiramente apenas para ficar mais fino. Transfira a carne para uma assadeira, untada com azeite, tempere com sal e pimenta-do-reino, coloque uma rodela de abacaxi em conserva, polvilhe uma porção de queijo prato ralado no ralo grosso e reserve.\n" +
                        "2 - Numa panela com água fervente cozinhe 5 batatas. Retire do fogo e escorra. Com a faca corte as batatas pela metade no sentido da largura e deixe esfriar. Com uma colher retire a polpa de cada batata. Reserve as batatas.\n" +
                        "3 - Coloque 3 colheres (sopa) de azeite, 1 cebola picada, 200g de cogumelo numa frigideira e refogue bem (+/- 5 minutos). Acrescente 1 caixinha de creme de leite, tempere com sal e pimenta-do-reino a gosto, misture e deixe reduzir (+/- 2 minutos). Retire do fogo.\n" +
                        "4 - Coloque uma porção do creme de cebola com champignons (feito acima) dentro de cada batata. Salpique 100g de queijo prato ralado no ralo grosso. Transfira as batatas para a assadeira com a carne (reservada acima). Leve ao forno médio pré-aquecido a 180°C por +/- 30 minutos. Retire as batatas da assadeira e leve novamente ao forno por + 10 minutos para terminar de assar a carne. Retire do forno. Em pratos de servir coloque uma batata recheada, um bife com abacaxi e sirva em seguida.",
                "Rende 5 porções");
        //Receitas de tortas
        inserirReceita(2,
                "Torta de Chocolate Cremosa",
                "TORTA\n" +
                        "200g de biscoito de chocolate sem recheio triturado\n50g de manteiga gelada\n3 ovos\n150ml de creme de leite fresco1 xícara de chá de açúcar\n300g de cream cheese\n300g de chocolate meio amargo picado e derretido\n" +
                        "CHANTILLY\n" +
                        "500ml de creme de leite fresco bem gelado\n100g de chocolate meio amargo ralado fino",
                "Em uma tigela coloque 200 g biscoito de chocolate sem recheio triturado, 50g de manteiga gelada.Misture bem.Transfira a massa para uma forma de fundo falso (22 cm de diâmetro), forre fundo e lateral formando uma borda de 4 cm de altura utilizando as costas de uma colherReserve.Coloque num liquidificador 3 ovos, 150ml de creme de leite fresco, 1 xícara (chá) de açúcar, 300g de cream cheese e bata bem até formar uma mistura homogênea.Com o liquidificador ainda ligado, adicione 300g de chocolate meio amargo, picado e derretido e bata até misturar.Desligue o liquidificador, despeje a mistura sobre a massa de biscoito (feita acima) e leve para assar em forno médio pré-aquecido a 180°C por mais ou menos 40 minutos.Retire do forno e leve para gelar por 1 hora.Desenforme, decore com chantilly de chocolate, raspas de chocolate e sirva em seguida.\n" +
                        "CHANTILLY\n" +
                        "Em uma batedeira coloque 2 xícaras (chá) de creme de leite fresco bem gelado, 100 g de chocolate meio amargo ralado fino.Bata bem até ficar em ponto de chantilly.Utilize em seguida.",
                null);
        inserirReceita(2,
                "Empatorta de Peru",
                "700g de farinha de trigo2 xícaras (chá) de manteiga sem sal (400 ml)1⁄2 colher (sopa) de fermento em pó1 colher (chá) de sal1 ovo100ml de água1 clara\n" +
                        "Recheio\n" +
                        "1 peru com 5 kg1 colher (sopa) de óleo1 colher (sopa) de alho picado1 xícara (chá) de cebola picada (150 g)1 colher (sopa) de colorau1 1⁄2 xícara (chá) de tomate sem pele e sem sementes picadinho (300 g)6 colheres (sopa) de extrato de tomate (150 g)1.8 kg de peru assado e desfiado• Caldo do cozimento do peru2 de requeijão gelados (440 g)• Salsinha picadinha1 gema",
                "1. Em uma tigela coloque 700 g de farinha de trigo, 2 xícaras (chá) de manteiga sem sal, ½ colher (sopa) de fermento em pó e 1 colher (chá) de sal.\n" +
                        "2. Misture até formar uma farofa.\n" +
                        "3. Adicione 1 ovo, junte 100 ml de água aos poucos e misture até formar uma massa homogênea.\n" +
                        "4. Divida a massa em 2 partes.\n" +
                        "5. Coloque entre dois sacos plásticos e com um rolo abra a massa bem fina. Com uma metade forre o fundo e a lateral de uma forma redonda de fundo falso (30 cm de diâmetro X 6,5 cm de altura).\n" +
                        "6. Pincele em toda a massa 1 clara. Reserve.\n" +
                        "Recheio\n" +
                        "1. Corte 1 peru nas juntas, coloque em uma assadeira e leve ao forno preaquecido a 220 º C por 1 hora e 10 minutos.\n" +
                        "2. Desfie o peru e reserve. (OBS: 1 peru de 5 kg rende 1,8 kg de carne desfiada)Em uma panela em fogo médio com 1 colher (sopa) de óleo refogue 1 colher (sopa) de alho picado com 1 xícara (chá) de cebola picada.\n" +
                        "3. Adicione 1 colher (sopa) de colorau, 1 ½ xícara (chá) de tomate sem pele e sem sementes picadinho, 6 colheres (sopa) de extrato de tomate, o peru assado e desfiado e o caldo do cozimento do peru.\n" +
                        "4. Misture, tampe a panela e deixe cozinhar até secar, mexendo de vez em quando ( por cerca de 10 minutos).\n" +
                        "5. Retire do fogo e divida em 2 partes.\n" +
                        "6. Em uma das partes adicione 2 copos de requeijão gelado, salsinha picadinha a gosto e misture bem.\n" +
                        "OBS: A outra parte do recheio guarde no freezer para usar depois em outra preparação.Montagem\n" +
                        "Coloque o recheio de peru com requeijão no refratário forrado com a massa (reservada acima) e cubra com o restante da massa aberta.Pincele 1 gema e leve para assar em forno médio preaquecido a 180°C por cerca de 45 minutos ou até que a massa esteja dourada e assada.Retire do forno.Deixe esfriar e sirva",
                null);
        inserirReceita(2,
                "Torta de Pudim",
                "Caramelo \n" +
                        "- 180 g de açúcar (1 xícara de chá + 1 ½ colher de sopa)\n" +
                        "- 50 ml de água (¼ xícara de chá)\n" +
                        "- 150 ml de creme de leite fresco (½ xícara de chá + 1 colher de sopa)\n" +
                        "- 20 g de manteiga gelada (1 colher de sopa cheia)\n" +
                        "Pudim \n" +
                        "- 1 lata de leite condensado (395 g)\n" +
                        "- 2 latas de leite (a mesma medida da lata de leite condensado = 600 ml)\n" +
                        "- 4 ovos\n" +
                        "Massa\n" +
                        "- 150 g de manteiga cortada em cubos (1 + ¼ xícara de chá)\n" +
                        "- 90 g de açúcar de confeiteiro peneirado (½ xícara de chá)\n" +
                        "- 50 g de farinha de castanha-do-pará (½ xícara de chá)\n" +
                        "- 350 g de farinha de trigo (3 xícaras de chá)\n" +
                        "- 1 ovo",
                "Caramelo \n" +
                        "- Em uma panela coloque 180 g de açúcar, 50 ml de água e leve ao fogo médio até derreter e ficar com cor de caramelo. Retire do fogo e acrescente 150 ml de creme de leite fresco e misture bem. Junte 20 g de manteiga gelada e misture até derreter a manteiga. Transfira o caramelo para uma tigela e leve para a geladeira até esfriar.\n" +
                        "Pudim \n" +
                        "Em um liquidificador coloque 1 lata de leite condensado, 2 latas de leite, 4 ovos e bata bem até ficar homogêneo. Desligue o liquidificador, retire a espuma que se formou e reserve.\n" +
                        "Massa \n" +
                        "- Coloque em uma tigela 150 g de manteiga cortada em cubos, 90 g de açúcar de confeiteiro peneirado, 50 g de farinha de castanha-do-pará, 350 g de farinha de trigo e misture com as pontas dos dedos até formar uma farofa. Acrescente 1 ovo e misture bem até formar uma massa homogênea. - Em forminhas individuais de fundo falso (8 cm de diâmetro x 2 cm de altura) coloque uma porção da massa (feita acima) forrando fundo e lateral. Leve a massa para assar em forno médio preaquecido a 180°C por aproximadamente 20 minutos. Retire do forno e deixe esfriar.\n" +
                        "Montagem \n" +
                        "- Em cada forminha com a massa (já assada) coloque uma porção de caramelo, espalhando bem no fundo e lateral. Em seguida, coloque uma porção do pudim (reservado acima). Leve novamente ao forno a 160°C por mais 20 minutos ou até dourar o pudim. Retire do forno e deixe esfriar. Desenforme e sirva em seguida.",
                "Rendimento: 17 tortinhas");
        inserirReceita(2,
                "Torta com Creme de Avelã",
                "Massa\n- ½ xícara (chá) de farinha de trigo (85g)\n- ¼ xícara (chá) de açúcar (50g)\n- 1 colher (chá) de fermento em pó\n- ¼ xícara (chá) de chocolate em pó (30g)\n- 2 xícaras (chá) de biscoito champanhe moído (150g)\n- ½ ovo- 50 g de manteiga gelada cortada em cubos (¼ xícara de chá)\n- ½ xícara (chá) de castanha de caju picada (60g)\n- 100 g de chocolate meio amargo picado (¾ xícara de chá)\n" +
                        "Recheio\n- 1 lata de leite condensado (395g)\n- 1 ½ ovo\n- 2 colheres (sopa) de chocolate em pó\n- 8 colheres (sopa rasa) de creme de chocolate com avelã (170g)",
                "Massa1 - Em uma tigela coloque ½ xícara (chá) de farinha de trigo, ¼ xícara (chá) de açúcar, 1 colher (chá) de fermento e m pó, ¼ xícara (chá) de chocolate em pó, 2 xícaras (chá) de biscoito champanhe moído, ½ ovo, 50 g de manteiga gelada cortada em cubos e misture bem até formar uma farofa. Reserve 1 xícara (chá) desta farofa.\n" +
                        "2 - Transfira o restante da farofa para uma forma redonda (22 cm de diâmetro) untada e polvilhada com chocolate em pó e com a ajuda de um saco plástico forre o fundo e 3 cm da lateral da forma.  Leve ao forno médio preaquecido a 180°C por 10 minutos. Retire do forno.\n" +
                        "3 - Coloque em uma outra tigela 1 xícara (chá) da farofa (reservada acima), ½ xícara (chá) de castanha de caju picada, 100 g de chocolate meio amargo picado, misture e reserve.\n" +
                        "Recheio4 - Em uma tigela misture com um batedor de arame 1 lata de leite condensado, 1 ½ ovo, 2 colheres (sopa) de chocolate em pó. Reserve.\n" +
                        "Montagem5 - Coloque sobre a massa assada 8 colheres (sopa rasa) de creme de chocolate com avelã. Em seguida coloque a mistura de leite condensado e chocolate em pó e cubra com o restante da farofa com castanha (reservada acima). Leve para assar em forno médio preaquecido a 180°C por cerca de 30 minutos. Retire do forno e deixe esfriar. Desenforme e sirva em seguida.",
                null);
        inserirReceita(2,
                "Torta de Tomate",
                "Massa- 1 ½ xícara (chá) de farinha de trigo (240 g)- 1 xícara (chá) de queijo parmesão ralado (90 g)- 1 ½ xícara (chá) de manteiga (150 g)- 1 gema\n" +
                        "Recheio- 1 ½ xícara (chá) de queijo prato ralado no ralo grosso (180 g)- ½ xícara (chá) de queijo parmesão ralado (50 g)- ½ xícara (chá) de maionese (100 g)- 1 ovo batido- sal e pimenta-do-reino moída a gosto- 300 g de tomate sem sementes picadinho, temperados com sal e escorrido na peneira (1 ½ xícara de chá)",
                "Massa1 - Em uma tigela, coloque 1 ½ xícara (chá) de farinha de trigo, 1 xícara (chá) de queijo parmesão ralado, 1 ½ xícara (chá) de manteiga, 1 gema e misture até formar uma massa homogênea. Coloque a massa em uma forma canelada de fundo falso (25 cm de diâmetro X 2,5 de altura) forrando fundo e lateral. Com um garfo fure toda a massa e leve para assar em forno médio pré-aquecido a 180°C por 25 minutos. Retire do forno e deixe esfriar.\n" +
                        "Recheio2 - Coloque em uma outra tigela 1 ½ xícara (chá) de queijo prato ralado no ralo grosso, ½ xícara (chá) de queijo parmesão ralado, ½ xícara (chá) de maionese, 1 ovo batido, sal e pimenta-do-reino moída a gosto e misture. Adicione 150 g de  tomate sem sementes picadinho, temperados com sal e escorrido na peneira.\n" +
                        "Montagem3 - Transfira a mistura de tomate para a assadeira com a massa (já assada), espalhe o restante dos tomates picadinhos (150 g) e leve novamente ao forno para assar por + 25 minutos. Retire do forno e deixe esfriar. Desenforme e sirva em seguida.",
                "Rendimento: 10 porções");
        inserirReceita(2,
                "Torta Cremosa de Coco e Requeijão",
                "Torta\n" +
                        "6 ovos\n" +
                        "1 lata de leite condensado (395 g)\n" +
                        "1 copo de requeijão (220 g)\n" +
                        "150 g de coco ralado fresco\n" +
                        "1 colher (sopa) de açúcar\n" +
                        " \n" +
                        "Calda Branca \n" +
                        "½ xícara (chá) de leite condensado\n" +
                        "¼ xícara (chá) de creme de leite fresco",
                "Torta\n" +
                        "1 - Num liquidificador coloque 6 ovos, 1 lata de leite condensado, 1 copo de requeijão (220 g), 150 g de coco ralado fresco e bata por uns 5 minutos.\n" +
                        "2 - Transfira a mistura para uma fôrma canelada (20 cm de diâmetro) de fundo falso forrada com papel manteiga e untada com manteiga. Polvilhe 1 colher (sopa) de açúcar e leve para assar em forno médio pré-aquecido a 180°C por +/- 30 minutos ou até firmar. Retire do forno, leve à geladeira por pelo menos 2 horas, desenforme e sirva em seguida.\n" +
                        "Calda Branca\n" +
                        "1 - Numa tigela coloque ½ xícara (chá) de leite condensado, ¼ xícara (chá) de creme de leite fresco, misture e utilize em seguida.",
                "Rende 11 porções");
        inserirReceita(2,
                "Torta de Nozes e Chocolate",
                "Massa \n" +
                        "1 pacote de bolacha de chocolate recheada com baunilha triturado\n" +
                        "50g de manteiga gelada (¼ xícara de chá)\n" +
                        "4 colheres (sopa) de farinha de trigo (40g)\n" +
                        "1 colher (sopa) de licor de chocolate (15ml)\n" +
                        " \n" +
                        "Recheio \n" +
                        "2 ovos\n" +
                        "1 xícara (chá) de açúcar mascavo (160g)\n" +
                        "½ xícara (chá) de farinha de trigo (80g)\n" +
                        "100g de manteiga em temperatura ambiente (½ xícara de chá)\n" +
                        "200 g de chocolate meio amargo picado (1 1/3 xícara de chá)\n" +
                        "1 xícara (chá) de nozes picadinhas",
                "Massa \n" +
                        "1 - Coloque em uma tigela 1 pacote de biscoito de chocolate recheado com baunilha triturado, 50 g de manteiga gelada, 4 colheres (sopa) de farinha de trigo, 1 colher (sopa) de licor de chocolate e misture bem até formar uma massa homogênea.\n" +
                        "2 - Em uma assadeira de fundo falso (20cm de diâmetro) forre o fundo e a lateral com a massa (feita acima) e reserve.\n" +
                        " \n" +
                        "Recheio \n" +
                        "3 - Coloque na batedeira 2 ovos, 1 xícara (chá) de açúcar mascavo e bata bem, em velocidade alta, até formar um creme fofo. Com a batedeira ligada adicione ½ xícara (chá) de farinha de trigo e bata. Acrescente 100g de manteiga em temperatura ambiente e bata até misturar. Desligue a batedeira, junte 200g de chocolate meio amargo picado, 1 xícara (chá) de nozes picadinhas e misture delicadamente.\n" +
                        "4 - Transfira este creme para a assadeira com a massa (reservada acima). Com uma espátula descole, com cuidado, o excesso de massa da lateral da assadeira e coloque sobre o recheio e leve para assar em forno médio pré-aquecido a 160°C por +/- 50 minutos ou até firmar. Retire do forno e deixe esfriar. Desenforme e sirva em seguida.",
                "Rende 12 porções");
        inserirReceita(2,
                "Torta de Pera com Chocolate",
                "400g de manteiga sem sal\n" +
                        "4 peras maduras pequenas (tipo portuguesa)\n" +
                        "4 xícaras de farinha de trigo\n" +
                        "3 xícaras de açúcar mascavo\n" +
                        "3 xícaras de açúcar cristal demerara\n" +
                        "3 cravos da índia\n" +
                        "1 barra de chocolate ao leite\n" +
                        "1 barra de chocolate meio amargo\n" +
                        "1⁄4 de xícara de canela em pó\n" +
                        "1⁄4 de xícara de rum",
                null,
                null);

        //Receitas de chocolate
        inserirReceita(3,
                "Brownie de Chocolate com Gengibre",
                "50 g farinha de milho fina\n" +
                        "10 g de cacau em pó\n" +
                        "250 g de chocolate meio amargo\n" +
                        "200 g de manteiga sem sal cortada em cubos\n" +
                        "20 ml de suco de gengibre\n" +
                        "5 ovos\n" +
                        "200 g de açúcar\n" +
                        "1 colher (chá) de fermento em pó\n" +
                        "100 g de nozes picadas grosseiramente",
                "1 - Coloque numa tigela a farinha de milho fina e o cacau em pó.\n" +
                        "2 - Misture e reserve.\n" +
                        "3 - Numa panela, em banho-maria, derreta o chocolate meio amargo picado com a manteiga sem sal cortada em cubos.\n" +
                        "4 - Retire do fogo.\n" +
                        "5 - Adicione o suco de gengibre e misture.\n" +
                        "6 - Acrescente a mistura de farinha com cacau em pó (reservada acima). Misture bem e reserve.\n" +
                        "7 - Numa batedeira, coloque os ovos e o açúcar. Bata bem até dobrar de volume.\n" +
                        "8 - Com a batedeira ainda ligada, adicione o fermento em pó e bata até misturar.\n" +
                        "9 - Desligue a batedeira. Acrescente a mistura de chocolate (reservada acima) e as nozes picadas. Misture.\n" +
                        "10 - Transfira a massa para uma assadeira retangular (18 cm X 30 cm) untada e forrada com papel manteiga.\n" +
                        "11 - Leve para assar em forno médio pré-aquecido a 180°C por +/- 40 minutos.\n" +
                        "12 - Retire do forno.\n" +
                        "13 - Cubra o brownie com papel manteiga.\n" +
                        "14 - Coloque outra assadeira do mesmo tamanho pressionando levemente o brownie para que fique mais compacto e úmido\n" +
                        "15 - Deixe por +/- 4 horas na geladeira.\n" +
                        "16 - Retire a assadeira de cima do brownie, desenforme, corte em quadrados e sirva em seguida.",
                "Rendimento: 20 porções");
        inserirReceita(3,
                "Taça Furiosa - Mousse de Chocolate Amargo com Uísque",
                "MOUSSE \n" +
                        "12 gemas peneiradas\n" +
                        "100 g de açúcar\n" +
                        "600 g de chocolate meio amargo derretido com 200 g de manteiga\n" +
                        "100 ml de uísque\n" +
                        "1 colher (sopa) de baunilha\n" +
                        "12 claras\n" +
                        "1 pitada de sal\n" +
                        "MORANGOS \n" +
                        "4 colheres (sopa) de açúcar\n" +
                        "150 ml de uísque\n" +
                        "500 g de morangos em cubos\n" +
                        "BACON\n" +
                        "5 tiras de bacon\n" +
                        "Açúcar mascavo a gosto",
                "MOUSSE \n" +
                        "1 - Em uma tigela coloque 12 gemas peneiradas, açúcar e bata bem até ficar fofo.\n" +
                        "2 - Adicione 600 g de chocolate meio amargo derretido com 200 g de manteiga, 100 ml de uísque, 1 colher (sopa) de baunilha, misture bem e reserve.\n" +
                        "3 - Coloque em uma batedeira 12 claras, 1 pitada de sal e bata bem até ficar em ponto de neve.\n" +
                        "4 - Desligue a batedeira, transfira as claras batidas para a mistura de chocolate e misture delicadamente.\n" +
                        "MORANGOS \n" +
                        "1 - Em uma panela coloque 4 colheres (sopa) de açúcar, 150 ml de uísque, 500 g de morangos em cubos e leve ao fogo baixo até formar uma calda mole. Retire do fogo e deixe esfriar.\n" +
                        " BACON \n" +
                        "1 - Em uma assadeira forrada com papel-manteiga coloque as 5 tiras de bacon e salpique açúcar mascavo a gosto até cobrir as tiras.\n" +
                        "2 - Leve ao forno médio preaquecido a 180°C até ficarem sequinhas e doces.\n" +
                        "3 - Retire do forno e deixe esfriar.\n" +
                        "MONTAGEM \n" +
                        "1 - Em taças de servir coloque uma porção de mousse de chocolate, um copinho plástico de café no meio da taça e leve ao freezer até endurecer.\n" +
                        "2 - Retire os copinhos de café das taças e nas cavidades que se formaram coloque o recheio de morango (reservado acima).\n" +
                        "3 - Cubra com a mousse de chocolate e leve à geladeira até endurecer.\n" +
                        "4 - Sirva em seguida com uma fatia de bacon caramelizada.",
                null);
        inserirReceita(3,
                "Lasanha de tapioca, Chocolate e Creme de Avelã",
                "300g massa de tapioca1 pote de creme de avelã\n200g de chocolate meio amargo em barra\n3 bananas maduras\n100ml de espumante\n200g de açúcar granulado\n100g de chocolate em pó meio amargo",
                "Faça as lâminas da tapioca (1 para cada porção). Reserve.\n" +
                        "Derreta o chocolate e o creme de avelã até que fiquem cremosos. Reserve.\n" +
                        "Faça a banana caramelizada: corte em rodelas e levar ao fogo médio com 1 copo de espumante até ferver. Após fervura, coloque o açúcar aos poucos até ponto de calda.\n" +
                        "Montagem do prato: para cada lâmina de tapioca, corte em cruz, fazendo assim 4 pedaços. Coloque o primeiro pedaço, em seguida, coloque o chocolate cremoso. Depois vai o segundo pedaço e logo depois o creme de avelã.\n" +
                        "Coloque o terceiro pedaço e sobre ele novamente o chocolate. Coloque o último pedaço da tapioca. Sobre ele, a banana caramelizada em calda de champanhe. Finalize com chocolate em pó no prato para decorar.\n" +
                        "Sirva quente.",
                null);
        inserirReceita(3,
                "Brownie de Chocolate com Gengibre",
                "50 g farinha de milho fina\n" +
                        "10 g de cacau em pó\n" +
                        "250 g de chocolate meio amargo\n" +
                        "200 g de manteiga sem sal cortada em cubos\n" +
                        "20 ml de suco de gengibre\n" +
                        "5 ovos\n" +
                        "200 g de açúcar\n" +
                        "1 colher (chá) de fermento em pó\n" +
                        "100 g de nozes picadas grosseiramente",
                "1 - Coloque numa tigela a farinha de milho fina e o cacau em pó.\n" +
                        "2 - Misture e reserve.\n" +
                        "3 - Numa panela, em banho-maria, derreta o chocolate meio amargo picado com a manteiga sem sal cortada em cubos.\n" +
                        "4 - Retire do fogo.\n" +
                        "5 - Adicione o suco de gengibre e misture.\n" +
                        "6 - Acrescente a mistura de farinha com cacau em pó (reservada acima). Misture bem e reserve.\n" +
                        "7 - Numa batedeira, coloque os ovos e o açúcar. Bata bem até dobrar de volume.\n" +
                        "8 - Com a batedeira ainda ligada, adicione o fermento em pó e bata até misturar.\n" +
                        "9 - Desligue a batedeira. Acrescente a mistura de chocolate (reservada acima) e as nozes picadas. Misture.\n" +
                        "10 - Transfira a massa para uma assadeira retangular (18 cm X 30 cm) untada e forrada com papel manteiga.\n" +
                        "11 - Leve para assar em forno médio pré-aquecido a 180°C por +/- 40 minutos.\n" +
                        "12 - Retire do forno.\n" +
                        "13 - Cubra o brownie com papel manteiga.\n" +
                        "14 - Coloque outra assadeira do mesmo tamanho pressionando levemente o brownie para que fique mais compacto e úmido\n" +
                        "15 - Deixe por +/- 4 horas na geladeira.\n" +
                        "16 - Retire a assadeira de cima do brownie, desenforme, corte em quadrados e sirva em seguida.",
                "Rendimento: 20 porções");
        inserirReceita(3,
                "Taça Furiosa - Mousse de Chocolate Amargo com Uísque",
                "MOUSSE \n" +
                        "12 gemas peneiradas\n" +
                        "100 g de açúcar\n" +
                        "600 g de chocolate meio amargo derretido com 200 g de manteiga\n" +
                        "100 ml de uísque\n" +
                        "1 colher (sopa) de baunilha\n" +
                        "12 claras\n" +
                        "1 pitada de sal\n" +
                        "MORANGOS \n" +
                        "4 colheres (sopa) de açúcar\n" +
                        "150 ml de uísque\n" +
                        "500 g de morangos em cubos\n" +
                        "BACON\n" +
                        "5 tiras de bacon\n" +
                        "Açúcar mascavo a gosto",
                "MOUSSE \n" +
                        "1 - Em uma tigela coloque 12 gemas peneiradas, açúcar e bata bem até ficar fofo.\n" +
                        "2 - Adicione 600 g de chocolate meio amargo derretido com 200 g de manteiga, 100 ml de uísque, 1 colher (sopa) de baunilha, misture bem e reserve.\n" +
                        "3 - Coloque em uma batedeira 12 claras, 1 pitada de sal e bata bem até ficar em ponto de neve.\n" +
                        "4 - Desligue a batedeira, transfira as claras batidas para a mistura de chocolate e misture delicadamente.\n" +
                        "MORANGOS \n" +
                        "1 - Em uma panela coloque 4 colheres (sopa) de açúcar, 150 ml de uísque, 500 g de morangos em cubos e leve ao fogo baixo até formar uma calda mole. Retire do fogo e deixe esfriar.\n" +
                        " BACON \n" +
                        "1 - Em uma assadeira forrada com papel-manteiga coloque as 5 tiras de bacon e salpique açúcar mascavo a gosto até cobrir as tiras.\n" +
                        "2 - Leve ao forno médio preaquecido a 180°C até ficarem sequinhas e doces.\n" +
                        "3 - Retire do forno e deixe esfriar.\n" +
                        "MONTAGEM \n" +
                        "1 - Em taças de servir coloque uma porção de mousse de chocolate, um copinho plástico de café no meio da taça e leve ao freezer até endurecer.\n" +
                        "2 - Retire os copinhos de café das taças e nas cavidades que se formaram coloque o recheio de morango (reservado acima).\n" +
                        "3 - Cubra com a mousse de chocolate e leve à geladeira até endurecer.\n" +
                        "4 - Sirva em seguida com uma fatia de bacon caramelizada.",
                null);
        inserirReceita(3,
                "Musse de Chocolate com Pimenta",
                "Chocolate meio amargo\n" +
                        "Pimenta dedo-de-moça\n" +
                        "Creme de leite\n" +
                        "Claras em neve\n" +
                        "Açúcar\n" +
                        "Xarope de baunilha",
                "1 - Quebre uma barra de chocolate em pedaços, misture com creme de leite e coloque no micro-ondas por 3 minutos para derreter.\n" +
                        "2 - Misture ao retirar.\n" +
                        "3 - Bata as claras em neve, acrescente açúcar e o chocolate derretido com creme de leite.\n" +
                        "4 - Bata toda essa mistura por mais três minutos na batedeira.\n" +
                        "5 - Pique a pimenta dedo-de-moça e o xarope de baunilha.\n" +
                        "6 - Misture.\n" +
                        "7 - Coloque nos potinhos individuais e coloque no freezer por 3 horas.\n" +
                        "8 - Antes de servir, coloque raspas de chocolate por cima.",
                null);
        inserirReceita(3,
                "Cookie crocante de chocolate",
                "- 225 g de chocolate meio amargo picado, derretido e temperado (1 ¾ xícara de chá)\n" +
                        "- 60 g de flocos de cereal (1 ½ xícara de chá)\n" +
                        "- 5 cookies picados (1 xícara de chá)\n" +
                        "- 3 biscoitos de chocolate com recheio de baunilha picados (1/2 xícara de chá)\n" +
                        "- 150 g de chocolate branco picado, derretido e temperado (1 1/4 xícara de chá)\n" +
                        "- 5 cookies picados (1 xícara de chá)\n" +
                        "- 3 biscoitos de chocolate com recheio de baunilha picados (1/2 xícara de chá)",
                "- Em uma tigela coloque 225 g de chocolate meio amargo picado, derretido e temperado, 60 g de flocos de cereal, 5 cookies picados, 3 biscoitos de chocolate com recheio de baunilha picados e misture bem.\n" +
                        "- Transfira a mistura feita acima para uma assadeira forrada com papel manteiga, espalhando bem e formando um disco único ou faça vários discos menores (8 cm de diâmetro). Com a ajuda de uma colher, coloque 150 g de chocolate branco picado, derretido e temperado no disco (ou nos discos). Em seguida, espalhe 5 cookies picados misturados com 3 biscoitos de chocolate com recheio de baunilha picados pressionando bem. Deixe secar por aproximadamente 30 minutos. Retire da assadeira, corte e sirva em seguida.",
                "Rendimento: 5 discos \n" +
                        "Custo da receita: R$5 cada disco\n" +
                        "Sugestão de venda: R$8");
        inserirReceita(3,
                "Bolo Mousse de Chocolate",
                "- 3 ovos\n" +
                        "- 1 lata de leite condensado (395 g)\n" +
                        "- 1 lata de creme de leite com soro (300 g)\n" +
                        "- 1 colher (sopa) de margarina\n" +
                        "- 1 xícara (chá) de achocolatado (140 g)\n" +
                        "- 2 colheres (sopa) de açúcar\n" +
                        "- 200 g de chocolate meio amargo picado (1 ½ xícara de chá)\n" +
                        "\n" +
                        "Calda de Chocolate\n" +
                        "- 150 ml de creme de leite fresco\n" +
                        "- 35 g de xarope de glucose ou glucose de milho transparente (3 colheres de sopa)\n" +
                        "- 250 g de chocolate amargo\n" +
                        "- 5 g de gelatina em pó sem sabor (½ colher de sopa) hidratada em 20 ml de água",
                "1- Em um liquidificador coloque 3 ovos, 1 lata de leite condensado, 1 lata de creme de leite com soro, 1 colher (sopa) de margarina, 1 xícara (chá) de achocolatado, 2 colheres (sopa) de açúcar e bata até misturar. Com o liquidificador ainda ligado, adicione 200 g de chocolate meio amargo picado e bata bem até formar uma mistura homogênea.\n" +
                        "2 - Transfira a mistura de chocolate (feito acima) para uma assadeira de fundo falso (20 cm X 7 cm de altura), untada e forre o fundo com um disco de papel manteiga e a lateral com uma tira de papel manteiga. Pincele o papel manteiga com margarina. Por fora da assadeira envolva-a toda com papel alumínio, tomando cuidado para não rasgar este papel e leve para assar em forno médio preaquecido a 180°C no banho-maria por cerca de 1 hora. Retire do forno e deixe esfriar. Desenforme e sirva em seguida com calda de chocolate.\n" +
                        "Calda de chocolate3 - Em uma panela coloque 150 ml de creme de leite fresco, 35 g de xarope de glucose e leve ao fogo médio até ferver. Retire do fogo.\n" +
                        "4 - Coloque em uma tigela 250 g de chocolate amargo, o creme de leite fervido e misture até derreter. Acrescente 5 g de gelatina em pó sem sabor hidratada em 20 ml de água e misture bem. Deixe amornar e sirva com o bolo.",
                null);

        //Receitas de mousses
        inserirReceita(4,
                "Mousse Light de Gorgonzola",
                "200g gorgonzola (ou 300g de mortadela)\n" +
                        "5 ovos caipiras\n" +
                        "1 xicara de chá (250ml) de leite\n" +
                        "sal e pimenta do reino a gosto",
                "1- Num liquidificador coloque 200g de gorgonzola (ou 300g de mortadela), 5 ovos, 250 ml de leite, sal e pimenta-do-reino a gosto e bata bem até formar uma mistura homogênea.\n" +
                        "OBS: se for fazer com mortadela, corte com uma tesoura a película que envolve a peça.\n" +
                        "2 - Transfira a mistura para forminhas individuais (4cm de diâmetro X 5cm de altura) untadas com manteiga e leve para assar no banho-maria em forno médio pré-aquecido a 160°C por +/- 20 minutos ou até firmar. Retire do forno e deixe esfriar. Desenforme e leve para gelar. Sirva em seguida.",
                "Se preferir, troque a pimenta do reino por pimenta rosa.\n" +
                        "Rende 10 porções");
        inserirReceita(4,
                "Mousse Rápida",
                "100g de pé-de-moleque picado (3/4 xícara de chá)\n" +
                        "200g de chocolate meio amargo derretido e temperado (1 + 1/3 xícara de chá)\n" +
                        "200g de doce de leite (3/4 xícara de chá)\n" +
                        "1 lata de creme de leite sem soro (3/4 xícara de chá)\n" +
                        "6 colheres (sopa) de cacau em pó (3/4 xícara de chá)",
                "1 - Numa tigela misture 100 g de pé-de-moleque picado com 200 g de chocolate meio amargo derretido e temperado. Rapidamente, com auxílio de um garfo, retire os pedaços de pé de moleque e deixe que o excesso de chocolate escorra. Depois transfira o pé-de-moleque para uma assadeira forrada com papel manteiga e deixe que sequem. Reserve o chocolate que banhou os pedaços de pé de moleque.\n" +
                        "2 - Numa batedeira coloque 200 g de doce de leite, 1 lata de creme de leite sem soro, 6 colheres (sopa) de cacau em pó, o chocolate meio amargo derretido (que banhou o pé de moleque) e bata bem até formar um creme homogêneo (+/- 5 minutos). Desligue a batedeira, adicione o pé-de-moleque e misture. Coloque em taças e leve para gelar por +/- 1 hora. Sirva em seguida.",
                "Rende 5 porções");
        inserirReceita(4,
                "Mousse de Cupuaçu",
                "200g de polpa de fruta (cupuaçu)\n" +
                        "1 lata de leite condensado\n" +
                        "1 lata de creme de leite\n" +
                        "½ pacote de gelatina sem sabor dissolvido em água quente\n" +
                        "folhas de hortelã e raspas de castanha para ornamentação do prato",
                "Bater todos os ingredientes no liquidificador\n" +
                        "Colocar em taças para gelar\n" +
                        "Decorar com as raspas e hortelã",
                null);
        inserirReceita(4,
                "Mousse Romeu e Julieta",
                "1 lata de leite condensado\n" +
                        "1 lata de creme de leite\n" +
                        "400g de queijo branco normal\n" +
                        "1 gelatina em pó incolor\n" +
                        "1kg de goiabada cascão",
                "Bater todos os ingredientes no liquidificador, menos a gelatina\n" +
                        "Dissolver a gelatina em 5 colheres de sopa de água\n" +
                        "Depois que a gelatina estiver hidratada bater também no liquidificador\n" +
                        "Colocar tudo na forma que antes deve ser molhada com água gelada\n" +
                        "Depois levar ao freezer\n" +
                        "Desenformá-la em um prato",
                "Calda: Amoleça a goiabada em banho-maria até ela virar uma pasta homogênea\n" +
                        "Modo de servir: Na hora de servir coloque a calda de goiabada em cima da mousse");

        //Receitas de bolos
        inserirReceita(5,
                "Bolo ninho de páscoa",
                "MASSA\n" +
                        "3 ovos\n" +
                        "1⁄2 xícara (chá) de açúcar (100 g)\n" +
                        "1⁄2 xícara (chá) de leite (180 ml)\n" +
                        "1⁄2 colher (chá) de essência de baunilha\n" +
                        "1 xícara (chá) de farinha de trigo peneirada\n" +
                        "1⁄2 colher (sopa) de fermento em pó peneirado\n" +
                        "DECORAÇÃO\n" +
                        "200g de chocolate meio amargo picado e derretido\n" +
                        "400g de doce de leite",
                "MASSA\n" +
                        "Em uma batedeira coloque 3 ovos, ½ xícara (chá) de açúcar e bata bem, em velocidade alta, por cerca de 10 minutos.\n" +
                        "Diminua a velocidade da batedeira, adicione ½ xícara (chá) de leite, ½ colher (chá) de essência de baunilha e bata até misturar.\n" +
                        "Desligue a batedeira, adicione, aos poucos, 1 xícara (chá) de farinha de trigo peneirada, ½ colher (sopa) de fermento em pó peneirado, misturando com um batedor de arame.\n" +
                        "Transfira a massa (feita acima) para uma assadeira redonda (19 cm de diâmetro X 6 cm de altura) com o fundo untado e enfarinhado e leve para assar em forno médio preaquecido a 180°C por 30 minutos.\n" +
                        "Retire do forno e deixe esfriar. Depois de frio, com uma faca, corte um disco de 10 cm de diâmetro na superfície central do bolo e retire a tampa.\n" +
                        "Com cuidado, utilizando uma colher, retire a parte superior do bolo formando uma cavidade de cerca de 3 cm de altura (sem chegar até o final do bolo). Reserve.\n" +
                        "DECORAÇÃO \n" +
                        "Em uma tigela coloque 200 g de chocolate meio amargo picado e derretido, 400 g de doce de leite e misture até ficar homogêneo.\n" +
                        "Retire ½ xícara (chá) desta mistura e reserve. Em seguida, cubra todo o bolo com o restante da mistura de chocolate e doce de leite (feita acima).\n" +
                        "Coloque ½ xícara (chá) da mistura de chocolate (reservada acima) dentro de um saco de confeiteiro com bico liso nº 4 e faça riscos sobre o bolo, imitando um ninho.\n" +
                        "OBS: quanto mais fizer, mais ficará parecido com um ninho.\n" +
                        "Decore com raspas de chocolate e na cavidade coloque ovinhos de chocolate confeitados. Sirva a seguir.",
                "Rende 12 porções");
        inserirReceita(5,
                "Red Velvet (Bolo Naked)",
                "MASSA\n" +
                        "100ml de leite aquecido\n" +
                        "45g de manteiga (3 colheres de sopa)\n" +
                        "20g de chocolate em pó peneirado (2 ½ colheres de sopa)\n" +
                        "1 1⁄2 colher (sopa) ½ de corante vermelho em gel (30 g)\n" +
                        "4 ovos\n" +
                        "270g de açúcar (1 + ¼ de xícara de chá cheia)\n" +
                        "1⁄2colher (chá) de vinagre branco\n" +
                        "210g de farinha de trigo peneirada (1 xícara de chá + 1 colher de sopa)\n" +
                        "1⁄2colher (sopa) de fermento em pó peneirado\n" +
                        " \n" +
                        "RECHEIO\n" +
                        "350g de cream cheese amolecido (1 ½ xícara de chá)\n" +
                        "150g de manteiga amolecida (¾ xícara de chá)\n" +
                        "675g de açúcar de confeiteiro peneirado (4 ½ xícaras de chá)\n" +
                        "1⁄2colher (café) de essência de baunilha\n" +
                        "1pacote de gelatina em pó sem sabor (12 g) hidratada e dissolvida em 4 colheres (sopa) de água\n" +
                        "Frutas vermelhas a gosto",
                "Na batedeira coloque 335g de cream cheese amolecido, 150g de manteiga amolecida e bata em velocidade alta até ficar cremoso (cerca de 40 segundos).\n" +
                        "Junte 675 g de açúcar de confeiteiro peneirado, ½ colher (café) de essência de baunilha e bata rapidamente até misturar (40 segundos).\n" +
                        "Com a batedeira ainda ligada, adicione em fio 1 pacote de gelatina em pó sem sabor (12 g) hidratada e dissolvida em 4 colheres (sopa) de água.\n" +
                        "Desligue a batedeira e reserve a mistura na geladeira.\n" +
                        "MONTAGEM\n" +
                        "Retire o recheio da geladeira e separe 1 xícara (chá) para a cobertura. O restante do recheio divida em 2 partes iguais. Reserve.\n" +
                        "Com uma faca corte o bolo em 3 discos no sentido da largura.\n" +
                        "Coloque um disco no centro de um prato, e com o auxílio de um saco de confeiteiro cubra totalmente este disco de massa com uma camada de recheio.\n" +
                        "Coloque outro disco de massa sobre o recheio e novamente, com o auxílio de um saco de confeiteiro, cubra totalmente este outro disco de massa com outra camada de recheio.\n" +
                        "Cubra com o último disco de massa e decore o bolo com aquela xícara (chá) de recheio reservada.\n" +
                        "Decore com frutas vermelhas e sirva em seguida.",
                null);
        inserirReceita(5,
                "Bolo Chifon de limão",
                " 330 g de claras (10 unidades médias)\n" +
                        " - 2 g de sal\n" +
                        " - 1 g de cremor de tártaro (ou 5 gotas de suco de limão)\n" +
                        " - 120 g de açúcar (½ xícara de chá + 2 colheres de sopa)\n" +
                        " - 110 g de gemas (6 unidades médias)\n" +
                        " - 80 g de açúcar (½ xícara de chá)\n" +
                        " - 100 ml de óleo de canola\n" +
                        " - 140 ml de suco de limão\n" +
                        " - Raspas da casca de 1 limão grande\n" +
                        " - 135 g de farinha de trigo peneirada (1 xícara chá cheia)\n" +
                        " - 4 g de fermento químico peneirado (1 colher de chá)\n" +
                        " - 35 g de amido de milho peneirado (¼ xícara de chá)\n" +
                        " - Açúcar impalpável para polvilhar sobre o bolo",
                "1 - Em uma batedeira coloque 330 g de claras, 2 g de sal, 1 g de cremor de tártaro e bata bem até ficar em ponto de neve. Com a batedeira ainda ligada, adicione 120 g de açúcar, aos poucos, até ficar firme. Desligue a batedeira e reserve.\n" +
                        "2 - Coloque na batedeira 110 g de gemas, 80 g de açúcar e bata bem até ficar branco e fofo. Com a batedeira ainda ligada adicione 100 ml de óleo de canola, 140 ml de suco de limão, raspas da casca de 1 limão grande. Acrescente 135 g de farinha de trigo peneirada, 4 g de fermento químico peneirado, 35 g de amido de milho peneirado e bata por mais 1 minuto. Desligue a batedeira, junte a mistura de claras (reservada acima) e misture.\n" +
                        "3 - Transfira a mistura (feita acima) para uma forma para bolo chiffon (24 cm de diâmetro X 11 cm de altura) e leve para assar em forno médio preaquecido a 180°C por 1 hora. Retire do forno, vire a forma de cabeça para baixo e deixe esfriar por 1 hora. Passe uma faca na lateral e no miolo da forma e descole o fundo. Vire de cabeça para baixo num prato de servir e polvilhe açúcar impalpável. Sirva em seguida.",
                null);
        inserirReceita(5,
                "Bolo Brownie com Cookie",
                "Cookie\n" +
                        "- 1/3 xícara (chá) de manteiga sem sal (80g)\n" +
                        "- ½ xícara (chá) de açúcar mascavo (75g)\n" +
                        "- ¼ xícara (chá) de açúcar (55g)\n" +
                        "- 1 ovo - ½ colher (chá) de bicarbonato de sódio\n" +
                        "- 1 xícara (chá) de farinha de trigo (180g)\n" +
                        "- 1 xícara (chá) de chips de chocolate (190g)\n" +
                        "- 2 pacotes de biscoito recheado com sabor de sua preferência (280g)\n" +
                        "Brownie \n" +
                        "- 1 xícara (chá) de chocolate meio amargo derretido (225g)\n" +
                        "- 1/3 xícara (chá) de manteiga sem sal derretida (80g)\n" +
                        "- 2 ovos - ¾ xícara (chá) de açúcar (160g)\n" +
                        "- 2 colheres (sopa) de farinha de trigo",
                "Cookie \n" +
                        "1 - Coloque em uma batedeira 1/3 xícara (chá) de manteiga sem sal, ½ xícara (chá) de açúcar mascavo, ¼ xícara (chá) de açúcar e bata bem, em velocidade alta, por +/- 2 minutos. Adicione 1 ovo, ½ colher (chá) de bicarbonato de sódio, 1 xícara (chá) de farinha de trigo e bata bem por +/- 2 minutos. Desligue a batedeira, acrescente 1 xícara (chá) de chips de chocolate e misture.\n" +
                        "2 - Transfira a massa para uma assadeira (30cm X 18cm) forrada com papel manteiga e untado com manteiga espalhando bem com as costas de uma colher. Coloque sobre a massa 2 pacotes de biscoito recheado (sabor de sua preferência), um ao lado do outro. Reserve.\n" +
                        "Brownie \n" +
                        "3 - Em uma tigela coloque 1 xícara (chá) de chocolate meio amargo derretido, 1/3 xícara (chá) de manteiga sem sal derretida, 2 ovos, ¾ xícara (chá) de açúcar e 2 colheres (sopa) de farinha de trigo e misture com um batedor de arame até obter um creme liso e homogêneo.\n" +
                        "4 - Espalhe essa massa de brownie sobre os biscoitos e leve para assar em forno médio preaquecido a 180ºC por +/- 40 minutos. Retire do forno, deixe amornar e sirva em seguida.",
                "Rendimento: 20 porções");
        inserirReceita(5,
                "Bolo de Cenoura de Micro-ondas",
                "- 2 ovos\n" +
                        "- ½ xícara (chá) de óleo\n" +
                        "- 250g de cenouras médias sem casca e cortadas em rodelas\n" +
                        "- 1 xícara (chá) de farinha de trigo\n" +
                        "- 1 xícara (chá) de açúcar\n" +
                        "- ½ colher (sopa) de fermento em pó\n" +
                        "Calda\n" +
                        "- 6 colheres (sopa) de chocolate em pó\n" +
                        "- 400 ml de leite\n" +
                        "- 1 colher (sopa) de manteiga\n" +
                        "- 4 colheres (sopa) de açúcar\n" +
                        "- 2 colheres (sopa) de amido de milho",
                "1 – Em um liquidificador, coloque 2 ovos, ½ xícara (chá) de óleo, 250g de cenouras médias sem casca e cortadas em rodelas e bata bem até formar uma mistura homogênea.\n" +
                        "2 - Coloque em uma tigela 1 xícara (chá) de farinha de trigo, 1 xícara (chá) de açúcar, ½ colher (sopa) de fermento em pó, a mistura do liquidificador (feita acima) e misture bem com um batedor de arame até formar uma massa.\n" +
                        "3 - Coloque a massa em uma forma para micro-ondas bem untada com manteiga e leve ao micro-ondas por 8 minutos na potência máxima, abrindo o forno por alguns segundos de 2 em 2 minutos para que a massa não derrame. Retire do micro-ondas e deixe esfriar.\n" +
                        "4 - Depois de frio, desenforme o bolo, cubra com a calda de chocolate e sirva em seguida.\n" +
                        " \n" +
                        "Calda\n" +
                        "5- Em uma tigela, coloque 6 colheres (sopa) de chocolate em pó, 400 ml de leite, 1 colher (sopa) de manteiga, 4 colheres (sopa) de açúcar, 2 colheres (sopa) de amido de milho, misture e leve ao micro-ondas por 1 ½ minuto. Retire do micro-ondas, misture bem e volte novamente ao micro-ondas por + 1 minuto. Retire do micro-ondas e misture bem até esfriar. Depois de frio, cubra o bolo com esta calda.",
                null);
        inserirReceita(5,
                "Bolo colorido",
                "Massa\n" +
                        "5 ovos\n" +
                        "150g de açúcar (3/4 xícara de chá)\n" +
                        "150g de farinha de trigo (1 xícara de chá cheia)\n" +
                        "1 colher (sopa) de fermento em pó\n" +
                        "corante alimentício na cor de sua preferência\n" +
                        " \n" +
                        "Creme\n" +
                        "1kg de manteiga sem sal em temperatura ambiente\n" +
                        "1kg de açúcar de confeiteiro peneirado\n" +
                        "gotas de baunilha a gosto",
                "Massa \n" +
                        "1- Em uma batedeira coloque 5 ovos, 150g de açúcar e bata até obter um creme fofo (+/- 10 minutos).\n" +
                        "2 - Numa tigela misture 150g de farinha de trigo com 1 colher (sopa) de fermento em pó e depois vá peneirando (essa mistura de farinha de trigo) sobre o creme de ovos e açúcar, mexendo delicadamente.\n" +
                        "3- Pingue corante alimentício (na cor de sua preferência) a gosto.\n" +
                        "4 - Despeje a massa em 1 fôrma redonda com fundo falso (30 cm) untada e enfarinhada apenas no fundo da fôrma e leve para assar em forno médio pré-aquecido a 180 °C por +/- 20 minutos. Retire do forno e deixe esfriar.\n" +
                        "5 - Faça mais 4 bolos usando corante alimentício de cores diferentes. Reserve.\n" +
                        " \n" +
                        "Creme Manteiga\n" +
                        "6 - Numa batedeira coloque 1 kg de manteiga sem sal em temperatura ambiente, aos poucos, e bata bem até ficar branca (+/- 5 minutos). Com a batedeira ainda ligada, adicione, aos poucos, 1 kg de açúcar de confeiteiro peneirado, gotas de baunilha a gosto e bata até misturar. Desligue a batedeira e reserve.\n" +
                        " \n" +
                        "Montagem\n" +
                        "\n" +
                        "7 - Corte cada bolo em 4 círculos com a mesma largura (3,5 cm) e diâmetros diferentes (medida da parte externa do círculo: 7 cm, 14 cm, 21 cm e 28 cm). Em seguida remonte os 5 bolos encaixando os círculos com cores diferentes.\n" +
                        "\n" +
                        " \n" +
                        "8 - Num prato coloque 1 bolo remontado e passe uma camada de creme. Coloque mais 1 bolo remontado e mais uma camada de creme. Repita o mesmo procedimento sendo a ultima camada de bolo remontado.\n" +
                        "\n" +
                        "\n" +
                        "Cubra todo o bolo com o restante do creme manteiga retirando o excesso com uma faca. Decore a gosto.",
                "Rende 20 porções");
        inserirReceita(5,
                "Bolo Terremoto",
                "½ xícara (chá) de nozes picadas (60g)\n" +
                        "2/3 xícara (chá) de chocolate ao leite picadinho (100g)\n" +
                        "2/3 xícara (chá) de coco ralado (70g)\n" +
                        "1 pacote de massa para bolo de chocolate (450g)\n" +
                        "1/3 xícara (chá) de óleo (80ml)\n" +
                        "3 ovos batidos\n" +
                        "200 ml de água\n" +
                        "½ xícara (chá) de manteiga (100g)\n" +
                        "1 xícara (chá) de cream cheese (225g)\n" +
                        "1 ½ xícara (chá) de açúcar (280g)",
                "1 - Numa assadeira (30cm de diâmetro) untada coloque ½ xícara (chá) de nozes picadas, 2/3 xícara (chá) de chocolate ao leite picadinho, 2/3 xícara (chá) de coco ralado e reserve.\n" +
                        "2 - Misture numa tigela 1 pacote de massa para bolo de chocolate (450g), 1/3 xícara (chá) de óleo, 3 ovos batidos, 200 ml de água. Despeje essa mistura sobre a camada de nozes, coco e chocolate. Reserve.\n" +
                        "3 - Coloque numa panela ½ xícara (chá) de manteiga, 1 xícara (chá) de cream cheese e leve ao fogo baixo até derreter a manteiga e amolecer o cream cheese. Retire do fogo, junte 1 ½ xícara (chá) de açúcar mexendo bem com um batedor de arame até incorporar. Com uma concha despeje esta mistura sobre a massa de chocolate sem chegar na borda. Leve para assar em forno médio pré-aquecido a 200°C por +/- 35 minutos ou até balançar levemente a forma o bolo não se mexer. Retire do forno, deixe amornar e sirva em seguida.",
                null);
        inserirReceita(5,
                "Bolo de Tapioca",
                "4 litros de leite fervente\n" +
                        "400ml de leite de coco\n" +
                        "600g de coco ralado sem açúcar\n" +
                        "1kg de açúcar\n" +
                        "1kg de tapioca granulada\n" +
                        "1 colher (sopa) de sal",
                "1 - Numa tigela coloque 2 litros de leite fervente, 400ml de leite de coco, 600g de coco ralado sem açúcar, 1kg de açúcar, 1kg de tapioca granulada, 1 colher (sopa) de sal e misture até os ingredientes se incorporarem. Adicione mais 2 litros de leite fervente e misture bem até ficar homogêneo.\n" +
                        "2 - Transfira a massa para um recipiente plástico e leve para gelar por 4 horas. Retire da geladeira corte em quadrados e sirva em seguida.",
                "Rende 30 porções");

    }




}
