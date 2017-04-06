package com.unmcelearning.android.thyroidpathology;

import java.util.ArrayList;

/**
 * Created by adamk_000 on 11/30/2016.
 */

public class Topic {

    private String topicName;
    private ArrayList<Level> topicLevelsArrayList;

    public Topic(String topicName, ArrayList<Level> topicLevelsArrayList) {

        setTopicName(topicName);
        setTopicLevelsArrayList(topicLevelsArrayList);

    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void setTopicLevelsArrayList(ArrayList<Level> topicLevelsArrayList) {
        this.topicLevelsArrayList = topicLevelsArrayList;
    }

    public String getTopicName() {
        return topicName;
    }

    public ArrayList<Level> getTopicLevelsArrayList() {
        return topicLevelsArrayList;
    }
}
