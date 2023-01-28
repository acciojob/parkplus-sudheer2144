package com.driver.model;

import com.driver.services.impl.PaymentServiceImpl;

import javax.persistence.*;
import java.util.stream.Stream;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(value = EnumType.STRING)
    private PaymentMode paymentMode;
    private boolean paymentCompleted;

    @OneToOne
    @JoinColumn
    private Reservation reservation;

    public Payment(int id, PaymentMode paymentMode, boolean paymentCompleted, Reservation reservation) {
        this.id = id;
        this.paymentMode = paymentMode;
        this.paymentCompleted = paymentCompleted;
        this.reservation = reservation;
    }

    public Payment() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(PaymentMode paymentMode) {
        this.paymentMode = paymentMode;
    }

    public boolean isPaymentCompleted() {
        return paymentCompleted;
    }

    public void setPaymentCompleted(boolean paymentCompleted) {
        this.paymentCompleted = paymentCompleted;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
