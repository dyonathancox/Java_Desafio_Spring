package dio.digitalInnovation.labpadroesspring.service.ClientService2;

import java.util.Optional;

import dio.digitalInnovation.labpadroesspring.model.Cliente;
import dio.digitalInnovation.labpadroesspring.model.ClienteRepository;
import dio.digitalInnovation.labpadroesspring.model.Endereco;
import dio.digitalInnovation.labpadroesspring.model.EnderecoRepository;
import dio.digitalInnovation.labpadroesspring.service.ClientService;
import dio.digitalInnovation.labpadroesspring.service.viaCep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService2 implements ClientService {


    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private viaCep viaCepService;


    @Override
    public Iterable<Cliente> buscarTodos() {
        // Busca todos os Clientes.
        return clienteRepository.findAll();
    }

    @Override
    public Cliente buscarPorId(Long id) {
        // Busca por ID.
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.get();
    }

    @Override
    public void inserir(Cliente cliente) {
        salvarClienteComCep(cliente);
    }

    @Override
    public void atualizar(Long id, Cliente cliente) {

        // Buscar por ID, se existir:
        Optional<Cliente> clienteBd = clienteRepository.findById(id);
        if (clienteBd.isPresent()) {
            salvarClienteComCep(cliente);
        }
    }

    @Override
    public void deletar(Long id) {

        // Deletar Cliente por ID.
        clienteRepository.deleteById(id);
    }

    private void salvarClienteComCep(Cliente cliente) {
        // Verificar se o Endereco do Cliente já existe (pelo CEP).

        String cep = cliente.getEndereco().getCep();
        Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
            // Caso não exista, integrar com o ViaCEP e persistir o retorno.
            Endereco novoEndereco = viaCepService.consultarCep(cep);
            enderecoRepository.save(novoEndereco);
            return novoEndereco;
        });
        cliente.setEndereco(endereco);
        // Inserir Cliente, vinculando o Endereco (novo/existente).
        clienteRepository.save(cliente);
    }

}
