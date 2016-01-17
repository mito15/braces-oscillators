package com.mito.mitomod.common;

import com.mito.mitomod.InstObject.BB_EventHandler;
import com.mito.mitomod.common.block.BlockBrace;
import com.mito.mitomod.common.block.BlockOscillator;
import com.mito.mitomod.common.block.BlockOscillatorChest;
import com.mito.mitomod.common.block.BlockOscillatorPipe;
import com.mito.mitomod.common.block.BlockOscillatorPipeLeaf;
import com.mito.mitomod.common.block.BlockOscillatorPipeNode;
import com.mito.mitomod.common.block.BlockOscillatorPipeRoot;
import com.mito.mitomod.common.entity.EntityBrace;
import com.mito.mitomod.common.entity.EntityFake;
import com.mito.mitomod.common.entity.EntityWall;
import com.mito.mitomod.common.item.ItemBar;
import com.mito.mitomod.common.item.ItemBender;
import com.mito.mitomod.common.item.ItemBrace;
import com.mito.mitomod.common.item.ItemRuler;
import com.mito.mitomod.common.item.ItemWall;
import com.mito.mitomod.common.tile.TileOscillator;
import com.mito.mitomod.common.tile.TileOscillatorChest;
import com.mito.mitomod.common.tile.TileOscillatorPipe;
import com.mito.mitomod.common.tile.TileOscillatorPipeLeaf;
import com.mito.mitomod.common.tile.TileOscillatorPipeNode;
import com.mito.mitomod.common.tile.TileOscillatorPipeRoot;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.RecipeSorter;

@Mod(modid = mitomain.MODID, version = mitomain.VERSION)
public class mitomain {

	public static final String[] colorName = new String[] { "black", "red", "green", "brown", "blue", "purple", "cyan", "silver", "gray", "pink", "lime", "yellow", "lightBlue", "magenta", "orange", "white" };
	public static final String[] color_name = new String[] { "black", "red", "green", "brown", "blue", "purple", "cyan", "silver", "gray", "pink", "lime", "yellow", "light_blue", "magenta", "orange", "white" };
	public static final String[] type_name = new String[] { "", "C", "noJ", "noJC ", "H", "thin1", "thin2" };
	//public static final String[] type_name1 = new String[] { "", "C ", "", "C ", "H ", "", "" };
	//public static final String[] type_name2 = new String[] { "", "", "(no junction)", "(no junction) ", "(no junction)", "", "(lateral)" };
	//public static final String[] type_name3 = new String[] { "", "", "", "", "", "thin ", "thin " };

	public static final String MODID = "mitomod";
	public static final String VERSION = "1.3.5";

	public static Item ItemBrace;
	public static Item ItemBar;
	public static Item ItemBender;
	public static Item ItemWall;
	public static Item ItemRuler;

	public static Block BlockBrace;
	public static Block BlockBraceFake;
	public static Block BlockOscillator;
	public static Block BlockOscillatorChest;
	public static Block BlockOscillatorPipe;
	public static Block BlockOscillatorPipeLeaf;
	public static Block BlockOscillatorPipeNode;
	public static Block BlockOscillatorPipeRoot;

	public static final CreativeTabs tab = new CreativeTabMito("CreativeTabMito");

	@Mod.Instance("mitomod")
	public static mitomain INSTANCE;

	@SidedProxy(clientSide = "com.mito.mitomod.client.mitoClientProxy", serverSide = "com.mito.mitomod.common.mitoCommonProxy")
	public static mitoCommonProxy proxy;
	//public static boolean vampSwitch = false;

	public static final int GUI_ID = 0;
	
	@SideOnly(Side.CLIENT)
	public static int AuroraRenderType;
	@SideOnly(Side.CLIENT)
	public static int OscillatorRenderType;
	@SideOnly(Side.CLIENT)
	public static int PipeRenderType;
	public BB_EventHandler leh;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		proxy.preInit();

		ItemBar = new ItemBar().setUnlocalizedName("ItemBar");
		ItemBrace = new ItemBrace().setUnlocalizedName("ItemBrace").setCreativeTab(mitomain.tab);
		ItemBender = new ItemBender().setUnlocalizedName("ItemBender").setCreativeTab(mitomain.tab);
		ItemWall = new ItemWall().setUnlocalizedName("ItemWall").setCreativeTab(mitomain.tab);
		ItemRuler = new ItemRuler().setUnlocalizedName("ItemRuler").setCreativeTab(mitomain.tab);

		BlockBrace = new BlockBrace(true);
		BlockBraceFake = new BlockBrace(false);
		//blockMachine = new machineBlock();
		//braceBlock = new braceBlock();
		//HorizontalBraceBlock = new HorizontalBraceBlock();
		//BlockWhiteAsh = new BlockWhiteAsh();
		//BlockAurora = new BlockAurora();
		BlockOscillator = new BlockOscillator();
		//BlockFractalite = new BlockFractalite();
		//BlockRadicalite = new BlockRadicalite();
		BlockOscillatorPipe = new BlockOscillatorPipe();
		BlockOscillatorPipeLeaf = new BlockOscillatorPipeLeaf();
		BlockOscillatorPipeNode = new BlockOscillatorPipeNode();
		BlockOscillatorPipeRoot = new BlockOscillatorPipeRoot();
		BlockOscillatorChest = new BlockOscillatorChest();

		String s = "";

