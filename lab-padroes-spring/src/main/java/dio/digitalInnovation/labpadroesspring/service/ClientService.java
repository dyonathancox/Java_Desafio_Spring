package dio.digitalInnovation.labpadroesspring.service;

import dio.digitalInnovation.labpadroesspring.model.Cliente;

public interface ClientService {

    Iterable<Cliente> buscarTodos();

    Cliente buscarPorId(Long id);

    void inserir(Cliente cliente);

    void atualizar(Long id, Cliente cliente);

    void deletar(Long id);

}