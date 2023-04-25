package br.com.igti.modulo_iv.data.remote

import br.com.igti.modulo_iv.util.LocalDateAdapter
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.util.concurrent.TimeUnit

class RetrofitClient {

    companion object{
        private var instance : RetrofitClient?= null
    }

    private var alunoRepository : IAlunoRepository

    init {

        //adicionando o adapter de conversao de datas do json
        val gson = GsonBuilder().registerTypeAdapter(
            LocalDate::class.java,
            LocalDateAdapter().nullSafe()
        ).create()


        //CONFIGURANDO E INSTANCIANDO A VARIAVEL DO RETROFIT
        val retrofit = Retrofit.Builder()
            .client(createOkHttpClient())//mostrando pro retrofit quem é meu cliente
            .baseUrl("http://18.231.66.6:8080") //url base de onde vou pegar os dados, é a rota base para as requisições
            .addConverterFactory(GsonConverterFactory.create(gson))//conversor do gson para utilizar nas classes do projeto
            .build()

        alunoRepository = retrofit.create(IAlunoRepository::class.java)
    }

    private fun createOkHttpClient(): OkHttpClient {
        //CONFIGURANDO O LADO CLIENTE, COLOCAMOS TEMPO LIMITE PARA ESPERA DE CONEXAO E CONFIGURAÇÃO DO LOG EMBAIXO
        return OkHttpClient.Builder()
            .connectTimeout(15L, TimeUnit.SECONDS)
            .readTimeout(15L, TimeUnit.SECONDS)
            .writeTimeout(15L, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor{ msg ->
                println("LOG APP: $msg")
            }.apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addNetworkInterceptor(HttpLoggingInterceptor{ msg ->
                println("LOG NTW: $msg")
            }.apply {
                level = HttpLoggingInterceptor.Level.BODY
            })

            .build()
    }

    @Synchronized
     fun getInstance() : RetrofitClient{
        if(instance == null){
            instance = RetrofitClient()
        }
         return instance as RetrofitClient
    }

    fun getAlunoApi() = alunoRepository


}