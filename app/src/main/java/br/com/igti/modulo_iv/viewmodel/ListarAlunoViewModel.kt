package br.com.igti.modulo_iv.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.igti.modulo_iv.data.remote.AlunoRepository
import br.com.igti.modulo_iv.data.remote.IAlunoRepository
import br.com.igti.modulo_iv.data.remote.RetrofitClient
import br.com.igti.modulo_iv.data.remote.dto.AlunoResponseDTO
import br.com.igti.modulo_iv.data.remote.dto.MessageDTO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListarAlunoViewModel(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val repository: IAlunoRepository = AlunoRepository(RetrofitClient())

    //utilizando flow para listar alunos
    private val _listaALunosFlow = MutableStateFlow<List<AlunoResponseDTO>>(listOf())
    val listaAlunosFlow : StateFlow<List<AlunoResponseDTO>> = _listaALunosFlow

    //utilizando livedata para listar alunos por id
    private val _alunoDetalhes : MutableLiveData<AlunoResponseDTO> = MutableLiveData()
    val alunoDetalhes : LiveData<AlunoResponseDTO> = _alunoDetalhes

    //utilizando livedata para a função excluir aluno
    private val _alunoExcluido = MutableLiveData(false)
    val alunoExcluido : LiveData<Boolean> = _alunoExcluido

    fun listarAlunos(){
        viewModelScope.launch(dispatcher){
            repository.listarAlunos().enqueue(object : Callback<List<AlunoResponseDTO>> {
                override fun onResponse(
                    call: Call<List<AlunoResponseDTO>>,
                    response: Response<List<AlunoResponseDTO>>
                ) {
                    if (response.isSuccessful){
                        response.body()?.let {
                            _listaALunosFlow.value = it
                        }
                    }
                }

                override fun onFailure(call: Call<List<AlunoResponseDTO>>, t: Throwable) {
                    _listaALunosFlow.value = listOf()
                    Log.e(ListarAlunoViewModel::class.java.name, t.toString())
                }

            })
        }
    }

    fun listarAlunosPorId(id : String){
        viewModelScope.launch(dispatcher) {
            repository.listarAlunosPorId(id).enqueue(object : Callback<AlunoResponseDTO>{
                override fun onResponse(
                    call: Call<AlunoResponseDTO>,
                    response: Response<AlunoResponseDTO>
                ) {
                    if(response.isSuccessful){
                        response.body()?.let {
                            _alunoDetalhes.value = it
                        }
                    }
                }

                override fun onFailure(call: Call<AlunoResponseDTO>, t: Throwable) {
                    Log.e(ListarAlunoViewModel::class.java.name, t.toString())
                }

            })
        }
    }

    fun excluirAlunos(id: String){
        viewModelScope.launch(dispatcher) {
            repository.excluirAlunos(id).enqueue(object : Callback<MessageDTO>{
                override fun onResponse(
                    call: Call<MessageDTO>,
                    response: Response<MessageDTO>
                ) {
                    if(response.isSuccessful){
                        alterarStatusExclusao(true)
                    }
                }

                override fun onFailure(call: Call<MessageDTO>, t: Throwable) {
                    Log.e(ListarAlunoViewModel::class.java.name, t.toString())
                }

            })
        }
    }

    fun alterarStatusExclusao(statusAluno: Boolean){
            _alunoExcluido.value = statusAluno

    }

}