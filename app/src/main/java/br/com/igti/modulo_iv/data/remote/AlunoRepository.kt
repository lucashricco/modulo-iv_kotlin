package br.com.igti.modulo_iv.data.remote

import br.com.igti.modulo_iv.data.remote.dto.AlunoRequestDTO
import br.com.igti.modulo_iv.data.remote.dto.AlunoResponseDTO
import br.com.igti.modulo_iv.data.remote.dto.MessageDTO
import okhttp3.Response
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Path

class AlunoRepository(private val retrofitClient: RetrofitClient) : IAlunoRepository {

    //ok
    override fun listarAlunos(): Call<List<AlunoResponseDTO>> =
        retrofitClient.getInstance().getAlunoApi().listarAlunos()
    //ok
    override fun listarAlunosPorId(@Path(value = "id") id: String): Call<AlunoResponseDTO> =
        retrofitClient.getInstance().getAlunoApi().listarAlunosPorId(id)

    //ok
    override fun cadastrarAlunos(aluno: AlunoRequestDTO): Call<MessageDTO> =
        retrofitClient.getInstance().getAlunoApi().cadastrarAlunos(aluno)

    override fun alterarAlunos(@Path(value = "id") id: String,@Body aluno: AlunoRequestDTO): Call<AlunoResponseDTO> =
        retrofitClient.getInstance().getAlunoApi().alterarAlunos(id,aluno)

    override fun excluirAlunos(@Path(value = "id") id: String): Call<MessageDTO> =
        retrofitClient.getInstance().getAlunoApi().excluirAlunos(id)


}