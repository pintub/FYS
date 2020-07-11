package com.fys.camfeedprocessing.bean;

import com.fys.camfeedprocessing.util.ParkingSlotState;

import java.util.List;
import java.util.stream.Collectors;

public class ParkingSlot {
    private long id;
    private String state = ParkingSlotState.VACANT.name();

    public ParkingSlot(int id, ParkingSlotState state){
        this.id = id;
        this.state = state.name();
    }

    public ParkingSlot(int id){
        this.id = id;
    }

    public static List<ParkingSlot> createParkingSlots(List<Integer> parkingSlots){
        return parkingSlots.stream().map(slot -> new ParkingSlot(slot)).collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    @Override
    public String toString() {
        return "ParkingSlot{" +
                "parkingSlotId=" + id +
                ", state=" + state +
                '}';
    }
}
