package com.pfa.revent.storage.controller;

import java.io.IOException;
import java.util.stream.Collectors;

import com.pfa.revent.entity.Event;
import com.pfa.revent.entity.User;
import com.pfa.revent.repository.EventRepository;
import com.pfa.revent.repository.UserRepository;
import com.pfa.revent.service.EventService;
import com.pfa.revent.service.UserService;
import com.pfa.revent.storage.StorageFileNotFoundException;
import com.pfa.revent.storage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class FileUploadController {

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private EventService eventService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

/*	private final StorageService storageService;

	@Autowired
	public FileUploadController(StorageService storageService) {
		this.storageService = storageService;
	}

	@GetMapping("/")
	public String listUploadedFiles(Model model) throws IOException {

		model.addAttribute("files", storageService.loadAll().map(
				path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
						"serveFile", path.getFileName().toString()).build().toUri().toString())
				.collect(Collectors.toList()));

		return "uploadForm";
	}

	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

		Resource file = storageService.loadAsResource(filename);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}

	@PostMapping("/upload")
	public String handleFileUpload(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) {

		storageService.store(file);
		redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded " + file.getOriginalFilename() + "!");

		return "redirect:/home";
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}*/


	@PostMapping("/event/{eventId}/uploadImage")
	public String uploadImage(Model model, @RequestParam("imageFile") MultipartFile imageFile, @PathVariable(name = "eventId") long eventId) {
		String returnValue = "/event/update-event";
		Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Invalid event eventId:" + eventId));
		try {
			eventService.saveImage(imageFile,eventId);
		} catch (IOException e) {
			model.addAttribute("event", event);
			model.addAttribute("error","Error while uploading the file");
			return returnValue;
		}
		model.addAttribute("event", event);
		model.addAttribute("image",event.getThumbnailName());
		System.out.println(event.getThumbnailName());
		return returnValue;
	}
	@PostMapping("/user/{userId}/uploadImage")
	public String uploadAvatar(Model model, @RequestParam("imageFile") MultipartFile imageFile, @PathVariable(name = "userId") long userId) {
		String returnValue = "/user/update-user";
		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user userId:" + userId));
		try {
			userService.saveImage(imageFile,userId);
		} catch (IOException e) {
			model.addAttribute("user", user);
			model.addAttribute("error","Error while uploading the file");
			return returnValue;
		}
		model.addAttribute("user", user);
		model.addAttribute("image",user.getAvatarName());
		System.out.println(user.getAvatarName());
		return returnValue;
	}

}
