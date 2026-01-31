package mindustry.usurv.Usurv;

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
import mindustry.world.modules.*;

import usurv.building.*;
import usurv.player.*;
import usurv.special.*;
import usurv.ai.*;

import mindustry.world.meta.*; 

public class usurvplugin extends Plugin{
    //player data
    public static HashMap<String, Tile> playerTile = new HashMap<>();
    public static HashMap<String, Team> playerTeam = new HashMap<>();
    public static HashMap<String, Player> playerByUUID = new HashMap<>();
    public static HashMap<String, Unit> playerLastUnit = new HashMap<>();

 //   public HashMap<String, Tile> givePlayerTile(){
 //       return playerTile;
  //  }

    //starting menus
    String[][] startingUnitNames = {{"[blue]nova", "[green]mono", "[crimson]mace"}};
    
    public static final int startingUnitMenu = Menus.registerMenu((player, option) -> {
        if (option == -1) return;
       
        UnitType type = UnitTypes.nova;
        if (option == 0) {
            type=UnitTypes.nova;
        }
        if (option == 1) {
            type=UnitTypes.mono;
        }
        if (option == 2) {
            type=UnitTypes.mace;
        }
        double maxdist = 0;
        Tile spawntile = null;
        double dist = 0;
        // find farthest spot away from units
        for (int x = 0;x<Vars.world.width();x=x+10) {
            for (int y = 0;y<Vars.world.height();y=y+10) {
                double mindist = 999999;
                for (Unit u : Groups.unit) {
                    int ux = Math.round(u.x);
                    int uy = Math.round(u.y);
                    dist = Math.sqrt((float)((ux-x)*(ux-x)+(uy-y)*(uy-y)));
                    mindist = Math.min(mindist,dist);  
                }
                //Log.info("checking tile " + x + " ," + y + " ,mindist/maxdist = " + mindist + "/" + maxdist);
                if ((mindist>maxdist) && (Vars.world.tile(x,y).block() == Blocks.air) && (Vars.world.tile(x,y).floor().drownTime == 0f)) {
                    spawntile = Vars.world.tile(x,y);
                    //Log.info("setting tile " + x + " ," + y + " as spawn tile");
                    maxdist = mindist;
                }
            }
        }
        Unit spawnedUnit = type.spawn(player.team(),spawntile.x*8,spawntile.y*8);
        player.unit(spawnedUnit);
    
                            
    });

    //create rules for clients
    private static void updateRules(){
        Rules clientrules = Vars.state.rules;

        //ai target change stuff
        Blocks.coreShard.flags = EnumSet.of(BlockFlag.drill);
        Blocks.vault.flags = EnumSet.of(BlockFlag.core);
        Blocks.container.flags = EnumSet.of(BlockFlag.core);
        
        //set general rules
        Blocks.vault.update=true;
        Blocks.container.update=true;
        Vars.state.rules.waves=false;
        Vars.state.rules.attackMode=false;
        Vars.state.rules.pvpAutoPause=false;
        Vars.state.rules.pvp=true;
        Vars.state.rules.canGameOver=false;
        Vars.state.rules.randomWaveAI=true;
        Vars.state.rules.fog=true;
        Vars.state.rules.unitCap=3;
        Vars.state.rules.unitCapVariable=false;
        Blocks.coreShard.health=999999;
        UnitTypes.alpha.health=0;
        
        //set client side rules
        clientrules.buildSpeedMultiplier=0.5f;
        Call.setRules(clientrules);
        //set server side rules
        Vars.state.rules.buildSpeedMultiplier=0f;
    }


    //called when game initializes
    @Override
    public void init(){


        //set unit ai's
       // for (UnitType unit : Vars.content.units()) {
         //   if (unit.flying == false && unit.naval == false) {
           // Log.info("Assigning ground ai to" + unit);
            //unit.aiController = GroundAIAlt::new;
            //}
        //}


        

        
        
        //todo make all players reload properly on map change
        Events.on(WorldLoadEvent.class, event -> {
            
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Log.info("what the fuck interrupted exception in usurvplugin.java?!?");
            }
            //start timers
            Timer timer = new Timer();
            UpdateCoreItems updatecoreitems = new UpdateCoreItems();
            UpdateConstructBlocks updateConstructBlocks = new UpdateConstructBlocks();
            PlayerUpdate playerUpdate = new PlayerUpdate();
            UpdateWrecks updateWrecks = new UpdateWrecks();
            //no scheduleatfixedrate because bugs
            timer.schedule(updatecoreitems, 10f, 0.2f);
            timer.schedule(playerUpdate, 10f, 0.1f);
            timer.schedule(updateConstructBlocks, 10f, 0.2f);
            timer.schedule(updateWrecks, 10f, 1f);
            //update rules
            updateRules();
            //reset player data
            playerTile.clear();
            playerTeam.clear();
            playerByUUID.clear();
            playerLastUnit.clear();
        });
        //generate player core and team
        Events.on(PlayerJoin.class, event -> {
            Player player = event.player;
            String uuid = player.uuid();
            //send clientside rules
            updateRules();

            int playerUCount = 0;

            if (!(playerTeam.get(uuid) == null)) {
                for (Unit j : Groups.unit) {
                    if (j.team == playerTeam.get(uuid)) {
                        playerUCount=playerUCount+1;
                    }
                }
            }
            
            if ((!playerTile.containsKey(player.uuid())) || (playerUCount == 0)) {
                if (playerUCount==0) {
                    playerTeam.remove(uuid);
                    if (!(playerTile.get(uuid) == null)) {
                        Call.constructFinish(playerTile.get(uuid), Blocks.scrapWall, null, (byte) 0, Team.derelict, null);
                    }
                    playerTile.remove(uuid);
                }
                Random rand = new Random();
                int randteamnum = rand.nextInt(200)+40;
                while (playerTeam.containsValue(Team.all[randteamnum])) {
                    randteamnum = rand.nextInt(200)+40;
                }
                Team playernewteam = Team.all[randteamnum];
                playerTeam.put(player.uuid(),playernewteam);
                playerByUUID.put(player.uuid(),player);
                player.team(playernewteam);
                for(int n = 1; n < Vars.world.width()/3; n++) {
                    if (Vars.world.tile(n*3,2).block() != Blocks.coreShard) {
                        Call.constructFinish(Vars.world.tile(n*3,2), Blocks.coreShard, null, (byte) 0, playernewteam, null);
                        playerTile.put(player.uuid(),(Vars.world.tile(n*3,2)));
                        Log.info("Generated player core and team for player " + player.name + " of team " + randteamnum + " calling starter unit picker menu");
                        Call.menu(player.con, startingUnitMenu, "Welcome", "Select starter unit", startingUnitNames);
                    
                        break;
                    }
                }
            } else {
                player.team(playerTeam.get(player.uuid()));
            }
        });


