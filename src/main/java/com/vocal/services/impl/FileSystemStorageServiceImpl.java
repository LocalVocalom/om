package com.vocal.services.impl;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.vocal.configs.StorageProperties;
import com.vocal.entities.User;
import com.vocal.entities.UserProfile;
import com.vocal.exceptions.StorageException;
import com.vocal.exceptions.StorageFileNotFoundException;
import com.vocal.repos.jpa.UserProfileRepo;
import com.vocal.services.DbConfigService;
import com.vocal.services.NewsService;
import com.vocal.services.PushMangerService;
import com.vocal.services.StorageService;
import com.vocal.utils.Properties;
import com.vocal.utils.Utils;

@Service
public class FileSystemStorageServiceImpl implements StorageService {

	private final Path rootLocation;
	
	@Autowired
	private NewsService newsService;
	
	@Autowired
	private PushMangerService pushMangerService;
	
	@Autowired
	private DbConfigService dbConfigService;
	
	@Value("${HOST_URL}")
	private String HOST_URL;
	
	@Value("${KYC_BUCKET}")
	private String kycBucketUrl;
	
	@Value("${PROFILE_BUCKET}")
	private String profileBucketUrl;
	
	@Value("${KYC_UPLOAD_SCRIPT}")
	private String kycUploadScript;
	
	@Value("${PROFILE_UPLOAD_SCRIPT}")
	private String profileUploadScript;
	
	@Value("${CDN_ENABLED}")
	private boolean cdnEnabled;
	
