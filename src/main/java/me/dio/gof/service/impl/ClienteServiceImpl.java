package me.dio.gof.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.dio.gof.service.ClienteService;
import me.dio.gof.service.ViaCepService;
import me.dio.gof.service.model.Cliente;
import me.dio.gof.service.model.ClienteRepository;
import me.dio.gof.service.model.Endereco;
import me.dio.gof.service.model.EnderecoRepository;

@Service
public class ClienteServiceImpl implements ClienteService{

	// Singleton
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private ViaCepService viaCepService;
	
	@Override
	public Iterable<Cliente> buscarTodos() {
		
		return clienteRepository.findAll();
	}

	@Override
	public Cliente buscarPorId(Long id) {
		
		Optional<Cliente> optionalCliente = clienteRepository.findById(id);
		
		if (optionalCliente.isPresent()) {
			return optionalCliente.get();
		}
		return null; 
	}

	@Override
	public void inserir(Cliente cliente) {
		
		salvarClienteEndereco(cliente);
	}


	@Override
	public void atualizar(Long id, Cliente cliente) {

		Optional<Cliente> optionalCliente = clienteRepository.findById(id);
		
		if (optionalCliente.isPresent()) {
			salvarClienteEndereco(cliente);
		}		 
	}

	@Override
	public void deletar(Long id) {

		clienteRepository.deleteById(id);
	}

	private void salvarClienteEndereco(Cliente cliente) {
		String cep = cliente.getEndereco().getCep();
		Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
			
			Endereco novoEndereco = viaCepService.consultarCep(cep);
			enderecoRepository.save(novoEndereco);
			return novoEndereco;
		});
		cliente.setEndereco(endereco);
		clienteRepository.save(cliente);
	}
}
