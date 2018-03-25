package me.limito.bukkit.shopcart.request

import me.limito.bukkit.shopcart.ShoppingCartReloaded
import me.limito.bukkit.shopcart.items.{CartItemInfo, ItemEncoder}
import org.bukkit.command.CommandSender
import org.bukkit.inventory.ItemStack

class RequestPutItem(commandSender: CommandSender, owner: String, itemStack: ItemStack, amount: Int) extends Request(commandSender) {
  private var info: CartItemInfo = _

  /** Здесь идет проверка условий для выполнения запроса (например, проверка наличия пермов) **/
  override def prehandle() {
    requirePermission("cartr.admin.put")

    info = ItemEncoder.createInfo(itemStack, owner, amount)
  }

  def handle() {
    val id = dao.addItem(info)
    sendMessage(ShoppingCartReloaded.instance.lang.format("cart-put.put", id))
  }


}
