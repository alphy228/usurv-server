package mindustry.usurv.building;

import arc.util.Timer;
import java.util.HashMap;
import java.lang.Runnable;
import arc.*;
import arc.util.*;
import arc.struct.Seq;
import mindustry.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.blocks.storage.*;
import mindustry.Vars;
import mindustry.game.Rules;
import mindustry.content.*;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.gen.Call;
import mindustry.game.Team;
import mindustry.mod.*;
import mindustry.world.blocks.distribution.*;
import mindustry.net.Administration.*;
import mindustry.content.Blocks;
import mindustry.usurv.Usurv;

public class UpdateCoreItems implements Runnable {
  HashMap<Item, Integer> usableItems = new HashMap<>();
  static HashMap<Item, Integer> consumableItems = new HashMap<>();
  @Override
  public void run() {
    Groups.player.each(player-> {
      usableItems.clear();
      Team team = player.team();
      if (!(player.unit() == null)) {
        Unit unit = player.unit();
        float x = unit.x;
        float y = unit.y;
        Groups.build.each(building-> {
          if ((!(building.tile.block().group == BlockGroup.transportation)) || building.tile.block() instanceof StorageBlock) {//fuck conveyors
            float bx = building.x;
            float by = building.y;
            float dx = x - bx;
            float dy = y - by;
            float distToPlayer = (float) Math.sqrt(dx * dx + dy * dy);
            if (distToPlayer <= Vars.itemTransferRange && building.tile.block().hasItems==true && player.team()==building.team()) {
              for (Item itemType : Items.serpuloItems) {
                int itemAmount = building.items.get(itemType);
                if (usableItems.get(itemType) == null) {
                  usableItems.put(itemType, itemAmount);
                } else {
                  usableItems.put(itemType, usableItems.get(itemType) + itemAmount);
                }
              }
            }
          }
        });
        Groups.unit.each(unit2-> {
          if (!(unit2 == null)) {
            float bx = unit2.x;
            float by = unit2.y;
            float dx = x - bx;
            float dy = y - by;
            float distToPlayer = (float) Math.sqrt(dx * dx + dy * dy);
            if (distToPlayer <= Vars.itemTransferRange && (!(unit2.item() == null)) && player.team()==unit2.team()) {
              Item unit2Item = unit2.item();
              int unit2ItemAmount = unit2.stack.amount;
              if (!(unit2ItemAmount == 0)) {
                if (usableItems.get(unit2Item) == null) {
                  usableItems.put(unit2Item, unit2ItemAmount);
                } else {
                  usableItems.put(unit2Item, usableItems.get(unit2Item) + unit2ItemAmount);
                }
              }
              //fuck this garbage fix never
              //Payloadc pay = (Payloadc)unit2;
              //pay.payloads().each(pbuilding -> pbuilding instanceof BuildPayload, (BuildPayload)pbuilding -> {
              //  if (pbuilding.block().hasItems==true) {
               //   for (Item itemType : Items.serpuloItems) {
              //      int itemAmount = pbuilding.items.get(itemType);
              //      if (usableItems.get(itemType) == null) {
              //        usableItems.put(itemType, itemAmount);
              //      } else {
              //        usableItems.put(itemType, usableItems.get(itemType) + itemAmount);
              //      }
              //    }
              //  }
              //});
            }
          }
        });
      ActuallyUpdateTheItemsNonStatic(usableItems, player);
      consumableItems = usableItems;
      }
    });
  }
  public static boolean Consume(Player player,Seq<Unit> units,Unit builderunit ,Item item, Integer amount){
    try {
      if (consumableItems.containsKey(item)){
        if (!(consumableItems.get(item) == null)) {
          if (consumableItems.get(item) >= amount) {
            consumableItems.put(item, consumableItems.get(item)-amount);

            // fucking consume the items
            int amount2 = amount;

            for (Unit unit2 : Groups.unit) {
              int inRange = 0;
              for (Unit unit : units) {
                float x = unit.x;
                float y = unit.y;
                float bx = unit2.x;
                float by = unit2.y;
                float dx = x - bx;
                float dy = y - by;
                float distToUnit = (float) Math.sqrt(dx * dx + dy * dy);
                if (distToUnit < Vars.itemTransferRange) {
                  inRange = inRange+1;
                }
              }
              if ((inRange>0) && (!(unit2.item() == null)) && (unit2.item()==item) && player.team()==unit2.team()) {
                Item unit2Item = unit2.item();
                int unit2ItemAmount = unit2.stack.amount;
                if ((unit2ItemAmount > 0) && (unit2Item == item)) {
                  int amount3 = amount2-unit2ItemAmount;
                  int keepAmount=0;
                  if (amount3 < 0) {
                    keepAmount = unit2ItemAmount-amount2;
                    amount2 = 0;
                  } else {
                    keepAmount = 0;
                    amount2 = amount2-unit2ItemAmount;
                  }
                  unit2.stack.amount=keepAmount;
                }
              }
            }

            for (Building building : Groups.build) {
          if ((!(building.tile.block().group == BlockGroup.transportation)) || building.tile.block() instanceof StorageBlock) {//fuck conveyors
                int inRange = 0;
                for (Unit unit : units) {
                  float x = unit.x;
                  float y = unit.y;
                  float bx = building.x;
                  float by = building.y;
                  float dx = x - bx;
                  float dy = y - by;
                  float distToUnit = (float) Math.sqrt(dx * dx + dy * dy);
                  if (distToUnit < Vars.itemTransferRange) {
                    inRange = inRange+1;
                  }
                }
                if ((inRange>0) && building.tile.block().hasItems==true && player.team()==building.team()) {
                  for (Item itemType : Items.serpuloItems) {
                    int itemAmount = building.items.get(itemType);
                    if ((itemAmount > 0) && (itemType == item)) {
                      int amount3 = amount2-itemAmount;
                      int keepAmount=0;
                      if (amount3 < 0) {
                        keepAmount = itemAmount-amount2;
                        amount2 = 0;
                      } else {
                        keepAmount = 0;
                        amount2 = amount2-itemAmount;
                      }
                      Call.setItem(building, itemType, keepAmount);
                    }
                  }
                }
              }
            }
            ActuallyUpdateTheItems(consumableItems, player);
            return true;
          } else {
            return false;
          }
        }
      }
      return false;
    } catch (Exception e) {
      Log.info("UpdateCoreItems.Consume is being a dick again");
      return false;
    }
  }
  
  public static void ActuallyUpdateTheItems(HashMap<Item, Integer> map, Player player){
    for (Item itemType : Items.serpuloItems) {
      if (map.get(itemType)!=null) {
        int itemAmount = map.get(itemType);
        usurvplugin.playerTile.get(player.uuid()).build.items.set(itemType, itemAmount);
      } else {
        usurvplugin.playerTile.get(player.uuid()).build.items.set(itemType, 0);
      }
    }
  }
  //this looks stupid holy shit
  public static void ActuallyUpdateTheItemsNonStatic(HashMap<Item, Integer> map, Player player){
    for (Item itemType : Items.serpuloItems) {
      if (map.get(itemType)!=null) {
        int itemAmount = map.get(itemType);
        usurvplugin.playerTile.get(player.uuid()).build.items.set(itemType, itemAmount);
      } else {
        usurvplugin.playerTile.get(player.uuid()).build.items.set(itemType, 0);
      }
    }
  }
}