        Events.on(PlayerLeave.class, event -> {
            Player player = event.player;
            String uuid = player.uuid();
            playerLastUnit.put(uuid,player.unit());
        });

        //create unit wrecks
        Events.on(UnitDestroyEvent.class, event -> {
            Unit unit = event.unit;
            ItemModule items = new ItemModule();
            Tile wreckTile = null;
            int wx = Math.round(unit.x/8);
            int wy = Math.round(unit.y/8);
            int attem = 0;
            while ((wreckTile == null)) {
                attem = attem+1;
                if (attem==2) return;
                for(int r = 0; r<20; r++) {
                    for(int dx = -r; dx <= r; dx++) {
                        for(int dy = -r; dy <= r; dy++) {
                            if(Math.abs(dx) != r && Math.abs(dy) != r) continue;
                            Tile t = Vars.world.tile(wx + dx, wy + dy);
                            if(t != null) {
                                if (t.block() == Blocks.air) {
                                    wx = t.x;
                                    wy = t.y;
                                    wreckTile = t;
                                    dx = 99999;
                                    dy = 99999;
                                    r = 99999;
                                }
                            }
                        }
                    }
                }
            }
            items.add(unit.stack.item, unit.stack.amount);
            Wreck wreck = new Wreck(wreckTile,unit.type,null,items);
            wrecks.add(wreck);
        });

        //create container/vault wrecks
        Events.on(BlockDestroyEvent.class, event -> {
            Tile tile = event.tile;
            Block block = tile.block();
            Building build = tile.build;
            if (block == Blocks.container || block == Blocks.vault) {
                if (build.team != Team.derelict) {
                    Wreck wreck = new Wreck(tile,null,block,build.items);
                    wrecks.add(wreck);
                }
            }
        });

        //add a chat filter that changes the contents of all messages
        //in this case, all instances of "heck" are censored
        Vars.netServer.admins.addChatFilter((player, text) -> text.replace("heck", "h*ck"));
        
    }

    //array with all wrecks
    public static Seq<Wreck> wrecks = new Seq<>();

    //update wrecks
    public class UpdateWrecks implements Runnable {
        @Override
        public void run() {
            for (Wreck wreck : wrecks) {
                wreck.updateWreck();
            }
        }
    }

    

    //register commands that run on the server
    @Override
    public void registerServerCommands(CommandHandler handler){
        handler.register("reactors", "List all thorium reactors in the map.", args -> {
            for(int x = 0; x < Vars.world.width(); x++){
                for(int y = 0; y < Vars.world.height(); y++){
                    //loop through and log all found reactors
                    //make sure to only log reactor centers
                    if(Vars.world.tile(x, y).block() == Blocks.thoriumReactor && Vars.world.tile(x, y).isCenter()){
                        Log.info("Reactor at @, @", x, y);
                    }
                }
            }
        });
    }

    //register commands that player can invoke in-game
    @Override
    public void registerClientCommands(CommandHandler handler){

        //register a simple reply command
        handler.<Player>register("reply", "<text...>", "A simple ping command that echoes a player's text.", (args, player) -> {
            player.sendMessage("You said: [accent] " + args[0]);
        });

        //register a whisper command which can be used to send other players messages
        handler.<Player>register("whisper", "<player> <text...>", "Whisper text to another player.", (args, player) -> {
            //find player by name
            Player other = Groups.player.find(p -> p.name.equalsIgnoreCase(args[0]));

            //give error message with scarlet-colored text if player isn't found
            if(other == null){
                player.sendMessage("[scarlet]No player by that name found!");
                return;
            }

            //send the other player a message, using [lightgray] for gray text color and [] to reset color
            other.sendMessage("[lightgray](whisper) " + player.name + ":[] " + args[1]);
        });
    }
}
