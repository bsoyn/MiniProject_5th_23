package untitled.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import untitled.domain.event.CoverCreated;
import untitled.domain.event.SummaryCreated;
import untitled.domain.event.BookRegistered;
import untitled.domain.event.PublicationRequested; 
import untitled.domain.event.WritingCompleted; 

import untitled.domain.aggregate.Manuscript;
import untitled.repository.ManuscriptRepository;

import untitled.dto.RequestPublicationDTO; 
import untitled.dto.TempSaveManuscriptDTO; 


@Service
@RequiredArgsConstructor
public class ManuscriptService {

    private final ManuscriptRepository manuscriptRepository;

    public Manuscript saveOrUpdateManuscript(RequestPublicationDTO dto) {
        if (dto.getId() != null) {
            return manuscriptRepository.findById(dto.getId())
                .map(manuscript -> {
                    manuscript.requestPublication(dto);
                    Manuscript saved = manuscriptRepository.save(manuscript); // 저장

                    PublicationRequested publicationRequested = new PublicationRequested(saved); 
                    publicationRequested.publishAfterCommit();
                    return saved;
                })
                .orElseGet(() -> saveNewManuscript(dto));
        }

        return saveNewManuscript(dto);
    }


    private Manuscript saveNewManuscript(RequestPublicationDTO dto) {
        Manuscript manuscript = new Manuscript();
        manuscript.requestPublication(dto);
        
        Manuscript saved = manuscriptRepository.save(manuscript); // 먼저 저장

        PublicationRequested publicationRequested = new PublicationRequested(saved); 
        publicationRequested.publishAfterCommit(); // 그 다음 이벤트 생성

        return saved;
    }


    public Manuscript completeWriting(Long manuscriptId, String penName) {
        return manuscriptRepository.findById(manuscriptId)
            .map(manuscript -> {
                WritingCompleted event = new WritingCompleted(manuscript, penName); 
                event.publishAfterCommit(); 
                return manuscript;
            })
            .orElseThrow(() -> new RuntimeException("해당 ID의 원고가 존재하지 않습니다."));
    }

    public Manuscript tempSaveManuscript(TempSaveManuscriptDTO dto) {
        if (dto.getManuscriptId() != null) {
            return manuscriptRepository.findById(dto.getManuscriptId())
                .map(manuscript -> {
                    manuscript.tempSave(dto);
                    return manuscript;
                })
                .orElseGet(() -> saveNewTempManuscript(dto));
        }

        return saveNewTempManuscript(dto);
    }

    private Manuscript saveNewTempManuscript(TempSaveManuscriptDTO dto) {
        Manuscript manuscript = new Manuscript();
        manuscript.tempSave(dto);
        return manuscriptRepository.save(manuscript);
    }

    public void applySummaryCreated(SummaryCreated event) {
        manuscriptRepository.findById(event.getManuscriptId()).ifPresent(manuscript -> {
            manuscript.applySummary(event.getSummary(), event.getCategory(), event.getPrice());
            manuscriptRepository.save(manuscript);
        });
    }

    public void applyCoverCreated(CoverCreated event) {
        manuscriptRepository.findById(event.getManuscriptId()).ifPresent(manuscript -> {
            manuscript.applyCover(event.getImageUrl());
            manuscriptRepository.save(manuscript);
        });
    }

    public void applyBookRegistered(BookRegistered event) {
        manuscriptRepository.findById(event.getManuscriptId()).ifPresent(manuscript -> {
            manuscript.markAsRegistered();
            manuscriptRepository.save(manuscript);
        });
    }



}
