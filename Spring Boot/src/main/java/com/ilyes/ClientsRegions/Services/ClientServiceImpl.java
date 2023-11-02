package com.ilyes.ClientsRegions.Services;

import com.ilyes.ClientsRegions.Errors.CustomErrorMessage;
import com.ilyes.ClientsRegions.dto.ClientDTO;
import com.ilyes.ClientsRegions.entities.Client;
import com.ilyes.ClientsRegions.repositories.*;
import com.ilyes.ClientsRegions.repositories.ClientRepository;
import com.ilyes.ClientsRegions.repositories.RegionRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService{

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private RegionRepository regionRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public ClientServiceImpl() {
        this.modelMapper = new ModelMapper();
    }

    @Override
    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }


    public Optional<ClientDTO> getClientById(Long id) {
        Optional<Client> clientOptional = clientRepository.findById(id);

        return clientOptional.map(client -> convertEntityToDto(client));
    }


    @Override
    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public ResponseEntity<?> updateClient(Long id, Client client) {
        Optional<Client> existingClient = clientRepository.findById(id);
        if (existingClient.isPresent()) {
            client.setIdClient(id);
            Client updatedClient = clientRepository.save(client);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedClient);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorMessage("Client not found"));
        }
    }


    @Override
    public void deleteClientById(Long id) {
        clientRepository.deleteById(id);
    }

    @Override
    public Page<ClientDTO> getAllClientsParPage(long page, long size) {
        Pageable pageable = PageRequest.of((int) page, (int) size);
        Page<Client> clientPage = clientRepository.findAll(pageable);

        // Convert the list of Client entities to a list of ClientDTO using the convertEntityToDto method
        List<ClientDTO> clientDTOs = clientPage.getContent().stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(clientDTOs, pageable, clientPage.getTotalElements());
    }

    @Override
    public Page<ClientDTO> getAllClientsByRegionParPage(long regionId, long page, long size) {
        Pageable pageable = PageRequest.of((int) page, (int) size);
        List<Client> clients = regionRepository.findByRegion(regionId); // Assuming this method returns a List<Client>

        // Convert the list of Client entities to a list of ClientDTO using the convertEntityToDto method
        List<ClientDTO> clientDTOs = clients.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(clientDTOs, pageable, clientDTOs.size());
    }



    @Override
    public ClientDTO convertEntityToDto(Client c) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        ClientDTO clientDTO = modelMapper.map(c, ClientDTO.class);
        return clientDTO;

    }

    @Override
    public Client convertDtoToEntity(ClientDTO clientDto) {
        Client client=modelMapper.map(clientDto, Client.class);
        return client;
    }
}

