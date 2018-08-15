package com.mw.raumships;

import com.mw.raumships.common.RSCommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import static com.mw.raumships.RaumShipsMod.*;

@Mod(modid = MODID, name = NAME, version = VERSION)
public class RaumShipsMod {
    public static final String MODID = "raumships";
    public static final String NAME = "RaumShips Mod";
    public static final String VERSION = "1.0";

    @SidedProxy(clientSide = "com.mw.raumships.client.RSClientProxy", serverSide = "com.mw.raumships.server.RSServerProxy")
    public static RSCommonProxy proxy;

    @Instance(MODID)
    public static RaumShipsMod instance;

    public static Minecraft mc;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) throws Exception {
        mc = Minecraft.getMinecraft();

        proxy.preInit(event);
    }

    @EventHandler
    public void load(FMLInitializationEvent event) throws Exception {
        proxy.load(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
