package com.triyasoft.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.triyasoft.model.DashboardDataModel.BuyersSummary.BuyerSummary;
import com.triyasoft.model.DashboardDataModel.TrafficSourcesSummary.TrafficSourceSummary;

public class DashboardDataModel {

	private Date lastUpdatedat = new Date();

	private OnGoingCalls onGoingCalls = new OnGoingCalls();

	private BuyerConversion buyerConversion = new BuyerConversion();

	private BuyerRevenue buyerRevenue = new BuyerRevenue();

	private PhoneCost phoneCost = new PhoneCost();

	private Profit profit = new Profit();

	private TrafficSourcesSummary trafficSourcesSummary = new TrafficSourcesSummary();

	private BuyersSummary buyersSummary = new BuyersSummary();

	public static void main(String[] args) {
		DashboardDataModel dashboardDataModel = new DashboardDataModel();
		dashboardDataModel.printAllValues();
	}

	public void printAllValues() {

		System.out.println("Last updated at: " + this.getLastUpdatedat());
		this.getOnGoingCalls().printValues();
		this.getBuyerConversion().printValues();
	}

	public class BuyersSummary {

		public List<BuyerSummary> buyersSummary = new ArrayList<BuyerSummary>();

		public BuyerSummary callingBuyer = new BuyerSummary();

		public BuyerSummary noBuyer = new BuyerSummary();

		private int totalBuyers = 0;

		private int totalCallsToBuyersYday = 0;

		private int totalCallsToBuyersToday = 0;

		private int currentLiveCallsToBuyers = 0;

		private String avgHandlingTime = "";

		private double totalBuyerRevenue = 0.0;

		private int overALlbuyersCurrentCap = 0;

		private int overALlbuyerstotalCap = 0;

		public BuyerSummary getBuyerSummaryInstance() {

			BuyerSummary buyerSummary = new BuyerSummary();
			this.getBuyersSummary().add(buyerSummary);

			return buyerSummary;
		}

		public class BuyerSummary implements Comparable<BuyerSummary> {

			private String buyerName = "";
			private int ydayCallstoBuyer = 0;
			private int todayCallstoBuyer = 0;
			private int liveCallsToBuyer = 0;
			private int buyerConcurrencyCap = 0;
			private int buyerTier = 1000;
			private double avgCalltime = 0.0;
			private int isBuyer_active = 0;
			private int buyer_weitage;

			public int getBuyer_weitage() {
				return buyer_weitage;
			}

			public void setBuyer_weitage(int buyer_weitage) {
				this.buyer_weitage = buyer_weitage;
			}

			public int getIsBuyer_active() {
				return isBuyer_active;
			}

			public void setIsBuyer_active(int isBuyer_active) {
				this.isBuyer_active = isBuyer_active;
			}

			private double todaysCalltimeTobuyer = 0.0;

			private double buyerRevenue = 0.0;

			private int buyerCurrentCap = 0;

			private int buyerDailyCap = 0;
			private String closeTiming = "";
			private int buyerId;

			public String getBuyerName() {
				return buyerName;
			}

			public void setBuyerName(String buyerName) {
				this.buyerName = buyerName;
			}

			public int getYdayCallstoBuyer() {
				return ydayCallstoBuyer;
			}

			public void setYdayCallstoBuyer(int ydayCallstoBuyer) {
				this.ydayCallstoBuyer = ydayCallstoBuyer;
			}

			public int getTodayCallstoBuyer() {
				return todayCallstoBuyer;
			}

			public void setTodayCallstoBuyer(int todayCallstoBuyer) {
				this.todayCallstoBuyer = todayCallstoBuyer;
			}

			public int getLiveCallsToBuyer() {
				return liveCallsToBuyer;
			}

			public void setLiveCallsToBuyer(int liveCallsToBuyer) {
				this.liveCallsToBuyer = liveCallsToBuyer;
			}

			public int getBuyerConcurrencyCap() {
				return buyerConcurrencyCap;
			}

			public void setBuyerConcurrencyCap(int buyerConcurrencyCap) {
				this.buyerConcurrencyCap = buyerConcurrencyCap;
			}

			public int getBuyerTier() {
				return buyerTier;
			}

			public void setBuyerTier(int buyerTier) {
				this.buyerTier = buyerTier;
			}

			public double getAvgCalltime() {
				return avgCalltime;
			}

			public void setAvgCalltime(double avgCalltime) {
				this.avgCalltime = avgCalltime;
			}

			public double getBuyerRevenue() {
				return buyerRevenue;
			}

