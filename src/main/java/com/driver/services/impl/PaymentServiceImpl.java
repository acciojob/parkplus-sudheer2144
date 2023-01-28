package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {

        Reservation newReservation=reservationRepository2.findById(reservationId).get();

        int bill = newReservation.getSpot().getPricePerHour() * newReservation.getNumberOfHours();
        if(amountSent<bill){
            throw new RuntimeException("Insufficient Amount");
        }

        PaymentMode paymentMode=null;
        for(PaymentMode defaultModes:PaymentMode.values()){
            if(defaultModes.toString().equals(mode.toUpperCase())){
                paymentMode=defaultModes;
                break;
            }
        }
        if(paymentMode==null){
            throw new RuntimeException("Payment mode not detected");
        }

        Payment newPayment = new Payment();
        newPayment.setPaymentMode(paymentMode);
        newPayment.setPaymentCompleted(true);
        newPayment.setReservation(newReservation);
        newReservation.setPayment(newPayment);

        reservationRepository2.save(newReservation);

        return newPayment;
    }
}
