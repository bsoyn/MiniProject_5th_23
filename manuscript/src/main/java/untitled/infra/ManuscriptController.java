package untitled.infra;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import untitled.domain.*;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import java.util.UUID;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;


//<<< Clean Arch / Inbound Adaptor

@RestController
// @RequestMapping(value="/manuscripts")
@Transactional
public class ManuscriptController {

    @Autowired
    ManuscriptRepository manuscriptRepository;


    // @RequestMapping(
    //     value = "/manuscripts/request-publication",
    //     method = RequestMethod.POST,
    //     produces = "application/json;charset=UTF-8"
    // )
    // public Manuscript requestPublication(
    //     HttpServletRequest request,
    //     HttpServletResponse response,
    //     @RequestBody RequestPublicationCommand requestPublicationCommand
    // ) throws Exception {
    //     System.out.println(
    //         "##### /manuscript/requestPublication  called #####"
    //     );
    //     Manuscript manuscript = new Manuscript();
    //     manuscript.requestPublication(requestPublicationCommand);
    //     manuscriptRepository.save(manuscript);
    //     return manuscript;
    // }

    @PostMapping(value = "/manuscripts/request-publication", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Manuscript requestPublication(
        @RequestParam("authorId") Long authorId,
        @RequestParam("title") String title,
        @RequestPart("content") MultipartFile contentFile
    ) throws IOException {

        System.out.println("##### /manuscript/request-publication  called #####");
        
         // 1. 저장할 경로 설정
        String filename = UUID.randomUUID() + "_" + contentFile.getOriginalFilename();
        Path filePath = Paths.get("uploads", filename);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, contentFile.getBytes());
        
        RequestPublicationCommand command = new RequestPublicationCommand();
        command.setAuthorId(authorId);
        command.setTitle(title);
        command.setContent(filePath.toFile());

        Manuscript manuscript = new Manuscript();
        manuscript.requestPublication(command);
        manuscriptRepository.save(manuscript);

        return manuscript;
    }



}
//>>> Clean Arch / Inbound Adaptor
