package id.ac.ui.cs.advprog.coworkingspace.workspace.service.rent;

import id.ac.ui.cs.advprog.coworkingspace.workspace.dto.SpaceRentResponse;
import id.ac.ui.cs.advprog.coworkingspace.workspace.dto.SpaceRentRequest;
import id.ac.ui.cs.advprog.coworkingspace.workspace.model.rent.SpaceRent;

import java.util.List;

public interface SpaceRentService {
    List<SpaceRentResponse> findAll();
    List<SpaceRentResponse> findAllByUserId(Integer userId);
    SpaceRentResponse getSpaceRentById(Integer id);
    SpaceRent create(Integer userId, SpaceRentRequest spaceRentRequest);
    void delete(Integer id);
    SpaceRent extendRent(Integer id, Integer addedDuration);
    SpaceRent upgrade(Integer id, Integer personalSpaceId);
    void checkRentFinish(Integer workspaceId);


}
