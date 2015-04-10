package org.rev317.min.api.methods;

import org.parabot.core.Context;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.input.Mouse;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.rev317.min.Loader;
import org.rev317.min.accessors.Interface;
import org.rev317.min.api.wrappers.Item;

import java.util.ArrayList;
import java.util.Properties;

/**
 * @author JKetelaar
 */
public class Trading {

    /*
    TODO Get the player its offer and implement its features
     */
    public static int OPPONENT_OFFER_INTERFACE_ID = 3416;
    public static int MY_OFFER_INTERFACE_ID = 3415;

    public static int FIRST_TRADE_INTERFACE_ID = 3323;
    public static int SECOND_TRADE_INTERFACE_ID = 3443;

    static {
        Properties p = Context.getInstance().getServerProviderInfo().getProperties();
        if (p.containsKey("opponentInterface")) {
            OPPONENT_OFFER_INTERFACE_ID = Integer.parseInt(p.getProperty("opponentInterface"));
        }
        if (p.containsKey("myInterface")) {
            OPPONENT_OFFER_INTERFACE_ID = Integer.parseInt(p.getProperty("myInterface"));
        }
        if (p.containsKey("firstTradeInterface")) {
            FIRST_TRADE_INTERFACE_ID = Integer.parseInt(p.getProperty("firstTradeInterface"));
        }
        if (p.containsKey("secondTradeInterface")) {
            SECOND_TRADE_INTERFACE_ID = Integer.parseInt(p.getProperty("secondTradeInterface"));
        }
    }

    /**
     * Checks if the first or the second screen is open, based on the given boolean
     * @param first If true, the first trade screen will be checked. If false, the second screen will be checked
     * @return True if the requested screen is open
     */
    public static boolean isOpen(boolean first){
        return Loader.getClient().getOpenInterfaceId() == (first ? FIRST_TRADE_INTERFACE_ID : SECOND_TRADE_INTERFACE_ID);
    }

    /**
     * Checks if any of the trade screens are open
     * @return True if open, false if not open
     */
    public static boolean isOpen(){
        return Loader.getClient().getOpenInterfaceId() == FIRST_TRADE_INTERFACE_ID || Loader.getClient().getOpenInterfaceId() == SECOND_TRADE_INTERFACE_ID;
    }

    /**
     * Still in development
     *      For now it simply walks away from the trade by walking to the location of the player
     */
    public static void close(){
        Players.getMyPlayer().getLocation().walkTo();
        Time.sleep(new SleepCondition() {
            @Override
            public boolean isValid() {
                return !isOpen();
            }
        }, 2500);
    }

    public static Item[] getMyOffer() {
        ArrayList<Item> items = new ArrayList<>();
        int[] ids = getItemIDs(MY_OFFER_INTERFACE_ID);
        int[] stacks = getItemStacks(MY_OFFER_INTERFACE_ID);
        for (int i = 0; i < ids.length; i++) {
            if (ids[i] > 0) {
                items.add(new Item(ids[i], stacks[i], i));
            }
        }
        return items.toArray(new Item[items.size()]);
    }

    public static Item[] getOpponentsOffer() {
        ArrayList<Item> items = new ArrayList<>();
        int[] ids = getItemIDs(OPPONENT_OFFER_INTERFACE_ID);
        int[] stacks = getItemStacks(OPPONENT_OFFER_INTERFACE_ID);
        for (int i = 0; i < ids.length; i++) {
            if (ids[i] > 0) {
                items.add(new Item(ids[i], stacks[i], i));
            }
        }
        return items.toArray(new Item[items.size()]);
    }

    private static int[] getItemIDs(int interfaceID) {
        Interface i;
        if ((i = Loader.getClient().getInterfaceCache()[interfaceID]) != null) {
            int[] items;
            if ((items = i.getItems()) != null && items.length > 0) {
                return items;
            }
        }
        return new int[0];
    }

    private static int[] getItemStacks(int interfaceID) {
        Interface i;

        if ((i = Loader.getClient().getInterfaceCache()[interfaceID]) != null) {
            int[] stacks;
            if ((stacks = i.getStackSizes()) != null && stacks.length > 0) {
                return stacks;
            }
        }
        return new int[0];
    }

    /**
     * TODO Figure a way to use packets instead
     * Accepts the offer and hits the button to continue to the second screen
     */
    private static void acceptOffer() {
        Time.sleep(500, 750);
        Mouse.getInstance().click(260, 190, true);
        Time.sleep(500, 750);
    }

    /**
     * TODO Figure a way to use packets instead
     * Accepts the trade and hits the button to complete the trade
     */
    private static void acceptTrade() {
        Time.sleep(500, 750);
        Mouse.getInstance().click(230, 310, true);
        Time.sleep(500, 750);
    }
}