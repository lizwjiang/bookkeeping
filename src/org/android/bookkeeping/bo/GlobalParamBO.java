/* GlobalParamBO.java
* Created on 2011-8-17
*/
package org.android.bookkeeping.bo;
/**
 * 
* Add one sentence class summary here.
* Add class description here.
*
* @author Johnson_Jiang01
* @version 1.0, 2011-8-17
 */
public class GlobalParamBO {
	private int id;
	private Double singleMaxAmount;
	private Double weeklyMaxAmount;
	private Double monthlyMaxAmount;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Double getSingleMaxAmount() {
		return singleMaxAmount;
	}
	public void setSingleMaxAmount(Double singleMaxAmount) {
		this.singleMaxAmount = singleMaxAmount;
	}
	public Double getWeeklyMaxAmount() {
		return weeklyMaxAmount;
	}
	public void setWeeklyMaxAmount(Double weeklyMaxAmount) {
		this.weeklyMaxAmount = weeklyMaxAmount;
	}
	public Double getMonthlyMaxAmount() {
		return monthlyMaxAmount;
	}
	public void setMonthlyMaxAmount(Double monthlyMaxAmount) {
		this.monthlyMaxAmount = monthlyMaxAmount;
	}
	
}
