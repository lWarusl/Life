package com.example.android.life.pojos;

import com.example.android.life.R;

public class PersonField {

    private String mGender = "";
    private int mBackground = R.drawable.frame_background;

    public PersonField(String gender){
        mGender = gender;
        setBackground(gender);

    }


    public void setGender(String mGender) {
        this.mGender = mGender;
        setBackground(mGender);
    }

    private void setBackground(String gender) {
        if(gender==null) gender="";
        switch (gender){
            case LifeBoard.GENDER_FEMALE:
                mBackground = R.drawable.frame_background_female;
                break;
            case LifeBoard.GENDER_MALE:
                mBackground = R.drawable.frame_background_male;
                break;
            default:
                mBackground = R.drawable.frame_background;
                break;
        }
    }

    public void setBackground(int mBackground) {
        this.mBackground = mBackground;
    }

    public String getGender() {
        return mGender;
    }

    public int getBackground() {
        return mBackground;
    }
}
