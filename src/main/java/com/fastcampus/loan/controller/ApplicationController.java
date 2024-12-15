package com.fastcampus.loan.controller;

import com.fastcampus.loan.dto.ApplicationDTO.*;
import com.fastcampus.loan.dto.FileDTO;
import com.fastcampus.loan.dto.ResponseDTO;
import com.fastcampus.loan.service.ApplicationService;
import com.fastcampus.loan.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/applications")
public class ApplicationController extends AbstractController {

    private final ApplicationService applicationService;

    private final FileStorageService fileStorageService;

    @PostMapping
    public ResponseDTO<Response> create(@RequestBody Request request) {
        Response response = applicationService.create(request);

        return ok(response);
    }

    @GetMapping("/{applicationId}")
    public ResponseDTO<Response> get(@PathVariable Long applicationId) {
        Response response = applicationService.get(applicationId);
        return ok(response);
    }

    @PutMapping("/{applicationId}")
    public ResponseDTO<Response> update(@PathVariable Long applicationId, @RequestBody Request request) {
        Response response = applicationService.update(applicationId, request);
        return ok(response);
    }

    @DeleteMapping("/{applicationId}")
    public ResponseDTO<Void> delete(@PathVariable Long applicationId) {
        applicationService.delete(applicationId);
        return ok();
    }

    @PostMapping("/{applicationId}/terms")
    public ResponseDTO<Boolean> acceptTerms(@PathVariable Long applicationId, @RequestBody AcceptTerms request) {
        boolean response = applicationService.acceptTerms(applicationId, request);
        return ok(response);
    }

    @PostMapping("/{applicationId}/files")
    public ResponseDTO<Void> upload(@PathVariable Long applicationId, MultipartFile file) {
        fileStorageService.save(applicationId, file);
        return ok();
    }

    @GetMapping("/{applicationId}/files")
    public ResponseEntity<Resource> download(@PathVariable Long applicationId, @RequestParam(value = "fileName") String fileName) {
        Resource file = fileStorageService.load(applicationId, fileName);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("/{applicationId}/files/infos")
    public ResponseDTO<List<FileDTO>> getFilesInfos(@PathVariable Long applicationId) {
        List<FileDTO> fileDTOList = fileStorageService.loadAll(applicationId).map(path -> {
            String fileName = path.getFileName().toString();
            return FileDTO.builder()
                    .name(fileName)
                    .url(MvcUriComponentsBuilder.fromMethodName(ApplicationController.class, "download", fileName).build().toString())
                    .build();
        }).collect(Collectors.toList());
        return ok(fileDTOList);
    }

    @DeleteMapping("/{applicationId}/files")
    public ResponseDTO<Void> deleteAll(@PathVariable Long applicationId) {
        fileStorageService.deleteAll(applicationId);
        return ok();
    }

    @PutMapping("/{applicationId}/contract")
    public ResponseDTO<Response> contract(@PathVariable Long applicationId) {
        Response response = applicationService.contract(applicationId);
        return ok(response);
    }
}
