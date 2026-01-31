package mindustry.usurv.special;

import java.lang.Math;
import java.lang.Runnable;
import arc.util.Timer;
import arc.util.Timer.Task;
import java.util.HashMap;
import java.util.Random;
import arc.*;
import arc.util.*;
import arc.struct.*;
import mindustry.*;
import mindustry.type.*;
import mindustry.world.Tile;
import mindustry.world.modules.*;
import mindustry.Vars;
import mindustry.game.Rules;
import mindustry.content.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.gen.Call;
import mindustry.game.Team;
import mindustry.world.*;
import mindustry.mod.*;
import mindustry.net.Administration.*;
import mindustry.content.Blocks;
import mindustry.ui.Menus;
import mindustry.usurv.building.*;
import mindustry.usurv.player.*;
import mindustry.usurv.Usurv;
import mindustry.world.meta.*; 

public class Wreck {
  private Tile tile;
  private UnitType type;
  private ItemModule items;
  private Block block;

  public Wreck(Tile tile, UnitType type, Block block, ItemModule setitems) {
    this.tile = tile;
    this.type = type;
    this.items = new ItemModule();
    this.block = block;
    this.items.set(setitems);
    if (tile.block() == Blocks.air) {
      if (block == null) {
        this.tile.setNet(Blocks.radar,Team.derelict,0);
        this.tile.build.health = type.health*6;
      } else {
        this.tile.setNet(block,Team.derelict,0);
        this.tile.build.health = block.health*6;
      }
      if (!(this.tile.build.items == null)) {
        this.tile.build.items.set(setitems);
      }
    }
  }
  
  public void updateWreck() {
    
    if ((this.tile.block()!=this.block && this.tile.block()!=Blocks.radar) || ((!(this.tile.build.items == null)) && this.tile.build.items.length() == 0)) {
      Usurv.wrecks.remove(this);
      return;
    }

    //stop repair abuse
    this.tile.build.team = Team.derelict;
    
    if (this.block == null) {
      Call.label(this.type + "'s wreck", 1f, this.tile.x*8, this.tile.y*8);
    } else {
      Call.label(this.block + "'s items", 1f, this.tile.x*8, this.tile.y*8);
    }
  }
}
