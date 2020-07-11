package com.successfactors.aft.parkingslot.service;

import com.successfactors.aft.parkingslot.model.ParkingSlot;
import com.successfactors.aft.parkingslot.repository.ParkingSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingSlotService {

    @Autowired
    private ParkingSlotRepository parkingSlotRepository;

    public List<ParkingSlot> getAllParkingSlots() {
        List<ParkingSlot> allParkingSlots = new ArrayList<>();
        parkingSlotRepository.findAll().forEach(allParkingSlots::add);

        return allParkingSlots;
    }

    public void addParkingSlot(ParkingSlot parkingSlot) {
        parkingSlotRepository.save(parkingSlot);
    }

    public void updateParkingSlot(String state, long id) {
        parkingSlotRepository.setStateInfoById(state, id);
    }

    public long count(){
        return parkingSlotRepository.count();
    }
}
