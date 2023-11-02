package com.ilyes.ClientsRegions.controllers;


import com.ilyes.ClientsRegions.Errors.CustomErrorMessage;
import com.ilyes.ClientsRegions.Services.ClientService;
import com.ilyes.ClientsRegions.dto.ClientDTO;
import com.ilyes.ClientsRegions.entities.Client;
import com.ilyes.ClientsRegions.entities.Region;
import com.ilyes.ClientsRegions.repositories.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    ClientService clientService;

    @Autowired
    RegionRepository regionRepository;

    @GetMapping()
    public ResponseEntity<?> getAllClients() {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(clientService.getAllClients());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorMessage("Internal Server Error"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClientById(@PathVariable Long id) {
        try{
            Optional<ClientDTO> client = clientService.getClientById(id);
            if (client.isPresent()) return ResponseEntity.ok(client.get());
            else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorMessage("Client not found"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorMessage("Internal Server Error"));
        }
    }

    @PostMapping()
    public ResponseEntity<?> createClient(@RequestBody Client client) {
        try{
            client.setDateCreation(new Date());
            Client savedClient = clientService.saveClient(client);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedClient);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorMessage("Internal Server Error"));
        }
    }

    @PutMapping("/{clientID}/{regionID}")
    public ResponseEntity<?> updateClient(@PathVariable Long clientID, @PathVariable Long regionID, @RequestBody Client client) {
        try{
            Optional<Region> region = regionRepository.findById(regionID);
            if (region.isPresent()) {
                    client.setRegion(region.get());

                    clientService.updateClient(clientID, client);
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(client);
                } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorMessage("Region not found"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorMessage("Internal Server Error"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable Long id) {
        try {
            Optional<ClientDTO> existingClient = clientService.getClientById(id);
            if (existingClient.isPresent()) {
                clientService.deleteClientById(id);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(existingClient);
            }else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorMessage("Client not found"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorMessage("Internal Server Error"));
        }
    }
}
