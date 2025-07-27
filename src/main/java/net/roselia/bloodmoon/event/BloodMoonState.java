package net.roselia.bloodmoon.event;

public class BloodMoonState {
    public static boolean IS_BLOOD_MOON = false;
    public static boolean BLOOD_MOON_RISEN = false;

    private static final long FADE_IN_START = 12800;   // Start fading in at early night
    private static final long FADE_IN_END = 14000;     // Fully faded in
    private static final long FADE_OUT_START = 21500;  // Start fading out late night
    private static final long FADE_OUT_END = 23450;    // Fully faded out

    public static float getBloodMoonIntensity(long timeOfDay) {
        if (!IS_BLOOD_MOON) return 0.0f;

        // Fade in
        if (timeOfDay >= FADE_IN_START && timeOfDay < FADE_IN_END) {
            float progress = (timeOfDay - FADE_IN_START) / (float)(FADE_IN_END - FADE_IN_START);
            return progress;
        }
        // Fully faded in
        else if (timeOfDay >= FADE_IN_END && timeOfDay < FADE_OUT_START) {
            return 1.0f;
        }
        // Fade out
        else if (timeOfDay >= FADE_OUT_START && timeOfDay < FADE_OUT_END) {
            float progress = 1.0f - (timeOfDay - FADE_OUT_START) / (float)(FADE_OUT_END - FADE_OUT_START);
            return progress;
        }
        // Not blood moon time
        return 0.0f;
    }
}