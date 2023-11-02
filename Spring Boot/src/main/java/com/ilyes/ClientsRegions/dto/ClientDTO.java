package com.ilyes.ClientsRegions.dto;

import com.ilyes.ClientsRegions.entities.Region;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ClientDTO {
    private Long id;
    private String name;
    private String email;

    private Long phoneNumber;
    private Date dateCreation;
    private Date dateNaissance;
    private String imagePath = null;
    public ClientDTO() {}

    @ManyToOne
    @JoinColumn(name = "id_region")
    private Region region;

    public void setNom(String nom) {
        this.name = nom;
    }
}
