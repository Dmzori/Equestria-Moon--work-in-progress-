package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import data.scripts.util.MagicIncompatibleHullmods;
import java.util.HashMap;
import java.util.Map;


public class ManaPool extends BaseHullMod 
{
    private static final Map hullSizeMap = new HashMap<>();
    private static final Map magicHModMap = new HashMap<>();//add hull IDs as keys and their mana point cost as their value
    private static final String MPOOL = "manapool";
    private static final String MANA_CRYSTAL_ID = "equestrian_crystals";
    
    static
    {
        hullSizeMap.put(ShipAPI.HullSize.FIGHTER, Float.valueOf(0.0F));
        hullSizeMap.put(ShipAPI.HullSize.FRIGATE, Float.valueOf(1.0F));
        hullSizeMap.put(ShipAPI.HullSize.DESTROYER, Float.valueOf(2.0F));
        hullSizeMap.put(ShipAPI.HullSize.CRUISER, Float.valueOf(3.0F));
        hullSizeMap.put(ShipAPI.HullSize.CAPITAL_SHIP, Float.valueOf(4.0F));
        magicHModMap.put("Pon_Spell_1", Integer.valueOf(1));//try the Float.value of and or just putting the naked object meaning 1
        magicHModMap.put("Pon_Spell_2", Integer.valueOf(1));
        magicHModMap.put("Pon_Spell_3", Integer.valueOf(1));
    }   
    
    
    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id)
    {
        //float manaCrystals = Global.getSector().getPlayerFleet().getCargo().getCommodityQuantity(MANA_CRYSTAL_ID); gets the amount of crystals your fleet owns
        int points = 0;
        Object[] hullMods = stats.getVariant().getHullMods().toArray();//gets a collection of hull IDs and changes them to an array
        //String hullID = stats.getVariant().getHullSpec().getHullId();
        if (null != hullSize)//can change to hullID
            switch (hullSize) //change cases to hullID
            {
                case FRIGATE:
                    points = 1;
                    break;
                case DESTROYER:
                    points = 2;
                    break;
                case CRUISER:
                    points = 3;
                    break;
                case CAPITAL_SHIP:
                    points = 4;
                    break;
            }
        if (hullMods != null && hullMods.length > 0)
        {
            for (Object hullMod : hullMods) 
            {//iterating through the collection of hull IDs with a for each loop
                if(magicHModMap.containsKey(hullMod))
                {
                    String hModString = magicHModMap.get(hullMod).toString();//this line breaks everything
                    int hModPointVal = Integer.parseInt(hModString);
                    if (points >= hModPointVal) //change points to mana crystals to use the amount of mana crystals as a mana pool 
                    {//if the point value of the hmod in question is less than or equal to points minus that value from points
                        points -= hModPointVal;
                    } 
                    else 
                    {
                        MagicIncompatibleHullmods.removeHullmodWithWarning(
                                stats.getVariant(), 
                                hullMod.toString(), //mod to be removed converted from an object to a string
                                MPOOL); //reason to remove
                    }
                }
            }
        }   
    }
 
    
    @Override
    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize)
    {
        if (index == 0)
            return "" + ((Float)hullSizeMap.get(ShipAPI.HullSize.FRIGATE)).intValue();
        if (index == 1)
            return "" + ((Float)hullSizeMap.get(ShipAPI.HullSize.DESTROYER)).intValue(); 
        if (index == 2)
            return "" + ((Float)hullSizeMap.get(ShipAPI.HullSize.CRUISER)).intValue(); 
        if (index == 3)
            return "" + ((Float)hullSizeMap.get(ShipAPI.HullSize.CAPITAL_SHIP)).intValue(); 
        return null;
    }
    
    
    
    
}
