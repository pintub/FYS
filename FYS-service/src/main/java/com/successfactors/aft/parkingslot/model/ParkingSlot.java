package com.successfactors.aft.parkingslot.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ParkingSlot {

    public ParkingSlot(){
    }

    public ParkingSlot(long id, String state){
        this.id = id;
        this.state = state;
    }

    @Id
    private long id;

    private long geoTag;

    private int parkingRow;

    private String state;

    private String sizeOfSlot;

    private String reservedType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGeoTag() {
        return geoTag;
    }

    public void setGeoTag(long geoTag) {
        this.geoTag = geoTag;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSizeOfSlot() {
        return sizeOfSlot;
    }

    public void setSizeOfSlot(String sizeOfSlot) {
        this.sizeOfSlot = sizeOfSlot;
    }

    public String getReservedType() {
        return reservedType;
    }

    public void setReservedType(String reservedType) {
        this.reservedType = reservedType;
    }

    public int getParkingRow() {
        return parkingRow;
    }

    public void setParkingRow(int parkingRow) {
        this.parkingRow = parkingRow;
    }
}
