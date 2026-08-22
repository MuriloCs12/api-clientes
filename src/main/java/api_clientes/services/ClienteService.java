package api_clientes.services;

import api_clientes.dto.ClienteCreateDTO;
import api_clientes.dto.ClienteDTO;
import api_clientes.dto.ClienteUpdateDTO;
import api_clientes.model.Cliente;
import api_clientes.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional(readOnly = true)
    public List<ClienteDTO> listarClientes() {
        return clienteRepository.findAll().stream()
                .map(c -> new ClienteDTO(c.getId(), c.getName(), c.getEmail()))
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<ClienteDTO> buscarCliente(Long id) {
        return clienteRepository.findById(id)
                .map(c -> new ClienteDTO(c.getId(), c.getName(), c.getEmail()));
    }

    @Transactional
    public ClienteDTO criarCliente(ClienteCreateDTO dto) {
        if (clienteRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("E-mail já cadastrado");
        }

        Cliente cliente = new Cliente(dto.name(), dto.email());
        Cliente clienteSalvo = clienteRepository.save(cliente);

        return new ClienteDTO(clienteSalvo.getId(), clienteSalvo.getName(), clienteSalvo.getEmail());
    }

    @Transactional
    public Optional<ClienteDTO> atualizarCliente(Long id, ClienteUpdateDTO dto) {
        return clienteRepository.findById(id).map(clienteExistente -> {
          if (!clienteExistente.getEmail().equals(dto.email()) && clienteRepository.existsByEmail(dto.email())) {
              throw new IllegalArgumentException("E-mail já cadastrado por outro cliente");
          }

          clienteExistente.setName(dto.name());
          clienteExistente.setEmail(dto.email());

          Cliente clienteAtualizado = clienteRepository.save(clienteExistente);
          return new ClienteDTO(clienteAtualizado.getId(), clienteAtualizado.getName(), clienteAtualizado.getEmail());
        });
    }

    @Transactional
    public boolean deletarCliente(Long id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
            return true;
        }
        return false;
    }
}