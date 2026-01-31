package mindustry.usurv.patches;



public class UsurvPatch {
  public static String patch = """
  name:"usurv content patch"

  //unit changes

  //flare tree
  unit.flare.buildSpeed: 0.2f
  unit.flare.mineTier: 1
  unit.flare.mineSpeed: 1f
  unit.flare.itemCapacity: 30

  unit.horizon.buildSpeed: 0.2f
  unit.horizon.mineTier: 1
  unit.horizon.mineSpeed: 1f
  unit.horizon.itemCapacity: 60

  unit.zenith.buildSpeed: 0.2f
  unit.zenith.mineTier: 1
  unit.zenith.mineSpeed: 1f
  unit.zenith.itemCapacity: 120

  unit.antumbra.buildSpeed: 0.2f
  unit.antumbra.mineTier: 1
  unit.antumbra.mineSpeed: 1f
  unit.antumbra.itemCapacity: 300

  unit.eclipse.buildSpeed: 0.2f
  unit.eclipse.mineTier: 1
  unit.eclipse.mineSpeed: 1f
  unit.eclipse.itemCapacity: 700


  //mono tree
  unit.mono.buildSpeed: 0.2f
  unit.mono.itemCapacity: 40

  unit.mono.itemCapacity: 40

  unit.poly.itemCapacity: 50

  unit.mega.itemCapacity: 100

  unit.quad.itemCapacity: 200

  unit.oct.itemCapacity: 600

  //nova tree
  unit.nova.mineTier: 1
  unit.nova.mineSpeed: 1f
  unit.nova.itemCapacity: 40

  unit.pulsar.itemCapacity: 80

  unit.quasar.itemCapacity: 250

  unit.vela.mineTier: 1
  unit.vela.mineSpeed: 1f
  unit.vela.itemCapacity: 550

  unit.corvus.buildSpeed: 0.2f
  unit.corvus.mineTier: 1
  unit.corvus.mineSpeed: 1f
  unit.corvus.itemCapacity: 3000
  unit.corvus.rotateToBuilding: false

  //crawler tree
  unit.crawler.buildSpeed: 0.2f
  unit.crawler.mineTier: 1
  unit.crawler.mineSpeed: 1f

  unit.atrax.buildSpeed: 0.2f
  unit.atrax.mineTier: 1
  unit.atrax.mineSpeed: 1f
  unit.atrax.itemCapacity: 80

  unit.spiroct.buildSpeed: 0.2f
  unit.spiroct.mineTier: 1
  unit.spiroct.mineSpeed: 1f
  unit.spiroct.itemCapacity: 200

  unit.arkyid.buildSpeed: 0.2f
  unit.arkyid.mineTier: 1
  unit.arkyid.mineSpeed: 1f
  unit.arkyid.itemCapacity: 500

  unit.toxopid.buildSpeed: 0.2f
  unit.toxopid.mineTier: 1
  unit.toxopid.mineSpeed: 1f
  unit.toxopid.itemCapacity: 1500

  //dagger tree
  unit.dagger.buildSpeed: 0.2f
  unit.dagger.mineTier: 1
  unit.dagger.mineSpeed: 1f
  unit.dagger.itemCapacity: 60

  unit.mace.buildSpeed: 0.2f
  unit.mace.mineTier: 1
  unit.mace.mineSpeed: 1f
  unit.mace.itemCapacity: 140

  unit.fortress.buildSpeed: 0.2f
  unit.fortress.mineTier: 1
  unit.fortress.mineSpeed: 1f
  unit.fortress.itemCapacity: 350

  unit.scepter.buildSpeed: 0.2f
  unit.scepter.mineTier: 1
  unit.scepter.mineSpeed: 1f
  unit.scepter.itemCapacity: 1150

  unit.reign.buildSpeed: 0.2f
  unit.reign.mineTier: 1
  unit.reign.mineSpeed: 1f
  unit.reign.itemCapacity: 4250

  //risso tree
  unit.risso.buildSpeed: 0.2f
  unit.risso.mineTier: 1
  unit.risso.mineSpeed: 1f
  unit.risso.rotateToBuilding: false
  unit.risso.itemCapacity: 100


  unit.minke.buildSpeed: 0.2f
  unit.minke.mineTier: 1
  unit.minke.mineSpeed: 1f
  unit.minke.rotateToBuilding: false
  unit.minke.itemCapacity: 250

  unit.bryde.buildSpeed: 0.2f
  unit.bryde.mineTier: 1
  unit.bryde.mineSpeed: 1f
  unit.bryde.rotateToBuilding: false
  unit.bryde.itemCapacity: 500

  unit.sei.buildSpeed: 0.2f
  unit.sei.mineTier: 1
  unit.sei.mineSpeed: 1f
  unit.sei.rotateToBuilding: false
  unit.sei.itemCapacity: 2000

  unit.omura.buildSpeed: 0.2f
  unit.omura.mineTier: 1
  unit.omura.mineSpeed: 1f
  unit.omura.rotateToBuilding: false
  unit.omura.itemCapacity: 6000

  //retusa tree
  unit.retusa.buildSpeed: 0.2f
  unit.retusa.mineTier: 1
  unit.retusa.mineSpeed: 1f
  unit.retusa.itemCapacity: 95

  unit.oxynoe.mineTier: 1
  unit.oxynoe.mineSpeed: 1f
  unit.oxynoe.itemCapacity: 180

  unit.cyerce.mineTier: 1
  unit.cyerce.mineSpeed: 1f
  unit.cyerce.itemCapacity: 455

  unit.aegires.mineTier: 1
  unit.aegires.mineSpeed: 1f
  unit.aegires.itemCapacity: 1750

  unit.navanax.mineTier: 1
  unit.navanax.mineSpeed: 1f
  unit.navanax.itemCapacity: 7500


  //block changes

  //radar(wreck), knockoff 1x1 container with infinite capacity
  block.radar.health: 99999
  block.radar.unloadable: true
  block.radar.itemCapacity: 99999
  block.radar.localizedName: "Wreck"
  block.radar.hasItems: true
  block.radar.fullIcon: scrap-wall-preview
  block.radar.uiIcon: block-scrap-wall-ui
  block.radar.region: block-scrap-wall-full
  block.radar.consumes: {
    remove: all
    items: [
    copper/99999
    lead/99999
    graphite/99999
    metaglass/99999
    silicon/99999
    titanium/99999
    plastanium/99999
    thorium/99999
    phase-fabric/99999
    surge-alloy/99999
    scrap/99999
    coal/99999
    sand/99999
    ]
  }
  """;
}