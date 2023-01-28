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
        ParkingLot createdParkingLot = new ParkingLot();
        createdParkingLot.setName(name);
        createdParkingLot.setAddress(address);
        parkingLotRepository1.save(createdParkingLot);
        return createdParkingLot;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {

        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();

        Spot createdSpot=new Spot();

        SpotType spotType=SpotType.TWO_WHEELER;
        if(numberOfWheels>2){
            spotType=SpotType.FOUR_WHEELER;
        }
        if(numberOfWheels>4){
            spotType=SpotType.OTHERS;
        }

        createdSpot.setOccupied(false);
        createdSpot.setParkingLot(parkingLot);
        createdSpot.setPricePerHour(pricePerHour);
        createdSpot.setSpotType(spotType);

        parkingLot.getSpotList().add(createdSpot);

        parkingLotRepository1.save(parkingLot);

        return createdSpot;
    }

    @Override
    public void deleteSpot(int spotId) {
            spotRepository1.deleteById(spotId);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        Spot foundSpot=new Spot();
        ParkingLot parkingLot=parkingLotRepository1.findById(parkingLotId).get();
        List<Spot> spotList=parkingLot.getSpotList();
        for(Spot spot:spotList){
            if(spot.getId()==spotId){
                foundSpot=spot;
                break;
            }
        }
        foundSpot.setParkingLot(parkingLot);
        foundSpot.setPricePerHour(pricePerHour);
        spotRepository1.save(foundSpot);
        return foundSpot;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        parkingLotRepository1.deleteById(parkingLotId);
    }
}