	@Autowired
	private UserProfileRepo userProfileRepo;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemStorageServiceImpl.class);
	
	@Autowired
	public FileSystemStorageServiceImpl(StorageProperties properties) {
		this.rootLocation = Paths.get(properties.getLocation());
	}

	@Override
	public String store(MultipartFile file, String subDirectoryToSave, long userId) {
		String originalFileName = file.getOriginalFilename();
		String cleanPath = StringUtils.cleanPath(originalFileName);
		//String filename = Paths.get(cleanPath).getFileName().toString();
		//String filename = Paths.get(cleanPath).getFileName().toString().replaceAll("\\s", "_");
		//String filename = Paths.get(cleanPath).getFileName().toString().replaceAll("\\s+", "_");
		String filename = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").format(LocalDateTime.now());
		String format;
		try {
			format = originalFileName.substring(originalFileName.lastIndexOf('.'));
		} catch (Exception ex) {
			format = "";
		}
		filename = filename + "_" + userId + format;
		
		LOGGER.debug("cleanPath={}, fileNameWithFormat={}", cleanPath, filename);
		Path targetDirectory = this.rootLocation.resolve(subDirectoryToSave);
		Path targetPath = targetDirectory.resolve(filename);
		try {
			if (file.isEmpty()) {
				LOGGER.error("failed to store empty file");
				throw new StorageException("Failed to store empty file " + filename);
			}
			if (filename.contains("..")) {
				// This is a security check
				LOGGER.error("filenames contains invalid/not allowed chars");
				throw new StorageException(
						"Cannot store file with relative path outside current directory "
								+ filename);
			}
			
			Files.createDirectories(targetDirectory);
			
			LOGGER.debug("The final path={}", targetPath);
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
			}
		}
		catch (IOException e) {
			LOGGER.debug("failed to store file, exception=", e.getMessage());
			throw new StorageException("Failed to store file ...." + filename, e);
		}
		
		return targetPath.toString();
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.rootLocation, 1)
				.filter(path -> !path.equals(this.rootLocation))
				.map(this.rootLocation::relativize);
		}
		catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}

	}

	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException(
						"Could not read file: " + filename);

			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	@Override
	public void init() {
		try {
			Files.createDirectories(rootLocation);
		}
		catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}

	@Override
	public String handleFileUpload(User user, String type, String headlines, String adharNumber, String name,
			String nominee, String email, Date dob, String address, String gender, int languageId, MultipartFile file, HttpServletRequest request) {
		// Decide the directory to save
		String SAVE_DIR;
		
		// TODO : apache commons stringUtils's equals and equalsIgnoreCase...
		if(!cdnEnabled) {
			if(type.equalsIgnoreCase("NEWS")) {
				LOGGER.debug("Uploading file: ---------------- {}",headlines);
				SAVE_DIR = "uploadFiles_NEWS";
				if(file.getOriginalFilename().toLowerCase().endsWith(".mp4")) {
					throw new IllegalArgumentException("News files with only mp4 formats are allowed");
				}
			} else if(type.equalsIgnoreCase("PROFILE")) {
				SAVE_DIR = "profiles";
			} else if(type.equalsIgnoreCase("KYC")) {
				SAVE_DIR = "uploadFiles_KYC";
			} else {
				SAVE_DIR = "unsortedUploads";
			}
		} else {
			SAVE_DIR = "";
		}
		
		
		if(type.equalsIgnoreCase("NEWS") && user.getUploadStatus() == 0) {
			LOGGER.info("user with userId = {} not allowed to upload NEWS with uploadStatus = {}", user.getUserId(), user.getUploadStatus());
			return file.getOriginalFilename();
		}
		LOGGER.info("user with userId={} uploading for type={} with uploadStatus={}", user.getUserId(), type, user.getUploadStatus());
		// store the file
		String returnedUrl = store(file, SAVE_DIR, user.getUserId());
		LOGGER.info(
				"Upload File: type={}, headlines={}, adharNumber={}, name={}, nominee={}, email={}, dob={}, address={}, fileUrl={}",
				type, headlines, adharNumber, name, nominee, email, dob, address, returnedUrl);

		if(type.equalsIgnoreCase("KYC")) {
			LOGGER.debug("dob={}, name={}, nominee={}", dob, name, nominee);
			String profilePicUrl; // String profilePicUrl = HOST_URL + "kyc_files/" + Utils.getFileName(returnedUrl);
			if(cdnEnabled) {
				String filename = Utils.getFileName(returnedUrl);
				profilePicUrl = kycBucketUrl + filename;
				String cmd = kycUploadScript + " " + filename;
				LOGGER.debug("KYC upload shell command={}", cmd);
				try {
					Runtime runtime = Runtime.getRuntime();
					runtime.exec(cmd);
				} catch (IOException e){
					LOGGER.error("Failed to execute  KYC upload shell command={}, exception=", cmd, e.getMessage());
				}
			} else {
				profilePicUrl = HOST_URL + "kyc_files/" + Utils.getFileName(returnedUrl);
			}
			
			UserProfile userProfile = userProfileRepo.findByUserId(user.getUserId());
			// if doesn't exist then create now
			if(userProfile == null) {
				userProfile = new UserProfile();
			}
			userProfile.setName(name);
			userProfile.setProfilePick(profilePicUrl);
			userProfileRepo.save(userProfile);
			
			Calendar cal  = Calendar.getInstance();
			Date currentDate = cal.getTime();
			//cal.add(Calendar.DAY_OF_MONTH, 7); // ADDING 7 MORE DAYS TO CURRENT DATE AND THEN CHANGED TO 1 DAY
			int insuranceActiveAfterDays = dbConfigService.getIntProperty(Properties.INSURENCE_ACTIVE_AFTER_DAYS.getProperty(), Properties.INSURENCE_ACTIVE_AFTER_DAYS.getDefaultValueAsInt());
			cal.add(Calendar.DAY_OF_MONTH, insuranceActiveAfterDays); 
			Date dateAfterAddingAfterDays = cal.getTime();
			
			//newsService.createUserInsurance(userId, name, nominee, dob, email, profilePicUrl, currentDate, dateAfterSevenDays, "PENDING", gender); 
			newsService.createUserInsurance(user.getUserId(), name, nominee, dob, email, profilePicUrl, currentDate, dateAfterAddingAfterDays, "ACTIVE", gender);  // CHANGED TO ACTIVE FROM 'PENDING'
			
			String insuranceRegMsg = dbConfigService.getPropertyByLanguageId(Properties.INSURENCE_REGISTRATION_TEXT.getProperty(), languageId);
			if(insuranceRegMsg == null) {
				LOGGER.debug("Insurance registration success msg not found for requested languageId, hence falling back to English");
				insuranceRegMsg = dbConfigService.getPropertyByLanguageId(Properties.INSURENCE_REGISTRATION_TEXT.getProperty(), Properties.INSURENCE_REGISTRATION_TEXT.getDefaultValue(), 2);
			}
			pushMangerService.sendPush(user.getUserId(), insuranceRegMsg, "imageUrl", "actionUrl", 5); // sending push with type 5
		
		} else if(type.equalsIgnoreCase("PROFILE")) {
			//returnedUrl = HOST_URL + "profiles/" + Utils.getFileName(returnedUrl);
			String filename = Utils.getFileName(returnedUrl);
			if(cdnEnabled) {
				String cmd = profileUploadScript + " " + filename;
				returnedUrl = profileBucketUrl + filename;
				LOGGER.debug("image only upload shell command={}", cmd);
				try {
					Runtime runtime = Runtime.getRuntime();
					runtime.exec(cmd);
				} catch (IOException e) {
					LOGGER.error("Failed to execute  profile upload shell command={}, exception=", cmd, e.getMessage());
				}
			} else {
				returnedUrl = HOST_URL + "profiles/" + Utils.getFileName(returnedUrl);
			}
		}
		return returnedUrl;
	}
}