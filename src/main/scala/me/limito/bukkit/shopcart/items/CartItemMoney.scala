package me.limito.bukkit.shopcart.items

import me.limito.bukkit.shopcart.{Lang, ShoppingCartReloaded}
import net.milkbowl.vault.economy.Economy
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.{Bukkit, Material}

class CartItemMoney(amount: Int) extends CartItem {
  override def getYouGetMessage(amount: Int, lang: Lang): String = lang.format("cart-get.get-money", amount)

  override def getLocalizedName(lang: Lang): String = lang.format("cart.money", amount)

  override def giveToPlayer(player: Player, amount: Int): Int = {
    val rsp = Bukkit.getServer.getServicesManager.getRegistration(classOf[Economy])
    if (rsp != null) {
      val econ = rsp.getProvider
      val response = econ.depositPlayer(player.getName, amount)
      if (response.transactionSuccess())
        return amount
    } else {
      player.sendMessage(ShoppingCartReloaded.instance.lang.get("cart-get.no-money"))
    }
    0
  }

  override def giveToPlayer(player: Player): Int = giveToPlayer(player, amount)

  override def getIcon: ItemStack = new ItemStack(Material.GOLD_INGOT)
}
