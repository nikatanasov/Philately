package Philately.stamp.service;

import Philately.stamp.model.Stamp;
import Philately.stamp.model.WishedStamp;
import Philately.stamp.repository.StampRepository;
import Philately.stamp.repository.WishedStampRepository;
import Philately.user.model.User;
import Philately.web.dto.AddingStampRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StampService {
    private final StampRepository stampRepository;
    private final WishedStampRepository wishedStampRepository;

    @Autowired
    public StampService(StampRepository stampRepository, WishedStampRepository wishedStampRepository) {
        this.stampRepository = stampRepository;
        this.wishedStampRepository = wishedStampRepository;
    }

    //Adding new stamp
    public void addNewStamp(User user, AddingStampRequest addingStampRequest) {
        Stamp stamp = Stamp.builder()
                .name(addingStampRequest.getName())
                .description(addingStampRequest.getDescription())
                .paper(addingStampRequest.getPaper())
                .imageUrl(addingStampRequest.getImageUrl())
                .owner(user)
                .build();
        stampRepository.save(stamp);
    }

    public List<Stamp> getOfferedStamps(User user) {
        return stampRepository.findAll().stream().filter(s->!s.getOwner().getId().equals(user.getId())).sorted(Comparator.comparing(Stamp::getName).thenComparing(Stamp::getDescription)).collect(Collectors.toList());
    }

    public void addWishedStamp(User user, UUID id) {
        Stamp stamp = stampRepository.findById(id).orElseThrow(()->new RuntimeException("No stamp with id "+id+"!"));
        List<WishedStamp> wishedStamps = wishedStampRepository.findAll().stream().filter(s->s.getOwner().equals(user)).collect(Collectors.toList());
        for(WishedStamp ws:wishedStamps){
            if(ws.getName().equals(stamp.getName()) && ws.getDescription().equals(stamp.getDescription()) && ws.getImageUrl().equals(stamp.getImageUrl())) return;
        }

        WishedStamp wishedStamp = WishedStamp.builder()
                .name(stamp.getName())
                .description(stamp.getDescription())
                .imageUrl(stamp.getImageUrl())
                .owner(user)
                .build();
        wishedStampRepository.save(wishedStamp);
    }

    public List<WishedStamp> getWishedStamps(User user) {
        return wishedStampRepository.findAll().stream().filter(s->s.getOwner().getId().equals(user.getId())).sorted(Comparator.comparing(WishedStamp::getName).thenComparing(WishedStamp::getDescription)).collect(Collectors.toList());
    }

    public void removeWishedStamp(UUID id) {
        //Stamp stamp = stampRepository.findById(id).orElseThrow(()->new RuntimeException("No stamp with id "+id+"!"));
        WishedStamp wishedStamp = wishedStampRepository.findById(id).orElseThrow(()->new RuntimeException("No wishedStamp with id "+id+"!"));
        wishedStampRepository.delete(wishedStamp);
    }
}
