package mindustry.usurv.logic;

import arc.*;
import arc.func.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.game.Teams.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.Vars;
import mindustry.world.blocks.storage.StorageBlock;

import java.lang.Math;

public class Ai {
    
    public static StorageBlock.StorageBuild closestStorage(Unit u, Boolean enemy) {
        double minDist = 999999;
        StorageBlock.StorageBuild result = null;
        for (Building b : Groups.build) {
            if (Math.hypot(u.x-b.x,u.y-b.y)<minDist && ((u.team == b.team && enemy == false) || u.team != b.team) && b instanceof StorageBlock.StorageBuild) {
                result = (StorageBlock.StorageBuild)b;
                minDist = Math.hypot(u.x-b.x,u.y-b.y);
            }
        }
        return result;
    }

    public static Unit closestUnit(Unit u, Boolean enemy) {
        double minDist = 999999;
        Unit result = null;
        for (Unit u2 : Groups.unit) {
            if (Math.hypot(u.x-u2.x,u.y-u2.y)<minDist && ((u.team == u2.team && enemy == false) || u2.team != u.team) && u != u2) {
                result = u2;
                minDist = Math.hypot(u.x-u2.x,u.y-u2.y);
            }
        }
        return result;
    }

    public static Teamc closestTarget(Unit u, Boolean enemy) {
        StorageBlock.StorageBuild cs = closestStorage(u, enemy);
        Unit cu = closestUnit(u, enemy);
        if (Math.hypot(u.x-cu.x,u.y-cu.y)>Math.hypot(u.x-cs.x,u.y-cs.y)) {
            return cu;
        } else {
            return cs;
        }
    }

}
