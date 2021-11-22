package com.vocal.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.vocal.entities.User;

import java.nio.file.Path;
import java.util.Date;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

public interface StorageService {

	void init();

	String store(MultipartFile file, String subDirectoryToSave, long userId);

	Stream<Path> loadAll();

	Path load(String filename);

	Resource loadAsResource(String filename);

	void deleteAll();

	// Added gender field 
	String handleFileUpload(User user, String type, String headlines, String adharNumber, String name, String nominee,
			String email, Date dob, String address, String gender, int languageId, MultipartFile file, HttpServletRequest request);

}