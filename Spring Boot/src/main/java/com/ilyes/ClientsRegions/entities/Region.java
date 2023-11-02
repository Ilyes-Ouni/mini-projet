package com.ilyes.ClientsRegions.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRegion;
    private String nom;
    private Date dateCreation;

    @JsonIgnore
    @OneToMany(mappedBy = "region")
    private List<Client> clients;

    public void addClient(Client client) {
        client.setRegion(this);
        this.clients.add(client);
    }

    public void removeClient(Client client) {
        client.setRegion(this);
        this.clients.remove(client);
    }
}
