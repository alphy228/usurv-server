package mindustry.usurv.player;

import java.lang.Math;
import java.lang.Runnable;
import arc.util.Timer;
import arc.util.Timer.Task;
import java.util.HashMap;
import java.util.Random;
import arc.*;
import arc.util.*;
import arc.struct.Seq;
import mindustry.*;
import mindustry.type.*;
import mindustry.world.Tile;
import mindustry.Vars;
import mindustry.game.Rules;
import mindustry.content.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.gen.Call;
import mindustry.game.Team;
import mindustry.mod.*;
import mindustry.net.Administration.*;
import mindustry.content.Blocks;
import mindustry.ui.Menus;
import mindustry.usurv.Usurv;
import mindustry.usurv.building.*;

public class PlayerUpdate implements Runnable {

  @Override
  public void run(){
    for (Player p : Groups.player) {


      // send player out of alpha coreunit
      if (!(p.unit() == null)) {
        if (p.unit().type == UnitTypes.alpha) {
          if (Usurv.playerLastUnit.containsKey(p.uuid())) {
            if (Usurv.playerLastUnit.get(p.uuid()) != null) {
              if (!(Usurv.playerLastUnit.get(p.uuid()).dead || (Usurv.playerLastUnit.get(p.uuid()) == null))) {
                p.unit(Usurv.playerLastUnit.get(p.uuid()));
              }
            }
          }
        }
      }




      
    }
  }
}
