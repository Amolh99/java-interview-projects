package com.example.modern;

import java.util.Objects;

/** Java 11 additions to String, used to normalise order-note text. */
public final class OrderText {

    private OrderText() {}

    public static boolean isMissing(String note) {
        return note == null || note.isBlank();
    }

    public static String normalise(String note) {
        return Objects.requireNonNullElse(note, "").strip();
    }

    public static long lineCount(String multiline) {
        return normalise(multiline).lines().count();
    }
}
