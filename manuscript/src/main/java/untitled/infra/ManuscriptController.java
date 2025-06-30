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

@RestController
@Transactional
public class ManuscriptController {

    @Autowired
    ManuscriptRepository manuscriptRepository;

    @RequestMapping(
        value = "/manuscripts/request-publication",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public Manuscript requestPublication(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody RequestPublicationCommand requestPublicationCommand
    ) throws Exception {
        System.out.println(
            "##### /manuscript/request-publication  called #####"
        );

        Manuscript manuscript = new Manuscript();
        manuscript.requestPublication(requestPublicationCommand);
        return manuscript;
    }
}
