package com.ilyes.ClientsRegions.controllers;


import com.ilyes.ClientsRegions.Services.RegionService;
import com.ilyes.ClientsRegions.Services.SharedService;
import com.ilyes.ClientsRegions.dto.RegionDTO;
import com.ilyes.ClientsRegions.entities.Region;
import com.ilyes.ClientsRegions.repositories.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;


@RestController
@CrossOrigin("*")
public class RegionTemplatesController implements WebMvcConfigurer {

    @Autowired
    RegionService regionService;

    @Autowired
    SharedService sharedService;

    TokenRepository tokenRepository;

    @GetMapping("/regions-list")
    public String home(ModelMap modelMap,
                       @RequestParam (name="page", defaultValue = "0") int page,
                       @RequestParam (name="size", defaultValue = "5") int size,
                       HttpServletRequest request) {

        String token = sharedService.getTokenFromCookie(request, "token");
        if(token != null) {
            try {
                Claims claims = Jwts.parser().setSigningKey("404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970").parseClaimsJws(token).getBody();
                modelMap.addAttribute("role", claims.get("role"));

                Page<RegionDTO> regions = regionService.getAllRegionsParPage(page, size);
                    modelMap.addAttribute("regions", regions);
                    modelMap.addAttribute("pages", new int[regions.getTotalPages()]);
                    modelMap.addAttribute("currentPage", page);
                    return "listeRegions";
            } catch (Exception e) {
                return "login";
            }
        }else return "login";
    }


    @GetMapping("/regionsTemplate/delete/{id}")
    public String deleteRegion(@PathVariable("id") Long id) {
        regionService.deleteRegion(id);
        return "redirect:/regions-list";
    }

    @GetMapping("/addRegion")
    public String createRegion(HttpServletRequest request) {

        String token = sharedService.getTokenFromCookie(request, "token");
        if(token != null) {
            try {
                Claims claims = Jwts.parser().setSigningKey("404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970").parseClaimsJws(token).getBody();
                if(claims.get("role").equals("USER")) return "redirect:/regions-list";

                return "createRegion";
            } catch (Exception e) {
                return "login";
            }
        }else return "login";
    }

    @PostMapping("/saveRegion")
    public String saveRegion(@RequestParam String nom,
                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateCreation,
                             RedirectAttributes redirectAttributes) {
        Region region = new Region();
        region.setNom(nom);
        region.setDateCreation(dateCreation);

        regionService.createRegion(region);
        return "redirect:/regions-list";
    }

    @GetMapping("/update")
    public String updateRegion(@RequestParam("idRegion") String idRegionString,
                               @RequestParam("nom") String nom,
                               @RequestParam("date") @DateTimeFormat(pattern="yyyy-MM-dd") Date date) {

        long idRegion = Long.parseLong(idRegionString);
        Region region = new Region();
        region.setIdRegion(idRegion);
        region.setNom(nom);
        region.setDateCreation(date);

        regionService.updateRegion(region);
        return "redirect:/regions-list";
    }

    @GetMapping("/updateRegion")
    public String ShowToUpdateRegionForm(@RequestParam("id") long id, ModelMap model, HttpServletRequest request) {

        String token = sharedService.getTokenFromCookie(request, "token");
        if(token != null) {
            try {
                RegionDTO region = regionService.getRegionById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid client Id:" + id));

                Claims claims = Jwts.parser().setSigningKey("404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970").parseClaimsJws(token).getBody();
                if(claims.get("role").equals("USER")) return "redirect:/regions-list";

                model.addAttribute("region", region);
                return "editerRegion";
            } catch (Exception e) {
                return "login";
            }
        }else return "login";
    }

    @GetMapping("/updateRegion/regions-list")
    public String RedirectToList(){
        return "redirect:/regions-list";
    }

}