			public void setBuyerRevenue(double buyerRevenue) {
				this.buyerRevenue = buyerRevenue;
			}

			public int getBuyerCurrentCap() {
				return buyerCurrentCap;
			}

			public void setBuyerCurrentCap(int buyerCurrentCap) {
				this.buyerCurrentCap = buyerCurrentCap;
			}

			public int getBuyerDailyCap() {
				return buyerDailyCap;
			}

			public void setBuyerDailyCap(int buyerDailyCap) {
				this.buyerDailyCap = buyerDailyCap;
			}

			public String getCloseTiming() {
				return closeTiming;
			}

			public void setCloseTiming(String closeTiming) {
				this.closeTiming = closeTiming;
			}

			public int getBuyerId() {

				return buyerId;
			}

			public void setBuyerId(int buyerId) {
				this.buyerId = buyerId;
			}

			public void incrementTodaysCallToBuyer() {
				this.todayCallstoBuyer++;

			}

			public void incrementLiveCalls() {

				this.liveCallsToBuyer++;

			}

			public double getTodaysCalltimeTobuyer() {
				return todaysCalltimeTobuyer;
			}

			public void setTodaysCalltimeTobuyer(double todaysCalltimeTobuyer) {
				this.todaysCalltimeTobuyer = todaysCalltimeTobuyer;
			}

			public void addToBuyersMinute(double duration) {
				this.todaysCalltimeTobuyer = this.todaysCalltimeTobuyer
						+ duration;

			}

			public void addToByerRevenue(double buyer_revenue) {

				this.buyerRevenue = this.buyerRevenue + buyer_revenue;

			}

			public void incrementYDayCallToBuyer() {
				this.ydayCallstoBuyer++;

			}

			public void a() {

			}

			@Override
			public int compareTo(BuyerSummary b) {

				/*
				 * if(this.isBuyer_active == 0) return 1;
				 * if(b.getIsBuyer_active() == 0) return -1;
				 * 
				 * if(b.getTodayCallstoBuyer() == this.getTodayCallstoBuyer())
				 * return b.getYdayCallstoBuyer() - this.getYdayCallstoBuyer();
				 * else return
				 * b.getTodayCallstoBuyer()-this.getTodayCallstoBuyer() ;
				 */

				if (this.isBuyer_active == 1 && b.getIsBuyer_active() == 1) {

					if (b.getTodayCallstoBuyer() == this.getTodayCallstoBuyer())
						return b.getYdayCallstoBuyer()
								- this.getYdayCallstoBuyer();
					else
						return b.getTodayCallstoBuyer()
								- this.getTodayCallstoBuyer();
				}

				if (this.isBuyer_active == 1 && b.getIsBuyer_active() == 0) {
					return -1;
				}

				if (this.isBuyer_active == 0 && b.getIsBuyer_active() == 1) {
					return 1;
				}

				if (this.isBuyer_active == 0 && b.getIsBuyer_active() == 0) {

					{
						if (b.getTodayCallstoBuyer() == this
								.getTodayCallstoBuyer())
							return b.getYdayCallstoBuyer()
									- this.getYdayCallstoBuyer();
						else
							return b.getTodayCallstoBuyer()
									- this.getTodayCallstoBuyer();
					}

				}
				return 0;

			}

		}

		public List<BuyerSummary> getBuyersSummary() {
			return buyersSummary;
		}

		public void setBuyerSummary(List<BuyerSummary> buyersSummary) {
			this.buyersSummary = buyersSummary;
		}

		public BuyerSummary getCallingBuyer() {
			return callingBuyer;
		}

		public void setCallingBuyer(BuyerSummary callingBuyer) {
			this.callingBuyer = callingBuyer;
		}

		public BuyerSummary getNoBuyer() {
			return noBuyer;
		}

		public void setNoBuyer(BuyerSummary noBuyer) {
			this.noBuyer = noBuyer;
		}

		public int getTotalBuyers() {
			return totalBuyers;
		}

		public void setTotalBuyers(int totalBuyers) {
			this.totalBuyers = totalBuyers;
		}

		public int getTotalCallsToBuyersYday() {
			return totalCallsToBuyersYday;
		}

		public void setTotalCallsToBuyersYday(int totalCallsToBuyersYday) {
			this.totalCallsToBuyersYday = totalCallsToBuyersYday;
		}

		public int getTotalCallsToBuyersToday() {
			return totalCallsToBuyersToday;
		}

