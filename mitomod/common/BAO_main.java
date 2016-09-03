package com.mito.mitomod.common;

import java.io.File;

import com.mito.mitomod.BraceBase.BB_EventHandler;
import com.mito.mitomod.BraceBase.Brace.Render.BB_TypeResister;
import com.mito.mitomod.common.block.BlockNeuron;
import com.mito.mitomod.common.block.BlockOscillator;
import com.mito.mitomod.common.block.BlockOscillatorChest;
import com.mito.mitomod.common.block.BlockOscillatorPipe;
import com.mito.mitomod.common.entity.EntityBrace;
import com.mito.mitomod.common.entity.EntityFake;
import com.mito.mitomod.common.entity.EntityWall;
import com.mito.mitomod.common.item.ItemBar;
import com.mito.mitomod.common.item.ItemBender;
import com.mito.mitomod.common.item.ItemBlockSetter;
import com.mito.mitomod.common.item.ItemBrace;
import com.mito.mitomod.common.item.ItemFakeBlock;
import com.mito.mitomod.common.item.ItemLinearMotor;
import com.mito.mitomod.common.item.ItemRuler;
import com.mito.mitomod.common.item.ItemSelectTool;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.FMLInjectionData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.RecipeSorter;

@Mod(modid = BAO_main.MODID, version = BAO_main.VERSION, guiFactory = "com.mito.ruins.client.config.CaveGuiFactory")
public class BAO_main {

	public static final String MODID = "mitomod";
	public static final String VERSION = "1.3.4";
	public static boolean debug = false;

	public static Item ItemBrace;
	public static Item ItemBar;
	public static Item ItemBender;
	public static Item ItemWall;
	public static Item ItemRuler;
	public static Item ItemLinearMotor;
	public static Item ItemBlockSetter;
	public static Item ItemSelectTool;
	public static Item ItemFakeBlock;

	public static Block BlockNeuron;
	public static Block BlockOscillator;
	public static Block BlockOscillatorChest;
	public static Block BlockOscillatorPipe;

	public static final CreativeTabs tab = new CreativeTabMito("CreativeTabBAO");

	@Mod.Instance(BAO_main.MODID)
	public static BAO_main INSTANCE;

	@SidedProxy(clientSide = "com.mito.mitomod.client.mitoClientProxy", serverSide = "com.mito.mitomod.common.mitoCommonProxy")
	public static mitoCommonProxy proxy;
	//public static boolean vampSwitch = false;

	public static final int GUI_ID_OCHEST = 0;
	public static final int GUI_ID_BBSetter = 1;
	public static final int GUI_ID_BBSelect = 2;

	@SideOnly(Side.CLIENT)
	public static int OscillatorRenderType;
	@SideOnly(Side.CLIENT)
	public static int PipeRenderType;
	public BB_EventHandler leh;

	//configuration
	public boolean vampmode = false;
	public File modelDir;
	public File shapesDir;
	public File GroupsDir;
	public File ObjsDir;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		File mcDir = (File) FMLInjectionData.data()[6];
		try {
			modelDir = new File(mcDir, "brace-models");
			modelDir.mkdir();
			shapesDir = new File(modelDir, "shapes");
			shapesDir.mkdir();
			GroupsDir = new File(modelDir, "groups");
			GroupsDir.mkdir();
			ObjsDir = new File(modelDir, "objects");
			ObjsDir.mkdir();
		} finally {
		}

		proxy.preInit();

		ItemFakeBlock = new ItemFakeBlock().setUnlocalizedName("ItemFakeBlock").setCreativeTab(BAO_main.tab);
		ItemSelectTool = new ItemSelectTool().setUnlocalizedName("ItemSelectTool").setCreativeTab(BAO_main.tab);
		ItemBrace = new ItemBrace().setUnlocalizedName("ItemBrace").setCreativeTab(BAO_main.tab);
		ItemBender = new ItemBender().setUnlocalizedName("ItemBender").setCreativeTab(BAO_main.tab);
		ItemBar = new ItemBar().setUnlocalizedName("ItemBar");
		ItemBlockSetter = new ItemBlockSetter().setUnlocalizedName("ItemBlockSetter").setCreativeTab(BAO_main.tab);
		//ItemWall = new ItemWall().setUnlocalizedName("ItemWall").setCreativeTab(mitomain.tab);
		ItemRuler = new ItemRuler().setUnlocalizedName("ItemRuler").setCreativeTab(BAO_main.tab);
		ItemLinearMotor = new ItemLinearMotor().setUnlocalizedName("ItemLinearMotor").setCreativeTab(BAO_main.tab);

		BlockNeuron = new BlockNeuron();
		//BlockWhiteAsh = new BlockWhiteAsh();
		//BlockAurora = new BlockAurora();
		BlockOscillator = new BlockOscillator();
		//BlockFractalite = new BlockFractalite();
		//BlockRadicalite = new BlockRadicalite();
		BlockOscillatorPipe = new BlockOscillatorPipe();
		BlockOscillatorChest = new BlockOscillatorChest();

