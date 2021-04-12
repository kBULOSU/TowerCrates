package br.com.yiatzz.crates.runnable;

import br.com.yiatzz.crates.CratesProvider;
import br.com.yiatzz.crates.crate.Crate;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.Vector3f;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;

public class CratesHeadsRotationRunnable implements Runnable {

    @Override
    public void run() {
        for (Crate crate : CratesProvider.Cache.Local.CRATES.provide().CACHE.values()) {
            if (crate.getHead() == null) {
                continue;
            }

            EntityArmorStand armorStand = ((CraftArmorStand) crate.getHead()).getHandle();
            armorStand.setHeadPose(new Vector3f(armorStand.headPose.getX(), armorStand.headPose.getY() + 0.17F, armorStand.headPose.getZ()));
        }
    }
}
