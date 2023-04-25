package br.com.igti.modulo_iv.ui.alunos

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.igti.modulo_iv.R
import br.com.igti.modulo_iv.databinding.FragmentListarAlunosBinding
import br.com.igti.modulo_iv.ui.alunos.adapter.AlunoAdapter
import br.com.igti.modulo_iv.ui.alunos.adapter.AlunoListener
import br.com.igti.modulo_iv.viewmodel.ListarAlunoViewModel
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ListarAlunosFragment : Fragment() {

    private var _binding: FragmentListarAlunosBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel : ListarAlunoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentListarAlunosBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerAluno.layoutManager = LinearLayoutManager(view.context)
        binding.recyclerAluno.addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.listarAlunos()
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.listaAlunosFlow.collect{
                if(!it.isNullOrEmpty()){
                    binding.recyclerAluno.adapter = AlunoAdapter(
                        dataSet = it,
                        alunoListener = object : AlunoListener{
                            override fun onAlunoCLickListener(id: String) {
                                viewModel.listarAlunosPorId(id)
                            }

                        }
                    )

                }
            }
        }
        viewModel.alunoDetalhes.observe(viewLifecycleOwner){
            if (it!=null && it.id.isNotEmpty()){

                val alertDialog = AlertDialog.Builder(view.context)
                alertDialog.setTitle("DETALHES DO ALUNO")
                alertDialog.setMessage("ID: ${it.id}" +
                        "\nNome: ${it.nome}" +
                        "\nSobrenome: ${it.sobrenome}" +
                        "\nNascimento: ${it.nascimento}")
                alertDialog.setPositiveButton("OK",null)
                alertDialog.setNeutralButton("ALTERAR"){ dialogInterface, i ->
                    val bundle = bundleOf(
                        "id" to it.id,
                        "nome" to it.nome,
                        "sobrenome" to it.sobrenome,
                        "nascimento" to it.nascimento.toString()
                    )
                    findNavController().navigate(R.id.action_FirstFragment_to_ThirdFragment, bundle)
                }
                alertDialog.setNegativeButton("EXCLUIR"){dialogInterface, i ->
                    viewModel.excluirAlunos(it.id)
                }
                alertDialog.show()
            }
        }
        viewModel.alunoExcluido.observe(viewLifecycleOwner){
            if(it != null && it == true){
                Toast.makeText(view.context,"CADASTRO EXCLUIDO COM SUCESSO!", Toast.LENGTH_LONG).show()
                lifecycleScope.launch {
                    viewModel.alterarStatusExclusao(false)
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}