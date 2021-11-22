package com.vocal.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vocal.repos.jpa.UserSourceRepo;
import com.vocal.services.GeoService;
import com.vocal.utils.CommonParams;


@RestController
public class UnspecifiedController {
	
	@Autowired
	private GeoService geoService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UnspecifiedController.class);
	
	/**
	 * This method is used to show formatted user count datewise of Gzb city
	 * @param days the number of previous days, there is also a limit
	 * @return the 
	 */
	@PostMapping("/getUserCountsGzb")
    public String getNumberOfUsersInSpecificCityUptoPreviousNDays(
                    @RequestParam("days") int days
                    ) {
            return geoService.getCountOfUsersOfSpecificCity(days);
    }
	
}
