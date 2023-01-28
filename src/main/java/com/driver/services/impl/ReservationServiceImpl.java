package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {

        try {
            if (!userRepository3.findById(userId).isPresent() || !parkingLotRepository3.findById(parkingLotId).isPresent()) {
                throw new RuntimeException("Cannot make reservation");
            }

            Spot createdSpot = null;
            User user = userRepository3.findById(userId).get();
            ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();

            List<Spot> parkingSpotList = parkingLot.getSpotList();

            int minPrice = Integer.MAX_VALUE;

            SpotType reqSpot = getReqSpotType(numberOfWheels);

            SpotType twoWheeler = SpotType.TWO_WHEELER;
            SpotType fourWheeler = SpotType.FOUR_WHEELER;
            SpotType others = SpotType.OTHERS;

            for (Spot spot : parkingSpotList) {
                SpotType spotType = spot.getSpotType();
                if (reqSpot.equals(twoWheeler) && (spotType.equals(twoWheeler) || spotType.equals(fourWheeler) || spotType.equals(others)) && !spot.getOccupied()) {
                    int price = spot.getPricePerHour() * numberOfWheels;
                    if (price < minPrice) {
                        minPrice = price;
                        createdSpot = spot;
                    }
                } else if (reqSpot.equals(fourWheeler) && spotType.equals(fourWheeler) || spotType.equals(others) && !spot.getOccupied()) {
                    int price = spot.getPricePerHour() * numberOfWheels;
                    if (price < minPrice) {
                        minPrice = price;
                        createdSpot = spot;
                    }
                } else if (reqSpot.equals(others) && spotType.equals(others) && !spot.getOccupied()) {
                    int price = spot.getPricePerHour() * numberOfWheels;
                    if (price < minPrice) {
                        minPrice = price;
                        createdSpot = spot;
                    }
                }
            }

            if (createdSpot == null) {
                throw new RuntimeException("Cannot make reservation");
            }

            Reservation reservation = new Reservation();
            reservation.setSpot(createdSpot);
            reservation.setUser(user);
            reservation.setNumberOfHours(timeInHours);

            createdSpot.setOccupied(true);
            createdSpot.getReservationList().add(reservation);

            user.getReservationList().add(reservation);

            spotRepository3.save(createdSpot);
            userRepository3.save(user);

            return reservation;
        }
        catch (Exception e){
            return null;
        }
    }


    public SpotType getReqSpotType(int wheels){
        SpotType spotType=SpotType.TWO_WHEELER;
        if(wheels>4){spotType=SpotType.OTHERS;}
        else if(wheels>2){spotType=SpotType.FOUR_WHEELER;}
        return spotType;
    }
}
