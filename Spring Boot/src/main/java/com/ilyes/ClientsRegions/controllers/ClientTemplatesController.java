package com.ilyes.ClientsRegions.controllers;


import com.ilyes.ClientsRegions.Services.ClientService;
import com.ilyes.ClientsRegions.Services.RegionService;
import com.ilyes.ClientsRegions.Services.SharedService;
import com.ilyes.ClientsRegions.dto.ClientDTO;
import com.ilyes.ClientsRegions.dto.RegionDTO;
import com.ilyes.ClientsRegions.entities.Client;
import com.ilyes.ClientsRegions.entities.Region;
import com.ilyes.ClientsRegions.repositories.UserRepository;
import com.ilyes.ClientsRegions.config.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class ClientTemplatesController {
    @Autowired
    ClientService clientService;

    @Autowired
    RegionService regionService;

    @Autowired
    SharedService sharedService;

    UserRepository userRepository;
    JwtService jwtService;

    ModelMapper modelMapper;


    @GetMapping("/")
    public String home(ModelMap modelMap) {
        return "redirect:/clients-list";
    }


    @GetMapping("/clients-list")
    public String home(ModelMap modelMap,
                       @RequestParam (name="page", defaultValue = "0") int page,
                       @RequestParam (name="size", defaultValue = "5") int size,
                       HttpServletRequest request) {

        String token = sharedService.getTokenFromCookie(request, "token");
        if(token != null ) {
            try {
                Claims claims = Jwts.parser().setSigningKey("404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970").parseClaimsJws(token).getBody();
                modelMap.addAttribute("role", claims.get("role"));

                Page<ClientDTO> clients = clientService.getAllClientsParPage(page, size);
                modelMap.addAttribute("clients", clients);
                modelMap.addAttribute("pages", new int[clients.getTotalPages()]);
                modelMap.addAttribute("currentPage", page);
                return "listeClients";
            }catch (Exception e){
                return "login";
            }
        }else return "login";
    }


    @GetMapping("/clientsTemplate/delete/{id}")
    public String deleteClient(@PathVariable("id") Long id) {
        clientService.deleteClientById(id);
        return "redirect:/clients-list";
    }

    @GetMapping("/addClient")
    public String createClient(HttpServletRequest request, ModelMap modelMap) {

        String token = sharedService.getTokenFromCookie(request, "token");
        if (token != null) {
            try {
                Claims claims = Jwts.parser().setSigningKey("404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970").parseClaimsJws(token).getBody();
                modelMap.addAttribute("role", claims.get("role"));

                if(claims.get("role").equals("USER")) return "redirect:/clients-list";

                modelMap.addAttribute("regions", regionService.getAllRegions());
                return "createClient";
            }catch (Exception e){
                return "redirect:/clients-list";
                // return "login";
            }
        } else {
            return "login";
        }
    }



    @PostMapping("/saveClient")
    public String saveClient(@RequestParam String nom,
                             @RequestParam String email,
                             @RequestParam Long phoneNumber,
                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateNaissance,
                             @RequestParam Long idRegion) {

        Optional<RegionDTO> regionDTOOptional = regionService.getRegionById(idRegion);
        if (regionDTOOptional.isPresent()) {
            // Convert the RegionDTO to a Region entity
            Region region = regionService.convertDtoToEntity(regionDTOOptional.get());

            // Create a new Client and set its properties
            Client client = new Client();
            client.setNom(nom);
            client.setEmail(email);
            client.setPhoneNumber(phoneNumber);
            client.setDateNaissance(dateNaissance);
            client.setDateCreation(new Date());
            client.setRegion(region);

            // Save the new Client
            clientService.saveClient(client);
        }
        return "redirect:/clients-list";
    }



    @GetMapping("/{id}")
    public String showUpdateClientForm(@PathVariable("id") long id, ModelMap model, HttpServletRequest request) {

        String token = sharedService.getTokenFromCookie(request, "token");
        if (token != null) {
            try {
                ClientDTO client = clientService.getClientById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid client Id:" + id));

                Claims claims = Jwts.parser().setSigningKey("404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970").parseClaimsJws(token).getBody();
                if(claims.get("role").equals("USER")) return "redirect:/clients-list";

                model.addAttribute("client", client);
                model.addAttribute("regions", regionService.getAllRegions());
                return "editerClient"; // the name of the Thymeleaf template for updating a client
            } catch (Exception e) {
                return "login";
            }
        } else return "login";
    }


    @PostMapping("/updateClient")
    public String updateClient(@RequestParam String idClient, @RequestParam String nom, @RequestParam long idRegion,
                               @RequestParam String email, @RequestParam String phoneNumber,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateNaissance) {

        Optional<ClientDTO> clientToUpdate = clientService.getClientById(Long.valueOf(idClient));
        Optional<RegionDTO> region = regionService.getRegionById(idRegion);

        if (clientToUpdate.isPresent() && region.isPresent()) {
            // Convert idRegion to a Region entity
            Region regionEntity = regionService.convertDtoToEntity(region.get());

            // Update the client properties
            ClientDTO updatedClientDTO = clientToUpdate.get();
            updatedClientDTO.setNom(nom);
            updatedClientDTO.setEmail(email);
            updatedClientDTO.setPhoneNumber(Long.valueOf(phoneNumber));
            updatedClientDTO.setDateNaissance(dateNaissance);
            updatedClientDTO.setRegion(regionEntity);

            // Convert the updated ClientDTO to a Client entity
            Client updatedClient = clientService.convertDtoToEntity(updatedClientDTO);

            // Update the client using clientService
            clientService.updateClient(Long.valueOf(idClient), updatedClient);
        }

        return "redirect:/clients-list";
    }



    public Client convertDtoToEntity(ClientDTO clientDto) {
        Client client=modelMapper.map(clientDto, Client.class);
        return client;
    }

    @GetMapping("/clients-by-regions")
    public String listClientsByRegion(ModelMap modelMap, @RequestParam(required = false) Long regionId,
                                      @RequestParam(name="page", defaultValue="0") int page,
                                      @RequestParam(name="size", defaultValue="3") int size,
                                      HttpServletRequest request) {

        String token = sharedService.getTokenFromCookie(request, "token");
        if(token != null ) {
            try {
                Claims claims = Jwts.parser().setSigningKey("404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970").parseClaimsJws(token).getBody();
                modelMap.addAttribute("role", claims.get("role"));

                List<RegionDTO> regions = regionService.getAllRegions();
                modelMap.addAttribute("regions", regions);
                if (regionId != null) {

                    modelMap.addAttribute("regionId", regionId);
                    Page<ClientDTO> clients = clientService.getAllClientsByRegionParPage(regionId, page, 3);
                    modelMap.addAttribute("clients", clients);
                    modelMap.addAttribute("pages", new int[clients.getTotalPages()]);
                    modelMap.addAttribute("currentPage", page);
                }
                return "listeClientsByRegions";
            } catch (Exception e) {
                return "login";
            }
        } else return "login";
    }
}
