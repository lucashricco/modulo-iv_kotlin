package br.com.igti.modulo_iv.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.igti.modulo_iv.data.remote.AlunoRepository
import br.com.igti.modulo_iv.data.remote.IAlunoRepository
import br.com.igti.modulo_iv.data.remote.RetrofitClient
import br.com.igti.modulo_iv.data.remote.dto.AlunoRequestDTO
import br.com.igti.modulo_iv.data.remote.dto.AlunoResponseDTO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlterarAlunoViewModel(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val repository: IAlunoRepository = AlunoRepository(RetrofitClient())

    private val _successFlow = MutableStateFlow("")
    val successFlow : StateFlow<String> = _successFlow

    fun alterarAlunos(id: String, aluno: AlunoRequestDTO){
        viewModelScope.launch(dispatcher){
            repository.alterarAlunos(id,aluno).enqueue(object : Callback<AlunoResponseDTO>{
                override fun onResponse(
                    call: Call<AlunoResponseDTO>,
                    response: Response<AlunoResponseDTO>
                ) {
                    if(response.isSuccessful){
                        response.body()?.let {
                            _successFlow.value = it.toString()
                        }
                    }
                }

                override fun onFailure(call: Call<AlunoResponseDTO>, t: Throwable) {
                    Log.e(AlterarAlunoViewModel :: class.java.name, t.stackTraceToString())
                }

            })
        }

    }


}