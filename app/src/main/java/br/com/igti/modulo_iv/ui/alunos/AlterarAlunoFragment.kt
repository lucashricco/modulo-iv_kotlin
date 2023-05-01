package br.com.igti.modulo_iv.ui.alunos

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.igti.modulo_iv.R
import br.com.igti.modulo_iv.data.remote.dto.AlunoRequestDTO
import br.com.igti.modulo_iv.databinding.FragmentAlterarAlunoBinding
import br.com.igti.modulo_iv.databinding.FragmentListarAlunosBinding
import br.com.igti.modulo_iv.ui.alunos.adapter.AlunoAdapter
import br.com.igti.modulo_iv.ui.alunos.adapter.AlunoListener
import br.com.igti.modulo_iv.viewmodel.AlterarAlunoViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*


class AlterarAlunoFragment : Fragment() {
    private var _binding: FragmentAlterarAlunoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel : AlterarAlunoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAlterarAlunoBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.edtId.setText(arguments?.getString("id"))
        binding.edtNome.setText(arguments?.getString("nome"))
        binding.edtSobrenome.setText(arguments?.getString("sobrenome"))
        binding.edtNascimento.setText(arguments?.getString("nascimento"))



        binding.btnSalvar.setOnClickListener {

            //alterando os dados no firebase, atualização simples, com a gente mesmo passando o paramentro
            val database = FirebaseDatabase.getInstance()
            val ref = database.getReference("alunos").child("igti2022")
            val update : MutableMap<String,Any> = HashMap()
            update["nome"] = "Lucas"
            update["idade"] = "25"
            ref.updateChildren(update)



           //alterando o json
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.alterarAlunos(
                    id = binding.edtId.text.toString(),
                    aluno = AlunoRequestDTO(
                        binding.edtNome.text.toString(),
                        binding.edtSobrenome.text.toString(),
                        LocalDate.parse(binding.edtNascimento.text.toString())

                    )
                )
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.successFlow.collect{
                if(!it.isNullOrEmpty()){
                    Snackbar.make(view, String.format(Locale.US,"Alterado com sucesso! $it"),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}