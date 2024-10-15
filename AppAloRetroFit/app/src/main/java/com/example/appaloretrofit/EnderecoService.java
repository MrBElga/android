package com.example.appaloretrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface EnderecoService {
    @GET ("{cep}/json")
    Call<Endereco> buscarCep(@Path("cep") String cep);
}
