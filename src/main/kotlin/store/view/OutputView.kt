package store.view

import store.model.Inventory
import store.model.Product
import store.model.Promotion
import store.model.PromotionedPurchase
import java.text.NumberFormat
import java.util.Locale
import javax.swing.text.html.HTML.Tag.P

object OutputView {
    private const val WELCOME_MESSAGE = "안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n"
    private const val PRODUCT_INFO = "- %s %s원 %d개 %s"
    private const val PRODUCT_INFO_OUT_OUF_STOCK = "- %s %s원 재고 없음 %s"

    private const val RECEIPT_HEADER = "==============W 편의점================\n상품명   수량   금액"
    private const val PURCHASED_PRODUCT_FORMAT = "%s    %d   %s"
    private const val RECEIPT_PROMOTIEND_PROUCT = "=============증 정==============="
    private const val PROMOTIONED_PRODUCT_FORMAT = "%s    %d"
    private const val SEPARATOR_LINE = "===================================="
    private const val TOTAL_PURCHASE_AMOUNT = "총구매액   %d   %s"
    private const val PROMOTIONAL_DISCOUNT_AMOUNT_FORMAT = "행사할인         -%s"
    private const val MEMBERSHIP_DISCOUNT_AMOUNT_FORMAT = "멤버십할인      -%s"
    private const val FINAL_PURCHASE_AMOUNT_FORMAT = "내실돈        %s"
    private const val MEMBERSHIP_DISCOUNT_RATE = 0.3
    private const val MEMBERSHIP_DISCOUNT_LIMIT = 8000

    fun printInventory(inventory: Inventory) {
        println(WELCOME_MESSAGE)
        inventory.products.forEach { product ->
            if(product.quantity != 0) println(PRODUCT_INFO.format(product.name, formatNumber(product.price), product.quantity, product.promotion?.name ?: ""))
            else println(PRODUCT_INFO_OUT_OUF_STOCK.format(product.name, formatNumber(product.price), product.promotion?.name ?: ""))
        }
    }

    fun printReceipt(purchases : List<PromotionedPurchase>, totalPromotionedPrice : Int) {
        var totalPrice = 0;
        var totalQuantity = 0;
        var totalPromotionalDiscount = 0
        println(RECEIPT_HEADER)
        purchases.forEach { purchase ->
            val item = purchase.purchaseItem
            totalPrice += item.price * item.quantity
            totalQuantity += item.quantity
            totalPromotionalDiscount += purchase.promotionalPrice
           println(PURCHASED_PRODUCT_FORMAT.format(item.productName, item.quantity, formatNumber(item.price * item.quantity)))
        }
        printPromotiendProdcut(purchases)
        printPaymentSummary(totalPrice, totalQuantity, totalPromotionalDiscount, totalPromotionedPrice)
    }

    private fun printPromotiendProdcut(purchases: List<PromotionedPurchase>) {
        if(purchases.sumOf { it.bonusItemCount } > 0) println(RECEIPT_PROMOTIEND_PROUCT)
        purchases.forEach { purchase ->
            val item = purchase.purchaseItem
            if(purchase.bonusItemCount > 0 ) println(PROMOTIONED_PRODUCT_FORMAT.format(item.productName, purchase.bonusItemCount))
        }
    }

    private fun printPaymentSummary(totalPrice : Int, totalQuantity : Int, totalPromotionalDiscount : Int, totalPromotionedPrice: Int) {
        println(SEPARATOR_LINE)
        println(TOTAL_PURCHASE_AMOUNT.format(totalQuantity, formatNumber(totalPrice)))
        println(PROMOTIONAL_DISCOUNT_AMOUNT_FORMAT.format(formatNumber(totalPromotionalDiscount)))
        var memberShipDiscount = ((totalPrice - totalPromotionedPrice) * MEMBERSHIP_DISCOUNT_RATE).toInt()
        if(memberShipDiscount > MEMBERSHIP_DISCOUNT_LIMIT) memberShipDiscount = MEMBERSHIP_DISCOUNT_LIMIT
        println(MEMBERSHIP_DISCOUNT_AMOUNT_FORMAT.format(formatNumber(memberShipDiscount)))
        val finalPurchaseAmount = totalPrice - totalPromotionalDiscount - memberShipDiscount
        println(FINAL_PURCHASE_AMOUNT_FORMAT.format(formatNumber(finalPurchaseAmount)))
    }

    private fun formatNumber(number: Int): String {
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        return numberFormat.format(number)
    }
}