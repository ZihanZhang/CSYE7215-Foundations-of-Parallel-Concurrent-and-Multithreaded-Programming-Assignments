package Zihan;

/**
 *  @author Zihan Zhang
 */


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class AuctionServer
{

	//@Invariant: every seller can sell 0 ~ maxSellerItems items
	//			  every buyer can bid 0 ~ maxBidCount items
	//    	 	  number of biddings has to be 0 ~ serverCapacity

	/**
	 * Singleton: the following code makes the server a Singleton. You should
	 * not edit the code in the following noted section.
	 * 
	 * For test purposes, we made the constructor protected. 
	 */

	/* Singleton: Begin code that you SHOULD NOT CHANGE! */
	protected AuctionServer()
	{
	}

	private static AuctionServer instance = new AuctionServer();

	public static AuctionServer getInstance()
	{
		return instance;
	}

	/* Singleton: End code that you SHOULD NOT CHANGE! */





	/* Statistic variables and server constants: Begin code you should likely leave alone. */


	/**
	 * Server statistic variables and access methods:
	 */
	private int soldItemsCount = 0;
	private int revenue = 0;

	public int soldItemsCount()
	{
		return this.soldItemsCount;
	}

	public int revenue()
	{
		return this.revenue;
	}



	/**
	 * Server restriction constants:
	 */
	public static final int maxBidCount = 10; // The maximum number of bids at any given time for a buyer.
	public static final int maxSellerItems = 20; // The maximum number of items that a seller can submit at any given time.
	public static final int serverCapacity = 80; // The maximum number of active items at a given time.


	/* Statistic variables and server constants: End code you should likely leave alone. */



	/**
	 * Some variables we think will be of potential use as you implement the server...
	 */

	// List of items currently up for bidding (will eventually remove things that have expired).
	private List<Item> itemsUpForBidding = new ArrayList<Item>();


	// The last value used as a listing ID.  We'll assume the first thing added gets a listing ID of 0.
	private int lastListingID = -1; 

	// List of item IDs and actual items.  This is a running list with everything ever added to the auction.
	private HashMap<Integer, Item> itemsAndIDs = new HashMap<Integer, Item>();

	// List of itemIDs and the highest bid for each item.  This is a running list with everything ever added to the auction.
	private HashMap<Integer, Integer> highestBids = new HashMap<Integer, Integer>();

	// List of itemIDs and the person who made the highest bid for each item.   This is a running list with everything ever bid upon.
	private HashMap<Integer, String> highestBidders = new HashMap<Integer, String>(); 




	// List of sellers and how many items they have currently up for bidding.
	private HashMap<String, Integer> itemsPerSeller = new HashMap<String, Integer>();

	// List of buyers and how many items on which they are currently bidding.
	private HashMap<String, Integer> itemsPerBuyer = new HashMap<String, Integer>();

	//seller list
	private List<Seller> sellers = new ArrayList<>();

	//bidder list
	private List<Bidder> bidders = new ArrayList<>();

	//qualifiedSeller list
	private HashMap<String, Integer> qualifiedSeller = new HashMap<>();

	//unbid seller
	private HashMap<String, Integer> nonBidSeller = new HashMap<>();



	// Object used for instance synchronization if you need to do it at some point 
	// since as a good practice we don't use synchronized (this) if we are doing internal
	// synchronization.
	//
	// private Object instanceLock = new Object(); 



	
	
	



	/*
	 *  The code from this point forward can and should be changed to correctly and safely 
	 *  implement the methods as needed to create a working multi-threaded server for the 
	 *  system.  If you need to add Object instances here to use for locking, place a comment
	 *  with them saying what they represent.  Note that if they just represent one structure
	 *  then you should probably be using that structure's intrinsic lock.
	 */

	private Object sellerlock = new Object();
	private Object itemlock = new Object();
	private Object buyerlock = new Object();
	private Object idlock = new Object();
	private Object qualock = new Object();


	/**
	 * Attempt to submit an <code>Item</code> to the auction
	 * @param sellerName Name of the <code>Seller</code>
	 * @param itemName Name of the <code>Item</code>
	 * @param lowestBiddingPrice Opening price
	 * @param biddingDurationMs Bidding duration in milliseconds
	 * @return A positive, unique listing ID if the <code>Item</code> listed successfully, otherwise -1
	 */

	//Precondition: None
	//Postcondition:A positive, unique listing ID if the Item listed successfully, otherwise -1
	//Exception: None
	public int submitItem(String sellerName, String itemName, int lowestBiddingPrice, int biddingDurationMs)
	{
//		*(itemlock)*
//		IF itemsUpForBidding < serverCapacity THEN
//			*(sellerlock)*
//			IF sellerName doesnt exist in sellers THEN
//				add seller to sellers and add item to the seller
//				set itemsPerSeller +1
//				lastListingID = lastListingID + 1
//				set itemsUpForBidding
//				set itemsAndIDs
//				set qualifiedSeller
//				return lastListingID
//			ELSE
//				IF seller in itemsPerSeller < maxSellerItems THEN
//					IF sellerName in qualifiedSeller < 3
//						check items in itemsPerSeller
//						set nonBidSeller
//						IF sellerName in nonBidSeller < 5
//							add item to the seller
//							set itemsPerSeller +1
//							lastListingID = lastListingID + 1
//							set itemsUpForBidding
//							set itemsAndIDs
//							set qualifiedSeller
//							return lastListingID
//						ELSE
//							return -1;
//						ENDIF
//					ELSE
//						return -1;
//					ENDIF
//				ELSE
//					return -1;
//				ENDIF
//			ENDIF
//		ELSE
//			return -1;
//		ENDIF

		int itemsnum;
		synchronized (itemlock) {
			itemsnum = itemsUpForBidding.size();
		}

		int quanum;
		synchronized (qualock) {
		    quanum = qualifiedSeller.get(sellerName);
        }
		if (itemsnum < serverCapacity && quanum != Integer.MIN_VALUE) {
		    synchronized (qualock) {
                if (!itemsPerSeller.containsKey(sellerName)) {
                    itemsPerSeller.put(sellerName, 0);
                }

                if (lowestBiddingPrice < 75) {
                    if (qualifiedSeller.get(sellerName) >= 2) {
                        qualifiedSeller.put(sellerName, Integer.MIN_VALUE);
                        return -1;
                    }
                    else {
                        qualifiedSeller.put(sellerName, qualifiedSeller.get(sellerName) + 1);
                    }
                }
                else {
                    qualifiedSeller.put(sellerName, 0);
                }
            }


			int thisid;
			synchronized (idlock) {
				thisid = lastListingID + 1;
			}

			Item newitem = new Item(sellerName, itemName, thisid, lowestBiddingPrice, biddingDurationMs);

			synchronized (itemlock) {
                itemsUpForBidding.add(thisid, newitem);
                itemsAndIDs.put(thisid, newitem);
                itemsPerSeller.put(sellerName, itemsPerSeller.get(sellerName) + 1);
            }

			return thisid;
		}

			
		// TODO: IMPLEMENT CODE HERE
		// Some reminders:
		//   Make sure there's room in the auction site.
		//   If the seller is a new one, add them to the list of sellers.
		//   If the seller has too many items up for bidding, don't let them add this one.
		//   Don't forget to increment the number of things the seller has currently listed.

		return -1;
	}



	/**
	 * Get all <code>Items</code> active in the auction
	 * @return A copy of the <code>List</code> of <code>Items</code>
	 */

	//Precondition:  None
	//Postcondition:  Current items returned
	//Exception:  None
	public List<Item> getItems()
	{
//		newitemsUpForBidding = copy itemsUpForBidding   //in case of illegal operation on it
//		return newitemsUpForBidding

        List<Item> items;
        synchronized (itemlock) {
            items = new ArrayList<Item>();
            for (Item it : itemsUpForBidding) {
                items.add(it);
            }
        }


		// TODO: IMPLEMENT CODE HERE
		// Some reminders:
		//    Don't forget that whatever you return is now outside of your control.
		
		return items;
	}


	/**
	 * Attempt to submit a bid for an <code>Item</code>
	 * @param bidderName Name of the <code>Bidder</code>
	 * @param listingID Unique ID of the <code>Item</code>
	 * @param biddingAmount Total amount to bid
	 * @return True if successfully bid, false otherwise
	 */

	//Precondition: None
	//Postcondition: if bid submitted successfully, return true. Else return false
	//Exception: None
	public boolean submitBid(String bidderName, int listingID, int biddingAmount)
	{
//		*(buyerlock)*
//		IF checkBidStatus = 2 THEN
//			IF itemsPerBuyer < maxBidCount THEN
//				IF bidderName != highestBidders THEN
//					*(sellerlock)*
//					IF biddingAmount > lowestBiddingPrice THEN
//						set highestBidders
//						set highestBids
//						set itemsPerBuyer
//						return true;
//					ELSE
//						return false;
//					ENDIF
//				ELSE
//					return false;
//				ENDIF
//			ELSE
//				return false;
//			ENDIF
//		ELSE
//			return false;
//		ENDIF
        synchronized (buyerlock) {
            if (checkBidStatus(bidderName, listingID) == 2) {
                if (itemsPerBuyer.get(bidderName) < maxBidCount) {
                    if (!bidderName.equals(highestBidders.get(listingID))) {
                        if (biddingAmount > itemPrice(listingID)) {
                            highestBidders.put(listingID, bidderName);
                            highestBids.put(listingID, biddingAmount);
                            itemsPerBuyer.put(bidderName, itemsPerBuyer.get(bidderName) + 1);
                            return true;
                        }
                    }
                }
            }
        }



		// TODO: IMPLEMENT CODE HERE
		// Some reminders:
		//   See if the item exists.
		//   See if it can be bid upon.
		//   See if this bidder has too many items in their bidding list.
		//   Get current bidding info.
		//   See if they already hold the highest bid.
		//   See if the new bid isn't better than the existing/opening bid floor.
		//   Decrement the former winning bidder's count
		//   Put your bid in place
		
		return false;
	}

	/**
	 * Check the status of a <code>Bidder</code>'s bid on an <code>Item</code>
	 * @param bidderName Name of <code>Bidder</code>
	 * @param listingID Unique ID of the <code>Item</code>
	 * @return 1 (success) if bid is over and this <code>Bidder</code> has won<br>
	 * 2 (open) if this <code>Item</code> is still up for auction<br>
	 * 3 (failed) If this <code>Bidder</code> did not win or the <code>Item</code> does not exist
	 */

	//Precondition: None
	//Postcondition: 1(success) if bid is over and this bidder has won
	//				 2 (open) if this Item is still up for auction
	//				 3 (failed) If this Bidder did not win or the Item does not exist
	//Exception: None
	public int checkBidStatus(String bidderName, int listingID)
	{
//		IF biddingopen THEN
//			return 2
//		ELSE
//			IF listingID not in itemsAndIDs THEN
//				return 3
//			ELSE
//				IF highestBidders != bidderName THEN
//					set itemsUpForBidding
//					set itemsAndIDs
//					set itemsPerBuyer
//					set itemsPerSeller
//					return 3
//				ELSE
//					set itemsUpForBidding
//					set itemsAndIDs
//					set itemsPerBuyer
//					set itemsPerSeller
//					set soldItemsCount
//					set revenue
//					return 1
//				ENDIF
//			ENDIF
//		ENDIF

        List<Item> items = new ArrayList<Item>();

        synchronized (itemlock) {
            for (int i = 0; i < itemsUpForBidding.size(); i++) {
                items.add(i, itemsUpForBidding.get(i));
            }
        }


        if (items.contains(bidderName)) {
            if (items.get(listingID).biddingOpen()) {
                return 2;
            }
            else if (highestBidders.get(listingID).equals(bidderName)){
                synchronized (itemlock) {
                    Item item = itemsUpForBidding.remove(listingID);
                    itemsPerBuyer.put(bidderName, itemsPerBuyer.get(bidderName) - 1);
                    itemsPerSeller.put(bidderName, itemsPerSeller.get(bidderName) - 1);
                    soldItemsCount++;
                    revenue = revenue + itemPrice(listingID);
                    return 1;
                }
            }
            else {
                return 3;
            }
        }
        else {
            return 3;
        }

		// TODO: IMPLEMENT CODE HERE
		// Some reminders:
		//   If the bidding is closed, clean up for that item.
		//     Remove item from the list of things up for bidding.
		//     Decrease the count of items being bid on by the winning bidder if there was any...
		//     Update the number of open bids for this seller
		
//		return -1;
	}

	/**
	 * Check the current bid for an <code>Item</code>
	 * @param listingID Unique ID of the <code>Item</code>
	 * @return The highest bid so far or the opening price if no bid has been made,
	 * -1 if no <code>Item</code> exists
	 */

	//Precondition: None
	//Postcondition: The highest bid so far or the opening price if no bid has been made
	//Exception: None
	public int itemPrice(int listingID)
	{
//		IF highestBids > lowestBiddingPrice THEN
//			return highestBids
//		ELSE
//			return lowestBiddingPrice
//		ENDIF

        synchronized (itemlock) {
            Item item = itemsAndIDs.get(listingID);
            if (!itemUnbid(listingID)) {
                return highestBids.get(listingID);
            }
            else {
                return item.lowestBiddingPrice();
            }
        }

		// TODO: IMPLEMENT CODE HERE
		
//		return -1;
	}

	/**
	 * Check whether an <code>Item</code> has been bid upon yet
	 * @param listingID Unique ID of the <code>Item</code>
	 * @return True if there is no bid or the <code>Item</code> does not exist, false otherwise
	 */

	//Precondition: None
	//Postcondition: True if there is no bid or the Item does not exist, false otherwise
	//Exception: None
	public Boolean itemUnbid(int listingID)
	{
//		IF listingID in highestBids THEN
//			return false
//		ELSE
//			return true
//		ENDIF
        synchronized (itemlock) {
            if (!highestBids.containsKey(listingID)) {
                return true;
            }
        }

		// TODO: IMPLEMENT CODE HERE
		
		return false;
	}


}
 