		GameRegistry.registerItem(ItemBar, "ItemBar");
		GameRegistry.registerItem(ItemBender, "ItemBender");
		//GameRegistry.registerItem(ItemWall, "ItemWall");
		GameRegistry.registerItem(ItemRuler, "ItemRuler");
		GameRegistry.registerItem(ItemBlockSetter, "ItemBlockSetter");
		GameRegistry.registerItem(ItemSelectTool, "ItemSelectTool");

		//GameRegistry.registerBlock(BlockWhiteAsh, "BlockWhiteAsh");
		//GameRegistry.registerBlock(BlockAurora, "BlockAurora");
		GameRegistry.registerBlock(BlockOscillator, "BlockOscillator");
		//GameRegistry.registerBlock(BlockFractalite, "BlockFractalite");
		//GameRegistry.registerBlock(BlockRadicalite, "BlockRadicalite");
		GameRegistry.registerBlock(BlockOscillatorPipe, "BlockOscillatorPipe");
		GameRegistry.registerBlock(BlockOscillatorChest, "BlockOscillatorChest");
		GameRegistry.registerItem(ItemBrace, "ItemBrace");
		GameRegistry.registerItem(ItemFakeBlock, "ItemFakeBlock");
		GameRegistry.registerItem(ItemLinearMotor, "ItemLinearMotor");
		GameRegistry.registerBlock(BlockNeuron, "BlockNeuron");

		PacketHandler.init();

		File configDir = new File(mcDir, "config");
		File configFile = new File(configDir, "Braces-Oscillators.cfg");
		Configuration cfg = new Configuration(configFile);
		try {
			cfg.load();//コンフィグをロード
			this.vampmode = cfg.getBoolean("vampire mode", "mode", false, "isPlayerBurnable?");
			debug = cfg.getBoolean("debug mode", "mode", false, "debug");
		} finally {
			cfg.save();//セーブ
		}
	}

	@EventHandler
	public void Init(FMLInitializationEvent e) {

		BB_TypeResister.loadModels();

		this.leh = new BB_EventHandler();
		MinecraftForge.EVENT_BUS.register(leh);
		FMLCommonHandler.instance().bus().register(leh);

		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);

		proxy.init();

		//GUIの登録

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

		//entityの登録

		EntityRegistry.registerModEntity(EntityBrace.class, "EntityBrace", 1, this, 512, 120, false);
		EntityRegistry.registerModEntity(EntityFake.class, "EntityFake", 3, this, 512, 120, false);
		EntityRegistry.registerModEntity(EntityWall.class, "EntityWall", 2, this, 512, 120, false);

		RegisterRecipe();

	}

	public void RegisterRecipe() {

		//RecipeSorter.register("mitomod;shaped", MitoShapedRecipe.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped before:minecraft:shapeless");
		RecipeSorter.register("mitomod;shapeless", MitoShapelessRecipe.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");

		GameRegistry.addRecipe(new MitoShapedRecipe());
		GameRegistry.addRecipe(new MitoShapelessRecipe());

		GameRegistry.addRecipe(new ItemStack(this.ItemBrace, 4, 0),
				"#  ",
				" # ",
				"  #",
				'#', Blocks.iron_bars);

		GameRegistry.addRecipe(new ItemStack(this.ItemBlockSetter),
				"GBG",
				"GCG",
				"GGG",
				'B', this.ItemBrace,
				'C', Blocks.chest,
				'G', Items.gold_ingot);

		GameRegistry.addRecipe(new ItemStack(this.ItemBender),
				" # ",
				" # ",
				"B B",
				'#', Items.iron_ingot,
				'B', this.ItemBar);

		GameRegistry.addRecipe(new ItemStack(this.ItemBar),
				"I  ",
				" I ",
				"  B",
				'B', Blocks.iron_bars,
				'I', Items.iron_ingot);

		GameRegistry.addRecipe(new ItemStack(this.BlockOscillator, 2),
				"IRI",
				"SQS",
				"IRI",
				'I', Blocks.iron_bars,
				'Q', Items.quartz,
				'R', Items.redstone,
				'S', Items.slime_ball);

		GameRegistry.addRecipe(new ItemStack(this.BlockOscillator, 2),
				"IRI",
				"SQS",
				"IRI",
				'I', Blocks.iron_bars,
				'Q', Items.quartz,
				'S', Items.redstone,
				'R', Items.slime_ball);

		GameRegistry.addRecipe(new ItemStack(this.BlockOscillatorPipe, 8),
				"OGO",
				'G', Blocks.glass,
				'O', this.BlockOscillator);

		GameRegistry.addRecipe(new ItemStack(this.BlockOscillatorChest, 2),
				"###",
				"# #",
				"###",
				'#', this.BlockOscillator);
	}

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent e) {
		PlayerVampire.sunFire(e);
	}
}
