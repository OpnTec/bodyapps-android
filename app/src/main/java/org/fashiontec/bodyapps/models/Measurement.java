/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */

package org.fashiontec.bodyapps.models;

import java.io.Serializable;

/**
 * Model object for measurement
 */
public class Measurement implements Serializable {

	private static final long serialVersionUID = 1L;
	private String ID;
	private String userID;
	private int personID;
	private String created;
	private int unit;
	private String mid_neck_girth;
	private String bust_girth;
	private String waist_girth;
	private String hip_girth;
	private String across_back_shoulder_width;
	private String shoulder_drop;
	private String shoulder_slope_degrees;
	private String arm_length;
	private String upper_arm_girth;
	private String armscye_girth;
	private String height;
	private String hip_height;
	private String wrist_girth;
    private String head_girth;
    private String head_and_neck_length;
    private String upper_chest_girth;
    private String shoulder_length;
    private String shoulder_and_arm_length;
    private String pic_front;
    private String pic_side;
    private String pic_back;
    private String notes;
    boolean isSynced;
    private int shoulder_type;
    private int arm_type;
    private int chest_type;
    private int back_shape;
    private int stomach_shape;

    public Measurement(String iD, String userID, int personID, int unit) {
        super();
        ID = iD;
        this.userID = userID;
        this.personID = personID;
        this.unit = unit;
        this.mid_neck_girth = "";
        this.bust_girth = "";
        this.waist_girth = "";
        this.hip_girth = "";
        this.across_back_shoulder_width = "";
        this.shoulder_drop = "";
        this.shoulder_slope_degrees = "";
        this.arm_length = "";
        this.upper_arm_girth = "";
        this.armscye_girth = "";
        this.height = "";
        this.hip_height = "";
        this.wrist_girth = "";
        this.pic_front="";
        this.pic_side="";
        this.pic_back="";
        this.head_girth="";
        this.head_and_neck_length="";
        this.shoulder_length="";
        this.shoulder_and_arm_length="";
        this.upper_chest_girth="";
        this.notes="";
        this.isSynced=false;

    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPic_front() {
        return pic_front;
    }

    public void setPic_front(String pic_front) {
        this.pic_front = pic_front;
    }

    public String getPic_side() {
        return pic_side;
    }

    public void setPic_side(String pic_side) {
        this.pic_side = pic_side;
    }

    public String getPic_back() {
        return pic_back;
    }

    public void setPic_back(String pic_back) {
        this.pic_back = pic_back;
    }

    public String getHead_girth() {
        return head_girth;
    }

    public void setHead_girth(String head_girth) {
        this.head_girth = head_girth;
    }

    public String getHead_and_neck_length() {
        return head_and_neck_length;
    }

    public void setHead_and_neck_length(String head_and_neck_length) {
        this.head_and_neck_length = head_and_neck_length;
    }

    public String getUpper_chest_girth() {
        return upper_chest_girth;
    }

    public void setUpper_chest_girth(String upper_chest_girth) {
        this.upper_chest_girth = upper_chest_girth;
    }

    public String getShoulder_length() {
        return shoulder_length;
    }

    public void setShoulder_length(String shoulder_length) {
        this.shoulder_length = shoulder_length;
    }

    public String getShoulder_and_arm_length() {
        return shoulder_and_arm_length;
    }

    public void setShoulder_and_arm_length(String shoulder_and_arm_length) {
        this.shoulder_and_arm_length = shoulder_and_arm_length;
    }

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public int getPersonID() {
		return personID;
	}

	public void setPersonID(int personID) {
		this.personID = personID;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public int getUnit() {
		return unit;
	}

	public void setUnit(int unit) {
		this.unit = unit;
	}

	public String getMid_neck_girth() {
		return mid_neck_girth;
	}

	public void setMid_neck_girth(String mid_neck_girth) {
		this.mid_neck_girth = mid_neck_girth;
	}

	public String getBust_girth() {
		return bust_girth;
	}

	public void setBust_girth(String bust_girth) {
		this.bust_girth = bust_girth;
	}

	public String getWaist_girth() {
		return waist_girth;
	}

	public void setWaist_girth(String waist_girth) {
		this.waist_girth = waist_girth;
	}

	public String getHip_girth() {
		return hip_girth;
	}

	public void setHip_girth(String hip_girth) {
		this.hip_girth = hip_girth;
	}

	public String getAcross_back_shoulder_width() {
		return across_back_shoulder_width;
	}

	public void setAcross_back_shoulder_width(String across_back_shoulder_width) {
		this.across_back_shoulder_width = across_back_shoulder_width;
	}

	public String getShoulder_drop() {
		return shoulder_drop;
	}

	public void setShoulder_drop(String shoulder_drop) {
		this.shoulder_drop = shoulder_drop;
	}

	public String getShoulder_slope_degrees() {
		return shoulder_slope_degrees;
	}

	public void setShoulder_slope_degrees(String shoulder_slope_degrees) {
		this.shoulder_slope_degrees = shoulder_slope_degrees;
	}

	public String getArm_length() {
		return arm_length;
	}

	public void setArm_length(String arm_length) {
		this.arm_length = arm_length;
	}

	public String getUpper_arm_girth() {
		return upper_arm_girth;
	}

	public void setUpper_arm_girth(String upper_arm_girth) {
		this.upper_arm_girth = upper_arm_girth;
	}

	public String getArmscye_girth() {
		return armscye_girth;
	}

	public void setArmscye_girth(String armscye_girth) {
		this.armscye_girth = armscye_girth;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getHip_height() {
		return hip_height;
	}

	public void setHip_height(String hip_height) {
		this.hip_height = hip_height;
	}

	public String getWrist_girth() {
		return wrist_girth;
	}

	public void setWrist_girth(String wrist_girth) {
		this.wrist_girth = wrist_girth;
	}

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean isSynced) {
        this.isSynced = isSynced;
    }

    public int getShoulder_type() {
        return shoulder_type;
    }

    public void setShoulder_type(int shoulder_type) {
        this.shoulder_type = shoulder_type;
    }

    public int getArm_type() {
        return arm_type;
    }

    public void setArm_type(int arm_type) {
        this.arm_type = arm_type;
    }

    public int getChest_type() {
        return chest_type;
    }

    public void setChest_type(int chest_type) {
        this.chest_type = chest_type;
    }

    public int getBack_shape() {
        return back_shape;
    }

    public void setBack_shape(int back_shape) {
        this.back_shape = back_shape;
    }

    public int getStomach_shape() {
        return stomach_shape;
    }

    public void setStomach_shape(int stomach_shape) {
        this.stomach_shape = stomach_shape;
    }
}
