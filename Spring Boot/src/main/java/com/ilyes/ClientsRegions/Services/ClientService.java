package com.ilyes.ClientsRegions.Services;

import com.ilyes.ClientsRegions.dto.ClientDTO;
import com.ilyes.ClientsRegions.entities.Client;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    List<ClientDTO> getAllClients();

    Optional<ClientDTO> getClientById(Long id);

    Client saveClient(Client client);
    ResponseEntity<?> updateClient(Long id, Client client);

    void deleteClientById(Long id);

    Page<ClientDTO> getAllClientsParPage(long page, long size);

    Page<ClientDTO> getAllClientsByRegionParPage(long regionId, long page, long size);

    ClientDTO convertEntityToDto(Client client);
    Client convertDtoToEntity(ClientDTO clientDto);
}

