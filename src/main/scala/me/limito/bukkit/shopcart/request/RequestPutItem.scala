package me.limito.bukkit.shopcart.request

import org.bukkit.command.CommandSender
import org.bukkit.inventory.ItemStack
import me.limito.bukkit.shopcart.items.CartItemInfo
import org.bukkit.enchantments.Enchantment
import collection.JavaConversions._

class RequestPutItem(requestManager: RequestManager, commandSender: CommandSender, owner: String, itemStack: ItemStack, amount: Int) extends Request(requestManager, commandSender) {
  /** Здесь идет первичная обработка запроса (в игровом потоке) **/
  def handle() {
    requirePermission("cart.put")

    val info = createInfo(itemStack)
    withDatabase(() => {
      val id = dao.addItem(info)
      sendMessage(s"Item added (id: $id)")
    })
  }

  private def createInfo(itemStack: ItemStack):CartItemInfo = {
    val enchInfo = if(itemStack.getEnchantments.isEmpty) "" else "#" + createEnchantmentsInfo(itemStack.getEnchantments)
    val itemName = if(itemStack.getDurability == 0) itemStack.getTypeId.toString else itemStack.getTypeId.toString + ":" + itemStack.getDurability.toString

    new CartItemInfo(null, "item", itemName + enchInfo, owner, amount, null)
  }

  private def createEnchantmentsInfo(enchs: java.util.Map[Enchantment, Integer]):String = (for ((id, level) <- enchs) yield (id.getId + ":" + level)).mkString("#")
}