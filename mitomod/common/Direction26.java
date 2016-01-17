package com.mito.mitomod.common;

public class Direction26 {
    /** Converts a side to the opposite side. This is the same as XOR'ing it with 1. */
    public static final int[] oppositeSide = new int[] {1, 0, 3, 2, 5, 4, 8, 9, 6, 7, 22, 23, 24, 25, 18, 19, 20, 21, 14, 15, 16, 17, 10, 11, 12, 13};
    /** gives the offset required for this axis to get the block at that side. */
    public static final int[] offsetsXForSide = new int[] {0, 0, 0, 0, -1, 1, -1, -1, 1, 1, 0, -1, -1, -1, 0, 1, 1, 1, 0, -1, -1, -1, 0, 1, 1, 1};
    /** gives the offset required for this axis to get the block at that side. */
    public static final int[] offsetsYForSide = new int[] {-1, 1, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1, 1, 1, 1, 1, 1, 1, 1, 1};
    /** gives the offset required for this axis to get the block at that side. */
    public static final int[] offsetsZForSide = new int[] {0, 0, -1, 1, 0, 0, -1, 1, 1, -1, -1, -1, 0, 1, 1, 1, 0, -1, -1, -1, 0, 1, 1, 1, 0, -1};
    public static final String[] facings = new String[] {"DOWN", "UP", "NORTH", "SOUTH", "WEST", "EAST", "NORTHWEST", "SOUTHWEST", "SOUTHEAST", "NORTHEAST", "LOWER_NORTH", "LOWER_NORTHWEST", "LOWER_WEST", "LOWER_SOUTHWEST", "LOWER_SOUTH", "LOWER_SOUTHEAST", "LOWER_EAST", "LOWER_NORTHEAST", "UPPER_NORTH", "UPPER_NORTHWEST", "UPPER_WEST", "UPPER_SOUTHWEST", "UPPER_SOUTH", "UPPER_SOUTHEAST", "UPPER_EAST", "UPPER_NORTHEAST"};
}