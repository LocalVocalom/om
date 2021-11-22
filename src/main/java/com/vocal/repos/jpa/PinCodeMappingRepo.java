package com.vocal.repos.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vocal.entities.PinCodeMapping;

@Repository("pinCodeMappingRepo")
public interface PinCodeMappingRepo extends JpaRepository<PinCodeMapping, Long>{
	
	PinCodeMapping findByPinCode(long pinCode);
	
	List<PinCodeMapping> findByCityId(long cityId);
	

}
