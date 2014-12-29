package com.comze_instancelabs.bedwars.villager;

import java.lang.reflect.Method;

import org.bukkit.entity.Player;

import com.comze_instancelabs.bedwars.villager.ReflectionUtils.NMSMerchantRecipe;
import com.comze_instancelabs.bedwars.villager.ReflectionUtils.NMSMerchantRecipeList;

public class NMSMerchant implements java.lang.reflect.InvocationHandler {
	private NMSMerchantRecipeList o = new NMSMerchantRecipeList(); // MerchantRecipeList
	private transient Object c; // EntityHuman
	public Object proxy;

	@Override
	public Object invoke(Object proxy, Method m, Object[] args) {
		try {
			if (m == null || m.getName() == null)
				return null;
			Class entityHuman = ReflectionUtils.getClassByName(ReflectionUtils.getNMSPackageName() + ".EntityHuman");
			if (m.getName().equals("a_") && args.length == 1 && args[0] != null && args[0].getClass().isInstance(entityHuman))
				this.a_(args[0]);
			else if (m.getName().equals("b") || m.getName().equals("m_") || m.getName().equals("u_")) // m_ = 1.6.4, b = 1.7.x, u_ = 1.8.x
				return this.b();
			else if (m.getName().equals("getOffers") && args.length == 1)
				return this.getOffers(args[0]);
			else if (m.getName().equals("a"))
				this.a(args[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void a_(Object player) { // Class = EntityHuman
		this.c = player;
	}

	public Object b() { // Return Class = EntityHuman
		return this.c;
	}

	public Object getOffers(Object player) { // Return Class = MerchantRecipeList, player Class = EntityHuman
		return this.o.getHandle();
	}

	public void a(Object recipe) { // recipe Class = MerchantRecipe
		this.o.add(new NMSMerchantRecipe(recipe));
	}

	/* Other Methods */
	public Player getBukkitEntity() {
		try {
			Class c = ReflectionUtils.getClassByName(ReflectionUtils.getNMSPackageName() + ".EntityHuman");
			Method m = c.getDeclaredMethod("getBukkitEntity");
			m.setAccessible(true);
			return (Player) m.invoke(this.c);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void clearRecipes() {
		this.o.clear();
	}

	public void setRecipes(NMSMerchantRecipeList recipes) {
		this.o = recipes;
	}

	public void openTrading(Object player, String title) { // player Class = EntityPlayer
		this.c = player;
		try {
			Class classs = ReflectionUtils.getClassByName(ReflectionUtils.getNMSPackageName() + ".EntityPlayer");
			Method m;
			if (this.getMethodArgs(classs, "openTrade") == 2) {
				// Older than Spigot 1.8
				m = classs.getDeclaredMethod("openTrade", ReflectionUtils.getClassByName(ReflectionUtils.getNMSPackageName() + ".IMerchant"), String.class);
				m.setAccessible(true);
				m.invoke(player, this.proxy, title);
			} else {
				// Spigot 1.8 and newer
				m = classs.getDeclaredMethod("openTrade", ReflectionUtils.getClassByName(ReflectionUtils.getNMSPackageName() + ".IMerchant"));
				m.setAccessible(true);
				m.invoke(player, this.proxy);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int getMethodArgs(Class classs, String methodName) {
		for (Method method : classs.getDeclaredMethods()) {
			if (method.getName().equals(methodName)) {
				return method.getParameterTypes().length;
			}
		}
		return -1;
	}

}
