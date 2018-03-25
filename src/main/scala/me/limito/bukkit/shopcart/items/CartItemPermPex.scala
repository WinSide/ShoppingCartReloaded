package me.limito.bukkit.shopcart.items

import me.limito.bukkit.shopcart.Lang.{TimeDuration, WorldName}
import me.limito.bukkit.shopcart.{Lang, ShoppingCartReloaded}
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import ru.tehkode.permissions.bukkit.PermissionsEx

class CartItemPermPex(permName: String, worldName: Option[String] = None, lifetime: Option[Long]) extends CartItem {
  val extras = (worldName.map(WorldName) ++ lifetime.map(TimeDuration)).toList

  override def getYouGetMessage(amount: Int, lang: Lang): String = lang.format("cart-get.get-perm", getLocalizedName(lang))

  override def getLocalizedName(lang: Lang): String = lang.formatSubtypeExtra("perm", permName, extras)

  override def giveToPlayer(player: Player, amount: Int): Int = {
    val worldNameOrNull = worldName.getOrElse(null)
    val user = PermissionsEx.getUser(player)

    /*if (user.has(permName, worldNameOrNull)) {
      val lang = ShoppingCartReloaded.instance.lang
      player.sendMessage(ShoppingCartReloaded.instance.lang.format("cart-get.already-has-perm", getLocalizedName(lang)))
      return 0
    }

    if (lifetime.isDefined)
      user.addTimedPermission(permName, worldNameOrNull, lifetime.get.toInt)
    else
      user.addPermission(permName, worldNameOrNull)*/
    1
  }

  override def giveToPlayer(player: Player): Int = giveToPlayer(player, 1)

  override def getIcon: ItemStack = new ItemStack(Material.BOOK)
}
