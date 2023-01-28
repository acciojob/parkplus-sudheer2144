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

        Reservation reservation=reservationRepository2.findById(reservationId).get();

        int bill = reservation.getSpot().getPricePerHour() * reservation.getNumberOfHours();
        if(amountSent<bill){
            throw new RuntimeException("Insufficient Amount");
        }

        PaymentMode paymentMode=null;
        for(PaymentMode defMode:PaymentMode.values()){
            if(defMode.toString().equals(mode.toUpperCase())){
                paymentMode=defMode;
                break;
            }
        }
        if(paymentMode==null){
            throw new RuntimeException("Payment mode not detected");
        }

        Payment payment = new Payment();
        payment.setPaymentMode(paymentMode);
        payment.setPaymentCompleted(true);
        payment.setReservation(reservation);
        reservation.setPayment(payment);

        reservationRepository2.save(reservation);

        return payment;
    }
}
