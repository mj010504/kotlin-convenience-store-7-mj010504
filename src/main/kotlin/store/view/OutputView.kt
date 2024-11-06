package store.view

import store.model.Inventory
import store.model.Product
import store.model.Promotion
import java.text.NumberFormat
import java.util.Locale

object OutputView {
    private const val WELCOME_MESSAGE = "안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n"
    private const val PRODUCT_INFO = "- %s %s원 %d개 %s"
    private const val PRODUCT_INFO_OUT_OUF_STOCK = "- %s %s원 재고 없음"

    fun printInventory(inventory: Inventory) {
        println(WELCOME_MESSAGE)
        inventory.products.forEach { product ->
            if(product.quntatiy != 0) println(PRODUCT_INFO.format(product.name, formatNumber(product.price), product.quntatiy, product.promotion?.name ?: ""))
            else println(PRODUCT_INFO_OUT_OUF_STOCK.format(product.name, formatNumber(product.price)))
        }
    }

    private fun formatNumber(number: Int): String {
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        return numberFormat.format(number)
    }
}