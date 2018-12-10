package com.triyasoft.utils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.triyasoft.model.Buyer;

public class RoutingUtil {

	public static int[]  getSortedTier(Map<Integer, List<Buyer>> cachedbuyerListByTier) {
		
		int[] tiers = new int[cachedbuyerListByTier.size()];
		int counter = 0;
		
		
		Set set = cachedbuyerListByTier.entrySet();
		
		Iterator iterator = set.iterator();
		counter = 0;
        while(iterator.hasNext()) {
              Map.Entry me = (Map.Entry)iterator.next();
              Integer tier = (Integer)me.getKey();
              tiers[counter++] = tier;
              
        }
        
        Arrays.sort(tiers);
		
       return  tiers;
	}
	
}
