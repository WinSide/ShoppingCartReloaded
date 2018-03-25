package me.limito.bukkit.shopcart.request

import me.limito.bukkit.shopcart.ShoppingCartReloaded
import me.limito.bukkit.shopcart.gui.{CartInventory, CartInventoryNew}
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class RequestShowGui(commandSender: CommandSender) extends Request(commandSender) {
  override def prehandle() {
    requirePermission("cartr.user.gui")
  }

  def handle() {
    val playerName = commandSender.getName
    val itemsInfo = ShoppingCartReloaded.instance.dao.getItemInfos(playerName)

    withBukkit(() => {
      val player = commandSender.asInstanceOf[Player]
      if (classExists("org.bukkit.event.inventory.InventoryDragEvent")) {
        val gui = new CartInventoryNew(player, itemsInfo)
        gui.open()
      } else {
        val gui = new CartInventory(player, itemsInfo)
        gui.open()
      }
    })
  }

  private def classExists(className: String) = {
    try {
      Class.forName(className)
      true
    }
    catch {
      case e: ClassNotFoundException => false
    }
  }
}
