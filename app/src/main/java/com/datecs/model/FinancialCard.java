package com.datecs.model;

import com.datecs.audioreader.AudioReader;

/**
 * Created by Nacho on 23/10/2016.
 */

public class FinancialCard {
    public final byte[] data;
        public final String holder;
        public final int month;
        public final String number;
        public final int year;

        public FinancialCard(byte[] masked, byte[] data) {
            String holder = null;
            String number = null;
            int month = AudioReader.TRACK_READ_MODE_NOT_ALLOWED;
            int year = AudioReader.TRACK_READ_MODE_NOT_ALLOWED;
            if (masked != null) {
                String[] values = new String(masked).split("\u0000");
                if (values.length > 0) {
                    holder = values[AudioReader.TRACK_READ_MODE_NOT_ALLOWED];
                }
                if (values.length > AudioReader.TRACK_READ_MODE_ALLOWED) {
                    number = values[AudioReader.TRACK_READ_MODE_ALLOWED];
                }
                try {
                    month = Integer.parseInt(values[AudioReader.TRACK_READ_MODE_REQUIRED].substring(AudioReader.TRACK_READ_MODE_NOT_ALLOWED, AudioReader.TRACK_READ_MODE_REQUIRED));
                } catch (NumberFormatException e) {
                }
                try {
                    year = Integer.parseInt(values[AudioReader.TRACK_READ_MODE_REQUIRED].substring(AudioReader.STATUS_INVALID_COMMAND_NUMBER, AudioReader.STATUS_CARD_NOT_ENABLED));
                } catch (NumberFormatException e2) {
                }
            }
            this.holder = holder;
            this.number = number;
            this.month = month;
            this.year = year;
            this.data = data;
        }
}
