package me.limito.bukkit.shopcart.items

import me.limito.bukkit.shopcart.ShoppingCartReloaded
import java.util.logging.Level
import me.limito.bukkit.shopcart.optional.nbt.NBTTag

class CartItemInfo(var id: Long,
                   var itemType: String,
                   var item: String,
                   var owner: String,
                   var amount: Int,
                   var extra: String,
                   var server: String = null) {
  def this() = this(0, "item", "0", "-", 1, null, null)

  override def toString = String.valueOf(id) + ": " + item

  def toItem: CartItem = {
    try {
      itemType match {
        case "item" => toMinecraftItem
        case "money" => toMoneyItem
        case "rgown" => toWGRegion(WGOwner)
        case "rgmem" => toWGRegion(WGMember)
        case "permgroup" => toPermGroup
        case _ => new CartItemUnknown()
      }
    } catch {
      case e: Exception =>
        ShoppingCartReloaded.instance.getLogger.log(Level.WARNING, "Error parsing cart item#" + id, e)
        new CartItemUnknown()
    }
  }

  private def toPermGroup = new CartItemPermGroup(item)
  private def toWGRegion(membershipType: WGMembershipType) = new CartItemWG(item, membershipType, amount)
  private def toMoneyItem: CartItemMoney = new CartItemMoney(amount)

  private def toMinecraftItem: CartItemItem = {
    val Array(main, enchants @ _*) = item.split("#", 2)
    val Array(id, meta @ _*) = main.split(":", 2)

    val ench = if(enchants.size > 0) parsePoundEnchantments(enchants(0)) else null
    new CartItemItem(id.toInt, if (meta.isEmpty) 0 else meta.head.toShort, amount, ench, parseNBT)
  }

  private def parseNBT: NBTTag = {
    if (extra == null)
      null
    else
      ShoppingCartReloaded.instance.nbtHelper.parseJson(extra)
  }

  private def parsePoundEnchantments(str: String): Array[LeveledEnchantment] = str.split("#").map (d => {
    val Array(id, level) = d.split(":")
    new LeveledEnchantment(id.toInt, level.toInt)
  })
}