		public void setTotalCallsToBuyersToday(int totalCallsToBuyersToday) {
			this.totalCallsToBuyersToday = totalCallsToBuyersToday;
		}

		public int getCurrentLiveCallsToBuyers() {
			return currentLiveCallsToBuyers;
		}

		public void setCurrentLiveCallsToBuyers(int currentLiveCallsToBuyers) {
			this.currentLiveCallsToBuyers = currentLiveCallsToBuyers;
		}

		public String getAvgHandlingTime() {
			return avgHandlingTime;
		}

		public void setAvgHandlingTime(String avgHandlingTime) {
			this.avgHandlingTime = avgHandlingTime;
		}

		public double getTotalBuyerRevenue() {
			return totalBuyerRevenue;
		}

		public void setTotalBuyerRevenue(double totalBuyerRevenue) {
			this.totalBuyerRevenue = totalBuyerRevenue;
		}

		public int getOverALlbuyersCurrentCap() {
			return overALlbuyersCurrentCap;
		}

		public void setOverALlbuyersCurrentCap(int overALlbuyersCurrentCap) {
			this.overALlbuyersCurrentCap = overALlbuyersCurrentCap;
		}

		public int getOverALlbuyerstotalCap() {
			return overALlbuyerstotalCap;
		}

		public void setOverALlbuyerstotalCap(int overALlbuyerstotalCap) {
			this.overALlbuyerstotalCap = overALlbuyerstotalCap;
		}

		public BuyerSummary getBuyer(int buyerId) {

			for (BuyerSummary buyerSummary : buyersSummary) {
				if (buyerSummary.getBuyerId() == buyerId)
					return buyerSummary;
			}

			return null;

		}

	}

	public class TrafficSourcesSummary {

		private List<TrafficSourceSummary> sourceSummaries = new ArrayList<TrafficSourceSummary>();
		private int sourceCount = 0;
		private int totalCallCountYday = 0;
		private int totalCallCountToday = 0;
		private int totalLiveCalls = 0;
		private double overAllavg = 0.0;
		private double totalPayout = 0.0;

		public TrafficSourceSummary getNewTrafficSourceSummaryInstance() {

			TrafficSourceSummary trafficSourceSummary = new TrafficSourceSummary();
			this.getSourceSummaries().add(trafficSourceSummary);

			return trafficSourceSummary;
		}

		public class TrafficSourceSummary implements
				Comparable<TrafficSourceSummary> {

			int trafficSourceId = 0;
			private String trafficSourceName = "";
			private int ydayCalls = 0;
			private int todayCalls = 0;
			private int liveCalls = 0;
			private double avgCall = 0.0;
			private double sourcePayout = 0.0;

			private double totalCallTime = 0.0;

			public double getTotalCallTime() {
				return totalCallTime;
			}

			public void setTotalCallTime(double totalCallTime) {
				this.totalCallTime = totalCallTime;
			}

			public String getTrafficSourceName() {
				return trafficSourceName;
			}

			public void setTrafficSourceName(String trafficSourceName) {
				this.trafficSourceName = trafficSourceName;
			}

			public int getYdayCalls() {
				return ydayCalls;
			}

			public void setYdayCalls(int ydayCalls) {
				this.ydayCalls = ydayCalls;
			}

			public int getTodayCalls() {
				return todayCalls;
			}

			public void setTodayCalls(int todayCalls) {
				this.todayCalls = todayCalls;
			}

			public int getLiveCalls() {
				return liveCalls;
			}

			public void setLiveCalls(int liveCalls) {
				this.liveCalls = liveCalls;
			}

			public double getAvgCall() {
				return avgCall;
			}

			public void setAvgCall(double avgCall) {
				this.avgCall = avgCall;
			}

			public double getSourcePayout() {
				return sourcePayout;
			}

			public void setSourcePayout(double sourcePayout) {
				this.sourcePayout = sourcePayout;
			}

			public int getTrafficSourceId() {
				return trafficSourceId;
			}

			public void setTrafficSourceId(int trafficSourceId) {
				this.trafficSourceId = trafficSourceId;
			}

			public void incermentTodayCallsCount() {

				this.todayCalls++;

			}

			public void addTotalCallTime(double callDuation) {

				this.totalCallTime = this.totalCallTime + callDuation;

			}

			public void addToCallRevenue(double traffic_source_revenue) {

				this.sourcePayout = this.sourcePayout + traffic_source_revenue;

			}

			public void incermentYdaytCallsCount() {

				this.ydayCalls++;

			}

			public void incrementLiveCall() {
				this.liveCalls++;

			}

			@Override
			public int compareTo(TrafficSourceSummary t) {
				if (t.getTodayCalls() == this.getTodayCalls())
					return t.getYdayCalls() - this.getYdayCalls();
				else
					return t.getTodayCalls() - this.getTodayCalls();
			}

		}

