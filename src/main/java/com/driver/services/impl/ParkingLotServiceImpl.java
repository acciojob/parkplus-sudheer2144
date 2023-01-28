package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setName(name);
        parkingLot.setAddress(address);
        parkingLotRepository1.save(parkingLot);
        return parkingLot;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {

        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();

        Spot spot=new Spot();

        SpotType spotType=SpotType.TWO_WHEELER;
        if(numberOfWheels>2){
            spotType=SpotType.FOUR_WHEELER;
        }
        if(numberOfWheels>4){
            spotType=SpotType.OTHERS;
        }

        spot.setOccupied(false);
        spot.setParkingLot(parkingLot);
        spot.setPricePerHour(pricePerHour);
        spot.setSpotType(spotType);

        List<Spot> spotList=parkingLot.getSpotList();
        if(spotList==null){
            spotList=new ArrayList<>();
        }
        spotList.add(spot);
        parkingLot.setSpotList(spotList);

        parkingLotRepository1.save(parkingLot);

        spotRepository1.save(spot);


        return spot;
    }

    @Override
    public void deleteSpot(int spotId) {
        if(spotRepository1.existsById(spotId)) {
            spotRepository1.deleteById(spotId);
        }
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        Spot spot=null;
        if(spotRepository1.existsById(spotId)){
            spot=spotRepository1.findById(spotId).get();
            if(parkingLotRepository1.existsById(parkingLotId)){
                spot.setParkingLot(parkingLotRepository1.findById(parkingLotId).get());
                spot.setPricePerHour(pricePerHour);
            }
        }
        return spot;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        if(parkingLotRepository1.existsById(parkingLotId)) {
            parkingLotRepository1.deleteById(parkingLotId);
        }
    }
}
