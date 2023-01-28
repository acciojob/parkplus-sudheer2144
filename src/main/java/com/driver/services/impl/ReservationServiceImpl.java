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

        Reservation reservation=new Reservation();

        if(!userRepository3.existsById(userId)){
            throw new RuntimeException("Cannot make reservation");
        }
        if(!parkingLotRepository3.existsById(parkingLotId)){
            throw new RuntimeException("Cannot make reservation");
        }

        ParkingLot parkingLot=parkingLotRepository3.findById(parkingLotId).get();
        User user=userRepository3.findById(userId).get();

        List<Spot> spotList=parkingLot.getSpotList();

        Spot createdSpot=null;

        int wheels=Integer.MAX_VALUE;

        for(Spot spot:spotList){
            int spotWheels = getReqSpotWheels(spot);
            if(spotWheels>=numberOfWheels && !spot.getOccupied()){
                if(spotWheels<wheels){
                    wheels=spotWheels;
                    createdSpot=spot;
                    break;
                }
            }
        }
        if(createdSpot==null){
            throw new RuntimeException("Cannot make reservation");
        }

        createdSpot.setOccupied(true);
        reservation.setSpot(createdSpot);
        reservation.setNumberOfHours(timeInHours);
        reservation.setUser(user);

        List<Reservation> spotReservations=createdSpot.getReservationList();
        if(spotReservations==null){
            spotReservations=new ArrayList<>();
        }
        spotReservations.add(reservation);
        createdSpot.setReservationList(spotReservations);

        List<Reservation> userReservations=user.getReservationList();
        if(userReservations==null){
            userReservations=new ArrayList<>();
        }
        userReservations.add(reservation);
        user.setReservationList(userReservations);

        userRepository3.save(user);
        spotRepository3.save(createdSpot);
        parkingLotRepository3.save(parkingLot);

        return reservation;
    }

    public int getReqSpotWheels(Spot GivenSpot){
        int wheels=2;
        if(GivenSpot.getSpotType().equals(SpotType.OTHERS)){wheels=5;}
        if(GivenSpot.getSpotType().equals(SpotType.FOUR_WHEELER)){wheels=4;}
        return wheels;
    }
    public SpotType getReqSpotType(int wheels){
        SpotType spotType=SpotType.TWO_WHEELER;
        if(wheels>4){spotType=SpotType.OTHERS;}
        else if(wheels>2){spotType=SpotType.FOUR_WHEELER;}
        return spotType;
    }
}
