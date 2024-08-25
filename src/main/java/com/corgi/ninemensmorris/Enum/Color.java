package com.corgi.ninemensmorris.Enum;

/**
 *
 * This enum represents the color of the player.
 * RED: The player is red.
 * YELLOW: The player is yellow.
 *
 */
public enum Color {
    RED("Red"),
    YELLOW("Yellow");

    private final String label;

    /**
     * A getter method to return the label
     * @return the label text
     */
    public String getLabel() {
        return label;
    }

    Color(String label) {
        this.label = label;
    }
}
