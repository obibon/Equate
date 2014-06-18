package com.llamacorp.unitcalc;

import java.util.ArrayList;
import java.util.Collections;

public class UnitType {
	//this is for communication with the parent
	OnConvertionListener mCallback;

	// Parent class must implement this interface
	public interface OnConvertionListener {
		public void convertFromTo(Unit fromUnit, Unit toUnit);
	}

	private String mName;
	private ArrayList<Unit> mUnitArray;
	private int mCurrUnitPos;
	private boolean mIsUnitSelected;

	/**
	 * Constructor
	 * @param hosting class must implement a function to do raw number conversion
	 */	
	public UnitType(Object parent, String name){
		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback = (OnConvertionListener) parent;
		} catch (ClassCastException e) {
			throw new ClassCastException(parent.toString()
					+ " must implement OnConvertKeySelectedListener");
		}

		mName = name;
		mUnitArray = new ArrayList<Unit>();
		mIsUnitSelected = false;
	}

	/**
	 * Used to build a UnitType
	 */
	public void addUnit(Unit u){
		mUnitArray.add(u);
	}
	
	/** Swap positions of units */	
	public void swapUnits(int pos1, int pos2){
		/*ArrayList<Unit> al = new ArrayList<Unit>();
		
		al.add(new UnitScalar("bderp1",43));
		al.add(new UnitScalar("bderp2",433));
		al.add(new UnitScalar("bderp3",42343));
		al.add(new UnitScalar("bderp4",14243));
		al=mUnitArray;
		System.out.println(al);
		Collections.swap(al, 2, 1);
		System.out.println(al);
		*/
		Collections.swap(mUnitArray, pos1, pos2);
		System.out.println(mUnitArray);
	}

	/**
	 * Find the position of the unit in the unit array
	 * @return -1 if selection failed, otherwise the position of the unit
	 */		
	public int findUnitPosition(Unit unit){
		for(int i=0;i<mUnitArray.size();i++){
			if(unit.equals(mUnitArray.get(i)))
				return i; //found the unit
		}
		return -1;  //if we didn't find the unit
	}


	/**
	 * If mCurrUnit not set, set mCurrUnit
	 * If mCurrUnit already set, call functions to perform a convert
	 */		
	public boolean selectUnit(int pos){
		//used to tell caller if we needed to do a conversion
		boolean didConvert = false;
		//If we've already selected a unit, do conversion
		if(mIsUnitSelected){
			//if the unit is the same as before, de-select it
			if(mCurrUnitPos==pos){
				mIsUnitSelected=false;
				return didConvert;
			}
			convert(pos);
			didConvert = true;
		}

		//Select new unit regardless
		mCurrUnitPos = pos;
		//Engage set flag
		mIsUnitSelected = true;
		return didConvert;
	}


	/**
	 * Resets mIsUnitSelected flag
	 */		
	public void clearUnitSelection(){
		mIsUnitSelected = false;
	}

	public boolean isUnitSelected(){
		return mIsUnitSelected;
	}

	public String getUnitTypeName(){
		return mName;
	}
	
	/**
	 * @param Index of Unit in the mUnitArray list
	 * @return String name to be displayed on convert button
	 */
	public String getUnitDisplayName(int pos){
		return mUnitArray.get(pos).toString();
	}
	
	public String getLowercaseLongName(int pos){
		return mUnitArray.get(pos).getLowercaseLongName();
	}
	
	/** Method builds charSequence array of long names of undisplayed units
	 * @param Array of long names of units not being displayed
	 * @return Number of units being displayed, used to find undisplayed units
	 */
	public CharSequence[] getUndisplayedUnitNames(int numDispUnits){
		//ArrayList<Unit> subList = mUnitArray.subList(numDispUnits, mUnitArray.size());
		//return subList.toArray(new CharSequence[subList.size()]);
		int arraySize = mUnitArray.size() - numDispUnits;
		CharSequence[] cs = new CharSequence[arraySize];
		for(int i=0;i<arraySize;i++){
			cs[i] = mUnitArray.get(numDispUnits+i).getLongName();
		}
		return cs;
	}
	
	public Unit getSelectedUnit(){
		return mUnitArray.get(mCurrUnitPos);
	}

	public int size() {
		return mUnitArray.size();
	}

	public int getCurrUnitPos(){
		return mCurrUnitPos;
	}

	/**
	 * Used to gather values to convert to and from and then call parent class to do raw conversion
	 * Convert from unit is specified by the current Unit
	 * @param newUnitPos is the Unit position to convert to
	 */
	private void convert(int newUnitPos){
		Unit fromUnit = mUnitArray.get(mCurrUnitPos);
		Unit toUnit = mUnitArray.get(newUnitPos);
		mCallback.convertFromTo(fromUnit, toUnit);
	}
}