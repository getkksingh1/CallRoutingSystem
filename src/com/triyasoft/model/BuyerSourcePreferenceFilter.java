package com.triyasoft.model;

import java.util.Date;
import java.util.List;

import com.triyasoft.daos.BuyerSourcePreferenceFilterDao;

public class BuyerSourcePreferenceFilter {

	public static void main(String[] args) {
		Buyer buyer = new Buyer();
		buyer.setBuyer_id(10);
		TrafficSource trafficSource = new TrafficSource();
		trafficSource.setId(5);

	}

	public static boolean evaluateOp(Buyer buyer, TrafficSource trafficSource) {

		List<BuyerSourcePreferenceFilter> buyerSourcePreferenceFilters = BuyerSourcePreferenceFilterDao
				.getFilters();

		if ((buyerSourcePreferenceFilters == null) || (buyer == null)
				|| (trafficSource == null))
			return true;

		boolean wantsFromBuyer = getWantsFromBuyerIds(
				buyerSourcePreferenceFilters, buyer.getBuyer_id(),
				trafficSource.getId());
		boolean dontwantsFromBuyer = getDontWantsFromBuyerIds(
				buyerSourcePreferenceFilters, buyer.getBuyer_id(),
				trafficSource.getId());

		/*
		 * for (BuyerSourcePreferenceFilter buyerSourcePreferenceFilter :
		 * buyerSourcePreferenceFilters) {
		 * 
		 * if((buyerSourcePreferenceFilter.getBuyer_id() == buyer.getBuyer_id())
		 * && (buyerSourcePreferenceFilter.getIs_active() ==1)) { {
		 * if("Eq".equalsIgnoreCase(buyerSourcePreferenceFilter.getOperator()))
		 * if(buyerSourcePreferenceFilter.getSource_id() !=
		 * trafficSource.getId()) { return false; }
		 * if("NotEq".equalsIgnoreCase(buyerSourcePreferenceFilter
		 * .getOperator())) if(buyerSourcePreferenceFilter.getSource_id() ==
		 * trafficSource.getId()) { return false; } } } }
		 */

		return (wantsFromBuyer && dontwantsFromBuyer);

	}

	private static boolean getDontWantsFromBuyerIds(
			List<BuyerSourcePreferenceFilter> buyerSourcePreferenceFilters,
			int buyer_id, int traffic_source_id) {

		for (BuyerSourcePreferenceFilter buyerSourcePreferenceFilter : buyerSourcePreferenceFilters) {

			if (buyerSourcePreferenceFilter.getBuyer_id() == buyer_id
					&& buyerSourcePreferenceFilter.getSource_id() == traffic_source_id
					&& "NotEq".equalsIgnoreCase(buyerSourcePreferenceFilter
							.getOperator()))
				return false;

		}
		return true;
	}

	private static boolean getWantsFromBuyerIds(
			List<BuyerSourcePreferenceFilter> buyerSourcePreferenceFilters,
			int buyer_id, int traffic_source_id) {

		boolean isEqFilterWithBuyer = false;

		for (BuyerSourcePreferenceFilter buyerSourcePreferenceFilter : buyerSourcePreferenceFilters) {
			if (buyerSourcePreferenceFilter.getBuyer_id() == buyer_id
					&& "Eq".equalsIgnoreCase(buyerSourcePreferenceFilter
							.getOperator())) {
				isEqFilterWithBuyer = true;
				if (buyerSourcePreferenceFilter.getSource_id() == traffic_source_id)
					return true;

			}

		}

		if (isEqFilterWithBuyer)
			return false;
		else
			return true;
	}

	private int id;
	private int buyer_id;
	private int source_id;
	private String operator;
	private Date created_date;
	private int is_active;

	private int org_id;
	private String orgnization_name;

	public int getOrg_id() {
		return org_id;
	}

	public void setOrg_id(int org_id) {
		this.org_id = org_id;
	}

	public String getOrgnization_name() {
		return orgnization_name;
	}

	public void setOrgnization_name(String orgnization_name) {
		this.orgnization_name = orgnization_name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBuyer_id() {
		return buyer_id;
	}

	public void setBuyer_id(int buyer_id) {
		this.buyer_id = buyer_id;
	}

	public int getSource_id() {
		return source_id;
	}

	public void setSource_id(int source_id) {
		this.source_id = source_id;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	public int getIs_active() {
		return is_active;
	}

	public void setIs_active(int is_active) {
		this.is_active = is_active;
	}

}
