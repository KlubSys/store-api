package com.klub.store.api.controller.restapi;

import com.klub.store.api.controller.dto.DownloadTaskDto;
import com.klub.store.api.exception.NotFoundException;
import com.klub.store.api.model.entity.KlubBlockGroup;
import com.klub.store.api.model.entity.KlubDownloadUploadTask;
import com.klub.store.api.repository.KlubBlockGroupRepository;
import com.klub.store.api.repository.KlubDownloadUploadTaskRepository;
import com.klub.store.api.service.KlubDownloadUploadTaskService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/downloads")
public class DownloadController {

    private final KlubDownloadUploadTaskService klubDownloadUploadTaskService;
    private final KlubBlockGroupRepository klubBlockGroupRepository;
    private final KlubDownloadUploadTaskRepository klubDownloadUploadTaskRepository;

    @Autowired
    public DownloadController(KlubDownloadUploadTaskService klubDownloadUploadTaskService,
                              KlubBlockGroupRepository klubBlockGroupRepository,
                              KlubDownloadUploadTaskRepository klubDownloadUploadTaskRepository) {
        this.klubDownloadUploadTaskService = klubDownloadUploadTaskService;
        this.klubBlockGroupRepository = klubBlockGroupRepository;
        this.klubDownloadUploadTaskRepository = klubDownloadUploadTaskRepository;
    }

    @PostMapping("init")
    public ResponseEntity<Map<String, Object>> initDownload(
            @RequestParam("blocGroupRef") String blocGroupRef) throws NotFoundException {
        Optional<KlubBlockGroup> blocGroupCtn = klubBlockGroupRepository
                .findByIdentifier(blocGroupRef);
        if (blocGroupCtn.isEmpty()) throw new NotFoundException("No such bloc group exists");

        KlubDownloadUploadTask dwn = klubDownloadUploadTaskService.createTask(blocGroupCtn.get());
        Map<String, Object> response = new HashMap<>();
        response.put("id", dwn.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<DownloadTaskDto>> getDownloads(@RequestParam("blocGroupRef") String[] blocGroupRef){
        List<DownloadTaskDto> response = klubDownloadUploadTaskService
                .getDownloadsForBlocGroupRef(List.of(blocGroupRef))
                .stream().map(DownloadTaskDto::from).collect(Collectors.toList());
        return new ResponseEntity<>(response,HttpStatus.OK);

    }

    @GetMapping("{id}")
    public ResponseEntity<DownloadTaskDto> getDownload(@PathVariable("id") String id) throws NotFoundException {
        Optional<KlubDownloadUploadTask> dwnCtn = klubDownloadUploadTaskRepository.findById(id);
        if (dwnCtn.isEmpty()) throw new NotFoundException("No such download task");

        DownloadTaskDto response = DownloadTaskDto.from(dwnCtn.get());
        return new ResponseEntity<>(response,HttpStatus.OK);

    }

    @PutMapping("{id}/_data")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> uploadData(@PathVariable("id") String id,
                                             @RequestBody String body)
            throws NotFoundException {
        Optional<KlubDownloadUploadTask> dwnCtn = klubDownloadUploadTaskRepository.findById(id);
        if (dwnCtn.isEmpty()) throw new NotFoundException("No such download task");
        klubDownloadUploadTaskService.updateData(dwnCtn.get(), body);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping(value = "{id}/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE )
    public void download(@PathVariable("id") String id, HttpServletResponse response) throws IOException {

        File fileData = new File("file.dat");// File.createTempFile("temp", "file.txt");
        FileUtils.writeStringToFile(fileData, "abcdefghijklmnopqrstuvwxyz".repeat(10000), "UTF-8");

        InputStream file = new FileInputStream(fileData);

        response.setHeader("Content-Disposition", "attachment; filename=\"dowbload_id\"");


        int readBytes = 0;
        byte[] toDownload = new byte[100];
        OutputStream downloadStream = response.getOutputStream();

        while((readBytes = file.read(toDownload))!= -1){
            downloadStream.write(toDownload, 0, readBytes);
        }
        downloadStream.flush();
        downloadStream.close();

    }
}