		/**
		for (int i2 = 0; i2 < 16; ++i2) {
			for (int i = 0; i < 5; ++i) {
				for (int i1 = 0; i1 < 7; ++i1) {
					int size = (int) Math.pow(2, i);
					s = s+"item.ItemBrace"+this.type_name[i1]+"size"+size+"_"+this.color_name[i2]+".name="+this.type_name3[i1]+this.colorName[i2]+" "+this.type_name1[i1]+"Brace x"+size+this.type_name2[i1]+"\r\n";
				}
			}
		}
		**/

		mitoLogger.info(s);

		GameRegistry.registerItem(ItemBender, "ItemBender");
		GameRegistry.registerItem(ItemBrace, "ItemBrace");
		GameRegistry.registerItem(ItemBar, "ItemBar");
		GameRegistry.registerItem(ItemWall, "ItemWall");
		GameRegistry.registerItem(ItemRuler, "ItemRuler");

		GameRegistry.registerBlock(BlockBrace, "BlockBrace");
		GameRegistry.registerBlock(BlockBraceFake, "BlockBraceFake");
		//GameRegistry.registerBlock(blockMachine, "blockMachine");
		//GameRegistry.registerBlock(braceBlock, "blockBrace");
		//GameRegistry.registerBlock(HorizontalBraceBlock, "horizontalBlockBrace");
		//GameRegistry.registerBlock(BlockWhiteAsh, "BlockWhiteAsh");
		//GameRegistry.registerBlock(BlockAurora, "BlockAurora");
		GameRegistry.registerBlock(BlockOscillator, "BlockOscillator");
		//GameRegistry.registerBlock(BlockFractalite, "BlockFractalite");
		//GameRegistry.registerBlock(BlockRadicalite, "BlockRadicalite");
		GameRegistry.registerBlock(BlockOscillatorPipe, "BlockOscillatorPipe");
		//GameRegistry.registerBlock(BlockOscillatorPipeLeaf, "BlockOscillatorPipeLeaf");
		//GameRegistry.registerBlock(BlockOscillatorPipeNode, "BlockOscillatorPipeNode");
		//GameRegistry.registerBlock(BlockOscillatorPipeRoot, "BlockOscillatorPipeRoot");
		GameRegistry.registerBlock(BlockOscillatorChest, "BlockOscillatorChest");

		//GameRegistry.addRecipe(new ItemStack(nethersoul, 1), new Object[] { "XXX", "X X", "XXX", 'X', Blocks.dirt });

		PacketHandler.init();
	}

	@EventHandler
	public void Init(FMLInitializationEvent e) {
		
		this.leh = new BB_EventHandler();
		MinecraftForge.EVENT_BUS.register(leh);
		FMLCommonHandler.instance().bus().register(leh);

		MinecraftForge.EVENT_BUS.register(this);FMLCommonHandler.instance().bus().register(this);

		proxy.init();
		
		GameRegistry.registerTileEntity(TileOscillator.class, "TileOscillator");
		GameRegistry.registerTileEntity(TileOscillatorPipe.class, "TileOscillatorPipe");
		GameRegistry.registerTileEntity(TileOscillatorPipeNode.class, "TileOscillatorPipeNode");
		GameRegistry.registerTileEntity(TileOscillatorPipeLeaf.class, "TileOscillatorPipeLeaf");
		GameRegistry.registerTileEntity(TileOscillatorPipeRoot.class, "TileOscillatorPipeRoot");
		GameRegistry.registerTileEntity(TileOscillatorChest.class, "TileOscillatorChest");

		//GUIの登録

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

		//entityの登録

		EntityRegistry.registerModEntity(EntityBrace.class, "EntityBrace", 1, this, 512, 120, false);
		EntityRegistry.registerModEntity(EntityFake.class, "EntityFake", 3, this, 512, 120, false);
		EntityRegistry.registerModEntity(EntityWall.class, "EntityWall", 2, this, 512, 120, false);

		//mobSpawnの追加

		//recipeの追加
		//RecipeSorter.register("mitomod;shaped", MitoShapedRecipe.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped before:minecraft:shapeless");
		RecipeSorter.register("mitomod;shapeless", MitoShapelessRecipe.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");

		GameRegistry.addRecipe(new MitoShapedRecipe());
		GameRegistry.addRecipe(new MitoShapelessRecipe());

		GameRegistry.addRecipe(new ItemStack(this.ItemBrace, 4, 0),
				"#  ",
				" # ",
				"  #",
				'#', Blocks.iron_bars);

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

	//@SubscribeEvent
	//public void KeyHandlingEvent(KeyInputEvent e) {
	//	mitoLogger.info();
	//}

	/*@SubscribeEvent
	public void onClientTick(PlayerTickEvent e) {
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e) {
	}
	
	@SubscribeEvent
	public void onLivingUpdate(LivingEvent.LivingUpdateEvent e) {
	}
	
	@SubscribeEvent
	public void onLivingSpawn(LivingSpawnEvent.CheckSpawn e) {
		if(e.entity instanceof EntityBat){
			e.setResult(Result.DENY);
		}
	}
	
	@SubscribeEvent
	public void onLivingUpdate(EntityJoinWorldEvent e) {
	}*/

	//public final Minecraft minecraft = Minecraft.getMinecraft();;

//	@SubscribeEvent
//	public void onPlayerTick(TickEvent.PlayerTickEvent e) {

		//PlayerVampire.sunFire(e);

//	}
}
