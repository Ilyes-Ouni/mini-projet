package com.ilyes.ClientsRegions.controllers;


import com.ilyes.ClientsRegions.Errors.CustomErrorMessage;
import com.ilyes.ClientsRegions.Services.ClientService;
import com.ilyes.ClientsRegions.Services.RegionServiceImpl;
import com.ilyes.ClientsRegions.dto.ClientDTO;
import com.ilyes.ClientsRegions.dto.RegionDTO;
import com.ilyes.ClientsRegions.entities.Client;
import com.ilyes.ClientsRegions.entities.Region;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/regions")
public class RegionController {

    @Autowired
    private RegionServiceImpl regionService;

    @Autowired
    private ClientService clientService;


    @GetMapping()
    public ResponseEntity<?> getAllRegions() {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(regionService.getAllRegions());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRegionById(@PathVariable Long id) {
        try {
            Optional<RegionDTO> region = regionService.getRegionById(id);
            if (region.isPresent()) return ResponseEntity.ok(region.get());
            else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorMessage("Region not found"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorMessage("Internal Server Error"));
        }
    }

    @PostMapping()
    public ResponseEntity<?> createRegion(@RequestBody Region region) {
        try{
            Region savedRegion = regionService.createRegion(region);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRegion);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorMessage("Internal Server Error"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRegion(@PathVariable Long id, @RequestBody Region region) {
        try {
            Optional<RegionDTO> existingRegion = regionService.getRegionById(id);
            if (existingRegion.isPresent()) {
                region.setIdRegion(id);
                region.setDateCreation(existingRegion.get().getDateCreation());
                Region updatedRegion = regionService.updateRegion(region);
                return ResponseEntity.ok(updatedRegion);
            } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorMessage("Region not found"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorMessage("Internal Server Error"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRegion(@PathVariable Long id) {
        try {
            Optional<RegionDTO> existingRegion = regionService.getRegionById(id);
            if (existingRegion.isPresent()) {
                regionService.deleteRegion(id);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(existingRegion);
            } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorMessage("Region not found"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorMessage("Internal Server Error"));
        }
    }

    @GetMapping("/{id}/clients")
    public ResponseEntity<?> getClientsByRegion(@PathVariable Long id) {
        try {
            Optional<RegionDTO> region = regionService.getRegionById(id);
            if (region.isPresent()) return ResponseEntity.ok(regionService.getClientsById(id));
            else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorMessage("Region not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorMessage("Internal Server Error"));
        }
    }

    @PostMapping("/{id}/clients")
    public ResponseEntity<?> createClientForRegion(@PathVariable Long id, @RequestBody Client client) {
        try {
            Optional<RegionDTO> regionDTOOptional = regionService.getRegionById(id);
            if (regionDTOOptional.isPresent()) {
                // Convert the RegionDTO to a Region entity
                Region region = regionService.convertDtoToEntity(regionDTOOptional.get());

                // Set the new region for the client
                client.setRegion(region);

                // Set other properties if needed
                client.setDateCreation(new Date());
                // ...

                // Save the new client
                Client savedClient = clientService.saveClient(client);

                // Add the client to the region
                region.addClient(savedClient);

                return ResponseEntity.status(HttpStatus.CREATED).body(savedClient);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorMessage("Region not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorMessage("Internal Server Error"));
        }
    }


    @PutMapping("/{id}/clients")
    public ResponseEntity<?> updateClientForRegion(@PathVariable Long id, @RequestBody Client client) {
        try {
            Optional<RegionDTO> regionDTOOptional = regionService.getRegionById(id);
            if (regionDTOOptional.isPresent()) {
                // Convert the RegionDTO to a Region entity
                Region region = regionService.convertDtoToEntity(regionDTOOptional.get());

                // Remove the client from its previous region (if any)
                client.getRegion().removeClient(client);

                // Set the new region for the client
                client.setRegion(region);

                // Save the updated client
                Client savedClient = clientService.saveClient(client);

                // Add the client to the updated region
                region.addClient(savedClient);

                return ResponseEntity.status(HttpStatus.ACCEPTED).body(savedClient);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorMessage("Region not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorMessage("Internal Server Error"));
        }
    }


    @GetMapping("/clients-by-region/{regionId}")
    public ResponseEntity<?> getAllClientsByRegionId(@PathVariable Long regionId) {
        try{
            Optional<RegionDTO> region = regionService.getRegionById(regionId);
            if (region.isPresent()) {
                List<ClientDTO> clients = regionService.getClientsById(regionId);
                return ResponseEntity.status(HttpStatus.CREATED).body(clients);
            } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorMessage("Region not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorMessage("Internal Server Error"));
        }
    }
}
