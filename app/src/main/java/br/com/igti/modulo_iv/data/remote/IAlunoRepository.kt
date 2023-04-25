package br.com.igti.modulo_iv.data.remote

import br.com.igti.modulo_iv.data.remote.dto.AlunoRequestDTO
import br.com.igti.modulo_iv.data.remote.dto.AlunoResponseDTO
import br.com.igti.modulo_iv.data.remote.dto.MessageDTO
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// é aqui nessa  onde vamos definir metodos(metodos http), assinaturas, parametros, retornos

interface IAlunoRepository {

    @GET("/alunos") // INFORMANDO O ENDPOINT QUE VOU PEGAR O DADO PARA ESSA FUNÇÃO
    fun listarAlunos() : Call<List<AlunoResponseDTO>> // tipo de retorno e qual a classe

    @GET("/alunos/{id}")
    fun listarAlunosPorId(@Path("id") id: String) : Call<AlunoResponseDTO>

    @POST("/alunos")
    fun cadastrarAlunos(@Body aluno : AlunoRequestDTO) : Call<MessageDTO>

    @PUT("/alunos/{id}")
    fun alterarAlunos(@Path("id") id: String, @Body aluno : AlunoRequestDTO) : Call<AlunoResponseDTO>

    @DELETE("/alunos/{id}")
    fun excluirAlunos(@Path("id") id: String) : Call<MessageDTO>
}