		public List<TrafficSourceSummary> getSourceSummaries() {
			return sourceSummaries;
		}

		public void setSourceSummaries(
				List<TrafficSourceSummary> sourceSummaries) {
			this.sourceSummaries = sourceSummaries;
		}

		public int getSourceCount() {
			return sourceCount;
		}

		public void setSourceCount(int sourceCount) {
			this.sourceCount = sourceCount;
		}

		public int getTotalCallCountYday() {
			return totalCallCountYday;
		}

		public void setTotalCallCountYday(int totalCallCountYday) {
			this.totalCallCountYday = totalCallCountYday;
		}

		public int getTotalCallCountToday() {
			return totalCallCountToday;
		}

		public void setTotalCallCountToday(int totalCallCountToday) {
			this.totalCallCountToday = totalCallCountToday;
		}

		public int getTotalLiveCalls() {
			return totalLiveCalls;
		}

		public void setTotalLiveCalls(int totalLiveCalls) {
			this.totalLiveCalls = totalLiveCalls;
		}

		public double getOverAllavg() {
			return overAllavg;
		}

		public void setOverAllavg(double overAllavg) {
			this.overAllavg = overAllavg;
		}

		public double getTotalPayout() {
			return totalPayout;
		}

		public void setTotalPayout(double totalPayout) {
			this.totalPayout = totalPayout;
		}

		public TrafficSourceSummary getTrafficSourceSummary(int trafficSourceId) {

			for (TrafficSourceSummary trafficSourceSummary : sourceSummaries) {
				if (trafficSourceSummary.getTrafficSourceId() == trafficSourceId)
					return trafficSourceSummary;
			}

			return null;
		}

	}

	public class Profit {

		private double profitToday = 0.0;
		private double profitYday = 0.0;
		private double profit4Hrs = 0.0;
		private double profit1Hrs = 0.0;

		public double getProfitToday() {
			return profitToday;
		}

		public void setProfitToday(double profitToday) {
			this.profitToday = profitToday;
		}

		public double getProfitYday() {
			return profitYday;
		}

		public void setProfitYday(double profitYday) {
			this.profitYday = profitYday;
		}

		public double getProfit4Hrs() {
			return profit4Hrs;
		}

		public void setProfit4Hrs(double profit4Hrs) {
			this.profit4Hrs = profit4Hrs;
		}

		public double getProfit1Hrs() {
			return profit1Hrs;
		}

		public void setProfit1Hrs(double profit1Hrs) {
			this.profit1Hrs = profit1Hrs;
		}

	}

	public class PhoneCost {

		private double phoneCostToday = 0.0;
		private double phoneCostYday = 0.0;
		private double phoneCost4Hrs = 0.0;
		private double phoneCost1Hrs = 0.0;

		public double getPhoneCostToday() {
			return phoneCostToday;
		}

		public void setPhoneCostToday(double phoneCostToday) {
			this.phoneCostToday = phoneCostToday;
		}

		public double getPhoneCostYday() {
			return phoneCostYday;
		}

		public void setPhoneCostYday(double phoneCostYday) {
			this.phoneCostYday = phoneCostYday;
		}

		public double getPhoneCost4Hrs() {
			return phoneCost4Hrs;
		}

		public void setPhoneCost4Hrs(double phoneCost4Hrs) {
			this.phoneCost4Hrs = phoneCost4Hrs;
		}

		public double getPhoneCost1Hrs() {
			return phoneCost1Hrs;
		}

		public void setPhoneCost1Hrs(double phoneCost1Hrs) {
			this.phoneCost1Hrs = phoneCost1Hrs;
		}

	}

	public class BuyerRevenue {

		private double buyerRevenueToday = 0.0;
		private double buyerRevenueYday = 0.0;
		private double buyerRevenue4Hrs = 0.0;
		private double buyerRevenue1Hrs = 0.0;

		public double getBuyerRevenueToday() {
			return buyerRevenueToday;
		}

		public void setBuyerRevenueToday(double buyerRevenueToday) {
			this.buyerRevenueToday = buyerRevenueToday;
		}

		public double getBuyerRevenueYday() {
			return buyerRevenueYday;
		}

		public void setBuyerRevenueYday(double buyerRevenueYday) {
			this.buyerRevenueYday = buyerRevenueYday;
		}

