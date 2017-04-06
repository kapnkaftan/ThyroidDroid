package com.unmcelearning.android.thyroidpathology;

/**
 * Created by adamk_000 on 1/7/2017.
 */

public class Level {

    private String levelString;
    private boolean levelGraduated;
    private String photoCred;

    public Level(String levelName, boolean levelGraduated, String photoCred) {

        setLevelString(levelName);
        setLevelGraduated(levelGraduated);
        setPhotoCred(photoCred);

    }

    public void setLevelString(String levelString) {
        this.levelString = levelString;
    }

    public void setPhotoCred(String photoCred) {
        this.photoCred = photoCred;
    }

    public void setLevelGraduated(boolean levelGraduated) {
        this.levelGraduated = levelGraduated;
    }

    public String getLevelString() {
        return levelString;
    }

    public boolean getLevelGraduated() {
        return levelGraduated;
    }

    public String getPhotoCred() {
        return photoCred;
    }
}
