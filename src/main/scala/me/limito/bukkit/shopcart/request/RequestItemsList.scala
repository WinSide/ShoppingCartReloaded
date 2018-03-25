package me.limito.bukkit.shopcart.request

import me.limito.bukkit.shopcart.ShoppingCartReloaded
import org.bukkit.command.CommandSender

class RequestItemsList(commandSender: CommandSender) extends Request(commandSender) {
  override def prehandle() {
    requirePermission("cartr.user.list")
  }

  def handle() {
    val playerName = commandSender.getName
    val itemsInfo = ShoppingCartReloaded.instance.dao.getItemInfos(playerName)
    val outLines = itemsInfo map (info => {
      val item = info.toItem;
      lang.format("cart.item", info.id, item.getLocalizedName(lang), info.amount)
    })
    val countMessage = lang.formatSubtype("cart.n-items", itemsInfo.size)

    withBukkit(() => {
      commandSender.sendMessage(countMessage)
      commandSender.sendMessage(outLines.toArray)
    })
  }
}
