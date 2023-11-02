package com.ilyes.ClientsRegions.Services;

import com.ilyes.ClientsRegions.dto.ClientDTO;
import com.ilyes.ClientsRegions.dto.RegionDTO;
import com.ilyes.ClientsRegions.entities.Client;
import com.ilyes.ClientsRegions.entities.Region;
import com.ilyes.ClientsRegions.repositories.RegionRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RegionServiceImpl implements RegionService{
    @Autowired
    private RegionRepository regionRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public RegionServiceImpl() {
        this.modelMapper = new ModelMapper();
    }

    public List<RegionDTO> getAllRegions() {
        List<Region> regions = regionRepository.findAll();

        // Convert the list of Region entities to a list of RegionDTOs
        List<RegionDTO> regionDTOs = regions.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());

        return regionDTOs;
    }

    public Optional<RegionDTO> getRegionById(Long id) {
        Optional<Region> regionOptional = regionRepository.findById(id);

        if (regionOptional.isPresent()) {
            RegionDTO regionDTO = convertEntityToDto(regionOptional.get());
            return Optional.of(regionDTO);
        } else {
            return Optional.empty();
        }
    }


    public Region createRegion(Region region) {
        region.setDateCreation(new Date());
        return regionRepository.save(region);
    }

    public Region updateRegion(Region region) {
        return regionRepository.save(region);
    }


    @Override
    public void deleteRegion(Long id) {
        regionRepository.deleteById(id);
    }

    @Override
    public List<ClientDTO> getClientsById(Long id) {
        List<Client> clients = regionRepository.findByRegion(id);

        // Convert the list of Client entities to a list of ClientDTOs
        List<ClientDTO> clientDTOs = clients.stream()
                .map(this::convertClientEntityToDto)
                .collect(Collectors.toList());

        return clientDTOs;
    }


    @Override
    public Page<RegionDTO> getAllRegionsParPage(long page, long size) {
        Page<Region> regionPage = regionRepository.findAll(PageRequest.of((int) page, (int) size));

        // Convert the Page<Region> to Page<RegionDTO> using a map function
        Page<RegionDTO> regionDTOPage = regionPage.map(this::convertEntityToDto);

        return regionDTOPage;
    }


    @Override
    public RegionDTO convertEntityToDto(Region c) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        RegionDTO regionDTO = modelMapper.map(c, RegionDTO.class);
        return regionDTO;

    }

    @Override
    public Region convertDtoToEntity(RegionDTO regionDto) {
        Region region=modelMapper.map(regionDto, Region.class);
        return region;
    }

    public ClientDTO convertClientEntityToDto(Client c) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        ClientDTO clientDTO = modelMapper.map(c, ClientDTO.class);
        return clientDTO;
    }
}

