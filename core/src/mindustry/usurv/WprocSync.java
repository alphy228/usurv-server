package mindustry.usurv;

import arc.util.*;
import arc.struct.Seq;

import mindustry.world.blocks.logic.LogicBlock;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.Vars;
import mindustry.content.*;
import mindustry.world.Block;

public class WprocSync {
    public int usedHeight = 6;
    public float maxUpdateRate = 0.12f;
    private String code = "setblock block @stone-wall @thisx @thisy @derelict 0";
    public int placeX = 0;

    public void init() {
        Vars.world.tile(0, usedHeight).setBlock(Blocks.worldProcessor);
        Timer.schedule(() -> {
            placeX = -1;
            for (int x=1;x<(Vars.world.width());x=x+1) {
                if (!(Vars.world.tile(x, usedHeight)==null)) {
                    Block b = Vars.world.tile(x, usedHeight).block();
                    if (b != Blocks.worldProcessor) {
                        placeX = x;
                        break;
                    }
                }
            }
            Seq<LogicBlock.LogicLink> links = ((LogicBlock.LogicBuild)Vars.world.tile(0, usedHeight).build).links;
            try {
                //Vars.state.rules.allowEditWorldProcessors=true;
                Call.constructFinish(Vars.world.tile(placeX, usedHeight), Blocks.worldProcessor, null, (byte)0, Team.malis, LogicBlock.compress(code, links));
                ((LogicBlock.LogicBuild)Vars.world.tile(placeX, usedHeight).build).readCompressed(LogicBlock.compress(code, links), false);
                //Vars.state.rules.allowEditWorldProcessors=false;
                //Timer.schedule(() -> {u.kill();},0.1f);
                //Vars.world.tile(placeX, usedHeight).setNet(Blocks.worldProcessor,Team.sharded,0);
                //if (Vars.world.tile(placeX, usedHeight).build instanceof LogicBlock.LogicBuild) {
                   //((LogicBlock.LogicBuild)Vars.world.tile(placeX, usedHeight).build).executor.load(LogicBlock.compress(code, links));
                   //Call.tileConfig(null, Vars.world.tile(placeX, usedHeight).build, LogicBlock.compress(code, links));
                    //((LogicBlock.LogicBuild)Vars.world.tile(placeX, usedHeight).build).updateCode;
                //}
            } catch (Exception e) {
                Log.info("Illegal world processor code was fed into mindustry.usurv.WprocSync");
            }
            code = "setblock block @stone-wall @thisx @thisy @derelict 0";
        } ,5, maxUpdateRate);
    }

    public void addCode(String added) {
        code=added+code;
    }


    public void setBuildingPropSynced(Building b, String logicProp, String logicVal) {
        addCode("getblock building b "+b.x/8+" "+b.y/8+"\nsetprop "+logicProp+" b "+logicVal+"\n");
    }
}



