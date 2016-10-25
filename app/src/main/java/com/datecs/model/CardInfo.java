package com.datecs.model;

import com.datecs.audioreader.rfid.ContactlessCard;

import java.util.Arrays;

/**
 * Created by Nacho on 23/10/2016.
 */

public class CardInfo {
    public final byte[] atr;
    public final int cardType;
    public final ContactlessCard contactlessCard;
    public final boolean track1Read;
    public final boolean track2Read;
    public final boolean track3Read;

    public CardInfo(int cardType, byte[] atr, boolean track1Read, boolean track2Read, boolean track3Read, ContactlessCard card) {
        this.cardType = cardType;
        this.atr = atr;
        this.track1Read = track1Read;
        this.track2Read = track2Read;
        this.track3Read = track3Read;
        this.contactlessCard = card;
    }

    @Override
    public String toString() {
        return "CardInfo{" +
                "atr=" + Arrays.toString(atr) +
                ", cardType=" + cardType +
                ", contactlessCard=" + contactlessCard +
                ", track1Read=" + track1Read +
                ", track2Read=" + track2Read +
                ", track3Read=" + track3Read +
                '}';
    }
}
