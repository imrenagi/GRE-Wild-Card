package com.imrenagi.greflashcard.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by imrenagi on 5/18/15.
 */
public class Word {

    public int id;

    @SerializedName("word")
    public String name;

    public String meaning;

}