		public double getBuyerRevenue4Hrs() {
			return buyerRevenue4Hrs;
		}

		public void setBuyerRevenue4Hrs(double buyerRevenue4Hrs) {
			this.buyerRevenue4Hrs = buyerRevenue4Hrs;
		}

		public double getBuyerRevenue1Hrs() {
			return buyerRevenue1Hrs;
		}

		public void setBuyerRevenue1Hrs(double buyerRevenue1Hrs) {
			this.buyerRevenue1Hrs = buyerRevenue1Hrs;
		}

	}

	public class BuyerConversion {

		private int buyerConvertedToday = 0;
		private int totalCallsToday = 0;
		private double buyerConvetedPercentageToday = 0;

		private int buyerConvertedYday = 0;
		private int totaslCallsYday = 0;
		private double buyerConvertedPercentageYday = 0;

		private int buyerConverted4Hrs = 0;
		private int totaslCalls4Hrs = 0;
		private double buyerConvertedPercentage4Hrs = 0;

		private int buyerConverted1Hrs = 0;
		private int totaslCalls1Hrs = 0;
		private double buyerConvertedPercentage1Hrs = 0;

		public int getBuyerConvertedToday() {
			return buyerConvertedToday;
		}

		public void printValues() {

			System.out.println("-------------------------------");
			System.out.println("Printing Buyer Conversions");
			System.out.println(this.buyerConvetedPercentageToday + "\t"
					+ this.buyerConvertedToday + "/" + this.totalCallsToday
					+ "\t Today");
			System.out.println(this.buyerConvertedPercentageYday + "\t"
					+ this.buyerConvertedYday + "/" + this.totaslCallsYday
					+ "\t Yesterday");
			System.out.println(this.buyerConvertedPercentage4Hrs + "\t"
					+ this.buyerConverted4Hrs + "/" + this.totaslCalls4Hrs
					+ "\t 4 Hrs");
			System.out.println(this.buyerConvertedPercentage1Hrs + "\t"
					+ this.buyerConverted1Hrs + "/" + this.totaslCalls1Hrs
					+ "\t 1 Hrs");

			System.out.println("-------------------------------");

		}

		public void setBuyerConvertedToday(int buyerConvertedToday) {
			this.buyerConvertedToday = buyerConvertedToday;
		}

		public int getTotalCallsToday() {
			return totalCallsToday;
		}

		public void setTotalCallsToday(int totalCallsToday) {
			this.totalCallsToday = totalCallsToday;
		}

		public double getBuyerConvetedPercentageToday() {
			return buyerConvetedPercentageToday;
		}

		public void setBuyerConvetedPercentageToday(
				double buyerConvetedPercentageToday) {
			this.buyerConvetedPercentageToday = buyerConvetedPercentageToday;
		}

		public int getBuyerConvertedYday() {
			return buyerConvertedYday;
		}

		public void setBuyerConvertedYday(int buyerConvertedYday) {
			this.buyerConvertedYday = buyerConvertedYday;
		}

		public int getTotaslCallsYday() {
			return totaslCallsYday;
		}

		public void setTotaslCallsYday(int totaslCallsYday) {
			this.totaslCallsYday = totaslCallsYday;
		}

		public double getBuyerConvertedPercentageYday() {
			return buyerConvertedPercentageYday;
		}

		public void setBuyerConvertedPercentageYday(
				double buyerConvertedPercentageYday) {
			this.buyerConvertedPercentageYday = buyerConvertedPercentageYday;
		}

		public int getBuyerConverted4Hrs() {
			return buyerConverted4Hrs;
		}

		public void setBuyerConverted4Hrs(int buyerConverted4Hrs) {
			this.buyerConverted4Hrs = buyerConverted4Hrs;
		}

		public int getTotaslCalls4Hrs() {
			return totaslCalls4Hrs;
		}

		public void setTotaslCalls4Hrs(int totaslCalls4Hrs) {
			this.totaslCalls4Hrs = totaslCalls4Hrs;
		}

		public double getBuyerConvertedPercentage4Hrs() {
			return buyerConvertedPercentage4Hrs;
		}

		public void setBuyerConvertedPercentage4Hrs(
				double buyerConvertedPercentage4Hrs) {
			this.buyerConvertedPercentage4Hrs = buyerConvertedPercentage4Hrs;
		}

		public int getBuyerConverted1Hrs() {
			return buyerConverted1Hrs;
		}

