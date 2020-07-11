package com.successfactors.aft.parkingslot.repository;

import com.successfactors.aft.parkingslot.model.ParkingSlot;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ParkingSlotRepository extends CrudRepository<ParkingSlot, Long> {

    @Modifying
    @Query("update ParkingSlot u set u.state = ?1 where u.id = ?2")
    void setStateInfoById(String state, long userId);
}
