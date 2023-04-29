package br.com.igti.modulo_iv.ui.alunos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.com.igti.modulo_iv.R
import br.com.igti.modulo_iv.data.remote.dto.AlunoFirebaseDTO
import br.com.igti.modulo_iv.data.remote.dto.AlunoRequestDTO
import br.com.igti.modulo_iv.databinding.FragmentCadastrarAlunoBinding
import br.com.igti.modulo_iv.viewmodel.CadastrarAlunoViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class CadastrarAlunoFragment : Fragment() {

    private var _binding: FragmentCadastrarAlunoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel : CadastrarAlunoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCadastrarAlunoBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSalvar.setOnClickListener {
            //coloquei a implementação dos 2 jeitos aqui dentro, porem apenas 1 seria utilizado de fato na app

            //implementação com json e api
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.cadastrarAlunos(
                    AlunoRequestDTO(
                        binding.edtNome.text.toString(),
                        binding.edtSobrenome.text.toString(),
                       LocalDate.parse(binding.edtNascimento.text.toString())

                    )
                )
            }

            //implementação com o firebase
            val database = FirebaseDatabase.getInstance()
            val ref = database.getReference("alunos")
            val alunos = ref.child("igti2022")
            alunos.setValue(AlunoFirebaseDTO(
                nome = binding.edtNome.text.toString(),
                sobrenome = binding.edtSobrenome.text.toString(),
                nascimento = binding.edtNascimento.text.toString(),
                idade = 24
            )).addOnSuccessListener {
                Toast.makeText(view.context,"Cadastrado com sucesso",Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                Toast.makeText(view.context,"Problemas ao cadastrar!",Toast.LENGTH_LONG).show()
            }

        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.successFlow.collect{
                if(!it.isNullOrEmpty()){
                    Snackbar.make(view, String.format(Locale.US,"Cadastrado com sucesso! $it"), LENGTH_LONG).show()
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}