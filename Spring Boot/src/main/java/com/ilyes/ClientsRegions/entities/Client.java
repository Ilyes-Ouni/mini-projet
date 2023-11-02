package com.ilyes.ClientsRegions.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idClient;
    private String nom;
    private String email;
    private Long phoneNumber;
    private Date dateCreation;
    private Date dateNaissance;
    private String imagePath = null;


    @ManyToOne
    @JoinColumn(name = "id_region")
    private Region region;
}
