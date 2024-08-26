package com.me.ecommerce.service;

import com.me.ecommerce.dto.response.CountryDTO;
import com.me.ecommerce.entity.Country;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface CountryService {

    List<CountryDTO> getAllCountries();

    CountryDTO getCountryById(UUID id);

    Country saveCountry(Country country);

    void deleteCountry(UUID id);

    ResponseEntity<Country> updateCountry(UUID id, Country country);

//    ResponseEntity<Country> updateCountry(UUID id, Country country);
}
