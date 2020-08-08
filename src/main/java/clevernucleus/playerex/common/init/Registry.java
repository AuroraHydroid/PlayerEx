package clevernucleus.playerex.common.init;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import clevernucleus.playerex.common.PlayerEx;
import clevernucleus.playerex.common.init.capability.IPlayerElements;
import clevernucleus.playerex.common.init.capability.PlayerElements;
import clevernucleus.playerex.common.init.capability.SyncPlayerElements;
import clevernucleus.playerex.common.init.element.*;
import clevernucleus.playerex.common.util.Calc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

/**
 * Mod registry. Holds all registry objects added by PlayerEx.
 */
@Mod.EventBusSubscriber(modid = PlayerEx.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registry {
	
	/** Holder for iterating over all nbt read/write elements. */
	public static final Set<IDataElement> DATA_ELEMENTS = new HashSet<IDataElement>();
	
	/** Holder for iterating over all game elements. */
	public static final Set<IElement> GAME_ELEMENTS = new HashSet<IElement>();
	
	/** Network instance. */
	public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(new ResourceLocation(PlayerEx.MODID, "path"), () -> "1", "1"::equals, "1"::equals);
	
	/** Capability access. */
	@CapabilityInject(IPlayerElements.class)
	public static final Capability<IPlayerElements> CAPABILITY = null;
	
	/** Capability pass-through function. */
	public static final Function<PlayerEntity, LazyOptional<IPlayerElements>> ELEMENTS = var -> var.getCapability(CAPABILITY, null);
	
	public static final IDataElement CONSTITUTION = new PropertyElement("Constitution", 0F, 1F, 10F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, var0.get(par0, var1) + par2);
		var0.add(par0, Registry.HEALTH, par2);
		var0.add(par0, Registry.ARMOUR_TOUGHNESS, par2 * 0.25D);
		var0.add(par0, Registry.KNOCKBACK_RESISTANCE, par2 * 0.02D);
		var0.add(par0, Registry.EXPLOSION_RESISTANCE, par2 * 0.02D);
		var0.add(par0, Registry.DROWNING_RESISTANCE, par2 * 0.02D);
	});
	
	public static final IDataElement STRENGTH = new PropertyElement("Strength", 0F, 1F, 10F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, var0.get(par0, var1) + par2);
		var0.add(par0, Registry.HEALTH_REGEN, par2 * 0.0005D);
		var0.add(par0, Registry.DAMAGE_RESISTANCE, par2 * 0.02D);
		var0.add(par0, Registry.FALL_RESISTANCE, par2 * 0.02D);
		var0.add(par0, Registry.MELEE_DAMAGE, par2 * 0.25D);
	});
	
	public static final IDataElement DEXTERITY = new PropertyElement("Dexterity", 0F, 1F, 10F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, var0.get(par0, var1) + par2);
		var0.add(par0, Registry.ARMOUR, par2 * 0.5D);
		var0.add(par0, Registry.MOVEMENT_SPEED, par2 * 0.02D);
		var0.add(par0, Registry.MELEE_CRIT_DAMAGE, par2 * 0.05D);
		var0.add(par0, Registry.MELEE_KNOCKBACK, par2 * 0.05D);
		var0.add(par0, Registry.ATTACK_SPEED, par2 * 0.25D);
		var0.add(par0, Registry.RANGED_DAMAGE, par2 * 0.25D);
	});
	
	public static final IDataElement INTELLIGENCE = new PropertyElement("Intelligence", 0F, 1F, 10F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, var0.get(par0, var1) + par2);
		var0.add(par0, Registry.HEALTH_REGEN_AMP, par2 * 0.02D);
		var0.add(par0, Registry.FIRE_RESISTANCE, par2 * 0.02D);
		var0.add(par0, Registry.LAVA_RESISTANCE, par2 * 0.02D);
		var0.add(par0, Registry.POISON_RESISTANCE, par2 * 0.02D);
		var0.add(par0, Registry.WITHER_RESISTANCE, par2 * 0.02D);
		var0.add(par0, Registry.RANGED_CRIT_DAMAGE, par2 * 0.05D);
		var0.add(par0, Registry.LIFESTEAL, par2 * 0.02D);
	});
	
	public static final IDataElement LUCK = new PropertyElement("Luck", 0F, 1F, 10F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, var0.get(par0, var1) + par2);
		var0.add(par0, Registry.LUCKINESS, par2);
		var0.add(par0, Registry.MELEE_CRIT_CHANCE, par2 * 0.02D);
		var0.add(par0, Registry.EVASION_CHANCE, par2 * 0.02D);
		var0.add(par0, Registry.RANGED_CRIT_CHANCE, par2 * 0.02D);
	});
	
	public static final IElement HEALTH = new BasicElement("Health", 1F, 10F, (par0, par1, par2) -> {
		par0.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(par0.getAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue() + par2);
		
		if(par2 < 0D) {
			par0.setHealth((par0.getHealth() + par2) < par0.getMaxHealth() ? par0.getMaxHealth() : ((par0.getHealth() + par2) < 1F) ? 1F : par0.getHealth());
		}
	}, (par0, par1) -> (double)par0.getMaxHealth());
	
	public static final IDataElement HEALTH_REGEN = new PropertyElement("HealthRegen", 0F, 0.0001F, 0.001F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, var0.get(par0, var1) + par2);
	});
	
	public static final IDataElement HEALTH_REGEN_AMP = new PropertyElement("HealthRegenAmp", 0F, 0.01F, 0.2F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, Calc.dim(var0.get(par0, var1), par2, 10D));
	});
	
	public static final IElement ARMOUR = new BasicElement("Armour", 1F, 10F, (par0, par1, par2) -> {
		par0.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(Calc.dim(par0.getAttribute(SharedMonsterAttributes.ARMOR).getBaseValue(), par2, 50D));
	}, (par0, par1) -> par0.getAttribute(SharedMonsterAttributes.ARMOR).getBaseValue());
	
	public static final IElement ARMOUR_TOUGHNESS = new BasicElement("ArmourToughness", 0.25F, 4F, (par0, par1, par2) -> {
		par0.getAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(Calc.dim(par0.getAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getBaseValue(), par2, 50D));
	}, (par0, par1) -> par0.getAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getBaseValue());
	
	public static final IElement KNOCKBACK_RESISTANCE = new BasicElement("KnockbackResistance", 0.01F, 0.2F, (par0, par1, par2) -> {
		par0.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(Calc.dim(par0.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getBaseValue(), par2, 1D));
	}, (par0, par1) -> par0.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getBaseValue());
	
	public static final IDataElement DAMAGE_RESISTANCE = new PropertyElement("DamageResistance", 0F, 0.01F, 0.2F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, Calc.dim(var0.get(par0, var1), par2, 1D));
	});
	
	public static final IDataElement FIRE_RESISTANCE = new PropertyElement("FireResistance", 0F, 0.01F, 0.2F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, Calc.dim(var0.get(par0, var1), par2, 1D));
	});
	
	public static final IDataElement LAVA_RESISTANCE = new PropertyElement("LavaResistance", 0F, 0.01F, 0.2F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, Calc.dim(var0.get(par0, var1), par2, 1D));
	});
	
	public static final IDataElement EXPLOSION_RESISTANCE = new PropertyElement("ExplosionResistance", 0F, 0.01F, 0.2F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, Calc.dim(var0.get(par0, var1), par2, 1D));
	});
	
	public static final IDataElement FALL_RESISTANCE = new PropertyElement("FallResistance", 0F, 0.01F, 0.2F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, Calc.dim(var0.get(par0, var1), par2, 1D));
	});
	
	public static final IDataElement POISON_RESISTANCE = new PropertyElement("PoisonResistance", 0F, 0.01F, 0.2F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, Calc.dim(var0.get(par0, var1), par2, 1D));
	});
	
	public static final IDataElement WITHER_RESISTANCE = new PropertyElement("WitherResistance", 0F, 0.01F, 0.2F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, Calc.dim(var0.get(par0, var1), par2, 1D));
	});
	
	public static final IDataElement DROWNING_RESISTANCE = new PropertyElement("DrowningResistance", 0F, 0.01F, 0.2F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, Calc.dim(var0.get(par0, var1), par2, 1D));
	});
	
	public static final IElement MOVEMENT_SPEED = new BasicElement("MovementSpeed", 0.01F, 0.2F, (par0, par1, par2) -> {
		//TODO
	}, (par0, par1) -> 0D);//TODO
	
	public static final IElement MELEE_DAMAGE = new BasicElement("MeleeDamage", 0.25F, 4F, (par0, par1, par2) -> {
		par0.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(par0.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue() + par2);
	}, (par0, par1) -> par0.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue());
	
	public static final IDataElement MELEE_CRIT_DAMAGE = new PropertyElement("MeleeCritDamage", 0F, 0.05F, 0.5F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, Calc.dim(var0.get(par0, var1), par2, 10D));
	});
	
	public static final IDataElement MELEE_CRIT_CHANCE = new PropertyElement("MeleeCritChance", 0F, 0.01F, 0.2F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, Calc.dim(var0.get(par0, var1), par2, 1D));
	});
	
	public static final IElement MELEE_KNOCKBACK = new BasicElement("MeleeKnockback", 0.1F, 2F, (par0, par1, par2) -> {
		par0.getAttribute(SharedMonsterAttributes.ATTACK_KNOCKBACK).setBaseValue(Calc.dim(par0.getAttribute(SharedMonsterAttributes.ATTACK_KNOCKBACK).getBaseValue(), par2, 5D));
	}, (par0, par1) -> par0.getAttribute(SharedMonsterAttributes.ATTACK_KNOCKBACK).getBaseValue());
	
	public static final IElement ATTACK_SPEED = new BasicElement("AttackSpeed", 0.25F, 4F, (par0, par1, par2) -> {
		par0.getAttribute(SharedMonsterAttributes.ATTACK_SPEED).setBaseValue(par0.getAttribute(SharedMonsterAttributes.ATTACK_SPEED).getBaseValue() + par2);
	}, (par0, par1) -> par0.getAttribute(SharedMonsterAttributes.ATTACK_SPEED).getBaseValue());
	
	public static final IDataElement EVASION_CHANCE = new PropertyElement("EvasionChance", 0F, 0.01F, 0.2F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, Calc.dim(var0.get(par0, var1), par2, 1D));
	});
	
	public static final IDataElement RANGED_DAMAGE = new PropertyElement("RangedDamage", 0F, 0.25F, 4F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, var0.get(par0, var1) + par2);
	});
	
	public static final IDataElement RANGED_CRIT_DAMAGE = new PropertyElement("RangedCritDamage", 0F, 0.05F, 0.5F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, Calc.dim(var0.get(par0, var1), par2, 10D));
	});
	
	public static final IDataElement RANGED_CRIT_CHANCE = new PropertyElement("RangedCritChance", 0F, 0.01F, 0.2F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, Calc.dim(var0.get(par0, var1), par2, 1D));
	});
	
	public static final IDataElement LIFESTEAL = new PropertyElement("Lifesteal", 0F, 0.01F, 0.2F, (par0, par1, par2) -> {
		IPlayerElements var0 = par1.one();
		IElement var1 = par1.two();
		
		var0.put(var1, Calc.dim(var0.get(par0, var1), par2, 10D));
	});
	
	public static final IElement LUCKINESS = new BasicElement("Luckiness", 1F, 10F, (par0, par1, par2) -> {
		par0.getAttribute(SharedMonsterAttributes.LUCK).setBaseValue(par0.getAttribute(SharedMonsterAttributes.LUCK).getBaseValue() + par2);
	}, (par0, par1) -> par0.getAttribute(SharedMonsterAttributes.LUCK).getBaseValue());
	
	/**
	 * Mod initialisation event.
	 * @param par0
	 */
	@SubscribeEvent
	public static void commonSetup(final FMLCommonSetupEvent par0) {
		CapabilityManager.INSTANCE.register(IPlayerElements.class, new Capability.IStorage<IPlayerElements>() {
			
			@Override
			public INBT writeNBT(Capability<IPlayerElements> par0, IPlayerElements par1, Direction par2) {
				return par1.write();
			}
			
			@Override
			public void readNBT(Capability<IPlayerElements> par0, IPlayerElements par1, Direction par2, INBT par3) {
				par1.read((CompoundNBT)par3);
			}
		}, PlayerElements::new);
		
		NETWORK.registerMessage(0, SyncPlayerElements.class, SyncPlayerElements::encode, SyncPlayerElements::decode, SyncPlayerElements::handle);
	}
}
