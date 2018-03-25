package me.limito.bukkit.shopcart.request

import java.util.logging.Level

import me.limito.bukkit.shopcart.ShoppingCartReloaded
import me.limito.bukkit.shopcart.items.{CartItem, CartItemInfo}
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

import scala.collection.mutable.ListBuffer

class RequestGiveAll(commandSender: CommandSender) extends Request(commandSender) {

  case class ItemGiveInfo(info: CartItemInfo, item: CartItem, amount: Int)

  override def prehandle() {
    requirePermission("cartr.user.get")

    if (!commandSender.isInstanceOf[Player]) {
      sendMessage(lang.get("cart.not-a-player"))
      return
    }
  }

  def handle() {
    val playerName = commandSender.getName
    val itemInfos = dao.getItemInfos(playerName)

    val updatedItemsBuffer = new ListBuffer[ItemGiveInfo]

    withBukkit(() => {
      itemInfos foreach (info => {
        val item = info.toItem
        val given = giveItem(item)
        info.amount -= given

        if (given > 0)
          updatedItemsBuffer += new ItemGiveInfo(info, item, given)
      })
    })

    val updatedItems = updatedItemsBuffer.toList

    dao.updateItems(updatedItemsBuffer.map(_.info).toList)

    if (!updatedItems.isEmpty)
      sendMessages(updatedItems.map(update => update.item.getYouGetMessage(update.amount, lang)))
    else
      sendMessage(lang.get("cart-get.get-nothing"))
  }

  private def giveItem(item: CartItem): Int = {
    try {
      item.giveToPlayer(commandSender.asInstanceOf[Player])
    } catch {
      case ex: Exception => ShoppingCartReloaded.instance.getLogger.log(Level.WARNING, "Error giving item", ex)
        0
    }
  }
}