		public void setBuyerConverted1Hrs(int buyerConverted1Hrs) {
			this.buyerConverted1Hrs = buyerConverted1Hrs;
		}

		public int getTotaslCalls1Hrs() {
			return totaslCalls1Hrs;
		}

		public void setTotaslCalls1Hrs(int totaslCalls1Hrs) {
			this.totaslCalls1Hrs = totaslCalls1Hrs;
		}

		public double getBuyerConvertedPercentage1Hrs() {
			return buyerConvertedPercentage1Hrs;
		}

		public void setBuyerConvertedPercentage1Hrs(
				double buyerConvertedPercentage1Hrs) {
			this.buyerConvertedPercentage1Hrs = buyerConvertedPercentage1Hrs;
		}

	}

	public class OnGoingCalls {

		private int ongoingCalls = 0;
		private int activeCC = 0;
		private int maxActiveCC = 0;
		private double activeCCPercentage = 0.0;

		private int pausedCC = 0;
		private int maxPausedCC = 0;
		private double pausedCCPercentage = 0.0;

		public int getOngoingCalls() {
			return ongoingCalls;
		}

		public void printValues() {

			System.out.println("-------------------------------");
			System.out.println("Printing OnGoing Calls Block");
			System.out.println(this.ongoingCalls + " Total");
			System.out.println(this.activeCCPercentage + "\t" + this.activeCC
					+ "/" + this.maxActiveCC + "\t Active CC");
			System.out.println(this.pausedCCPercentage + "\t" + this.pausedCC
					+ "/" + this.maxPausedCC + "\t Paused CC ");
			System.out.println("-------------------------------");

		}

		public void setOngoingCalls(int ongoingCalls) {
			this.ongoingCalls = ongoingCalls;
		}

		public int getActiveCC() {
			return activeCC;
		}

		public void setActiveCC(int activeCC) {
			this.activeCC = activeCC;
		}

		public int getMaxActiveCC() {
			return maxActiveCC;
		}

		public void setMaxActiveCC(int maxActiveCC) {
			this.maxActiveCC = maxActiveCC;
		}

		public double getActiveCCPercentage() {
			return activeCCPercentage;
		}

		public void setActiveCCPercentage(double activeCCPercentage) {
			this.activeCCPercentage = activeCCPercentage;
		}

		public int getPausedCC() {
			return pausedCC;
		}

		public void setPausedCC(int pausedCC) {
			this.pausedCC = pausedCC;
		}

		public int getMaxPausedCC() {
			return maxPausedCC;
		}

		public void setMaxPausedCC(int maxPausedCC) {
			this.maxPausedCC = maxPausedCC;
		}

		public double getPausedCCPercentage() {
			return pausedCCPercentage;
		}

		public void setPausedCCPercentage(double pausedCCPercentage) {
			this.pausedCCPercentage = pausedCCPercentage;
		}

	}

	public Date getLastUpdatedat() {
		return lastUpdatedat;
	}

	public void setLastUpdatedat(Date lastUpdatedat) {
		this.lastUpdatedat = lastUpdatedat;
	}

	public OnGoingCalls getOnGoingCalls() {
		return onGoingCalls;
	}

	public void setOnGoingCalls(OnGoingCalls onGoingCalls) {
		this.onGoingCalls = onGoingCalls;
	}

	public BuyerConversion getBuyerConversion() {
		return buyerConversion;
	}

	public void setBuyerConversion(BuyerConversion buyerConversion) {
		this.buyerConversion = buyerConversion;
	}

	public BuyerRevenue getBuyerRevenue() {
		return buyerRevenue;
	}

	public void setBuyerRevenue(BuyerRevenue buyerRevenue) {
		this.buyerRevenue = buyerRevenue;
	}

	public PhoneCost getPhoneCost() {
		return phoneCost;
	}

	public void setPhoneCost(PhoneCost phoneCost) {
		this.phoneCost = phoneCost;
	}

	public Profit getProfit() {
		return profit;
	}

	public void setProfit(Profit profit) {
		this.profit = profit;
	}

	public TrafficSourcesSummary getTrafficSourcesSummary() {
		return trafficSourcesSummary;
	}

	public void setTrafficSourcesSummary(
			TrafficSourcesSummary trafficSourcesSummary) {
		this.trafficSourcesSummary = trafficSourcesSummary;
	}

	public BuyersSummary getBuyersSummary() {
		return buyersSummary;
	}

	public void setBuyersSummary(BuyersSummary buyersSummary) {
		this.buyersSummary = buyersSummary;
	}

}
