package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import data.scripts.util.MagicIncompatibleHullmods;
import java.util.HashMap;
import java.util.Map;


public class Smoll_ManaPool_Hullmod extends BaseHullMod 
{
    private static final Map magicHModMap = new HashMap<>();//add hull IDs as keys and their mana point cost as their value
    private static final String MPOOL = "Smoll_Mana_Pool";
    private static final String MANACRYSTALID = "equestrian_crystals";
    private static final String REMOVEDREASON = "not enough mana";
    
    static
    {
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
        String hullID = stats.getVariant().getHullSpec().getHullId();
        if (null != hullID)//this sets a ships mana pool
            switch (hullID) 
            {
                case "equestria_ugly": //change to ship ID and set points to that ships mana pool
                    points = 1;
                    break;
                
                case "equestria_gale":
                    points = 2;
                    break;
                    
                case "equestria_gust":
                    points = 1;
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
                        MagicIncompatibleHullmods.removeHullmodWithWarning(stats.getVariant(), 
                                hullMod.toString(), //mod to be removed converted from an object to a string
                                REMOVEDREASON); //reason to remove this might break if it isnt a hullmod ID
                    }
                }
            }
        }   
    }
 
    
    @Override
    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize)
    {
        if (index == 0)
            return "";//this needs to be a ships mana pool
        return null;
    }
}
