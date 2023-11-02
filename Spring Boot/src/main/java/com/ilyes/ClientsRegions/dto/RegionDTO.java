package com.ilyes.ClientsRegions.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ilyes.ClientsRegions.entities.Client;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegionDTO {

    private Long idRegion;
    private String nom;
    private Date dateCreation;

    @JsonIgnore
    private List<Client> clients;

    public void addClient(Client client) {
        this.clients.add(client);
    }

    public void removeClient(Client client) {
        this.clients.remove(client);
    }
}
